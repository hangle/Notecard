/*		VISUAL METRIC			July 4, 2014

	Functions common to:
		DisplayText
		DisplayVisual
		BoxField
*/
package com.client
import java.awt.Font
import java.awt.FontMetrics
//import java.awt.Color
//import javax.swing._
import java.awt.Component

trait VisualMetric  extends Component {
		// DisplayText, DisplayVisual, and BoxField
		// 'receive_object(..) set 'metrics' by invoking 
		// metrics=establishMetrics(nameFont, styleFont, sizeFont)
	var metrics:FontMetrics=null
		//invoked by 'receive_objects(..)
	def establishMetrics(nameFont:String, 
						 styleFont:Int, 
						 sizeFont:Int)={
		val font=new Font(nameFont,styleFont, sizeFont)
		setFont(font)		
		getFontMetrics(font) 	// Toolkit method
		}
							//Note:  '4' has also been added to
							// 'getHeight() in RowPosition's
							// defaultRowHeightWidth().
	def local_getMetricsHeight()={ metrics.getHeight() +4 }
	def local_getMetricsWidth(text:String) ={ 
			metrics.stringWidth(text) +4 
			}
}
