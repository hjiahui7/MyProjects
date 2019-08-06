/*
* esh - the 'pluggable' shell.
*
* Developed by Godmar Back for CS 3214 Fall 2009
* Virginia Tech.
*/
#include <stdio.h>
#include <readline/readline.h>
#include <unistd.h>
#include "esh.h"
#include "esh-sys-utils.h"
#include <string.h>
#include <sys/wait.h>
#include <assert.h>
#include <signal.h>
#include <sys/types.h>
#include <fcntl.h>

//field
struct list jobList;
struct termios *shell_state;
int jobId = 0;
pid_t stateId;
static void give_terminal_to(pid_t pgrp, struct termios *pg_tty_state);
static void usage(char *progname);
static char *build_prompt_from_plugins(void);
static void sigchld_handler(int sig, siginfo_t *info, void *_ctxt);
static void wait_for_job(struct esh_pipeline *pipeline);
static struct esh_pipeline *get_job_from_pgrp(pid_t pgrp);
static struct esh_pipeline* findPipeByPid(int id);
static struct esh_command* getCommandByPid(pid_t thisPid);
static struct list* get_jobs(void);
/**
* Assign ownership of ther terminal to process group
* pgrp, restoring its terminal state if provided.
*
* Before printing a new prompt, the shell should
* invoke this function with its own process group
* id (obtained on startup via getpgrp()) and a
* sane terminal state (obtained on startup via
* esh_sys_tty_init()).
*/
static void give_terminal_to(pid_t pgrp, struct termios *pg_tty_state)
{
	esh_signal_block(SIGTTOU);
	int rc = tcsetpgrp(esh_sys_tty_getfd(), pgrp);
	if (rc == -1)
		esh_sys_fatal_error("tcsetpgrp: ");

	if (pg_tty_state)
		esh_sys_tty_restore(pg_tty_state);
	esh_signal_unblock(SIGTTOU);
}

//show the command for user. 
static void usage(char *progname)
{
	printf("Usage: %s -h\n"
		" -h            print this help\n"
		" -p  plugindir directory from which to load plug-ins\n",
		progname);

	exit(EXIT_SUCCESS);
}

/* Build a prompt by assembling fragments from loaded plugins that
* implement 'make_prompt.'
*
* This function demonstrates how to iterate over all loaded plugins.
*/
static char *
build_prompt_from_plugins(void)
{
	char *prompt = NULL;
	struct list_elem * e = list_begin(&esh_plugin_list);

	for (; e != list_end(&esh_plugin_list); e = list_next(e)) {
		struct esh_plugin *plugin = list_entry(e, struct esh_plugin, elem);

		if (plugin->make_prompt == NULL)
			continue;

		/* append prompt fragment created by plug-in */
		char * p = plugin->make_prompt();
		if (prompt == NULL) {
			prompt = p;
		}
		else {
			prompt = realloc(prompt, strlen(prompt) + strlen(p) + 1);
			strcat(prompt, p);
			free(p);	
		}
	}

	/* default prompt */
	if (prompt == NULL)
		prompt = strdup("esh> ");

	return prompt;
}

/* The shell object plugins use.
* Some methods are set to defaults.
*/
struct esh_shell shell =
{
	.get_cmd_from_pid = getCommandByPid,
	.get_job_from_pgrp = get_job_from_pgrp,
	.get_job_from_jid = findPipeByPid,
	.get_jobs = get_jobs,
	.build_prompt = build_prompt_from_plugins,
	.readline = readline,       /* GNU readline(3) */
	.parse_command_line = esh_parse_command_line /* Default parser */
};


// main function for shell.
int main(int ac, char *av[])
{
	int opt;
	list_init(&esh_plugin_list);
	list_init(&jobList);
	/* Process command-line arguments. See getopt(3) */
	while ((opt = getopt(ac, av, "hp:")) > 0) {
		switch (opt) {
		case 'h':
			usage(av[0]);
			break;

		case 'p':
			esh_plugin_load_from_directory(optarg);
			break;
		}
	}
	esh_signal_sethandler(SIGCHLD, sigchld_handler);
	setpgid(0, 0);
	shell_state = esh_sys_tty_init();
	esh_plugin_initialize(&shell);
	stateId = getpgrp();
	/* Read/eval loop. */
	for (;;) {
		
		if (list_empty(&jobList)) jobId = 0;
		clearMark(0);
		give_terminal_to(stateId, shell_state);
		/* Do not output a prompt unless shell's stdin is a terminal */

		char * prompt = isatty(0) ? shell.build_prompt() : NULL;
		char * cmdline = shell.readline(prompt);
		free(prompt);

		/* User typed EOF */
		if (cmdline == NULL)  
			break;

		bool raw_cmdline_check = false;
		struct list_elem * e = list_begin(&esh_plugin_list);
		for (; e != list_end(&esh_plugin_list); e = list_next(e)) {
			struct esh_plugin *plugin = list_entry(e, struct esh_plugin, elem);
			if (plugin->process_raw_cmdline) {
			 	raw_cmdline_check = plugin->process_raw_cmdline(&cmdline);
				if (raw_cmdline_check) {
					break;
				}
			}
		}
		if(raw_cmdline_check)
		{
			continue;
		}
		struct esh_command_line * cline = shell.parse_command_line(cmdline);
		
		free(cmdline);
		if (cline == NULL)                  /* Error in command line */
			continue;

		if (list_empty(&cline->pipes)) {    /* User hit enter */
			esh_command_line_free(cline);
			continue;
		}
		eval(cline);
		esh_command_line_free(cline);
	}
	return 0;
}

void eval(struct esh_command_line * cline)
{
	struct list *thispipe = &cline->pipes;
	
	while (!list_empty(thispipe))
	{
		//bool firstTime = true;
		
		esh_signal_block(SIGCHLD);
		bool hasChild = false;
		pid_t childPid;
		struct list_elem *pipeline = list_pop_front(thispipe);
		struct esh_pipeline *pipeNode = list_entry(pipeline, struct esh_pipeline, elem);
		struct list_elem *comNode;

		bool process_pipeline_check = false;
		struct list_elem * e = list_begin(&esh_plugin_list);
		for (; e != list_end(&esh_plugin_list); e = list_next(e)) {
			struct esh_plugin *plugin = list_entry(e, struct esh_plugin, elem);
			if (plugin->process_pipeline) {
			 	process_pipeline_check = plugin->process_pipeline(pipeNode);
				if (process_pipeline_check) {
					break;
				}
			}
		}

		int forepipe[2];
		int backpipe[2];
		// pid_t firstPgid;
		int count = 0;
		jobId++;
		pipeNode->jid = jobId;
		pipeNode->pgrp = -1;

		//bool first;
		for (comNode = list_begin(&pipeNode->commands); comNode != list_end(&pipeNode->commands); comNode = list_next(comNode))
		{
			struct esh_command *com = list_entry(comNode, struct esh_command, elem);
			bool checkPlug= false;
			struct list_elem * e = list_begin(&esh_plugin_list);
			for (; e != list_end(&esh_plugin_list); e = list_next(e)) {
				struct esh_plugin *plugin = list_entry(e, struct esh_plugin, elem);
				if (plugin->process_builtin) {
					checkPlug = plugin->process_builtin(com);
					if (checkPlug) {
						break;
					}
				}
			}
			if(checkPlug)
			{
				list_remove(comNode);
				continue;
			}
			fflush(stdout);
			if (check_isBuildin(com))
			{
				doBuildin(com);
				list_remove(comNode); 
			}
			else
			{
				if(list_size(&pipeNode->commands) > 1)
				{
					// printf("I am here 196\n");
					if(list_next(comNode)!= list_tail(&pipeNode->commands))
					{
						pipe(backpipe);
					}
				}
				hasChild = true;
				childPid = fork();
				
				if (childPid == 0)
				{
					if(list_size(&pipeNode->commands) > 1)
					{
						if(comNode != list_begin(&pipeNode->commands) )
						{
							close(forepipe[1]); 
							dup2(forepipe[0], 0);
							close(forepipe[0]);
						}
						if(list_next(comNode)!= list_tail(&pipeNode->commands))
						{
							close(backpipe[0]);
							dup2(backpipe[1], 1);
							close(backpipe[1]);
						}
					}
					if (com->iored_input != NULL)
					{
						int in = open(com->iored_input, O_RDONLY);
						dup2(in, 0);
						close(in);
					}			
					if(com->iored_output != NULL)
					{
						if(com->append_to_output)		
						{						
							int out = open(com->iored_output, O_WRONLY | O_APPEND | O_CREAT, S_IRUSR  | S_IWUSR );
							dup2(out, 1);
							close(out);
						}
						else
						{
							int out = open(com->iored_output, O_WRONLY | O_TRUNC | O_CREAT, S_IRUSR  | S_IWUSR);
							dup2(out, 1);
							close(out);
						}
					}
					if(count == 0)
					{
						pipeNode->pgrp = getpid();
					}
					setpgid(0, pipeNode->pgrp);
					com->pid = getpid();
					
					if (execvp(com->argv[0], com->argv) == -1)
					{
						exit(0);
					}
				}
				else if (childPid < 0)
				{
					perror("fork error");
					exit(0);
				}
				else
				{
					
					if(list_size(&pipeNode->commands) > 1)
					{
						//if(list_next(comNode) != list_begin(&pipeNode->commands))
						if(comNode != list_begin(&pipeNode->commands))
						{
							//printf("I am in 296\n");
							close(forepipe[0]);
							close(forepipe[1]);
						}
						if(list_next(comNode)!= list_tail(&pipeNode->commands))
						{
							//printf("I am in 302\n");
							forepipe[0] = backpipe[0];
							forepipe[1] = backpipe[1];
						}
					}
					if(count == 0)
					{
						pipeNode->pgrp = childPid;
					}
					setpgid(childPid, pipeNode->pgrp);
					com->pid = childPid;
					struct list_elem * e = list_begin(&esh_plugin_list);
					for (; e != list_end(&esh_plugin_list); e = list_next(e)) {
						struct esh_plugin *plugin = list_entry(e, struct esh_plugin, elem);
						if (plugin->pipeline_forked) {
							plugin->pipeline_forked(pipeNode);
						}
					}
				}
			}
			count++;
		}
		// give terminal once to the pgrp
		if (hasChild && !pipeNode->bg_job)
		{
			give_terminal_to(pipeNode->pgrp, NULL);
		}
		list_remove(pipeline);
		if (hasChild)
		{
			list_push_front(&jobList, pipeline);
			printf("[%d] %d\n", pipeNode->jid, pipeNode->pgrp); 
			if (pipeNode->bg_job)
			{
				pipeNode->status = BACKGROUND;
			}
			else
			{
				pipeNode->status = FOREGROUND;	
				wait_for_job(pipeNode);			
				give_terminal_to(stateId, shell_state);
			}

		}
		esh_signal_unblock(SIGCHLD); 
	}
}

// check if the commend is a build in commend
bool check_isBuildin(struct esh_command *com)
{
	char* comLine = com->argv[0];
	if (strcmp(comLine, "jobs") == 0) return true;
	if (strcmp(comLine, "fg") == 0) return true;
	if (strcmp(comLine, "bg") == 0) return true;
	if (strcmp(comLine, "kill") == 0) return true;
	if (strcmp(comLine, "stop") == 0) return true;
	return false;
}

//run the functions for differnet commend
void doBuildin(struct esh_command *com)
{
	if(com != NULL)
	{
		char *comLine = com->argv[0];
		if (strcmp(comLine, "jobs") == 0) doJobs(0);
		if (com->argv[1] != NULL)
		{
			if (strcmp(comLine, "fg") == 0)  doFg(atoi(com->argv[1]));
			else if (strcmp(comLine, "bg") == 0)  doBg(atoi(com->argv[1]));
			else if (strcmp(comLine, "kill") == 0) doKill(atoi(com->argv[1]));
			else if (strcmp(comLine, "stop") == 0) doStop(atoi(com->argv[1]));
		}
	}
	else
	{
		printf("Null input command");
	}
}
/*
we get this in web, the most important thing in our program,
which will get the signal and receive the child process, then do the child_status_change
*/
static void sigchld_handler(int sig, siginfo_t *info, void *_ctxt)
{
	pid_t child;
	int status;
	assert(sig == SIGCHLD);
	 while ((child = waitpid(-1, &status, WUNTRACED|WNOHANG)) > 0) {
        child_status_change(child, status);
    }
	clearMark(0);
}

/*wait teh child of specific job, we get this in web
*/
static void wait_for_job(struct esh_pipeline *pipeline)
{
	while (pipeline->status == FOREGROUND && !list_empty(&pipeline->commands)) {
		int status;
		pid_t child = waitpid(-1, &status, WUNTRACED);
		if (child != -1)
			child_status_change(child, status);
		
	}
}

/*
child_status_change will change our program status by using WIFEXITED, WTERMSIG, WIFSTOPPED or WTERMSIG
*/
void child_status_change(pid_t child, int status)
{
	struct esh_command* curCommand = getCommandByPid(child);
	bool command_status_change_check = false;
	struct list_elem * e = list_begin(&esh_plugin_list);
	for (; e != list_end(&esh_plugin_list); e = list_next(e)) {
		struct esh_plugin *plugin = list_entry(e, struct esh_plugin, elem);
		if (plugin->command_status_change) {
			command_status_change_check = plugin->command_status_change(curCommand,status);
			if (command_status_change_check) {
				break;
			}
		}
	}
	struct esh_pipeline* curPipeline = curCommand->pipeline;
	if (WIFEXITED(status) || SIGINT == WTERMSIG(status)||SIGKILL== WTERMSIG(status))
	{         
		if(list_size(&curPipeline->commands)==1)
		{
			curPipeline->status = STOPPED;
		}
		curPipeline->complete = true;
		list_remove(&curCommand->elem);	
		esh_command_free(curCommand);
		give_terminal_to(stateId, shell_state);
		
	}
	else if (WIFSTOPPED(status))//WTFSTOPPED
	{
		curPipeline->waiting = true;
		if(curPipeline->status != STOPPED)
		{
			printf("[%d] Stopped (", curPipeline->jid);
			printJobName(curPipeline);
			printf(")\n");
		}
		curPipeline->status = STOPPED;
		esh_sys_tty_save(&curPipeline->saved_tty_state);
		give_terminal_to(stateId, shell_state);
	}
}


//It will shwo the jobs which are running or stoped.
//Esh will traverse the job list, showing all tasks
void doJobs(int a)
{
	esh_signal_block(SIGCHLD);
	struct list_elem * thisJob;
	for (thisJob = list_begin(&jobList); thisJob != list_end(&jobList); thisJob = list_next(thisJob))
	{
		struct esh_pipeline *j = list_entry(thisJob, struct esh_pipeline, elem);
		printf("[%d] ", j->jid);
		if (j->status == FOREGROUND)
		{
			printf("Running ");
		}
		else if (j->status == STOPPED)
		{
			printf("Stopped ");
		}
		else if (j->status == BACKGROUND)
		{
			printf("Running ");
		}
		else if (j->status == NEEDSTERMINAL)
		{
			printf("Stopped ");
		}
		printf("(");
		printJobName(j);
		printf(")");
		printf("\n");
	}
	esh_signal_unblock(SIGCHLD);
}

//the help function for jobs,print all;

void printJobName(struct esh_pipeline *j)
{
	struct list_elem *cmd = list_begin(&j->commands);
	for (; cmd != list_end(&j->commands); cmd = list_next(cmd))
	{
		struct esh_command *curLine = list_entry(cmd, struct esh_command, elem);
		char** curl = curLine->argv;
		int i = 0;
		while (curl[i]) {
			printf("%s", curl[i]);
			i++;
			if (curl[i + 1] == NULL)
			{
				printf(" ");
			}
		}
		if (list_next(cmd) != list_end(&j->commands)) {
			printf(" | ");
		}
	}
}
//the function for bfgg, move the job to foreground wait until finish
//Jid: the job id we need process.
void doFg(int jId)
{
	struct esh_pipeline *thisPipe = findPipeByPid(jId);
	printJobName(thisPipe);
	printf("\n");
	if (thisPipe == NULL)
	{
		return;
	}
	kill(-thisPipe->pgrp, SIGCONT);
	thisPipe->status = FOREGROUND;
	give_terminal_to(thisPipe->pgrp, NULL);
	wait_for_job(thisPipe);
}
/*the function for bg, move the job to background and continue
Jid: the job id we need process.
Move a stopped foreground job to background, and continue it.
*/
void doBg(int jId)
{
	struct esh_pipeline *thisPipe = findPipeByPid(jId);
	printJobName(thisPipe);
	printf("\n");
	if (thisPipe == NULL)
	{
		printf("Did not find job by id");
		return;
	}
	kill(-thisPipe->pgrp, SIGCONT);
	thisPipe->status = BACKGROUND;
}
/*the function for kill, pass the signal SIGKILL to process
Jid: the job id we need process.
Kill a job by sending kill signal. 
The job will catch the signal and remove from job list.
Restore shell control to user.
*/
void doKill(int jId)
{
	struct esh_pipeline *thisPipe = findPipeByPid(jId);
	if (thisPipe == NULL)
	{
		printf("Do kill command not find job");
		return;
	}
	kill(-thisPipe->pgrp, SIGKILL);
}

/*the function for stop, pass the signal SIGTSTP to process
Jid: the job id we need process.
Stop a job by sending stop signal. 
The job will catch the signal.
Restore shell control to user.
*/
void doStop(int jId)
{
	struct esh_pipeline *thisPipe = findPipeByPid(jId);
	if (thisPipe == NULL)
	{
		printf("Do stop command not find job");
		return;
	}
	thisPipe->status = STOPPED;
	thisPipe->waiting = true;
	esh_sys_tty_save(&thisPipe->saved_tty_state);
	kill(-thisPipe->pgrp, SIGTSTP);
}
//return the pipeline with specific pid
static struct esh_pipeline* findPipeByPid(int id)
{
	struct list_elem * thisJob;
	for (thisJob = list_begin(&jobList); thisJob != list_end(&jobList); thisJob = list_next(thisJob))
	{
		struct esh_pipeline *pipe = list_entry(thisJob, struct esh_pipeline, elem);
		if (pipe->jid == id)
		{
			return pipe;
		}
	}
	return NULL;
}
// return the command by pid
static struct esh_command* getCommandByPid(pid_t thisPid)
{
	struct list_elem * thisJob;
	for (thisJob = list_begin(&jobList); thisJob != list_end(&jobList); thisJob = list_next(thisJob))
	{
		struct esh_pipeline *pipe = list_entry(thisJob, struct esh_pipeline, elem);
		struct list_elem * comd;
		for (comd = list_begin(&pipe->commands); comd != list_end(&pipe->commands); comd = list_next(comd))
		{
			struct esh_command *thisComd = list_entry(comd, struct esh_command, elem);
			if (thisComd->pid == thisPid)
			{
				return  thisComd;
			}
		}
	}
	return NULL;
}

//clean the extra jobs and pipelines 
void clearMark(int a)
{
	esh_signal_block(SIGCHLD);
	struct list_elem * thisJob;
	for (thisJob = list_begin(&jobList); thisJob != list_end(&jobList); thisJob = list_next(thisJob))
	{
		struct esh_pipeline *pipe = list_entry(thisJob, struct esh_pipeline, elem);
		if (pipe->complete)
		{
			if (list_empty(&pipe->commands))
			{
				list_remove(thisJob);
				esh_pipeline_free(pipe);
				//pipeҪ�ǵ�free
			}
		}
	}
	esh_signal_unblock(SIGCHLD);
}
//return the current jobs list
static struct list* get_jobs(void)
{
	return &jobList;
}

/* Return job corresponding to pgrp */
static struct esh_pipeline *get_job_from_pgrp(pid_t pgrp)
{
	struct list_elem * thisJob;
	for (thisJob = list_begin(&jobList); thisJob != list_end(&jobList); thisJob = list_next(thisJob))
	{
		struct esh_pipeline *pipe = list_entry(thisJob, struct esh_pipeline, elem);
		if (pipe->pgrp == pgrp)
		{
			return pipe;
		}
	}
	return NULL;
}
