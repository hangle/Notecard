/* date:   Nov 2, 2012
		   Used by: LogicTokener 
		    and by: LogicTest
	The elements of List[Logic] consists of:
		Relation, e.g., '($abc)=(male)'
		AndOr, logic operator (i.e., 'and' or 'or')
		OpenParen,  double parentheses '((' to push subexpression	
		CloseParen, ')' following a Relation to pop subexpression	

	A condition string, such as:
			((1)=(1)and(2)=(2))or((3)=...) )
	generates a Logic list whose types are:
			OpenParen, Relation, AndOr, Relation, CloseParen, AndOr,
			OpenParen, Relation ..., CloseParen. 

*/
package com.client
import collection.mutable

object LogicType {
		val relationRegex="""([<>=!]+)([1nsc]+)?""" .r
		val gestaultRegex=   """([0-9]+%)([1nsc]+)?""" .r
		val matchRegex=    """(m||!m)([1nsc]+)?""" .r
		def getOperatorAndQualifier(s:String): (String,String)={
		println("LogicType getOperatorAndQual... s="+s)
			s match {
				case relationRegex(a,b) => (a,b)
				case gestaultRegex(a,b) =>    (a,b)
				case matchRegex(a,b) => (a,b)
				case _ => println("LogicType unknown op="+s); (s,null)
				}
		}
			// from script.LogicSupport
	  def removeSpaces(list: List[Char]): List[Char]={
          list match {
              case Nil=> Nil
              case e::t if(e==' ' || e=='\t')=> removeSpaces(t)
              case e:: t=> e :: removeSpaces(t)
              }
          }
		// from script.LogicSupport
		// used in script.ValidateLogic
      def removeSpacesInString(s:String):String ={
          val list=removeSpaces(s.toList)
          list.mkString
          }
	  	// Removes leading and trailing spaces and reduces
		// internal spaces to one space between words.
	def oneSpace(s:String):String={
		val ss=s.trim
		val list=reduceToOneSpace(ss.toList)
		list.mkString
		}
		  	//Example:  "now    is     the time"  to  "now is the time"
	def reduceToOneSpace(ll:List[Char]): List[Char]={
		ll match {
			case Nil=> Nil
			case a :: b :: t if a==' ' && b==' '=> reduceToOneSpace(b::t)
			case a :: t if a==' ' =>  a:: reduceToOneSpace(t)
			case _=>  ll.head :: reduceToOneSpace(ll.tail) 
			}
		}

	class Logic
		// the 'x' in Relationx avoids conflict with Relation a module
		// that supported a obsolate logic testing
	case class Relationx(left:String, operator:String, right:String) extends Logic {
			//	println("LogicType:  instantiate Relationx")
		var leftValue=(left.tail).init   // remove parentheses
			//println("LogicType leftValue="+leftValue)
		var rightValue=(right.tail).init
			// println("LogicType rightValue="+rightValue)
		val (op,qualifiers)=getOperatorAndQualifier(operator)
		println("LogicType:  op="+op+"   qualifiers="+qualifiers)
				// Perform operation on left and right variables, returning
				// true of false. Invoked by LogicTest.recurse()
		def evaluate(table:mutable.Map[String,String]):Boolean={ // logicTest/ 
					//println("LogicType evaluate() ")
				// Convert $<variable> to value
			leftValue=variableToValue(leftValue, table)
				// Skip if match operator. Do table conversions later
			if( ! op.contains("m"))
						// Convert $<variable> to value
					rightValue=variableToValue(rightValue, table)
				// left and right values are converted to lower case letters
				// (qualifier=nc), or spaces removed (qualifier=ns) or only 
				// one space  retained(qualifier=1s)
		println("LogicType: rightValue="+rightValue)
			if(qualifiers != null){	
				leftValue= qualifyValue(qualifiers, leftValue)
					// match qualitifier not performed on list of right values
				if( ! isMatchOperator(op))
					rightValue=qualifyValue(qualifiers, rightValue)
					//	println("LogicType=leftValue="+leftValue+"  rightValue="+rightValue)
				}
				// Indicate whether left and right values are both numbers
			val bothAreNumbers=areNumbers(leftValue, rightValue)
		   println("LogicType: op match {  op: "+op)
		   op match {
			case "=" =>
				val xxx=if(leftValue==rightValue) true; else false
				xxx
			case "<>" => 
				if(leftValue != rightValue) true; else false
				true
			case "!=" =>
				if(leftValue != rightValue) true; else false
			case ">"  =>
				Comparison.greaterThan(leftValue,rightValue, bothAreNumbers)
			case ">=" =>
				Comparison.greaterThanOrEqual(leftValue,rightValue,bothAreNumbers)
			case "<"  =>

				Comparison.lessThan(leftValue, rightValue, bothAreNumbers)
			case "<=" => 
				Comparison.lessThanOrEqual(leftValue,rightValue,bothAreNumbers)
			case "%"  =>
				val percent= operator.init  // drop % symbol
						// Target to be matched to  is rightValue
						// rating >= percent
				Gestalt.testGestalt(percent, rightValue, leftValue)
	 		case "m"  =>
				matchMultipleStringsToTarget(leftValue, rightValue, table)
			case "!m" =>  // return true when match fails
				if(matchMultipleStringsToTarget(leftValue, rightValue, table) ) false; else true
			case _=>
					false
				}
			}	
			// 'leftValue' contains one or more space separated strings whereby
			// each is compared to the 'rightValue'. 'true' returned when left
			// operand matches any element in the right operand's list.
		def	matchMultipleStringsToTarget(leftValue:String, 
										 rightValue:String, 
										 table:mutable.Map[String,String]):Boolean={
				val array= rightValue.trim.split("[ \t]+")
				println("LogicType:  foreach:  "+array.foreach(println) )
						// Right operand containing $<variable>s are translated to values
				val array2= convertVariableToValue(array, table)
				if(array2.contains(leftValue)){
				 	println("LogicType: matgch Multiple true:")
					true
					}
				 else{
				 	println("LogicType: matgch Multiple false:")
				 	false
					}
				}
				// 'm' match command right operand may contain 0 to n $<variable>s.
		def convertVariableToValue( array:Array[String], 
									table:mutable.Map[String,String]): Array[String]={
			println("LogicType  convertVariableToValue...")
			val buffer= new collection.mutable.ArrayBuffer[String]()
			for(e <- array) {
						// $<variable> to Value
				if(e(0)=='$')
					buffer+=variableToValue(e, table)
				  else
				  	buffer+= e
				}	
			buffer.toArray
			}
		def isMatchOperator(operator:String):Boolean={
			if(operator=="m" || operator=="!m") true; else false
			}

			// In a relation, determine is both variables are
			// numbers.
		def areNumbers(leftValue:String, rightValue:String) : Boolean={
			val left:Boolean= VString.isNumber(leftValue)
			val right:Boolean= VString.isNumber(rightValue)
			left == right
			}
			// Detects '$' variables e.g., ($abc).
			// Table key to return the key's value. 
		def variableToValue(variable:String, table:mutable.Map[String,String])={
			if(variable(0) != '$') variable
			  else 	{
				table.get(variable) match {
					case Some(s)=> s.trim
					case _=> "unknown"
					}
				}
			}	
			// Qualifier is a character pair determining how 'leftValue'
			// and 'rightValue' are to be modified. For example,'ns' 
			// removes all whitespaces in both values.
		def	qualifyValue(tag:String, value:String)= {
			var tagString=tag
			var valueString=value
			while(! tagString.isEmpty) {
				var e=tagString.take(2)  // tag is a pair of characters
				e match {
					case "nc" =>   // no case 
						valueString=valueString.toLowerCase
					case "ns" =>   // no whitespaces
						valueString=removeSpacesInString(valueString)
					case "1s" =>   // one space    
						valueString=oneSpace(valueString)
					}
				tagString=tagString.drop(2) // remove after doing its job
				}
			valueString
			}
		}
	case class AndOr(val andOr:String) extends Logic {
		override def toString="andOr="+andOr
		def get=andOr
		}
	case class OpenParen() extends Logic {
		override def toString="("
		}
	case class CloseParen() extends Logic {
		override def toString=")"
		}
			//----------------------------------------------------
			//	Following code was LogicTokener
			//----------------------------------------------------
	def logicTokener(condition:String) ={ //Invoked by: LogicTest.logicTest
			// convert condition string to List[String]
			println("LogicType: logicTokener():  condition="+condition)
		val extracted= extract( List[String](),condition)
				println("LogicType logicTokener extracted="+extracted)
		combine(List[Logic](),extracted)
		}
		 // detects '(('
	def isDoubleParens(s:String)={
		if(s.length < 2) 
			false
		  else	{ 
			if(s(0)=='(' &&  s(1) =='('   )  
				true 
			else 
				false
			}
		}
	def isOpenParens(s:String)={ s(0)=='(' }
	def isCloseParens(s:String)={ s(0)==')' }
	def isAndOrOperator(s:String)= { s=="and" || s=="or" }
	def isPercentSymbolPresent(s:String)={ s.contains("%") }
	def isStartOfOperator(s:String)={ 
		val c=s(0);  // 1 or 2 digits precede % operator
		if(c.isDigit)	
			true
		else
			c=='='|| c=='<' || c=='>' || c=='!' || c=='a' || c=='o' ||  c=='m' || c=='n' 
				}
		// Parses logic string, like, "((abc)=(efg)or(xyz)=(mng))" to
		// List( (,(abc),=,(efg),or,(xyz),=,(mng),) ) one character at
		// a time. 
	def extract(l:List[String],expr:String): List[String] = { // recursive
	   if(expr=="")
			l.reverse  //List
   	    else {
			if( isDoubleParens(expr)) {    // two '('s indicate a push
				val  paren=expr.take(1)
				extract(paren :: l,expr.drop(1))
				}
				// substring to end of relation, extracting relation
				// expr==(
		else if(isOpenParens(expr)) { 
			val index= expr.indexOf(")") +1
			extract(expr.take(index)::l, expr.drop(index)  )  // remove relation expression
			}
				// detect operator, then substring to start of next relation
				// expr== '=' or (<,>,!,a,o,m,n)
		else if( isStartOfOperator(expr)) {
					//println("LogicType:  extract()  isStartOfOperator.. expr="+expr)
			val index=expr.indexOf("(")
			extract(expr.take(index)::l, expr.drop(index)) // removed operator expression
			}
				// expr==)
		else if( isCloseParens(expr)) {
			extract(expr(0).toString :: l, expr.drop(1))
			}
		else { println("LogicType: unknown expr(0)=|"+expr(0)+"|")
			Nil
	 	      }
		}
	  }
		// Combines Reletion elements of left, operator, and right values
		// List( (,(abc),=,(efg),or,(xyz),=,(mng),) ) to:
		// List( (,(abc)=(efg),or,(xyz)=(mng),)  )
		// invoke by logicTokener()
	def combine(ll:List[Logic],l:List[String]) : List[Logic]={
		println("LogicType combine l="+l)
		if(l.isEmpty) ll.reverse
    	   else {
				val s=l.head
				println("LogicType: s="+s)
				if(s.length==1 && isOpenParens(s)) {
					combine(OpenParen():: ll, l.tail)
					}
				else if(s.length==1 && isCloseParens(s)  ) {
				println("LogicType: isColseParens(s) s="+s)
					combine(CloseParen() :: ll, l.tail)
					}
				else if(isOpenParens(s) ) {
				println("LogicType: isOpenParens(s) s="+s)
					combine(Relationx(l(0),l(1),l(2)) :: ll,  l.drop(3))
					}
				else if(isAndOrOperator(s) ) {
				println("LogicType: isAndOr(s) s="+s)
					combine(AndOr(s):: ll,  l.tail)
					}		
				else {
					println("LogicType: unknown value="+s)
					Nil
					}
			}
	    }

}

