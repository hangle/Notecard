/* date:   Oct 26, 2011
							DISPLAY TEXT
	Child of RowerNode.  RowerNode invoked 'convertrowColumnToPixel(...)' and
	adds this object to the notePanel which invokes 'render(...).

	DisplayText is a child of CardSet.  When CardSet executes DisplayText.startDisplayText(..)
	then the 'x' and 'y' coordinates as pixels are computed by RowPlacement functions.
	These coordinates are used in 'render()' by 'setBounds()':
	 	Component.setBounds(x, y, visualObjectWidth(), visualObjectHeight());
	However, 'render()' is not invoked until just prior to the 'wait()'is issued  by
	CardSet, for example: 
		CardSet children has executed, now invoke the 'render()' methods
		// of DisplayText, DisplayVisual, and BoxField.
		showPanel(notePanel) // display panel content (paint, validate)
		haltCommandExecution(lock)//Stop (issue wait()) to allow the user to enter responses.
		clearNotePanel(notePanel)		//remove all components & clear screen
 
 	A 'd' command, such as:
			d now is (%% /size 24/for all) good men
	has two size value (24 and the default size [typically 16] for 'now is' and 'good men').
	The 'y' coordinates must be adjusted upwards for 'now is' and 'good men' .  
			(see:  y=adjustYyForSizeLessThanMax(yy:Int) )

*/ 

package com.client
import scala.collection.mutable.Map
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Color
import javax.swing._

case class DisplayText(var symbolTable:Map[String,String]) 
										extends JLabel with Node with Visual with VisualMetric {
		/*
					Node
	symbolTable holds $<variables>		def setId
						def convertToSibling
						def convertToChild
					Visual
						def render
					VisualMetric
						var metrics
						def establishMetrics
						def visualObjectHeight()
						dev visualObjectWidth()
			*/
//------------paramters pass by .struct file-----------------
	var styleFont=0
	var sizeFont=0
	var nameFont=""
	var duration=0
	var text=""
	var xcolor:Color=Color.green
//-------------computed parameters passed by .struct file--------------
	var metricWidth=0   // metric + text
	var metricHeight=0   // metric + text
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			}
//-------------------------------------------------------------------
				// RowerNode row value may jump incremen-by-one value, such as,
				// 'd /7/now is the time' specifies line on row '7'.
	var rowOffset=0
				// RowerNode invokes 'displayText.adjustedY=diff+rowPosition.priorYFromTop +2
				// 
	var adjustedY=0
				// The row of VisualObjects adds to this value
	var accumulatedX=0
				// RowerNode.column * DefaultFont.defaultPixelLetter()
	var startColumnX=0
				//  Get 'accumulatedX' from row RowPosition.currentPixelWidth 
				//  and then add text width to RowPosition's currentPixelWidth 
				//  for next  VisualObject. 
	def startDisplayText(rowPosition:RowPosition, startColumn:Int) {
				// RowerNode.startColumnX= (column) * defaultFont.defaultPixelLetter()
				// for example:  'd 10/text begins column 10' where RowNode.column=10
		startColumnX=startColumn
					// Line row may have multiple VisualObjects so prior text 
					// width is added for each VisualObject.
		accumulatedX=rowPosition.currentPixelWidth
					// Added for the  next VisualObject. 
					// 'metricWidth' is the conversion of 'text' variable to pixels. 
					// Added to RowPosition.currentPixelWidth
		rowPosition.sumToCurrentWidth(metricWidth)
		}
				// In NoteLayout, LayoutManager.layoutContainer iterates
               	// thru all components added to the notecard panel.
               	// This method invokes 'render() for all VisualObjects.
	def render() {
	     setForeground(xcolor)
       	 setText(text)
		 setBounds( startColumnX+accumulatedX, 
		 			adjustedY, 
					metricWidth, 
					metricHeight )
//		println("DisplayText: render()  adjustedY="+adjustedY+"   metricHeight="+metricHeight)
		}
		// CreateClass generates instances of DisplayText without fields or parameters.
		// However, it invokes 'receive_objects' to load parameters from *.struct
		// file as well as symbolic addresses to be physical ones. 
	def  receive_objects(structSet:List[String] ) {
			import util.control.Breaks._
			var flag=true
			for( e <- structSet) {
			  breakable { if(e=="%%") break   // end of arguments
			  else {
				var pair=e.split("[\t]")	
				pair(0) match {
							case "address" => //println(pair(1))
									setAddress(pair(1))
							case "sibling" =>
									setNext(pair(1))
							case "style" => //println(pair(1) )
									styleFont=pair(1).toInt
							case "size" => //println(pair(1) )
									sizeFont=pair(1).toInt
							case "column" => //println(pair(1) )
									duration=pair(1).toInt
							case "name" => //println(pair(1))
									nameFont= pair(1)
							case "text"=> //println(pair(1))
									text=pair(1)
							case "color"=> //println(pair(1))
									xcolor=Paint.setColor(pair(1))
							}
				}
			   }  //breakable		 
			 metrics=establishMetrics(nameFont, styleFont, sizeFont)
			 metricWidth= visualObjectWidth(text)  // VisualMetric trait
			 metricHeight=visualObjectHeight() // VisualMetric trait
			 }
		}
	}

