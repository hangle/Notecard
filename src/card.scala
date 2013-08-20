/* date:   Oct 26, 2011
							CLIENT  <alias is tst>
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
			var name=""
	
			name="pool/one"
				//default to command line filename
			var argument=if(argv.size ==1) argv(0) 
						 else name

			var file=""
			var isLoop=true
						// Create table to hold card variables ($<variable>)
			var symbolTable=Map[String,String]()

						// If filename has path, eg., 'pool/file', then Session
						// Holds pathname across one or more files. If a
						// file has a path, then it is added to subsequent
						// files lacking a pathname. Path with '/'
			Session.setSessionPath("") 
			Session.setClientNotecardState
						// Asterisk command '* end' of FrameTask sets
				
		// 'isLoop' false
			while(isLoop)	{
						// Passed to Notecard and then to NextFile and
						// FrameTask to read new .struct file or to 
						// end the session. 
				var taskGather=new TaskGather()
						// reads <.struct> command file and creates a network
						// of linked lists changing symbolic address to physical
						// ones, and finally returning the root of the network. 
				val f:Notecard=loadFileAndBuildNetwork( argument, symbolTable)//CommandNetwork

						// Invoke root of network and begin the Card session
				f.startNotecard(taskGather)
						// Next file or terminate session 
				if(taskGather.isNextFile) {			
						argument=taskGather.getFileName
						}
				   else if(taskGather.isEndSession) {
						isLoop=false         // terminate while loop
						}
				} //while
			for(m <- symbolTable)
				println(m._1+"  "+m._2)

			System.exit(0)
		} //main
	}
