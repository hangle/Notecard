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
	CardSet(s).  The variable 'whichBackup' distinguishes the two mechanisms.
*/
package com.client

class AddCardSetFlags  {
			// In Notecard-doAddButton, set 'true' whenever '+Add' button is 
			// activated, however, '+Add' is not armed unless CardSet had 
			// dependent AddCardSet(s).
			// In Notecard-doNextButton, when it is found 'true', then it
			// is set 'false'.
	var activatedAddButton=false
			// In Notecard.executeOneNotecardChild- case cs:CardSet=> set true this
			// variable. In CardSet throw AddButtonException, set variable false
	var hasDependentAdd=false

	}
