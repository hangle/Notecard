<h2>+Add Button Mechanism</h2>

<p>In testing, for example, asking for the capital of Ohio, <br />
Notecard provides CardSets acting as prompts and/or probes, <br />
that is "hints", to elicit a weak response.  These cards <br />
are optional and can be bypassed.   </p>

<p>The optional CardSet is termed a ButtonCardSet. A <br />
particular ButtonCardSet is only associated with the <br />
CardSet asking the question. The ButtonCardSet is <br />
activated by the '+Add' button.    </p>

<p>The following script illustrates a CardSet asking <br />
a question, followed by a ButtonCardSet that <br />
provides a probe to elicit a correct response.    </p>

<p>The trick is to skip to the ButtonCardSet and then <br />
return to the question-CardSet to complete the answer.    </p>

<pre><code>c
d Capital of Ohio is (# $columbus)
b
d Discover of the New World.
* end
</code></pre>

<p>The tag 'c' begins the CardSet asking the question. <br />
The tag 'b' begins the ButtonCardSet providing the <br />
probe.  Its activation is via the '+Add' button that <br />
is highlighted by the question-asking CardSet.    </p>

<p>Unless the '+Add' button is activated, then the user's <br />
response entry, followed by the 'Next' button, bypasses <br />
the ButtonCardSet. Again, the ButtonCardSet is optional.    </p>

<p>Activation of the '+Add' button presents the card with <br />
the probe. The question bypass is, however, only <br />
temporary; the user is returned to the question-asking <br />
CardSet following the completion of the ButtonCardSet <br />
series.     </p>

<p>More than one ButtonCardSets can be associated <br />
with a given CardSet. When there is more than one, <br />
the series can be terminated at any point with a return <br />
to the question-asking CardSet by activation of the '+Add' <br />
button. The '+Add' button acts like a toggle switch to <br />
start/stop the action, similar to that of the '*' button.   </p>

<p>The '+Add' button activates the first ButtonCardSet; <br />
the 'Next' button activates the remaining series of <br />
ButtonCardSets. When the last of the series is encountered, <br />
the '+Add' button will no longer be highlighted. <br />
Nevertheless, the 'Next' button activation preforms the <br />
CardSet return.   </p>

<p>Question-asking CardSet with two ButtonCardSets.   </p>

<pre><code>c
d Capital of Ohio is (# $columbus)   
b   
d Discover of the New World.
b
d Begins with C and has 8 letters
* end
</code></pre>

<p>Of the two ButtonCardSets, the last terminates the series <br />
and returns the user to the question-CardSet.  The '+Add' <br />
button is highlighted during the series-- except for the <br />
last. Activation of the '+Add'  button, terminates the <br />
series and returns the user to the question-CardSet.   </p>

<p>Script Program</p>

<p>The Script Program distinguishes a ButtonCardSet by the CardScript <br />
parameter variable 'button'.  All CardSets and ButtonCardSets have <br />
this parameter.  A CardSet lacking an associated ButtonCardSet has <br />
a 'button' value of zero (0). A CardSet with one or more <br />
ButtonCardSets has a value of one (1). ButtonCardSets either have <br />
a button value of two (2) or ninety-nine (99). The 99 is reserved <br />
for the last ButtonCardSet in the series.    </p>

<p>Notecard Program</p>

<p>Recall that a 'c' command with a "failed" logic expression, such as <br />
'(1)=(2)' causes the CardSet object, along with its children and <br />
grandchildren, to be skipped in the Notecard class (see. <br />
executeNotecardChildren). The same happens with ButtonCardSets <br />
unless the '+Add' button is activated. The user must force the <br />
presentation of the ButtonCardSet.  The ButtonCardSet is a <br />
CardSet that displays a card. It differer in the value of its <br />
'button' parameter.   </p>

<p>A CardSet, having an associated ButtonCardSet (button=1), highlights <br />
the '+Add' button(color yellow) and the Notecard class global variable <br />
'activatedAddButton' is set true. The ButtonCardSet(s) are no longer <br />
skipped. Another important task of the CardSet is to save its node <br />
address (Linker.saveCurrentNode).  When the ButtonCardSet series <br />
terminate, Linker.estoreCurrentNode is invoked to re-present the <br />
CardSet (see Linker for mechanism).    </p>
