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

import imp, atexit
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




c.sendline("monitorinfo lalala")
assert c.expect("Wrong format, You can enter either 16:9 or 21:9 or 4:3") == 0, "Invalid format: 16:9"

c.sendline("monitorinfo 16:9")
assert c.expect("need more parameter") == 0, "Missing parameter"

c.sendline("monitorinfo 16:9 15")
assert c.expect("This monitor you choose is:  13.05 inches X 7.35 inches") == 0, "TestPass"

c.sendline("monitorinfo 16:9 30.3 1080")
assert c.expect("This monitor you choose is:  26.36 inches X 14.85 inches. The resulotion is: 77") == 0, "TestPass"

shellio.success()