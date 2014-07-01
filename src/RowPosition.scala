/* date:   Nov 13, 2011
						ROW POSITION
		Instantiated by CardSet.startCardSet(..).   As lines of text
		are displayed, the RowPosition object sums the height of
		each row (currentHeight) in order to place the next row
		relative to the row one. 

  		Converts row values, e.g., 1,2,3, to pixel values. 
		Created by the CardSet object. It is used by the visual
		CardSet children to position the visual components
		in 'notePanel'

		Classes (DisplayText, DisplayVariable, BoxField) having the 
		trait 'Visual' define 'convertRowColumnPixal(..). The function
		invokes:
			xx=rowPosition.getCurrentWidth()
			yy=rowPosition.getCurrentHeight()
*/
package com.client
import java.awt._
import javax.swing._

class RowPosition (font:Font)  {
	var row=0		// row parameter of RowerNode 
	var column=0	// column parameter of RowerNode
					
		// width of default font used to establish
		// a line's starting column position. 
	var pixelWidth=0
		// Number pixels from top of window.  First line 
		// height is 0.  The 1st line's font size is added
		// to 'currentHeight' and so on for subsequent lines. 
	var currentHeight=0; 
		// 'd (%% now is) the time (# $abc)' have three components. Each
		// is displayed separately.  'currentWidth' keeps track of where
		// on the line that the next component begins. 
	var currentWidth=5;	  // updated by 'setCurrentPixelWidth(..)'
		// 'd (%% /size 10/now is)(%% /size 20/ The time) has two text
		// components of differet size or height.  RowerNode finds the
		// larges size, in the case of this example, it is 20. 'maxHeight'
		// is added to 'currentHeight'. 
	var maxHeight=0	           
	val pair= defaultRowHeightWidth(font) //establish metrics 
	pixelWidth= pair._1
		// Invoked by RowerNode.startRowerNode(..)
	def loadRowAndColumn(r:Int, c:Int, max:Int) {
		row=r 
		column=c
			//Convert column to currentWidth (pixels)
		setCurrentPixelWidth(column)
		setCurrentPixelHeight(row, max)
		}
	def setCurrentPixelHeight(row:Int, max:Int) {
		
		//println("RowPosition: setCurrentPixelHeight():  row="+row+"  maxHeight="+maxHeight)
//		val rowMaxHeight=row * maxHeight
		val rowMaxHeight=row * max
//		println("RowPosition: setCurrentP... row*max="+rowMaxHeight)
//		println("RowPosition: setCurrentP... row*max="+rowMaxHeight)
		currentHeight= rowMaxHeight
		}
		//Passed by 'convertRowColumnPosition' see Visual trait
		//to the 'yy' values of the visual components
	def getCurrentHeight()= {
				//println("RowPosition: getCurrentHeight()")
				currentHeight} 
		//Passed by 'convertRowColumnPosition' see Visual trait
		//to the 'xx'  values of the visual components
	def getCurrentWidth()={ currentWidth}
		// RowerNode finds the visual component with the  largest height 
		// (via 'Visual'.local_getMetricsHeight) and passes it as 'size'.  
		// Invoked by  RowerNode.iterateRowerNodeChildren(largestHeight).
		// after children have been iterated.
	def sumMaxHeightToCurrentHeight(maxSizePixel:Int) { 
		maxHeight= maxSizePixel 
		//println("RowPosition  sumMaxHeight..  row="+row+"   maxSizePixel="+maxSizePixel+"  maxHeight="+maxHeight)
		currentHeight += maxSizePixel
		//println("\tRowPosition currentHeight="+currentHeight)
		} 
		//Invoked in RowerNode after its children have been
		//iterated. Reset for the next RowerNode instance
	def resetCurrentWidth() { currentWidth=5; }
		//When row has multiple visual components, then component
		//width values are added to 'currentWidth'.
		//Visual.local_getMetricsWidth() determines the width
	def sumToCurrentWidth(value:Int) { currentWidth += value; }
		// Invoked when object is instantiated
	def defaultRowHeightWidth(font:Font):(Int,Int)={
		val label=new JLabel("text");  //"text" used to get fontMetrics
		label.setFont(font) 
		val fm:FontMetrics=label.getFontMetrics(font)
		(fm.charWidth(' '), fm.getHeight() )
		} 
		// invoke in RowerNode.startRowerNode by call to
		// RowPosition.invokeRowColumn(...).
		// example:  'd  15/now is' where routine converts
		// 15 to pixels.
	def setCurrentPixelWidth( column:Int) { 
			var value=0;
			if(column==0) value=5;   
			val pixelColumn= pixelWidth * column + value
			currentWidth=pixelColumn; 
			}
	}

