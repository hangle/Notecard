/* date:   Dec 12, 2011
  							GROUP RESOLVE 
		GroupNode is a child of CardSet.  When the first GroupNode is detected,
		then CardSet instantiates GroupResolve and passes it GroupNode.
				(CardSet.createGroupResolveAndPassItGroupNode(groupNode) )
		Next, 'whatToDo(..)' is invoked having two options:
			1)	'do'     Invokes 'iterateCardSetChildren' to execute cmds.
			2)  'skip'   Iterate to next GroupNode without execution of cmds.


		Example script code:				second example		third example
		g (1) = (2)					g (1) =(2)						g(1)=(2)
		d this will not be seen		d this will not be seen			d Not seen
		d also this					d also this						ge (1)=(3)
		g							ge								d Not seen 
									d this will be seen				ge (1)=(1)
																	d Seen

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
					// The 1st GroupNode sets 'thenTrue' to either 'true' or false.
					// The 2nd GroupNode whose Group tag is 'ge' will executed
					// the enclose Group command when 'thenTrue' is false.
	var thenTrue=false      
	var elseCondition=false
	var elseConditionTrue=false

		// Invoked in CardSet
		// Group action is based on outcome history
		// one or more GroupNodes. For example, if
		// the then Group clause fails, then its 
		// commands are skipped; and the
		// else Group clause commands are executed. 
	def actionToTake(groupNode:GroupNode)= {	// returns either 'skip' or 'do' or 'done'
				// GroupNode.whatKind is: 
				//		ThenNode--	condition only
				//		ElseNode--	else but no condition
				//		ElseConditionNode-- else and condition
				//		EmptyNode--	no condition and no else
		groupNode.whatKind match {
					// condition only 
			case ThenNode=>	
				if(groupNode.isConditionTrue) {
					println("GroupResolve:  thenTrue is true and do returned")	
					thenTrue=true  // case ElseNode=> will return 'skip'
					"do"
					}
				 else {
					println("GroupResolve:  thenTrue is fasle and skip is returned")	
					thenTrue=false
					"skip"
					}
					// tag 'ge' indicating else without condition expression
			case ElseNode=> 
							// 'elseConditionTrue' must also be tested
				if(thenTrue ==false && elseConditionTrue==false) {
					"do"	}
				else{
					"skip" }

					// tag 'ge <condition>'. 
			case ElseConditionNode=>   // (3)  else and condition
					if( thenTrue==false && elseCondition==false && groupNode.isConditionTrue==true){
						elseConditionTrue=true
						"do"
						}
					else
						"skip"

			case EmptyNode=>  // no else and no condition
					println("GroupResolve:  case EmptyNode=> done")
					"done"
			case _=> println("\t\tGroupResolve: unknown match value="+groupNode.whatKind)
			}
		}
			
	}
	                

