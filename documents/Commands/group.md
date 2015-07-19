<h1>Group Command</h1>

<p>The Group command, with the tag 'g' and a logic component, controls whether or <br />
not a group of Card Set commands execute.   The following script shows two <br />
Group commands. One Group command suceeds; the other fails.  </p>

<pre>
        c                     c
        d now                 d now
        d is                  d is
        g (1) = (1)           g (1) = (2)
        d the                 d the
        d time                d time
        c ...                 c  ...        
</pre>

<p>In the case of 'g (1)=(1)', the following commands are executed:  </p>

<pre><code>    d the  
    d time
</code></pre>

<p>In the case of 'g (1)=(2), these commands are skipped.  </p>

<p>The scope of Group command extends to the end of the Card Set, or until another <br />
'g' tag is encountered, for example  </p>

<pre>
        c                     c
        d now                 d now
        d is                  d is
        g (1) = (1)           g (1) = (2)
        d the                 d the  
        g                     g
        d time                d time
        c ...                 c  ...        
</pre>

<p>In the left hand script, all 'd' commands will execute.  In the right hand script, <br />
all 'd' commands will execute with the exception of 'd the'.  The tag 'ge' may <br />
follow a Group command with a logic component. When the logic component fails, <br />
then the group of commands following the 'ge' tag are executed.  </p>

<pre>
        c                     c
        d now                 d now
        d is                  d is
        g (1) = (1)           g (1) = (2)
        d the                 d the  
        ge                    ge
        d time                d time
        c ...                 c  ...       
</pre>

<p>In the left-hand script, the 'd the' command is executed but not the 'd time' <br />
command.  In the right-hand script, the 'd time' command is executed but not <br />
the 'd the' command.  </p>
