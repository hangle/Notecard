<h1>AddCardSet (Notecard)</h1>

<p>The script shows a CardSet and a AddCardSet: </p>

<pre><code>    c
    d I am a card set

    b
    d I am a button card set

    * end
</code></pre>

<p>The AddCardSet with the beginning 'b' tag is associated with <br />
the CardSet with the begnning 'c' tag.  Both the 'c' and 'b' <br />
commands have the same functionality of clearing the screen.    </p>

<p>A CardSet having an associated AddCardSet arms its '+Add' <br />
button; the button color becomes yellow.  Activating this button <br />
executes the AddCardSet.  Waiting for the 'Next' button to <br />
be armed and activating it causes the AddCardSet to be <br />
skipped.    </p>

<h2>Approach</h2>

<p>Notecard  execution is based on a linked list structure of <br />
commands where there is a hierarchy of four sublists and <br />
four parent classes for the four sublists:   </p>

<pre><code>    Notecard
    CarsSet
    RowerNode
    BoxField
</code></pre>

<p>CardSet, RowerNode, and BoxField parent classes have two <br />
variables that hold physical addresses to other class types:   </p>

<pre><code>    child           // starting address of sublist
    sibling         // address of next member in
            // the current sublist
</code></pre>

<p>The CardSet class has an additional physical address variable:   </p>

<pre><code>    button      // starting address of AddCardSet
</code></pre>

<p>The script program treated the AddCardSet as if it was <br />
a CardSet.  At the end, AddCardSet elements are removed <br />
from the linked list heirarchy and assigned to the 'button' <br />
address variables.  The sibling address of CardSet with an <br />
associated AddCardSet is changed to point to next sibling <br />
remaining in the heirarchy.   </p>

<p>Note, a CardSet may have two or more associated AddCardSet <br />
members. Thus it is appropriate to indicate that the button <br />
variable of CardSet holds a sublist of AddCardSet members.   </p>

<p>Notecard's input file '*.struct' distinguishes CardSet and <br />
AddCardSet by different classnames:   </p>

<pre><code>    %CardSet
    %AddCardSet
</code></pre>

<p>'CreateClass.create_object(...)' removed this distinction <br />
by instantiating a CardSet object for both:   </p>

<pre><code>    case "%CardSet" | "%AddCardSet" =&gt;
        val cardSet=CardSet(symbolTable)
</code></pre>

<p>The common CardSet object maintains the distinction via <br />
the 'button' address variable; either it is zero (0) or <br />
it has a physical address value of a AddCardSet. <br />
'CardSet.isAddCardSet' return true when a physical <br />
address is present.    </p>

<p>'Notecard' iterates its children and invokes <br />
'executeNotecardChildren(...):   </p>

<pre><code>    def iterateNotecardChildren(...)
        reset(getFirstChild)
        while(iterate) {
            ...
            executeNotecardChildren(...)
            }

    def executeNotecardChildren(obj:Any, ...)
        obj match   {
                case cs:CardSet=&gt; 
                if(cs.isAddCardSet) 
                    buttonSet.armAddCardSet     
                ...
                cs.startCardSet(...)
                waitOverDoButtons(..., cs, ...)

        case ft:NotecardTask=&gt; ...
        case nf:NextFile =&gt; ...
        ...
</code></pre>

<p>If the CardSet object has an associated AddCardSet, then the <br />
'+Add' button is colored yellow (armAddCardSet).  Next, the <br />
parent CardList processes its sublist of children:   </p>

<pre><code>    cs.startCardSet(...)
</code></pre>

<p>The 'waitOverDoButton(...)' test whether the user activated the <br />
'Next' or the '+Add' button.  In the case of the '+Add' button, <br />
the 'doAddCardSet' function is invoked.     </p>

<pre><code>    ...
    val button= cs.getAddCardSet 
    val buttonCardSet= button.asInstanceOf[CardSet]
    reset(buttonCardSet)        // Linker trait
    while(iterate) {        // Linker trait
        Value match {
        case bcs:CardSet=&gt;
            bcs.startCardSet(...)
    ...
</code></pre>

<p>The 'cs.getAddCardSet' returns CardSet's button:Node value. <br />
The asInstanceOf[CardSet] expression converts it to a CardSet object <br />
which is the associated AddCardSet. 'bsc.startCardSet(...) is <br />
invoked to executed this particular CardSet.   </p>

<p>Two Notecard function have similar code:   </p>

<pre><code>    def iterateNotecardChildren(...)
        reset(getFirstChild)
        while(iterate) {

    def doAddCardSet(..., cs, ...) 
                    ...
        reset(buttonCardSet)    
        while(iterate) {
</code></pre>

<p>When 'doAddCardSet' terminates, then the stack will unwind to <br />
the 'while(iterate)' loop in 'iterateNotecardChildren'. Unfortunately, <br />
as shown, this loop will not point to the initial CardSet object which <br />
is in the linked list hierarchy.  The 'reset(buttonCardSet)' switched <br />
the pointer to the '+Add' button that was removed from the heirarchy.    </p>

<p>Beore 'reset(buttonCardSet)' was executed. the 'cs' value was saved <br />
and following the while loop, it was restored.  Restoring the value <br />
allows the inital CardSet to redisplayed.   </p>
