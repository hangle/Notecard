// date:   Oct 26, 2011
/*
	The '*' command is processed by CardSetTask, for example:
		* status you have completed the survey 
	On creation, two parameters are loaded into CardSetTask:
		taskx		-- task, such as, 'status', 'continue', 'break'
		typex		-- task argument
	'*' commands:
		* status <message> Message is displayed in status box
		* continue<no arg> Preceding Card commands are displayed and
							NEXT button is enabled. When NEXT is
							activated, then following Card commands
							are processed. 
		* end	Terminate session by invoking 'start' window
*/
package com.client
import scala.collection.mutable.Map
import javax.swing.JLabel
import javax.swing.JPanel

case class CardSetTask(var symbolTable:Map[String,String]) extends Node   {
													/*
					Node
	symbolTable holds $<variables>		def setId
						def convertToSibling
						def convertToChild
*/

//------------paramters pass by .struct file-----------------
	var taskx=""    // Indicates task such as 'status'
	var typex=""	// A task has a argument, such as, message in 'status'
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			}
//-------------------------------------------------------------------
								// Invoked by CardSet( FrameNodeTasl is child of CardSet)
	def startCardSetTask( inputFocus:InputFocus, statusLine:StatusLine, notePanel:JPanel) {
		taskx match {
			case "status" => 
					statusLine.addMessageToStatusLine(typex)
			case "continue"=>  
					activateNextButtonForContinue(inputFocus, notePanel)
			case _=> println("CardSetTask  unknown type="+ taskx)
			}
		}
		// Invoked by "* continue" cmd.  Text cmds that preceed the command are displayed.
		// The '* continue' commands, invokes  InputFocus to activate NEXT button and 
		// issue wait().
	def  activateNextButtonForContinue(inputFocus:InputFocus, notePanel:JPanel) {
		 notePanel.validate()
		 notePanel.repaint() //added because PROR button would not display 'd' cmd
		 inputFocus.establishAsteriskContinue // enable NEXT button and issue wait()
		}
		// CreateClass generates instances of CardSetTask without fields or parameters.
		// However, it invokes 'receive_objects' to load parameters from *.struct
		// file as well as symbolic addresses to be physical ones. 
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
									taskx=pair(1)
							case "type" => 
									typex= pair(1)
							}
				}
			   }  //breakable		 
			  }

/*
		val in=structSet.iterator
		setAddress(in.next)
		setNext(in.next)
		taskx=in.next
		typex=in.next
		val percent=in.next
*/
		}
	}

