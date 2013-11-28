<h1>Load Command</h1>

<p>The Load command has an 'l' tag, followed by none  or one or more spaces.  </p>

<p>The Load command line is followed by one or more Assign commands. Together, <br />
the Load and Assign commands is termed the Load command set,  for example:    </p>

<pre><code>    l
    a $one=1
    a $two= $one +3
</code></pre>

<p>The Load command set is a special case.  It assign values to <br />
$variables the first time the Load command set is executed but <br />
but not on subsequent encounters.   </p>

<p>The execution of a script file repeats when the file does not <br />
terminated with the '* end' command or with the execution of <br />
the File 'f' command.  For example:  </p>

<pre><code>    l
    a $one =0
    c
    d (% $one)
    c
    d last card set
    a $one=$one + 1
</code></pre>

<p>The script file does not end; it loops. The variable $one is <br />
initialized once and is incremented on each loop.   </p>

<p>How is the 'l' command practical? Suppose a script asked two questions <br />
requiring that they be successfully answered before continuing.  </p>

<pre><code>    l
    a capital1=false
    a capital2=false
    c
    d capital of Ohio (# $columbus)
    g ($columbus)=(Columbus)
    a capital1=true
    c
    d capital of New York (# $albany)   
    g ($albany)=(Albany)
    a capital2=true
    f nextFile (capital1)=(true) and (capital2)=(true)
</code></pre>

<p>Both '$capital1' and '$capital2' must be 'true' to escape the loop.  </p>

<p>The 'l' command and its associated 'a' commands are not commands of <br />
the CardSet.  A script syntax error is displayed when a 'l'  tag <br />
is found within the CardSet scope (<em>todo</em>).  </p>

<p>Note, the Assign 'a' command is a member of the CardSet as well <br />
as a member of the Load command set.  In the '*.struct' file, <br />
membership distinction is maintained by renaming '%Assign' to <br />
'%LoadAssign' for 'a' commands associated with the Load command set.   </p>
