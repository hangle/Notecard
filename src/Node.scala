/* date:   Nov 1, 2011
The following hierarchy is comprised of 'Command' classes,
corresponding to the script commands in <filename.nc> files. The
Node class builds a hierarchy of linked lists.

Framer(parent)
		LoadDictionary
			Assign
		NextFile
		FrameTask
		CardSet(parent)
				Assign
				CardSetTask
				GroupNode
				XNode
				RowerNode(parent)
						DisplayText
						DisplayVariable
						BoxField(parent)
							EditNode

The above 'Command' classes form a hierarchy of linked lists. 
'Notecard' is the root class of this hierarchy. Its children are 
'NextFile', 'FrameTask', and 'CardSet'.  'CardSet is 
also a parent, having 5 class children.  Finally, 
'RowerNode' is a parent with 3 class children.  These classes
all extend the 'Node' trait.  The data member 'next' is the
node element of the linked list.

The 'Server' program, whose input is from the 'Script'
program (<xxx.script>, builds this hierarchy and converts the 
physical address of 'next' to a symbolic address (symId). Each
class instance, is linked to a sibling class or is the
end element of the list. If the class is also a parent
of a list, it will have a symbolic child address (symChild)
that references the first child class.

The following are four lines of the '.struct' files employed
to create a 'CardSet' instance.  The lines beginning with
"ac" are three symbolic addesses. One address represents
'symId', that is, the symbolic address of the 'CardSet' 
instance.  Another address is the symbolic address of
the sibling instance--symSibling. Finally, since 'CardSet'
is a parent class, it has the symbolic address of its
first child--symChild.
		%CardSet
		2005		//symbolic address of 1st child , 
						//		e.g., RowerNode
		2010		//symbolic address of CardSet
		2007		//symbolic address of next sibling, 
						//		e.g., CardSet or FrameTask 
The method 'setId' is employed by  'CreateClass' 
to add the tuple('symId' (key) and physical address 
(value)) of each class instance the the Map
(swizzleMap).  The value 'xnode' is the physical 
address of 'objectx:Any' such as CardSet. 

		val xnode:Node=objectx.asInstanceOf[Node]

The 'swizzleTable' table is updated:  '+ (smyId->xnode)"

Next, 'CreateClass' employees 'convertToSibling', 
'convertToChild' and 'convertToCondition' to 
replace the symbolic addresses with physical
addresses.  The physical address is returned by:

		=swizzleTable(symSibling)

to either 'next:Node', sibling:Node,
A command class will have at least a call to 
'convertToSibling'.  If the class is also a parent, it then
has a call to 'convertToChild'.  
For example, CardSet has all two converts: 

	def convertToReference(swizzleTable:Map[String, Node]) ={
			convertToSibling(swizzleTable)    // has sibling
			convertToChild(swizzleTable)      // is a parent
			}
While 'DisplayText', for example, is only a sibling classs:

	def convertToReference(swizzleTable:Map[String, Node])={
			convertToSibling(swizzleTable)
			}

The three Command classes that are parent classes are Framer,
CardSet, and RowerNode.  These classes have the capability
to iterate their children via the Linker trait (see Linker.scala).
The linker trait has three methods:
							 listenerArray:ArrayBuffer[KeyListenerObject]) {
		
		def reset(child:Node) { iterator=child}
		def iterate= ...
	    def value= ...
A parent Command class first calls
		reset(getFirstChild)  //getFirstChild is a Node method
		while(iterate) {...}  //iterate false at end of sibling
							  //chain
The 1st child instance is returned via 'Value'

*/
package com.client
import scala.collection.mutable.Map

trait Node  {
	var symId="" //setAddress(): symbolic address created by server system
	var symChild:String="0"   //setChild
	var symSibling:String="0" //setNext
	var symButton:String="0" 
	var child:Node=null	 // 1st child of parent list assigned by:converToChild
	var next:Node=null   // sibling
	var addButton:Node=null // AddButtonCardSet
	var condition:Node=null // logic condition, eg, (male)=($gender)
	var backup:Node=null	// current node assigned after Framer completes
							// processing of CardSet
	var node:Node=null
//	def getId=symId
//	def getFirstChild=child //root of list, assigned by 'reset'
	def getNext= next
	def setBackup= backup=next//Framer invoked on return from startCardSet 
	def getBackup= backup		// used by Linker's loadIteratorWithBackup
			// setAddress() and setNext() are invoked by the object's
			// receive_object() method
	def setAddress(value:String) {symId=value}
	def setNext(value:String) = symSibling=value
	def setChild(value:String) = symChild=value
	def setAddButton(value:String)= symButton=value
			// 'symId' is the symbolic address whereas 'objectx' is
			// the "physical address". Invoked in 'CreateClass' which in turn '			'
			// switches or swizzles the phy addr for the symbolic one
   	def setId(swizzleTable:Map[String, Node], objectx:Any )= {
		val xnode:Node=objectx.asInstanceOf[Node]	
			//	println("Node  (symId -> xnode)="+symId+"  "+xnode)
		swizzleTable + (symId -> xnode) 
		}
			// 'symSibling' of one object matches the symbolic address of
			//  another object. The latter object is the next node in a 
			// link list.  the set object use 'next' to retrieves 
			// the corresponding physical address from the Map table
			// and stores it in 'next' (it now has a physical referenec 
			// to the next set object).[see Core as how the list is iterated]
	def convertToSibling(swizzleTable:Map[String, Node]) = {
		if(symSibling=="0" ) 
			 next;
		else {
			if(swizzleTable.contains(symSibling)) {
				next=swizzleTable(symSibling)
				}
			else{   
				println("Node  sibling throw exception")
			    throw new Exception
			    }
			}
		}
	def convertToChild(swizzleTable:Map[String, Node]) ={
		if(symChild != "0") {
		//	println("Node: swizzleTable.contains(symChild)="+swizzleTable.contains(symChild) ")
			if(swizzleTable.contains(symChild)){
				child=swizzleTable(symChild) 
				}
			else    {
				//println("Node "+symChild+" not in swizzleTable")
				throw new Exception 
				}
			}
		}
	def convertToButton(swizzleTable:Map[String, Node]) ={
	//println("Node convert to Buttton")
		if(symButton != "0") {
			//println("Node: swizzleTable.contains(symButton)="+swizzleTable.contains(symButton)+"  symButton="+symButton )
			if(swizzleTable.contains(symButton)){
				addButton=swizzleTable(symButton) 
				//println("Node: button="+button)
				}
			else    {
				//println("Node (symButton) "+symButton+" not in swizzleTable")
				throw new Exception 
				}
			}
		}

}
