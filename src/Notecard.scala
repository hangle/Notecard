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
		// Create three buttons with actionListeners.
		// Buttons are added to panel.Lock to issue notifyAll
	val buttonPanel= new JPanel		
		// Create buttons '*', 'PRIOR', 'NEXT'
	val buttonSet= new ButtonSet(buttonPanel, lock) //Buttons:  Next, Prior, and ' * '
		// Create a JPanel with a layout of NoteLayout and
		// enclose it in a black border.
	val notePanel=new NotePanel().getNotePanel 
		// Save current CardSet to be restore  after AddCardSet
		// have exe
	var currentCardSet:Node= _
		// Used in 'doAsteriskButton' to hold JFrame reference to
		// disposed of Management file.
	val backupMechanism= new BackupMechanism
		// FrameTask was passed a '* manage <filename>' command and CardSet
		// has instantiated a Notecard object. This object is created  in 
		// NotecardTask and is passed here from 'taskGather'. 
	var manageNotecard:Notecard= _

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
			// Load 'oldJFrame' on list to be disposed by 'card' when
			// the *.struct file ends.
		taskGather.addOldJFrameList(taskGather.oldJFrame)
				try{ 
			// Execute Notecard's children (CardSet, NextFile, NotecardTask)
		iterateNotecardChildren(notePanel, taskGather, buttonSet, defaultFont)
						// doAsteriskButton() throws the exception caught here. This function
						// has recursively invoked Notecard and the exception is a way
						// to continue processing its children.
				}catch { case ex: Exception=> 
					//	oldJFrameManagement.dispose()
						}
		} 
		// CardSet, FrameTask, and Filename objects of Notecard(parent) are
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
				// If '* end' or 'f <filename>' commands are detected, 
				// then 'isTaskNone' is false, causing the iteration to
				// be terminated, returning control to 'card'. 
			if(taskGather.isTaskNone) {
					// process CardSet or FrameTask or NextFile. 'Value' returned from 'iterate'.
				executeNotecardChildren(node, notePanel, taskGather, buttonSet, defaultFont)
				}
			}      //---while
		}
		//Notecard's children: CardSet, FrameTask, NextFile, LoadDictionary
	def executeNotecardChildren(obj:Node, 
								notePanel:JPanel, 
								taskGather:TaskGather, 
								buttonSet:ButtonSet,
								defaultFont:DefaultFont) { 
		obj match	{
				// CardSet executes a series of commands that
				// constitute a single Card set. 
			case cs:CardSet=> //println("cs:CardSet") //
						// Do CardSet if condition is true or 'c' command lacks condition.
						// Otherwise skip 'wait' so as to process next CardSet.
						// 	If the 'c' command has logic ,e.g, 'c (2)=(3)', then the logic
						//	is tested and the CardSet instances is skipped when 'false',
						//  If 'c' cmd has no logic, then outcome is always 'true'.
				if(cs.noConditionOrIsTrue( symbolTable)){	//'c <cmd> has no logic /
						// 1st sibling to pass condition test is stored in 'firstChild'.
					backupMechanism.captureFirstChild(node)
						// save all 'node's to be used to back up to prior CardSet
					backupMechanism.storePriorSiblingInBackupList( node)
						// Activate CardSet to process RowerNode,
						// Assigner, CardSetTask, GroupNode, XNode to
						// present one Card. CardSet enters a wait() state
						// until button( NEXT,PRIOR,'*') is pressed, thus causing
						// it to return.
					cs.startCardSet(notePanel, 
									lock, 
									buttonSet, 
									statusLine, 
									backupMechanism,
									defaultFont,
									false)
						// wait() of CardSet just released. Determine if either
						// backup button or * asterisk button or '+Add' button was 
						// activated. If so, than take care of button activated.
					waitOverDoButtons(taskGather, cs,notePanel,lock,buttonSet, statusLine, defaultFont)
					}
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
		// Invoked when CardSet returns from  executeNotecardChildren()
		// Determine if and which button (Prior, +Add ,Next or '*')  was activated.
	def waitOverDoButtons(taskGather:TaskGather, 
						  cardSet:CardSet,
	    				  notePanel:JPanel, 
						  lock:AnyRef, 
						  buttonSet:ButtonSet, 
						  statusLine:StatusLine,
						  defaultFont: DefaultFont):Unit={ 
		buttonSet.selectedButton match {
				case "next"  =>
						buttonSet.grayAndDisableAddButton
				case "prior" =>
						buttonSet.grayAndDisableAddButton
						doPriorButton// load Linker.doIteratorWithBackup			
				case "*" =>
						doAsteriskButton(taskGather)
				case "+" =>
						doAddCardSet(taskGather,
										cardSet,
										notePanel,
										lock,
										buttonSet,
										statusLine,
										defaultFont)
				case _=>
							println("Notecard: unknown button actionPerformed")
				}
		}
	def waitOverInAddDoButtons(taskGather:TaskGather, 
						  cardSet:CardSet,
	    				  notePanel:JPanel, 
						  lock:AnyRef, 
						  buttonSet:ButtonSet, 
						  statusLine:StatusLine,
						  addBackupMechanism:BackupMechanism):Unit={ 
		buttonSet.selectedButton match {
			case "next"  =>
							//println("Notecard case next")
					buttonSet.grayAndDisableAddButton
						// in the +Add-CardSet operation, the Next

						// null will escape 'while(iterate)' in 
						// 'doAddCardSet'.
					iterator=null
			case "prior" =>
							//println("Notecard case prior")
					buttonSet.grayAndDisableAddButton
					doAddPriorButton(addBackupMechanism)			
			case "*" =>
							//println("Notecard case +")
					doAsteriskButton(taskGather)
			case "+" =>
							//println("Notecard case +")
						// do nothing
			case _=>

					println("Notecard: unknown add button actionPerformed")
			}
		}
			// Invoked when '+Add' button activated
	def doAddCardSet(taskGather:TaskGather, 
					 cardSet:CardSet, 
					 notePanel:JPanel, 
					 lock:AnyRef, 
					 buttonSet:ButtonSet, 
					 statusLine:StatusLine, 
					 defaultFont:DefaultFont)={ 
			// Node.symButton !="0" indicates that CardSet has a child AddCardSet
			// Save Link iterator's node in order to restore this node
			// when button CardSets end.
		saveCurrentCardSet	  // iterator's node is saved in Notecard.currentCardSet. 
			// Add-CardSet has its own backup system so create class.
		val addBackupMechanism= new BackupMechanism
			// fetch physical address of AddCardSet from parent CardSet that
			// addresses the 1st Add-CardSet.
		val addButton= cardSet.getAddCardSet
		val addCardSet= addButton.asInstanceOf[CardSet]
				// AddCardSet(s) will be processed as if they are CardSet(s).
		reset(addCardSet)
				// loop one or more AddCardSet(s). iterate() returns 'node'
				// as current CarsSet.
		while(iterate) {
			node match {
				case cs:CardSet=>
							// 	If the 'c' command has logic ,e.g, 'c (2)=(3)', then the logic
							//	is tested and the CardSet instances is skipped when 'false',
							//  If 'c' cmd has no logic, then outcome is always 'true'.
					if(cs.noConditionOrIsTrue( symbolTable)){	//'c <cmd> has no logic /
							// 1st sibling to pass condition test is stored in 'firstChild'.
						addBackupMechanism.captureFirstChild(node)
							// save all 'node's to be used to back up to prior Add-CardSets
						addBackupMechanism.storePriorSiblingInBackupList( node)
							// process Add-CardSet children
						cs.startCardSet(notePanel, 
										lock, 
										buttonSet, 
										statusLine, 
										addBackupMechanism, 
										defaultFont,
										true)   // indicate AddCardSet
							// Next button terminates iterate loop by setting
							// Linker.iterator to null. +Add button does nothing
							// so looping continues. 
						waitOverInAddDoButtons(taskGather, 
												cs, 
												notePanel, 
												lock, 
												buttonSet, 
												statusLine, 
												addBackupMechanism)
						}
				case _=> println("Notecard: Unknown Value")
			}
	//		buttonSet.grayAndDisableAddButton
		}
			// Restore Link iterator to the CardSet from which '+Add' button was
			// activated. 
		restoreCurrentCardSet
	}
		// keep in order to restore after management file completes.
	def saveCurrentCardSet { currentCardSet=node }
	def restoreCurrentCardSet { iterator=currentCardSet }
		//Backup set up
	def doPriorButton {
			// assign Linker.backup to 'iterator' to
			// enable the 'while(iterate)' to display prior Card.
		iterator=backupMechanism.loadIteratorWithBackup 		// see Linker 
		buttonSet.resetPriorButton	// turn off backup mechanism
		}
		//Backup set up
	def doAddPriorButton(addBackupMechanism:BackupMechanism) {
			// assign Linker.backup to 'iterator' to
			// enable the 'while(iterate)' to display prior Card.
		iterator=addBackupMechanism.loadIteratorWithBackup 		// see Linker 
		buttonSet.resetPriorButton	// turn off backup mechanism
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
		val window= new CardWindow(notePanel,buttonPanel, statusLine, frame_width, frame_height)
		window.setVisible(true)
		window
		}
		// The '*' button was activated to switch from the current script file to a second
		// script file. The '* mangage <filename>' command provides the name
		// of this second file. NotecardTask handles '* manage <filename>' command and
		// creates a Linked List structure from this file whose root is assigned to 
		// 'manageNotecard'.   The root of the List List structure's of the current file is 
		// is assigned to 'initialNotecard'.  Notecard is recursively invoked by 
		// 'manageNotecard.startNotecard(..).
	def doAsteriskButton(taskGather:TaskGather) {
			// Session keep track of whether Notecard is invoked	
			// by 'card',i.e., 'clientNotecard'  or invoked 
			// by '*' button, i.e., 'manageNotecard'
		if(Session.initialNotecardState) { 
					// set  'manageNotecard' state false so next time '*' button is
					// activated, the 'else' group is executed
			Session.initialNotecardState=false   
					//buttonSet.grayAndDisableAsteriskButton
			if(manageNotecard == null) {
					//'*' button activated without '* manage <filename>' command. The 
					// 'xyzxxyzv' file does not exist, so 'start' file is invoked.
				manageNotecard=CommandNetwork.loadFileAndBuildNetwork( "xyzxxyzv", symbolTable)
				}
					// check if 'loadFileAnd...' changed variable's state.
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
				throw new Exception
				}
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
