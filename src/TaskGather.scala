/* date:   Dec 14, 2011
  							TASK GATHER 
		Establish communication with:
			Notecard
			NextFile
			NotecardTask
		Created in 'card' and passed to Notecard and
		into: 
			NextFile 	   'f <filename> cmd assigns filename
			NotecardTask,  '* end' cmd caused END_SESSION returned to 'card'. 
							to terminate the session.`
		Return from Notecard requires either a new .struct 
		file or the termination of the session.
		Establishes communication between  and Notecard,
		as well as NextFile.  Action taken is to either 
		end the session or to read a new (next) .struct file.
_____________________________
*/
package com.client
import javax.swing.JFrame

class TaskGather()   {
	val NEXT_FILE=1
	val END_SESSION=2
	val TASK_NONE=3     //default state
	var task=TASK_NONE
		// JFrame instance assigned in Notecard that is passed to 'card'
		// to be disposed of.
	var oldJFrame:JFrame=null
		//'filename' transfered from NextFile to 'card'.
	var filename=""     
		// NotecardTask.loadFileAndBuildNetword() creates the Linked List class network
		// with .struct file from '* manage <filename.struct>. 'manageNotecard' is
		// the root of this linked list. 
	var manageNotecard:Notecard=null

	var oldJFrameList=List[JFrame]()
		// Used by Notecard to assign NotecardTask's framer to manageNotecard.
	def isManagement= if(manageNotecard ==null) false; else true
		// Tested at the completion of 'card'. 
		// loop. Sets while(isLoop) false for '* end'
		// command
	def setEndSession:Unit={ task=END_SESSION }
		// TaskGather.task defaults to TASK_NONE
	def isEndSession=if(task==END_SESSION) true; else false
		// Tests if NextFile's 'f <filename>' command is found as
		// a NotecardTask object. If so, NotecardTask.getFilename
		// returns <filename> to 'card'.
	def isNextFile=if(task==NEXT_FILE) true; else false
		// If '* end' or 'f <filename>' commands are detected, 
		// then 'isTaskNone' is false, causing the Notecard's iteration 
		// to be terminated, returning control to 'card'. 
	def setTaskNone:Unit={task=TASK_NONE }
	def isTaskNone=if(task==TASK_NONE) true; else false
		// NextFile object assigns NEXT_FILE to 'task'.
	def setNextFileFlag:Unit={ task=NEXT_FILE }
	def getFileName = filename   // see 'card'
		// Transfers <filename> from NextFile to
		// TaskGather.getFilename in 'card'.
	def putFileName(s:String)={ filename=s} //NextFile.startNextFile
		// java.awt.Window.dispose() is used on a GUI component that is
		// to properly release and destroy native UI resources (such
		// as the screen).  	
	def disposeJFrameResources={ 
				oldJFrame.dispose() 
				disposeOldJFrame 
				}

	def addOldJFrameList(old:JFrame)= oldJFrameList = old :: oldJFrameList
	def disposeOldJFrame= { oldJFrameList.foreach(x=> x.dispose()) }

}
