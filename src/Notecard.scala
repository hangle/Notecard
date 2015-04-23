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
		LoadDictionary  -- initialize $<variable> dictionary

	In the example, Notecard  invokes CardSet three times, handing 
	off a single Card Set each time (see iterateNotecardChildren())
*/ 
package com.client
import scala.collection.mutable.Map
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.JFrame

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
	var fontSize=14
	var fontStyle=1
	var fontName="TimesRoman"
	var asteriskButtonState="on"
	var priorButtonState="on"

//------------------------------swizzle routines---------------------
							// uses Map to convert symbolic addr to physical addr
	def convertToReference(swizzleTable:Map[String, Node]) ={
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
		// Create three buttons with actionListeners.
		// Buttons are added to panel.Lock to issue notifyAll
	val buttonPanel= new JPanel		
		// Create buttons '*', "+ADD", 'PRIOR', 'NEXT'
	val buttonSet= new ButtonSet(buttonPanel, lock) //Buttons:  Next, Prior, and ' * '
		// Create a JPanel with a layout of NoteLayout and
		// enclose it in a black border.
	val notePanel=new NotePanel().getNotePanel 
		// Save current CardSet to be restore  after AddCardSet
		// has executed
	var tmpNode:Node= _
	var currentCardSet:Node= _
		// Creates a List of nodes used when Prior button is activated to
		// re-present the prior CardSet.
	val backupMechanism= new BackupMechanism("CardSet")
		// Creates list of nodes to enable backup within AddCardSets.
		// Instaniated in 'executeOneNotecardChild when CardSet button==1
	var addBackupMechanism:BackupMechanism = _
		// Support of two backup mechanism (1) CardSet, and (2)AddCardSet/
		// variable indicates the current backup system./
	var whichBackup="cardSet"
		// FrameTask was passed a '* manage <filename>' command and CardSet
		// has instantiated a Notecard object. This object is created  in 
		// NotecardTask and is passed here from 'taskGather'. 
	var manageNotecard:Notecard= _
		// Contains 'activatedAddButton', 'isFirstAddCardSet'. 
	val addCardSetFlags=new AddCardSetFlags

	def startNotecard(taskGather:TaskGather) {
			// * button to Management file is alway armed
		buttonSet.armAsteriskButton
			// Container of values to pass to RowPosition.
		val defaultFont=new DefaultFont(fontName, fontStyle, fontSize)

			// 'oldJFrame' references CardWindow whose subclass is JFrame. 
			// Used in 'card' to dispose of JFrame.
			//taskGather.oldJFrame=createCardWindow 	

			// Creates the notecard window (JFrame) with a BorderLayout  and adds Note 
			// and Button panels to this window along with statusLine:JLabel. Also makes
			// the window visible.
		taskGather.oldJFrame=createAndMakeVisibleCardWindow (notePanel, 
															buttonPanel,
															statusLine, 
															frame_width, 
															frame_height)	
			// Load 'oldJFrame' on list to be /disposed by 'card' when
			// the *.struct file ends.
		taskGather.addOldJFrameList(taskGather.oldJFrame)
				try{ 
			// Execute Notecard's children (CardSet, NextFile, NotecardTask)
		iterateNotecardChildren(notePanel, taskGather, buttonSet, defaultFont)
						// doAsteriskButton() throws the exception caught here. This function
						// has recursively invoked Notecard and the exception is a way
						// to continue processing its children.
				}catch { case ex: AsteriskException=>  }
		} 
		// CardSet, FrameTask, and Filename children of Notecard(parent) are
		// executed. Execution is interrupted by '* end' or 'f <name>' commands.   
	def iterateNotecardChildren(notePanel:JPanel,
								taskGather:TaskGather, 
								buttonSet:ButtonSet,
								defaultFont:DefaultFont) {
		reset(child)//In Linker, getFirstChild points to root of linked list 

		while(iterate) {	//Linker iterates linked list of siblings
				// gray the Prior button if 1st sibling is 1st CardSe/t.
				// thus having no prior CardSet. 
			if(isChild){ // 1st Card of Card  file  (see Linker)
				buttonSet.grayAndDisablePriorButton
				}
						// The 'AddButtonException' is caught having been thrown by the
						// 'CardSet.executeOneCardSetChild' (see xn:XNode=> ) causing the
						// termination of the current CardSet and the initiation of the
						// dependent AddCardSet.
					try {
				// If '* end' or 'f <filename>' commands are detected, 
				// then 'isTaskNone' is false, causing the iteration to
				// be terminated, returning control to 'card'. 
			if(taskGather.isTaskNone){ 
					// process CardSet or FrameTask or NextFile. 'Value' returned from 'iterate'.
				executeOneNotecardChild(node,   // iterate return node (see Linker)
										notePanel, 
										taskGather, 
										buttonSet, 
										defaultFont)

				}
					}  catch { case _:AddButtonException => }
			}   
		}
			//Notecard's children: CardSet, FrameTask, NextFile, LoadDictionary
	def executeOneNotecardChild(obj:Node, 
								notePanel:JPanel, 
								taskGather:TaskGather, 
								buttonSet:ButtonSet,
								defaultFont:DefaultFont) { 
		obj match	{
				// CardSet with 'button' values of 2 or 99 are ButtonCardSet types.
				// button > 1  where 1=CardSet, 2=ButtonCardSet, 3=LastButtonCardSet
			case acs:CardSet if(acs.isAddCardSet) =>
					// +Add button detected in 'waitOverDoButton'				
					// AddCardSets fall thru (not processed) unless ButtonSet 'event'
					// indicated that the '+Add' button was activated in addition that
					// the '+Add' button is only armed by a CardSet with dependent
					// AddCardSet objects.
					// The "||" <or> necessary to support backup of AddCardSet(s).
				if(buttonSet.selectedButton == "+" || buttonSet.selectedButton=="prior"){
							// Indicates which BackupMechanism to execute in 'doPriorButton'
						whichBackup="addCardSet"
							// 1st sibling to pass condition test is stored in 'firstChild'
							// to prevent backup beyond the 1st child. 'firstChild' in 
							// AddBackupMechansim is null and is replaced by 'obj'.
						addBackupMechanism.captureFirstChild(obj)
							// save all 'node's (obj) to be used to back up to prior CardSet
						addBackupMechanism.storePriorSiblingInBackupList( obj)
						startCardSetThenDoButtonsAfter( acs,
														notePanel,
														taskGather,
														buttonSet,
														defaultFont,
														addCardSetFlags,
														addBackupMechanism)
						}
				// CardSet executes a series of children commands that
				// constitute a single Card set. 
			case cs:CardSet=> //println("cs:CardSet") //
						// Do CardSet if condition is true or 'c' command lacks condition.
						// Otherwise skip 'wait' so as to process next CardSet.
						// 	If the 'c' command has logic ,e.g, 'c (2)=(3)', then the logic
						//	is tested and the CardSet instances is skipped when 'false',
						//  If 'c' cmd has no logic, then outcome is always 'true'.
				if(cs.noConditionOrIsTrue( symbolTable)){	//'c <cmd> has no logic /
						// 1st sibling to pass condition test is stored in 'firstChild'/
						// to prevent backup beyond the 1st child.
					backupMechanism.captureFirstChild(node)
						// save all 'node's to be used to back up to prior CardSet
					backupMechanism.storePriorSiblingInBackupList( node)
								// button==1 A CardSet with associate ButtonCardSet
					if(cs.hasAddCardSet) {
								// Arm +Add button and save 'current node' to restore CardSet
						buttonSet.armAddButton
							// Create BackupMechanism for AddCardSet(s)
						establishAddBackup(addCardSetFlags)
						}
						// Activate CardSet to process RowerNode,:
						// Assigner, CardSetTask, GroupNode, XNode to
						// present one Card. CardSet enters a wait() state
						// until button( NEXT,PRIOR,'*') is pressed, thus causing
						// it to return.
					startCardSetThenDoButtonsAfter( cs,
													notePanel,
													taskGather,
													buttonSet,
													defaultFont,
													addCardSetFlags,
													backupMechanism)

					}
					else println("Notecard:  skip cs.noConditionOrIsTrue")
					// FrameTask is an <asterisk> command that performs
					// a notecard task, such as ending the card session.
			case ft:NotecardTask=> //println("ft:NotecardTask") 
					ft.startNotecardTask(taskGather)
						// check if task was '* manage <filename>'
					if(taskGather.isManagement){
							// transfer management's Notecard to current object.
						manageNotecard=taskGather.manageNotecard
						}
						// NextFile is a command that provides the name 
						// of the <.struct> file that is read by Client. 
			case nf:NextFile=> //println(" nf:NextFile")
					nf.startNextFile(taskGather)
			case ld:LoadDictionary=> //println("ld:LoadDictionary")
					ld.startLoadDictionary
			case _=> println("Notecard: unknown isObject"+ obj)
			}
		}
			// Create BackupMechanism for AddCardSet(s)
	def establishAddBackup(addCardSetFlags:AddCardSetFlags) {
				// Indicates which BackupMechanism to execute in 'doPriorButton'
			whichBackup="cardSet"
				// release prior backup mechanism
			addBackupMechanism=null
				// AddCardSets have their own backup mechanism.
			addBackupMechanism= new BackupMechanism("AddCardSet")
				// In CardSet. 'hadDependentAdd' along with "+" throws
				// AddButtonException in 'xn:XNote=>'
			addCardSetFlags.hasDependentAdd=true
				// current CardSet node is saved to be re-installed by
				// 'restoreCurrentCardSet'
			saveCurrentCardSet 
		}

	def startCardSetThenDoButtonsAfter( cardSet:CardSet,
										notePanel:JPanel,
										taskGather:TaskGather,
										buttonSet:ButtonSet,
										defaultFont:DefaultFont,
										addCardSetFlags:AddCardSetFlags,
										backupMechanism:BackupMechanism)= {
		cardSet.startCardSet(notePanel, 
							lock, 
							buttonSet, 
							statusLine, 
							backupMechanism,
							defaultFont,
							addCardSetFlags)
				// wait() of CardSet just released. Determine if either
				// backup button or * asterisk button or '+Add' button was 
				// activated. If so, than take care of button activated.
		waitOverDoButtons(taskGather,
						  cardSet,
						  notePanel,
						  lock,buttonSet, 
						  statusLine, 
						  defaultFont,
						  addCardSetFlags) 
		}

		// Invoked when CardSet returns from  executeOneNotecardChild()
		// Determine if and which button (Prior, +Add ,Next or '*')  was activated.
	def waitOverDoButtons(taskGather:TaskGather, 
						  cardSet:CardSet,
	    				  notePanel:JPanel, 
						  lock:AnyRef, 
						  buttonSet:ButtonSet, 
						  statusLine:StatusLine,
						  defaultFont: DefaultFont,
						  addCardSetFlags:AddCardSetFlags):Unit={ 
			// Note: in CardSet child XNode, button activations are not in
			// this control path.
		buttonSet.selectedButton match {
				case "next"  =>
						doNextButton(buttonSet, addCardSetFlags)
				case "prior" =>
						doPriorButton(buttonSet, addCardSetFlags)
				case "*" =>
						doAsteriskButton(buttonSet, taskGather)
				case "+" =>
						doAddButton(buttonSet, cardSet, addCardSetFlags)
				case _=>
					println("Notecard: unknown button actionPerformed--"+buttonSet.selectedButton)
				}
		}
			//
	def saveCurrentCardSet { currentCardSet=node }
	def restoreCurrentCardSet { iterator=currentCardSet }

	def doNextButton(buttonSet:ButtonSet, addCardSetFlags:AddCardSetFlags) ={
			// add button armed via 'acs:CardSet=> ...'
		buttonSet.grayAndDisableAddButton
		buttonSet.grayAndDisablePriorButton
			// Terminate AddCardSet series
		if(addCardSetFlags.activatedAddButton) {
			addCardSetFlags.activatedAddButton=false
			restoreCurrentCardSet
			}
		}
		// Return to previous CardSet by assigning the previous CardSet node
		// to Linker's 'iterator'. 
		// Note: CardSet and AddCardSet have separate backup mechanisms.
	def doPriorButton(buttonSet:ButtonSet, addCardSetFlags:AddCardSetFlags)={
		buttonSet.grayAndDisableAddButton
		buttonSet.grayAndDisablePriorButton
		if(whichBackup=="cardSet"){
				doPriorButtonBackup(backupMechanism)
				}
			else{ //--if else then whichBackup=="addCardSet"

					// processing Add sets so armAddButton
				buttonSet.armAddButton	
				doPriorButtonBackup(addBackupMechanism)
				}
		}

		//Execute back up operation for either CardSet or for AddCardSet
	def doPriorButtonBackup(backupMechanism:BackupMechanism) {
			// assign Linker.backup to 'iterator' to
			// enable the 'while(iterate)' to display prior Card.
		iterator=backupMechanism.loadIteratorWithBackup 		// see Linker 
		buttonSet.resetPriorButton	// 'priorButton=false
		}

		// The '*' button was activated to switch from the current script file to a second
		// script file. The '* mangage <filename>' command provides the name
		// of this second file. NotecardTask handles '* manage <filename>' command and
		// creates a Linked List structure from this file whose root is assigned to 
		// 'manageNotecard'.   The root of the List List structure's of the current file is 
		// is assigned to 'initialNotecard'.  Notecard is recursively invoked by 
		// 'manageNotecard.startNotecard(..).
	def doAsteriskButton(buttonSet:ButtonSet, taskGather:TaskGather) {
			// Session keep track of whether Notecard is invoked	
			// by 'card',i.e., 'clientNotecard'  or invoked 
			// by '*' button, i.e., 'manageNotecard'
		if(Session.initialNotecardState) { 
					// set  'manageNotecard' state false so next time '*' button is
					// activated, the 'else' group is executed
			Session.initialNotecardState=false   
			if(manageNotecard == null) {
					//'*' button activated without '* manage <filename>' command. The 
					// 'xyzxxyzv' file does not exist, so 'start' file is invoked.
				manageNotecard=CommandNetwork.loadFileAndBuildNetwork( "xyzxxyzv", symbolTable)
				}
					// 'manageNotecard' is the command linked-list hierarchy of the file
					// whose filename is an argument of NotecardTask (* manage <filename> ).
					// see 'executeOneNotecardChild'-- 'case ft:NotecardTask'. 
			if(manageNotecard != null) {     
					//Store Card node if needed to restart clientNotecard
				saveCurrentNode   // Linker
					// window may display two asterisk buttons. ensure that
					// 'initialNotecardState's asterisk button is the grayed one.
				buttonSet.grayAndDisableAsteriskButton
					//Begin executing the Card file designated
					//by '* manage <filename> in FrameTask
				manageNotecard.startNotecard(taskGather)
					// Invokes new CardWindow and setVisible. This function is
					// also invoked in 'startNotecard', However, unless it is also
					// called here, the window is blanked when 'startNotecard' 
					// returns from the manageNotecard state.
				var oldManagement=createAndMakeVisibleCardWindow (  notePanel, 
																	buttonPanel,
																	statusLine, 
																	frame_width, 
																	frame_height)	
					// Put on List to be invoked in 'card' to dispose of
					// window resources.
				taskGather.addOldJFrameList(oldManagement)
					// Display the Card from which the '*' button was activated
				restoreCurrentNode //Linker   Restore clientNotecard
					// reestablish 'initialNotecardState's grayed button is high-
					// lighted.
				buttonSet.armAsteriskButton
			   	}
				
			 else println("Notecard: manageNotecard is NULL unable to call startFile?")
			}
				// Management system currently running. The following code
				// switches system back to the initial system.
		  else{	
					// set 'manageNotecard' state 'true' so the next time '*' is 
					// activated the 'then' group is executed
				Session.initialNotecardState=true // 
					// Notecard's entry point is 'startNotecard'.  The thrown
					// exception is caught by 'startNotecard', causing it to
					// be returned to the invoking function.  Since the current
					// state is 'manageNotecard',then  the invoking function has to
					// be Notecard, rather than 'card'. 
				throw new AsteriskException
				}
		}

	def doAddButton(buttonSet:ButtonSet,
					cardSet:CardSet,
					addCardSetFlags:AddCardSetFlags)={
			 // Restores the parent CardSet (see: doNextButton(... ) 
		addCardSetFlags.activatedAddButton=true

			// Last AddCardSet restores associated CardSet
			// last AddCardSet in series has a button value
			// of 99, therefore, terminate the series.
		if(cardSet.isLastAddCardSet){
				// Prevents addCardSet(s) from being executed.
			addCardSetFlags.activatedAddButton=false
				//Enable CardSet to repeat
			restoreCurrentCardSet  // see Linker trait
			}

		}
			// Invokes new CardWindow (extends JFrame) and setVisible
	def createAndMakeVisibleCardWindow( notePanel:JPanel,
										buttonPanel:JPanel,
										statusLine:JLabel,
										frame_width:Int,  // object parameter
										frame_height:Int) // object parameter
										={
			// Creates the notecard window (JFrame) with a BorderLayout
			// and adds Note and Button panels to this window along with
			// statusLine:JLabel.
			// Also allows window size to change (height,width)
		val window= new CardWindow(notePanel, buttonPanel, statusLine, frame_width, frame_height)
		window.setVisible(true)
		window
		}
		// CreateClass generates an instance of Notecard without fields or parameters.
		// However, it invokes 'receive_objects' to load parameters from *.struct
		// file as well as symbolic addresses to be physical ones. 
	def receive_objects(structSet:List[String]) {
			import util.control.Breaks._
			var flag=true
			for( e <- structSet) {
			  breakable { if(e=="%%") break   // end of arguments
			  else {
				var pair=e.split("[\t]")	
				pair(0) match {
					case "child" => 
							setChild(pair(1))
					case "height" => 
							frame_height=pair(1).toInt
					case "width" => 
							frame_width= pair(1).toInt
					case "size" => 
							fontSize= pair(1).toInt
					case "style"=>
							fontStyle= pair(1).toInt
					case "fontSize" | "font_size" => 
							font_size= pair(1).toInt
					case "fontStyle" =>
							fontStyle= pair(1).toInt
					case "fontName" =>
							fontName= pair(1)
					case "name" =>
							fontName=pair(1)
					case "asteriskButton"=>          // either "on" or "off"
							asteriskButtonState= pair(1)
								// 'isAsteriskButtonOn' will disable '*' button 
								//  when 'asteriskButtonState'= 'off'.
							buttonSet.isAsteriskButtonOn=asteriskButtonState 
					case "priorButton" =>
							priorButtonState=pair(1)
								// 'isPriorButtonOn' will disable PRIOR button 
								//  when 'priorButtonState'= 'off'.
							buttonSet.isPriorButtonOn= priorButtonState   
					case _=> println("Notecard: unknown argument="+e)
					}
				}
			   }  //breakable		 
			  }
		}	

}
