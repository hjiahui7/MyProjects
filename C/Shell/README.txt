	Students infomation:
==========================================
Jiahui Huang <hjiahui7>
Huanbo Fu <huanbof>


	How to execute the shell
==========================================
Get into directory /src
Run command 	"make"
Run command 	"./esh"
Run command 	"./esh -p plugins" to use plugins

To test plugins, 
	useing command "stdriver.py -p plugins plugins.tst"


	Important Notes
==========================================
We pass all the Basic tests and Advanced tests


	Description of Base Functionality
==========================================
jobs:
It will shwo the jobs which are running or stoped.
Esh will traverse the job list, showing all tasks

fg:
change the background job to foreground.
Esh will choose the newest job in background if fg command run without parameter
If the paramater is a jid, fg will bring the specific job with same jid.
shell will set the jobs static to foreground and wait it untill finished.

bg:
Move a stopped foreground job to background, and continue it.

kill:
Kill a job by sending kill signal. 
The job will catch the signal and remove from job list.
Restore shell control to user.

stop:
Stop a job by sending stop signal. 
The job will catch the signal.
Restore shell control to user.


^c:
Kill a job by sending kill signal. 
The job will catch the signal and remove from job list.
Restore shell control to user.

^z:
Stop a job by sending stop signal. 
The job will catch the signal.
Restore shell control to user.


	Description of Extended Functionality
=============================================
I/O:
It will redirection the stdin and stdout from keyboard/monitor to a file.
">>" should be followd by file name. the output will write in to the file.

pipes:
Use pipes to redirection the stdout of job1 to the stdin of job2

Exclusice Access:
Allows a FG process to have exclusive access
(such as vim).



	List of Plugins Implemented
==========================================
(Written by Our Team)
|--------------------------------------------------
|  Name		| Number changer
|--------------------------------------------------           
|  Description 	| change decimal to binary or hex, 
| 		| and result will show in the terminal.	
|--------------------------------------------------

|--------------------------------------------------
|  Name		| triangle area calculator
|--------------------------------------------------           
|  Description 	| Can calculate the area of triangle by using 3 ages or one height and width
|--------------------------------------------------

|--------------------------------------------------
|  Name		| monitorinfo
|--------------------------------------------------           
|  Description 	| It will calcuate the length width and Producer Price Index of the monitor
   	monitorinfo proportion diagonal Resolution(optional)
|--------------------------------------------------


(Written by Others)

|--------------------------------------------------
|  Name		| Party_Prompt
|--------------------------------------------------     
| Author    | Phillip Sullivan phills7
				Sammy Tieng sammy9
|--------------------------------------------------   
|  Description 	| Creates a custom prompt for the user, where it displays the user running the plugin 
					and the hostname. The party_prompt displays the prompt in a colorful way making it a
						party, and the colors change every time a command is made.
|--------------------------------------------------


|--------------------------------------------------
|  Name		| Help
|--------------------------------------------------     
| Author    | Phillip Sullivan phills7
			Sammy Tieng sammy9
|--------------------------------------------------   
|  Description 	| Allows the user to find out more information on the built in commands.
The user can see how to use the built commands, the functionality, and
the exit status of the command. The user can find information about all
the commands or a specific built command.
|--------------------------------------------------


|--------------------------------------------------
|  Name		| Exit
|--------------------------------------------------     
| Author    | Phillip Sullivan phills7
			Sammy Tieng sammy9
|--------------------------------------------------   
|  Description 	| Allows the shell to exit gracefully.  When the exit built in is called,
all processes created by the shell are sent SIGKILL and then esh returns
EXIT_SUCCESS
|--------------------------------------------------


|--------------------------------------------------
|  Name		| Thicc Prompt
|--------------------------------------------------     
| Author    | Christian Cabauatan chrisc98
				Brandon Manzaneda   branm14
|--------------------------------------------------   
|  Description 	| This plugin adds command line numbers to the esh shell as 
well as add the name of the user to the command line. The
color of the command line is also changed to red.
|--------------------------------------------------

|--------------------------------------------------
|  Name		| Task Manager
|--------------------------------------------------     
| Author    | Christian Cabauatan chrisc98
				Brandon Manzaneda   branm14
|--------------------------------------------------   
|  Description 	| Allows the user to create text based tasks for themselves in 
the shell. A list keeps track of created tasks and assigns a
task ID in relation to the order of which the task was added 
to the list. The user is able to remove a specific task by 
referencing the task's ID. The user is able to bring up all 
available tasks that have not yet been removed. 
|--------------------------------------------------
|--------------------------------------------------
|  Name		| recipe
|--------------------------------------------------     
| Author    | Hamza Monawer    hmonawer
				Ali Mohammadian  alim2
|--------------------------------------------------   
|  Description 	| Allows the user to quickly discover popular recipes,
accompanied by directions. This is a fun and entertaining
plug-in that encompasses the most joyous dishes that
a shell user would like. 
|--------------------------------------------------

|--------------------------------------------------
|  Name		| pushd
|--------------------------------------------------     
| Author    | Hamza Monawer    hmonawer
				Ali Mohammadian  alim2
|--------------------------------------------------   
|  Description 	| Allows the user to save frequently used directories,
while also navigating to that directory. Once a directory
is a saved, the directory can that be traveled to again
using the index number of that directory. Directories that 
are saved can also be displayed with this command.
|--------------------------------------------------

|--------------------------------------------------
|  Name		| change any 32 bits integer into binary
|--------------------------------------------------     
| Author    | Han Liu         <han3>
			  Xiaolin Zhou    <xiaolinz>
|--------------------------------------------------   
|  Description 	| You can use this plugin to change any 32 bits integer into binary. 
Both sign and unsign int. You can also personalize the numbr of digits you want your binary to be.
|--------------------------------------------------