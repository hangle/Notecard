/*
				BACKUP MECHANISM

	The sequence of CardSets are executed by Linker following the
	CardSet's 'node' to the next CardSet.  As a CardSet node is
	processed, it is stored in 'backupList'.  

	The Next button moves the CardSets forward. The Prior button
	moves the CardSets backward.  When the Prior button is activated,
	the prior CardSet is presented.

*/

package com.client
		// Created in Notecard.
class BackupMechanism {
			// stores nodes of CardSets that have been processed.
		var backupList:List[Node]=Nil

			// 1st CardSet sibling that passes the condition test 
		var firstChild:Node=null

			// Store 1st CardSet sibling that passes the condition test 
		def captureFirstChild(node:Node) {
			if(firstChild==null)
				firstChild= node
			}
			// test in Notecard and InputFocus to prevent backup
			// beyond 1st sibling. Also to disable Prior button.
		def isFirstChild={
			if(firstChild eq backupList.head)
				true
			else
				false
			}
			// Activated by CardSet to only store Node(s)
			// of CardSet objects. Note, in iterate(), 'node'
			// is prior instance.
		def storePriorSiblingInBackupList( node:Node) {
				backupList= node :: backupList
				//		println("Linker:  storeCurr...  backupList.size="+backupList.size)	
				}

			// Notecard invoked when 'PRIOR' button is activated. 
			// Instead of loading 'iterator' with the next Card, 
			// the backup mechanism loads it with the prior Card.
			// Note, the first Card has no prior Card
		def loadIteratorWithBackup=  {
			var iterator:Node=null
		//			println("BackupMech:  backupList.tail.size="+backupList.tail.size)
			if( ! backupList.tail.isEmpty) {  // Indicates that backup
											  // can be initiated.
						//   println("BackupMech:  backupList not empty")
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
			  else { 
			  		// Can not backup so impliment first CardSet
			  	iterator=backupList.head  //restore iterator with 1st sibling
				backupList=Nil
				}
			iterator
			}
}
