/* date:   Nov 13, 2011
   

*/
package com.client
import java.awt._

class NoteLayout   extends LayoutManager{
		/*
                 * The layoutContainer(...)  iterates
                 * thru all components added to the notecard panel.
                 * This method invokes 'render() for all Visual objs.
				 * Visual objs are:
				 		BoxField
						DisplayText
						DisplayVariable
		 */
	def addLayoutComponent(name:String, component:Component)  {}
	def removeLayoutComponent(component:Component)  {}
	def preferredLayoutSize(parent: Container) = { new Dimension(0,0) }
	def minimumLayoutSize(parent:Container)= preferredLayoutSize(parent)
	def layoutContainer(parent: Container) {
		for(i <- 0 until parent.getComponentCount()) {
	//			println("NoteLayout: i="+i)
			parent.getComponent(i).asInstanceOf[Visual].render()
			}
		}

}
