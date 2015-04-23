/*				ADD CARD SET FLAGS

	Instantiated by Notecard and passed to CardSet via startCardSet(...

	The '+Add' button is not armed unless the current CardSet
	has an associated AddCardSet'

	In 'Notecard.executeOneNotecardChild' the statement: 	
			case acs:CardSet if(acs.isAddCardSet) =>
	only executes AddCardSet(s) when 'activatedCardSet' is true."
	Also, 'isFirstAddCardSet' is set false after being set true in the following:

	In 'Notecard.executeOneNotecardChild' the statement: 	
			case cs:CardSet  =>
	Detects that CardSet has an associated AddCardSet to set 'isFirstAddCardSet' true.

	AddCardSet(s) have there own backup mechanism in contrast to the mechanism of
	CardSet(s).  The variable 'whichBackup' distinguishes the two mechanisms.
*/
package com.client

class AddCardSetFlags  {
			// 
//	var isFirstAddCardSet= false
			// set 'true' whenever '+Add' button is activated, however, '+Add' is
			// not armed unless CardSet had dependent AddCardSet(s).
	var activatedAddButton=false
			// In Notecard.executeOneNotecardChild- case cs:CardSet=> set true this
			// variable. In CardSet throw AddButtonException, set variable false
	var hasDependentAdd=false
//	var whichBackup="cardSet"   // either CardSet or AddCardSet

	}
