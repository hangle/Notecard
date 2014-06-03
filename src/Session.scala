/* date:   May 31, 2012

  Created in 'card'(main) to hold variables
  that last the entire Notecard session. The
  session variables extends across the Notecard
  files (*.nc).

  Active in following objects:
  	Tst:
			Initialize file path used in FilePort
			Establishes the current state to that of 'card'.
 	FilePort:
  			If current filename lacks a pathname, and Session has
			one, then the Session path (Session.getPath) is added 
			to filename
	Notecard:
		  The '*' or Asterisk button switches between two
		  Card files.  The Session instance keeps track
		  of which Card file is active: (see Notecard)
				isInitialNotecardState ( state discovery)
				setManageNotecardState( transition to Manage state)
				setInitialNotecardState( transition to Initial state)
*/
package com.client

object Session  {
		// Initialized at beginning of session.
		// Path:
		// A <*.nc> file having a path, such as, 
		// pool/file.nc, where 'pool' is the path,
		// is stored.  This path is appended to
		// any <.nc> file lacking a path. 
		// Note, a filename entry of '/<file>'
		// reinitializes the path to "". 
	var path=""
	var initialNotecardState:Boolean= true
	def getSessionPath=path   //In FilePort
	def setSessionPath(s:String) { path=s}  //In FilePort
	def pathSessionEmpty = if(path =="") true; else false  //In FilePort

}
