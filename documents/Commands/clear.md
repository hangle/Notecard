<h1>Clear Command</h1>

<p>The Clear command, with the tag 'c',  clears the Notecard window. It is the first 
command of the Card Set. It removes the text and input fields of the prior Card Set.
The Clear command and the commands following this command up to, but not including, 
the next Clear command constitutes a Card Set.  The following example are the 
commands in the file 'myProgram.nc':</p>

<pre><code>    c
    d card set one
    c
    d card set two
    c
    d card set three
    d with an extra display
    c
    d card set four
    * end
</code></pre>

<p>The file has four Card Sets. Following the execution of the first Card Set, displaying 
'card set one', the Clear command of the second Card set clears the Notecard window of 
'card set one'.  The Clear command has an optional Logic component (see logic 
component). The logic outcome of the component determines whether or not the Card Set 
is executed or is skipped. In following  four Card Sets, the Clear commands have 
logic components.</p>

<pre><code>    c (1) = (2)
    d card set one
    c (2) = (2)
    d card set two
    c (3) = (1)
    d card set three
    c (4) = (4)
    d card set four
</code></pre>

<p>The logic components of Card Set one and Card Set three will fail and these two Sets
are skipped. The logic of Card Sets  two and four will succeed and the two Sets are 
executed.</p>
