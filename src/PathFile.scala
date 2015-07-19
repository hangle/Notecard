/* date:   Dec 14, 2011
		PATH FILE  <trait> of FilePort
	Separates filename from pathname, provided the string
	has a path  delimited by a slash '/'.
	Used in NextFile and 'card'
*/
package com.client

trait PathFile  {
		//envoked by FilePort to return filename
	def getFileName(s:String)={
					// Find last '/'
		if( ! isSlash(s)) //no path
			s       // return filename
		   else { 
			val l=locationOfSlash(s) //index of '/'
			s.drop(l+1)  //remove path to yeild filename
			}
		}
		//envoked by FilePort to return pathname
	def getPathName(s:String)={
					// Find last '/'
		if(  isSlash(s)) {  // true if path exists
			val l=locationOfSlash(s)
			var path=s.take(l+1) // extract path
			path=path.trim
				//println("PathFile: path="+path)
				// If path has '/' but lacks a pathname, 
				// then initialize to ""
			path
			}
		    else ""
		}
	def isSlash(s:String)= if(locationOfSlash(s) > -1) true; else false
	def locationOfSlash(s:String)= s.lastIndexOf("/") 
	}




