<h1>ButtonCardSet (Script)</h1>

<h2>b Command</h2>

<p>A ButtonCardSet begins with a command whose tag is 'b'. The <br />
ButtonCardSet is a CardSet that is invoked by the button labeled <br />
'+Add'.  The 'b' command allows the presentation of this specialized <br />
CardSet to be controlled by the user. <br />
 The 'b' command of the ButtonCardSet has the same functionality <br />
as  the 'c' command of the CardSet; both clear the screen. Both <br />
sets have the same commands (display, group, eXecute, assign, <br />
edit).   The ButtonCardSet is, however, dependent on an <br />
immediately prior CardSet.  For example:  </p>

<pre><code>    c
    d Capital of Ohio (# $columbus)
    x
    g ($columbus)=(Columbus)
    d Correct
    ge 
    d Sorry, Columbus is correct
                    .
    b
    d The capital is named after the
    d person who discovered the New
    d World

    * end
</code></pre>

<p>A CardSet with a following ButtonCardSet has its '+Add' button <br />
enabled; its color changes from grey to yellow.  Failure to <br />
activate the '+Add' button causes the ButtonCardSet to be skipped. <br />
Activation of this button causes the execution of the ButtonCardSet.  </p>

<p>When the ButtonCardSet terminates, the user is returned to the <br />
associated CardSet.  It is hoped that the supplemental probe of the <br />
New World is sufficient to achieve the correct response-- 'Columbus'.  </p>

<h2>Approach</h2>

<p>The script program (script.scala) validates the '*.nc' file commands <br />
(ParseValidator class.).  The 'CommandMaker.distributeScriptToMaker' <br />
invokes functions to handle a specified tag (like 'c' or 'b').  The 'c' <br />
and 'b' commands both clear the Notecard panel.  Both comands are <br />
leading tags of the CardSet command group.  The CommandMaker class <br />
matches  the 'c' and 'b' tags.  Since both commands have the same <br />
functionality,   CommandMaker invokes the 'CardCommand.cardCommand' for <br />
both; however, this    function is passed "CardSet" or "ButtonCardSet" <br />
to establish later distinction  when 'CardScript.cardScript' is invoked.  </p>

<pre>
            case 'c' =>
                CardCommand.cardCommand(..., "CardSet")
            case 'b' =>
                CardCommand.cardCommand(..., "ButtonCardSet")
</pre>

<p>CardScript.cardScript write commands to 'script:ArrayBuffer[String]'.    </p>

<pre>
            %CardSet        <  >        %ButtonCardSet
            name    ...`                name   ...
            condition ...               condition
            %%                          %%
</pre>

<p>The script program next builds the linked list structure with the <br />
'BuildStructure' class, passing 'script:ArrayBuffer[String]' to <br />
'buildStructure'. The output of  'buildStructure' is <br />
'struct:ArrayBuffer[String]'. 'struct' has the same information <br />
as that of 'script', that is:  </p>

<pre><code>        &lt;%classname&gt; 
        parameters 
        &lt;%%&gt;
</code></pre>

<p>Missing from the original 'struct' ArrayBuffer are symbolic <br />
addresses:   </p>

<pre><code>        &lt;%classname&gt;
        &lt;address&gt;
        &lt;child address&gt;
        &lt;sibling address&gt;
        parameters
        &lt;%%&gt;
</code></pre>

<p>Also missing from the '%CardSet' of our Ohio Capital script example is a new <br />
symbolic  address labeled Button (i.e., 2013):  </p>

<pre><code>        %CardSet
        2002             &lt;Address of CardSet&gt;
        2003             &lt;Address of child (%RowerNode) &gt;
        2020             &lt;Address of Next sibling (%NotecardTask [end]) &gt;
        2013             &lt;Address of '%ButtonCardSet'&gt;
        name    0
        condition   0
        &lt;%%&gt;
</code></pre>

<p>In 'BuildStructure', the symbolic address of Button prior to the invocation <br />
of  </p>

<pre><code>        ButtonCardSetRemap.buttonCardSetRemap(struct)
</code></pre>

<p>was  zero, for example:</p>

<pre><code>        %CardSet
        2002             &lt;Address of CardSet&gt;
        2003             &lt;Address of child (%RowerNode) &gt;
        2013             &lt;Address of Next sibling (%ButtonCardSet) &gt;
        0                &lt;Address effectively null &gt;
        name    0
        condition   0
        &lt;%%&gt;
</code></pre>

<p>'buttonCardSetRemap(struct)' moved the symbolic address from Next sibling to <br />
Button.  In turn,  its finds the symbolic address of the next sibling; in this <br />
case the address is  '%NotecardTask'.</p>
