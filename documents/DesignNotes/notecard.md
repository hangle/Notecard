<h1>Notecard Program</h1>

<p>The program presents a window about the size of a note card. <br />
The window displays text to and accepts input from the program <br />
user.  The text and input are controlled by a script file of <br />
ten command types.     </p>

<p>The description of the Notecard program begins with the script <br />
file 'nowis.nc'.  To keep the explanation simple, 'nowis.nc' <br />
consists of the three commands types whose tags are (c, d, *). <br />
'nowis.nc' presents two successive card windows; the first <br />
displays "now is" and the second displays "the time".  </p>

<pre>
        c
                d now is
                c
                d the time
                * end
</pre>

<p>The Script program has validated these commands and has <br />
generated an input file (nowis.struct) to the Notecard program <br />
(beginning with the '%Notecard' group and ending with the <br />
'%NotecardTask' group:  </p>

<pre>
                 %Notecard                         %CardSet
                 child       2002                  address    2005
                 height      300                   child      2006
                 width       400                   sibling    2008
                 font_size   14                    name       0
                 asteriskButton on                 condition  0
                 priorButton    on                 %%
                 %%

                 %CardSet                         %RowerNode
                 address    2002                  address   2006
                 child      2003                  child     2007
                 sibling    2005                  sibling   0
                 name       0                     row       0
                 condition  0                     column    0
                 %%                               %%

                 %RowerNode                       %DisplayText
                 address    2003                  address  2007
                 child      2004                  sibling      0
                 sigling    0                     style    14
                 row        0                     column    0 
                 column     0                     name    TimesRoman
                 %%`                              text    the time
                                                  color   black
                                                  %%
                 %DisplayText                    
                 address    2004
                 sibling    0                    %NotecardTask
                 style      1                    address   2008
                 size       14                   task      end
                 column     0                    type      0
                 name       TimesRoman           %%
                 text       now is
                 color      black
                 %%
</pre>

<p>The script beginning with the single '%' symbol, followed by a <br />
classname, such as,   "Notecard", create objects from four case <br />
classes (Notecard, CardSet, RowerNode, and  DisplayText).     </p>

<pre><code>            CreateClass.scala
            -----------------
    %&lt;classname&gt;  match {                             
        case "%Notecard" =&gt;     notecard=Notecard(..)  ...
        case "%CardSet" =&gt;      cardSet= CardSet(..)  ...
        case "%RowerNode" =&gt;    rowerNode=RowerNode(..)  ...
        case "%DipslayText" =&gt;  displayText=DisplayText(..)  ...
        .... }
</code></pre>

<p>The pairs of values following the '%<classname>' tag are arguments <br />
added to the newly  created case classes.  The tag '%%' terminates <br />
these arguments. Each case class has the method 'receive_objects(..)' <br />
that assigns these arguments to the class parameters.  For example, <br />
the arguments for the 'displayText' instance are:   </p>

<pre><code>    14
    0
    TimesRoman
    the time
    black
</code></pre>

<p>The instances of the case classes form a structure of linked <br />
lists; Notecard iterates through these lists to display its cards. <br />
Note in the 'nowis.struct' the values ranging from 2002 to <br />
2008. These numbers are paired with 'child', 'address' and 'sibling'. <br />
Together, the pairs  represent the symbolic addresses of the linked <br />
list structure.   </p>

<p>The root node instance of the structure is 'Notecard'.  It is the <br />
parent of the two 'CardSet' instances. 'Notecard's child value <br />
references the first 'CardSet' whose address is 2002.  The sibling <br />
value of the first 'CardSet' references the second 'CardSet' <br />
instance whose sibling value is 0, terminating the list.   </p>

<p>The two 'CardSet' instances are both parents having child values <br />
(2003 and 2006) referencing the two 'RowerNode' instances. 'The <br />
RowerNode' instances are parents referencing the 'DisplayText' <br />
instances.  </p>

<p>Finally, 'DisplayText' is also a parent but 'nowis.nc' does not <br />
require its children; its  child value is 0.</p>

<p>The linked list classes methods: </p>

<pre><code>contvertToReference(swizzleTable:Map[String,Node])
</code></pre>

<p>converts symbolic addresses to physical ones.</p>

<h2>Parent Child Linkage  </h2>

<p>The classes 'Notecard', 'CardSet', 'RowerNode', and <br />
'DisplayText' extend the trait 'Node'.    The trait 'Node' has <br />
two variables (child and next) that establishes the <br />
parent/child linkage. </p>

<p>The parent classes 'Notecard', 'CardSet', 'RowerNode', and <br />
'DisplayText' extend the trait 'Link'  having two funtions <br />
to process the parent's linked list.    </p>

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

<p>The 'Notecard' instance passes 'Node.child' to 'reset(...)' <br />
and executes the 'while' loop:  </p>

<pre><code>    reset(getFirstChild)   
    while(iterate) {  
        Value match {  
            case cs: CardSet =&gt;  cs.startCardSet(...)  
            ...  }
        }
</code></pre>

<p>When 'startCardSet(..)' is invoked for the first 'CardSet' <br />
instance, a similar reset  while routine is executed:</p>

<pre><code>    reset(getFirstChild)   
    while(iterate) {  
        Value match {  
            case rn: RowerNode =&gt;  rn.startRowerNode(...)  
            ...  }
        }
</code></pre>

<p>Again, in 'RowerNode'  a similar reset/while routine is <br />
executed to execute 'DisplayText' <br />
instance. </p>

<pre><code>    reset(getFirstChild)
    while(iterate) {    
        Value match {  
            case rn: RowerNode =&gt; rn.startRowerNode(...)  
            ... }  
            }
</code></pre>

<p>The text of the first card (i.e., "now is") is not shown <br />
until 'CardSet' reaches the   end of its list to terminate <br />
the 'while(iterate)' loop.  At this point, the window <br />
panel is shown and execution is halted by 'wait()'.  </p>

<pre><code>    showPanel(...)  
    haltCommandExecution( lock )
</code></pre>

<p>At this point, the NEXT button  has been enabled and its color <br />
is now orange.  When the button  is activated, the  wait <br />
state is released:  </p>

<pre><code>    ButtonSet.lock.notifyAll()
</code></pre>

<p>The operation is more complicted when a CardSet has one or <br />
more input fields to be   captured.  The Next button is not <br />
activated until this capture is completed.   </p>

<p>The 'nowis.nc' example is shown as simplified code, for <br />
example, the linked list structure was depicted five case <br />
classes.  The complete set of linked list classes, <br />
as well as associated script command examples, are:   </p>

<pre>
                                                 Script Examples  
                                 ---------------
               Notecard  
                  NotecardTask                   * end  
                  NextFile                       f maleScript  
                  CardSet                        c (1)=(2)  
                      Assign                     a $count=$count+1  
                      XNoded                     x  
                      GroupNode                  g (1) = (1)  
                      CardSetTask                * continue  
                      RowerNoded                 d 3/5/  
                          DisplayText            d now is  
                          DisplayVariable        d (% $count)  
                          BoxField               d (# $name)  
                              EditNode           e ($count) < (5)  
</pre>

<h2>Specialized Layout Manager</h2>

<p>The Display command is line oriented such that the text <br />
exceeding the window's width is lost from view.  A <br />
specialized layout manager 'NoteLayout' that  extends the <br />
abstract class 'awt.LayoutManager' is utilized to display <br />
lines of text.  The   crucial abstract method that the <br />
'NoteLayout' impliments is:  </p>

<pre><code>          def layoutContainer(notePanel: Container) {  
              for( i - 0 until notePanel.getComponentCount())    
                   notePanel.getComponent(i).asInstanceOf[Visual].render()    
              }
</code></pre>

<p>The classes that render text are 'DisplayText', <br />
'DisplayVariable' and 'BoxField';  each extends the trait <br />
'Visual' having an abstract method 'render()'.  A simplified <br />
version of 'render()' in 'DisplayText' in which 'awt.Component' <br />
methods are invoked:   </p>

<pre><code>          def render() {  
              setForeground(color)    //color= black  
              setText(text)           //text=  hello world  
              setBounds(column,row, ....)  // column=0, row=0  
              }
</code></pre>

<p>The 'setBounds' method is the key to the line oriented feature <br />
of Notecard.   </p>

<p>In the root structure class, 'Notecard', the 'NotePanel' class <br />
instantiates a 'JPanel'  object passing it 'new NoteLayout'.    </p>

<h2>Response Capture  </h2>

<p>In the script 'inputOnly.nc' the user is presented a card window <br />
that has a response  or input field.   </p>

<pre>
                inputOnly.nc (file)
        -----------------
               c  
               d (# $myInput)
</pre>

<p>The following shows the 'inputOnly.struct' file.</p>

<pre>
                %Notecard                      %BoxField
                child   2002                   address  2006   0
                height  300                    address  0 
                width   400                    sibling   0
                font_size   14                 field    $myInput
                asteriskButton  on             length   10
                priorButton on                 column   00
                %%                             size     14
                                               style    1
                %CardSet                       name     TimesRoman
                address    2002                color    black
                child      2003                limit    99
                sibling    0                   option   0
                name       0                    %%
                condition  0
                %%                             

                %RowerNode
                address 2003
                child   2004
                sibling 0
                row     0
                column  0
                %%
</pre>

<p>The user's input is handled by the 'BoxField' class (extends <br />
JTextField) . The input is   stored in '$myInput'.  Until the <br />
user hits the 'ENTER' key, the 'NEXT' button is inactive. <br />
The 'ENTER' key set the button color to orange.  Activation of <br />
the button would display the next card, provided one existed <br />
(instead, a new instance of 'Notecard' is created and  the <br />
card repeats).  Typically, a '* end' command is present to <br />
terminate the session.  </p>

<p>The parent of 'BoxField' is 'RowerNode'.  When this parent <br />
detects its 'BoxField' child,  it creates the following <br />
instances and passes its child to both:  </p>

<pre><code>    KeyListenerObject (extends KeyListener)
    InputFocus
</code></pre>

<p>'KeyListenerObject' captures the user's response so that <br />
'BoxField' can store it in  '$myInput'.</p>

<p>The role of 'InputFocus' handles the multiple 'BoxField's <br />
that a card my have, for example:  </p>

<pre>
            c
            d Enter first name (# $first) and last (# $last)
            d Also your age (# $age) and sex (# $gender)
            c ....
</pre>

<p>Four 'BoxField' instances are created for this card.  When <br />
the card is presented, the first  input field is given <br />
focus. When '$first' is captured, the focus must shift <br />
to the next input field to catpure '$last'. And so on until <br />
all fields have been entered; at which point,  'InputFocus' <br />
sets the 'NEXT' button to orange.   </p>
