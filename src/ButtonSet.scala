/* date:   Dec 12, 2011
						[note was  ButtonAction prior 12/12/2011]] 
	Notecard creates three notecard buttons with ActionListeners 
	and places them on a buttonPanel whose default layout is 
	FlowLayout.  

	ButtonSet instance is passed to CardSet which invokes
	'armNextButton' to enable the 'NEXT' button to acquire focus,
	and to set its background to orange. 
	====================not found ???=====================
	When the NEXT button is armed, the button is made inactive,
	its background is grayed, and the 'wait' condition in CardSet 
	is released (see: 'nextButtonResponse). 
	=========================================================

	ButtonSet is passed to FieldFocus constructor in CardSet.
	When FieldFocus in KeyListenerObject detects that all Text fields
	are captured, it invokes 'armNextbutton()' giving this button
	the focus.  When Next button is hit, then 'actionPerformed' is
	invoked, initiating notifyAll to release the 'wait' in CardSet. 

	The PRIOR button activation caused the prior Card to be initiated.
	Because the first Card has no prior card, the PRIOR button is 
	disabled for this card. 

	During the execution of a Card, activation of the '*' or Asterisk 
	button will initiate a Card file whose <filename> is designated 
	by the '* manage <filename>' command. A second activation of 
	the '*' button, returns the process to the Card from which the
	first '*' button activation took place.

Collaborates with:
	Notecard
		'* manage <filename> command    ????

		Following the Card set display (executeCommandsAndDisplay),
		the Next and Prior (not for 1st Card) buttons are armed and
		a wait state exists until released by ButtonSet (start())
	InputFocus
		'x' command  also releases the 'wait' state	(start())
*/
package com.client
import javax.swing._
import java.awt._
import java.awt.event._

class ButtonSet(buttonPanel:JPanel, lock:AnyRef) extends ActionListener{
	val next=new JButton("Next")
	val prior=new JButton("Prior")
	val asterisk=new JButton(" * ")
	val addButton=new JButton("+Add")
	var selectedButton="" // indicates 'actionPerformed' result
	var plusButton=false
	var nextButton=false
	var priorButton=false; // set true by button, set false in Notecard
	var asteriskButton=false//'true' when '*' button hit (see actionPerformed)
	var isAsteriskButtonOn="on"  // when 'off', button is disabled by * cmd
	var isPriorButtonOn = "on" //when 'off', button is disabled
			// button acquires listener and is added to NotePanel/
	createActionButton(buttonPanel, asterisk)
	createActionButton(buttonPanel, addButton)
	createActionButton(buttonPanel, prior)
	createActionButton(buttonPanel, next)

	grayAndDisableNextButton
	grayAndDisableAsteriskButton
	grayAndDisablePriorButton 
	grayAndDisableAddButton 

		// Determine which button was activated and take 
		// appropriate action.
	def actionPerformed(event:ActionEvent) { 
		event getActionCommand() match{
				// Next button enabled by FieldFocus
			case "Next"=>  
				selectedButton="next" //Notecard: match expression
					// Next button has been activated, so:
					// disable it,  gray the button,
					// then release the wait state in CardSet
				grayAndDisableNextButton 
					// Disabled PRIOR button because it was occasionally
					// gaining focus after NEXT button activation, causing
					// the next spacebar key to initiate backup. 
				start()   //unlock all
			case "Prior"=> 
							//	println("ButtonSet: actionPerf...    --prior-- ")
				selectedButton="prior"  //Notecard: match expression
				grayAndDisablePriorButton
				start()   //unlock all
			case " * "=> 
							//	println("ButtonSet: actionPerf...    --*-- ")
						//used in Notecard: match expression
				selectedButton="*"  
					// Move to a new script file so gray all buttons
					// in old script before the move.
				start()   //unlock all
			case "+Add"=>
				selectedButton="+"  //Notecard: match expression
//				grayAndDisableAddButton
				start()    //unlock all
			case _=> 		println("ButtonSet unknown event=")
			}
		}

	def resetPriorButton= priorButton=false //set false in Notecard after executing
						  // prior Card.
	def createActionButton(buttonPanel:JPanel, button:JButton)={
		button.addActionListener(this)
		buttonPanel.add(button)
		}
			// Invoked in InputFocus  ??
	def grayAndDisableThreeButtons { //+,Prior,Next buttons
		grayAndDisableNextButton
		grayAndDisableAddButton
		grayAndDisablePriorButton
		}
	def grayAndDisableAsteriskButton={
		asterisk.setEnabled(false)
		asterisk.setBackground(Color.lightGray)
		}
	def grayAndDisablePriorButton={
		prior.setEnabled(false)
		prior.setBackground(Color.lightGray)
		}
	def grayAndDisableAddButton={
		addButton.setEnabled(false)
		addButton.setBackground(Color.lightGray)
		}
	def grayAndDisableNextButton={ // invoked in CardSet and
					// in ButtonSet. see notifyGray...
		next.setEnabled(false)
		next.setBackground(Color.lightGray)
		}
		// Invoked by CardSet and InputFocus
		// CardSet in startCardSet after children 
		//   iteration and just before 'wait'is issued.
		//   It causes the 'Next' button to acquire focus.
		// InputFocus when all fields are captured. 
		//	 InputFocus.actWhenCaptureComplete
	def armNextButton = {
		next.setEnabled(true)
		next setBackground(Color.ORANGE)
		}
		// Invoked in:  ButtonSet, InputFocus & CardSet
	def armPriorButton= {
			// can be disabled by '* priorButton  off'
			// on/off assigned in Notecard
		if(isPriorButtonOn=="on"){
			prior.setBackground(Color.GREEN)		
			prior.setEnabled(true)
			}
		else
			grayAndDisablePriorButton
		}
	def armAsteriskButton= {
			// can be disabled by '* asteriskButton  off'
			// on/off assigned in Notecard
			//println("ButtonSet: armAsteriskButton")
		if(isAsteriskButtonOn=="on") {
				asterisk.setEnabled(true)
				asterisk.setBackground(Color.white)
				}
			else {
				//println("ButtonSet: grayAndDisableAsteriskButton")
				grayAndDisableAsteriskButton
				}
		}
	def armAddButton={
		addButton.setEnabled(true)	
		addButton.setBackground(Color.YELLOW)
		}

		//card commands halted by 'wait' in
		// CardSet. 'Next' button action 
		// terminates the wait condition.
	def start():Unit= lock.synchronized{ lock.notifyAll() } 
		// Invoked by InputFocus in support ofg
		// the '* continue' statement. 
	def issueWait:Unit=lock.synchronized{ lock.wait() }
}

