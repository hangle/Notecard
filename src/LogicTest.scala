/* date:   Nov 2, 2012
								Collaborates with:  LogicType
   
	Performs a logic test on an conditional expression, such as:
		(1)=(2) 
	LogicToken creates a List[Logic] with one element type: Relation. 
	LogicType document describes how Relation's 'evaluate' returns 'false'
	in the case of 1==2.
	In the 'recurse' function, the 'while' statement will iterate once, 
	activating 'case r:Relation=>', passing 'current' (false) to 'resolve'.
	Two other values are passed to 'resolve':  'andOr' and 'running'.  These
	values are predefined: 'andOr='and' and 'running'='true'.  In this
	example, 'resolve' returns 'false'. If Relation's parameters with '1','=',
	'1', then 'true' is returned to 'running (the values returned by the
	function).  

	Extending the example to :  (1)=(2)or(3)=(3), the List[Logic] now has
	three elements whose types are Relation, AndOr, and Relation.  After
	the first Relation is processed, the second 'while' loop activated
	'case a:AndOr=> setting 'andOr' equal to 'or'.  The third and last
	'while' iteration passes 'or', 'false', and 'true' to 'resolve'. 

	The following is a partial example:
		((1)=(2)or(3)=(3))and((1)....))
	In List[Logic] has multiple elements of which the first type is
	OpenPrens which activates 'case o:OpenPren=> to invoke 'recurse'
	recursely to evaluate and resolve '(1)=(2)or(3)=(3)' to 'true'.
	The following element type is ClosedParen (')') ending the 
	termination of the 'while' iteration and forcing 'recurse' to
	return 'true' to 'popped'. 
	
	The remaining list elements are AndOr, OpenParen, ..., and finally,  
	ClosedParen. The AndOr object stores 'and', and the OpenParen objects invokes 
	'recurse' function.  The following types .... will be evaluated yielding

	in 'case o:OpenParen=> passing 'popped' to 'resolve' holding the parameter 
	values 'and' and 'true'(from the evaluation of: (1)=(2)or(3)=(3) )
*/
package com.client
import com.client.LogicType._
import scala.collection.mutable

object LogicTest  {

	var flag=true    	// Used in While loop
			// Perform logic test of condition expression, e.g., '(1)=(1)'
			// List[Logic] is provided by LogicType.
   	def logicTest(condition:String, symbolTable:mutable.Map[String,String]):Boolean= {
		val logicList=LogicType.logicTokener(condition)
		recurse(logicList,symbolTable)
		}

	def recurse(xlogicList:List[Logic],
				symbolTable:mutable.Map[String,String]): Boolean={
		var logicList= xlogicList
		var andOr="and"
		var running:Boolean=true // outcome of 'resolve'
		var current:Boolean=true // outcome of 'evaluate'
		var popped: Boolean=true // outcome returned by 'recurse'
				//println("LogicTest  recurse():  (should always be true) running="+running)
					// Returns 'list.head' of Logic list as well as shortens  this
					// list to list.tail
				def getToken={
						val token=logicList.head
						logicList=logicList.tail
						token
						}
					// Controls looping action of 'recurse' allowing 'recurse'
					// to break the loop (setting 'flag' to false). Also, the 
					// looping action terminates with end of list
				def isLogicList= { ! logicList.isEmpty && flag==true}	
		flag=true
			//loop while List not empty & flag==true
		while(isLogicList) {  
			getToken match {
				case r:Relationx=> 	// to evaluate
					current=r.evaluate(symbolTable)
					running=resolve(andOr, current, running)
				case a:AndOr=>		// to resolve
					andOr=a.andOr
					//println("Test2: AndOr=> running="+running)/g
				case o:OpenParen =>
					//println("OpenParen-------------")
					popped=recurse(logicList, symbolTable)
					running=resolve(andOr, popped, running)
					flag=true // its off, so turn on to keep looping 
				case g:CloseParen=>  	 // to pop
					//println("CloseParen............")
					flag=false // terminate while loop
				case _=> println("test: unknown");false
				}
			}
		running   // on return, the value of 'running' is assigned to 'popped'
		}
			// And or Or comparison of prior relation outcome and current 
			// relation outcome, e.g., '(1)=(1)and(1)=(2)' is false
	def resolve(andOr:String, current:Boolean, running:Boolean):Boolean={
		andOr match {
			case "and"=>
				current && running
			case "or"=>
				current || running
			}
		}
/*
def main(argv:Array[String]) {
	import SymbolTable._
	val table=SymbolTable.load
	val t=new LogicTokener
	var expr= ""
	expr="((axbc)=nc ns(xxab)and(1)=(3))"
	expr="((axbc)=nc ns(xxab)and(1)=(3))or(2)=(2)"
	expr="((axbc)=nc ns(xxab)and(1)=(3))or((eft)=(hig)and(33)=(44))"
	expr="(hugh)=ncns(hugh)"
	expr="((1)=(1)and(2)=(2))or((3)=(5)and(4)=(9))"  // true
	expr="((1)=(1)and(2)=(2))and((3)=(5)and(4)=(9))" // false
	expr="(1)=(1)and(2)=(2)and(3)=(5)or(4)=(5)"
	expr="((1)=(2)and(2)=(5)or(5)=(5))" // true
	expr="((3)=(5)and(4)=(9))or((1)=(1)and(2)=(1))or(5)=(5)" // true
	expr="((3)=(5)and(4)=(9))or((1)=(1)and(2)=(1))or(5)=(4)" //false
	expr="(1)=(1)and(2)=(2)and(3)=(3)"  // true
	expr="(1)=(1)and(2)=(2)and(3)=(5)"  // false
	expr="((1)=(6)or(2)=(2))and((3)=(5)or(4)=(4))"  // true
	expr="((1)=(6)or(2)=(2))and((3)=(5)or(4)=(7))"  // false
	expr="($one)=($two)"
	expr="($one)=(two)"
	expr="(02.2)<(2.3)and(9)>(12)"
	println("+++++++++++++expression="+expr+"=====================")
//	var r=t.extract(expr)
//	if(r.isEmpty) println("result is empty")
//	r.foreach(println)
	//println(" ---Logic---")
//	val rr= t.combine(r)
	//rr.foreach(println)
	//println("-------------")
	val rr=t.logicTokener(expr)
	val x=logicTest(rr, table)
	println("logicTest="+x)

	}
*/
}
