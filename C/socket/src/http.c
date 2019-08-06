/*
 * A partial implementation of HTTP/1.0
 *
 * This code is mainly intended as a replacement for the book's 'tiny.c' server
 * It provides a *partial* implementation of HTTP/1.0 which can form a basis for
 * the assignment.
 *
 * @author G. Back for CS 3214 Spring 2018
 */
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <string.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdbool.h>
#include <errno.h>
#include <unistd.h>
#include <time.h>
#include <fcntl.h>
#include <linux/limits.h>
#include <jansson.h>

#include "globals.h"
#include "http.h"
#include "hexdump.h"
#include "socket.h"
#include "bufio.h"

// Need macros here because of the sizeof
#define CRLF "\r\n"
#define STARTS_WITH(field_name, header) \
    (!strncasecmp(field_name, header, sizeof(header) - 1))
static int private(struct http_transaction *ta, char *basedir);
static bool handle_static_asset(struct http_transaction *ta, char *basedir);
char * server_root;     // root from which static files are served
static const char * SECRET_CODE = "secret_code";
//static int last_request_time;
static const char * const USR = "user0";
static const char * const PWD = "thepassword";
static const char * const COOKIE_HDDR = "Set-Cookie";
/* Parse HTTP request line, setting req_method, req_path, and req_version. */
static bool
http_parse_request(struct http_transaction *ta)
{
    size_t req_offset;
    ssize_t len = bufio_readline(ta->client->bufio, &req_offset);
    if (len < 2)       // error, EOF, or less than 2 characters
        return false;

    char *request = bufio_offset2ptr(ta->client->bufio, req_offset);
    char *endptr;
    char *method = strtok_r(request, " ", &endptr);
    if (method == NULL)
        return false;

    if (!strcmp(method, "GET"))
        ta->req_method = HTTP_GET;
    else if (!strcmp(method, "POST"))
        ta->req_method = HTTP_POST;
    else
        ta->req_method = HTTP_UNKNOWN;

    char *req_path = strtok_r(NULL, " ", &endptr);
    if (req_path == NULL)
        return false;

    ta->req_path = bufio_ptr2offset(ta->client->bufio, req_path);

    char *http_version = strtok_r(NULL, CRLF, &endptr);
    if (http_version == NULL)  // would be HTTP 0.9
        return false;

    if (!strcmp(http_version, "HTTP/1.1"))
        ta->req_version = HTTP_1_1;
    else if (!strcmp(http_version, "HTTP/1.0"))
        ta->req_version = HTTP_1_0;
    else
        return false;

    return true;
}

/* Process HTTP headers. */
static bool
http_process_headers(struct http_transaction *ta)
{
    for (;;) {
        size_t header_offset;
        ssize_t len = bufio_readline(ta->client->bufio, &header_offset);
        if (len <= 0)
            return false;

        char *header = bufio_offset2ptr(ta->client->bufio, header_offset);

        if (len == 2 && STARTS_WITH(header, CRLF))       // empty CRLF
            return true;

        header[len-2] = '\0';
        char *endptr;
        char *field_name = strtok_r(header, ":", &endptr);
        char *field_value = strtok_r(NULL, " \t", &endptr);    // skip leading & trailing OWS

        if (field_name == NULL)
            return false;

         
        if (!strcasecmp(field_name, "Content-Length")) {
            ta->req_content_len = atoi(field_value);
        }

        if (!strcasecmp(field_name, "Cookie")) {
            field_value += 11;
            ta->cookie = malloc(sizeof(char) * strlen(field_value));
            strcpy(ta->cookie,field_value);
        }
    }
}

const int MAX_HEADER_LEN = 2048;

/* add a formatted header to the response buffer. */
void 
http_add_header(buffer_t * resp, char* key, char* fmt, ...)
{
    va_list ap;

    buffer_appends(resp, key);
    buffer_appends(resp, ": ");

    va_start(ap, fmt);
    char *error = buffer_ensure_capacity(resp, MAX_HEADER_LEN);
    int len = vsnprintf(error, MAX_HEADER_LEN, fmt, ap);
    resp->len += len > MAX_HEADER_LEN ? MAX_HEADER_LEN - 1 : len;
    va_end(ap);

    buffer_appends(resp, "\r\n");
}

/* add a content-length header. */
static void
add_content_length(buffer_t *res, size_t len)
{
    http_add_header(res, "Content-Length", "%ld", len);
}

/* start the response by writing the first line of the response 
 * to the response buffer.  Used in send_response_header */
static void
start_response(struct http_transaction * ta, buffer_t *res)
{
    
    buffer_appends(res, "HTTP/1.1 ");

    switch (ta->resp_status) {
    case HTTP_OK:
        buffer_appends(res, "200 OK");
        break;
    case HTTP_BAD_REQUEST:
        buffer_appends(res, "400 Bad Request");
        break;
    case HTTP_PERMISSION_DENIED:
        buffer_appends(res, "403 Permission Denied");
        break;
    case HTTP_NOT_FOUND:
        buffer_appends(res, "404 Not Found");
        break;
    case HTTP_METHOD_NOT_ALLOWED:
        buffer_appends(res, "405 Method Not Allowed");
        break;
    case HTTP_REQUEST_TIMEOUT:
        buffer_appends(res, "408 Request Timeout");
        break;
    case HTTP_REQUEST_TOO_LONG:
        buffer_appends(res, "414 Request Too Long");
        break;
    case HTTP_NOT_IMPLEMENTED:
        buffer_appends(res, "501 Not Implemented");
        break;
    case HTTP_SERVICE_UNAVAILABLE:
        buffer_appends(res, "503 Service Unavailable");
        break;
    case HTTP_INTERNAL_ERROR:
    default:
        buffer_appends(res, "500 Internal Server Error");
        break;
    }
    buffer_appends(res, CRLF);
}

/* Send response headers to client */
static bool send_response_header(struct http_transaction *ta)
{
    buffer_t response;
    buffer_init(&response, 80);

    start_response(ta, &response);
    if (bufio_sendbuffer(ta->client->bufio, &response) == -1)
        return false;

    buffer_appends(&ta->resp_headers, CRLF);
    if (bufio_sendbuffer(ta->client->bufio, &ta->resp_headers) == -1)
        return false;

    buffer_delete(&response);
    return true;
}

/* Send a full response to client with the content in resp_body. */
static bool send_response(struct http_transaction *ta)
{
    // add content-length.  All other headers must have already been set.
    add_content_length(&ta->resp_headers, ta->resp_body.len);

    if (!send_response_header(ta))
        return false;

    return bufio_sendbuffer(ta->client->bufio, &ta->resp_body) != -1;
}

const int MAX_ERROR_LEN = 2048;

/* Send an error response. */
static bool send_error(struct http_transaction * ta, enum http_response_status status, const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    char *error = buffer_ensure_capacity(&ta->resp_body, MAX_ERROR_LEN);
    int len = vsnprintf(error, MAX_ERROR_LEN, fmt, ap);
    ta->resp_body.len += len > MAX_ERROR_LEN ? MAX_ERROR_LEN - 1 : len;
    va_end(ap);
    ta->resp_status = status;
    return send_response(ta);
}

/* Send Not Found response. */
static bool
send_not_found(struct http_transaction *ta)
{
    return send_error(ta, HTTP_NOT_FOUND, "File %s not found", 
        bufio_offset2ptr(ta->client->bufio, ta->req_path));
}

/* A start at assigning an appropriate mime type.  Real-world 
 * servers use more extensive lists such as /etc/mime.types
 */
static const char *
guess_mime_type(char *filename)
{
    char *suffix = strrchr(filename, '.');
    if (suffix == NULL)
        return "text/plain";

    if (!strcasecmp(suffix, ".html"))
        return "text/html";

    if (!strcasecmp(suffix, ".gif"))
        return "image/gif";

    if (!strcasecmp(suffix, ".png"))
        return "image/png";

    if (!strcasecmp(suffix, ".jpg"))
        return "image/jpeg";

    if (!strcasecmp(suffix, ".js"))
        return "text/javascript";

    return "text/plain";
}

/* Handle HTTP transaction for static files. */
static bool handle_static_asset(struct http_transaction *ta, char *basedir)
{
    char fname[PATH_MAX];

    char *req_path = bufio_offset2ptr(ta->client->bufio, ta->req_path);
    // The code below is vulnerable to an attack.  Can you see
    // which?  Fix it to avoid indirect object reference (IDOR) attacks.
    snprintf(fname, sizeof fname, "%s%s", basedir, req_path);
    if(!strstr(fname, ".."))
    {
        if (access(fname, R_OK)) 
        {
            if (errno == EACCES)
                return send_error(ta, HTTP_PERMISSION_DENIED, "Permission denied.");
            else
                return send_not_found(ta);
        }
    }
    else
    {
        return send_error(ta, HTTP_NOT_FOUND, "Not Found.");
    }
    // Determine file size
    struct stat st;
    int rc = stat(fname, &st);
    if (rc == -1)
        return send_error(ta, HTTP_INTERNAL_ERROR, "Could not stat file.");

    int filefd = open(fname, O_RDONLY);
    if (filefd == -1) {
        return send_not_found(ta);
    }

    ta->resp_status = HTTP_OK;
    add_content_length(&ta->resp_headers, st.st_size);
    http_add_header(&ta->resp_headers, "Content-Type", "%s", guess_mime_type(fname));

    bool success = send_response_header(ta);
    if (!success)
        goto out;

    success = bufio_sendfile(ta->client->bufio, filefd, NULL, st.st_size) == st.st_size;
out:
    close(filefd);
    return success;
}


static int handle_api(struct http_transaction *ta)
{
    if(ta->req_method == HTTP_GET)
    {
        const char* cookie = ta->cookie;
        ta->resp_status = HTTP_OK;
        if(cookie != NULL)
        {
            buffer_appends(&ta->resp_body, ta->input_json);
        }
        else
        {
            buffer_appends(&ta->resp_body, "{}");
        }
        return send_response(ta);
    }
    else if(ta->req_method == HTTP_POST)	
    {
        json_error_t error;
        jwt_t *mytoken;	
        char *body = bufio_offset2ptr(ta->client->bufio, ta->req_body);
        json_t * json_files = json_loadb(body, ta->req_content_len, 0, &error);
        const char* user = json_string_value(json_object_get(json_files, "username"));
        const char* password = json_string_value(json_object_get(json_files, "password"));
        if(user== NULL || password== NULL || strcmp(user, USR) || strcmp(password, PWD))
        {
            return send_error(ta, HTTP_PERMISSION_DENIED, "Invalid user");
        }	
        jwt_new(&mytoken);
        time_t now = time(NULL);
        jwt_add_grant_int(mytoken, "exp", now + token_expiration_time);
        jwt_add_grant_int(mytoken, "iat", now);
        jwt_add_grant(mytoken,"sub", user);
        char *input_json = jwt_get_grants_json(mytoken, NULL);
        buffer_appends(&ta->resp_body, input_json);
        jwt_set_alg(mytoken, JWT_ALG_HS256,(unsigned char *)SECRET_CODE, strlen(SECRET_CODE));
        char *encoded = jwt_encode_str(mytoken);
        ta->resp_status = HTTP_OK;
        http_add_header(&ta->resp_headers, "Content-Type", "application/json");
        http_add_header(&ta->resp_headers, "Set-Cookie","auth_token=%s; Path=/", encoded);
        return send_response(ta);
    }
    else
    {
        ta->resp_status = HTTP_METHOD_NOT_ALLOWED;
        send_response(ta);
    }
    return 0;
}

/* Set up an http client, associating it with a bufio buffer. */
void 
http_setup_client(struct http_client *self, struct bufio *bufio)
{
    self->bufio = bufio;
}

/* Handle a single HTTP transaction.  Returns true on success. */
bool http_handle_transaction(struct http_client *self)
{
    bool rc;
    struct http_transaction ta;
    do
    {
        rc = false;
        memset(&ta, 0, sizeof ta);
        ta.client = self;

        if (!http_parse_request(&ta))
            return false;

        if (!http_process_headers(&ta))
            return false;

        if (ta.req_content_len > 0) {
            int rc = bufio_read(self->bufio, ta.req_content_len, &ta.req_body);
            if (rc != ta.req_content_len)
                return false;
        }

        buffer_init(&ta.resp_headers, 1024);
        http_add_header(&ta.resp_headers, "Server", "CS3214-Personal-Server");
        buffer_init(&ta.resp_body, 0);
        char *req_path = bufio_offset2ptr(ta.client->bufio, ta.req_path);
        if (STARTS_WITH(req_path, "/api")) 
        {
            if(!strcmp(req_path, "/api/login"))
            {
                rc = handle_api(&ta);
            }
            else
            {
                return send_error(&ta, HTTP_NOT_FOUND, "Not find");
            }
        } 
        else if (STARTS_WITH(req_path, "/private")) {
            rc = private(&ta, server_root);
        } else {
            rc = handle_static_asset(&ta, server_root);
        }
        buffer_delete(&ta.resp_headers);
        buffer_delete(&ta.resp_body);
    }
    while(rc & (ta.req_version == HTTP_1_1));
    return true;
}


static int private(struct http_transaction *ta, char *basedir)
{
    const char* cookie = ta->cookie;
    if(cookie != NULL)
    {
        jwt_t *ymtoken;
        jwt_decode(&ymtoken, cookie, (unsigned char *)SECRET_CODE, strlen(SECRET_CODE));
        const char *client = jwt_get_grant(ymtoken, "sub");
        time_t exper_time = jwt_get_grant_int(ymtoken, "exp");
        time_t now = time(NULL);
        if(exper_time > now && strcmp(client, "user0") == 0)
        {
            bool rc = handle_static_asset(ta, server_root);
            return rc;
        }   
    }
    return send_error(ta, HTTP_PERMISSION_DENIED, "HTTP_PERMISSION_DENIED");
}