/* date:   Nov 13, 2011
						ROW POSITION
		Instantiated by CardSet.startCardSet(..).   As lines of text
		are displayed, the RowPosition object sums the height of
		each row (yCoordinate) in order to place the next row
		relative to the row one. 

		Script program codes row 1 as 0, row 2 as 1, row 3 as 2, and so on.

		Classes (DisplayText, DisplayVariable, BoxField) having the 
		trait 'Visual' define 'convertRowColumnPixal(..). The function
		invokes:
			xx=rowPosition.getCurrentWidth()
			yy=rowPosition.getCurrentHeight()
		RowPosition's 'yCoordinate' is assigned to 'yy' in visual classes.
		The first line of a CardSet, lacking a row position value, such as
		'd /5/row five',occupies a 'yCoordinate' of 0, the next line,
		again lacking a row position value, occupies a 'yCoordinate' that
		is typically the default pixel size. 
*/
package com.client
import java.awt._
import javax.swing._

class RowPosition (defaultFont:DefaultFont)  {
		// Number pixels from top of window.  First line 
		// height is 0.  The 1st line's font size is added
		// to 'yCoordinate' and so on for subsequent lines. 
	var yCoordinate=0; 
		// 'd (%% now is) the time (# $abc)' have three components. Each
		// is displayed separately.  'currentPixelWidth' keeps track of where
		// on the line that the next component begins. 
	var currentPixelWidth=5;	  // updated by 'setCurrentPixelWidth(..)'
		// uses JLabel("text") to extract default width and height.
	val pair= defaultRowHeightWidth(defaultFont) //establish metrics 
		// width of default font used to establish
		// a line's starting column position. 
	val pixelWidth= pair._1
	val defaultHeight= pair._2  
		// changed if maxHeight > defaultHeight, (greatestHeight=maxHeight)
		// passed to VisualMetric objects (e.g., DisplayText)
	var greatestHeight=defaultHeight  
	var priorMaxHeight=defaultHeight
	var priorRow= 0
	var priorY=0
		// Used by 'loadRowAndColumn(...)' in RowerNode object. Arg is 'maxHeight'.
		// if maxHeight <= to defaultHeight, then greatestHeight==defaultHeight,
		// otherwise it becomes equal to maxHeight
	def findGreatestHeight(maxHeight:Int, defaultHeight:Int)={
		if(maxHeight > defaultHeight)
			maxHeight
		  else
			defaultHeight
		}
		// Invoked by RowerNode.startRowerNode(..)
		// 'maxHeight' is the maximum height of the scan of all visial object of row
		// Note:  row and column arguments set by Script program. row 1 becomes row 0,
		// row 2 is 1, and so on. The row value is added/multiplied to a pixel height 
		// value (either defaultHeight or maxHeight). 
		// 'yCoorinate' sets the 'y' value in the visual objects (e.g., DisplayText').
		// The first 'd' command, with a pixel height 'greatestHeight', sets 'yCoordinate'
		// to zero. 'yCoordinate' is a cumulating value. When 'currentRow' is just one
		// more than the 'priorRow', then the prior row pixel heigiht increments the
		// 'yCoordinate' value. When the 'currentRow' is more than one more than the
		// 'priorRow', than the difference is multiplied by the prior row pixel height.
	def loadRowAndColumn(row:Int, column:Int, maxHeight:Int) {
		currentPixelWidth=pixelWidth * column
			// Is either 'maxHeidht' or 'defaultHeight' which ever is the larger.
		greatestHeight=findGreatestHeight(maxHeight, defaultHeight)
		if(row > priorRow)
			yCoordinate = incrementRow(row, priorRow, priorMaxHeight, priorY)
		  else
		  	yCoordinate= decrementRow(row, priorRow, defaultHeight)
				//println("RowPosition:  yCoordinate="+yCoordinate)
		priorY=yCoordinate
				//println("\t\tRowPosition:  priorY="+priorY+"   yCoordinate="+yCoordinate)
		priorMaxHeight = greatestHeight //findGreatestHeight(maxHeight, defaultHeight)
		priorRow=row
	//			println("rowPosition:       ......................")
	//			println("RowPosition:  row="+row+"  greatestHeight="+greatestHeight+"   defaultHeight="							+defaultHeight+"   priorMaxHeight="+priorMaxHeight)
		}
		// Alters the 'yCoordinate' for successive rows where 'currentRow > 'priorRow'.
		// When 'currentRow' is the first row, then 'yCoordinate' is assigned 0. 
		// When 'currentRow' is just one value more than the 'priorRow', the pixel height
		// of the prior row is added to 'yCoordinate'.
	def incrementRow(currentRow:Int, priorRow:Int, priorMaxHeight:Int, proirY:Int):Int={
			//				println("RowPosition: currentRow="+currentRow+" priorRow="+priorRow+
					//		"  priorMaxHeight="+priorMaxHeight+" proirY="+priorY)
		var adjustment=0
		if(currentRow==0) { 	// row 0
			adjustment=0
			}
		 else  
		 	if(currentRow==priorRow+1 ) {  // row 1 more than prior row
//				println("RowPosition:  step=1  currentRow="+currentRow+" priorRow="+priorRow+" priorMaxHeight="+priorMaxHeight)
	 			adjustment=priorMaxHeight
				}
		 else {					// 
			adjustment=defaultHeight * (currentRow-priorRow)
//			println("RowPosition: xxx")
			adjustment 
			}
		priorY + adjustment
		}
		// The currentRow is less than the priorRow, e.g., following screen placement on
		// the 10th row, the 'd' command, 'd /5/row 5' inserts test on the 5th row.
		// Note. 'decrementRow(...)' does not account that a prior row has a pixel size
		// greater/less than the 'defaultHeight'.
	def decrementRow(currentRow:Int, priorRow:Int, defaultHeight:Int):Int={
		//	println("RowPosition: --decrementRow..")
		defaultHeight * currentRow
		}

		// Elements may be larger or smaller than the default height
	def isMaxHeightEqualDefaultHeight(maxHeight:Int, defaultHeight:Int)= maxHeight == defaultHeight
		//Invoked in RowerNode after its children have been
		//iterated. Reset for the next RowerNode instance
	def resetCurrentWidth() { currentPixelWidth=5; }
		//When row has multiple visual components, then component
		//width values are added to 'currentPixelWidth'.
		//Visual.local_getMetricsWidth() determines the width
	def sumToCurrentWidth(value:Int) { currentPixelWidth += value; }
		// Invoked when object is instantiated
	def defaultRowHeightWidth(defaultFont:DefaultFont):(Int,Int)={
		val label=new JLabel("text");  //"text" used to get fontMetrics
	//	println("RowPosition:  defaultFont="+defaultFont)
		label.setFont(defaultFont) 
		val fm:FontMetrics=label.getFontMetrics(defaultFont)
		(fm.charWidth(' '), fm.getHeight()+4 )
		} 
	}


