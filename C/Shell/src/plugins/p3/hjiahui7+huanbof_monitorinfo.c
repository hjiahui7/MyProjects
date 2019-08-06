#include <stdbool.h>
#include <stdio.h>
#include <pwd.h>
#include <unistd.h>
#include <sys/types.h>
#include "../esh.h"
#include <signal.h>
#include "../esh-sys-utils.h"
#include <ctype.h>

static bool checkIsDigit(char *str);

static bool
init_plugin(struct esh_shell *shell)
{
    printf("Plugin 'monitorinfo' initialized...\n");
    return true;
}

/* Implement chdir built-in.
 * Returns true if handled, false otherwise. */
static bool
monitorinfo_builtin(struct esh_command *cmd)
{
    double size[3][2] = {{0.87, 0.49}, {0.92, 0.39}, {0.8, 0.6}};
    if (strcmp(cmd->argv[0], "monitorinfo") != 0)
    {
        return true;
    }
    if (cmd->argv[1] != NULL && (strcmp(cmd->argv[1], "16:9") == 0 || strcmp(cmd->argv[1], "21:9") == 0 || strcmp(cmd->argv[1], "4:3") == 0))
    {
        double inch;
        if (cmd->argv[2] != NULL && checkIsDigit(cmd->argv[2]))
        {
            inch = atof(cmd->argv[2]);
        }
        else 
        {
            printf("need more parameter\n");
            return true;
        }
        int resolution = 0;
        if (cmd->argv[3] != NULL && checkIsDigit(cmd->argv[3]))
        {
            resolution = atoi(cmd->argv[3]);
        }
        int proportion;
        if (strcmp(cmd->argv[1], "16:9") == 0)
        {
            proportion = 0;
        }
        else if (strcmp(cmd->argv[1], "21:9") == 0)
        {
            proportion = 1;
        }  
        else if (strcmp(cmd->argv[1], "4:3") == 0)
        {
            proportion = 2;
        }
        double length = inch * size[proportion][0];
        double width = inch * size[proportion][1];
        if (resolution == 0)
        {
            printf("This monitor you choose is:  %.2f inches X %.2f inches\n", length, width);
            return true;
        }
        else
        {
            printf("This monitor you choose is:  %.2f inches X %.2f inches. The resulotion is: %d\n",
                   length, width, resolution / (int)(width));
            return true;
        }
    }
    printf("Wrong format, You can enter either 16:9 or 21:9 or 4:3\n");
    return true;
}

struct esh_plugin esh_module = {
    .rank = 1,
    .init = init_plugin,
    .process_builtin = monitorinfo_builtin
};

static bool checkIsDigit(char *str)
{
    int i = 0;
    for (i = 0; str[i] != '\0'; i++)
    {
        if (str[0] == '-')
        {
            continue;
        }
        // check for alphabets
        if (isalpha(str[i]) != 0 && str[i] != '.')
        {
            return false;
        }
    }
    return true;
}