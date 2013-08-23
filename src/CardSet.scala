/* 
date:   Oct 26, 2011
				CARD SET

	Notecard is the parent of CardSet; it hands off one cards set 
	at a time.  CardSet is the parent of a set of commands 
	(Card Set). It represents a single Card.

	A Card Set of Card consist of
		RowerNode	    --display text and capture input fields
		CardSetTask 	--<asterisk> commands
		GroupNode	    --combines commands associated with logic
		AssignerNode	--assign and math operation
		XNode			--execute preceeding Card cmds
	CardSet begins the Card set by clearing the screen, e.g., 
	'c '<clear command>. 

	If the 'c' command has logic ,e.g, 'c (2)=(3)', then the logic
	is tested and the CardSet instances is skipped when 'false',
	allowing the next CardSet (card set) instance to execute.
	  

	CardSet iterates through its children (see iterateChildren() )
	then issues a 'wait' condition, allowing the user to enter
	one or more responses (BoxField) or to activate the 'Next'
	button (ButtonSet), causing the next card set commands to execute.
	ButtonSet raises 'notifyAll', releasing the 'wait'.
*/ 

package com.client
import scala.collection.mutable.Map
import collection.mutable.ArrayBuffer
import java.awt.Font
import javax.swing._
case class CardSet(var symbolTable:Map[String,String]) extends Linker{
															/*
				Linker extends Node
symbolTable holds $<variables>		def setId
					def convertToSibling
					def convertToChild
				Linker
					def reset
					def iterate
					def Value
		*/
//------------paramters pass by .struct file-----------------
	var node_name=""
				// such as:  c (1)=(2)
				// If so, then 'condition' is converted from a symbolic 
				// to a physical address of a Condition object.
	var go_continue="" //not operational
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node]) ={
			convertToSibling(swizzleTable)
			convertToChild(swizzleTable)  // Is a parent
			}
//-------------------------------------------------------------------

	var groupResolve:GroupResolve=null //1st GroupNode of card set will instantiate
									   // GroupResolve. Nulled at end of group.
	val ThenNode=1        	 	//Has condition expression.e.g., (1)=(1)
	val ElseNode=2		   		//post is "else" but not condition
	val ElseConditionNode=3		//post is "else" and has condition
	val EmptyNode=4	

	var listeners:List[KeyListenerObject]=Nil
//------------paramters pass by .struct file-----------------
	var conditionStruct=""
//----------------------------------------
	val indexer=new Indexer(-1) //Indexer has member 'index' that is initialized to 
		//minus one. RowerNode increments this index each time a
		//KeyListenerObject is created giving this object a 
		// unique index
	def startCardSet(notePanel:JPanel, lock:AnyRef, buttonSet:ButtonSet, statusLine:StatusLine)={ 
			// Assign Linker.next to 'backup'. The next time
			// CardSet is executed, 'backup' holds the pointer
			// to the prior Card. 
			// Used to capture one or more input fields.
		val inputFocus= new InputFocus(buttonSet ) 
			// prior card set may have posted a status message so remove for new card.
		statusLine.clearStatusLine 
			// 	If the 'c' command has logic ,e.g, 'c (2)=(3)', then the logic
			//	is tested and the CardSet instances is skipped when 'false',
			//  If 'c' cmd has no logic, then outcome is always 'true'.
		if(noConditionOrIsTrue(conditionStruct, symbolTable)){	//'c <cmd> has no logic /
			// or has logic and the logic is true
			// Collection container for all KeyListernerObject(s)
			// created in RowerNode.  At the completion of the
			// card, listeners are removed via BoxField.
			// removeKeyListener().  If not removed, then on backup
			// with the PRIOR button, each BoxField would have 2 or
			// more KeyListerObject(s).
		val listenerArray= new ArrayBuffer[KeyListenerObject]
			// Used by Visual objects (DisplayText, DisplayVariable, BoxField) in 
			// collaboration with RowerNode. RowerNode passes row and column placement
			// values to RowPosition.  These value are converted to pixels.  When the
			// "next" row is to be displayed, RowPosition.currentPosition becomes
			// the 'y' value java.awt.Component.setBounds(x,y,width, height)
		val rowPosition=initializeRowPosition(18) //***skip*** see LabelPixelHeight.java
			// Iterate card commands then display	
		executeCardCommandsAndDisplay(notePanel, 
					  rowPosition,
					  lock, 
					  inputFocus, 
					  indexer, 
					  statusLine, 
					  listenerArray) 
				// ------------Card Set has been displayed--------------
				// arm Buttons and enter 'wait' state. 
				// Button press releases 'wait' state (ButtonSet.start() ):"
				// do not enable PRIOR button for 1st CardSet child since there is
				// nothing more to back up to. 
		if(buttonSet.isFirstChildFalse) {
				buttonSet.turnOnPriorButton
				}
				// When Card lacks input fields, then turn on NEXT button in
				// order to transit to next Card. With one or more input 
				// fields, InputFocus will turn on NEXT button. 
		if(inputFocus.isNoInputFields){			// True if no input fields 
				// Enable NEXT button, give it
				// focus and color it orange
				buttonSet.armNextButton	
				}
		showPanel(notePanel) // display panel content (paint, validate)
			// Stop (issue wait()) to allow the user to enter responses.
		haltCommandExecution(lock)	
		clearNotePanel(notePanel)	//remove all components & clear screen
						// KeyListenerObject(s) are removed from the
						// corresponding BoxField
		removeInputFieldListeners(listenerArray) 
		}
	}// control returns to Notecard to process the next card set
	def executeCardCommandsAndDisplay(notePanel:JPanel,
					  rowPosition:RowPosition,
					  lock:AnyRef,
					  inputFocus:InputFocus,
					  indexer:Indexer, 
					  statusLine: StatusLine,
					  listenerArray: ArrayBuffer[KeyListenerObject]) {
			reset(getFirstChild)    //point to head of linked list (setFirstChild)
			iterateCardSetChildren( rowPosition, 
					  notePanel, 
					  lock, 
					  inputFocus, 
					  indexer, 
					  statusLine, 
					  listenerArray)
		}
		// Card set consist of RowerNode, Assigner, CardSetTask 
		// GroupNode, and XNode
	def iterateCardSetChildren(rowPosition:RowPosition, 
				 notePanel:JPanel, 
				 lock:AnyRef, 
				 inputFocus:InputFocus,
				 indexer:Indexer,
				 statusLine:StatusLine, 
				 listenerArray: ArrayBuffer[KeyListenerObject]) {
		reset(getFirstChild)
			// iterate initialized in executeCardCommandsAndDisplay
		while(iterate) 
			// Value returns 'next' of Node which 
			// references the next sibling of
			// resetBackupButton= backupButton=false 
			// set false in Notecard after executing
		 	// prior Card.
			executeCardSetChildren(Value, 
					 rowPosition, 
					 notePanel, 
					 lock:AnyRef, 
					 inputFocus, indexer, statusLine, listenerArray)
		}
		// Children separated by match statement to invoke their respective modules
	def executeCardSetChildren(obj:Any,  // children
				 rowPosition:RowPosition, 
				 notePanel:JPanel, 
				 lock:AnyRef, 
				 inputFocus:InputFocus, 
				 indexer:Indexer,
				 statusLine:StatusLine,
				 listenerArray:ArrayBuffer[KeyListenerObject]) {	
		obj match	{
			case rn:RowerNode=> // children DisplayText,DisplayVariable,BoxField
				rn.startRowerNode(rowPosition, 
						notePanel, 
						inputFocus, 
						indexer:Indexer, 
						statusLine,
						listenerArray) //recusion
		case as:Assigner=> 
				as.startAssigner
			case cst:CardSetTask =>
				cst.startCardSetTask (inputFocus, statusLine, notePanel)
			case gn:GroupNode=> 
			 		// GroupResolve created if null so as to exist until 'g' 
					// or end of Card	
				createGroupResolveAndPassItGroupNode(gn) 
					// GroupResolve asks GroupNode.whatKind as to Then, Else, 
					// ElseCondition,or Empty; and following evaluation,  
					// returns 'skip' or 'do'.  If 'do' then the group of 
					// commands is executed; if 'skip', then the group is bypassed.
					whatToDo(groupResolve, 
							 obj, 
							 rowPosition, 
							 notePanel, 
							 lock, 
							 inputFocus, 
							 indexer, 
							 statusLine, 
							 listenerArray) //recusion
			case xn:XNode=> 
				showPanel(notePanel)
				inputFocus.turnOnXNode  //prevents InputFocus.actWhenAllFieldsCaptured 
							// from enabling NEXT button
				haltCommandExecution(lock) // issue lock.wait()
			case _=> println("\tCardSet: unknown CardSetChild obj="+obj)	
			}
		}
	/*
	---------------------------------------------------------------
						GroupNode
	Example script code:
		g (1) =(2)
		d this will not be seen
		d also this
		g
	GroupResolve detects that subsequent commands( two 'd' cmds) are to be skipped.
	The second 'g' command ends the scope of the Group's control.

	----------------------------------------------------------------
	*/
		// Invoked in 'iterateCardSetChildren when GroupNode child 
		// is matched. Calls on GroupResolve to decide whether to 
		// "do" or "skip" or create a new GroupResolve object
		// Note, when "do", 'iterateCardSetChildren' is called
	def whatToDo(   groupResolve: GroupResolve, 
			obj:Any, 
			rowPosition:RowPosition, 
			notePanel:JPanel, 
			lock:AnyRef,
			inputFocus:InputFocus,
			indexer:Indexer,
			statusLine:StatusLine,
			listenerArray:ArrayBuffer[KeyListenerObject]):Unit= {
		groupResolve.actionToTake match{
			case  "do"  => // test was successful so execute to next 'g' or end of Card set
				iterateCardSetChildren(rowPosition, 
							notePanel, 
							lock, 
							inputFocus, 
							indexer, 
							statusLine, 
							listenerArray)
			case  "skip" =>  // test was false, so skip to next 'g' or to end of Card set
					//finds next 'g' cmd or returns null when no 'g' found
				val groupNode=iterateToNextGroup(groupResolve, 
								 obj, 
								 rowPosition, 
								 notePanel, 
								 lock, 
								 inputFocus, 
								 indexer, 
								 statusLine, 
								 listenerArray)
				if(groupNode==null) println("CardSet:  groupNode is null"); 
					else println("FRameNode groupNode is NOT null")
				if(groupNode!=null) { 
						// Create GroupResolve if not already created and
						// attach GroupNode instance to it.
					createGroupResolveAndPassItGroupNode(groupNode) //creates 
						// GroupResolve asks GroupNode.whatKind as to Then, Else, ElseCondition,or Empty;
						// and following evaluation,  returns 'skip' or 'do'.  If 'do' then the group of 
						// commands is executed; if 'skip', then the group is bypassed.
					whatToDo(groupResolve, 
						 obj, 
						 rowPosition, 
						 notePanel, 
						 lock, 
						 inputFocus, 
						 indexer, 
						 statusLine, 
						 listenerArray)
					}
					// No 'else' and no condition
			case  "done"=> 
			}
		}
		//skip objects that are within the scope of GroupNode whose outcome 
		//is 'false'. recursive, skipping objects, until finding the next
		// GroupNode object or reaching the end of the card set of objects.
	def iterateToNextGroup( groupResolve: GroupResolve, 
				obj:Any, 
				rowPosition:RowPosition, 
				notePanel:JPanel, 
				lock:AnyRef,
				inputFocus:InputFocus,
				indexer:Indexer,
				statusLine:StatusLine,
				listenerArray:ArrayBuffer[KeyListenerObject]):GroupNode= {
		var gNode:GroupNode=null
		if(iterate) {  //iterate is method of Core returning Value (cmd object, eg, GroupNode)
			       // iteration terminates at end of cmd set, returning 'null'
			val any:Any=Value match{			
				   //case gn:GroupNode=> gn  //found next 'g' cmd, so stop iteration
				case gn:GroupNode=> 
						// Create GroupResolve if not already created and
						// attach GroupNode instance to it.
					createGroupResolveAndPassItGroupNode(gn) //creates 
						// GroupResolve asks GroupNode.whatKind as to Then, Else, 
						// ElseCondition,or Empty;
						// and following evaluation,  returns 'skip' or 'do'.  
						// If 'do' then the group of 
						// commands is executed; if 'skip', then the group is bypassed.
					whatToDo(groupResolve, 
							 obj, 
							 rowPosition, 
							 notePanel, 
							 lock, 
							 inputFocus, 
							 indexer, 
							 statusLine, 
							 listenerArray)	
								//keep looking for 'g' cmd
					gNode=gn
				case _=> 
					gNode=iterateToNextGroup(groupResolve, 
								 obj, 
								 rowPosition, 
								 notePanel, 
								 lock, 
								 inputFocus, 
								 indexer, 
								 statusLine, 
								 listenerArray)
					}
			}

		gNode
		}
		// created by GroupNode child iteration or by whatToDo(...)
	def createGroupResolveAndPassItGroupNode(groupNode:GroupNode) {
				// Only one GroupResolve is allocated for the CardSet, so
				// Card will not support more than one Group set ???????
				// Need to remove GroupResolve when 'g' or end of Card.--to do
			if(groupResolve==null) 
				groupResolve=new GroupResolve()
				// GroupResolve uses GroupNode's 'isConditionTrue' to test
			groupResolve.attachGroupNode(groupNode)
			}
//---------------------------------------------------------------------
 	def showPanel(notePanel:JPanel) {
			// JPanel extends JComponent having
			// validate(), and repaint()
		notePanel.validate()
		notePanel.repaint()
		}
			//wait() released by notifyAll by "Next button" in ButtonSet
	def haltCommandExecution(lock:AnyRef): Unit=lock.synchronized {
		println("CardSet lock here")
		lock.wait()	
		}
	def clearNotePanel(notePanel:JPanel) {
			// JPanel extends JComponent having
			// val, statusLine:StatusLine, idate(), and repaint()/
		notePanel.removeAll()  // removes all components from panel
		notePanel.repaint()
		}
		// In RowerNode, the BoxField object is an argument to
		// KeyListenerObject.  BoxField invokes its method
		// addKeyListener(this), where this is the KeyListenerObject. 
	def removeInputFieldListeners(listenerArray:ArrayBuffer[KeyListenerObject]) {
			// Iterate all, if any,  KeyListenerObject(s) created 
		for (e <- listenerArray) {
				//KeyListenerObject returns the BoxField constructor instance.
			e.getBoxField.removeKeyListener(e)
			}
		}
		//Determines whether or not the card is displayed
	def noConditionOrIsTrue(condition:String, symbolTable:Map[String,String]) ={
		if(condition != "")
				LogicTest.logicTest(condition, symbolTable)
			else
				true   // No logic expression  
		}
	def initializeRowPosition(size:Int)= { new RowPosition(new Font("Serif", 0, size)) }

	def receive_objects(structSet:List[String]) {
			//Load class instance with argument 
		val in=structSet.iterator
		setChild(in.next)	
		setAddress(in.next)
		setNext(in.next)
		node_name=in.next
		conditionStruct=in.next
		if(conditionStruct=="null") //inconsistent use of "" and "null" in <.struct> file
			conditionStruct=""
		
		}

	}

