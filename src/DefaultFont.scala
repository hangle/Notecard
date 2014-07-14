/* Jul 13, 2014
				DefaultFont

	Container to transfer Font values to 
	RowerNode and RowPosition.  The metric values
	are:  fontName, fontSize, and fontStyle
	Notecard defalt values are:
		TimesRoman
		22
		1
*/
package com.client
import java.awt.Font

class DefaultFont( fontName:String, fontStyle:Int, fontSize:Int)
		extends Font(fontName, fontStyle, fontSize){

	}



