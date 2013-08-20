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
			symbolTable holds $<variables>				def setId
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
						println("status <msg>")
						statusLine.addMessageToStatusLine(typex)
			case "continue"=>  // 
						activateNextButtonForContinue(inputFocus, notePanel)
			case _=> println("FrameNodeType  unknown type="+ taskx)
			}
		}
							// Invoked by "* continue" cmd.  It displays the text cmds
							// that preceded the '* continue' commands, then invokes
							// InputFocus to activate NEXT button and issue wait().
	def  activateNextButtonForContinue(inputFocus:InputFocus, notePanel:JPanel) {
		 notePanel.validate()
		 notePanel.repaint() //added because PROR button would not display 'd' cmd
		 inputFocus.establishAsteriskContinue // enable NEXT button and issue wait()
		}
				// *.struct file delivers symbolic links and object parameters.
	def  receive_objects(structSet:List[String] ) {
		val in=structSet.iterator
		setAddress(in.next)
		setNext(in.next)
		taskx=in.next
		typex=in.next
		}

	}

