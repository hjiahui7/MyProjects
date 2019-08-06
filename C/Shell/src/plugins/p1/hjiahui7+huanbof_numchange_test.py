#!/usr/bin/python
#
# Tests the functionality of gback's glob module
# Also serves as example of how to write plugin tests.
# You can copy the first part of this file.
#
import sys

# the script will be invoked with two arguments:
# argv[1]: the name of the hosting shell's eshoutput.py file
# argv[2]: a string " -p dirname" where dirname is passed to stdriver.py
#          this will be passed on to the shell verbatim.
esh_output_filename = sys.argv[1]
shell_arguments = sys.argv[2]

import imp, atexit, sys
sys.path.append("/home/courses/cs3214/software/pexpect-dpty/");
import pexpect, shellio, signal, time, os, re, proc_check

#Ensure the shell process is terminated
def force_shell_termination(shell_process):
	c.close(force=True)

#pulling in the regular expression and other definitions
def_module = imp.load_source('', esh_output_filename)
logfile = None
if hasattr(def_module, 'logfile'):
    logfile = def_module.logfile

#spawn an instance of the shell
c = pexpect.spawn(def_module.shell + shell_arguments,  drainpty=True, logfile=logfile)

atexit.register(force_shell_termination, shell_process=c)

# set timeout for all following 'expect*' calls to 2 seconds
c.timeout = 2 

# ensure that shell prints expected prompt
assert c.expect(def_module.prompt) == 0, "Shell did not print expected prompt (1)" 




c.sendline("numchanger aa 10")
assert c.expect("Wrong format, You can enter either binary or hex") == 0, "Invalid format, you can enter hex or binary see example in readme"

c.sendline("numchanger hex aaa")
assert c.expect("Wrong input data") == 0, "Wrong input data"


c.sendline("numchanger hex 10")
assert c.expect("Your number 10 is a in hex") == 0, "Right data"

c.sendline("numchanger binary 10")
assert c.expect("Your number 10 is 1010 in binary") == 0, "Right data"

shellio.success()
