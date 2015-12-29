/*			ADD BUTTON EXCEPTION		April 2015

	The 'X' command suspends the execution of CardSet commands
	to allow the user to enter a text response.

		c
		d capital of Ohio (# $capital)
		x
		g ($capital)=(Columbus)]
		d Correct

	The 'X' command in 'executeOneCardSetChild(...) is matched via 'case xn:Xnode=>'
	which eventually suspends execution (via halt).
	The user may activate either the 'Next' or '+Add' buttons to release the wait
	state caused by the 'halt'.

	In the event the '+add' button is activated, then the 'AddButtonException' is
	thrown. 

	The exception is caught by the parent Notecard in 'iterateNotecardChildren(...)'
	just before it terminates to process the next CardSet. 

	If the next CardSet is an AddCardSet and the +Add button released the
	wait state, CardSet executes the AddCardSet object. 

*/
package com.client

class AddButtonException	extends Exception

object AddButtonException {}
