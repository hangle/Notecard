/* date:   Nov 18, 2011

	Determine whether or not isNumber's argument
	is a string number.
*/
package com.client

object VString  {

	def isNumber(s:String) ={
//		val isDigits="""([-+]+||[0-9.]+)""" .r 
		val isDigits="""([-+]?[0-9.]+)""" .r 
				//println("VString: s="+s)
		s match {
			case isDigits(x)=> 
				//println("\ttrue")
				true
			case _=> 
				//println("\tfalse")
				false
		} }
}
