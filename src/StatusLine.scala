/* date:   Jun 24, 2012
   
*/
package com.client
import javax.swing._

class StatusLine  extends JLabel {
	setText("")
	def clearStatusLine=setText("")	
	def	addMessageToStatusLine(message:String)= setText(message)
}
