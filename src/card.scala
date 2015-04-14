/* date:   Oct 26, 2011
							card  <was tst>
	'card.scala' executes the Notecard program.  
	Program reads the script command file whose extension is '.struct'.
	This input is generated by the 'script.scala' of the Script
	repository. 

	The $<variable> 'symbolTable' is initialized at this level so that
	it exists accross '.struct' files invoked indirectly by 'Notecard'

	The function 'loadFileAndBuildNetwork(...)' 
		(1) reads the '.struct' file
		(2) converts symbolic address to physical ones.
		(3) returns the root of the linked list structure (i.e, 'notecard')

	Call to 'startNotecard(...) begins the iteration of the linked lists nodes.

	On the return from 'startNotecard(...)' 'TaskGather' determines whether or
	not a new '.struct' file has been issued by the 'Filename' command.  If
	not, then it checks to determine if an '* end' command has been issued.
	If so, then the program terminates. 
*/ 
import scala.collection.mutable.Map
import com.client.Notecard
import com.client.TaskGather
import com.client.CommandNetwork._
import com.client.LinkTool._
import com.client.Session._
import com.client.Session

object card   {
	def main(argv: Array[String]) {
			// Command line argument of script file (no extension).
		var structFile=if(argv.size ==1) argv(0) 
					 // default script presenting a CardSet window that 
					 // allows the user to enter a filename.
			 else "start"   

				//println("card:  structFile="+structFile)
		var isLoop=true
			// Create table to hold card variables ($<variable>)
		var symbolTable=Map[String,String]()

				// If filename has path, eg., 'pool/file', then Session
				// Holds pathname across one or more files. If a
				// file has a path, then it is added to subsequent
				// files lacking a pathname. Path with '/'
			Session.setSessionPath("") 
				// Asterisk command '* end' of FrameTask sets
						try{
			while(isLoop)	{
					// Passed to Notecard and then to NextFile and
					// FrameTask to read new .struct file or to 
					// end the session. 
				var taskGather=new TaskGather()
					// reads <.struct> command file and creates a network/ of linked 
					// lists changing symbolic address to physical ones, and finally 
					// returning the root of the network. Also invoked in NotecardTask.
				val notecard:Notecard=loadFileAndBuildNetwork( structFile, symbolTable)//CommandNetwork
					// Invoke root of network and begin the Card session
				notecard.startNotecard(taskGather)
					// Window resources created in Notecard are no longer needed.
				taskGather.disposeJFrameResources
					// Next file or terminate session 
				if(taskGather.isNextFile) {			
					structFile=taskGather.getFileName
					//println("card: structFile=["+structFile+"]")
					}
				   else if(taskGather.isEndSession) {
					isLoop=false         // terminate while loop
					}
				} //while
						}catch{ case ex: Exception=>
							println("card: Exception thrown")
							}

			System.exit(0)
		} //main
	}
