/* date:   Nov 17, 2011
	
	Creates a JPanel object returned by
	getNotePanel().  JPanel is passed
	a specialized layout manager (NoteLayout).

	The panel displays the
	Card material. Also establishes
	a black border arround the panel

	JPanel is an extension of JComponent which also extends
	JContainer.  

	Object created by Notecard-- the root parent  of the
	command structure.  

*/
package com.client
import javax.swing._
import java.awt._
import java.awt.event._

class NotePanel   {
		// JPanel extends JComponent having defs:
		// validate, repaint, remove
	val notePanel=new JPanel( new NoteLayout);
	notePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	def getNotePanel=notePanel

}


