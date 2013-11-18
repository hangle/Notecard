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
		var iterator:Node=null // used in a while statemnt, eg., while(iterator)
		var value:Node=null    
		var backupList:List[Node]=Nil
			// capture 'reset's argument to be used by 'isChild'
		var childNode:Node=null   
			// Invoked by Notecard.iterateNotecardChildren to detect
			// if iteration involves the first child. If so,
			// then the Next button is grayed since backup impossible
		def isChild= if(childNode eq value) true; else false
			// and 'iterate' are methods to iterate these
			// lists of objects. 'Value' returns the object
			// referenced on each iteration.
		def Value=value    
		def reset(child:Node) { //initialize the list
					// child is the 1st child of the Parent, that is 1st sibling 
					iterator=child
					childNode=child
					value=child
					}
		def iterate= {
			if(iterator==null)
				false
			  else {
				value=iterator
					// store current Node before accessing the 
					// next Node
					// Node.next yeilds the Next sibling
				iterator=iterator.getNext.asInstanceOf[Node]
				true
				}
			}
			// Activated by Notecard to only store Node(s)
			// of CardSet objects
		def storeCurrentIterator {backupList= value :: backupList}
			// Instead of loading 'iterator' with the next Card, 
			// the backup mechanism loads it with the prior Card.
			// note, the frist Card has no prior Card
		def loadIteratorWithBackup=  {
			if( backupList.tail != Nil) {  //attempt backup on 1st sibling
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
			  	iterator=backupList.head  //restore iterator with 1st sibling
							  //first Card is reloaded by Notecard
			}

		var current:Node=null  // saved 
		def saveCurrentNode { current= value }
		def restoreCurrentNode { iterator=current }
}
																	  

