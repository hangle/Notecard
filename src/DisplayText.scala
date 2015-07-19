/* date:   Oct 26, 2011
							DISPLAY TEXT
	Child of RowerNode.  RowerNode invoked 'convertrowColumnToPixel(...)' and
	adds this object to the notePanel which invokes 'render(...).

	DisplayText is a child of CardSet.  When CardSet executes DisplayText.startDisplayText(..)
	then the 'x' and 'y' coordinates as pixels are computed by RowPlacement functions.
	These coordinates are used in 'render()' by 'setBounds()':
	 	Component.setBounds(x, y, local_getMetricsWidth(), local_getMetricsHeight());
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
						def local_getMetricsHeight()
						dev local_getMetricsWidth()
			*/
//------------paramters pass by .struct file-----------------
	var styleFont=0
	var sizeFont=0
	var nameFont=""
	var duration=0
	var text=""
	var xcolor:Color=Color.green
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			}
//-------------------------------------------------------------------
	var xx=0   //set by RowPosition
	var yy=0   //set by RowPosition
	var defaultHeight=0
	var greatestHeight=0
	var maxHeight=0
				// Assigned in RowerNode by visiting each Visual component
				// of the 'd' command and finding the one with the 
				// greatest height value. 
	def startDisplayText(rowPosition:RowPosition) {
				// the value (getCurrentHeight()) is actually the 'y' axis 
				// position of the next line, so subtract the height
				// value of the current line. 
			//	println("DisplayText: startDisplayText")
		yy=rowPosition.yCoordinate
		xx=rowPosition.currentPixelWidth
		greatestHeight= rowPosition.greatestHeight
		defaultHeight=  rowPosition.defaultHeight
				// computes the metric width of the text string so as
				// to adjust row position for next display component
		rowPosition.sumToCurrentWidth(local_getMetricsWidth(text))
		}
				// In NoteLayout, LayoutManager.layoutContainer iterates
               	// thru all components added to the notecard panel.
               	// This method invokes 'render() for all Visual objs.
	def render() {
		 val maxHeight=local_getMetricsHeight()

	     setForeground(xcolor)
       	 setText(text)
		 // println("DisplayText:  render()  text="+text)
		 var y=yy    // in event that yy does not need an adjustment
		 if (greatestHeight != maxHeight){ 
			y+= greatestHeight - maxHeight - 3 
			}
		setBounds(xx, y, local_getMetricsWidth(text), maxHeight )
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
			 }
		}
	}

