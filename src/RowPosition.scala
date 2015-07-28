/* date:   Nov 13, 2011
						ROW POSITION
		Instantiated by CardSet.startCardSet(..) but CardSet does not invoke
		its functions. CardSet passes it to RowerNode.startRowerNode(..).   
		As lines of text are displayed, the RowPosition object sums the height 
		of each row (yCoordinateFromTop) in order to place the next row
		relative to the row one. 

		Script program codes row 1 as 0, row 2 as 1, row 3 as 2, and so on.

		Classes (DisplayText, DisplayVariable, BoxField) having the 
		trait 'Visual' define a VisualObject. 
*/
package com.client
import java.awt._
import javax.swing._

class RowPosition (defaultFont:DefaultFont)  {
		// Number pixels from top of window.  First line 
		// height is 0.  The 1st line's font size is added
		// to 'yCoordinateFromTop' and so on for subsequent lines. 
	var yCoordinateFromTop=0; 
		// 'd (%% now is) the time (# $abc)' have three components. Each
		// is displayed separately.  'currentPixelWidth' keeps track of where
		// on the line that the next component begins. 
	var currentPixelWidth=5;	// updated by 'setCurrentPixelWidth(..)'
	var priorRow= 0
		// End of 'iterateRowerNodeChildren(..), maxHeight is assigned to priorYFromTop to
		// begin next row's pixel distance from window's top. 
	var priorYFromTop=0
		//Invoked in RowerNode after its children have been
		//iterated. Reset for the next RowerNode instance
	def resetCurrentWidth() { currentPixelWidth=5; }
		//When row has multiple visual components, then component
		//width values are added to 'currentPixelWidth'.
		//Visual.local_getMetricsWidth() determines the width
	def sumToCurrentWidth(value:Int) { currentPixelWidth += value; }
	}


