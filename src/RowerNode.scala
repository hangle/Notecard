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

	RowerNode handles a line with different word sizes, for example,
		d (%%/size 6/now) (%%/size 8/is) (%%/size 10/the) (%%/size 12/time) 
												(%%/size 16/for) (%%/size 18)
	

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
//-------------------------------------scala fsc------------------------------
	def startRowerNode(
			rowPosition:RowPosition, // converts row values (1,2,3,4,...) to pixels.
			notePanel:JPanel, 		// window where lines are displayed/
			inputFocus:InputFocus,  // passed to KeyListenerObject
			statusLine:StatusLine,	// passed to KeyListenerObject. message to client (optional)
			defaultFont:DefaultFont, // used for start column
			listenerArray:ArrayBuffer[KeyListenerObject] //array of KeyListenerObjects
			) {
				// The 'column' value of 5, for example, 'd 5/text', specifies that 'text' begins in column 5. 
		val startColumnX= (column) * defaultFont.defaultPixelLetter()
		println("RowerNode startColumnX="+startColumnX+" column="+column+"  defaultLetter="+defaultFont.defaultPixelLetter() )
				// Used to access 'row' and 'DefaultFont.defaultHeight()'
		rowPosition.getRowerNode(row, defaultFont.defaultHeight())
				// Iterate each RowerNode Visual object.
				// For the current Row, find the largest height value of all 'd' 
				// command Visual components. 
		val maxHeight= maxHeightValuesOfVisualObjects()

				//  sets CurrentWidth of RowPosition
				//	resolveColumnRowPosiscala fsction(rowPosition, column, row)
				//  execute DisplayText, DisplayVariable, BoxField
		iterateRowerNodeChildren(maxHeight,
								 rowPosition, 
								 notePanel, 
					 			 inputFocus, 
					 			 statusLine, 
								 startColumnX,
					 			 listenerArray)
			// Visual material has been displayed, so initialized RowPosition values

			// for next VisualObject.
		rowPosition.resetCurrentWidth()
		rowPosition.addToPriorYFromTop(maxHeight)
		}
	def iterateRowerNodeChildren(maxHeight:Int,
								 rowPosition:RowPosition, 
								 notePanel:JPanel, 
								 inputFocus:InputFocus, 
								 statusLine:StatusLine,
								 startColumnX:Int,
								 listenerArray:ArrayBuffer[KeyListenerObject]) {
				// Iterate each RowerNode Visual object.
				//'maxHeight' is subtracted from the height  of the VisualObject and 
				// assigned to 'adjustetY' values of the VisualObject.
		assignAdjustedHeightInEachVidualObject( maxHeight, 
												notePanel,
												inputFocus,
												statusLine,
												rowPosition,
												startColumnX,
												listenerArray) 
		}
		// A 'd' cmd line may consist of multiple text and input Visual objects.
		// Find the largest height (size) value in pixels among the 'd' command's
		// Visual objects. Assign 'maxHeight' to every line component
		// via Visual object's 'maxHeight'.
	def maxHeightValuesOfVisualObjects()= {
		var maxHeight=0
		reset(child)//point to head of linked list (setFirstChild)
		while(iterate) {   //Linker processes linked list of visual display components
			node match {
				case dt:DisplayText=>
						val height=dt.visualObjectHeight()
						if(height > maxHeight) maxHeight=height
				case dv:DisplayVariable=>
						val height=dv.visualObjectHeight()

						if(height > maxHeight) maxHeight=height
				case bf:BoxField=>
						val height=bf.visualObjectHeight()
						if(height > maxHeight) maxHeight=height
				case _=> //println("RowerNode: maxHeight..()  case _=>")
				}
			}
		maxHeight
		}
		//'maxHeight' of maxHeightValuesOfVisualObject(..) is subtracted from the height
		// of the VisualObject. If maxHeight equals metricHeight, then 'diff' is zero.
	def assignAdjustedHeightInEachVidualObject( maxHeight:Int, 
												notePanel:JPanel,
												inputFocus:InputFocus,
												statusLine:StatusLine,
												rowPosition:RowPosition,
												startColumnX:Int,
												listenerArray:ArrayBuffer[KeyListenerObject]) { 
		reset(child)
			// Every Visual object of the 'd' command  will have 'maxHeight

		while(iterate) {
			var diff=0
			node match {
				case dt:DisplayText=>
							//  Subtract metricHeight from maximum height. Add difference to
							//  to accumulated 'priorYFromTop' assigned to adjustedY.
			  	//		adjustmentHeightForText(dt, maxHeight, rowPosition.priorYFromTop) 
			  			adjustmentHeightForText(dt, maxHeight, rowPosition) 
							//  Get 'accumulatedX' from row RowPosition.currentPixelWidth and
							//  then add text width to RowPosition's currentPixelWidth for next
							//  VisualObject. 
						dt.startDisplayText(rowPosition, startColumnX)
							//  VisualObject added to Component. 
						notePanel.add(dt)			
				case dv:DisplayVariable=>
							//  Subtract metricHeight from maximum height. Add difference to
							//  to accumulated 'priorYFromTop' assigned to adjustedY.
			  			adjustmentHeightForVariable(dv, maxHeight, rowPosition) 
							//  Get 'accumulatedX' from row RowPosition.currentPixelWidth and
							//  then add text width to RowPosition's currentPixelWidth for next
							//  VisualObject. 
						dv.startDisplayVariable(rowPosition, startColumnX)
							//  VisualObject added to Component. 
						notePanel.add(dv)			

				case bf:BoxField=>
							//  to accumulated 'priorYFromTop' assigned to adjustedY.
			  		//	adjustmentHeightForBox(bf, maxHeight, rowPosition.priorYFromTop) 
			  			adjustmentHeightForBox(bf, maxHeight, rowPosition) 
							//  Get 'accumuscala fsclatedX' from row RowPosition.currentPixelWidth and

							//  then add text width to RowPosition's currentPixelWidth for next
							//  VisualObject. 
						bf.startBoxField(rowPosition, startColumnX)
							//  VisualObject added to Component. 
						notePanel.add(bf)			
						createListenerAndFocus(bf, inputFocus,  statusLine, listenerArray)

				case _=> //println("RowerNode: maxHeight..()  case _=>")

				}
			}
		}
			// compute the difference in height between the metricHeight (default height)
			// and the maximun height of the row's VisualObjects. 
	def adjustmentHeightForText(  displayText:DisplayText, 
									maxHeight:Int,
								//	priorYFromTop:Int) {
									rowPosition:RowPosition) {
		var diff=0
		val height=displayText.metricHeight
		if(height < maxHeight) 
			diff=maxHeight-height 
//		println("RowerNode: displayText diff="+diff)
			
			// '2' added because BoxField not aligned with DisplayText or DisplayVariable
		displayText.adjustedY=diff+rowPosition.priorYFromTop +2
		}
			// compute the difference in height between the metricHeight (default height)
			// and the maximun height of the row's VisualObjects. 
	def adjustmentHeightForVariable(displayVariable:DisplayVariable, 
									maxHeight:Int,
									rowPosition:RowPosition) {
		var diff=0
		val height=displayVariable.metricHeight
		if(height < maxHeight) 
			diff=maxHeight-height 
//		println("RowerNode: variableText diff="+diff)
			
			// '2' added because BoxField not aligned with DisplayText or DisplayVariable
		displayVariable.adjustedY=diff+rowPosition.priorYFromTop +2
		}
			// compute the difference in height between the metricHeight (default height)
			// and the maximun height of the row's VisualObjects. 
	def adjustmentHeightForBox( boxField:BoxField, 
								maxHeight:Int,
								rowPosition:RowPosition) {
		var diff=0
		val height=boxField.metricHeight
		if(height < maxHeight) 
			diff=maxHeight-height
//		println("RowerNode: height="+height+" maxHeight="+maxHeight+"  boxField diff="+diff+" priorY..="+priorYFromTop)
		boxField.adjustedY=diff+rowPosition.priorYFromTop 
		}


	def createListenerAndFocus( boxField:BoxField, 
				inputFocus:InputFocus, 
				statusLine:StatusLine,
				listenerArray:ArrayBuffer[KeyListenerObject]) {
				// KeyListenerObject "listens" for key input characters. 

		val keyListenerObject=new KeyListenerObject(boxField,  

													inputFocus, 
													statusLine)

				// added to an ArrayBuffer of InputFocus
				// 'components += component (boxField)
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
									if(column != 0) column -= 1
									//println("RowerNode: receive_ob... column="+column)

							}
				}
			   }  //breakable		 
			  }
	
		}
}

