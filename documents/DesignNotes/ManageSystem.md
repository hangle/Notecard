<h1>Management System</h1>

<p>The Management System allows the user to switch from one  script command file (*.nc) to <br />
another script file by activating a button.  The switch occurs from any CardSet of the <br />
first file to the beginning CardSet of the second file.  From any CardSet of the second <br />
file, activation of the button returns the display of the CardSet  within the first file <br />
from which the button was initially activated.</p>

<p>Switching to a 'management' file was useful within a survey/interview context.  When an <br />
interviewee is unable to complete the survey,  then an interviewer, following button <br />
activation,  can record in the 'management' file the reasons for interruption.  In addition, <br />
the 'management' file records the stopping point of the interview so that the interview can <br />
be restarted at a later time (current system lacks the restart mechanism).</p>

<p>The button with the symbol '<em>'  controls the file switching. The '</em>' button is one of <br />
three buttons that reside at the bottom of the Notecard window.  When the '<em>' button is <br />
activated, then the user is switched to the start of the 'management' file.  Within the <br />
'management' file, the activation of the '</em>' button returns the user to the initial file <br />
and to the CardSet from which the '*' button was initiated.</p>

<p>The designated <filenameA>  serves as an example of the initial file;  and the <management> <br />
file as the switch-to target file.  Both <filenameA>  and <management> are converted to <br />
Linked List structures by CommandNetwork.loadFileAndBuildNetwork().   The function returns <br />
the root of the Linked List structure and is invoked in two places:  (card and NotecardTask). <br />
The two roots produces by <filenameA> and <management> files are assigned different names <br />
('initialNotecard:Notecard' and 'manageNotecard:Notecard')</p>

<h2>notecard:Notecard root</h2>

<p>An essential task of the Notecard class is to iterate its linked list of children.   The <br />
following function processes three children types (CardSet, NotecardTask, NextFile) of <br />
the Notecard parent:</p>

<pre>
        def executeNotecardChildren(...)
             child match    {
                case cs:CardSet=>
                    cs.startCardSet(...)
                    waitOverDoButtons(..)
                case ft:NotecardTask=> ...  
                    ft.startNotecardTask(..)    // processes'* manage <filename>' command   
                    manageNotecard= ...
                case nf:NextFile=>        ...
                }
</pre>

<p>The function 'startCardSet' executes the children of the CardSet class.  CardSet's  children <br />
list terminates invoking a 'wait()' state.  This wait passes control  to the three buttons <br />
('*', PRIOR, NEXT).  Activation of a button releases the 'wait()' state.   The function <br />
'waitOverDoButton()'  determines which button was activated and takes appropriate action. <br />
In the case of the '*' button,  the  function 'doAsteriskButton(..) is called.</p>

<p>The ft.startNotecardTask(..)  processes the Asterisk commands (* end, * manage <filename>, <br />
* save <filename>).   The Asterisk command, such as,  '* manage  myManageFile'  invokes:</p>

<pre>
    val manageNotecard=
        CommandNetwork.loadFileAndBuildNetwork(“myManageFile” ,symbolTable) 
</pre>

<p>The variable 'manageNotecard' is the root of the linked list structure that displays <br />
'myManageFile'.  'manageNote' is returned from NotecardTask to Notecard (the above does <br />
not show how it is returned).  Note that this is taking place in Notecard while <br />
'initialNotecard' structure  is displaying <filenameA>.  </p>

<p>To switch from 'initialNotecard' structure to the 'manageNotecard' structure is a simple <br />
matter of Notecard recursively calling itself:  </p>

<pre>
        manageNotecard.startNotecard(...)
</pre>

<p>This call to 'startNotecard(..)' is done from within 'doAsteriskButton(..)' .     </p>

<p>The complicating matter is that, while the 'manageNotecard' structure is running, the next <br />
time  the '*' button is activated, then it causes the 'initialNotecard' structure to resume <br />
runnning.  To handle this complication, two mutually exclusive  states are created in the <br />
form of the boolean variable:</p>

<pre>
        var initialNotecardState
</pre>

<p>When 'initialNotecardState' is true,  then the management structure is recursively called:  </p>

<pre>
        manageNotecard.startNotecard(..)
</pre>

<p>In addition 'initialNotecardState' becomes false so that the next time the '*' button is <br />
activated,  the recursive call is not repeated.   Instead, 'initialNoteCard' becomes true <br />
and an exception is thrown to terminate the 'manageNotecard' structure so as to return to <br />
processing the 'initialManage' structure.  </p>

<p>Within Notecard, the exception thrown by 'doAsteriskButton()' is caught by the try block in <br />
'startNotecard()'.   This causes the process to return to 'doAsteriskButton()' from which it <br />
was invoked by 'startNotecard()'.  Before 'manageInitial.startNotecard()' was called,  the <br />
reference node of the CardSet, from which the '*' button was activated,  is saved.   On the <br />
return to 'doAsteriskButton()',  the reference node of the  CardSet is restored within the <br />
'initialNotecard' structure. </p>

<p>The node saved and then restored causes the CardSet, that initially activated the '*' button, <br />
to repeat.</p>
