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
				//println("Tokener: extract() expr="+expr)
		var l=List[String]()
		var flag=false
		var buffer=new StringBuffer
				//println("Tokener:  extract():  expr="+expr)
		val expr2=LogicType.removeSpacesInString(expr)
				//println("Tokener:  expr2="+expr2)
		for(e <- expr2) {
			if(isOperator(e)) {
						//println("Tokener:  operator e="+e)
					// Ensure element is valad, otherwise a specious blank element
					// is stored in list
				if(buffer.toString !="")
						l=buffer.toString :: l
							// println("Tokener: op buffer=|"+buffer.toString+"|")
				l=e.toString :: l
				buffer=new StringBuffer
				}
			else if(isParenthesis(e)) {
					//test length in event an operator
					// preceded an parenthesis
				if(  buffer.length != 0) {
					l=buffer.toString :: l
							// println("Tokener: ( buffer=|"+buffer.toString+"|")
					buffer=new StringBuffer 
					}
				l=e.toString :: l
				}
			else if(isNumber(e)) {
						//println("Tokener: isNumber  e="+e)
				buffer.append(e)
				}
			else if(isVariable(e)) {
				buffer.append(e)
				}
			else  println("Tokener: unknown char=|"+e+"|")
			}
		l=buffer.toString :: l 
		l.reverse
		}
}
			


