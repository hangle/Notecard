/*				Add Card Set Flags

Instantiated as a global variable in Notecard.

*/
package com.client

class AddCardSetFlags  {
			// Invoked in Notecard.executeNotecardChild() when case aceCardSet is
			// an AddCardSet
	def reset {
			activatedAddButton=false
			createAddBackup=false
			firstInputActivated=false
			}
			// 1st InputField execution in InputFocus sets it true.
			// CardSet.startCardSet... will not activate 'armAddBtton' when 
			// 'firstInputActivated' is true. 
	var firstInputActivated=false
			// Tested in 'doNextButton' when true invoke 'addCardSetOverRestoreCardSet'
			// which sets it 'false' 
			// set 'true' in Notecard.doAddButton()
			// set 'true' in CardSet with 'case xn:XNode=>'
			// 			if(buttonSet.selectedButton == "+" 
			//					&& addCardSetFlags.createAddBackup ){
			//				addCardSetFlags.activatedAddButton=true
	var activatedAddButton=false
			// In Notecard.executeOneNotecardChild- case cs:CardSet=> set true
			// provided CardSet had a dependent AddCardSet.
			// 'establishAddBackup() is invoked and 'createAddBackup' set true.
			// In CardSet case xn:Xnode=> 
			//			if(buttonSet.selectedButton == "+" 
			//				&& addCardSetFlags.createAddBackup )
			// 						createAddBackup is set false
	var createAddBackup=false
	}


		// In Notecard-doAddButton, set 'true' whenever '+Add' button is 
			// activated, however, '+Add' is not armed unless CardSet had 
			// dependent AddCardSet(s).
			// In Notecard-doNextButton, when it is found 'true', then it
			// is set 'false'.
			// In CardSet  with execution of XNode=> and following release of the
			// lock, value is set true 

/*				ADD CARD SET FLAGS

	Instantiated by Notecard and passed to CardSet via startCardSet(...

	The '+Add' button is not armed unless the current CardSet
	has an associated AddCardSet'

	In 'Notecard.executeOneNotecardChild' the statement: 	
			case acs:CardSet if(acs.isAddCardSet) =>
	only executes AddCardSet(s) when 'activatedCardSet' is true."

	In 'Notecard.executeOneNotecardChild' the statement: 	
			case cs:CardSet  =>

	AddCardSet(s) have there own backup mechanism in contrast to the mechanism of
	CardSet(s).  The variable 'whichBackup' distinguishes th
*/
