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
		if(isCondition) 
					// false if condition fails, otherwise true
			LogicTest.logicTest(conditionStruct, symbolTable) 
		  else
			true  //condition not present but treat it as if it is true
		}
	def isCondition:Boolean= {if(conditionStruct=="0") false; else true }
	def setGroupNodeType:Int = {
		if(isCondition  && post=="") ThenNode  //not else and not an 'g' 
		else if(post=="else" &&  ! isCondition) ElseNode // else without a condtion expression
		else if(post=="else" &&  isCondition) ElseConditionNode // else with condition expression
		else if( ! isCondition && post=="") EmptyNode  // g without a condition or an 'else'.
		else 0
		}
		//Load class instance with argument 
		//values from <.struct> file. Method
		//invoked in CreateClass
	def receive_objects(structSet:List[String]) {
		val in=structSet.iterator
		setAddress(in.next);
		setNext(in.next)
		groupName= in.next
		conditionStruct=in.next
		if(conditionStruct=="") //inconsistent use of "" and "" in <.struct> file
			conditionStruct=""
		post=in.next       // "else" or ""
			// value established based on the combination of 'condition' and
			// 'else' being present or not.  Example 'condition' present
			// but 'else' missing, then the 'kind' value indicates only 
			// a "then" state.
		kind=setGroupNodeType
		val percent= in.next
		//println("GroupNode: percent="+percent)
		}
}
