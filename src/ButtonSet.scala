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
	val buttonCardSet=new JButton("+Add")
	var selectedButton="" // indicates 'actionPerformed' result
	var plusButton=false
	var nextButton=false
	var priorButton=false; // set true by button, set false in Notecard
	var asteriskButton=false//'true' when '*' button hit (see actionPerformed)
	var firstChild=false  // set true when Card is 1st child in Notecard chain. 
	var isAsteriskButtonOn="on"  // when 'off', button is disabled
	var isPriorButtonOn = "on" //when 'off', button is disabled
			// button acquires listener and is added to NotePanel/
	createActionButton(buttonPanel, asterisk)
	createActionButton(buttonPanel, buttonCardSet)
	createActionButton(buttonPanel, prior)
	createActionButton(buttonPanel, next)

	grayAndDisableNextButton
			// disable until '* manage <filename>' cmd
	grayAndDisableAsteriskButton
	grayAndDisablePriorButton 
	grayAndDisableButtonCardSet 
	
	def turnOnFirstChild=firstChild=true // Notecard set it in first iteration
	def turnOffFirstChild=firstChild=false// Notecard turns off after 1st iteration
		// Used in CardSet to enable PRIOR button when Card has 
		//  no input fields.
		// Used twice InputFocus. First, to enable when all fields
		//  are captured, and second when '* continue' is issued. 
	def isFirstChildFalse= firstChild==false
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
				notifyGrayAndDisableNext 
			case "Prior"=> 
				selectedButton="prior"  //Notecard: match expression
				notifyGrayAndDisableNext 
			case " * "=> 
				selectedButton="*"  //Notecard: match expression
				notifyGrayAndDisableNext 
				start()
			case "+Add"=>
				selectedButton="+"  //Notecard: match expression
				grayAndDisableButtonCardSet
				start() 
			case _=> 		println("ButtonSet unknown event=")
			}

		}
	def resetPriorButton= priorButton=false //set false in Notecard after executing
						  // prior Card.
	def createActionButton(buttonPanel:JPanel, button:JButton)={
		button.addActionListener(this)
		buttonPanel.add(button)
		}
	def grayAndDisableAsteriskButton={
		asterisk.setEnabled(false)
		asterisk.setBackground(Color.lightGray)
		}
	def grayAndDisablePriorButton={
		prior.setEnabled(false)
		prior.setBackground(Color.lightGray)
		}
	def grayAndDisableButtonCardSet={
		buttonCardSet.setEnabled(false)
		buttonCardSet.setBackground(Color.lightGray)
		}
	def grayAndDisableNextButton={ // invoked in CardSet and
					// in ButtonSet. see notifyGray...
		next.setEnabled(false)
		next.setBackground(Color.lightGray)
		}
		// orange Next button arms this method
		// Invoked early in CardSet:establishNextButton
	def notifyGrayAndDisableNext = {
			// Disabled PRIOR button because it was occasionally
			// gaining focus after NEXT button activation, causing
			// the next spacebar key to initiate backup. 
		grayAndDisablePriorButton
			// Disable button
		grayAndDisableNextButton
		start		//(notifyAll). In CardSet, initiates the next 
		   		// Card Set or terminate XNode wait. 
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
		next.requestFocus()
		}
		// Invoked in:  ButtonSet & CardSet
	def armPriorButton= {
			// can be disabled by '* priorButton  off'
			// on/off assigned in Notecard
		if(isPriorButtonOn=="on"){
			prior.setBackground(Color.GREEN)		
			prior.setEnabled(true)
			next.requestFocus()
			}
		else
			grayAndDisablePriorButton
		}
	def armAsteriskButton= {
			// can be disabled by '* asteriskButton  off'
			// on/off assigned in Notecard
		if(isAsteriskButtonOn=="on") {
				asterisk.setEnabled(true)
				asterisk.setBackground(Color.white)
				}
			else
				grayAndDisableAsteriskButton
		}
	def armButtonCardSet={
		buttonCardSet.setEnabled(true)	
		buttonCardSet.setBackground(Color.YELLOW)
		}

		//card commands halted by 'wait' in
		// CardSet. 'Next' button action 
		// terminates the wait condition.
	def start():Unit= lock.synchronized{ lock.notifyAll() } 
		// Invoked by InputFocus in support ofg
		// the '* continue' statement. 
	def issueWait:Unit=lock.synchronized{ lock.wait() }
}

