/* date:   Feb 12, 2012
  	Creates the notecard window with a BorderLayout
	The panel 'note' occupies the center region of
	this layout and the buttons and status line are
	combined into a panel with a Box layout that 
	occupies the southern region of the BorderLayout.

	The content of the JLabel statusLine is the
	asterisk status command, e.g., '* status  message'

	Added xlocate and ylocate Nov 1, 2015

	Creaded in:  Notecard
*/
package com.client
import java.awt._
import javax.swing._
import javax.swing.border._
import scala.language.postfixOps

class CardWindow(notePanel:JPanel, 
				 buttonPanel:JPanel, 
				 statusLine:JLabel, 
				 width:Int, 
				 height:Int,
				 xlocate:Int,
				 ylocate:Int)  extends JFrame {
					// 350 width & 450 height are default values for window size
	val w=if(width==0) 350; else width
	val h=if(height==0) 450; else height
	this.setSize( new Dimension(w, h))
	val box= Box.createVerticalBox()
	statusLine.setBorder(new LineBorder(Color.green, 1))
				// 0.5f centers the status line
	statusLine.setAlignmentX(0.5f)
	box.add(statusLine)
	box.add(buttonPanel)
	val note=new JPanel()
	note.setBackground(Color.white)
	this.getContentPane().add(notePanel, BorderLayout.CENTER);	
	this.getContentPane.add(box, BorderLayout.SOUTH)
	this.setDefaultCloseOperation(JFrame EXIT_ON_CLOSE)
	this.setLocation(xlocate, ylocate)
}
