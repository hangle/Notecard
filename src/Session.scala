/* date:   May 31, 2012

  Created in 'card' to hold variables
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
				isClientNotecardState ( state discovery)
				setManageNotecardState( transition to Manage state)
				setClientNotecardState( transition to Client state)
*/
package com.client

object Session  extends App{
				// Initialized at beginning of session.
				// Path:
				// A <*.nc> file having a path, such as, 
				// pool/file.nc, where 'pool' is the path,
				// is stored.  This path is appended to
				// any <.nc> file lacking a path. 
				// Note, a filename entry of '/<file>'
				// reinitializes the path to "". 
	var path=""
	def getSessionPath=path   //In FilePort
	def setSessionPath(s:String) { path=s}  //In FilePort
	def pathSessionEmpty = if(path =="") true; else false  //In FilePort

				// Asterisk '*' button controls
	var currentNotecardState="client"
	def setClientNotecardState {currentNotecardState="client"}
	def setManageNotecardState {currentNotecardState="manager"}	
	def whichNotecardState= currentNotecardState
	def isManageNotecardState= if(currentNotecardState=="manager") true; else false
	def isClientNotecardState=   if(currentNotecardState=="client") true; else false

	var asteriskButtonState="disarmed"
				// Used in Notecard to activate '*' button in either the
				// Client or Manage states.
	def isAsteriskButtonArmed= asteriskButtonState=="arm"
				// Invoked in NotecardTask by '* manage ..." command
	def armAsteriskButton= asteriskButtonState="arm"
}
