/* date:   Oct 30, 2011
   
_____________________________

*/

package com.client

object Share   {

			// initialize index in one class and allow
			// another class to increment the index.
	class Indexer() {
			var index=0
			def increment= index+= 1
			def getIndex=index
			}

// -----------------------------------------------
	def main(argv:Array[String]) {

		}

}
