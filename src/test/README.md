<hr />

<h2>Test Directory (Script/src/test):  </h2>

<p>The execution of the script in the DEMO directory:  </p>

<pre><code>    scala card demo/card
</code></pre>

<p>reveals features of the Notecard capabilities.  However, it <br />
does not exercise the complete range of capabilities.  The <br />
DEMO directory just highlights the major Notecard features. <br />
For the user, it illustrates the potential of the Notecard <br />
system.  </p>

<p>The TEST directory holds the '.nc' and '.struct' files. The <br />
'.struct' files contain script to exercises features of the <br />
system.  Its purpose is not a tutorial; rather, it is a means <br />
to reveal or find errors and imitations in the script <br />
commands.  The code of the '.struct' files has passed the <br />
edits and verfications of the 'script' program.  However, <br />
the TEST files verify whether or not the commands are <br />
accurately operating. For example, the script:  </p>

<pre><code>    g (1)=(1)
    d Success
    ge
    d Failure
</code></pre>

<p>should print 'Success' and not 'Failure'.</p>

<p>To date, the following TEST script files are available:   </p>

<pre><code>appearance.nc       
    Example:
        d 5/4/color red/Sorry wrong answer
        d now is (%%/color blue/for all good) men
groupCmd.nc
    Example:
        g (flag)=(true)
        ge
inputFields.nc
    Example:
        d Enter (# $name)
        d (# $one)  (# $two)  (# $three)
letterSize.nc       Note, component of appearance
    Example:
        d 5/4/size 20/Sorry wrong answer
        d now is (%%/size 10/for all good) men
loadDictionary.nc
    Example:
        l
        a $count=0
        a $flag=false
addCardSet.nc
    Example:
        c
        d This is a CardSet
        +
        d This is an AddCardSet
editCommand.nc
    Example
        c
        d Enter your age (# $age)
        e number
        e ($age) &gt;(0) and ($age) &lt; (100)    
logic.nc
    &lt;linked to&gt;
  logicAnd.nc
  logicOr.nc

    Example
        d g (1)&gt;=(1)
        d d Success
        d ge
        d d Failure
        d * continue
</code></pre>

<h2>Execution:</h2>

<p>Execution of the sript appearance.nc  must  be compiled in <br />
the Script/src directory to produce a appearance.struct file:    </p>

<pre><code>    scala script test/appearance
</code></pre>

<p>The output file of the 'scala script text/appearance' is <br />
'appearance.struct'.  This file is executed in the Notecard <br />
src directory:</p>

<pre><code>    scala card test/appearance
</code></pre>
