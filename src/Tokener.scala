/* date:   Jun 16, 2012

	Parses math string expression, such as:
		one*2.4/.033+two
	The above string become a List[String]:
		List("one", "*", "2.4", "/", ".033", "+", "two")    
	where "one" and "two" are variables and "*", "/",
	and "+" are operators

	Colaborates with Assigner
*/
package com.client

class Tokener { 

	def isNumber(c:Char)={
		val Number="""([0-9.])""" .r
		Number findFirstIn c.toString  match {
			case Some(e)=> true
			case None=> false
			}
		}
	def isVariable(c:Char)={
		val Variable="""([$a-zA-Z_])""" .r
		Variable findFirstIn c.toString match {
			case Some(e)=> true
			case None=> false
			}
		}
	def isOperator(c:Char)={
		if(c=='*' || c=='/' || c=='+' || c=='-' ||c=='%' || c=='^') 
		     true
		else false }
	def isParenthesis(c:Char)= { if(c=='(' || c==')') true; else false }

		// Parses math expression, separating the match elements
		// into number, variables, operators, and parentheses. :
	def extract(expr:String) = {
		println("Tokener: extract() expr="+expr)
		var l=List[String]()
		var flag=false
		var buffer=new StringBuffer
		for(e <- expr) {
			if(isOperator(e)) {
				l=buffer.toString :: l
				l=e.toString :: l
				buffer=new StringBuffer
				}
			else if(isParenthesis(e)) {
					//test length in event an operator
					// preceded an parenthesis
				if(  buffer.length != 0) {
					l=buffer.toString :: l
					buffer=new StringBuffer }
				l=e.toString :: l
				}
			else if(isNumber(e)) {
				buffer.append(e)
				}
			else if(isVariable(e)) {
				buffer.append(e)
				}
			else  println("Tokener: unknown char="+e)
			}
		l=buffer.toString :: l 
		l.reverse
		}
}
			


