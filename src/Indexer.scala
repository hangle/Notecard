/* date:   Jan 21, 2012

	Initialize index in one class and allow
	another class to increment the index.
*/
package com.client
class Indexer(initialValue:Int)   {
			var index=initialValue
					// invoked when BoxField is detected by RowerNode
			def increment= index+= 1
			def getIndex=index
			}




