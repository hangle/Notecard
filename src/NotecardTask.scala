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
		symbolTable holds $<variables>		def setId
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
		//Created by 'card' and passed to:
		//	Notecard, NextFile, NotecardTask
	def startNotecardTask(     taskGather:TaskGather) {

		xtask match {
			case "end" => 		// * end   script encountered
				println("NotecardTask:   end case")
				taskGather.setEndSession
			case "manage"=> 	// * manage <filename> script encountered
				establishManagementFile(taskGather, xtype)
			case "save"=>		// * save <filename> script encountered.
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
			// pass notecard to current instance of Notecard
		taskGather.manageNotecard= notecard
		}
		// transfer SymbolTable $<variable> to file where file content
		// is variable name <key> and value, separated by a tab. 
	def writeSymbolTableToFile(filename:String	) {

		}
		// CreateClass generates instances of NotecardTask without fields or parameters.
		// However, it invokes 'receive_objects' to load parameters from *.struct
		// file as well as symbolic addresses to be made physical ones. 
	def  receive_objects(structSet:List[String] ) {
			import util.control.Breaks._
			var flag=true
			for( e <- structSet) {
			  breakable { if(e=="%%") break   // end of arguments
			  else {
				var pair=e.split("[\t]")	
				pair(0) match {
							case "address" => 
									setAddress(pair(1))
							case "sibling" =>
									setNext(pair(1))
							case "task" => 
									xtask=pair(1)
							case "type" => 
									xtype= pair(1)
							}
				}
			   }  //breakable		 
			  }
	}
}
