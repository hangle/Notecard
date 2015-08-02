/* Jul 13, 2014
				DefaultFont
	Created in Notecard from its parameters of name, style, size.
	and passed  to RowerNode.  The metric values
	are:  fontName, fontSize, and fontStyle

	
*/
package com.client
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Component

class DefaultFont( fontName:String, fontStyle:Int, fontSize:Int) extends Component  {

	override def toString= {"name="+fontName+"  style="+fontStyle+"  size="+fontSize }

	var defaultMetric=establishDefaultMetrics(fontName, fontStyle, fontSize)

		// Note: VisualMetric has 'establishMetrics(...)
	def establishDefaultMetrics( fontName:String, 
								 fontStyle:Int, 
								 fontSize:Int):FontMetrics={
		val font=new Font(fontName,fontStyle, fontSize)
		setFont(font)		
		getFontMetrics(font) 	// Toolkit method
		}
	def defaultPixelLetter() =defaultMetric.stringWidth("t")
	def defaultHeight()={ defaultMetric.getHeight() +4 }

	}

