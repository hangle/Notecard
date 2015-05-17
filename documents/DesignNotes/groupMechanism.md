<h1>Group Command Mechanism</h1>

<p>Within the CardSet, the Group 'g' command controls whether or not a group <br />
of commands are executed or skipped. The following script illustrates the <br />
control of the Group command:  </p>

<pre><code>    c
    d not in scope of group command
    g (1)=(1)
    d in scope of group command
    d also in scope of group command
    c  
    ...
</code></pre>

<p>The condition of the Group command '(1)=(1) is true, thus the two Display 'd' <br />
command are processed.  </p>

<pre><code>    d in scope of group command
    d also in scope of group command
</code></pre>

<p>The two Display commands in question are not executed in the following:  </p>

<pre><code>    c
    d not in scope of group command
    g (1)=(2)
    d in scope of group command
    d also in scope of group command
    c  
    ...
</code></pre>

<p>The role of GroupNode is to test the condition of the Group command, <br />
provided that the command has a conditional expression. In the <br />
following script, the second Group command has just the tag 'g'.  </p>

<pre><code>    c
    d not in scope of group command
    g (1)=(2)
    d in scope of group command
    d also in scope of group command
    g
    d not in scope of group command
    c  
    ...
</code></pre>

<p>The scope of 'g (1)=(2)' extends not to the start of the Clear command <br />
but to the second 'g' command.  The second 'd not in scope of ...' is <br />
displayed.   </p>

<p>The 'g' command, without a conditional expression, is a means to <br />
establish scope of the Group command execution.  </p>

<p>The script arguments passed to GroupNode when instantiated are:  </p>

<pre><code>    condition   &lt;expression&gt;    //&lt;expression&gt;  e.g., (1)=(2)
    post        else &lt;or&gt; 0     // else condition cited below.
</code></pre>

<p>The Group command may have the tag 'ge' that denotes an else <br />
condition. For example:</p>

<pre><code>    c
    g ($capital)=(Columbus)
    d Correct
    ge
    d Sorry, Columbus is the capital of Ohio
    c
    ...
</code></pre>

<p>Had $capital contained the value Cleveland, then the second Displays <br />
command, and not the first, would have printed.   </p>

<h3>GroupNode</h3>

<p>In the above script code, the script program translates the 'g ($capital)=(columbus)' <br />
and 'ge' to the following in the '.struct' file:  </p>

<pre><code>    %GroupNode
    address 2023
    sibling 2024
    name        0
    condition   ($capital)=(Columbus)
    post    0
    %%
    %DisplayText 
    ...
    %%
    %GroutNode
    address 2025
    sibling 2026
    name        0
    condition   0
    post    else
    %DisplayText 
    ...
    %%
</code></pre>

<p>The paramter 'name' is not operational.  The first GroupNode 'condition' argument <br />
is  '($capital)=(Columbus)' and its 'post' argument is '0'.  The 'condition' <br />
Targument of the second GroupNode is zero; but its 'post' argument is 'else'.   </p>

<p>In the GroupNode class, the combinition of 'condition' and 'post' produce the <br />
following mutually exclusive types:  </p>

<pre><code>    ThenNode = 1        Has condition expression, e.g., g (1)=(1)
    ElseNode = 2        "else" type but no condition, i.e., ge
    ElseConditionNode=3 "else" type with condition, e.g., ge (1)=(1)
    EmptyNode=4     no "else' type and no condition, i.e., g
</code></pre>

<p>The type value (1,2,3,4) is assigned to 'kind'.</p>

<h3>CardSet</h3>

<p>The 'Notecard' object invokes 'startCardSet(...)' consisting of:  </p>

<pre><code>executeCardSet(...
    iterateCardSetChildren(...`
        executeOneCardSet(...
    }
</code></pre>

<p>The Group command is a child class of CardSet. In the iteration of CardSet objects, <br />
the function 'executeOneCardSet' is invoked to process child object.   </p>

<pre><code>    obj match {
        case rn:RowerNode=&gt; ...
        case as:Assigner=&gt;  ...
        case cst:CardSetTask=&gt; ...
        case xn: XNode =&gt; ...
        case gn:GroupNode=&gt;
            whatToDo( ...
</code></pre>

<p>The Group command is processed by the 'whatToDo(...) function.  This function decides <br />
whether of not to execute the CardSet commands within its scope of control. It also <br />
decides on whether the Group command has completed its task.  </p>

<p>The 'whatToDo(...)' function consist of a match statement with three case statements  </p>

<pre><code>    groupResolve.actionToTake(groupNode) match
        case "do"=&gt; iterateCardSetChildren(...)
        case "skip"=&gt; iterateToNextGroup)(...)
        case "done"=&gt;
</code></pre>

<p>The 'actionToTake(groupNode)' decides whether of not to execute the CardSet commands. <br />
The "do" action invokes 'iterateCardSetChildren(...)'.  This invocation is a recursive <br />
call since it has already been invoked by 'startCardSet(...).  The commands are <br />
executed until the CardSet terminates or until 'executeOneCardSet(...) encounters <br />
a 'case gn:GroupNode=>' to recusrively invokes 'whatToDo(...).  </p>

<p>The "skip" action invokes 'iterateToNextGroup(...)'.  CardSet commands are processed <br />
but not executed, hense they are skipped.  The 'iterateToNextGroup(...) iterates one <br />
CardSet object on each invocation, passing the object to a match statement:  </p>

<pre><code>    case gn: GroupNode=&gt; whatToDo(...)
    case _=&gt; iterateToNextGroup(...)
</code></pre>

<p>'iterateToNextGroup' is recursively called until the CardSet terminates or until <br />
another GroupNode object is encountered.  </p>
