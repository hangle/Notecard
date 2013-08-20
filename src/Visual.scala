/* date:   Nov 13, 2011

	Trait of:
		BoxField
		DisplayText
		DisplayVariable
 	The function render() is invoked by layoutContainer in
	NoteLayout.  layoutContainer iterates all components
	added to the notecard panel to display items.
*/
package com.client


trait Visual   {

	def render()
//	def convertRowColumnToPixel(rowPosition:RowPosition)

}


