/* date:   Oct 26, 2011
			ROWER NODE
	A Card set having 5 Display commands will have 5 RowerNode objects,
	that is, each display line (d cmd) is associated with a RowerNode 
	object.  

	A Display line may have multiple components.
	Example:
			d 5/2/color red/ (% $name) enter age (# $age) and (# $sex)
	RowerNode is the parent of the following 'Visual' (shown in the 
	notePanel) children:
			DisplayText,	// "enter",  "age", "and"
			BoxField		//  $age, $sex
			DisplayVariable // $name
			ListBoxNode  (not operational)

	A card line consists of one or more children or visual components. 
	A line has a 'notePanel' row position and starting column
	position. The <display cmd>, e.g., 'd 15/3/hello' specifies the
	'row' and 'column' parameters.  When neither parameter is specified,
	then the RowPosition object determines the row/column positions.
*/ 
package com.client
import scala.collection.mutable.Map
import collection.mutable.ArrayBuffer
import javax.swing._

case class RowerNode(var symbolTable:Map[String,String]) extends  Linker {
													/*
			Linker
				def reset
				def iterate
				def Value
			Linker extends Node
				def setId
				def convertToSibling
				def convertToChild
	*/
//------------paramters pass by .struct file-----------------
	var row=0
	var column=0
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node]) ={
			convertToSibling(swizzleTable)
			convertToChild(swizzleTable)  // Is a parent
			}
//-------------------------------------------------------------------
	def startRowerNode(
			rowPosition:RowPosition, // converts row values (1,2,3,4,...) to pixels.
			notePanel:JPanel, 		// window where lines are displayed/
			inputFocus:InputFocus,  // passed to KeyListenerObject
			indexer:Indexer,		// passed to KeyListenerObject
			statusLine:StatusLine,	// passed to KeyListenerObject. message to client (optional)
			listenerArray:ArrayBuffer[KeyListenerObject] //array of KeyListenerObjects
			) {

				// Find the largest height value of all 'd' command Visual
				// components. Insert largest value in each component.  
		val maxHeight= maxHeightValuesOfVisualObjects()
		rowPosition.loadRowAndColumn(row, column, maxHeight)	
				//  sets CurrentWidth of RowPosition
				//	resolveColumnRowPosition(rowPosition, column, row)
				//  execute DisplayText, DisplayVariable, BoxField
		iterateRowerNodeChildren(rowPosition, 
					 notePanel, 
					 inputFocus, 
					 indexer, 
					 statusLine, 
					 listenerArray)
			//The visual components of the 'd' command my specify different height
			//values.  'maxHeightValuesOfVisualObjects' finds the maximum height.
			// 'currentHeight' is incremented after each 'd' command executes. 
		rowPosition.sumMaxHeightToCurrentHeight(maxHeight)
			// initialized for next 'd' command line. 
		rowPosition.resetCurrentWidth()
		}
	def iterateRowerNodeChildren(rowPosition:RowPosition, 
				 notePanel:JPanel, 
				 inputFocus:InputFocus, 
				 indexer:Indexer,
				 statusLine:StatusLine,
				 listenerArray:ArrayBuffer[KeyListenerObject]) {
		reset(child)	//point to head of linked list (setFirstChild)
		while(iterate) {   	//Linker processes linked list of CardSet children
			executeRowerNodeChildren(node, // node references a particular sibling, 
							// such as DisplayText 
						 rowPosition, 
						 notePanel, 
						 inputFocus, 
						 indexer, 
						 statusLine , 
						 listenerArray)
			}
		}
	def executeRowerNodeChildren(obj:Node,  
				 rowPosition:RowPosition, 
				 notePanel:JPanel, 
				 inputFocus:InputFocus, 
				 indexer:Indexer,
				 statusLine:StatusLine,
				 listenerArray:ArrayBuffer[KeyListenerObject]) {
		obj match{
			case dt:DisplayText=>
			//println("RowerNode: case dt:DisplayText=>")
				dt.startDisplayText(rowPosition)
				notePanel.add(dt)			
			case dv:DisplayVariable=>
				dv.startDisplayVariable(rowPosition)
				notePanel.add(dv)
			case bf:BoxField=>
				bf.startBoxField(rowPosition)
				notePanel.add(bf) //refresh panel before creating listeners or incur focus problems
				createListenerAndFocus(bf, inputFocus, indexer:Indexer, statusLine, listenerArray)
			case _=>
				println("RowerNode:  unkown RowerNode child")
			 	throw new Exception
			}
		}

		// Find the largest height (size) value in pixels among the 'd' command's
		// Visual objects. 
	def maxHeightValuesOfVisualObjects()= {
		var maxValue=0
		//reset(getFirstChild)//point to head of linked list (setFirstChild)
		reset(child)//point to head of linked list (setFirstChild)
		while(iterate) {   //Linker processes linked list of CardSet children
			node match {
				case dt:DisplayText=>
					if (maxValue < dt.local_getMetricsHeight() )
						maxValue=dt.local_getMetricsHeight()
				case dv:DisplayVariable=>
					if (maxValue < dv.local_getMetricsHeight() )
						maxValue=dv.local_getMetricsHeight()
				case bf:BoxField=>
					if (maxValue < bf.local_getMetricsHeight() )
						maxValue=bf.local_getMetricsHeight()
				case _=> //println("RowerNode: maxHeight..()  case _=>")
				}
			}
		reset(child)
			// Every Visual object of the 'd' command  will have 'maxHeight
		while(iterate) {
			node match {
				case dt:DisplayText=>
						dt.maxHeight=maxValue
				case dv:DisplayVariable=>
						dv.maxHeight=maxValue
				case bf:BoxField=>
						bf.maxHeight=maxValue
				case _=> //println("RowerNode: maxHeight..()  case _=>")
				}
			}
		maxValue
		}

	def createListenerAndFocus( boxField:BoxField, 
				inputFocus:InputFocus, 
				indexer:Indexer, 
				statusLine:StatusLine,
				listenerArray:ArrayBuffer[KeyListenerObject]) {
		indexer.increment   // add one to index member of Indexer
				// KeyListenerObject "listens" for key input characters. 
		val keyListenerObject=new KeyListenerObject(boxField,  
													inputFocus, 
													indexer.getIndex, 
													statusLine)
				// added to an ArrayBuffer of InputFocus
		inputFocus.addToArray(boxField) 
				// ListenerArray used in CardSet to remove 
				// listeners on card termination
		listenerArray += keyListenerObject
		}
		// CreateClass generates instances of RowerNode without fields or parameters.
		// However, it invokes 'receive_objects' to load parameters from *.struct
		// file as well as symbolic addresses to be made physical ones. 
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
							case "address" => //println(pair(1))
									setAddress(pair(1))
							case "sibling" =>
									setNext(pair(1))
							case "row" => //println(pair(1) )
									row=pair(1).toInt
							case "column" => //println(pair(1))
									column= pair(1).toInt
							}
				}
			   }  //breakable		 
			  }
	
/*
		val in=structSet.iterator
		setChild(in.next)	
		setAddress(in.next)
		setNext(in.next)
		row=in.next.toInt
		column=in.next.toInt
		 val percent= in.next
		 //println("RowerNode: percent="+percent)
*/
		}
}

