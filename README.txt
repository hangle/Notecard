
Notecard Program
================

The Notecard presents a window about the size of a note card.  The window displays text   
to and accepts input from the program user.  

The text and response capture are controlled by a script file of ten command types  
(see documents/Command directory).    

Command documents and Notecard window examples are shown at:

		http://notecard.org/document

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


Fast Scala Compiler (src directory)  
-------------------------

		fsc *.scala  

Script repository:
-----------------

The script program (see Script repository) converts the commands in files with   
the '.nc' extension to elements in files with the '.struct' extension. The   
Notecard program 'card' executes the '.struct' files.  

The script program's essential role is the validation of script commands.  
