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

class TaskGather()   {
	val NEXT_FILE=1
	val END_SESSION=2
	val TASK_NONE=3     //default state
	var task=TASK_NONE
		//'filename' transfered from NextFile to 'card'.
	var filename=""     
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
