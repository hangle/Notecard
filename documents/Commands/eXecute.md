<h1>eXecute Command</h1>

<p>The eXecute command, with the tag of 'x' (no trailing space required), halts <br />
the execution of the Card Set and passes control to the input mechanism. When <br />
the input capture is complete, then control returns to execute the remaining <br />
Card Set.  </p>

<p>The function of the eXecute command is to capture a response and display it <br />
within the context of the same Card Set, for example:  </p>

<pre><code>    c  
    d Enter your age (# $age)  
    x  
    d Your age is (% $age)  
    c ...
</code></pre>

<p>In the above script, the input field is shown. The command 'd Your age is <br />
(% $age)' is not shown because execution of the Card Set has been halted. Had <br />
the 'x' command been missing, then the 'd' command would have executed and $age <br />
would have printed 'Unkwn($age)'.  </p>

<p>Suppose the user entered '22' for their age. Now, control returns to execute <br />
the 'd' command and the $age variables causes '22' to be printed.  A more <br />
practical application shows the value of the eXecute command:  </p>

<pre><code>    c  
    d What is the capital of Ohio (# $capital)  
    x  
    g ($capital)  =  (Columbus) 
    d Correct   
    ge  
    d Wrong.  Columbus is the capital  
    c  ...
</code></pre>
