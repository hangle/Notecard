/* date:   Dec 5, 2011
   						KEY LISTENER OBJECT 
	KeyListenerObject "listens" for key input characters. 

	Created in RowerNode when object is a BoxField. Note, KeyListenerObject
	is added to BoxField (	boxField.addKeyListener(this) )

	'keyReleased' returns the input character from a BoxField response. 
	When the character is an Enter key, then the field text is added to the
	'symbolTable'.
	
	Next, IndexFocus indicates whether all fields have
	been captured (IndexFocus uses ButtonSet to activate the Next button
	which when activated, issues NotifyAll, releasing the wait in CardSet).
*/
package com.client
import java.awt.event._
import java.awt._
import javax.swing._

class KeyListenerObject(boxField:BoxField,  
						inputFocus:InputFocus, 
						listenerIndex:Int, // unique identifer for each listener
										// Note:  listenerIndex not used in class 
										// suggesting that it is no longer operational.
						statusLine:StatusLine)	extends KeyListener   { 

			// BoxField parameters, i.e., getLimit, isYesNoMode, 
			// IndexFocus   -- allow communication with IndexFocus
			// listenerIndex	-- each KeyListenerObject given a unique 
			//						id by RowerNode:Indexer
			//				    --- first KeyListenerObject index is zero
	var limit=boxField.getLimit;   // number of input characters accepted.
	var count=0   // count of the number of input chars received.
			//addKey.. is JTextField method--needed to detect ENTER key.
	boxField.addKeyListener(this)

	def getBoxField=boxField // In CardSet, to removeKeyListener(..)
	def keyTyped(event: KeyEvent) {}
	def keyPressed(event: KeyEvent) { }
			// Input characters are captured as an input respones when:
			//	1.	yes/no response /or
			//  2.  multiple choice
			//	3.	enter key activated /or
			//	4.	number of input characters exceed limit
	def keyReleased(event: KeyEvent) {
		val key:Int= event.getKeyChar()
			// Example command  'd  (#yn  $ans)' sets 'BoxField.column'
			// to '1'
		if(boxField.isYesNoMode) {   //BoxField.column ==1
				// Record 'y' or 'n' if valuation succeeds
			if(evaluateYesNoEntry(key))
						// switches focus to next component if all fields have
						// not been captured.
					inputFocus.actWhenAllFieldsCaptured
			}
		  else if(boxField.isMultipleChoice) {  //BoxField.options > 0
			// Validates that response is numeric and that response does
			// not exceed 'options' value, is so then store response.
			// On failure, set count to zero, and display status message. 
			// Script example:
			//		d (#3 $frequency)
			//		d 1. Often
			//		d 2. Sometimes
			//		d 3. Never
			count=MultipleListener.multipleListener(key, 
													count, 
													inputFocus, 
													boxField, 
													statusLine) 
			}
		  else if(key==KeyEvent.VK_ENTER){  //test for 'ENTER' key.  VK_ENTER= 10
				// Store input if Edit is on and is successful or
				// store input if edit if off
			if(captureInputResponse ){ 	//fails if Edit fails
						// inputFocus counts number response required
						// Releases CardSet wait() when count achieved
				inputFocus.actWhenAllFieldsCaptured
				}
			}
				// limit placed on number of input chars
		  else if(isInputLengthEqualToLimit(key, limit)) //true if count = limit
						// Store input if Edit is on and is successful or
						// store input if edit if off
			if(captureInputResponse)	//fails if Edit fails
						// inputFocus counts number response required
						// Releases CardSet wait() when count achieved
				inputFocus.actWhenAllFieldsCaptured
		}
		// Invoked when number chars limit reached, or ENTER key detected.
		// Store BoxField response in symbol table and return 'true', unless
		// 'Edit' is on. If so, the
	def captureInputResponse:Boolean= { // invoked following KeyRelease
		boxField.addFieldToSymbolTable // store input resonse
		if(boxField.isEditNodeOn) {      // Edit evaluation active
					// Iterate EditNode children. Returns true if all
					// EditField(s) associated with the BoxField each return 
					// true, or returns false is any one returns false
					//	   Invoked in KeyListenerObject.actOnNewLineEvent(...)
			if( ! boxField.isEditSuccessful ){  // Do when Edit evaluation fails
					//On failure, clear input field and display status message.
				boxField.clearInputField
				statusLine.clearStatusLine   // removes prior message, if any
				statusLine.addMessageToStatusLine(boxField.getEditMessage)
				false
			 	}
			 else{
				statusLine.clearStatusLine   // removes prior message, if any
				true
				}
			 }
		  else true
		}    
			// Record input as 'y' or 'no
    def evaluateYesNoEntry(key:Int):Boolean={
		if(boxField.validateYesNoResponse(key)){ //key=110 or 121
				// invoked by 'y'or 'n' keys and not
				// by 'ENTER' key. 
			boxField.addFieldToSymbolTable // store input before evaluating EditNode
			true
			}
		  else{				// User failed to enter either 'y' or 'n'
			statusLine.addMessageToStatusLine("Enter 'y' or 'n' ")
			boxField.clearInputField // remove invalid character
			false
			}
		}
		 // Count number of input chars, adjust for
		 // deletes and backups
	def isInputLengthEqualToLimit(key:Int, limit:Int)= {
					// prior letter deleted so decrement the
					// letter 'count' if greater than zero.
		if(key== KeyEvent.VK_DELETE || key== KeyEvent.VK_BACK_SPACE) {
			if(count > 0)
				count-= 1;  // character removed, so decrement count
			false
			}
		else {
			count += 1;
			if(count >= limit)	
				true;
			  else
				false;
			}
		}
}

