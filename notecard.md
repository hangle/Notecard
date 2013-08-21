<h1>Notecard Program</h1>

<p>The program presents a window about the size of a note card.  The window displays text to and accepts <br />
input from the program user.  The text and input are controlled by a script file of eight command types.   </p>

<p>The description of the Notecard program begins with the script file 'helloWorld.nc' consisting of <br />
a clear command 'c' and a display command 'd hellow world'.  </p>

<pre><code>              helloWorld.nc  (file)  

    c  
    d hellow world
</code></pre>

The Script program has validated these commands and has generated an input file to the Notecard  
program:  

<pre><code>    helloWorld.struct (file)  

    %Notecard  
    2002  
    300  
    400  
    14  
    %%  
    %CardSet  
    2003  
    2002  
    0  


    %%  
    %RowerNode  
    2004  
    2003  
    0  
    0  
    0  
    %%  
    %DisplayText  
    2004  
    0  
    1  
    14  
    0  
    TimesRoman  
    hello world  
    black  
    %%
</code></pre>

The script beginning with the single '%' symbol create objects from four case classes.  
The 'symbolTable' (Map[String,String]), containing $<variables>, is passed to each   
object.     

<pre><code>    &lt; %&lt;classname&gt;  match {                             // CreateClass.scala  
        case "%Notecard" =&gt;     notecard=Notecard(symbolTable)  
        case "%CardSet" =&gt;      cardSet= CardSet(symbolTablle)  
        case "%RowerNode" =&gt;    rowerNode=RowerNode(symbolTable)  
        case "%DipslayText" =&gt;  displayText=DisplayText(symbolTable)  
        .... }
</code></pre>

Using '%Notecard' of the script file 'hellowWorld.nc' as an example, the line '%%' terminates  
its elements. The script values '2002' to '2004' are symbolic addresses establishing a  
structure of linked lists:   

<pre><code>    'cardSet' is a child of 'notecard'  
    'rowerNode is a child of 'cardSet'  
    'displayText is a child of 'rowerNode'
</code></pre>

The other values of '%Notecard' (300, 400, 14) become field values of 'notecard'. 'cardSet'  
acquires field values (0, "", "" ), 'rowerNode' acquires values (0,0,0) and 'displayText'    
acquires (0,14,0, "", 1, 14, 0, TimesRoman, hello world, black).    

The 'notecard' object is the root of the linked list structure. It may have multiple 'cardSet'  
objects, but in the case of 'helloWorld.nc', there is only one 'cardSet'

The 'cardSet' object clears the window and passes control to its child, 'rowerNode'.   
The 'rowerNode' object employees its field elements row (0) and column (0) to  
position its child objects with the context of the window.    

The  'rowerNode' object has only one child, 'displayText'.  When control is passed to this  
object, the text "hello world" is set up in black using TimesRoman font to be rendered.   

<h2>Parent Child Linkage  </h2>

The classes 'Notecard', 'CardSet', 'RowerNode', and 'DisplayText' extend the trait 'Node'.  
The trait 'Node' has two variables (child and next) that establishes the parent/child  
linkage. The objects 'notecard', 'cardSet', and 'rowerNode' are parents;  each  
has one child to which 'Node.child' holds the reference. In the case of a parent object   
with multiple children, the siblings would be linked by 'Node.next'.  In our example,  
each parent has only one child so 'Node.next' is null.   

The parent classes 'Notecard', 'CardSet', 'RowerNode', and 'DisplayText' extend the   
trait 'Link'  having two funtions to process the parent's linked list.  

<pre><code>    def reset(child:Node) { iterator=child }  
    def iterate= {  
        if(iterator==null)  
            false  
          else {  
            Value=iterator  
            iterator=iterator.getNext.asInstanceOf[Node]  
            true  
            }  
        }
</code></pre>

The 'notecard' object passes 'Node.child' to 'reset(...)' and executes:  

<pre><code>    while(iterate) {  
        Value match {  
            case cs: CardSet =&gt;  cs.startCardSet(...)  
            ...
</code></pre>

Control passes to the 'cardSet' object.  The 'Link' trait functions of 'cardSet' passes  
control to the 'rowerNote' object, and 'rowerNode' objects passes control to the  
'displayText' object.  The 'displayText' object sets up "hello world" to be rendered   
which does not happen until 'cardSet' reaches the end of its linked list:  

<pre><code>    while(iterate) {    
        Value match {  
            case rn: RowerNode =&gt; rn.startRowerNode(...)  
            ... }  
            }  
    showPanel(...)  
    haultCommandExecution( lock )
</code></pre>

The 'showPanel(...)' causes "hellow world" to be displayed.  The next command issues  
a wait state, haulting the execution of the "script".  At this point, the NEXT button  
has been enabled and its color is now orange.  When the button is activated, the  
wait state is released:

<pre><code>    ButtonSet.lock.notifyAll()
</code></pre>

Next, the button is grayed and disabled.   

The operation is more complicted when a CardSet has one or more input fields to be   
captured.  The Next button cannot be activated until this capture is completed. 

The 'helloWorld.nc' example is shown as simplified code, for example, the linked
list structure is depicted as:



b6b11261da4e617553a172b393a9ecfa



The actual structure is:  



b0e5d0dc5b268f8c3d0bafce8ae0fd09


</pre>

<h2>Specialized Layout Manager</h2>

<p>The Display command is line oriented such that the text exceeding the window's width <br />
is lost from view.  A specialized layout manager 'NoteLayout' that  extends the <br />
abstract class 'awt.LayoutManager' is utilized to display lines of text.  The <br />
crucial abstract method that the 'NoteLayout' impliments is:  </p>

<pre>
              def layoutContainer(notePanel: Container) {  
                  for( i - 0 until notePanel.getComponentCount())    
                       notePanel.getComponent(i).asInstanceOf[Visual].render()    
                  }  
The classes that render text are 'DisplayText', 'DisplayVariable' and 'BoxField'; 
each extends the trait 'Visual' having an abstract method 'render()'.  A simplified  
version of 'render()' in 'DisplayText' in which 'awt.Component' methods are invoked: 

              def render() {  
                  setForeground(color)    //color= black  
                  setText(text)           //text=  hello world  
                  setBounds(column,row, ....)  // column=0, row=0  
                  }      
</pre>

<p>The 'setBounds' method is the key to the line oriented feature of Notecard.   </p>

<p>In the root structure class, 'Notecard', the 'NotePanel' class instantiates a 'JPanel' <br />
object passing it 'new NoteLayout'.  </p>

<h2>Response Capture  </h2>

<p>In the script 'findName.nc' the user is asked to enter first and last names:  </p>

<pre>
                            findName.nc (file)
               c  
               d Enter first (# $first) and last (# $last) names
</pre>

<p>The following shows the structure file (findName.struct) with '%Notecard' and <br />
'%CarsSet' elements removed. The parent '%RowerNode' and its five children are <br />
shown:  </p>

<pre>
                                    %RowerNode
                                    2004
                                    2003
                                    0
                                    0
                                    0
                                    %%

         %DisplayText   %BoxField   %DisplayText  %BoxField   %DisplayText
         2004           2005        2006          2007        2008
         2005           2006        2007          2008        0
         1              $first      1             $last       1
         14             10          14            10          14
         0              00          0             00          0
         TimesRoman     14          TimesRoman    14          TimesRoman
         Enter first    1           and last      1            names
         black          TimesRoman  black         TimesRoman  black 
         %%             black       %%            black       %%
                        99                        99
                        0                         0
                        %%                        %%
</pre>

<p>The '%DisplayText' script have the text (Enter first, and last, names). The '%BoxField' <br />
script have the 'symbolTable' variables ($first, $last).  The user is required to <br />
enter two responses and to then activate the NEXT button.  </p>

<p>A few tasks must occur before the NEXT button is enabled and its color set to orange. The <br />
first input field must be given focus.  The ENTER key assigns $first to the 'symbolTable' <br />
and the focus shifts to the second input field. The ENTER key assigns $second. A check <br />
is made that there are no additional input fields.    </p>

<p>'BoxField' establishes the window's input field. When the field gains focus, then the <br />
'KeyListener' interface reports the keys that are entered.  The class 'KeyListenerObject' <br />
extends 'KeyListener'.  'RowerNode' instantiates a 'KeyListererObject' when it detects <br />
that one of its children is a 'BoxField'.  Also, the 'BoxField' object is passed to <br />
the 'KeyListenerObject', along with an object of 'InputFocus' that maintains which <br />
'BoxField' object has focus.  </p>

<p>The 'InputFocus' object is created by the parent 'CardSet'.  A CardSet may have two <br />
or more Display commands with input fields, for example:  </p>

<pre><code>    c  
    d First name (# $first) last name (# $last)  
    d Street (# $street)  city (# $city)  
    d Zip code (# $zip)
</code></pre>

<p>Each 'BoxField' object is added to an array within  'InputFocus'.  In the example, <br />
'InputFocus' tracks the focus of five 'BoxField' objects. It also determines when all <br />
five responses have been captured.  It then enables the NEXT button and sets it to orange.   </p>
