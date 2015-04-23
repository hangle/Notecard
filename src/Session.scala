/* date:   May 31, 2012

  Created in 'card'(main) to hold variables
  that last the entire Notecard session. The
  session variables extends across the Notecard
  files (*.nc).

  Active in following objects:
  	card:
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

		// Notecard.doAsteriskButton if true, then set to false as well as
		// store Current Node and invoke 'startNotecard' with Management file.
		// When false, then set true and throw exception that is caught in
		// 'startNotecard' following 'iterateNotecardChildren'.
	var initialNotecardState:Boolean= true


	def getSessionPath=path   //In FilePort
			// In card, path==""
	def setSessionPath(s:String) { path=s}  //In FilePort
	def pathSessionEmpty = if(path =="") true; else false  //In FilePort

}
