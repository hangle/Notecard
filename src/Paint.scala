/* date:   Dec 4, 2011
*/
package com.client
import java.awt._

object Paint   {

	def setColor(color:String )= {
	    color match {
			case "red"=> Color.red
			case "blue"=>Color.blue
			case "cyan"=>Color.cyan
			case "darkGray"=>Color.darkGray
			case "gray"=> Color.gray
			case "green"=> Color.green
			case "lightGray"=>Color.lightGray
			case "magenta"=> Color.magenta
			case "orange"=> Color.orange
			case "pink"=> Color.pink
			case "white"=> Color.white
			case "yellow"=> Color.yellow
			case _=> Color.black
			}
		}
/*
	def main(argv:Array[String]) {
		}
*/

}
