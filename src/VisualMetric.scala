/*		VISUAL METRIC			July 4, 2014

	Functions common to:
		DisplayText
		DisplayVisual
		BoxField
*/
package com.client
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Component

trait VisualMetric  extends Component {
		// DisplayText, DisplayVisual, and BoxField
		// 'receive_object(..) set 'metrics' by invoking 
		// metrics=establishMetrics(nameFont, styleFont, sizeFont)
	var metrics:FontMetrics= _
		//invoked by 'receive_objects(..)
	def establishMetrics(nameFont:String, 
						 styleFont:Int, 
						 sizeFont:Int):FontMetrics={
		val font=new Font(nameFont,styleFont, sizeFont)
		setFont(font)		
		getFontMetrics(font) 	// Toolkit method
		}
							//Note:  '4' has also been added to
							// 'getHeight() in RowPosition's
							// defaultRowHeightWidth().
			// used in RowerNode and in VisualObjects
	def visualObjectHeight()={ metrics.getHeight() +4 }
	def visualObjectWidth(text:String) ={ 
			metrics.stringWidth(text) +4 
			}
}
