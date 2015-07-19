<h1>Load Command (Script)</h1>

<h2>Script File</h2>

<p>The Load 'l' command, along with associated Assign 'a' commands, initializes <br />
'$variables'. That is, the associated 'a' commands are only executed once. <br />
The 'l' command and   associated 'a' commands are termed the  Load command <br />
set.  </p>

<pre><code>    l 
    a $one=1
    a $one=2
</code></pre>

<p>Assign 'a' commands that immediately follow the Load 'l' command are associated <br />
with the Load command.</p>

<p>The script can be made to loop or repeat when and '* end' command is missing or <br />
a File 'f' command is not executed. </p>

<pre><code>    l
    a $one=0
    c
    d (% $one)      // prints the value of $one
    a $one = $one + 1
</code></pre>

<p>The above script, without '* end',  loops  and increment the value of $one each <br />
time that the CardSet is encountered:   </p>

<pre><code>    c
    d (% one)       // prints 0,1,2,3,...
    a $one=$one+1
</code></pre>

<h2>Two Kinds of Assign Commands</h2>

<p>Above script has two 'a' commands:</p>

<pre><code>    a $one= 0       // executues once
    a $one=$one+1       // executes each iteration
</code></pre>

<p>In the Notecard program, both commands are serviced by the AssignerNode class. <br />
However, the AssignerNode class is found at two different node in the Linked <br />
List structure of command classes:</p>

<pre>
        Notecard
            LoadDictionary
                AssignerNode
            NotecardType
            NextFile
            CardSet
                AssignerNode
                CardSetType
                GroupNode
                RowerNode
                    DisplayVariable
                    DisplayText
                    BoxField
                        EditNode
</pre>

<p>First, it is a child of 'LoadDictionary' and next it is a child of 'CardSet. <br />
The Script program creates this structure, converts the structure's physical <br />
addresses to symbolic ones, and passes the symbolic addresses, via the '.struct' <br />
file, to the Notecard program.</p>

<h2>Load command and Associated Assign Commands</h2>

<p>The 'ParserValidator' object collaborates with the ScanScriptFile object search <br />
the script file for:</p>

<pre><code>    Named Edit commands
    Asterisk Appearance commands
    Load command and associated Assign commands
</code></pre>

<p>The 'LoadScriptCommand' object finds the 'l' command and associated 'a' commands <br />
and transform the 'a' tag to '+', for example:</p>

<pre><code>    before              after
    ------               -----
    l               l
    a $one=0        + $one=0
    c               c
    d (% $one)      d (% $one)
    a $one=$one+1   a $one=$one+1
</code></pre>

<p>The Assign command 'a $one=$one+1' is not in the scope of the Load command so its tag <br />
is unchanged.   </p>

<p>In 'CommandMaker' the command tags  ('d','c','f','g','*', 'a', 'x', l, +) creates <br />
'<classname>Command' classes.  The following code high lights the tags (</p>

<pre><code>    case 'l' =&gt;
        LoadDictionaryCommand.loadDictionaryCommand(...)
    case '+' =&gt;
        Assigner.assignerCommand(..., "+")
    case 'a' =&gt;
        Assigner.assignerCommand(..., "a")
</code></pre>

<p>The 'Assigner' instance gains an argument ("a", "+") to indicate its node in the Linked <br />
List structure.  'Assigner' invokes 'assignerScript' to create the Assign command script; <br />
however, the parameters "a" or "+" produce two kinds of script for the '.struct' file:  </p>

<pre>
        if(kind=="a") 
            script += "%AssignerNode"
        else
            script += "%LoadAssign"
</pre>

<p>'script' is an ArrayBuffer whose output is the '.struct' file. The 'Notecard' program <br />
employees "%AssignerNode" to instantiate 'AssignerNode' to executes the Assign command. <br />
The 'Notecard' program does not instaniate a 'LoadAssign' class; instead, a <br />
'AssignerNode' instance is created.  The instance's symbolic addresses places it its <br />
appropriate node of the Linked List structure.   </p>

<h2>Structure Placement of AssignerNode and LoadAssign</h2>
