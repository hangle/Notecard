<h1>Input Mechanism</h1>

<p>The commands of a CardSet are executed and control passes <br />
to an input field (JComponent) or a button (JButton). <br />
On completion of the CardSet commands, execution is halted <br />
by CardSet:</p>

<pre><code>    def haltCommandExecution(lock:AnyRef): Unit=lock.synchronized {
        lock.wait() 
        }
</code></pre>

<p>When a CardSet has an input field, the mechanism is a little more <br />
complicated, so the initial description excludes the input field.  </p>

<p>The following script lacks a input field:  </p>

<pre><code>    c
    d display text
    * end
</code></pre>

<p>The CardSet commands consist of 'c' and 'd display text'. <br />
Their execution initiates the 'wait()' state and the <br />
Next button is enabled (colored orange); The Next button <br />
activation causes ButtonSet to release the wait state by <br />
invoking:</p>

<pre><code>    def start():Unit= {
        lock.synchronized{ lock.notifyAll() } 
        }
</code></pre>

<p>CardSet returns control to Notecard to process its next child. <br />
In the case of the above script, the Asterisk '* end' command <br />
is executed and the session is terminated.  </p>

<p>The following script has an input field:</p>

<pre><code>    c
    d (# $age)
    * end
</code></pre>

<p>When a CardSet object is invoked, it creates an InputFocus object <br />
that is used by the input field. Also created is a ListenerArray <br />
object used by the input field.  In the earlier script, InputFocus <br />
and ListenerArray were unused. The current script brings the two <br />
into play.   </p>

<p>The BoxField class supports the input field operation. This class <br />
is a child of the RowerNode class which, in turn, is a child of <br />
CardSet ( d (# $age) is a 'd' command occupying a given card row <br />
is supported by RowerNode).  </p>

<h3>RowerNode</h3>

<p>Parent: CardSet <br />
Children: DisplayText, DisplayVariable, BoxField.  </p>

<pre><code>    def executeOneRowerNodeChild(...
        obj match {
            case dt:DisplayText=&gt; ...
            case dv:DisplayVariable=&gt; ...
            case bf:BoxField=&gt;
                BoxField.startBoxField(rowPosition)
                notePanel.add(bf) // bf is JTextField
                createListenerAndFocus(...)
            }   
        }

    def createListAndFocus( ...
        inputFocus.addToArray(boxField)         
        val keyListenerObject(boxField, inputFocus, ...
</code></pre>

<p>The above code is described with respect to the following CardSet <br />
script that has two input fields (BoxField)s.  </p>

<pre><code>    c
    d (# $one)
    d (# $two)
    c ...
</code></pre>

<p>The screen displays two input fields; the first on row 1 and the <br />
second on row 2. The class InputFocus places the cursor into the <br />
first field.  InputFocus moves the cursor to the second field on <br />
capture of the first response. On capture of the second response, <br />
InputFocus detects that the CardSet, with its two input fields, <br />
has captured both responses, therefor, the Next button is <br />
enabled, allowing the next CardSet to execute.  </p>

<p>The function 'createListAndFocus' passes BoxField to InputFocus to <br />
be stored in an ArrayBuffer[JComponent] (BoxField extends JTextField). <br />
This array serves two purposes. First, each JComponent holds the <br />
screen coordinates of the input field.  Second, the size of the array <br />
indicates the number of CardSet input fields.   </p>

<h3>InputFocus</h3>

<p>Parent: RowerNode
Collaberates with: KeyListenerObject.</p>

<p>In RowerNode, the class KeyListenerObject is created for each input <br />
field (BoxField) of the CardSet.  Its parameters are InputFocus and <br />
BoxField. The KeyListenerObject performs the response capture  and <br />
transfer control to 'InputFocus.actWhenAll- FieldsCaptured' when the <br />
Enter key is pressed.</p>

<pre><code>    def actWhenAllFieldsCaptured(...
        arrayIndex += 1 
        if(arrayIndex == components.size)
            ...
            buttonSet.armNextButton
            buttonSet.next.requestFocus
            ...
          else
            components(arrayIndex).requestFocus
</code></pre>

<p>Recall, the number of elements in ArrayBuffer[JComponent], determined <br />
by 'component.size', is equal to the number of CardSet input fields. <br />
In the script example, the if statment fails for the first response <br />
capture, so the focus is directed to the second field (component(1)). <br />
The if statement succeeds after the second response is captured. <br />
The Next button is enabled and given focus.   </p>
