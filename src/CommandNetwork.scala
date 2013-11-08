/*
	Invoked in:
		card.scala
		NotecardTask   (support of Management file)

	Reads <.struct> command file and creates a network
	of linked lists changing symbolic address to physical
	ones, and finally returning the root of the network. 
*/
package com.client
import scala.collection.mutable.Map

object CommandNetwork  {
		// Returns Notecard the root of the command hierarchy of 
		// linked lists.
	def loadFileAndBuildNetwork(fileName:String, symbolTable:Map[String,String])= {
			// Routines to extract filename and pathname
		val fp=new FilePort
			//Returns 'start.struct' if file not found. 
			//If 'argv' filename lacks a path, then the currentPath
			//is added to it, provided currentPath exists.
		val file=fp.establishFileName(fileName)  //FilePort object
			// Creates List[List[String]] where each element
			// is a card set
		val allCardSets= StructScript.structListList(file)
		//println("CommandNetwork:  allCardSets.size="+allCardSets.size)	
			// In CreateClass.  Command classes are instantiated and
			// symbolic addresses of linked lists nodes are
			// converted to physical addresses creating network of 
			// linked lists.
		val createClass= new CreateClass
		createClass.establishObjectNetwork(symbolTable, allCardSets)
		}
	}
	
