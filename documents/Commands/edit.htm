<h1>Edit Command</h1>

<p>The Edit command, with the tag 'e' (follow by a space) and a logic component, 
causes a captured response to be evaluated prior to being stored. When the 
evaluation fails, then the input is not retained. Instead, the response field 
is cleared and the user is given the opportunity to enter a valid response. <br />
The Edit command is associated with a Display command having a input field 
component. For example:</p>

<pre><code>    d Enter your gender (# $gender)
    e ($gender)=(male) or ($gender)=(female)
</code></pre>

<p>The Edit command acts upon the Display's (# $gender) component. The user must 
enter 'male' or 'female' in order to proceed.  The problem with the above script 
is that the user may not know the proper response. The Edit command, however, <br />
has an optional feature that presents a message to the user when the Edit 
condition fails.  The message has the tag 'status=' followed by the message:</p>

<pre><code>    e ($gender)=(male) or ($gender)=(female) status=enter male or female
</code></pre>

<p>The 'status=' message is displayed on the line above the button set. </p>

<p>The are two specialized Edit Commands that have tags in place of the logic 
components. These tags are:</p>

<pre><code>    letter
    number
</code></pre>

<p>The 'e letter' command requires all input characters to be letters of a-z or 
A-Z.  The 'e number' command requires all input characters to be numbers of 0-9.
The specialized Edit commands have builtin messages of:</p>

<pre><code>    'number is required'
    'letters are required'
</code></pre>

<p>More than one Edit command may be associated with the Display's input field. All 
Edit commands must must pass evaluation before the response is stored. For 
example:</p>

<pre><code>    d Enter age (# $age)
    e number
    e ($age) &gt; 0 and ($age) &lt; (100) status=99 if age &gt; 99
</code></pre>

<p>A Display command with multiple input field components poses a problem, for 
example:</p>

<pre><code>    d Enter name (# $name) age (# $age) and gender (# $gender)
</code></pre>

<p>In a Display command with a single input field component, the association of input 
field component and the following Edit commands is obvious. With multiple input 
field components, a different syntax is required. The Edit command requires an 
identifier establishing the association with the particular input field component. 
The identifier is the $<variable> of the input field component.  The $(variable) 
identifier follows the 'e' tag and space. For example:</p>

<pre><code>    d Enter name (# $name) age (# $age) and gender (# $gender)
    e $name letter
    e $age number
    e $age ($age) &gt;(0) and ($age) &lt; (100) status=99 if age &gt; 99
    e $gender ($gender)=(male) or ($gender)=(female) status= male or female
</code></pre>
