#include <stdbool.h>
#include <stdio.h>
#include <pwd.h>
#include <unistd.h>
#include <sys/types.h>
#include "../esh.h"
#include <signal.h>
#include "../esh-sys-utils.h"
#include <ctype.h>


static bool checkIsDigit(char * str);

static bool 
init_plugin(struct esh_shell *shell)
{
    printf("Plugin 'numberchanger' initialized...\n");
    printf("The number you enter should not bigger or smaller than 2^31(2147483647)\n");
    return true;
}

/* Implement chdir built-in.
 * Returns true if handled, false otherwise. */
static bool
numberChanger_builtin(struct esh_command *cmd)
{
    if (strcmp(cmd->argv[0], "numchanger") != 0)
    {
        return true;
    }  
    if (strcmp(cmd->argv[1], "binary") == 0 || strcmp(cmd->argv[1], "hex") == 0)
    {
        int number;
        if(cmd->argv[2] != NULL && checkIsDigit(cmd->argv[2]))
        {
            number= atoi(cmd->argv[2]);
        }
        else
        {
            printf("Wrong input data\n");
            return true;
        }
        if(strcmp(cmd->argv[1], "hex") == 0 )
        {
            char hex[10000];
            sprintf(hex,"%x",number);
            printf("Your number %d is %s in hex\n",number,hex);
            return true;
        }
        else
        {
            printf("Your number %d is ",number);
            int array[64];
            int count =0;
            while (number) 
            {
                if (number & 1)
                {
                    array[count] = 1;
                }
                else
                {
                    array[count] = 0;
                } 
                count++;
                number = number >> 1;
            }
            for(; count > 0; count--)
            {
                printf("%d", array[count -1]);
            }
            printf(" in binary\n");
            return true;
        }
    }
    printf("Wrong format, You can enter either binary or hex\n");
    return flase;
}

struct esh_plugin esh_module = {
  .rank = 1,
  .init = init_plugin,
  .process_builtin = numberChanger_builtin
};

static bool checkIsDigit(char * str)
{
    int i =0;
    for (i=0; str[i]!= '\0'; i++) 
    { 
        if (str[0] == '-')
        {
            continue;
        } 
        // check for alphabets 
        if (isalpha(str[i]) != 0) 
        {
            return false;
        }
    } 
    return true;
}