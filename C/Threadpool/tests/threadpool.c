#include "threadpool.h"
#include "list.h"
#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <semaphore.h>
#include <pthread.h>



enum status  { 
    unstart,  
    running,    
    completed,
};

struct thread_pool
{

        struct list workers;
        struct list submissions;
        int sizeOfThread;
        pthread_mutex_t lock;
        sem_t semaphore;
        bool shutdown;
        
};


struct future
{
        struct thread_pool *pool;
        fork_join_task_t func;
        void* data;
        void* result;
        enum status curState;//lock
        pthread_mutex_t lock;
        sem_t semaphore;
        struct list_elem elem;
        struct worker* worker;
        struct worker* creator;
        int depth;
};

struct worker
{
        struct list futures;
        struct thread_pool * pool; 
        pthread_t thread; //...
        pthread_mutex_t lock;
        struct list_elem elem;
        int depth;
};
static void *startWork(void *w);
static __thread struct worker* internal = NULL;
// static __thread struct worker* gWorker = NULL;
static struct future* steal(struct thread_pool *curPool);
static void *futureGetHelper(struct future *curFuture);
static int max(int a, int b);

/* Create a new thread pool with no more than n threads. */
struct thread_pool * thread_pool_new(int nthreads)
{
     	// printf("Being here\n");
        // fflush(stdout);			
					
    struct thread_pool* newPool = calloc(1,sizeof(struct thread_pool));
    newPool->shutdown = false;
    newPool->sizeOfThread = nthreads;
    list_init(&newPool->workers);
    list_init(&newPool->submissions);
    pthread_mutex_init(&newPool->lock, NULL);
    sem_init(&newPool->semaphore, 0, 0);

//     struct worker *gWorker = calloc(1, sizeof(struct worker));
//     list_init(&gWorker->futures);
//     gWorker->pool = newPool;
//     internal = gWorker;
//     pthread_mutex_init(&gWorker->lock,NULL);
    pthread_mutex_lock(&newPool->lock);
    int i = 0;
    for(;i<nthreads;i++)
    {
        struct worker *w = calloc(1, sizeof(struct worker));
        w->pool = newPool;
        pthread_mutex_init(&w->lock,NULL);
        list_init(&w->futures);
        list_push_back(&newPool->workers,&w->elem);
        pthread_create(&w->thread, NULL, startWork, w);
    }
    pthread_mutex_unlock(&newPool->lock);
    return newPool;
}

static void *startWork(void *c)
{
          
        struct worker *curWork= c;
        struct thread_pool *curPool = curWork->pool;
        pthread_mutex_lock(&curPool->lock);
        pthread_mutex_unlock(&curPool->lock);
        internal = curWork;
        while (true) 
        {
                if(curPool -> shutdown) 
                {
                        break;
                }
                bool checkFind = false;
                struct future* thisFuture = NULL;
                
                if(!list_empty(&curWork->futures))
                {
                        if(!list_empty(&curWork->futures))
                        {
                                pthread_mutex_lock(&curWork->lock);
                                if(!list_empty(&curWork->futures))
                                {//current queue
                                        thisFuture =list_entry(list_pop_front(&curWork->futures), struct future, elem);
                                        pthread_mutex_lock(&thisFuture->lock);
                                        thisFuture->curState = running;
                                        thisFuture->worker = internal;
                                        // pthread_mutex_unlock(&thisFuture->lock);
                                        checkFind = true;
                                }
                                pthread_mutex_unlock(&curWork->lock);
                        }
                }
                if(!checkFind)
                {
                        if(!list_empty(&curPool->submissions))
                        {
                                pthread_mutex_lock(&curPool->lock);
                                if(!list_empty(&curPool->submissions))
                                {//submission 
                                        thisFuture = list_entry(list_pop_back(&curPool->submissions), struct future, elem);
                                        pthread_mutex_lock(&thisFuture->lock);
                                        thisFuture->curState = running;
                                        thisFuture->worker = internal;
                                        // pthread_mutex_unlock(&thisFuture->lock);
                                        checkFind = true;
                                }
                                pthread_mutex_unlock(&curPool->lock);
                        }
                }
                if(!checkFind)
                {//steal
                        thisFuture = steal(curPool);
                }
                
                if (thisFuture != NULL)
                {
                        pthread_mutex_lock(&curWork->lock);
                        int olddepth = curWork->depth;
                        curWork->depth = thisFuture->depth;
                        pthread_mutex_unlock(&curWork->lock);
                        pthread_mutex_unlock(&thisFuture->lock);
                        thisFuture->result = (thisFuture->func)(curPool, thisFuture->data);
                        //depth--;
                        pthread_mutex_lock(&curWork->lock);
                        curWork->depth = olddepth;
                        pthread_mutex_unlock(&curWork->lock);
                        pthread_mutex_lock(&thisFuture->lock);
                        thisFuture->curState = completed;
                        sem_post(&thisFuture->semaphore);
                        pthread_mutex_unlock(&thisFuture->lock);
                }
                if(thisFuture == NULL)
                {
                        sem_wait(&curPool->semaphore);
                }
        }       
        return NULL;
}

static struct future* steal(struct thread_pool *curPool)
{
        struct list_elem *e;
        for (e = list_begin (&curPool->workers); e != list_end (&curPool->workers); e = e->next)
        {
                struct worker* curWork = list_entry(e,  struct worker, elem);
                if(!list_empty(&curWork->futures))
                {
                        pthread_mutex_lock(&curWork->lock);
                        if(!list_empty(&curWork->futures))
                        {
                                struct future* curfuture = list_entry(list_pop_back(&curWork->futures), struct future, elem);
                                //double check
                                pthread_mutex_lock(&curfuture->lock);
                                curfuture->curState = running;
                                curfuture->worker = internal;
                                // pthread_mutex_unlock(&curfuture->lock);
                                pthread_mutex_unlock(&curWork->lock);
                                return curfuture;
                        }
                        pthread_mutex_unlock(&curWork->lock);
                }
        }
        return NULL;
}


/* 
 * Shutdown this thread pool in an orderly fashion.  
 * Tasks that have been submitted but not executed may or
 * may not be executed.
 *
 * Deallocate the thread pool object before returning. 
 */
void thread_pool_shutdown_and_destroy(struct thread_pool *curPool)
{
        // pthread_mutex_lock(&curPool->lock);
        curPool->shutdown = true;
        // pthread_mutex_unlock(&curPool->lock);
        int i = 0;
        for(; i < curPool->sizeOfThread;i++)
        {
                sem_post(&curPool->semaphore);
        }
        i = 0;
        for(; i < curPool->sizeOfThread;i++)
        {
                // pthread_mutex_lock(&curPool->lock);
                struct worker* curWork = list_entry(list_pop_front(&curPool->workers),  struct worker, elem);
                // pthread_mutex_unlock(&curPool->lock);
                pthread_join(curWork->thread, NULL);
                free(curWork);
        }

        pthread_mutex_destroy(&curPool->lock);
        sem_destroy(&curPool->semaphore);
        free(curPool);
       
}

/* 
 * Submit a fork join task to the thread pool and return a
 * future.  The returned future can be used in future_get()
 * to obtain the result.
 * 'pool' - the pool to which to submit
 * 'task' - the task to be submitted.
 * 'data' - data to be passed to the task's function
 *
 * Returns a future representing this computation.
 */
struct future * thread_pool_submit(struct thread_pool *pool, fork_join_task_t task, void * data)
{
        
        struct future *thisfuture = calloc(1, sizeof(struct future));
        thisfuture->func = task;
        thisfuture->data = data;
        thisfuture->pool = pool;
        thisfuture->curState = unstart;
        thisfuture->worker = NULL;
        pthread_mutex_init(&thisfuture->lock, NULL);
        sem_init(&thisfuture->semaphore, 0, 0);
        if(internal != NULL)
        {
                thisfuture->creator = internal;
                thisfuture->depth = internal->depth + 1;
                pthread_mutex_lock(&internal->lock);
                list_push_front(&internal->futures, &thisfuture->elem);
                pthread_mutex_unlock(&internal->lock);
        }
        else
        {
                thisfuture->depth = 0;
                thisfuture->creator = NULL;
                pthread_mutex_lock(&pool->lock);
                list_push_back(&pool->submissions, &thisfuture->elem);
                pthread_mutex_unlock(&pool->lock);
        }
        sem_post(&pool->semaphore);
        
        return thisfuture;
}

/* Make sure that the thread pool has completed the execution
 * of the fork join task this future represents.
 *
 * Returns the value returned by this task.
 */
void * future_get(struct future *curFuture)
{
        if(internal == NULL)
        {       
                sem_wait(&curFuture->semaphore);
                return curFuture->result;
        }
        struct future *thisFuture = futureGetHelper(curFuture);
        return thisFuture->result;
}


static void *futureGetHelper(struct future *curFuture)
{
        pthread_mutex_lock(&curFuture->creator->lock);
        // pthtgread_mutex_lock(&internal->lock);
        pthread_mutex_lock(&curFuture->lock);
        
        if(curFuture->curState == running)
        {
                //max(1,2);
                while(curFuture->curState != completed)
                {
						bool hutou = false;
					
						pthread_mutex_unlock(&curFuture->lock);
						pthread_mutex_lock(&curFuture->lock);
						if(curFuture->curState == completed || list_empty(&curFuture->worker->futures)) { pthread_mutex_unlock(&curFuture->lock); break;}
                        printf("I am here while loop! the size is: %zd\n", list_size(&curFuture->worker->futures));
                        if(!list_empty(&curFuture->worker->futures))
                        {
                                pthread_mutex_lock(&curFuture->worker->lock);
                                struct list_elem *e;
                                for (e = list_begin (&curFuture->worker->futures); e != list_end (&curFuture->worker->futures); e = e->next)
                                {

                                        struct future* candidate_Future = list_entry(e, struct future, elem);
                                        pthread_mutex_lock(&candidate_Future->lock);
                                        int mindepth = max(curFuture->depth, internal->depth);
                                        // printf("curFuture->depth is: %d\n", curFuture->depth);
                                        // printf("internal->depth is: %d\n", internal->depth);
                                        // printf("candidate_Future->depth is: %d\n", candidate_Future->depth);
                                        if (candidate_Future->creator == curFuture->worker && candidate_Future->depth > mindepth)
                                        {
											
												if (internal == curFuture->creator)
												{
													//stealing & leapfrogging between 2, common case
													hutou = true;
													pthread_mutex_unlock(&candidate_Future->lock);
													break;
												}
											
                                                //printf("helping\n");
                                                candidate_Future->curState = running;
                                                candidate_Future->worker = internal;
                                                list_remove(&candidate_Future->elem);
                                                int olddepth = internal->depth;
                                                internal->depth = candidate_Future->depth;

                                                pthread_mutex_unlock(&curFuture->worker->lock);
                                                pthread_mutex_unlock(&candidate_Future->lock);
                                                pthread_mutex_unlock(&curFuture->creator->lock);
                                                pthread_mutex_unlock(&curFuture->lock);
                                                
                                                candidate_Future->result = (candidate_Future->func)(candidate_Future->pool, candidate_Future->data);
                                                pthread_mutex_lock(&candidate_Future->lock);
                                                candidate_Future->curState = completed;
                                                sem_post(&candidate_Future->semaphore);
                                                pthread_mutex_unlock(&candidate_Future->lock);
                                                // pthread_mutex_lock(&internal->lock);
                                                internal->depth = olddepth;
												
												if (!hutou)
												{
													pthread_mutex_lock(&curFuture->creator->lock);
													pthread_mutex_lock(&curFuture->lock);
													pthread_mutex_lock(&curFuture->worker->lock);
												}
												else
												{
													pthread_mutex_lock(&curFuture->worker->lock);
													pthread_mutex_lock(&curFuture->lock);
													pthread_mutex_lock(&curFuture->creator->lock);
												}
                                                break;
                                        }
                                        pthread_mutex_unlock(&candidate_Future->lock);
                                }
                                pthread_mutex_unlock(&curFuture->worker->lock);
								
								if (hutou == true)
												{
													pthread_mutex_unlock(&curFuture->lock);
													break;
												}
                        }
                        
                        
                }
                
                pthread_mutex_unlock(&curFuture->creator->lock);
                pthread_mutex_unlock(&curFuture->lock);
                // pthread_mutex_unlock(&internal->lock);
                // pthread_mutex_unlock(&curFuture->creator->lock);
                
                 sem_wait(&curFuture->semaphore);
        }
        else if(curFuture->curState == completed)
        {
                
                pthread_mutex_unlock(&curFuture->lock);
                // pthread_mutex_unlock(&internal->lock);
                pthread_mutex_unlock(&curFuture->creator->lock);
                
        }
        else
        {   
                curFuture->curState = running;
                list_remove(&curFuture->elem);
                curFuture->worker = internal;
                int olddepth = internal->depth;
                internal->depth = curFuture->depth;
                curFuture->depth = max(curFuture->depth, internal ->depth + 1);
                pthread_mutex_unlock(&curFuture->lock);
                // pthread_mutex_unlock(&internal->lock);
                pthread_mutex_unlock(&curFuture->creator->lock);
                curFuture->result = (curFuture->func)(curFuture->pool, curFuture->data);
                pthread_mutex_lock(&curFuture->lock);
                curFuture->curState = completed;
                sem_post(&curFuture->semaphore);
                pthread_mutex_unlock(&curFuture->lock);
                // pthread_mutex_lock(&internal->lock);
                internal->depth = olddepth;
                // pthread_mutex_unlock(&internal->lock);
        }
        return curFuture;
}


/* Deallocate this future.  Must be called after future_get() */
void future_free(struct future *curFuture)
{
    sem_destroy(&curFuture->semaphore);
    free(curFuture);
}


static int max(int a, int b) {return a>b?a:b;}