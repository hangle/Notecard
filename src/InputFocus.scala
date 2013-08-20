/* date:   Jun 25, 2012
   			Initial creation date:   Dec 10, 2011
                                   INPUT FOCUS  (was FieldFocus then BoxFieldFocus)   
               Created in CardSet and passed to RowerNode and then to 
               KeyListenerObject(boxField, inputFocus,Indexer.getIndex, statusLine) 
   
             RowerNode: 
			 	Iterates and invokes children (DisplayText,DisplayVariable,BoxField).
				BoxField invokes createListenerAndFocus(..) to create
						increment Indexer
						KeyListenerObject
						add BoxField to InputFocus
						add KeyListener to listenerArras
              
              KeyListenerObject: 
                  When a new line event (ENTER key) is detected, InputFocus.
                  actWhenCaptureComplete(..) is invoked. This method determines 
				  if all inputs have been collected.  If so, then NEXT button is
                  activated and, when pressed, it causes the release of 
				  CardSet's wait(). 
                  
              The display command   'd  (# $1) (# $2) (# $3) (# $4) ' creates
              4 answer fields.  As reponses are entered, the cursor will move
              from fields $1 to $2 to $3 to $4. However, the user may override
              this sequence or order, 

			**Note:
			  Components within a single container have a defined focus cycle of
			  its own.  Thus, the focus for the first component is automatically
			  set by the system. This cycle is overriden in 'addToArray(..)' to
			  address the situation when a Card has two or more XNode commands,
			  for example:
			  		c
					d (# $a)
					x
					d (# $b)

				In RowerNode, a BoxField child invokes 'addToArray(<BoxField>)'
				which increments 'counter' and when '1', it activates:
								if(counter==1)
									component.requestFocus
				As a result, this overrides the defined focus cycle, however, the
				effects are the same.  The very first BoxField is given focus. 

				The XNode command halts command execution until the first input
				field is captured. When captured, 'actWhenAllFieldsCaptured(..) is
				invoked to discover that, indeed, all fields (i.e., $a) have been
				accounted for. In this case, 'counter' is returned to zero (0).

				Card command execution is restarted, and now the second field
				($b) is processed causing 'counter' to be incremented to '1',
				thus invoking:
								if(counter==1)
									component.requestFocus
				Thus, $b is given focus.
*/
package com.client
import javax.swing.JComponent
import collection.mutable.ArrayBuffer
						// ButtonSet used when when all inputs are captured so as to
						// activate NEXT button and to release wait() in CardSet
class InputFocus ( buttonSet:ButtonSet) {
	var components=new ArrayBuffer[JComponent] //JComponent can be BoxField
				// Increment in RowerNode when BoxField object is detected.
	var counter=0  	 
				//Index of current component incremented when KeyListenerObject
				// detects ENTER key and invokes actWenAllFieldsCaptured().
	var arrayIndex= 0	   
	var xnodeState=false   // default until XNode is encountered in CardSet
	def turnOnXNode={ xnodeState=true}   // CardSet has encountered a XNode command
	def turnOffXNode={ xnodeState=false}// capture completes so turn off thirs condition
					// In 'addToArray(..)', 'counter' is incremented by an InputField 
					// instance. It is also initialized when 'actWhenAllFieldsCaptured(..)'
					// handles an XNode condition. 
	def isNoInputFields= {counter == 0}  //Used in CardSet:establishNextButton
					// ArrayBuffer[JComponent] added to and count of number
					// Invoked in RowerNode after BoxField detected.r
	def addToArray(component:JComponent) {
			components += component
			counter+= 1
					// see **Note  above
					// First JComponent of an XNode group of input fields. This will also
					// include 1st JComponent of the Card.
			if(counter==1)
				component.requestFocus
			}
					// Used 'KeyListenerObject' to determine if all input 
					// fields have been captured.
	def actWhenAllFieldsCaptured  {   //was actWhenCaptureComplete
		arrayIndex +=1  // JComponent index
		println("InputFocus:  actWhenAllFieldsCaptured():  arrayIndex  ="+arrayIndex+
									"  component.size="+components.size)
				// When true, than all inputs are accounted for.
		if(arrayIndex  ==components.size) {

				// XNode state treated differently because the processing of input
				// fields have been halted. Fields preceeding 'x' command are captured
				// before command execution is restarted. 
			if(xnodeState==true) { // set true in CardSet by XNode command
				turnOffXNode// turn off until next XNode instance
						// allows 'isNoInputFields' in CardSet to active 'NEXT' button
						// also forces focus in 'setFieldFocus' to first JComponent
						// of XNode input fields. 
				counter=0  
				buttonSet.start() // XNode releases wait()
				}
			  else {
							// In iteration, first CardSet set 'firstChild' to 'true',
							// next and subsequent CardSet set it to 'false'
				if( ! buttonSet.isFirstChildFalse) 
						buttonSet.grayAndDisablePriorButton // color button green and enable it.
				  
				println("InputFocus actWhenAll...() isFirstChildFalse="+buttonSet.isFirstChildFalse)
							// NEXT button releases wait()
				buttonSet.armNextButton//enable button,set focus,color button orange
				}
			}
		  else{
			  //setFieldFocus //
			  components(arrayIndex).requestFocus
			  }
		}
					// Invoked by CardSetTask in support of '* continue' cmd.
					// CardSetTask 1st invokes 'notePanel.validate(), and then
					// 'notePanel.repaint(), finally invoking this method.
					// NEXT button enabled. When activated, then rest of Card cmds 
					// are executed.
	def establishAsteriskContinue {
					// In iteration, first CardSet set 'firstChild' to 'true',
					// next and subsequent CardSet set it to 'false'
		if(buttonSet.isFirstChildFalse) 
			println("InputFocus extablishAsterisk...() first child false")
					// enable button, get focus, color button orange
		buttonSet.armNextButton
				buttonSet.turnOnPriorButton
				// wait() invoked, button hit invokes 'notifyAll()'
		buttonSet.issueWait
		}
}				
