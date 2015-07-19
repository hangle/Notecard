<h1>AddCardSet</h1>

<p>The following script creates a CardSet and an AddCardSet.   </p>

<pre><code>    c
    d What is the capital of Ohio (# $columbus)

    +
    d Discover of the New World.

    * end
</code></pre>

<p>The CardSet, beginning with the command 'c', asks a question requiring <br />
a user response. The AddCardSet, beginning with the command '+', <br />
provides a probe or prompt, that is, a hint, to the user.  If it is <br />
not needed, then it is esaily skipped by activating the 'Next' button.      </p>

<p>The question-CardSet arms the '+Add' button. Activation of the +Add <br />
button presents the AddCardSet along  with its hint. In the <br />
AddCardSet, the activation of the +Add button returns the user to the <br />
question-CardSet to complete the response.       </p>

<p>On the otherhand, the AddCardSet can be avoided. The user can answer <br />
the question, followed by the 'Next' button, to skip the AddCardSet.   </p>

<p>The AddCardSet is always associated with the immediately preceding <br />
CardSet.  A CardSet may have more than one AddCardSet as the <br />
following script illustrates.   </p>

<pre><code>    c
    d What is the capital of Ohio (# $columbus)

    +
    d Discover of the New World.

    +
    d Begins with 'C' and has 8 letters

    * end
</code></pre>

<p>Termination of the last AddCardSet initiates the return or re-display <br />
of the question-CardSet. A single AddCardSet is also the last in the <br />
series. In a sequential series of AddCardSets, any AddCardSet can <br />
initiate the return and re-display with the activation of the 'Next' <br />
button.  </p>

<h2>Notecard Program</h2>

<p>The CardSAet and AddCardSet are identical in almost all respects. The <br />
differ by the 'button' parameter that each has.    </p>

<pre><code>    CardSet        button=0     No associated AddCardSet
    CardSet        button=1     Associated AddCardSet
    AddCardSet     button=2     Not last
    AddCardSet     button=99    Last AddCardSet
</code></pre>

<p>A CardSet whose button parameter is '1' has one or more associated <br />
AddCardSets.   </p>

<h2>Notecard class:   </h2>

<p>CardSet and AddCardSet are children of the Notecard object. The <br />
AddCardSet objects are treated as CardSet objects but retains <br />
AddCardSet indentity by 'button' parameter values of '2' or '99'.      </p>

<pre><code>    def executeOneNotecardChild(...)  
       obj match{
        ...
        case acs:CardSet if(acs.isAddCardSet) =&gt;
            if(buttonSet.selectedButton == "+" || ...) {
                ...
        case cs:CardSet=&gt; 
                ...
            if(cs.hasAddCardSet) {
                buttonSet.armAddButton
            ...
</code></pre>

<p>The first case statement will catch all AddCardSets; however, it <br />
is not executed unless the '+Add' button was activated (selected- <br />
Button=="+"). The second case statement detects that the CardSet <br />
has a dependent AddCardSet (button variable==1) and thus arms the <br />
'+Add' button. The user activating the 'Next' button causes the <br />
AddCardSet to be skipped (selectdButton=="next").  </p>

<p>A CardSet with associated AddCardSet(s) is returned (re-displayed) <br />
once the executed AddCardSet(s) series has ended or at any time <br />
during the execution that the "Next" button is activated.   </p>

<h2>End of AddCardSet Series.  </h2>

<p>The last AddCardSet in a series had the 'button' value of '99'. <br />
Notecard-- doAddButton function invokes 'CardSet.isLastAddCardSet' <br />
to test this condition.  </p>

<pre><code>    def doAddButton( ...
        ...
        addCardSetFlags.activatedAddButton=true
        if(cardSet.isLastAddCardSet){  // 'button==99'
            addCardSetFlags.activatedAddButton=false
            restoreCurrentCardSet  // see Linker trait
            }
        ,,,
</code></pre>

<p>If condition is found, then 'restoreCurrentCardSet' is invoked. <br />
Control passes to the 'while(iterate)' statement:    </p>

<pre><code>    def iterateNotecardChildren(...)
        reset(child)
        while(iterate) {
            ...
            executeOneNotecardChild(...
            ...
</code></pre>

<p>Normally, the 'while' statement executes the next or subsequent <br />
child object.  However, 'restoreCurrentCardSet' has "loaded" the <br />
CardSet associated with the AddCardSet(s) just before control <br />
passes to the 'while' statement, thus affecting a return.   </p>

<pre><code>    def executeOneNotecardChild(...)  
       obj match{
        case bcs:CardSet if(bcs.isAddCardSet) =&gt;
                ...
        case cs:CardSet=&gt; 
                ...
            if(cs.hasAddCardSet) { // 'button'==2&lt;or&gt;99
                storeCurrentCardSet
            ...
</code></pre>

<p>In the above abstract code, the function 'storeCurrentCardSet' <br />
saves the CardSet that has associated AddCardSet(s).    </p>

<h2>AddCardSet Termination with Next Button.  </h2>

<p>The 'Next' button is designed to render the following CardSet.  In <br />
order to re-display  the earlier CardSet with its associated <br />
AddCardSet(s), the program must recognize that the '+Add' button <br />
was activated.  The 'doAddButton' functions establishes this <br />
detection with the Boolean value 'activatedAddButton':  </p>

<pre><code>    def doAddButton( ...
        ...
        addCardSetFlags.activatedAddButton=true
        ...
</code></pre>

<p>In turn, the 'doNextButton(... function employees this value to <br />
re-display the earlier CardSet.   </p>

<pre><code>    def doNextButton(...
        ....
        if(addCardSetFlags.activatedAddButton) {
            addCardSetFlags.activatedAddButton=false
            restoreCurrentCardSet
            }
        ...
</code></pre>

<h2>Wait State</h2>

<p>The Notecard buttons and input fields operate when the system is <br />
put into a wait state by the CardSet object.   </p>

<pre><code>    def haltCommandExecution(lock:AnyRef): Unit=lock.synchronized {
        lock.wait()
</code></pre>

<p>The wait is issued just before CardSet returns to Notecard-- its parent. <br />
CardSet children have executed and text is displayed and input fields <br />
are presented.  A return key following the last response or a button <br />
activation releases the wait state.   </p>

<p>The 'x' command presents a problem. The following script incorporates <br />
the 'x' command:    </p>

<pre><code>    c
    d What is the capital of Ohio (# $columbus)
    x
    g ($columbus)=(Columbus)
    d Correct
    ge
    d Sorry, Columbus is the capital.
</code></pre>

<p>In CardSet, the function: 'executeOneCardSetChild(... processes the 'x' <br />
command:   </p>

<pre><code>    case xn:XNode =&gt;    
        ...
        haltCommandExecution(lock) // issue lock.wait()
        ...
</code></pre>

<p>This stops the execution of:  </p>

<pre><code>    g ($columbus)=(Columbus)
    d Correct
    ge
    d Sorry, Columbus is the capital.
</code></pre>

<p>The wait is not released until the response has been entered, thus not <br />
all CardSet children have been executed. The halt operation allows the <br />
the $columbus variable to hold the user's input so as to be tested by the <br />
Group 'g' command.  The user's entry of the 'Enter' key releases the <br />
wait allowing the remaining script to execute.    </p>

<p>The problem this presents for the following AddCardSet, such as:   </p>

<pre><code>    +
    d Discover of the New World.
</code></pre>

<p>is to prevent the execution of remaining CardSet script when the <br />
'+Add' button is activated (user choses not to enter a response <br />
but to view the hint). First, 'selectedButton=="+" ' indicates that <br />
the user opted for the hint.  Next, an exception is thrown.    </p>

<pre><code>    case xn:XNode =&gt;    
        ...
        if(buttonSet.selectedButton == "+" &amp;&amp; addCardSetFlags.hasDependentAdd ){
            ...
            throw new AddButtonException
            ...
</code></pre>

<p>The thrown exception is caught by Notecard:</p>

<pre><code>        try {
    executeOneNotecardChild(...
        }  catch { case _:AddButtonException =&gt; }
</code></pre>

<p>The next CardSet, which happends to be version of AddCardSet, is <br />
processed to provide the hint of 'Discover of the New World'.  </p>

<h2>Backup Mechanism of AddCardSet(s). </h2>

<p>The Notecard system has two backup mechanisms. One mechanism for CardSet <br />
and one for AddCardSet.  </p>

<p>A CardSet with only one associated AddCardSet does not have a backup. <br />
Nevertheless, the BackupMechanism is instantiated by that CardSet:  </p>

<pre><code>    case cs:CardSet=&gt; 
        ...
        if(cs.hasAddCardSet) {
            whichBackup="addCardSet"
            establishAddBackup(addCardSetFlags)
        ...

    def establishAddBackup(...
        addBackupMechanism= new BackupMechanism("AddCardSet")
        addCardSetFlags.hasDependentAdd=true
        ...
</code></pre>

<p>A new BackupMechanism is instantiated each time a CardSet had one or more <br />
associated AddCardSet series.    </p>

<pre><code>    def doPriorButton( ...
        if(whichBackup=="cardSet") ...
            doPriorButtonBackup(backupMechanism)
          else  
            doPriorButtonBackup(addBackupMechanism)
        ...
</code></pre>
