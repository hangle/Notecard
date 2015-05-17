/* date:   Jun 25, 2012
	Initial creation date:   Dec 10, 2011
             INPUT FOCUS  (in Java was FieldFocus then BoxFieldFocus)   

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
		which increments 'counterInputFields' and when '1', it activates:
						if(counterInputFields==1)
							component.requestFocus
		As a result, this overrides the defined focus cycle, however, the
		effects are the same.  The very first BoxField is given focus. 

		The XNode command halts command execution until the first input
		field is captured. When captured, 'actWhenAllFieldsCaptured(..) is
		invoked to discover that, indeed, all fields (i.e., $a) have been
		accounted for. In this case, 'counterInputFields' is returned to zero (0).

		Card command execution is restarted, and now the second field
		(# $b) is processed causing 'counterInputFields' to be incremented to '1',
		($b) is processed causing 'counterInputFields' to be incremented to '1',
		thus invoking:
				if(counterInputFields==1)
					component.requestFocus
		Thus, (# $b) field is given focus.
*/
package com.client
import javax.swing.JComponent
import collection.mutable.ArrayBuffer
			// ButtonSet used when when all inputs are captured so as to
			// activate NEXT button and to release wait() in CardSet
class InputFocus ( buttonSet:ButtonSet, backupMechanism:BackupMechanism) {
	var components=new ArrayBuffer[JComponent] //JComponent can be BoxField
			// Increment in  InputFocus.addToArray(...), however, RowerNode invokes
			// this function `when BoxField object is detected.
	var counterInputFields=0  	 
			//Index of current component incremented when KeyListenerObject
			// detects ENTER key and invokes actWhenAllFieldsCaptured().
	var arrayIndex= 0	   
			// default until XNode is encounterInputFieldsed in CardSet
	var xnodeState=false   
			// Set false at beginning of iteration and set true at end.
			// Arm Prior button when true provided CardSet not first. 
			// Invoked by CardSet (2 places): 
			// (1) following 'executeCardSetCommands(...'.
			// (2) following case xn:Xnode =>
			// Invoked by CardSet (2 places) just before 'haltCommandExecution'.
			// Note: focus not requested when CardSet has no InputFields (counterInputFields==0)
			// or when 'actWhenAllFieldsCaptured' set this value to 0.
	def giveFocusToFirstInputField {
		if(counterInputFields >0) {
				// input has not been captured so disable +Add and Prior buttons
				// while input active, disable +Add, Prior, and Next buttons
				// buttonSet.grayAndDisableNextAndPrior
			buttonSet.grayAndDisableNextButton
			buttonSet.grayAndDisablePriorButton
	//		val component=components(0)
			val component=components(arrayIndex)
			component.requestFocus
			}
		}
	def turnOnXNode={ xnodeState=true}   // CardSet has encounterInputFieldsed a XNode command
	def turnOffXNode={ xnodeState=false}// capture completes so turn off this condition
		// In 'addToArray(..)', 'counterInputFields' is incremented by an InputField 
		// instance. It is also initialized when 'actWhenAllFieldsCaptured(..)'
		// handles an XNode condition. 
	def isNoInputFields= {counterInputFields == 0}  //Used in CardSet:establishNextButton
		// ArrayBuffer[JComponent] added to and count of number
		// Invoked in RowerNode after BoxField detected.
	def addToArray(component:JComponent) {
			components += component
			counterInputFields+= 1
			}
		// called by 'KeyListenerObject' each time a response is posted. 
		// Function  determines if all input fields have been captured.
	def actWhenAllFieldsCaptured  {  
			// Count the number of times the 'Enter' key has been pressed in
			// KeyListenerObject.
		arrayIndex +=1  // JComponent index
		//println("InputFocus:  arrayIndex="+arrayIndex+"  components.size="+components.size)
				//	println("InputField: here arrayIndex="+arrayIndex)
			// When true, than all inputs are accounted for.
		if(arrayIndex == components.size) {
					// XNode state treated differently because the processing of input
					// fields have been halted. Fields preceeding 'x' command are captured
					// before command execution is restarted. 
			if(xnodeState==true) { // set true in CardSet by XNode command
					// turn off until next XNode instance
				turnOffXNode
					//Allows RowerNode to begin counting BoxField objects , if any.
				counterInputFields=0  
					// Release CardSet to execute remaining CardSet commands.
				buttonSet.start() // XNode releases wait()
				//println("InputFocus:  start()")
					}
			  else{
						// 1st child has no sibling to backup to
						// also coded in CardSet
				if( ! backupMechanism.isFirstChild) {
						// arm only after '* continue' abd 'x' commands have completed.
					buttonSet.armPriorButton
					}
							// CardSet has been halted so control is turned over to
							// Next button to initiate release (start()). 
				buttonSet.armNextButton   //enable button,color button orange
			   	buttonSet.next.requestFocus
				}
			}
		  else{
					// Move cursor to next input field 
				//println("InputFocus: actWhenAllFieldsCaptured arrayIndex!=size-- component.requestFocus")
		//	if(arrayIndex+1 >=components.length)
		//	  	println("ImputFocus:  ********* arrayIndex="+arrayIndex+" Greater<or>Equal components.length="+components.length+"********")
		//	else
	    	  components(arrayIndex).requestFocus
			  }
	}
			// Invoked by CardSetTask in support of '* continue' cmd.
			// CardSetTask 1st invokes 'notePanel.validate(), and then
			// 'notePanel.repaint(), finally invoking this method.
			// NEXT button enabled. When activated, then rest of Card cmds 
			// are executed.
	def establishAsteriskContinue {
			// enable button, get focus, color button orange
		buttonSet.armNextButton 
		//println("InputFocus: establishAsteriskContinue-- NextButton.requstFocus")
		buttonSet.next.requestFocus
			// wait() invoked, button hit invokes 'notifyAll()'
		buttonSet.issueWait
		}
}				
