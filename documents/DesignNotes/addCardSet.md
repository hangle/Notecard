<h2>AddCardSet</h2>

<p>The following script creates a CardSet and an AddCardSet.   </p>

<pre><code>    c
    d What is the capital of Ohio (# $columbus)

    +
    d Discover of the New World.

    * end
</code></pre>

<p>The CardSet, beginning with the command 'c', asks a <br />
question requiring a user response. The AddCardSet, <br />
beginning with the command '+', provides a probe or <br />
prompt, that is, a hint, to the user   </p>

<p>The question-CardSet arms the '+Add' button. Activation <br />
of the +Add button presents the AddCardSet along <br />
with its hint. In the AddCardSet, the activation of <br />
the +Add button returns the user to the question-CardSet <br />
to complete the response.       </p>

<p>On the otherhand, the AddCardSet can be avoided. The <br />
user can answer the question, followed by the 'Next' <br />
button, to skip the AddCardSet.   </p>

<p>The AddCardSet is always associated with the <br />
immediately preceding CardSet.  A CardSet may have <br />
more than one AddCardSet as the following script <br />
illustrates.   </p>

<pre><code>    c
    d What is the capital of Ohio (# $columbus)

    +
    d Discover of the New World.

    +
    d Begins with 'C' and has 8 letters

    * end
</code></pre>

<p>Termination of the last AddCardSet initiates the <br />
return or re-display of the question-CardSet. A <br />
single AddCardSet is also the last in the series. <br />
The series my be terminated before the last <br />
AddCardSet by activation of the 'Next' button.   </p>

<p>Notecard Program</p>

<p>The CardSet and AddCardSet are identical in almost <br />
all respects. The differ by the 'button' parameter that <br />
each has.    </p>

<pre><code>    CardSet        button=0     No associated AddCardSet
    CardSet        button=1     Associated AddCardSet
    AddCardSet     button=2     Not last
    AddCardSet     button=99    Last AddCardSet
</code></pre>

<p>A CardSet whose button parameter is '1' has one or more <br />
associated AddCardSets.   </p>

<p>Notecard class:   </p>

<p>CardSet and AddCardSet are children of the Notecard object. <br />
The AddCardSet objects are treated as CardSet objects but retains <br />
AddCardSet indentity by 'button' parameter values of '2' or '99'.      </p>

<pre><code>def executeNotecardChildren(...)  obj match{

            // test button for '2' or '99'
    case bcs:CardSet if(bcs.isAddCardSet) =&gt;
        if(activatedAddButton)
            startCardSetThenDoButtonsAfter(...)
    case cs:CardSet=&gt; 
            // test button for '1'
        if(cs.hasAddCardSet) {
            saveCurrentNode
            buttonSet.armAddButton
            }
          ...
        startCardSetThenDoButtonsAfter(...)
</code></pre>

<p>The first case statement will catch all AddCardSet; however, it <br />
is not executed unless 'activatedAddButton' is true.  The +Add <br />
button sets this boolean value to 'true'.  The user activating <br />
the Next button causes the AddCardSet to be skipped (actuvated- <br />
AddButton is false). The function 'startCardSetThenDoButtonsAfter' <br />
is bypassed.   </p>

<p>Since a CardSet preceeds an associated AddCardSet, the second case <br />
statement handles this object. The 'if(cs.hasAddCardSet)' detects <br />
(button ==1) CardSet objects with dependent AddCardSets. In this <br />
situation, the '+Add' button is highlighted and 'Node' or pointer <br />
to the CardSet is saved to allow the CardSet to be re-presented.   </p>

<pre><code>def waitOverDoButtons(...)  buttonSet.selectedButton match {

    case "+"     //+Add button activated
        if(cardSet.isLastAddCardSet) {  //button== 99   
            activateAddButton=false
            restoreCurrentNode
            }
          else
            activateAddButton=true
</code></pre>

<p>The last AddCardSet, following the associated CardSet, establishes <br />
a return to this CardSet with the function 'restoreCurrentNode' <br />
(see Linker.scala).   </p>

<pre><code>def iterateNotecardChildren(...)

    reset(child)
    while(iterate) {
        ...
        executeNotecardChildren(node, 
        }
</code></pre>

<p>The 'iterate' function addresses the next Notecard child to execute. <br />
Assume that a series of AddCardSets have been processed. The CardSet <br />
that is associated with these objects has had it address saved:   </p>

<pre><code>    saveCurrentNode
</code></pre>

<p>The function:  </p>

<pre><code>    restoreCurrentNode
</code></pre>

<p>causes 'while(iterate) {...' to reprocess the associated CardSet.   </p>
