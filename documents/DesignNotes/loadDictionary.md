<h1>Load Dictionary (Notecard)</h1>

<h3>Script File</h3>

<p>The Load 'l' command, along with associated Assign 'a' commands, <br />
initializes '$variables'. That is, the associated 'a' commands <br />
are only executed once.  The 'l' command and   associated 'a' <br />
commands are termed the  Load command set.  </p>

<pre><code>    l 
    a $one=1
    a $one=2
</code></pre>

<p>The script can be made to loop or repeat when and '* end' <br />
command is missing or a file  command is not executed. </p>

<pre><code>    l
    a $one=0
    c
    d (% $one)      // prints the value of $one
    a $one = $one + 1
</code></pre>

<p>The above script, without '* end',  loops  and increment the <br />
value of $one each time that the CardSet is encountered:   </p>

<pre><code>    c
    d (% one)       // prints 0,1,2,3,...
    a $one=$one+1
</code></pre>

<p>A practical application of the Load command set is to require <br />
questions to be correctly answered before proceeding with the <br />
exercise.  </p>

<pre><code>    l
    a $capital1=false
    a $capital2=false
    c
    d Capital of Ohio (# $columbus)
    g ($columbus)=(Columbus)
    a $capital1=true
    c
    d Capital of New York (# $albany)
    g ($albany)=(Albany)
    a $capital2=true
    f nextExercise ($capital1)=(true) and ($capital2)=(true)
</code></pre>

<p>Correct answers set $capital1 and $capital2 to 'true', <br />
allowing the File  'f' command to execute, thus terminating the
looping action. </p>

<pre><code>    c ($capital1) = (false)
    d Capital of Ohio (# $columbus)
    g ($columbus)=(Columbus)
    a $capital1=true
    d Correct
    ge
    d Sorry, Columbus is the capital
</code></pre>

<h3>Linked-List Class Structure</h3>

<p>The following is the linked list structure of the command classes. <br />
Notecard is the root of the structure.  As a parent, it has 4 <br />
children (LoadDictionary, NotecardType, NextFile, and CardSet). <br />
LoadDictionary and CardSet are also parents  </p>

<p>The classes to the right of 'Notecard' are children and <br />
grandchildren of 'Notecard'.  The classes to the right of <br />
'CardSet' are children and grandchildren of this class. <br />
The Load command (classname: LoadDictionary) and its <br />
associated Assign commands  add the following substructure:  </p>

<pre>
        Notecard
            LoadDictionary
                AssignerNode (LoadAssign)
            NotecardType
            NextFile
            CardSet
                AssignerNode
                CardSetType
                GroupNode
                XNode
                RowerNode
                    DisplayVariable
                    DisplayText
                    BoxField
                        EditNode
</pre>

<p>The Assign command (clasname: AssignerNode) has different <br />
parent classes   (LoadDictionary and CardSet).   The <br />
AssignerNode, whose parent is CardSet, executes everytime <br />
it is accessed; the AssignerNode, whose parant is <br />
LoadDictionary, exectues just once.  </p>

<p>The LoadDictionary is envoked on each loop or iteration, however, <br />
its Assigner children are only processed during the first loop.  </p>

<pre><code>    def startLoadDictionary  {
            // retrieve key (filename)/value from symbol
            // table. Initially, get returns None
        symbolTable.get(filename) match {
                // Do nothing when get yields the paramter
                // filename from the Symbol table
            case Some(value) =&gt;
                // Initial value
            case None =&gt;
                symbolTable += filename-&gt; "0"
                    //Only executed one time
                iterateAssignerObjects
            }
        }
</code></pre>

<p>It is processed on the first loop because 'startLoadDictionary' <br />
fails, in a match statement, to find its 'filename' parameter in <br />
the SymbolTable. As such, it executes the LoadDictionary children <br />
(Assigner objects). It also loads 'filename' to the SymbolTable so <br />
that on subsequent invocations, its children objects are not <br />
processed.  </p>

<h3>Renaming the AssignNode class</h3>

<p>The AssignNode class whose parent is LoadDictionary has <br />
had its '%&lt; classname >   renamed from that of '%AssignerNode' <br />
to that of '%LoadAssign'.  The name change was   done by <br />
the Script program.  'CreateClass' instantiates the <br />
'AssignerNode' instance when either '%AssignerNode' or <br />
'%LoadAssign' is encounter in 'create_object()'.  The <br />
function 'CreateClass.swizzleReferences()' converts <br />
the symbolic addresses to physical ones. The Script program <br />
made sure that the AssignerNode instances were  attached <br />
to their proper parent instance. </p>
