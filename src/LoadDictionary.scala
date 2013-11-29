/* date:  Nov 26, 2013
						LOAD DICTIONARY

	Child of Notecard and parent of LoadAssign. Note, LoadAssign is 
	actually an Assigner object.  

	Dictionary is 'symbolTable' containing $<variable> values. 

	The LoadDictionary is a special case.  It loads or assign values
	to the 'symbolTable' using the Assigner class.   In an '*.nc'
	script file, 
*/ 

package com.client
import scala.collection.mutable.Map

case class LoadDictionary(var symbolTable:Map[String,String]) extends Linker{
/*
				Linker
					def reset
					def iterate
					def Value
				Linker extends Node
*/
//------------paramters pass by .struct file-----------------
	var filename:String =""    // added to symbolTable to flag execution of object
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			//println("LoadDictionary  convertToChild")
			convertToChild(swizzleTable)  // Is a parent of Assigner
			}
//-------------------------------------------------------------------
	def startLoadDictionary  {
		symbolTable.get(filename) match {
				
				case Some(value) =>
								//Only executed one time
				case None =>
								// 
						symbolTable += filename-> "0"
						iterateAssignerObjects
				}
		}
	
	def iterateAssignerObjects {
			reset(getFirstChild)
			while( iterate) {
		//			println("LoadDictionary  iterate")
				Value match {
						case as:Assigner=> as.startAssigner
						case _=> println("LoadDictionary:  unknown Value="+Value)
						}
				}
			}
	def  receive_objects(structSet:List[String] ) {
		import util.control.Breaks._
			var flag=true
			for( e <- structSet) {
			  breakable { if(e=="%%") break   // end of arguments
			  else {
				var pair=e.split("[\t]")	
				pair(0) match {
							case "child" => //println(pair(1))
									setChild(pair(1))
							case "address" =>
									setAddress(pair(1))
							case "sibling" =>
									setNext(pair(1))
							case "filename" =>
										// "xyz" added to make name unique
									filename="xyz"+pair(1)
							case _=>
							}
				}
			   }  //breakable		 
			 }

		}
/*
		val in=structSet.iterator
		setChild(in.next)    // EditNode children	
	   	setAddress(in.next)
		setNext(in.next)	 // next Sibling
		field=in.next
		length=in.next.toInt
		column=in.next.toInt
		sizeFont=in.next.toInt
		styleFont=in.next.toInt
		nameFont=in.next
		ycolor=Paint.setColor(in.next) //see Paint object
		limit=in.next.toInt
		options=in.next.toInt

		metrics=establishMetrics(nameFont, styleFont, sizeFont)
		val percent= in.next
	//	println("BoxField: percent="+percent)
*/

	}

