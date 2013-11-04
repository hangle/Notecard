// date:   Oct 26, 2011
/**					CREATE CLASS
	The Server system has created notecard objects and has built
	a network of link lists connecting these objects. The system
	passes the link lists in  '.struct' files to the 'Notecard'  program. 
	The link list physical addresses, held in the list's 'next' 
	variable, are converted to strings (symbolic addresses). The 
	physical address of the object, itself, is converted to a 
	string and is written to the '.struct' file. 
			
	When the object is instantiated by the 'Notecard'  system it
	reads '<obj>.read_object()' its portion of the '.struct file'
	Two string items of this portion are:
			address  --symbolic address of the object
			next     --symbolic of the next object in the link list
	swizzleTable is a Map[String,Node] into which each object stores
	its 'address' string as a key and its physical address as the
	key value. After all objects have loaded their mappings
	(symbol addr->physical addr), then the objects are iterated. 
	Each object accesses the swizzleTable using its 'next' 
	symbolic address to retrieve the physical address of the
	object to which it is linked and the 'next' variable is
	rewritten with the this physical address (see: Node).
	 Some objects read a '.struct' file value that is a symbolic
	address whose object variable is 'parent'. This 'parent'
	symbolic address is the root of a link list.  While a
	list node may be a child, this child can support a 
	link list  (see: Node trait).

				assigner.receive_objects(structObj.tail)
				swizzleTable=assigner.setId(swizzleTable, assigner)
						//condition Node for logic tests,e.g., ($gender)=(male)
				assigner	
			case "%CardSetTask"=>
				val cardSetTask=CardSetTask(symbolTable)
				cardSetTask.receive_objects(structObj.tail)
				swizzleTable=cardSetTask.setId(swizzleTable, cardSetTask)
				cardSetTask
			case "%RowerNode"=>// println("%\t\tCreateClass:  %RowerNode")
				val rowerNode=RowerNode(symbolTable)
				rowerNode.receive_objects(structObj.tail)
				swizzleTable=rowerNode.setId(swizzleTable, rowerNode)
	In *.struct file,  A parent class having children and which is
	also a child of a parenet itself, has symbolic addresses immediately
	following the '%<object name>':
		1st symbolic value is the address of the first child
		2nd symbolic value is the address of the object
		3d  symbolic value is the address of the next sibling
	A class that is just a child has:
		1nd symbolic value is the address of the object
		2d  symbolic value is the address of the next sibling

*/
package com.client
import scala.collection.mutable.Map

//object CreateClass   extends Node {
class CreateClass   extends Node {


	var obj:Any=null
	var coreVector=List[Any]()

	var swizzleTable= Map[String,Node]()
	var root:Notecard=null
			// 'allStructSets' is a List containing List elements. Each element
			// 'structSet' begins with '%<class name> followed by class parameters
			// <class name> used to instantiate the class. 
			// invoked by  CommandNetwork.fileLoad_BuildNetwork(..)
	def establishObjectNetwork( symbolTable:Map[String,String],
								allStructSets:List[List[String]])= {
			//println("CreateClass:  allStructSets.size="+allStructSets.size)
			for(structSet <-allStructSets) {
					//'structSet' is List[String], representing one Card
					// containing the object's  name, such as, %DisplayText,
					// followed by one or  more argument values that are 
					// loaded into the object's fields.
				obj=create_object(structSet, symbolTable) //CreateClass
					//build object list
				coreVector=obj:: coreVector
				}
			for(core <- coreVector.reverse) {
			 		//convert symbolic address to physical one in Node
				swizzleReference(core)
				}
			root  // notecard assigned to root  by 'create_object(..)'
			}
		// The *.struct commands , such as %DisplayText,  used the
		// its %<class name> in a match statement to create the named
		// object, e.g., Display(symbolTable) Next, the remaining list 
		// string values (class parameters) are added to the object.
		// Finally, the object's symbolic address is converted to
		// a physical address.
	def create_object(structObj:List[String], symbolTable:Map[String,String]):Any = {
	//	println("herex ")
		structObj.head match{   
			case "%Notecard"=> // println("\t\tCreateClass  %Notecard") 
				val notecard= Notecard(symbolTable)
				root=notecard   //Notecard is special, it is the root of the hierarchy
					// Removes tag such as %%Notecard and passes the
					// object's file parameters such as height,width,size
					// Adds object to swizzle table
				notecard.receive_objects(structObj.tail)
				swizzleTable=notecard.setId(swizzleTable, notecard)
				notecard
			case "%CardSet"=> 
				val cardSet=CardSet(symbolTable)
				cardSet.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=cardSet.setId(swizzleTable, cardSet) //setId is a Node method
				cardSet
			case "%NotecardTask"=>
				val notecardTask=NotecardTask(symbolTable)
				notecardTask.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=notecardTask.setId(swizzleTable, notecardTask)
				notecardTask	
			case "%NextFile"=> 
				val nextFile=NextFile(symbolTable)
				nextFile.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=nextFile.setId(swizzleTable, nextFile)
				nextFile
			case "%AssignerNode"=>
				val assigner=Assigner(symbolTable)
				assigner.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=assigner.setId(swizzleTable, assigner)
				assigner	
			case "%CardSetTask"=>
				val cardSetTask=CardSetTask(symbolTable)
				cardSetTask.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=cardSetTask.setId(swizzleTable, cardSetTask)
				cardSetTask
			case "%RowerNode"=>
				val rowerNode=RowerNode(symbolTable)
				rowerNode.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=rowerNode.setId(swizzleTable, rowerNode)
				rowerNode
			case "%DisplayText"=>
				val displayText=DisplayText(symbolTable)
				displayText.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=displayText.setId(swizzleTable, displayText)
				displayText
			case "%BoxField"=>
				val boxField=BoxField(symbolTable)
				boxField.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=boxField.setId(swizzleTable, boxField)
				boxField
			case "%GroupNode"=>
				val groupNode=GroupNode(symbolTable)
				groupNode.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=groupNode.setId(swizzleTable, groupNode)
				groupNode
			case "%DisplayVariable"=>
				val displayVariable=DisplayVariable(symbolTable)
				displayVariable.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=displayVariable.setId(swizzleTable, displayVariable)
				displayVariable
			case "%XNode"=>
				val xnode=XNode(symbolTable)
				xnode.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=xnode.setId(swizzleTable, xnode)
				xnode
			case "%EditNode"=>
				val editNode=EditNode(symbolTable)
				editNode.receive_objects(structObj.tail) // pass parameters to object
				swizzleTable=editNode.setId(swizzleTable, editNode)
				editNode
			case _=> println("unknown in CreateClass:create_object="+structObj.head )
				}
		
		}
		/* 
		Every Card command class has 'convertToReference' method. This 
		method may have one or two Node methods:
			convertToSibling
			convertToChild
		which reads the SwizzleTable, extracting the physical address that
		is associated with the symbolic address.  The physical addresses
		are assigned to Node.next or to  Node.child.  */
	def swizzleReference( factoryObj:Any)= {
		factoryObj match {
			case nc:Notecard=>
				nc.convertToReference(swizzleTable) 
			case cs:CardSet=>
				cs.convertToReference(swizzleTable) 
			case ft:NotecardTask=>
				ft.convertToReference(swizzleTable) 
			case nf:NextFile=>
				nf.convertToReference(swizzleTable) 
			case as:Assigner=>
				as.convertToReference(swizzleTable) 
			case cst:CardSetTask=>
				cst.convertToReference(swizzleTable) 
			case rn:RowerNode=>
				rn.convertToReference(swizzleTable) 
			case dt:DisplayText=>
				dt.convertToReference(swizzleTable) 
			case bf:BoxField=>
				bf.convertToReference(swizzleTable)
			case gn:GroupNode=>
				gn.convertToReference(swizzleTable)
			case dv:DisplayVariable=>
			 	dv.convertToReference(swizzleTable)
			case xn:XNode=>
				xn.convertToReference(swizzleTable)
			case en:EditNode=>
				en.convertToReference(swizzleTable)
			case _=> 
				println("CreateClass case_=>  "+factoryObj)
				println("CreateClass  throw exception")
				throw new Exception
			}
		}
	def getNotecard= root  //Notecard is the hierarchy root-- see: createObject
}
