<h1>Logic Component</h1>

<p>The following command types have a logic component.  </p>

<pre><code>    Clear
    Group
    Assign
    File
    Edit
</code></pre>

<p>The outcome of the logic component determines whether or not the command is <br />
executed.  As example, a Clear command with a logic component and an associated <br />
Display command is:  </p>

<pre><code>    c (1) = (2)  
    d This display is never executed becase the Clear command always fails.  
    c ...
</code></pre>

<p>When the logic of the Clear 'c' command fails, then the entire group CardSet commands <br />
are skipped.  </p>

<p>The logic component consists of the following elements:  </p>

<pre><code>    value / $Variable  
    relational operators  
    logic operators  
    qualifying tags
</code></pre>

<hr />

<p>Value / $Variable elements  </p>

<hr />

<p>Logic values are, for example:   </p>

<pre><code>    22, male, flag, 3.14.
</code></pre>

<p>$Variables are, for example:    </p>

<pre><code>    $male and $xyz.
</code></pre>

<p>A Value or $Variable are enclosed in parentheses, for example:   </p>

<pre><code>    (22), (male), (flag), (3.14), ($male), ($xyz)  (east end)
</code></pre>

<hr />

<p>Relational operator</p>

<hr />

<p>The relational operators are:</p>

<pre>
        <        less than
        >        greater than
        <=       less than or equal to
        >=       greater than or equal to
        !=       not equal
        <>       not equal
        n%       percentage match when n is a percentage value (see explanation below)
        m        match left-operand with right-operand list of items. (see explanation below)
        !m       match left-operand with right-operand list of items. (see explanation below)
</pre>

<p>The relational operator compares two operands (Value / $Variable), for example:  </p>

<pre><code>    ($age) &gt;= (21)
</code></pre>

<hr />

<p>Logic operators </p>

<hr />

<pre><code>    or
    and
</code></pre>

<p>The logic operator compares the outcome of relational operations, for example:</p>

<pre><code>    $age) &gt;= (21) and ($age) &lt; (65)
    ($a) &lt; ($b) or ($c) =($d) ) and ( ($e) &gt;= ($f) and ($g) &lt;&gt; ($h) )
</code></pre>

<hr />

<p>Qualifying tags</p>

<hr />

<pre><code>    nc      No case. Convert to lower case before relational operation.
    ns      No spaces. Remove spaces before relational operation.
    1s      Reduce consecutive spaces to one space.
</code></pre>

<p>The following logic component with the 'nc' tag returns true:  </p>

<pre><code>    (Columbus) = nc (columbus)
</code></pre>

<p>The following logic component with the 'ns' tag returns true:  </p>

<pre><code>    (Columbus) = ns ( C olu m bus )
</code></pre>

<p>The following logic component with the 'nc' and 'ns' tags returns true:  </p>

<pre><code>    (Columbus) = nc ns ( c olu m bus )
</code></pre>

<p>The following logic component with the '1s' tag returns true:</p>

<pre><code>    (now is the time) = 1s (now   is  the   time)
</code></pre>

<p>The qualifying tags 'nc', 'nc', and '1s' when used in the Match 'm' statement <br />
only affect the left operand. They are not applied to the list of string <br />
elements in the right operand. However, the qualifying operations are <br />
applied to $Variables in right operand as well as the left operand:</p>

<pre><code>    ($one) m nc ns ($two $three $four)
</code></pre>

<hr />

<p>n % operator   </p>

<hr />

<p>Measures the similarity of two strings. Two identical strings measure 100%. Two <br />
strings that are closely similar have a higher score than two completely dissimilar <br />
strings which measure 0%.  The two strings “philadelphia” and “philedelphia” are <br />
not identical but are similar in that both contain “phil” and “delphia”. There is <br />
a 11 to 12 letter match, yielding a value of 91%. The following logic component <br />
would return a true outcome because 91% match exceeds 80%:  </p>

<pre><code>    (philadelphia) 80% (philedelphia)
</code></pre>

<p>In an application, use of the N % operator allows partial credit  for a typing <br />
or misspelling mistake.   </p>

<hr />

<p>m operator   </p>

<hr />

<p>The m operator compares a Value or $Variable to one or more values (i.e., list <br />
of values and/or $Variables).  For example, the following returns true because <br />
'west' is a member of the list of values.  </p>

<pre><code>    (west) m (north east west south)
</code></pre>

<p>The '!m' operator returns false when a match occurs.</p>

<p>The list of Values and/or $Variables is contained in the right operand.  </p>
