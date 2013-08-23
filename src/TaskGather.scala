/* date:   Dec 14, 2011
  							TASK GATHERER 
		Created in 'card' and passed to Notecard and
		into: 
			NextFile 	   Assigns filename
			NotecardTask,  If the parameter 'xtask' is "end", then
						   END_SESSION is returned to 'card'. 

		Return from Notecard requires either a new .struct 
		file or the termination of the session.
		Establishes communication between TaskLoop and Notecard,
		as well as NextFile.  Action taken is to either 
		end the session or to read a new (next) .struct file.
_____________________________
*/
package com.client

class TaskGather()   {
	val NEXT_FILE=1
	val END_SESSION=2
	val TASK_NONE=3     //default state
	var task=TASK_NONE
	var filename=""     //value transfered from NextFile to 'card'.
	var manageNotecard:Notecard=null

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
}
