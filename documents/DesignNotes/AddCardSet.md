<h1>AddCardSet (Notecard)</h1>

<p>The script shows a two CardSets beginning the the 'c' (clear) <br />
command and a Add-CardSet beginning with a '+' command ('+' <br />
also clears the screen):   </p>

<pre><code>    c
    d I am a card set

    +
    d I am an add card set

    c
    d I am a second card set

    * end
</code></pre>

<p>The 'c' command begins a CardSet. The '+' command begins an 
AddCardSet.</p>

<p>Notecard buttons consist of the following:  </p>

<pre><code>    *, +Add, Prior, and Next
</code></pre>

<p>The 1st CardSet has its '+Add' and 'Next' buttons  both enabled ('+Add 
button is yellow and 'Next' button is orange).</p>

<p>In the first CardSet with the display 'I am a card set', activation <br />
of the Next button presents the second CardSet with the display 'I <br />
am a second card set'.  The Add-CardSet is skipped without <br />
displaying 'I am an add card set'.   </p>

<p>One or more Add-CardSets that immediately follow a CardSet are <br />
controlled by the +Add button.  While 'I am a card set' is <br />
shown, then activation of the +Add button clears the screen and <br />
displays 'I am an add card set'.  A second activation of the <br />
+Add button terminates the Add-CardSet because the linked list <br />
of the one Add-CardSet has ended.  </p>

<p>Following the screen erasurer of 'I am an add card set', <br />
the initial CardSet with the display 'I am a card set' is <br />
reactivated.   </p>

<p>In the case where there are two or more immediate Add-CardSets, <br />
the +Add button would activate the multiple Add-CardSets. <br />
Activation, of the Next button would, however, terminate <br />
the Add-CardSets.  </p>

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
a CardSet.  In the Script program, AddCardSet elements are removed <br />
from the linked list heirarchy by ButtonCardSetRemap and are <br />
assigned to the 'button' address variables.  The sibling address <br />
of CardSet with an associated AddCardSet is changed to point to <br />
next sibling remaining in the heirarchy.   </p>

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
it has a physical address value of an AddCardSet. <br />
'CardSet.isAddCardSet' return true when a physical <br />
address is present.    </p>

<h2>Notecard</h2>

<p>'Notecard', as the root of the linked hierarchy, iterates its children <br />
and invokes 'executeNotecardChildren(...):   </p>

<pre><code>    def executeNotecardChildren(obj:Any, ...)
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

<p>If the CardSet object has an associated AddCardSet, then
on completion of the CardSet objects (waitOverDoButtons(...),
the '+Add' button is colored yellow (armAddCardSet). <br />
Activation of the +Add button along with 'doAddCardButton(...) <br />
invokes:
        cs.startCardSet(...)</p>

<p>It is noted that Notecard invokes 'startCardSet(...)' from two 
different functions:
        1.  startNotecard(...) <br />
        2.  doAddCardButton(...)
Also, 'doAddCardButton' is called within the scope of
'startNotecard'.  The 'startCardSet' boolean parameter 'addCardSetFlag'
indicates which function invoked it. This parameter is 'true' when
called by 'doAddCardButton'. The parameter functions to enable and
disable the +Add button.</p>

<h2>Add Button</h2>

<p>Two functions of ButtonSet control the AddCardSet button
    1. armAddButton             --yellow highlight 
    2. grayAndDisableAddButton  --grays an inactivates the button</p>

<pre><code>addCardSetFlag match {
    case true =&gt;            // AddCardSet
        if(isLastAddCardSet)    // symSibling =="0"     see def above
            buttonSet.grayAndDisableAddButton
          else
            buttonSet.armAddButton  // color button yellow
    case false =&gt;           // CardSet
        if(isAddCardSet)        // symAddButton != "0"  see def above
            buttonSet.armAddButton  // color button yellow
          else
            buttonSet.grayAndDisableAddButton
        }
</code></pre>
