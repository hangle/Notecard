<h1>AddCardSet</h1>

<p>An AddCardSet has the same functionality as a CardSet. <br />
However, it does not begin with the 'c' command. Instead, <br />
it begins with the '+' command; and, like the 'c' command, <br />
it also clears the  screen.    </p>

<p>The two Sets differ by their controlling button. The CardSet <br />
is activated by the Next button.  The AddCardSet is activated <br />
by the +Add button.  </p>

<p>One or more AddCardSets are associated with a particular <br />
CardSet.  As such, the AddCardSet(s) must immediately <br />
follow the particular CardSet. The user has the option of <br />
executing the associated AddCardSet(s) by activating the <br />
+Add button, or skipping the AddCardSet(s) by activating <br />
the Next button.  </p>

<p>The AddCardSets to provide  optional information that <br />
serves to clarify the information provided by a CardSet.    </p>

<p>The following  CardSet asks the user to enter the capital of Ohio.    </p>

<pre><code>    c
    d Capital of Ohio is: (# $capital)
</code></pre>

<p>In the case of a user who "knows" the answer but cannot quite recall <br />
it, a hint or a probe would be beneficial, perhaps something like:  </p>

<pre><code>    Discover of the New World
</code></pre>

<p>An AddCardSet providing the hint or probe must be optional.  Users <br />
knowing the answer should not be encumbered with unneeded information.  </p>

<p>The hint or probe CardSet is assigned to the '+Add' button. <br />
User activation of this button presents the following commands:   </p>

<pre><code>    +
    d Discover of the New World
</code></pre>

<p>The tag '+' is the leading command of the AddCardSet.  </p>

<p>The following commands includes two AddCardSets associated <br />
with the CardSet seeking the answer: Columbus.  </p>

<pre><code>    c
    d Capital of Ohio is: (# $capitalOh)
                        .
    +
    d Discover of the New World
                        .
    +
    d Answer begins with C and has 8 letters 
                        .
    c 
    d Capital of New York is (# capitalNY)
    ...
</code></pre>

<p>A CardSet that has associated one or more AddCardSets arms the <br />
'+Add' button.  Its color becomes yellow.   </p>

<p>The user who answers the Ohio question and does not activate the <br />
'+Add' button skips the AddCardSets and is presented with the <br />
New York question.   </p>

<p>In the case of multiple AddCardSets associated with a particular <br />
CardSet, the '+' Add button of the last AddCardSet terminates the <br />
series and re-presents the associated CardSet.  Thus, the user, <br />
equipped with the addition information of the AddCardSets, is <br />
given the opportunity to respond appropriately to the question.     </p>

<p>In a series of AddCardSet, the series is terminated at any <br />
point by the activation of the Next button. Again, the user <br />
is returned to the associated CardSet.  </p>
