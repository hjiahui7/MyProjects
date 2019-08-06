#include <stdbool.h>
#include <stdio.h>
#include <pwd.h>
#include <unistd.h>
#include <sys/types.h>
#include "../esh.h"
#include <signal.h>
#include "../esh-sys-utils.h"
#include <ctype.h>
#include <math.h>
bool checkisdigit(char* argv);
float sqrttt(float number);
static bool 
init_plugin(struct esh_shell *shell)
{
    printf("Plugin 'areaTriCal' initialized...\n");
    return true;
}

/* Implement chdir built-in.
 * Returns true if handled, false otherwise. */
static bool
areaTriCal_builtin(struct esh_command *cmd)
{
    if (strcmp(cmd->argv[0], "areaTriCal") != 0)
    {
        return true;
    }  
    if (cmd->argv[1] != NULL && strcmp(cmd->argv[1], "mod1") == 0)
    {

        if(cmd->argv[2] != NULL && cmd->argv[3] != NULL &&cmd->argv[4] != NULL &&checkisdigit(cmd->argv[2])&& checkisdigit(cmd->argv[3])&& checkisdigit(cmd->argv[4]))
        {
            double number1= atoi(cmd->argv[2]);
            double number2= atoi(cmd->argv[3]);
            double number3= atoi(cmd->argv[4]);
            double p = (number1 + number2 + number3)/2;
            double t = p*(p-number1)*(p-number2)*(p-number3);
            double result = sqrttt(t);
            printf("Your result using mod1 is %lf\n" , result);
            return true;
        }
        else
        {
            printf("Wrong input data of ages\n");
            return true;
        }
    }
    else if (strcmp(cmd->argv[1], "mod2") == 0)
    {
        if(cmd->argv[2] != NULL && cmd->argv[3] != NULL &&checkisdigit(cmd->argv[2])&& checkisdigit(cmd->argv[3]))
        {
            double number1= (double)atoi(cmd->argv[2]);
            double number2= (double)atoi(cmd->argv[3]);
            double result = number1 * number2 /2;
            printf("Your result using mod2 is %lf\n" , result);
            return true;
        }
        else
        {
            printf("Wrong input data of height or width\n");
            return true;
        }
    }
    printf("Wrong format, You can enter either mod1 or mod2\n");
    return true;
}

struct esh_plugin esh_module = {
  .rank = 1,
  .init = init_plugin,
  .process_builtin = areaTriCal_builtin
};

bool checkisdigit(char* argv)
{
    int i =0;
    for (i=0; argv[i]!= '\0'; i++) 
    { 
        if (!isdigit(argv[i]))
        {
            return false;
        } 
    } 
    return true;
}
float sqrttt(float number) 
{ 

  float x = number; 
  float y = 1; 
  while(x - y > 0) 
  { 
    x = (x + y)/2; 
    y = number/x; 
  } 
  return x; 
} 