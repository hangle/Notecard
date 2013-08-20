/* date:   Jun 16, 2012
   
			Recursive-Descent Expression Parser

	Code is based on H. Schildt and J. Holmes 'The Art of Java'
	routine to evaluate math expressions, such as '1+4/2'.
	The term 'Descent' denotes a hierarchy of functions beginning
	with:
			evaluate
			addSubstract
			multiplyDivide
			exponent 	
			unary		(not operational)
			parentheses	
			atom
	
	A List[String] contains the math expression,  each element of the
	list is either a number, variable, operator, or a parenthesis.
	The parser 'getToken' extracts the head element, and reassigns
	the tail element to the list. 

	When the expression '1+4/2' is passed to 'evaluate', the 'parser'
	extracts '1' (getToken) and holds it (token). Next, 
	'addSubstract' is invoked and immediately invokes 'multiplyDivide'.  
	'multiplyDivide' invokes 'exponent', and so, until 'atom' is invoked.
	'atom' assigns the value '1', held by 'token', to 'result'. The 
	'result' is returned by'atom'. 

	The calling stack begins to unwind.  Each function in the hierarchy
	returns '1' to the local variable 'result' of each function. Note
	in function 'atom', after the held '1' is released by 'token',
	'getToken' is invoked.  Now, 'token' holds '+'.

	As the stack unwinds, each function has access to '1' and '+'.  If 
	a function does not recognized the operator '+', then it returns
	'1' to the calling function. Finally, the 'addSubstract' recognizes
	this operator:
			while(token == "+" || token == "-") 
	
	Within the while loop, 'getToken' extracts '4'
	and invokes 'multiplyDivide' causing the process to decend to 'atom'.
	Now, 'atom' assigns '4' to 'result' and invokes 'getToken' extracting
	the operator '*'.  The stack again unwinds with '4' and '*', however,
	the process does not make its way back to 'addSubstract' because
	the operator '*' is recognized by 'multiplyDivide' (note: the '4' is
	held by 'result'):

			while(token =='*' || token =="/")
	
	Within the while loop of 'multiplyDivide', the parser extracts 2
	and invokes 'exponent', decending to 'atom'.   The stack unwinds
	with '2' and 'Nil' (end of expression), when it reaches 'multiplyDivide',
	then the '2' is divided into the local variable 'result', holding '4'.
	'result' now equals '2'.  The while loop terminates due to 'token' holding 
	'Nil' and '2' returns to the calling function 'addSubstract'. 
	
	Recall that 'result' value of 'addSubstract' held '1' when its first
	call to 'multiplyDivide' returned. Now the second call to 'multiplyDivide'
	returns '2'.  The two values are added, assigned to 'result', terminating
	the loop, and returning 'result' of '3' to 'evaluate'. 

	The routine respects the precedence of the math operators, for example,
	division has a higher precedence that addition.  In the example,
	'1+4/2' the '1' is not added until the '4' is divided by '2'
*/
package com.client
import scala.collection.mutable.Map

class Evaluator(var l:List[String], symbolTable:Map[String,String])  { 
	println("Evaluator instantiated")
	var token=""
	val variableRegex= """([a-zA-Z_])""" .r

					// Assigner's entry routine 
	def evaluate:Double={
		println("Evaluator ")
		getToken	// extracts the 1st element from list
		addSubtract // resolve element having the lowest precedence
		}
					// Extracts math element from 
					// expression list
	def getToken{
		if(isNotEmpty) {
			token=l.head
			l=l.tail
			}
				}
	def isNotEmpty= { ! l.isEmpty}
	def isVariable(variable:String)= {
		variableRegex.findFirstIn(variable) match {
			case Some(x)=> true
			case None => false
			}}
	def addSubtract:Double={
		var result=multiplyDivide
		while(token == "+" || token == "-") {
			val op=token
			getToken
			op match { 
				case "+"=> result=result + multiplyDivide
				case "-"=> result=result - multiplyDivide
				}
			}
		result
		}
	def multiplyDivide:Double={
		var result=exponent
		while(token == "*" || token == "/") {
			val op=token
			getToken
			op match { 
				case "*"=> result=result * exponent
				case "/"=> result=result / exponent
				}
			}
		result
		}	
	def exponent:Double= {
		var squareValue=1.0
		var result=unary
		if(token=="^") {
			getToken
			val partialResult= unary
			for(z <- 1 to partialResult.toInt)
				squareValue=squareValue *result 
			result=squareValue
			}
		result
		}
	def unary:Double ={
		parentheses
		}
	def parentheses={
		var result=0.0
		if(token =="(" ) {
			getToken
			result=addSubtract
			getToken
			}
		else
			result=atom
		result
		}
	def atom:Double ={
		var number=""
		val value=token
						// value having [a-zA-Z_] are variables
		if(isVariable(value) ){
			println("is variable") 
			number=symbolTable(value)
			}
			else number=value
		getToken
		number.toDouble
		}
}
