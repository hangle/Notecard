/* date:   Oct 25, 2011
			NOTECARD
	Notecard  executes one or more Card Sets from a .struct
	file. 
	Example of a script file containing three Card Sets:
			c
			d first card                     
			c
			d second card
			c
			d three card
			* end
	Notecard  executes the three Card Sets and returns
	to 'card' to terminate the program ('* end' command). 
	Notecard  is the root of the notecard classes and is the
	parent of the following children:
	
		CardSet  --one or more notecard sets
		NotecardTask  --<asterisk commands>
		NextFile   --filename of next *.struct file

	In the example, Notecard  invokes CardSet three times, handing 
	off a single Card Set each time (see iterateNotecardChildren())
*/ 
package com.client
import scala.collection.mutable.Map
import javax.swing.JPanel
import javax.swing.JLabel
case class Notecard(var symbolTable:Map[String,String]) extends Linker {
			/*
				Linker extends Node	
					def setId
					def convertToSiblingd
					def convertToChild
					def convertToCondition
				Linker
					def reset
					def iterate
					def Value
			*/
//------------paramters pass by .struct file-----------------
	var frame_height=0
	var frame_width=0
	var font_size=0
	symId="2001"    	// Root symbolic address.
//------------------------------swizzle routines---------------------
							// uses Map to convert symbolic addr to physical addr
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToChild(swizzleTable)  // Is a parent of CardSet  isNoInputFields=true

			}
//-------------------------------------------------------------------
		// passed to KeyListenerObject, via Notecard,
		//     CardSet and then RowerNode,
		//     to allow 'notifyAll' to release 'wait' in
		//     this class.
	var lock:AnyRef= new Object()
		// Contains JLabel to display a message with
		// cmd '* stats <message>' and a message when
		// EditNode fails.
	val statusLine=new StatusLine
		// Panel holding buttons
	val buttonPanel= new JPanel		
		// Create three buttons with actionListeners.
		// Buttons are added to panel.Lock to issue notifyAll
	val buttonSet= new ButtonSet(buttonPanel, lock) //Buttons:  Next, Prior, and ' * '
		// FrameTask was passed a '* manage <filename>' command and CardSet
		// has instantiated a Notecard object. This object is
		// passed here from 'taskGather'. 
	var manageNotecard:Notecard=null
		// Create a JPanel with a layout of NoteLayout and
		// enclose it in a black border.
	val notePanel=new NotePanel().getNotePanel 
		// Initiates Notecard passing an argument 'taskGather' with 
		// parameters NEXT_FILE or END_SESSION
	def startNotecard(taskGather:TaskGather) {
		// Invokes new CardWindow and setVisible:  see above
		createCardWindow 	
		// special case for '* manage <filename>' with respect to 
		// the Manage state. For the Client state, the '*' button is
		// enabled in 'executeNotecardChildren (invokes armAsteriskBCardSet  
		// For the Manage state, if not enabled here, then '*' button will
		// be grayed out, making it impossible to return to Client state.
		if(Session.isAsteriskButtonArmed)
			buttonSet.armAsteriskButton


	try{ // see 'executeNotecardChildren' when '*' button is activated and 'isClientNotecardState'
					 // is false.
		// Notecard is root of a linked list containing its
		// children.
		iterateNotecardChildren(notePanel, taskGather, buttonSet)
		// Return to Client to either read a new .struct
		// file or to terminate the program.
				}catch { case ex: Exception=> 
						println("Notecard: exception caught in startNotecard()") 
						Thread.sleep(1000)
						sys.exit()
						}
		} 
		// CardSet, FrameTask, and Filename objects of Notecard(parent) are
		// executed. Execution is interrupted by '* end' or 'f <name>' commands.   
	def iterateNotecardChildren(notePanel:JPanel,taskGather:TaskGather, buttonSet:ButtonSet) {
		reset(getFirstChild)//In Linker, getFirstChild points to root of linked list 
		while(iterate) {	//Linker iterates linked list

				// gray the Prior button if 1st sibling is 1st CardSet.
				// thus having no prior CardSet. 
			if(isChild){ // 1st Card of Card  file  (see Linker)
				buttonSet.turnOnFirstChild    // sets 'firstChild' to true
				buttonSet.grayAndDisablePriorButton
				}
			  else{ // for second and subsequent card sets.
				// in inputfocus 'isfirstchildfalse' in 'actwhenallfieldscapured' and
				// 'establishaseriskcontinue' turns prior button green and 
				// enables button.
				//println("Notecard turnOffFirstChild")
				buttonSet.turnOffFirstChild //sets firstChild to false
				}
				// If '* end' or 'f <filename>' commands are detected, 
				// then 'isTaskNone' is false, causing the iteration to
				// be terminated, returning control to 'card'. 
			if(taskGather.isTaskNone) {
				// process CardSet or FrameTask or NextFile 
				executeNotecardChildren(Value, notePanel, taskGather, buttonSet:ButtonSet)
				}
			}      //---while
		}
		//Notecard's children: CardSet, FrameTask, NextFile
	def executeNotecardChildren(obj:Any, 
					notePanel:JPanel, 
					taskGather:TaskGather, 
					buttonSet:ButtonSet) { 
		obj match	{
				// CardSet executes a series of commands that
				// constitute a single Card set. 
			case cs:CardSet=> //
				// In Linker, the current iterator is added to
				// 'backupList' to support PRIOR button
				storeCurrentIterator
				// Activate CardSet to process RowerNode,
				// Assigner, CardSetTask, GroupNode, XNode to
				// present one Card. CardSet enters a wait() state
				// until button( NEXT,PRIOR,'*') is pressed, thus causing
				// it to return.
				cs.startCardSet(notePanel, lock, buttonSet, statusLine)
				// wait() of CardSet just released. Determine if either
				// backup button or * asterisk button was activated.
				// If so, than take care of // PRIOR and'*' buttons
				waitOverDoButtons(taskGather)
				// FrameTask is an <asterisk> command that performs
				// a notecard task, such as ending the card session.
			case ft:NotecardTask=> 
					ft.startNotecardTask(taskGather)
						// check if task was '* manage <filename>'
					if(taskGather.isManagement){
						// transfer management's Notecard to current object.
						manageNotecard=taskGather.manageNotecard
						// Enable button and change button color
						buttonSet.armAsteriskButton
						}
						// NextFile is a command that provides the name 
						// of the <.struct> file that is read by Client. 
			case nf:NextFile=> 
					nf.startNextFile(taskGather)
			case _=> println("unknown isObject")
			}
		}
		// Invoked when CardSet returns from  executeNotecardChildren()
		// Determine if and which button (Prior or '*')  was activated.
		//   Invokes doPriorButton(),  when Prior button activated in actionPerformed()
		//   Invokes doAsteriskButton(),  when '*' button activated in actionPerformed()
	def waitOverDoButtons(taskGather:TaskGather) {
				// buttons release the wait() state in CardSet.
				// The button PRIOR and '*' require special 
				// handling. The NEXT button does not, and falls
				// thru to execute another iteration loop. 
		if(buttonSet.isPriorButtonActivated) {
				// load  iterator with prior CardSet then reset prior 
				// button ('ButtonSet.priorButton' becomes 'false')
			doPriorButton // client has activated PRIOR button
			}
		else if(buttonSet.isAsteriskOn)	
			doAsteriskButton(taskGather) //  client has activated '*' button
		}
		//Backup set up
	def doPriorButton {
			// assign Linker.backup to 'iterator' to
			// enable the 'while(iterate)' to display prior Card.
		loadIteratorWithBackup  
		buttonSet.resetPriorButton	// turn off backup mechanism
		}
			// Invokes new CardWindow and setVisible:  see above
	def createCardWindow {
			// Creates the notecard window (JFrame) with a BorderLayout
			// and adds Note and Button panels to this window along with
			// statusLine:JLabel.
			// Also allows window size to change (height,width)
		val window= new CardWindow(notePanel,buttonPanel, statusLine, frame_width, frame_height)
		window.setVisible(true)
		}
			// wait() is over and '*' button detected by 'waitOverDoButtons'.
			// 1st discover state--either 'client' or 'manage'.
			// When 'client', then:
			//	clear states-- resetAsteriskButton
			//	set Session to indicate manage state
			//	save client node in the event that it must be restored
			//	invoke Notecard.startNotecard presenting 1st Card of manage file
			//	when startNotecard returns, then have Linker restore saved node.
			// When 'manage', then:
			//	clear states-- resetAsteriskButton
			//	set Session to indicate client state
			//	throw exception
	def doAsteriskButton(taskGather:TaskGather) {
			// Session keep track of whether Notecard is invoked	
			// by 'card',i.e., 'clientNotecard'  or invoked 
			// by '*' button, i.e., 'manageNotecard'
		if(Session.isClientNotecardState) {  //current state 'clientNotecardState'
			buttonSet.resetAsteriskButton
			Session.setManageNotecardState   // set  'manageNotecard' state  
			if(manageNotecard != null) {     // in event of no '* manage <file>' command
					//Store Card node if needed to restart clientNotecard
				saveCurrentNode   // Linker
					//Begin executing the Card file designated
					//by '* manage <filename> in FrameTask
					manageNotecard.startNotecard(taskGather)
					// Invokes new CardWindow and setVisible. This function is
					// also invoked in 'startNotecard', However, unless it is also
					// called here, the window is blanked when 'startNotecard' 
					// returns from the manageNotecard state.
					createCardWindow  
					// Display the Card from which the '*' button was activated
					restoreCurrentNode //Linker   Restore clientNotecard
					}
				  else println("Notecard:  manageNotecard is NULL")
				}
			else{	// current state is 'manageNotecardState' so switch
				// to 'clientNotecardState'.
				buttonSet.resetAsteriskButton
				Session.setClientNotecardState
					// Notecard's entry point is 'startNotecard'.  The thrown
					// exception is caught by 'startNotecard', causing it to
					// be returned to the invoking function.  Since the current
					// state is 'manageNotecard',then  the invoking function has to
					// be Notecard, rather than 'card'. 
				println("Notecard: throw new exception")
				throw new Exception
				}
		}
		// *.struct file delivers symbolic links and object parameters.
	def receive_objects(structSet:List[String]) {
		val in=structSet.iterator  //note: 'next' is an iterator method
		setChild(in.next)	
		frame_height=in.next.toInt
		frame_width=in.next.toInt
		font_size=in.next.toInt
		val percent=in.next
		//println("Notecard: percent="+percent)

		}	

}
