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
		val relationRegex="""([<>=!M]+)([1nsc]+)?""" .r
		val matchRegex=   """([0-9]+%)([1nsc]+)?""" .r
		def getOperatorAndQualifier(s:String): (String,String)={
			s match {
				case relationRegex(a,b) => (a,b)
				case matchRegex(a,b) =>    (a,b)
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
		var leftValue=(left.tail).init   // remove parentheses
		var rightValue=(right.tail).init
		val (op,qualifiers)=getOperatorAndQualifier(operator)
				// Perform operation on left and right variables, returning
				// true of false. Invoked by LogicTest.recurse()
		def evaluate(table:mutable.Map[String,String]):Boolean={ // logicTest/ 
				// Convert $<variable> to value
			leftValue=variableToValue(leftValue, table)
				// Convert $<variable> to value
			rightValue=variableToValue(rightValue, table)
				// left and right values are converted to lower case letters
				// (qualifier=nc), or spaces removed (qualifier=ns) or only 
				// one space  retained(qualifier=1s)
			if(qualifiers != null){	
				leftValue= qualifyValue(qualifiers, leftValue)
				rightValue=qualifyValue(qualifiers, rightValue)
				}
				// Indicate whether left and right values are both numbers
			val bothAreNumbers=areNumbers(leftValue, rightValue)
				//	println("LogicType:  evaluate():  op="+op)
		   op match {
			case "=" =>
				val xxx=if(leftValue==rightValue) true; else false
				println("LogicType:  Relationx:  leftValue="+leftValue++"   rightValue="+rightValue+"  xxx="+xxx)
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
	//			println("LogicType operatorSymbol is %")
				val percent= operator.init  // drop % symbol
						// Target to be matched to  is rightValue
						// rating >= percent
				Gestalt.testGestalt(percent, rightValue, leftValue)
	 		case "m"  =>
				matchMultipleStringsToTarget(leftValue, rightValue)
			case _=>
					false
				}
			}	
			// 'leftValue' contains one or more space separated strings whereby
			// each is compared to the 'rightValue'.
		def	matchMultipleStringsToTarget(leftValue:String, rightValue:String):Boolean={
				val array= rightValue.trim.split("[ \t]+")
				array.foreach(println)
				if(array.contains(leftValue))
					true
				 else
				 	false
				}
			// Operator detected by inspection of 1st character and
			// second character equal to '=' or'm'.  
		def isRelationOperator(operator:String)={
			val relationOperatorRegex="""([=<>!%nm][=m]?)([n1][cs])?""" .r
			operator match {
				case relationOperatorRegex(op, qualify)=> (op, qualify)
				case _=> (null, null)
				}
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
						println("LogicType 'ns' ")
						valueString=removeSpacesInString(valueString)
					case "1s" =>   // one space    
						println("LogicType '1s' ")
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
		val extracted= extract( List[String](),condition)
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
		else if(isOpenParens(expr)) { 
			val index= expr.indexOf(")") +1
			extract(expr.take(index)::l, expr.drop(index)  )  // remove relation expression
			}
				// detect operator, then substring to start of next relation
		else if( isStartOfOperator(expr)) {
			val index=expr.indexOf("(")
			extract(expr.take(index)::l, expr.drop(index)) // removed operator expression
			}
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
	def combine(ll:List[Logic],l:List[String]) : List[Logic]={
		if(l.isEmpty) ll.reverse
    	   else {
		val s=l.head
		if(s.length==1 && isOpenParens(s)) {
			combine(OpenParen():: ll, l.tail)
			}
		else if(s.length==1 && isCloseParens(s)  ) {
			combine(CloseParen() :: ll, l.tail)
			}
		else if(isOpenParens(s) ) {
			combine(Relationx(l(0),l(1),l(2)) :: ll,  l.drop(3))
			}
		else if(isAndOrOperator(s) ) {
			combine(AndOr(s):: ll,  l.tail)
			}		
		else {
			println("unknown value="+s)
			Nil
			}
		}
	    }

}
