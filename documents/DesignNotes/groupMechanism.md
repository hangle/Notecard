<h1>Group Command Mechanism</h1>

<p>The following command script displays 'not in scope...'.  The two display <br />
commands:</p>

<pre><code>    d in scope of group command
    d also in scope of group command
</code></pre>

<p>are bounded by the Group command 'g (1)=(1)' and the Clear command of <br />
the next CardSet.  These Display commands are executed when the <br />
condition '(1)=(1)' of the Group Command is true.   </p>

<pre><code>    c
    d not in scope of group command
    g (1)=(1)
    d in scope of group command
    d also in scope of group command
    c  
    ...
</code></pre>

<p>The two Display commands in question are not executed in the following:  </p>

<pre><code>    c
    d not in scope of group command
    g (1)=(2)
    d in scope of group command
    d also in scope of group command
    c  
    ...
</code></pre>

<p>The role of GroupNode is to test the condition of the Group command, <br />
provided that the command has a conditional expression. In the <br />
following script, the second Group command has just the tag 'g'.  </p>

<pre><code>    c
    d not in scope of group command
    g (1)=(2)
    d in scope of group command
    d also in scope of group command
    g
    d not in scope of group command
    c  
    ...
</code></pre>

<p>The scope of 'g (1)=(2)' extends not to the start of the Clear command <br />
but to the second 'g' command.  The second 'd not in scope of ...' is <br />
displayed.   </p>

<p>The 'g' command, without a conditional expression, is a means to <br />
establish scope of the Group command execution.  </p>

<p>The script arguments passed to GroupNode when instantiated are:  </p>

<pre><code>    condition   &lt;expression&gt;    //&lt;expression&gt;  e.g., (1)=(2)
    post        else &lt;or&gt; 0     // else condition cited below.
</code></pre>

<p>The Group command may have the tag 'ge' that denotes an else <br />
condition. For example:</p>

<pre><code>    c
    g($capital)=(Columbus)
    d Correct
    ge
    d Sorry, Columbus is the capital of Ohio
    c
    ...
</code></pre>

<p>Had $capital contained the value Cleveland, then the second Displays <br />
command, and not the first, would have printed.   </p>
