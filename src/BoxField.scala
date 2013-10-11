/* date:   Nov 27, 2011
							BOX FIELD
	A component of the Display command, for example:
			d Enter (# $one)    //(# $one) is an input field component
	The 'SymbolTable' stores the user's input value along with the
	key 'one'. 

	KeyListererObject.actOnNewLineEvent adds key and value to 
	SymbolTable by calling BoxField.addFieldToSymbolTable.

	Child of RowerNode.  RowerNode invoked 'convertrowColumnToPixel(...)' and
	adds this object to the notePanel which invokes 'render(...).

	BoxField extends JTextField to capture the user's input response.  
	It also extends Visual BoxField instances that are created by RowerNode


	BoxField is a child of CardSet.  When CardSet executes BoxField.startBoxField(..)
	then the 'x' and 'y' coordinates as pixels are computed by RowPlacement functions.
	These coordinates are used in 'render()' by 'setBounds()':
	 	Component.setBounds(x, y, local_getMetricsWidth(), local_getMetricsHeight());
	However, 'render()' is not invoked until just prior to the 'wait()'is issued  by
	CardSet, for example: 
			// CardSet children has executed, now invoke the 'render()' methods
			// of DisplayText, DisplayVisual, and BoxField.
			showPanel(notePanel) // display panel content (paint, validate)
			haltCommandExecution(lock)//Stop (issue wait()) to allow the user to enter responses.
			clearNotePanel(notePanel)		//remove all components & clear screen
 	A 'd' command, such as:
			d now is (%% /size 24/for all) good men
	has two size value (24 and the default size [typically 16] for 'now is' and 'good men').
	The 'y' coordinates must be adjusted upwards for 'now is' and 'good men' .  
			(see:  y=adjustYyForSizeLessThanMax(yy:Int) )
*/ 

package com.client
import scala.collection.mutable.Map
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Color
import javax.swing._

case class BoxField(var symbolTable:Map[String,String]) extends JTextField  with Visual with Linker{
													/*
				Linker
					def reset
					def iterate
					def Value
				Linker extends Node
	symbolTable holds $<variables>	def setId
					def convertToSibling
					def convertToChild
				Visual
					def render
					def convertRowColumnToPixel
*/
//------------paramters pass by .struct file-----------------
   var field=""     //e.g.,  $one where field="$one"
   var length=0		// display length of input field
   var column=0		// 1== Yes/No input mode
   var sizeFont=0
   var styleFont=0

   var nameFont=""
   var ycolor:Color=Color.black
   var limit=0		// number of input characters allowed.
   var options=0	// number options of multiple choice  
   				
//----------------------------(used in KeyListenerObject)-----------
	def getLimit=limit    
	def isYesNoMode=if(column==1) true; else false 
	def isValidOptionRange= { if(options > 0 && getInput.toInt  <= options) true; else false }
    def isMultipleChoice= {if(options > 0) true; else false} //used in KeyListenerObject
	def validateYesNoResponse(key:Int)= if(key==110 || key==121) true; else false
	def adjustLimit= { if(options <= 9) 1; else 2 }  //
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			convertToChild(swizzleTable)  // Is a parent of the Edit commands
			}
//-------------------------------------------------------------------

	def startBoxField(rowPosition:RowPosition) {
		xx=rowPosition.getCurrentWidth()
		yy=rowPosition.getCurrentHeight()
			// computes the metric width of the text string so as
			// to adjust row position for next display component
		rowPosition.sumToCurrentWidth(local_getMetricsWidth())
		}
			// record captured response
	def addFieldToSymbolTable {
		symbolTable += (field -> getInput)
		}
	def removeFieldFromSymbolTable { 
		symbolTable -= field
		}
	def getInput= getText().trim      //getText() in JTextField
        var xx=0   //set by RowPosition
	var yy=0   //set by RowPosition
	var metrics:FontMetrics=null
			// Assigned in RowerNode by visiting each Visual component
			// of the 'd' command and finding the one with the 
			// greatest height value. 
	var maxHeight=0

				// In NoteLayout, LayoutManager.layoutContainer iterates
                		// thru all components added to the notecard panel.
                   		// This method invokes 'render() for all Visual objs.
	def render() {
		setForeground(ycolor)
        setColumns(5)
		var y=yy    // in event that yy does not need an adjustment
		if(isHeightDifferentThanMaxHeight) 
			// y axis is ajusted downward for text whose height < maxHeight
			// so that text of different sizes are aligned on the same line.
					y=adjustYyForSizeLessThanMax( yy )
        setBounds(xx, y, local_getMetricsWidth(), local_getMetricsHeight());
		}
		// if text height is not same as the largest height 
	def isHeightDifferentThanMaxHeight: Boolean={
			local_getMetricsHeight() != maxHeight
			}
		// In 'd' command ( d (%% /size 10/now) (%% /size 15/is) ) for 'now'
		// to be aligned with 'is',  the y axis value of Component() must be
		// adjusted. 
	def adjustYyForSizeLessThanMax(yy:Int)= { 
		val difference= maxHeight - local_getMetricsHeight()
		yy + difference - (difference * .25).toInt
		}

	def establishMetrics(nameFont:String, styleFont:Int, sizeFont:Int)={
		val font=new Font(nameFont,styleFont, sizeFont)
		setFont(font)
		getFontMetrics(font)
		}
	def local_getMetricsHeight()={ metrics.getHeight() +4 }
	def local_getMetricsWidth() ={ metrics.charWidth('m') * length }
		// When an EditNode fails, its message parameter
		// passed to BoxFieldc
	var editMessage=""
		// Invoked by KeyListenerObject.
	def getEditMessage=editMessage
	def detectYesNoToSetLengthToOne(xtype:Int, size:Int)={

		}
		// used in KeyListenerObject when 'captureInputResponse is invoked.
		// Indicates the Edit command is present and is subject to test.
	def isEditNodeOn= {if(symChild != "0") true; else false }
		// Iterate EditNode children. Returns true if all
		// EditField(s) associated with the BoxField each return 
		// true, or returns false is any one returns false
		//	   Invoked in KeyListenerObject.actOnNewLineEvent(...)
	def isEditSuccessful: Boolean={
		var editNode:EditNode=null
		var editSuccess=true
		val response=getInput  // the input to be evaluated
		reset(getFirstChild) 
		while(iterate) { 		 
			editNode=Value.asInstanceOf[EditNode]
			if(editSuccess){ //any failure turns 'if' expression off
				editSuccess=editNode.evaluateTheEditNode(response) //return false if failure
				if( ! editSuccess) //load message when EditNode fails. 
					editMessage=editNode.getFailureMessage 
				}
			}
		editSuccess
		}
	def clearInputField=setText("")	
	def  receive_objects(structSet:List[String] ) {
		import util.control.Breaks._
			var flag=true
			for( e <- structSet) {
			  breakable { if(e=="%%") break   // end of arguments
			  else {
				var pair=e.split("[\t]")	
				pair(0) match {
							case "child" =>
									setChild(pair(1))
							case "address" => println(pair(1))
									setAddress(pair(1))
							case "sibling" =>
									setNext(pair(1))
							case "field" => println(pair(1) )
									field=pair(1)
							case "length" => println(pair(1) )
									length=pair(1).toInt
							case "column" => println(pair(1) )
									column=pair(1).toInt
							case "size" => println(pair(1))
									sizeFont= pair(1).toInt
							case "style" =>
									styleFont= pair(1).toInt
							case "name"=> println(pair(1))
									nameFont=pair(1)
							case "color"=> println(pair(1))
									ycolor=Paint.setColor(pair(1))
							case "limit"  => 
									limit= pair(1).toInt
							case "options" => 
									options=pair(1).toInt
							}
				}
			   }  //breakable		 
			 metrics=establishMetrics(nameFont, styleFont, sizeFont)
			 }

		}
/*
		val in=structSet.iterator
		setChild(in.next)    // EditNode children	
	   	setAddress(in.next)
		setNext(in.next)	 // next Sibling
		field=in.next
		length=in.next.toInt
		column=in.next.toInt
		sizeFont=in.next.toInt
		styleFont=in.next.toInt
		nameFont=in.next
		ycolor=Paint.setColor(in.next) //see Paint object
		limit=in.next.toInt
		options=in.next.toInt

		metrics=establishMetrics(nameFont, styleFont, sizeFont)
		val percent= in.next
	//	println("BoxField: percent="+percent)
*/

	}

