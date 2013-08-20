/* date:   Oct 28, 2012
											Invoked by: KeyListenerObject
				Multiple Choice Response Capture
	
	The Display commands:
			d How often do you travel
			d (#3 $frequency)
			d 1. Often
			d 2. Sometimes
			d 3. Never
	creates a input field whose size is one character width.
	Input characters that are not numeric are rejected causing
	the input field to be cleared. Similarly, if the response value 
	is greater than 3 or less than 1,  then the response is
	rejected and the field is cleared. In both cases, a status
	message is displayed. 

	BoxField parameter 'options' ,when greater than zero, indicates
	a multiple choice response field.  An 'options' value is the
	number of choices.  In the above example, the numeric value following
	the '#' tag designates three (3) options. 

	Option values less than 10 limits the response input to one character, 
	provided the character is numeric and within the option range. An options
	range greater than 9, limits the response input to two characters (
	either two numeric characters, or one numeric and a VK_ENTER character).
*/
package com.client
import java.awt.event._
import java.awt._
import javax.swing._

object MultipleListener  extends App{

			// supports multiple choice entry mode. Invoked when 'options'  parameter
			// is non zero. Rejects non numeric inputs as well as any numeric value
			// greater than 'options'. /
	def multipleListener(key:Int,
						 countVal:Int,
						 inputFocus:InputFocus,
						 boxField: BoxField,
						 statusLine:StatusLine):Int = {
		var count=countVal  // count is increment and may be set to zero
		statusLine.clearStatusLine   // removes prior message, if any
		count+= 1   // increment for each input character
		key match {
			case KeyEvent.VK_ENTER=>
				println("vk-enter   field="+boxField.field)
				if(boxField.isValidOptionRange){
					println("valid option range")
						// switches focus to next component if all fields have
						// not been captured, otherwise activate NEXT button. 
					inputFocus.actWhenAllFieldsCaptured
					}
				else{			// number not in range of options
					boxField.clearInputField // remove current response
				    statusLine.addMessageToStatusLine("enter values 1-"+boxField.options)
					}
			case KeyEvent.VK_BACK_SPACE  =>
				println("vk-back-space")
				count -= 1
			case KeyEvent.VK_DELETE =>
				println("vk-delete")
				count=0		
				boxField.clearInputField // remove current response
			case _=>
				println(" _=> key")
				if(isNumber(key)) 
						// count=0 when option range is exceeded
					count=validateOptionRange(count, boxField, inputFocus,statusLine)
				else{
					println("\tnot number clear input field")
					count=0
					statusLine.addMessageToStatusLine("numbers only")
					boxField.clearInputField // remove current response
					}
			}
		count
		}
	def isNumber(key:Int)= { if(key > 47 && key < 58) true; else false }

				// Invoked when input character is a number. The option range
				// is checked (>0 and less then options+1. Following successful
				// range check, if the count of input characters is equal to
				// the limit parameter, the input field is captured.
	def validateOptionRange(countVal:Int,
							boxField:BoxField,
							inputFocus:InputFocus,
							statusLine:StatusLine)={
		println("KetyLIstneerObject validateOptionRange")
		var count=countVal
		if(boxField.isValidOptionRange)  
			if(count==boxField.limit) {
				println("valid option range  count="+count+"   limit="+boxField.limit)
				boxField.addFieldToSymbolTable // store input resonse
				inputFocus.actWhenAllFieldsCaptured
				}
			else
				println("count not equal to limit")
		else {  // is number but not in option range
			println("number but not in option range")
			count=0
			boxField.clearInputField // remove current response
			statusLine.addMessageToStatusLine("enter values 1-"+boxField.options)
		    }
		count
		}


}
