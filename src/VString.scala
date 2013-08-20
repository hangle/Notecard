/* date:   Nov 18, 2011

	Determine whether or not isNumber's argument
	is a string number.
*/
package com.client

object VString  extends App{

	def isNumber(s:String) ={
		val isDigits="""([0-9]+)""" .r 
		s match {
				case isDigits(x)=> true
				case _=> false
		} }
//	implicit def xxx(s:String)=new {def isNum= isNumber(s) }
/*
	println( isNumber("22"))
	println( isNumber("2x2"))
	println("5".isNum)
	println("x".isNum)
*/
}
