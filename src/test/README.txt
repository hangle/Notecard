
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

The TEST directory holds the '.nc' and '.struct' files. The  
'.struct' files contain script to exercises features of the  
system.  Its purpose is not a tutorial; rather, it is a means    
to reveal or find errors and imitations in the script   
commands.  The code of the '.struct' files has passed the  
edits and verfications of the 'script' program.  However,  
the TEST files verify whether or not the commands are  
accurately operating. For example, the script:  

		g (1)=(1)
		d Success
		ge
		d Failure

should print 'Success' and not 'Failure'.

To date, the following TEST script files are available:   

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
	logic.nc
		<linked to>
	  logicAnd.nc
	  logicOr.nc

		Example
			d g (1)>=(1)
			d d Success
			d ge
			d d Failure
			d * continue


Execution:
--------------

Execution of the sript appearance.nc  must  be compiled in   
the Script/src directory to produce a appearance.struct file:    

		scala script test/appearance

The output file of the 'scala script text/appearance' is  
'appearance.struct'.  This file is executed in the Notecard  
src directory:

		scala card test/appearance
