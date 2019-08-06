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
        int workId;
        struct worker* creator;
        int depth;
};

struct worker
{
        struct list futures;
        struct thread_pool * pool; 
        pthread_t thread; //...
        pthread_mutex_t lock;
        int threadId;
        struct list_elem elem;
        int workId;
        int depth;
};
static void *startWork(void *w);
static __thread struct worker* internal = NULL;

/* Create a new thread pool with no more than n threads. */
struct thread_pool * thread_pool_new(int nthreads)
{
    struct thread_pool* newPool = calloc(1,sizeof(struct thread_pool));
    newPool->shutdown = false;
    newPool->sizeOfThread = nthreads;
    list_init(&newPool->workers);
    list_init(&newPool->submissions);
    pthread_mutex_init(&newPool->lock, NULL);
    sem_init(&newPool->semaphore, 0, 0);
    int i = 0;
    pthread_mutex_lock(&newPool->lock);
    for(;i<nthreads;i++)
    {
        struct worker *w = calloc(1, sizeof(struct worker));
        w->pool = newPool;
        w->workId = i;
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
        internal = curWork;
        //struct worker *thisWorker =curWork;
        pthread_mutex_lock(&curPool->lock);
        pthread_mutex_unlock(&curPool->lock);
        while (true) 
        {
                if(curPool -> shutdown) 
                {
                        break;
                }
                bool checkFind = false;
                struct future* thisFuture = NULL;
                pthread_mutex_lock(&curPool->lock);
                if( !checkFind && !list_empty(&curPool->submissions))
                {//submission 
                        thisFuture = list_entry(list_pop_back(&curPool->submissions), struct future, elem);
                        checkFind = true;
                }
                pthread_mutex_unlock(&curPool->lock);
                if (thisFuture != NULL)
                {
                        thisFuture->result = (thisFuture->func)(curPool, thisFuture->data);
                        pthread_mutex_lock(&curWork->lock);
                        sem_post(&thisFuture->semaphore);
                        thisFuture->curState = completed;
                        pthread_mutex_unlock(&curWork->lock);
                }
                if(thisFuture == NULL)
                {
                        sem_wait(&curPool->semaphore);
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
        
        curPool->shutdown = true;
        int i = 0;
        for(; i < curPool->sizeOfThread + 4;i++)
        {
                sem_post(&curPool->semaphore);
        }
        i = 0;
        for(; i < curPool->sizeOfThread;i++)
        {
                pthread_mutex_lock(&curPool->lock);
                struct worker* curWork = list_entry(list_pop_front(&curPool->workers),  struct worker, elem);
                pthread_mutex_unlock(&curPool->lock);
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
        thisfuture->creator = NULL;
        thisfuture->workId = -1;
        pthread_mutex_init(&thisfuture->lock, NULL);
        sem_init(&thisfuture->semaphore, 0, 0);
        if(internal != NULL)
        {
                struct worker* curWork = internal;
                thisfuture->creator = curWork;
                thisfuture->workId = curWork->workId;
                pthread_mutex_lock(&curWork->lock);
                list_push_front(&curWork->futures, &thisfuture->elem);
                pthread_mutex_unlock(&curWork->lock);
        }
        else
        {
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
        pthread_mutex_lock(&curFuture->creator->lock);
        if(curFuture->curState == running)
        {
                pthread_mutex_unlock(&curFuture->creator->lock);
                sem_wait(&curFuture->semaphore);
        }
        else if(curFuture->curState == completed)
        {
                pthread_mutex_unlock(&curFuture->creator->lock);
        }
        else
        {   
                curFuture->curState = running;
                list_remove(&curFuture->elem);
                pthread_mutex_unlock(&curFuture->creator->lock);
                curFuture->result = (curFuture->func)(curFuture->pool, curFuture->data);
                pthread_mutex_lock(&curFuture->creator->lock);
                sem_post(&curFuture->semaphore);
                curFuture->curState = completed;
                pthread_mutex_unlock(&curFuture->creator->lock);
        }
        return curFuture->result;
}



/* Deallocate this future.  Must be called after future_get() */
void future_free(struct future *curFuture)
{
    sem_destroy(&curFuture->semaphore);
    free(curFuture);
}


