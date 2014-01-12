<pre><code>    BUTTON CARDSET
</code></pre>

<p>In a series of CardSets, the next CardSet is initiated by the
'Next' button.  A ButtonCardSet is initiated by the '+Add' button.</p>

<p>The following  CardSet asks the user to enter the capital of Ohio.  </p>

<pre><code>    c
    d Capital of Ohio is: (# $capital)
</code></pre>

<p>In the case of a user who "knows" the answer but cannot quite recall <br />
it, a hint or a probe would be beneficial.  Something like:  </p>

<pre><code>    Person who discovered the New World
</code></pre>

<p>A CardSet providing the hint or probe must be optional.  Users <br />
knowing the answer should not be encumbered with this CardSet  </p>

<p>The hint or probe CardSet is assigned to the '+Add' button. <br />
User activation of this button presents the following commands: </p>

<pre><code>    b
    d Person who discovered the New World
</code></pre>

<p>The tag 'b' is the leading command of the ButtonCardSet.  The 'b' <br />
command has the same functionality as the 'c' command.   </p>

<p>The following commands includes an additional Button CardSet:  </p>

<pre><code>    c
    d Capital of Ohio is: (# $capitalOh)
                        .
    b
    d Person who discovered the New World
                        .
    b
    d Answer begins with/and ends with,
    d and has 8 letters:
    d         C o l _ _ b u s
                        .
    c Capital of New York is (# capitalNY)
    ...
</code></pre>

<p>The ButtonCardSets must immediately follow the CardSet to which <br />
they are associated.  </p>

<p>A CardSet that has associated one or more ButtonCardSets arms the <br />
'+Add' button.  Its color becomes green.   </p>

<p>The user who answers the Ohio question and does not activate the <br />
'+Add' button skips the ButtonCardSets and is presented with the <br />
New York question.  </p>

<p>Activation of the '+Add' button in the Ohio question CardSet has <br />
the same affect has hitting the 'Next' button. That is, the <br />
following ButtonCardSet is executed.   </p>

<p>The user activates the 'Next' button in both ButtonCardSets.   </p>

<p>Activation of the 'Next' button in the last ButtonCardSet <br />
restores the CardSet associated with the ButtonCardSets.   </p>

<p>The initial CardSet is again executed. Hopefully, the user has been <br />
provided with enough supplemential informaton to answer the Ohio <br />
question.    </p>
