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

/*
def main(argv:Array[String]) {
	var f=new JFrame("Action")
	f setDefaultCloseOperation(JFrame EXIT_ON_CLOSE)
	f setSize new Dimension(320, 240)
	f.getContentPane.add(notePanel, BorderLayout.CENTER)
	f setVisible true
	}
*/
}


