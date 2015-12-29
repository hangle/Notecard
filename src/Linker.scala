/* date:   Feb 7, 2012
	The methods 'reset' and 'iterate' are
	used by parent classes:
		Notecard
		CardSet
		RowerNode
		BoxField
	to execute their children classes. The first child
	is the first sibling whose 'next:Node' points to the
	next sibling, and so on, until 'next' is null.
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
			// capture 'reset's argument to be used by 'isChild'
		var current:Node=null  // saved 
			// Notecard: doAsteriskButton() Employed to restart clientNotecard state.
			// Invoked prior to calling 'startNotecard()
		def saveCurrentNode { current= node } 
			// Notecard: doAsteriskButton() Employed to restart clientNotecard state.
			// Invoked following  call to 'startNotecard()
		def restoreCurrentNode { iterator=current }

			// Invoked by Notecard.iterateNotecardChildren to detect
			// if iteration involves the first child. If so,
			// then the Next button is grayed since backup impossible
			// and 'iterate' are methods to iterate these
			// lists of objects. 'node' returns the object
			// referenced on each iteration.
		def isChild= if(child eq node) true; else false
		def reset(child:Node) { //initialize the list
					// child is the 1st child of the Parent, that is 1st sibling 
			iterator=child
			}
			// Next button terminates iterate loop in Notecard.doAddCardSet
		def terminateIterate { iterator==null}
		
		def iterate= {
			if(iterator==null)
				false   // escape 'while(iterate)' loop
			  else {
					// store current sibling Node before accessing the 
					// next sibling Node
				node=iterator
					// Node.next yeilds the Next sibling
				iterator=iterator.next
				true
				}
			}
	}
																	  
	
