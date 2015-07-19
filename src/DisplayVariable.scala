/* date:   Dec 19, 2011
							DISPLAY VARIABLE
	Identical to DisplayText with the exception that a symbolTable value 
	is displayed. 
	Example of Display Variable command:
			d 5/3/Thank you (% $name)
	The expression '(% $name)' has the 'name' applied as the symbolTable
	key to display the value associated with this key. 
		
	In the example, the display of $name must be spaced beyond 'Thank you '.
	RowerNode parent invokes DisplayVariable.startDisplayVariable passing
	it the column position, via RowPosition, to begin the display of $name.  
	The column position will vary depending on the arguments read (receiveObject) 
	by this class:
		nameFont
		styleFont
		sizeFont
	The three values are passed to  establishMetrics() to enable calling of:
		getMetricsHeight
		getMetricsWidth

	The RowPosition object holds the current x,y pixel ending coordinates of,
	in the case of the example 'Thank you '.  getMetricsWidth is the pixel
	size of $name, and is passed back to RowPosition so that it can 
	communicate the new x,y coordinates to any material written after
	$name. 

	The render() method of this object displays the text material. This mehod 
	is not invoked by the object however.  In CardSet, validate() and
	repaint() of JComponent is invoked following execution of the Card
	Set commands. JComponent triggers render() of the DisplayVariable object.
	render() conveys the x,y pixel coordinates to JComponent. 

	
	DisplayVisual is a child of CardSet.  When CardSet executes DisplayVariable.startDisplayVariable(..)
	then the 'x' and 'y' coordinates as pixels are computed by RowPlacement functions.
	These coordinates are used in 'render()' by 'setBounds()':
	 	Component.setBounds(x, y, local_getMetricsWidth(), local_getMetricsHeight());
	However, 'render()' is not invoked until just prior to the 'wait()'is issued  by
	CardSet, for example: 
							// CardSet children has executed, now invoke the 'render()' methods
							// of DisplayText, DisplayVisual, and BoxField.
				showPanel(notePanel) // display panel content (paint, validate)
				haltCommandExecution(lock)//Stop (issue wait()) to allow the user to enter responses.
				clearNotePanel(notePanel)		//remove all components & clear screen
 
 	A 'd' command, such as:
			d now is (%% /size 24/for all) good men
	has two size value (24 and the default size [typically 16] for 'now is' and 'good men').

*/ 

package com.client
import scala.collection.mutable.Map
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Color
import javax.swing._

case class DisplayVariable(var symbolTable:Map[String,String]) 
						extends JLabel with Node  with Visual with VisualMetric{
	/*
				Node
symbolTable holds $<variables>		def setId
					def convertToSibling
					def convertToChild
					def convertToCondition
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
	var column="0"		// not used ??	
	var dollarVariable=""
	var xcolor:Color=Color.green
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node]) ={
			convertToSibling(swizzleTable)
			}

//-------------------------------------------------------------------
	var symbolTableText=""
	var xx=0   //set by RowPosition
	var yy=0   //set by RowPosition
		// Assigned in RowerNode by visiting each Visual component
		// of the 'd' command and finding the one with the 
		// greatest height value. 
	//var greatestHeight= rowPosition.greatestHeight
    //var defaultHeight=  rowPosition.defaultHeight
	var defaultHeight=0
	var greatestHeight=0
	var maxHeight=0
		//invoked by RowerNode which has the row position
		//as well as the starting column position.
	def startDisplayVariable(rowPosition:RowPosition) {
			// the value (getCurrentHeight()) is actually the 'y' axis 
			// position of the next line, so subtract the height
			// value of the current line. 
		yy=rowPosition.yCoordinate
		xx=rowPosition.currentPixelWidth
		greatestHeight= rowPosition.greatestHeight
		defaultHeight=  rowPosition.defaultHeight

			// computes the metric width of the text string so as
			//extract $<variable> value
		symbolTableText=convertVariableToText(dollarVariable)
			// to adjust row position for next display component
		rowPosition.sumToCurrentWidth(local_getMetricsWidth(symbolTableText))
		}
		//Access symbolTable to find variable <key>.
		//Returns value associated with key.
	def convertVariableToText(variable:String)= {
		if(symbolTable.contains(variable)) {
			symbolTable(variable)
			}
		  else
		  	"Unkwn("+variable+")"
		}
		// In NoteLayout, LayoutManager.layoutContainer iterates
		// thru all components added to the notecard panel.
		// This method invokes 'render() for all Visual objs.
	def render() {
		val maxHeight= local_getMetricsHeight()
		setForeground(xcolor)
		setText(symbolTableText)
		var y=yy    // in event that yy does not need an adjustment
		//println("DisplayVariable y="+y)

		 if (greatestHeight != maxHeight){ 		 
		 //	println("DisplayVariable greatestHeight="+greatestHeight+"   maxHeight="+maxHeight)
			y+= greatestHeight - maxHeight - 3 
			}

		// println("DisplayVariable: y="+y)
		setBounds(xx, y, local_getMetricsWidth(symbolTableText), maxHeight);
		}
		// CreateClass generates instances of NotecardTask without fields or parameters.
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
							case "address" => //println(pair(1))
									setAddress(pair(1))
							case "sibling" =>
									setNext(pair(1))
							case "style" => //println(pair(1) )
									styleFont=pair(1).toInt
							case "size" => //println(pair(1) )
									sizeFont=pair(1).toInt
							case "column" => //println(pair(1) )
									column=pair(1)
							case "name" => //println(pair(1))
									nameFont= pair(1)
							case "text"=> //println(pair(1))
									dollarVariable=pair(1)
							case "color"=> //println(pair(1))
									xcolor=Paint.setColor(pair(1))
							}
				}
			   }  //breakable		 
			 metrics=establishMetrics(nameFont, styleFont, sizeFont)
			 }

		}
/*
		val in=structSet.iterator
		setAddress(in.next)
		setNext(in.next)
		dollarVariable=in.next
		column=in.next
		sizeFont=in.next.toInt
		styleFont=in.next.toInt
		nameFont=in.next
		xcolor=Paint.setColor(in.next) //see Paint objectw
		metrics=establishMetrics(nameFont, styleFont, sizeFont)
		val percent= in. next
		//println("DisplayVariable: percent="+percent)
*/
	}

