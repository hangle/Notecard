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
										symbolTable holds $<variables>		
				Linker extends Node
					def setId
					def convertToSibling
					def convertToChild
				Linker
					def reset
					def iterate
					var node
		*/
//------------paramters pass by .struct file-----------------
	var conditionStruct=""
	var node_name=""   /// Name or label of CardSet:  <not operational>
//------------------------------swizzle routines (see: Node)-----------
	def convertToReference(swizzleTable:Map[String, Node]) ={
			convertToSibling(swizzleTable)
			convertToChild(swizzleTable)  // Is a parent
			convertToAddButton(swizzleTable) // ButtonCardSet
			}
				// 'button' is the physical addr of the ButtonCardSet. If CardSet has no
				// associated ButtonCardSet, then button equals 'null'.
	def getAddCardSet=addButton  //invoked in Notecard by 'loadIteratorWithButton..' prior
								 // to calling 'cardSet.startCardSet(...)
//-------------------------------------------------------------------
//	var notecardButtonSet:ButtonSet=null
	var groupResolve:GroupResolve=null //1st GroupNode of card set will instantiate
									   // GroupResolve. Nulled at end of group.

			// Indexer has member 'index' that is initialized to 
			// minus one. RowerNode increments this index each time a
			// KeyListenerObject is created giving this object a unique index
	val indexer=new Indexer(-1) 
			// Notecard sets it to 'true' when '+Add' button is activated.

			// CardSet may point to a AddCardSet object, or AddCardSet object
			// has return pointer to parent 'button' of CardSet. [symButton.Node.scala:
			// similar to 'symChild' and 'symSibling' ]. Used by Notecard
	def isAddCardSet= { if(symAddButton != "0") true; else false}// Invoked by Notecard parent
			// Invoked by Notecard
	def startCardSet(notePanel:JPanel, 
					 lock:AnyRef, 
					 buttonSet:ButtonSet, 
					 statusLine:StatusLine,
					 backupMechanism:BackupMechanism)={ 
			// Assign Linker.next to 'backup'. The next time
			// CardSet is executed, 'backup' holds the pointer
			// to the prior Card.  Used to capture one or more input fields.
		val inputFocus= new InputFocus(buttonSet, backupMechanism ) 
			// set true at end
		val listenerArray= new ArrayBuffer[KeyListenerObject]
			// Used by Visual objects (DisplayText, DisplayVariable, BoxField) in 
			// collaboration with RowerNode. RowerNode passes row and column placement
			// values to RowPosition.  These value are converted to pixels.  When the
			// "next" row is to be displayed, RowPosition.currentPosition becomes
			// the 'y' value java.awt.Component.setBounds(x,y,width, height)
		val rowPosition=initializeRowPosition(18) //***skip*** see LabelPixelHeight.java
			// set true following execution of CardSet children
		inputFocus.completedCardSetIteration=false
			// prior card set may have posted a status message so remove for new card.
		statusLine.clearStatusLine 
			// 	symButton not zero indicating AddCardSet object
		if(isAddCardSet)
				// color button yellow and enable it
			buttonSet.armAddCardSet
				// Iterate CardSet commands then display	
		executeCardCommandsAndDisplay(notePanel, 
									  rowPosition,
									  lock, 
									  inputFocus, 
									  indexer, 
									  statusLine, 
									  listenerArray) 
				// set false before iteration. Input focus can arm Prior button.
		inputFocus.completedCardSetIteration= true
				// Enable * button for Management file
		buttonSet.armAsteriskButton
				// When Card lacks input fields, then turn on NEXT button in
				// order to transit to next Card. With one or more input 
				// fields, InputFocus will turn on NEXT button. 
		if(inputFocus.isNoInputFields){			// True if no input fields,i.e. (# $abc) 
						// Also enable PRIOR button when not first CardSet
				if( ! backupMechanism.isFirstChild) {
					buttonSet.armPriorButton
					}
						// Enable NEXT button, give it  focus and color it orange
				buttonSet.armNextButton	
				buttonSet.next.requestFocus
				}
		showPanel(notePanel) // display panel content (paint, validate)
			// Invoked by CardSet (2 places) just before 'haltCommandExecution'.
			// Note: focus not requested when CardSet has no InputFields (counterInputFields==0)
			// or when 'actWhenAllFieldsCaptured' set this value to 0.
		inputFocus.giveFocusToFirstInputField
			// Stop (issue wait()) to allow the user to enter responses.
		haltCommandExecution(lock)	

		clearNotePanel(notePanel)	//remove all components & clear screen
							// KeyListenerObject(s) are removed from the
							// corresponding BoxField
		removeInputFieldListeners(listenerArray) 
	}// control returns to Notecard to process the next card set

	def executeCardCommandsAndDisplay(notePanel:JPanel,
									  rowPosition:RowPosition,
									  lock:AnyRef,
									  inputFocus:InputFocus,
									  indexer:Indexer, 
									  statusLine: StatusLine,
									  listenerArray: ArrayBuffer[KeyListenerObject]) {
			reset(child)    //point to head of linked list
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
	def iterateCardSetChildren(  rowPosition:RowPosition, 
								 notePanel:JPanel, 
								 lock:AnyRef, 
								 inputFocus:InputFocus,
								 indexer:Indexer,
								 statusLine:StatusLine, 
								 listenerArray: ArrayBuffer[KeyListenerObject]) {
			// Iterate children (via sibling nodes), returning 'node' as
			// either the 1st child or the current sibling node.
			// see 'Linker' trait.
		while(iterate) // initialized by 'reset(child)'
				// Execute RowerNode, Assigner, CardSetTask, GroupNode, or eXecute.
			executeCardSetChildren(  node, // Current sibling--see Linker  node,   // current  node 
									 rowPosition, 
									 notePanel, 
									 lock:AnyRef, 
									 inputFocus, 
									 indexer, 
									 statusLine, 
									 listenerArray)
		}
		// Children separated by match statement to invoke their respective modules
	def executeCardSetChildren(  obj:Node,  // children
								 rowPosition:RowPosition, 
								 notePanel:JPanel, 
								 lock:AnyRef, 
								 inputFocus:InputFocus, 
								 indexer:Indexer,
								 statusLine:StatusLine,
								 listenerArray:ArrayBuffer[KeyListenerObject]) {	
		obj match	{
			case rn:RowerNode=> 
						// children: DisplayText,DisplayVariable,BoxField
				rn.startRowerNode(rowPosition, 
								  notePanel, 
								  inputFocus, 
								  indexer:Indexer, 
								  statusLine,
								  listenerArray) //recusion
			case as:Assigner=> 
						// Assign command, e.g., 'a flag=true' or 'a count=count+1'.
				as.startAssigner
			case cst:CardSetTask =>
						// * asterisk commands for '* status <msg>' and 
						// '* continue'.
				cst.startCardSetTask (inputFocus, statusLine, notePanel)
			case gn:GroupNode=> 
						// GroupResolve created if null so as to exist until 'g' 
						// or end of CardSet	
				createGroupResolve
						// Determine whether to 'do' the enclosed commnds of the 
						// Group command or to 'skip' these commands. 
				whatToDo(groupResolve, 
					 	 gn,
						 rowPosition, 
						 notePanel, 
						 lock, 
						 inputFocus, 
						 indexer, 
						 statusLine, 
						 listenerArray) //recusion
			case xn:XNode => 
						// 'x' command to process prior input fields.
				showPanel(notePanel)
					// Invoked by CardSet (2 places) just before 'haltCommandExecution'.
					// Note: focus not requested when CardSet has no InputFields (counterInputFields==0)
					// or when 'actWhenAllFieldsCaptured' set this value to 0.
				inputFocus.giveFocusToFirstInputField
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
	The second 'g' command ends the scope of the Group's control. Scope also ends
	at end of the CardSet.

	----------------------------------------------------------------
	*/
		// Recursive function calling itself when GroupResolve.actionToTake'
		// returns 'skip'.
		// Invoked in 'CardSet.executeCardSetChildren' when GroupMatch instance
		// is matched. 
		// 'whatToDo()' uses 'GroupResolve.actionToTake()' to decide whether to 'do'
		// or 'skip' the commands enclosed by the Group command. 'actionToTake()' also
		// returns 'done' which terminates the recursive function.
	def whatToDo(   groupResolve: GroupResolve, 
					//obj:Any, 
					groupNode:GroupNode,
					rowPosition: RowPosition, 
					notePanel: JPanel, 
					lock:AnyRef,
					inputFocus:InputFocus,
					indexer:Indexer,
					statusLine:StatusLine,
					listenerArray:ArrayBuffer[KeyListenerObject]):Unit= {
				// 'actionToTake()' returns 'do', 'skip' and 'done' by determining the type 
				// of Goup command. The types are:  g <condition>, ge <condition>, ge, and g.  
				// 'actionToTake() also determines whether the <condition> expression is true
				// or not.
		groupResolve.actionToTake(groupNode) match{
			case  "do"  => 
						// Outcome successful, so executed the enclosed Group commands
				iterateCardSetChildren(	rowPosition, 
										notePanel, 
										lock, 
										inputFocus, 
										indexer, 
										statusLine, 
										listenerArray)
			case  "skip" =>  
						// Outcome unccessful, so skip the enclosed Group commands.
				iterateToNextGroup(groupResolve, 
								  // groupNode, 
								   rowPosition, 
								   notePanel, 
								   lock, 
								   inputFocus, 
								   indexer, 
								   statusLine, 
								   listenerArray)
							// A Group command having just the tag 'g'. --no 'else' and no condition
			case  "done"=> 
			}
		}
		//Skips objects that are within the scope of GroupNode whose outcome 
		//is 'false'. Recursive, skipping objects, until finding the next
		// GroupNode object or reaching the end of the card set of objects.
	def iterateToNextGroup( 	groupResolve: GroupResolve, 
							//	obj:Any, 
								rowPosition:RowPosition, 
								notePanel:JPanel, 
								lock:AnyRef,
								inputFocus:InputFocus,
								indexer:Indexer,
								statusLine:StatusLine,
								listenerArray:ArrayBuffer[KeyListenerObject]) {
			// Process just one command. If the command is not 'GroupNode' then call itself
			// to process the next command.
		if(iterate) {  //iterate is method of Core returning node (cmd object, eg, GroupNode)
			       // iteration terminates at end of cmd set, returning 'null'
			//val any:Any=Value match{			
			val any:Any=node match{			
						//case gn:GroupNode=> gn  //found next 'g' cmd, so stop iteration
				case gn:GroupNode=> 
						// Create GroupResolve if not already created and
						// attach GroupNode instance to it.
						// Determine whether to 'do' the enclosed commnds of the Group command
						// or 'skip' these commands. 
					whatToDo(groupResolve, 
							// obj, 
							 gn,
							 rowPosition, 
							 notePanel, 
							 lock, 
							 inputFocus, 
							 indexer, 
							 statusLine, 
							 listenerArray)	
								//keep looking for 'g' cmd
				case _=> 
						// Not GroupNode, make recursive call to process next command.
					iterateToNextGroup(groupResolve, 
											 rowPosition, 
											 notePanel, 
											 lock, 
											 inputFocus, 
											 indexer, 
											 statusLine, 
											 listenerArray)
					}
			}
		//gNode
		}
		// created by GroupNode child iteration or by whatToDo(...)
	def createGroupResolve {
				// Only one GroupResolve is allocated for the CardSet, so
				// Card will not support more than one Group set ???????
				// Need to remove GroupResolve when 'g' or end of Card.--to do
			if(groupResolve==null) 
				groupResolve=new GroupResolve()
			}
//---------------------------------------------------------------------
 	def showPanel(notePanel:JPanel) {
			// JPanel extends JComponent having
			// validate(), and repaint()
		notePanel.validate()
		notePanel.repaint()
		}
			//wait() released by notifyAll by "Next button" in ButtonSet
			// Control passes to user to enter response(s).
			// called in 'startCardSet...' and 'executeCardSetChild...' for xnode
	def haltCommandExecution(lock:AnyRef): Unit=lock.synchronized {
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
	//def noConditionOrIsTrue(condition:String, symbolTable:Map[String,String]) ={
	def noConditionOrIsTrue(symbolTable:Map[String,String]) ={
		if(conditionStruct != "0")
				LogicTest.logicTest(conditionStruct, symbolTable)
			else
				true   // No logic expression  
		}
	def initializeRowPosition(size:Int)= { new RowPosition(new Font("Serif", 0, size)) }

		// CreateClass generates instances of CardSet without fields or parameters.
		// However, it invokes 'receive_objects' to load parameters from *.struct
		// file as well as symbolic addresses to be made physical ones. 
	def receive_objects(structSet:List[String]) {
			//Load class instance with argument 
		import util.control.Breaks._
			var flag=true
			for( e <- structSet) {
				  breakable { if(e=="%%") break   // end of arguments
			  else {
				var pair=e.split("[\t]")	
				pair(0) match {
							case "child" => 
									setChild(pair(1))  // Node  symChild
							case "address" =>
									setAddress(pair(1))  // Node
							case "sibling" =>
									setNext(pair(1))  // Node
										// +Add button
							case "button" =>
									setAddButton(pair(1))  // Node
							case "condition" =>
									conditionStruct=pair(1)
							case "name" => 
									node_name= pair(1)
							case _=> println("CardSet: unknown "+pair(0) )
							}
				}
			   }  //breakable		 
			  }
		}
	}

