/* date:   Nov 13, 2011
						ROW POSITION
		Instantiated by CardSet.startCardSet(..).   As lines of text
		are displayed, the RowPosition object sums the height of
		each row (currentPixelHeight) in order to place the next row
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
		// Number pixels from top of window.  First line 
		// height is 0.  The 1st line's font size is added
		// to 'currentPixelHeight' and so on for subsequent lines. 
	var currentPixelHeight=0; 
		// 'd (%% now is) the time (# $abc)' have three components. Each
		// is displayed separately.  'currentPixelWidth' keeps track of where
		// on the line that the next component begins. 
	var currentPixelWidth=5;	  // updated by 'setCurrentPixelWidth(..)'
		// 'd (%% /size 10/now is)(%% /size 20/ The time) has two text
		// components of differet size or height.  RowerNode finds the
		// larges size, in the case of this example, it is 20. 'maxRowHeight'
		// is added to 'currentPixelHeight'. 
	val pair= defaultRowHeightWidth(font) //establish metrics 
		// width of default font used to establish
		// a line's starting column position. 
	var pixelWidth= pair._1
		// Invoked by RowerNode.startRowerNode(..)
		// 'maxRowHeight' is the maximum height of the scan of all visial object of row
		// Note:  row and column arguments set by Script program
	def loadRowAndColumn(row:Int, column:Int, maxRowHeight:Int) {
		//println("RowPosition: maxRowHeight="+maxRowHeight+"  row="+row)
			//Convert column to currentPixelWidth (pixels)
//		currentPixelWidth=setCurrentPixelWidth(column)
		currentPixelWidth=pixelWidth * column
		//println("\tRowPosition:  currentPixelHeight="+maxRowHeight * row)
		currentPixelHeight= maxRowHeight * row
		}
				// invoke in RowerNode.startRowerNode by call to
		// RowPosition.invokeRowColumn(...).
		// example:  'd  15/now is' where routine converts
		// 15 to pixels.
/*
	def setCurrentPixelWidth( column:Int)= { 
		var value=0;
		pixelWidth * column + value
		}
*/
		//Invoked in RowerNode after its children have been
		//iterated. Reset for the next RowerNode instance
	def resetCurrentWidth() { currentPixelWidth=5; }
		//When row has multiple visual components, then component
		//width values are added to 'currentPixelWidth'.
		//Visual.local_getMetricsWidth() determines the width
	def sumToCurrentWidth(value:Int) { currentPixelWidth += value; }
		// Invoked when object is instantiated
	def defaultRowHeightWidth(font:Font):(Int,Int)={
		val label=new JLabel("text");  //"text" used to get fontMetrics
		label.setFont(font) 
		val fm:FontMetrics=label.getFontMetrics(font)
		(fm.charWidth(' '), fm.getHeight() )
		} 
	}

