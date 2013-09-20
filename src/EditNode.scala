/* date:   Jun 22, 2012
					EDIT NODE
	Evaluates the user's input before the input is stored.
	If the evaluation fails, then  the user is given the 
	opportunity to reenter the correct response. 
	For example, the Edit command (beginning with the 'e' tag):
			d Enter the type of animal shown (# $animal)
			e ($animal)=(cat)
	requires the input to be "cat", otherwise the input field
	is cleared, awaiting a new entry.  The Edit command has
	the capability of displaying a user prompt on failure. The
	prompt message is displayed on the status line. In the command,
	the message follows the 'status=' tag, for example:
			e ($animal)=(cat) status=The animal picture is a cat
	The expression '($animal)=(cat)' is a logic expression that is
	similar in function to the logic of the 'c', 'f', 'a', 'g' 
	commands.  For example:
			e ($ans)=(y) or ($ans)=(n) status=Enter y or n 
	supports a yes/no like response (note: the answer box 
	(# /limit 1/ $ans) restricts imput to one character).

	The literals 'number' and 'letter' may replace the logic expression,
	for example
			e number status=A number is required
	The literal 'letter' forces the input to consist of a non numeric
	value. 

	The Edit command has an optional identifier associating it with
	a particular input field:
			e <identifier> <logic> <status>

	The identifier is required when a Display command has more that
	one input field, e.g.,
			d Enter quantity=(# $quantity) and price=(# $price)
			e number
	The Edit command is only applied to the input of $price and not
	$quantity.  The $<variable> will associate the Edit command with
	a particular input field, e.g.,
			e $price number
			e $quantity number
			e $quantity ($quantity)> (1) and ($quantity) < (5)
	More than one Edit command can be associated with an input field
	as explained next.

	The EditNode is a child of the  AnswerBox parent,  allowing an 
	input field to have more than one edit evaluation. If anyone
	evaluation fails, then the user is required to reenter a response.

	The EditNode action occurs in the KeyListenerObject--it is here
	that the input field is captured and can be subject to evaluation.
	KeyListenerObject invokes BoxField (parent) to process its 
	EditNode children. 
*/
package com.client
import scala.collection.mutable.Map

case class EditNode(var symbolTable:Map[String,String]) extends Node {
	/*
					Node
	symbolTable holds $<variables>		def setId
						def convertToSibling
						def convertToChild
	*/
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)    //child of AnswerBox
			}
//-------------------------------------------------------------------
	var xtype=""
	var statusMessage=""
	var variable=""
	var conditionStruct=""
// -----------------------------------------------

		//Invoked by BoxField in an iteration loop
	def evaluateTheEditNode(response:String):Boolean= {
		if(isNumberOrLetter(xtype)) // edit cmd such as  'e number'
			evaluateNumberOrLetter(xtype, response)
		  else {  // edit cmd has logic such as 'e ($age) > (0)'
			val xxx=isConditionTrue	  // invokes LogicTest.logicTest(..)
			xxx
			}
		}
	def isNumberOrLetter(xtype:String):Boolean= { xtype=="number" || xtype=="letter"}
	def evaluateNumberOrLetter(xtype:String, response:String)= {
		xtype match {
			case "number" => 
				areAllDigits(response)
			case "letter" => 
				areAllLetters(response)
			case _=> println("EditNode unknown xtype="+xtype)
					false
			}
		}
	def areAllDigits(s:String):Boolean={ s.forall(x=> x.isDigit || x=='.') }
 	def areAllLetters(s:String):Boolean={ s.forall(x=> x.isLetter || x==' ' ) }
	def isConditionPresent ={ if(conditionStruct != "") true; else false }
	def isConditionTrue:Boolean ={ LogicTest.logicTest(conditionStruct, symbolTable) }
		// Invoked by BoxField
	def getFailureMessage= statusMessage
// -----------------------------------------------
		//Load class instance with argument 
		//values from <.struct> file. Method
		//invoked in CreateClass
	def  receive_objects(structSet:List[String] ) {
		val in=structSet.iterator
		setAddress(in.next)  //Node
		setNext(in.next)     //Node link to next EditNode
		conditionStruct=in.next // logic expression such as (1)=(1)
		xtype=in.next		// 'number' or 'letter'
		statusMessage=in.next
		variable=in.next	// $<variable> associated with edit
		val percent=in.next
		//println("EditNode: percent="+percent)
		}
}
