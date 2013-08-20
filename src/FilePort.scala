/* date:   Dec 16, 2011
					FILE PORT   <was CMD LINE ARGUMENT> 
	
	Provides the following services:
		Extract filename:   PathFile.getFileName(s)
		Extract pathname:	PathFile.getPathName(s)

		Stores pathname in Session provided
		current filename has path:  Session.setPath(..)

		If current filename lacks a pathname, and Session has
		one, then the Session path (Session.getPath) is added 
		to filename
*/
package com.client
import java.io.File._
import com.client.Session._
				//PathFile
				//  Separates filename from pathname, provided 
				//  the string has a path  delimited by a slash '/'.
				//  Used in NextFile and Client
class FilePort  extends PathFile {
				//name lacking a path has the currentPath
				//added to it, provided currentPath exists.
				//name with path will have its path replace 
				//currentPath
	def establishFileName(s:String)={ 
		var file=getFileName(s)  //In PathFile, extracts filename from current File
								//println("FilePort:xxx file="+file)
		var currentPath=getPathName(s) //In PathFile, extracts path 
		if(currentPath=="/") {          //  Path symbol without pathname
				setSessionPath("")  //  "/" initializes session path to "" 
				currentPath=""    		// also initialize currentPath 
				}
		//println("FilePort: currentPath="+currentPath)
		if (currentPath != "" ){  //current File has Pathname, so add it to Session and file
				setSessionPath(currentPath) //In Session: stores 
				file= getSessionPath+file+".struct" //In Session
				//println("FilePort: currentPath found:  getSessionPath added to file") 
				}
			else {  //currentPath is null
				if( ! pathSessionEmpty) { //current file lacks path, but Session has path
					file=getSessionPath+file+".struct"   // getPath is in Session
					//println("FilePort: pathEmpty failed: getSessionPath added to file")
					}
				 else {
					file=file+".struct"  // curent file lacks path & Session path nonexistent
					//println("FilePort: path is empty")
					}
				}
				//does file exists, otherwise return start file
		replaceNonExistentFile(file)  // returns file
		}
				//default to 'start' filename if file not found	
	def replaceNonExistentFile(s: String)= {
					println("FilePort:  file="+s)
		val f=new java.io.File(s)
		val e=f.exists()
		if(e) s else "start.struct"

}

}
