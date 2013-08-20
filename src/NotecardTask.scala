/* date:   Oct 26, 2011
						NOTECARD TASK
	Tasks activated by script '*' (asterisk) command, e.g., '* end'
          Tasks:
                 Terminate the card session
                 Save the symbol table
				 Management filename recorded
                 Deactivate the backup mechanism
*/
package com.client
import scala.collection.mutable.Map
import com.client.CommandNetwork._
import com.client.LinkTool._
import java.io._

case class NotecardTask(var symbolTable:Map[String,String]) extends Node  {
												/* Node
		symbolTable holds $<variables>				def setId
													def convertToSibling
													def convertToChild
													def convertToCondition
													*/
//------------paramters pass by .struct file-----------------
		var xtask=""
		var xtype=""
		var notecard:Notecard=null  //root of linked list command hierarchy

//-------------------swizzle---------------------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			}
//-----------------------------------------------------------
			//invoked by Notecard
			//Created by Client(tst) and passed to:
			//	Notecard, NextFile, NotecardTask
	def startNotecardTask(     taskGather:TaskGather) {

		println("NotecardTask: xtask="+xtask+"  xtype="+xtype)
		xtask match {
				case "end" => 		// * end   script encountered
						taskGather.setEndSession
				case "manage"=> 	// * manage <filename> script encountered
						establishManagementFile(taskGather, xtype)
				case "save"=>		// * save <filename> script encountered.
						println("NotecardTask  * save ")
						writeSymbolTableToFile(xtype, symbolTable)
				case "nobackup"=>
				case _=> println("NotecardTask: Unknown type=["+xtask+"]")
				}
		}
//			NotecardTask  isNoInputFields=false

				// Write $<variable>s to 'filename' file.  
 	def writeSymbolTableToFile(filename:String, symbolTable:Map[String,String]) {
			val file=new File(filename)
			val printer= new PrintWriter(file)
				try {
			symbolTable.map{x=> printer.println(x._1+"\t"+x._2) }
				} finally{ printer.close()}
		}

				// Creates Notecard instance from filename 'xtype' to be returned 
				// it to the current instance of Notecard via 'taskGather'. 
	def establishManagementFile(taskGather:TaskGather, filename:String) = {
						// reads <.struct> command file and creates a command 
						// hierarchy of linked lists, returning the root of 
						// the network.  If file not found, then it returns 
						// 'start.struct'.
		notecard=loadFileAndBuildNetwork(filename, symbolTable) //In CommandNetwork	
		println("NotecardTask: manage ")
		Session.armAsteriskButton  // Allows Notecard to enable AsteriskButton
					// pass notecard to current instance of Notecard
		taskGather.manageNotecard= notecard
		}
				// transfer SymbolTable $<variable> to file where file content
				// is variable name <key> and value, separated by a tab. 
	def writeSymbolTableToFile(filename:String	) {

		}
						//CreateClass loads class instance with argument 
						//values from <.struct> file. 
	def  receive_objects(structSet:List[String] ) {
		val in=structSet.iterator
		setAddress(in.next)  //Node
		setNext(in.next)     //Node
		xtask=in.next
		xtype=in.next
		}
	}

