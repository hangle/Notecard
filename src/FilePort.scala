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
		//  Used in NextFile and 'card'
class FilePort  extends PathFile {
		//name lacking a path has the currentPath
		//added to it, provided currentPath exists.
		//name with path will have its path replace 
		//currentPath
	def establishFileName(s:String): String ={ 
		var fileName=getFileName(s)  //In PathFile, extracts filename from current File
		var currentPath=getPathName(s) //In PathFile, extracts path 
		if(currentPath=="/") {          //  Path symbol without pathname
			setSessionPath("")  //  "/" initializes session path to "" 
			currentPath=""    		// also initialize currentPath 
			}
		if (currentPath != "" ){  //current File has Pathname, so add it to Session and file
				setSessionPath(currentPath) //In Session: stores 
				fileName= getSessionPath+fileName+".struct" //In Session
				}
			else {  
				if( ! pathSessionEmpty) { //current file lacks path, but Session has path
					fileName=getSessionPath+fileName+".struct"   // getPath is in Session
					}
				 else {
					fileName=fileName+".struct"  // curent file lacks path & Session path nonexistent
					}
				}
			//if file exists return fileName, otherwise return start file
		replaceNonExistentFileWithStartFile(fileName)  // returns file
		}
			// default to default 'start' filename if file not found	
			// otherwise return fileName
	def replaceNonExistentFileWithStartFile(fileName: String)= {
		val f=new java.io.File(fileName)
		val existFlag=f.exists() //returns true if file exist.
		if(existFlag) 
				fileName 
			else {
				//println("FilePort:  exit(0)")
				//System.exit(0)
				"start.struct"
				}
	}

}
