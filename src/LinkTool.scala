
package com.client


object LinkTool extends Node with Linker {

	def showHierarchy(notecard:Notecard) {
		notecard.reset(notecard.getFirstChild)
		while(notecard.iterate) {
			notecard.Value match	{
				case cs:CardSet=> println("\tCardSet")
						showCardSet(cs)
				case ft:NotecardTask=> println("\tFrameTask")
				case nf:NextFile=> println("\tNextFile")
				case _=> println("\tunknown isObject")
				}
			}
		println("------------------------------------------")
		}
	def showCardSet(frameNode:CardSet) {
		frameNode.reset(frameNode.getFirstChild)
		while(frameNode.iterate) {
			frameNode.Value match	{
				case rn:RowerNode=>	println("\t\t RowerNode")
						showRowerNode(rn)
				case as:Assigner=> println("\t\tAssigner") 
				case cst:CardSetTask=>  println("\t\tCardSetTask")
				case gn: GroupNode=> 	println("\t\tGroupNode=>")
				case xn:XNode=> println("\t\tXNode")
				case _=> println("\t\tunknown")
				}
			}
		}
	def showRowerNode(rowerNode:RowerNode) {
			rowerNode.reset(rowerNode.getFirstChild)
			rowerNode.Value match {
						case xx:DisplayText=>println("\t\t\tDisplayText")
						case _=> println("===========Unknown")
						}
	    }
}
