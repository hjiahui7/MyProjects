/*
 * Skeleton files for personal server assignment.
 *
 * @author Godmar Back
 * written for CS3214, Spring 2018.
 */

#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>
#include "buffer.h"
#include "hexdump.h"
#include "http.h"
#include "socket.h"
#include "bufio.h"
#include "globals.h"
#include "threadpool.h"

/* Implement HTML5 fallback.
 * This means that if a non-API path refers to a file and that
 * file is not found or is a directory, return /index.html
 * instead.  Otherwise, return the file.
 */

bool html5_fallback = false;
bool silent_mode = false;
int token_expiration_time = 24 * 60 * 60;   // default token expiration time is 1 day

static void*connect_method(struct thread_pool *pool, void * data);
/*
 * A non-concurrent, iterative server that serves one client at a time.
 * For each client, it handles exactly 1 HTTP transaction.
 */
static void
server_loop(char *port_string)
{
    
    int nthreads = 500;
    struct thread_pool * threadpool = thread_pool_new(nthreads);
    int accepting_socket = socket_open_bind_listen(port_string, 1024);
    while (accepting_socket != -1) 
    {
        // fprintf(stderr, "Waiting for client...\n");
        int client_socket = socket_accept_client(accepting_socket);
        if (client_socket == -1)
            return;
        int *arg = malloc(sizeof(int));
        *arg = client_socket;
        thread_pool_submit(threadpool, connect_method, arg);
    }
    thread_pool_shutdown_and_destroy(threadpool);
}

static void *connect_method(struct thread_pool *pool, void * data)
{
    int client_socket = *((int*)data);
    struct http_client client;
    http_setup_client(&client, bufio_create(client_socket));
    http_handle_transaction(&client);
    bufio_close(client.bufio);
    return NULL;
}

static void
usage(char * av0)
{
    // fprintf(stderr, "Usage: %s [-p port] [-R rootdir] [-h] [-e seconds]\n"
    //     "  -p port      port number to bind to\n"
    //     "  -R rootdir   root directory from which to serve files\n"
    //     "  -e seconds   expiration time for tokens in seconds\n"
    //     "  -h           display this help\n"
    //     , av0);
    exit(EXIT_FAILURE);
}

int
main(int ac, char *av[])
{
    int opt;
    char *port_string = NULL;
    while ((opt = getopt(ac, av, "ahp:R:se:")) != -1) {
        switch (opt) {
            case 'a':
                html5_fallback = true;
                break;

            case 'p':
                port_string = optarg;
                break;

            case 'e':
                token_expiration_time = atoi(optarg);
                break;

            case 's':
                silent_mode = true;
                break;

            case 'R':
                server_root = optarg;
                break;

            case 'h':
            default:    /* '?' */
                usage(av[0]);
        }
    }

    // fprintf(stderr, "Using port %s\n", port_string);
    server_loop(port_string);
    exit(EXIT_SUCCESS);
}

