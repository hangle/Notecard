/* date:   Nov 13, 2011
						ROW POSITION
		Instantiated by CardSet.startCardSet(..) but CardSet does not invoke
		its functions. CardSet passes it to RowerNode.startRowerNode(..).   
		As lines of text are displayed, the RowPosition object sums the height 
		of each row in order to place the next row
		relative to the row one. 

		Script program codes row 1 as 0, row 2 as 1, row 3 as 2, and so on.

		Classes (DisplayText, DisplayVariable, BoxField) having the 
		trait 'VisualMetric' define a VisualObject. 

		RowPosition accesses each RowerNode via 'startRowerNode(..)'.
		At the end of RowerNode's execution of its VisualObjects:
				rowPosition.resetCurrentWidth() //adjusted for each VisualObject.
				rowPosition.addToPriorYFromTop(maxHeight)//Y of next RowerNode
		are invoked for the next RowerNode. 

*/
package com.client
import java.awt._
import javax.swing._

//class RowPosition (defaultFont:DefaultFont)  {
class RowPosition  {

		// 
	var priorRow=0
		// VisualObject's 'column' variable will alter this value.
	var startingColumnWidth=0
		// 'd (%% now is) the time (# $abc)' have three components. Each
		// is displayed separately.  'currentPixelWidth' keeps track of where
		// on the line that the next component begins. 
	var currentPixelWidth=5;	// updated by 'setCurrentPixelWidth(..)'
		// End of 'iterateRowerNodeChildren(..), maxHeight is assigned to priorYFromTop to
		// begin next row's pixel distance from window's top. 
	var priorYFromTop=0
		//Invoked in RowerNode after its VisualObjects have been 
		//processed(iterated). 
	def addToPriorYFromTop(maxHeight:Int) { priorYFromTop+= maxHeight}
		// Invoked by RowerNode.startRowerNode(..) following iteration of all VisualObjects.
	def resetCurrentWidth() { currentPixelWidth=5; }
		//When row has multiple visual components, then component
		//width values are added to 'currentPixelWidth'.
		//Visual.local_getMetricsWidth() determines the width
	def sumToCurrentWidth(value:Int) { currentPixelWidth += value; }
		// Invoked in RowerNode. When row==priorRow, then row was incremented by 1,
		// Handle differences greater or lesser than 0. Increment is > or < than 1.
		// For example:  c
		//				 d /3/first line begins row 3
	def getRowerNode(row:Int, defaultHeight:Int) {
		if(row > priorRow) {  // example 'd /3/...' 
			rowIncrease(row, defaultHeight)
			}
		if(row < priorRow) {
			rowDecrease(row, defaultHeight)
			}
		if(row==priorRow)
				priorRow +=1
		}
		// 		
	def rowIncrease(row:Int, defaultHeight:Int) {
		val rowDifference=row - priorRow
		val offset=rowDifference * defaultHeight
		priorYFromTop += offset
		priorRow=row 
		}
	def rowDecrease(row:Int, defaultHeight:Int) {
		val rowDifference=priorRow - row
		val offset=rowDifference * defaultHeight
		priorYFromTop -= offset
		priorRow -=row 
		}
	}


