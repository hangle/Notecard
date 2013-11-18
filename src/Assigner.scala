// date:   Oct 26, 2011
/*
			---------------------------------------------
			The Assign command, such as 'a $gender=male', appears in the
			card set but it is not a child of CardSet (parent of the card set) 
			giving the illusion that it is executed along with other card set
			commands. However,i t is not executed in the card set. The command is a 
			child of Notecard and is executed when CardSet returns. Is this a
			problem if within the card set as assignment is made and a subsequent
			card command uses this assignment? This situation can only happen 
			if an XNode command is executed, followed by the command that uses
			the assignment. However, the XNode command is a child, like the
			Assigner command, of Notecard. 

			As a child of CardSet, the Assign command can be used before
			and after a card set. 
			---------------------------------------------
	
		The Assign command, beginning with an 'a' tag, assigns a value to a
		variable, for example:
			a $gender=male
			a $name=$first
		The assign value may be the results of a math expression:
			a $result= 4 + 2 / 4
			a $increment= $increment + 1
		Finally, the value may be conditionally assigned. Note, the logical
		expression follows the value assignment and is preceded by
		a 'if' tag:
			a $increment=$increment+1 if($gener)=(male)

		The math value is returned as a Double and converted to a string,
		retaining 4 decimal places. 
		Script system 
			1.)  Removes spaces and tabs from math expression
			2.)  Indicates whether or not source is a match expression
				   of just a "simple" assignment of one value. 

*/
package com.client
import scala.collection.mutable.Map

case class Assigner(var symbolTable:Map[String,String]) extends Linker  {
													/*
				Linker
					def reset
					def iterate
					def Value
				Linker extends Node
	symbolTable holds $<variables>	def setId
					def convertToSibling
					def convertToChild
*/
//-------------paramerters from .struct file--------
	var target=""
	var source=""
	var special=""
	var conditionStruct=""

//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			}
//-------------------------------------------------------------------
	val variableRegex= """([a-zA-Z_])""" .r

	def startAssigner= {
	    val goOn=if(conditionStruct != "0") 
					LogicTest.logicTest(conditionStruct, symbolTable)
			   else true
			// Execute if condition is true or if condition does not exist
		if(goOn) {
			if(special=="simple") {  //source is a single value, e.g.,  3.14
				if(isVariable(source)) // detect '$'
					source=retrieveValueFromSymbolTable(source, symbolTable)
					addFieldToSymbolTable(target, source, symbolTable)
					}
				else 
					//source is, e.g., (rate+2)/4^tax
					mathExpression(target,source, symbolTable)
				false
				}
		}
	def isVariable(variable:String)= { variable(0)=='$'  }
	def retrieveValueFromSymbolTable(source:String, 
					 table:Map[String,String])={
		table.get(source) match {
			case Some(value)=> value
			case None=> "uknwn"
			}
		}
	def addFieldToSymbolTable(key:String, value:String, table:Map[String,String]) {
		table += (key -> value)
		//println("Assigner: addFieldToSymbolTable:key="+key+"   input="+value)
		}
	def mathExpression(key:String,expr: String, symbolTable:Map[String,String])  {
			// converts expression string to List[String]
		val t =new Tokener 
		val list=t.extract(expr)
			// Recursive-decent math expression parser
		val e=new Evaluator(list, symbolTable)
		val result=e.evaluate.toString  // double to string
		addFieldToSymbolTable(key,result, symbolTable)
		}
	//------------------------------------------------------
		// *.struct file delivers symbolic links and object parameters.
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
							case "target" => //println(pair(1) )
									target=pair(1)
							case "source" => //println(pair(1) )
									source=pair(1)
							case "special" => //println(pair(1) )
									special=pair(1)
							case "condition" => //println(pair(1))
									conditionStruct= pair(1)
							}
				}
			   }  //breakable		 
			 }
		}

/*
		val in=structSet.iterator
		setAddress(in.next)
		setNext(in.next)
		target=in.next		// Value is assigned to 'target'
		source=in.next		// Expression whose value is assigned to 'target'.
		special=in.next		// A one value assignment to 'target' is marked 'simple'.
					//     A math expression is marked ""
		conditionStruct=in.next // Address of the first 'Condition' object. 
		val percent=in.next
		//println("Assigner: percent="+percent)
*/
	}

