/* date:   Nov 3, 2011
											Invoked in:  Notecard
								NEXT FILE
	Child of Notecard
	Script examples:
			f myfile
			f onefile (2)=($gender)   
	For the example 'f myfile', the filename 'myfile' is passed to
	'card' and is an argument in 'fileLoad_BuildNetwork()' which
	reads 'myfile.
*/
package com.client
import scala.collection.mutable.Map
								
case class NextFile(symbolTable:Map[String,String]) extends Linker {
												/*	 Node
			symbolTable holds $<variables>				def setId
														def convertToSibling
														def convertToChild

												*/
//------------paramters pass by .struct file-----------------
	var next_filename=""
	var xtype=""
 	var conditionStruct="" 
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			}
//-------------------------------------------------------------------

	def getFileName=next_filename
					// TaskGather created in 'card' which
					// reads the <.struct> file
	def startNextFile(taskGather:TaskGather) {
		println("NextFile  ------------------------------")
		if(isConditionPresent) {
			if(isConditionTrue) {
							//println("NextFile:  isConditionTrue()   ISTRUE")
						// Instructions passed back to 'card'. 
					taskGather.setNextFileFlag
					}
					// WHY END SESSION ???.  commands should just continue?????
					// Nov 20, 2012.  git merge 'newLogic' 
		//		else taskGather.setTaskNone //set task to END_SESSION
				}
		    else {  //Condition not present, so branch to 'next file' or
					// if file not found, then branch to START file.
				taskGather.setNextFileFlag
				}
					//if not $<variable> then nothing happens.
		next_filename=variableToFilename(next_filename, symbolTable)
							//println("NextFile: startNextFile:  next_fileame="+next_filename)
		taskGather.putFileName(next_filename)   
		taskGather.isNextFile //indicate that
		}
		//def isConditionPresent= {if(symCondition !="") true; else false}
	def isConditionPresent= {if(conditionStruct !="") true; else false}
	def isConditionTrue ={
			LogicTest.logicTest(conditionStruct, symbolTable)
			}
	def variableToFilename(fn: String, symbolTable:Map[String,String])={
			if(fn.take(1)=="$") {   
					//val variable=fn.drop(1) //remove $ symbol
				    //symbolTable(variable)
			    symbolTable(fn)
				}
			else fn  //fn was not a $variable--do nothing
			}
						//Load class instance with argument 
						//values from <.struct> file. Method
						//invoked in CreateClass
	def receive_objects(structSet:List[String]) {
		val in=structSet.iterator
		setAddress(in.next);
		setNext(in.next)
		next_filename=in.next
		println("NextFile =============="+next_filename+"====================")
					//NextFile.java commend of 'type": (xtype sub for type)
					//"start" to indicate that filename originates from
				    // the Start-file card or from the File-not-found card. 
				    // At present, the user has no control over this parameter. 
		conditionStruct=in.next
		}
}
