<h1>Notecard Program</h1>

<p>The program presents a window about the size of a note card.  The window displays text to and 
accepts   input from the program user.  The text and input are controlled by a script file of 
eight command types.   </p>

<p>The description of the Notecard program begins with the script file 'nowis.nc'.  To keep the
explanation simple, 'nowis.nc' consists of the three commands types whose tags are (c, d, *).
'nowis.nc' presents two successive card windows; the first displays "now is" and the second 
displays "the time".</p>

<pre>
                                c
                                d now is
                                c
                                d the time
                                * end
</pre>

<p>The Script program has validated these commands and has generated an input file to the Notecard <br />
program:  </p>

<pre>
                 %Notecard                        %CardSet
                 child       2002                  child    2006
                 height 300                        address  2005
                 width  400                        siblinng 2008
                 font_size  14                     name        0
                 asteriskButton on                 condition   0
                 priorButton    on                 %%
                 %%

                 %CardSet                         %RowerNode
                 child      2003                  child     2007
                 address    2002                  address   2006
                 sibling    2005                  sibling   0
                 name       0                     row       0
                 condition  0                     column    0
                 %%                               %%

                 %RowerNode                       %DisplayText
                 child      2004                  address   2007
                 address    2003                  sibling   0
                 sibling    0                     style   14
                 row    0                         column  0
                 column 0                         name TimesRoman
                 %%                               text the time
                                                  color black
                 %DisplayText                     %%
                 address    2004
                 sibling    0                     %NotecardTask
                 style  1                         address   2008
                 size   14                        task   end
                 column 0                         type   0
                 name   TimesRoman                %%
                 text   now is
                 color  black
                 %%
</pre>

<p>The script beginning with the single '%' symbol, followed by a classname, such as,
"Notecard", create objects from four case classes (Notecard, CardSet, RowerNode, and
DisplayText).  </p>

<pre><code>    %&lt;classname&gt;  match {                             // CreateClass.scala  
        case "%Notecard" =&gt;     notecard=Notecard(..)  
        case "%CardSet" =&gt;      cardSet= CardSet(..)  
        case "%RowerNode" =&gt;    rowerNode=RowerNode(..)  
        case "%DipslayText" =&gt;  displayText=DisplayText(..)  
        .... }
</code></pre>

<p>The pairs of values following the '%<classname>' tag are arguments added to the newly
created case class.  The tag '%%' terminates these arguments. Each case class has the 
method 'receive_objects(..)' that assigns these arguments to the class parameters. </p>

<p>The instances of the case classes form a structure of linked lists; Notecard iterates through
these lists to display its cards.  Note in the 'nowis.struct' the values ranging from 2002 to
2008. These numbers are paired with 'child', 'address' and 'sibling'. Together, the pairs
represent the symbolic addresses of the linked list structure. </p>

<p>The root node instance of the structure is 'Notecard'.  It is the parent of the two 'CardSet'
instances. 'Notecard's child value references the first 'CardSet' whose address is 2002.
The sibling value of the first 'CardSet' references the second 'CardSet' instance whose
sibling value is 0, terminating the list.</p>

<p>The two 'CardSet' instances are both parents having child values (2003 and 2006) referencing
the two 'RowerNode' instances. 'The RowerNode' instances are parents referencing the
'DisplayText' instances.</p>

<p>Finally, 'DisplayText' is also a parent but 'nowis.nc' does not require its children; its
child value is 0.</p>

<p>The linked list classes methods 'contvertToReference(swizzleTable:Map[String,Node])' to 
convert symbolic addresses to physical ones.</p>

<h2>Parent Child Linkage  </h2>

<p>The classes 'Notecard', 'CardSet', 'RowerNode', and 'DisplayText' extend the trait 'Node'. <br />
The trait 'Node' has two variables (child and next) that establishes the parent/child <br />
linkage. </p>

<p>The parent classes 'Notecard', 'CardSet', 'RowerNode', and 'DisplayText' extend the <br />
trait 'Link'  having two funtions to process the parent's linked list.  </p>

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

<p>The 'Notecard' instance passes 'Node.child' to 'reset(...)' and executes the
'while' loop:  </p>

<pre><code>    reset(getFirstChild)   
    while(iterate) {  
        Value match {  
            case cs: CardSet =&gt;  cs.startCardSet(...)  
            ...  }
        }
</code></pre>

<p>When 'startCardSet(..)' is invoked for the first 'CardSet' instance, a similar reset/
while routine is executed:</p>

<pre><code>    reset(getFirstChild)   
    while(iterate) {  
        Value match {  
            case rn: RowerNode =&gt;  rn.startRowerNode(...)  
            ...  }
        }
</code></pre>

<p>Again, in 'RowerNode'  a similar reset/while routine is executed to execute 'DisplayText'
instance. </p>

<pre><code>    reset(getFirstChild)
    while(iterate) {    
        Value match {  
            case rn: RowerNode =&gt; rn.startRowerNode(...)  
            ... }  
            }
</code></pre>

<p>The text of the first card (i.e., "now is") is not shown until 'CardSet' reaches the end of its
list to terminate the 'while(iterate)' loop.  At this point, the window panel is shown
and execution is halted by 'wait()'.</p>

<pre><code>    showPanel(...)  
    haltCommandExecution( lock )
</code></pre>

<p>At this point, the NEXT button  has been enabled and its color is now orange.  When the button 
is activated, the  wait state is released:</p>

<pre><code>    ButtonSet.lock.notifyAll()
</code></pre>

<p>The operation is more complicted when a CardSet has one or more input fields to be <br />
captured.  The Next button is not activated until this capture is completed. </p>

<p>The 'nowis.nc' example is shown as simplified code, for example, the linked list structure was 
depicted five case classes.  The complete set of linked list classes  are:  </p>

<pre>
                                                 Script Examples  
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

<p>The Display command is line oriented such that the text exceeding the window's width <br />
is lost from view.  A specialized layout manager 'NoteLayout' that  extends the <br />
abstract class 'awt.LayoutManager' is utilized to display lines of text.  The <br />
crucial abstract method that the 'NoteLayout' impliments is:  </p>

<pre><code>          def layoutContainer(notePanel: Container) {  
              for( i - 0 until notePanel.getComponentCount())    
                   notePanel.getComponent(i).asInstanceOf[Visual].render()    
              }
</code></pre>

<p>The classes that render text are 'DisplayText', 'DisplayVariable' and 'BoxField'; 
each extends the trait 'Visual' having an abstract method 'render()'.  A simplified <br />
version of 'render()' in 'DisplayText' in which 'awt.Component' methods are invoked: </p>

<pre><code>          def render() {  
              setForeground(color)    //color= black  
              setText(text)           //text=  hello world  
              setBounds(column,row, ....)  // column=0, row=0  
              }
</code></pre>

<p>The 'setBounds' method is the key to the line oriented feature of Notecard.   </p>

<p>In the root structure class, 'Notecard', the 'NotePanel' class instantiates a 'JPanel' <br />
object passing it 'new NoteLayout'.  </p>

<h2>Response Capture  </h2>

<p>In the script 'inputOnly.nc' the user is presented a card window that has a response
or input field. </p>

<pre>
                            inputOnly.nc (file)
               c  
               d (# $myInput)
</pre>

<p>The following shows the 'inputOnly.struct' file.</p>

<pre>
                %Notecard                      %BoxField
                child   2002                   child     0
                height  300                    address  2004
                width   400                    sibling   0
                font_size   14                 field    $myInput
                asteriskButton  on             length   10
                priorButton on                 column   00
                %%                             size     14
                                               style    1
                %CardSet                       name     TimesRoman
                child   2003                   color    black
                address 2002                   limit    99
                sibling 0                      option   0
                name        0                  %%
                condition       0
                %%                             

                %RowerNode
                child   2004
                address 2003
                sibling 0
                row 0
                column  0
                %%
    </pre>

The user's input is handled by the 'BoxField' class (extends JTextField) . The input is 
stored in '$myInput'.  Until the user hits the 'ENTER' key, the 'NEXT' button is inactive. 
The 'ENTER' key set the button color to orange.  Activation of the button would display 
the next card, provided one existed (instead, a new instance of 'Notecard' is created and 
the card repeats).  Typically, a '* end' command is present to terminate the session.

<pre>
        .
</pre>

<p>The parent of 'BoxField' is 'RowerNode'.  When this parent detects its 'BoxField' child,
it creates the following instances and passes its child to both:</p>

<pre><code>    KeyListenerObject (extends KeyListener)
    InputFocus
</code></pre>

<p>'KeyListenerObject' captures the user's response so that 'BoxField' can store it in
'$myInput'.</p>

<p>The role of 'InputFocus' handles the multiple 'BoxField's that a card my have, for example:</p>

<pre>
            c
            d Enter first name (# $first) and last (# $last)
            d Also your age (# $age) and sex (# $gender)
            c ....
</pre>

<p>Four 'BoxField' instances are created for this card.  When the card is presented, the first
input field is given focus. When '$first' is captured, the focus must shift to the next 
input field to catpure '$last'. And so on until all fields have been entered; at which point,
'InputFocus' sets the 'NEXT' button to orange. </p>
