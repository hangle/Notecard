/* date:   Nov 10, 2012
  						Used in:  LogicType 

*/
package com.client

object Comparison  extends App{

	def greaterThan(left:String, right:String, bothNumbers:Boolean):Boolean ={
		if(bothNumbers) 
			left.toDouble > right.toDouble
		else
			if(left.compareTo(right) > 0)
				true
			  else
				false }	
	def greaterThanOrEqual(left:String, right:String, bothNumbers:Boolean):Boolean ={
		if(bothNumbers) 
			left.toDouble >= right.toDouble
		else 	{
			val result= left.compareTo(right) 
			if(result >= 0) 
				true	
			  else
				false
			} }	
	def lessThan(left:String, right:String, bothNumbers:Boolean):Boolean ={
		if(bothNumbers) 
			left.toDouble < right.toDouble
		else
			if(left.compareTo(right) < 0)
				true
			  else
				false }	
	def lessThanOrEqual(left:String, right:String, bothNumbers:Boolean):Boolean ={
		if(bothNumbers) 
			left.toDouble <= right.toDouble
		else 	{
			val result= left.compareTo(right) 
			if(result <= 0) 
				true	
			  else
				false
			} }	
}
