/* date:  Nov 26, 2013
						LOAD DICTIONARY

	Child of Notecard and parent of LoadAssign. Note, LoadAssign is 
	actually an Assigner object.  

	Dictionary is 'symbolTable' containing $<variable> values. 

	The LoadDictionary is a special case.  It loads or assign values
	to the 'symbolTable' using the Assigner class.   In an '*.nc'
	script file, 

	Script example:
			l
			a $count=0
			c
			d (% $count)    // prints 0,1,2,3,4
			f nextFile ($count)=(5)

	The example script loops and prints the value of $count until
	count is "5", whereupon, 'nextFile.struct' executes.

	The LoadDictionary is envoked on each loop or iteration, however,
	its Assigner children are only processed during the first loop.

	It is processed on the first loop because 'startLoadDictionary'
	fails, in a match statement, to find its 'filename' parameter in 
	the SymbolTable. As such, it executes the LoadDictionary children
	(Assigner objects). It also loads 'filename' to the SymbolTable so
	that on subsequent invocations, its children objects are not
	processed.

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
//---o----------------------------------------------------------------
	def startLoadDictionary  {
				// retrieve key (filename)/value from symbol
				// table. Initially, get returns None
			symbolTable.get(filename) match {
					// Do nothing when get yields the paramter
					// filename from the Symbol table
				case Some(value) =>
					// Initial value
				case None =>
						// store key and value (0) in SymbolTable.
					symbolTable += filename-> "0"
						//Only executed one time
					iterateAssignerObjects
				}
			}
	def iterateAssignerObjects {
			reset(child) // 1st child or 1st sibling
			while( iterate) {
		//			println("LoadDictionary  iterate")
				node match {
						case as:Assigner=> as.startAssigner
						case _=> println("LoadDictionary:  unknown Value="+node)
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

