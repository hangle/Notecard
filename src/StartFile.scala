
/*
 Unlike other external script files, start.struct
 is hardcoded in the Array array.  The reason for
 this arrangement is that 'start' is a program constant
 If the system cannot find a particular script file,
 then the script file name defaults to start.struct
 StartFile.scala is created by the startfile script
*/

/*
	The 'start file' is a '.nc' script file, such as:

	c
	d Enter start filename (# $startfile)
	f $startfile
	* end

	In the event the user mistypes the name of a
	'.nc' file, then a window with an input field
	is presented to allow the user to enter the
	correct filename.

*/
package com.client

  object StartFile { 
val array=Array(
"%Notecard",
"child	2002",
//"height	250",
//"width	350",
//"font_size	14",
"%%",
"%CardSet",
"child	2003",
"address	2002",
"sibling	2015",
"name		0",
"condition		0",
"%%",
"%RowerNode",
"child	2004",
"address	2003",
"sibling	2005",
"row	0",
"column	10",
"%%",
"%DisplayText",
"address	2004",
"sibling	0",
"style	1",
"size	14",
"column	0",
"name	TimesRoman",
"text	START CARD",
"color	blue",
"%%",
"%RowerNode",
"child	2006",
"address	2005",
"sibling	2009",
"row	2",
"column	1",
"%%",
"%DisplayText",
"address	2006",
"sibling	2007",
"style	1",
"size	14",
"column	0",
"name	TimesRoman",
"text	Enter filename ",
"color	black",
"%%",
"%BoxField",
"child	0",
"address	2007",
"sibling	2008",
"field	$startFile",
"length	10",
"column	00",
"size	14",
"style	1",
"name	TimesRoman",
"color	black",
"limit	99",
"options	0",
"%%",
"%DisplayText",
"address	2008",
"sibling	0",
"style	1",
"size	14",
"column	0",
"name	TimesRoman",
"text	  ",
"color	black",
"%%",
"%RowerNode",
"child	2010",
"address	2009",
"sibling	2011",
"row	4",
"column	20",
"%%",
"%DisplayText",
"address	2010",
"sibling	0",
"style	1",
"size	14",
"column	0",
"name	TimesRoman",
"text	[omit 'nc' extension]",
"color	black",
"%%",
"%RowerNode",
"child	2012",
"address	2011",
"sibling	2013",
"row	5",
"column	20",
"%%",
"%DisplayText",
"address	2012",
"sibling	0",
"style	1",
"size	14",
"column	0",
"name	TimesRoman",
"text	Start card repeats if file",
"color	black",
"%%",
"%RowerNode",
"child	2014",
"address	2013",
"sibling	0",
"row	7",
"column	20",
"%%",
"%DisplayText",
"address	2014",
"sibling	0",
"style	1",
"size	14",
"column	0",
"name	TimesRoman",
"text	not found.",
"color	black",
"%%",
"%NextFile",
"address	2015",
"sibling	0",
"filename	$startFile",
"condition	 0",
"%%")


	def getStartArray=array
	def main(argv:Array[String]) { 


getStartArray.foreach(println)  // for testing purpose
  }  // end main

}
