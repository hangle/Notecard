
---------------------------------------------  
Test Directory (Script/src/test):  
---------------------------------------------   

The execution of the script in the DEMO directory:  

		scala card demo/card

reveals features of the Notecard capabilities.  However, it  
does not exercise the complete range of capabilities.  The  
DEMO directory just highlights the major Notecard features.  
For the user, it illustrates the potential of the Notecard   
system.  

The TEST directory contains script to exercises every   
feature of the system.  Its purpose is not a tutorial;   
rather, it is a means  to reveal or find errors and   
limitations in the script :code.   

It is expected that each script in the TEST directory   
will grow to exercise every combination of script elements  
in serarch of a failure, either leading to code correction  
or a controlled exception in the script program. For  
example, suppose a command begins with the letter 's'  
(not a valid command tag), then the Script program will  
print this invalid line and will indicate that the 's' is  
an invalid command tag. Or, execution of the Notecard  
program fails. In this case, the culprit may be the Script  
program for not detecting the code that caused the  
Notecard program to fail.   

The  focus of the TEST directory is to sresss the system.  
The TEST directory also serves as a regression test.   

To date, the following TEST scripts are available:   

	appearance.nc		
		Example:
			d 5/4/color red/Sorry wrong answer
			d now is (%%/color blue/for all good) men
	groupCmd.nc
		Example:
			g (flag)=(true)
			ge
	inputFields.nc
		Example:
			d Enter (# $name)
			d (# $one)  (# $two)  (# $three)
	letterSize.nc		Note, component of appearance
		Example:
			d 5/4/size 20/Sorry wrong answer
			d now is (%%/size 10/for all good) men
	loadDictionary.nc
		Example:
			l
			a $count=0
			a $flag=false
	addCardSet.nc
		Example:
			c
			d This is a CardSet
			+
			d This is an AddCardSet
	editCommand.nc
		Example
			c
			d Enter your age (# $age)
			e number
			e ($age) >(0) and ($age) < (100)	
Execution:
--------------

Execution of the sript appearance.nc  must  be compiled in   
the Script/src directory:    

		scala script test/appearance

The output file of the 'scala script text/appearance' is  
'appearance.struct'.  This file is executed in the Notecard  
src directory:

		scala card test/appearance
