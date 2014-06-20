/* date:   Feb 7, 2012
	The methods 'reset' and 'iterate' are
	used by parent classes:
		Notecard
		CardSet
		RowerNode
		BoxField
	to execute their children classes. The first child
	is the first sibling whose 'next:Node' points to the
	next sibling, and so on, until 'getNext:Node' is null.
	Example in Notecard:
				reset(getFirstChild)  // In Node	
				while(iterate) {...}		

	The trait also handles the backup mechanism in which the
	user may access the prior Card by activating the Prior
	button.  The Prior button in Notecard invokes the method:
	    'loadIteratorWithBackup' activated in ButtonSet

	Note, each parent class creates its own instance of the 
	Linker trait and this trait extends Node 

	Notecard maintains a list of backup nodes that consist of
	just CardSet objects which consist of just Card 
	commands. When Notecard returns from iterating its children,
	it invokes Linker.storeCurrentIterator holding the current
	CardSet.  This Linker method builds the 'backupList' of
	Nodes. 

	When backing up, the PRIOR button, invokes 'Linker.
	loadIteratorWithBackup'.  Instead of loading 'iterator' 
	with the next Card, the backup mechanism loads it 
	with the prior Card node from 'backupList'.
	*/
package com.client

trait Linker extends Node   {
				// while statemnt argument, eg., 'while(iterator)'
				// 'reset(child:Node) assigns parent's first child to 'iterator'
				//	before 'while(iterator)' is invoked.
				// 'iterate' function assign "next" sibling to 'iterator'
		var iterator:Node=null 
		var value:Node=null    
		var backupList:List[Node]=Nil
			// capture 'reset's argument to be used by 'isChild'
		var childNode:Node=null   
		var current:Node=null  // saved 
		def saveCurrentNode { current= value }
		def restoreCurrentNode { iterator=current }

			// Invoked by Notecard.iterateNotecardChildren to detect
			// if iteration involves the first child. If so,
			// then the Next button is grayed since backup impossible
			// and 'iterate' are methods to iterate these
			// lists of objects. 'Value' returns the object
			// referenced on each iteration.
		def isChild= if(childNode eq value) true; else false
		def Value=value    
		def reset(child:Node) { //initialize the list
					// child is the 1st child of the Parent, that is 1st sibling 
			iterator=child
			childNode=child
			}
			// Next button terminates iterate loop in Notecard.doAddCardSet
		def terminateIterate { iterator==null}
		
		def iterate= {
			if(iterator==null)
				false
			  else {
					// store current Node before accessing the 
					// next Node
				value=iterator
				node=value
					// Node.next yeilds the Next sibling
				iterator=iterator.next
				true
				}
			}
			// Activated by Notecard to only store Node(s)
			// of CardSet objects. Note, in iterate(), value
			// is prior instance.
		def storePriorSiblingInBackupList {
				backupList= value :: backupList
				//println("Linker:  storeCurr...  backupList.size="+backupList.size)	
				}

			// Notecard invoked when 'PRIOR' button is activated. 
			// Instead of loading 'iterator' with the next Card, 
			// the backup mechanism loads it with the prior Card.
			// Note, the frist Card has no prior Card
		def loadIteratorWithBackup=  {
			if( ! backupList.tail.isEmpty) {  // Indicates that backup
											  // can be initiated.
					// backing up so drop the current Node
				backupList=backupList.tail
					// set iterator to the prior Node
				iterator=backupList.head
					// if 1st sibling, then do not drop it otherwise
					// the list will be empty.
				if(  backupList.tail != Nil)
							// also drop the prior Node since it will
							// again be captured by 'backupList'
							// the first Card is reloaded by Notecard
					backupList=backupList.tail
				}		
			  else  
			  		// Can not backup so impliment first CardSet
			  	iterator=backupList.head  //restore iterator with 1st sibling
			}
	

	}
																	  

