<h1>Assign Command</h1>

<p>The Assign command has an 'a' tag, followed by one or more spaces and by a <br />
$<variable> having a trailing equal '=' sign. The $<variable> is always the <br />
left-hand expression.  The expression to the right of the '=' sign is either:    </p>

<pre><code>    value  
    $&lt;variable&gt;  
    math expression of Values and $&lt;Variables&gt;
</code></pre>

<p>For example:  </p>

<pre><code>    a $one = 22  
    a $two = $three  
    a $three = 2.123 * 5/ ( 2.1 - $fraction)
</code></pre>

<p>In the math expression, numeric character strings are converted to decimals. <br />
The Assign Command has an optional Logic component (see Logic component). <br />
This component has a restriction that is not present for this component in <br />
the Clear, File, and Edit commands. In the case of the Assign command, the <br />
Logic expression must be preceded by an 'if' tag, for example:  </p>

<pre><code>    a $one = 22 if ($one) &gt; (22)  
    a $two = $three if ($four) &lt;= ($five)  
    a $three= 2.123 * 5/ ( 2.1 - $fraction) if ($three) &lt;&gt; (5)
</code></pre>
