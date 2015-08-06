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
	 	Component.setBounds(x, y, visualObjectWidth("m"), visualObjectHeight());
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

case class BoxField(var symbolTable:Map[String,String]) 
				extends JTextField  with Visual with Linker with VisualMetric{
/*
				Linker
					def reset
					def iterate
					val node
				Linker extends Node
	symbolTable holds $<variables>	def setId
					def convertToSibling
					def convertToChild
				Visual
					def render
				VisualMetric
					var metrics
					def establishMetrics
					def visualObjectHeight()
					dev visualObjectWidth("m")
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
//-------------computed parameters passed by .struct file--------------
	var metricWidth=0   // metric + text
	var metricHeight=0   // metric + text
//---------------------------------------------------------------------
   		// Assigned by RowerNode.maxJeogjtValueOfVisualObject
		// RowPosition.priorYFromTop+2
	var adjustedY=0
		// The row of VisualObjects adds to this value
	var accumulatedX=0	
//----------------------------(used in KeyListenerObject)-----------
	
	def getLimit=limit    // KeyListenerObject restrict number input characters.
	def isYesNoMode=if(column==1) true; else false // KeyListenerObject
			// MultipleListener
	def isValidOptionRange= { if(options > 0 && getInput.toInt  <= options) true; else false }
    def isMultipleChoice= {if(options > 0) true; else false} //used in KeyListenerObject
			// KeyListenerObject
	def validateYesNoResponse(key:Int)= if(key==110 || key==121) true; else false
//------------------------------swizzle routines---------------------
	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			convertToChild(swizzleTable)  // Is a parent of the Edit commands
			}

//-------------------------------------------------------------------

			// RowerNode.column * DefaultFont.defaultPixelLetter()
	var startColumnX=0

	def startBoxField(rowPosition:RowPosition, startColumn:Int) {
			// Line row may have multiple VisualObjects so prior text 
			// width is added for each VisualObject.
		accumulatedX=rowPosition.currentPixelWidth

			// computes the metric width of the text string so as
			// to adjust row position for next display component
		rowPosition.sumToCurrentWidth(length * visualObjectWidth("m"))
		}
  			// In NoteLayout, LayoutManager.layoutContainer iterates
           	// thru all components added to the notecard panel.
           	// This method invokes 'render() for all Visual objs.
	def render() {
		setForeground(ycolor)

        setBounds(  startColumnX +accumulatedX, 
					adjustedY, 
					length * visualObjectWidth("m"), 
					visualObjectHeight());
		}

				// record captured response
	def addFieldToSymbolTable {
		symbolTable += (field -> getInput)
		}
	def removeFieldFromSymbolTable { 
		symbolTable -= field
		}
	def getInput= getText().trim      //getText() in JTextField
		// When an EditNode fails, its message parameter
		// passed to BoxField
	var editMessage=""
		// Invoked by KeyListenerObject.
	def getEditMessage=editMessage
		// used in KeyListenerObject when 'captureInputResponse is invoked.
		// Indicates the Edit command is present and is subject to test.
		// 'symChild' indicates that BoxField is parent of EditNode.
	def isEditNodeOn= {if(symChild != "0") true; else false }
		// Iterate EditNode children. Returns true if all
		// EditField(s) associated with the BoxField each return 
   var flag=true // true, or returns false if any one returns false
		//	   Invoked in KeyListenerObject.actOnNewLineEvent(...)
	def isEditSuccessful: Boolean={
		var editSuccess=true
		val response=getInput  // the input to be evaluated

		reset(child)    // initialize  'while(iterate)'
		while(iterate) {// loop to execute children 		 
			val editNode=node.asInstanceOf[EditNode] 
			if(editSuccess){ //any failure turns 'if' expression off
				editSuccess=editNode.evaluateTheEditNode(response) //return false if failure
				if( ! editSuccess) //load message when EditNode fails. 
					editMessage=editNode.getFailureMessage 
				}
			}
		editSuccess
		}
		// Invoked in MultipleListener and KeyListenerObject
	def clearInputField=setText("")	

		// CreateClass generates instances of BoxField without fields or parameters.
		// However, it invokes 'receive_objects' to load parameters from *.struct
		// file as well as symbolic addresses to be physical ones. 
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
							case "address" => //println(pair(1))
									setAddress(pair(1))
							case "sibling" =>
									setNext(pair(1))
							case "field" => //println(pair(1) )
									field=pair(1)
							case "length" => //println(pair(1) )
									length=pair(1).toInt

							case "column" => //println(pair(1) )
									column=pair(1).toInt
							case "size" => //println(pair(1))
									sizeFont= pair(1).toInt

							case "style" =>
									styleFont= pair(1).toInt
							case "name"=> //println(pair(1))
									nameFont=pair(1)
							case "color"=> //println(pair(1))
									ycolor=Paint.setColor(pair(1))
							case "limit"  => 
									limit= pair(1).toInt
							case "options" => 
									options=pair(1).toInt
							}
				}
			   }  //breakable		 
			 metrics=establishMetrics(nameFont, styleFont, sizeFont)
			 metricHeight=visualObjectHeight() // VisualMetric trait
			 }
		}
	}

