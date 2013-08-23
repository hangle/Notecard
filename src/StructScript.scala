// date:   Oct 24, 2011
/*
						STRUCT SCRIPT
		
		Reads <file>.struct into ListList[String] where
		each element is List[String] beginning with 
		%<classname> ,e.g., %Notecard

		A <classname>Group consist of a %<classname>,
		argument values, and is terminated by '%%'.
		For example:
			%Notecard
			2002
			550
			450
			16
			%%
		From the input file, each <classname>Group becomes a List 
		that is assigned to a common List, as returned by
		structsToClassnameGroup(...) to User.scala

		In the event a '.struct' is not found, then the
		commands internalized in StartFile are executed
		asking the user to enter the name of a "start" file. 
*/
package com.client
//package note.client

          import scala.io._
          import java.io._
object StructScript   {
					// Invoked by Client (tst) to read <.struct> file
	def structListList(filename:String):List[List[String]]={
		val list= scriptFile(filename)
		structsToClassnameGroup(list).reverse	
		}
	def scriptFile(filename:String):List[String] ={
			// Terminate program if 'start.struct' file is found
			// because 'start' file commands are found in 
			// 'StartFile.scala'.  In event user creates 'start.nc'.
		isStartFile
		if( ! isFile(filename)) {
			// file not./ found, so default to 'start' file
			// in StartFile.scala which allows
			// user to enter a '.nc' filename
		println("StructScript   invoke substituteStartFile")
				// Reads script
			substituteStartFile //return list
			}
		  else {
			val list=fileToList(filename)
			if(list.length == 0)
				throw new IOException(filename+" is empty")
			    else
				    list
			}
		}
		// <*.struct> file not found so use start.struct
		// 'start.struct' script is in 'StartFile.scala'.
	def substituteStartFile ={	StartFile.getStartArray.toList }
	def fileToList(filename: String):List[String] = {
		Source.fromFile(filename).getLines.toList
		}
	def isFile(filename:String) = {
		if(new File(filename).exists()) true; else false
		}
	def isStartFile={
		if(new File("start.struct").exists()) {
			println("Remove 'start.struct' file.")
			println("---program terminated---")
			sys.exit(0)
			}
		}
		// Combine lines into CardSets where set begins with
		// '%', like '%DisplayText'. Then combine Card sets lists
		// in a single list. 
	  def structsToClassnameGroup(l:List[String]):List[List[String]]={
		var ll:List[List[String]]=Nil
		var xl:List[String]=Nil
		var flag=false
		for(s <-l) { 
			s match { 
				case "%%" => 
					ll=xl.reverse :: ll
					flag=true
					// a 'text' line may begin with '%' so make sure
					// it is followed by '%%' (end of set)
				case s  if(flag ==true && s.startsWith("%"))=> 
					xl=s :: Nil
					flag=false
				case _=>   
					xl= s :: xl
				}
			}
		ll
		}
}

