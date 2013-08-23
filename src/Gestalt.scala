/**
	Measures the similarity of two strings whereby two identical
	strings will measure 100%. Two strings that are closely similar
	have a higher score  than two completley dissimilar strings 
	which measure 0%.

	Routine first finds the longest matching substring in two 
	similar strings (source and target strings). For example, in the
	almost matching strings of "philadelphia" and "philedelphia", the 
	longest matching, common substring is "delphia". The substrings to 
	the left of the longest common substring ("phila" and "phile"), are 
	subject to the same treatment; in which case, the common substring
	"phil" is found. The substrings to the left of "phil"
	would repeat the operation if any existed. 

	The substrings to the right of the longest matching, common 
	substring are processed in the same way.  There are no substring to 
	the right of "delphia". The substrings to the right of "phil" 
	are "a" and "e"; the routine found no match in these substrings.

	The length of the matching substrings ("delphia" and "phil") are
	totaled and divided by the length of the target string
	("philadelphia") to yield a measure of similarity (92%).
*/
package com.client
import scala.collection.mutable.ListBuffer

//class Gestalt {
object Gestalt {

	def testGestalt(percent:String, target:String, source:String): Boolean={
		val rating=gestaltRating(source, target)
		if(rating.toInt >= percent.toInt)
				true
			else
				false
		}
		// Ratings range from 0 to 100 as a
		// measure of 'source' and 'target'
		// string comparison. 
	def gestaltRating(source:String, target:String): Int={
		if(source==target) 
			100     // complete match so return 100%
		    else
				// find the degree 'source' and 
				// 'target' are similar.
			ratePartialMatch(source,target)
		} 
	def ratePartialMatch(source:String, target:String): Int={
				// list of matching substrings
		val matches=matchSubstrings(source, target)
				// match.length/target.length
		rateSourceTargetAggrement(matches, target)
		}
	def matchSubstrings(source:String, target:String): List[String]= {
			// finds all substring matches between 'source' 
			// and 'target'.
			// for example:   xapplle   &  zappell    yields
			// the substrings   'app'  'll' 'e' in that order.
			// 'buffer' collects the substring matches. 
		val buffer= new ListBuffer[String]
			// find 1st longest, common substring		
		val longest=findLongestSubstringMatch(source, target)
			//recursive function to find other 
			//common substring in 'source' & 'target'	
		leftAndRightOfLongestSubstring(buffer, longest, source, target)
		buffer.toList		
		}
	def findLongestSubstringMatch(o:String, t:String): String={
			// matching 'o' and 't' may produce many matches.
			// the routine finds the longest match.
		var substrings:List[String]=Nil
		var longest=""
			// compare each char of 'o' with all 
			// chars of 't'
		for(i <- 0 until o.length)
			for(j<-0 until t.length) 
				// 1st chars match, 
		 	   if(o.charAt(i)==t.charAt(j))
					// now find other matches, for example,
					// "ppp" and "ppp" will find the following
					// matches  "ppp", "pp", "p"
				substrings=getSubMatch(o.substring(i),t.substring(j))::substrings
					// find substring with greatest length, in
					// example of "ppp", "pp" "p", longest is "ppp"
		substrings.foreach((s)=> if(s.length > longest.length) longest=s)
		longest
		}
	def leftAndRightOfLongestSubstring(buffer:ListBuffer[String],
					   longest:String, 
					   source:String, 
					   target:String) {
			// capture the prior "longest" match in 'buffer', 
			// extract the substrings to the right and left of 
			// the match, and then recursively call itself for
			// the right and for the left substrings. 
		if(longest !="")  
			buffer += longest  //collect matches
		val sourceIndex=source.indexOf(longest)
		val targetIndex=target.indexOf(longest)
		val xsource=source.substring(0, sourceIndex)
		val xtarget=target.substring(0, targetIndex)
		val ysource=source.substring(sourceIndex+ longest.length)
		val ytarget=target.substring(targetIndex+ longest.length)
		var newLong=""
			// recursive call for the left substring
		if(xtarget!="" && xsource!="" && longest!="") {
			newLong=findLongestSubstringMatch(xsource, xtarget)
			leftAndRightOfLongestSubstring(buffer, newLong, xsource, xtarget)
			}
			// recursive call for the right substring
		if(ytarget!="" && ysource!="" && longest!="") {
			newLong=findLongestSubstringMatch(ysource, ytarget)
			leftAndRightOfLongestSubstring(buffer, newLong,ysource,ytarget)
			}		
		}
	def getSubMatch(s:String, t:String):String={
			// Recursively collect common chars
			// in 's' and 't' until noncommon chars
			// are detected or until at end of string. 
		if(s.length==0 || t.length==0  || s.charAt(0) != t.charAt(0) )
			""	
		else  s.charAt(0) + getSubMatch(s.substring(1), t.substring(1)) 
		}	// Operator detected by inspection of 1st character and
	def rateSourceTargetAggrement(matches:List[String], target:String): Int={
			// number of chars in all the matching substrings is
			// divided by the number of chars in target. 
	        val sourceSize=	(0 /: matches) (_+_.length)
	        ((sourceSize/(target.length * 1.0)) * 100.0 +0.5).toInt
		}

}
