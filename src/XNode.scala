/* date:   Dec 22, 2011
						X NODE	
		Child of CardSet. Suspends processing of the Card allowing 
		prior Card commands to execute
		Card Example:   c
						d Enter (# $one)
						x
						d Enter (# $two)
		Window is cleared, the first display cmd is rendered, and 
		execution is halted. At this point, the second display cmd
		is not presented.  User enters a response to $one, and 
		processing starts, presenting the second display cmd. 

		Purpose of XNode is to signal CardSet to issue a 'wait' 
		state.

		CardSet:
			case xn:XNode=> 
					showPanel(notePanel)
							//prevents InputFocus.actWhenAllFieldsCaptured 
							//from enabling NEXT button
					inputFocus.turnOnXNode  
					haltCommandExecution(lock) // issue lock.wait()

*/
package com.client
import scala.collection.mutable.Map

case class XNode(var symbolTable:Map[String,String]) extends Node {
						/*
						Node
symbolTable holds $<variables>				def setId
							def convertToSibling
							def convertToChild
							def convertToCondition
						*/
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			}
//-------------------------------------------------------------------
		//Load class instance with argument 
		//values from <.struct> file. Method
		//invoked in CreateClass
	def  receive_objects(structSet:List[String] ) {
		val in=structSet.iterator
		setAddress(in.next)  //Node
		setNext(in.next)     //Node
		}
	}

