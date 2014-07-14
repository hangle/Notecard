/* date:   Dec 12, 2011
	The Group 'g' command conditionally controls one or more
	other commands.  The are four types of 'g' commands
	Script Examples:
				g (1)=(1)	// if then
				ge		// else
				ge (2)=(2)	// else if
				g		// ends group scope
*/
package com.client
import scala.collection.mutable.Map

case class GroupNode(symbolTable:Map[String,String]) extends Node   {
						/* Node: 		
	symbolTable holds $<variables>			def setId
							def convertToSibling
							def convertToChild
						*/
//------------paramters pass by .struct file-----------------
	var groupName=""
	var post=""     //post = to 'else', then the Group is an else clause.
	var conditionStruct=""
	//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			}
//-------------------------------------------------------------------
	val ThenNode=1         //Has Condition expression.e.g., (1)=(1)greaterThan
	val ElseNode=2		   //post is "else" but not Condition
	val ElseConditionNode=3//post is "else" and has Condition
	val EmptyNode=4		   //No Condition or no "else"
	var kind=0

	def whatKind=kind   	//Invoked by GroupResolve where kind= setGroupNodeType
	def isConditionTrue:Boolean ={ // Invoked in GroupResolve
		if(isCondition) {
					// false if condition fails, otherwise true
			val outcome=LogicTest.logicTest(conditionStruct, symbolTable) 
			outcome
			}
		  else
			true  //condition not present but treat it as if it is true
		}
	def isCondition:Boolean= {if(conditionStruct=="0") false; else true }

		// returns 1,2,3,or 4 based on post value 0/else and on condition present true/false.
	def setGroupNodeType:Int = {
		if(isCondition  && post=="0") 
			ThenNode  //not else and not an 'g' 
		else if(post=="else" &&  ! isCondition) 
			ElseNode // else without a condtion expression
		else if(post=="else" &&  isCondition) 
			ElseConditionNode // else with condition expression
		else if( ! isCondition && post=="0") 
			EmptyNode  // g without a condition or an 'else'.
		else 0
		}
		//Load class instance with argument 
		//values from <.struct> file. Method
		//invoked in CreateClass
	def  receive_objects(structSet:List[String] ) {
		import util.control.Breaks._
		var flag=true
		for( e <- structSet) {
		  breakable { if(e=="%%") break   // end of arguments
		  else {
			var pair=e.split("[\t]")	
			pair(0) match {
					case "address" => //println(pair(1))
							setAddress(pair(1))
					case "sibling" =>
							setNext(pair(1))
					case "condition" =>
							conditionStruct= pair(1)
					case "post"=>		// 0 or 'else'
							post=pair(1)
							kind=setGroupNodeType
					case "name" =>
							groupName= pair(1)
				}
				}
			   }  //breakable		 
			 }
		}
}
