/* date:   Dec 12, 2011
  							GROUP RESOLVE 
		GroupNode is a child of CardSet.  When this child is detected,
		then CardSet instantiates GroupResolve and passes it GroupNode.
			(CardSet.createGroupResolveAndPassItGroupNode(groupNode) )
		Next, 'whatToDo(..)' is invoked having three options:
			1)	'do'
						invokes 'iterateCardSetChildren
			2)  'skip'
			3)  instantiate a new 'GroupResolve' object.

		Example script code:					second example
		g (1) = (2)						g (1) =(2)
		d this will not be seen			d this will not be seen
		d also this						d also this
		g								ge 
										d this will be seen
		Group control covers the two 'd' display commands. In this example, 
		the second 'g' command releases the Group control.  The "life" of
		GroupResolve exists until this control ends, that is, it can spans
		one of more GroupNode objects. For the example, 'actionToTake'
		returns "skip" for the first 'g', since 1 != 2, and 'done' for the
		second 'g'.

		In the second example, GroupResolve has recorded (thenTrue) that the 
		1st 'g's condition had failed, and as such, it returns "do" for the
		'ge' (group else command) so 'd this will be seen' is executed.

		In the second example, the "do" causes CardSet to iterate to the 
		end of its list. For the 'else' to just affect 'd this will be seen', 
		then a 'g' command must follow this 'd' command. In this 'g' command, 
		"done" is returned.

		A 'ge ($one=Tom)' is detected as a 'else if' command.
*/
package com.client

class GroupResolve()   {
	val ThenNode=1         //Has condition expression.e.g., (1)=(1)
	val ElseNode=2		   //post is "else" but no condition
	val ElseConditionNode=3//post is "else" and has condition--else if
	val EmptyNode=4		   //No condition or no "else"
	var kind=0
	var groupNode:GroupNode=null
	var thenTrue=false      // The Group else clause will consult 
				// this value.  If true, then the else 
				// Group of commands are skipped. If true,
				// then the else Goup commands are executed.
	var elseCondition=false

	def attachGroupNode(group:GroupNode) { 
		groupNode=group 
		}
		// Group action is based on outcome history
		// one or more GroupNodes. For example, if
		// the then Group clause fails, then its 
		// commands are skipped; and the
		// else Group clause commands are executed. 
	def actionToTake= {	// returns either 'skip' or 'do'
				// GroupNode.whatKind is: 
				//		ThenNode--	condition only
				//		ElseNode--	else but no condition
				//		ElseConditionNode-- else and condition
				//		EmptyNode--	no condition and no else
		groupNode.whatKind match {
			case ThenNode=>	// condition only 
				if(groupNode.isConditionTrue) {
					thenTrue=true
					"do"
					}
				 else {
					thenTrue=false
					"skip"
					}
			case ElseNode=> // else but no condition
				if(thenTrue ==false) {
					"do"	}
				else{
					"skip" }
			case ElseConditionNode=>   // else and condition
				if(elseCondition==false) {
					if(thenTrue==true) "skip"
					if(groupNode.isConditionTrue) {
						elseCondition=true
						"do"
						}
					}
				 else
				 	"skip"
			case EmptyNode=>  // no else and no condition
					"done"
			case _=> println("\t\tGroupResolve: unknown")
			}
		}
			
	}
	                

