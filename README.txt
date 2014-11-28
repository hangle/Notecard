
Notecard Program
================

The Notecard presents a window about the size of a note card.  The window displays text   
to and accepts input from the program user (see example: www.notecard.org/document).  
The text and response capture are controlled by a script file of ten command types  
(see documents/Command directory).    

Applications:  
-------------

		Survey collection  
		Interviews
		Learning application   
		
Language:  
-------------
		Scala  

Run (src directory):  
-------------------


		scala card demo/card

<pre>
                      'scala card' executes the Notecard program.  In the argument 
                      'demo/card/', 'card' is a script file ('card.struct) in the 
                      'demo' directory. Execution illustrates the capabilities of 
                      the script commands.
</pre>

These capabilities can also be viewed at:  

		www.notecard.org:8090/cxm/background/introShowFeatures.htm  

Note, the applet system, written in java, is no longer supported.  

Fast Scala Compiler (src directory)  
-------------------------

		fsc *.scala  

Script repository:
-----------------

The input file, 'card.struct', to Notecard is generated by the program 'script.scala'  
(see Script repository).  This program converts the script commands in files having  
the '.nc' extension to the commands of Notecard files having the '.struct' extension.  

An essential role of the script program is to validate the script commands.   
