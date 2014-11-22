<h1>Management System</h1>

<p>The Management System allows the user to switch from one  script <br />
command file (*.nc) to   another script file by activating a button. <br />
The switch occurs from any CardSet of the   first file to the <br />
beginning CardSet of the second file.  From any CardSet of the second <br />
file, activation of the button returns the display of the CardSet <br />
within the first file  from which the button was initially activated.  </p>

<p>Switching to a 'management' file was useful within a survey/interview <br />
context.  When an   interviewee is unable to complete the survey, <br />
then an interviewer, following button activation, can record in <br />
the 'management' file the reasons for interruption.  In addition, <br />
the 'management' file records the stopping point of the interview <br />
so that the interview can   be restarted at a later time (current <br />
system lacks the restart mechanism).</p>

<p>The button with the symbol '<em>'  controls the file switching. The <br />
'</em>' button is one of   three buttons that reside at the bottom of <br />
the Notecard window.  When the '<em>' button is   activated, then the <br />
user is switched to the start of the 'management' file.  Within the <br />
'management' file, the activation of the '</em>' button returns the <br />
user to the initial file   and to the CardSet from which the '*' <br />
button was initiated.  </p>

<p>The designated <filenameA>  serves as an example of the initial <br />
file;  and the <management>   file as the switch-to target file. <br />
Both <filenameA>  and <management> are converted to    Linked <br />
List structures by CommandNetwork.loadFileAndBuildNetwork(). <br />
The function returns the root of the Linked List structure and <br />
is invoked in two places:  (card and NotecardTask).  The two <br />
roots produces by <filenameA> and <management> files are <br />
assigned different names   ('initialNotecard:Notecard' and <br />
'manageNotecard:Notecard')</p>

<h2>notecard:Notecard root</h2>

<p>An essential task of the Notecard class is to iterate its linked <br />
list of children.   The   following function processes three children <br />
types (CardSet, NotecardTask, NextFile) of   the Notecard parent:</p>

<pre>
    def executeNotecardChildren(...)
         child match    {
            case cs:CardSet=>
                cs.startCardSet(...)
                waitOverDoButtons(..)
            case ft:NotecardTask=> ...  
                ft.startNotecardTask(..)// processes'* manage <filename>' cmd   
                manageNotecard= ...
            case nf:NextFile=>        ...
            }
</pre>

<p>The function 'startCardSet' executes the children of the CardSet class. <br />
CardSet's  children   list terminates invoking a 'wait()' state.  This <br />
wait passes control  to the three buttons ('*', PRIOR, NEXT). <br />
Activation of a button releases the 'wait()' state.   The function <br />
'waitOverDoButton()'  determines which button was activated and <br />
takes appropriate action.  In the case of the '*' button,  the <br />
function 'doAsteriskButton(..) is called.  </p>

<p>The ft.startNotecardTask(..)  processes the Asterisk commands (* end, <br />
* manage <filename>,   * save <filename>).   The Asterisk command, <br />
such as,  '* manage  myManageFile'  invokes: </p>

<pre>
    val manageNotecard=
        CommandNetwork.loadFileAndBuildNetwork(“myManageFile” ,symbolTable) 
</pre>

<p>The variable 'manageNotecard' is the root of the linked list <br />
structure that displays   'myManageFile'.  'manageNote' is returned <br />
from NotecardTask to Notecard (the above does   not show how it <br />
is returned).  Note that this is taking place in Notecard while <br />
'initialNotecard' structure  is displaying <filenameA>.  </p>

<p>To switch from 'initialNotecard' structure to the 'manageNotecard' <br />
structure is a simple   matter of Notecard recursively calling <br />
itself:  </p>

<pre>
        manageNotecard.startNotecard(...)
</pre>

<p>This call to 'startNotecard(..)' is done from within 'doAsteriskButton(..)' .     </p>

<p>The complicating matter is that, while the 'manageNotecard' structure <br />
is running, the next     time  the '*' button is activated, then <br />
it causes the 'initialNotecard' structure to resume  runnning.  To <br />
handle this complication, two mutually exclusive  states are <br />
created in the form of the boolean variable:  </p>

<pre>
        var initialNotecardState
</pre>

<p>When 'initialNotecardState' is true,  then the management structure <br />
is recursively called:  </p>

<pre>
        manageNotecard.startNotecard(..)
</pre>

<p>In addition 'initialNotecardState' becomes false so that the next <br />
time the '*' button is   activated,  the recursive call is not <br />
repeated.   Instead, 'initialNoteCard' becomes true and an exception <br />
is thrown to terminate the 'manageNotecard' structure so as to return <br />
to processing the 'initialManage' structure.  </p>

<p>Within Notecard, the exception thrown by 'doAsteriskButton()' is <br />
caught by the try block in 'startNotecard()'. This causes the <br />
process to return to 'doAsteriskButton()' from which it   was invoked <br />
by 'startNotecard()'.  Before 'manageInitial.startNotecard()' was <br />
called,  the reference node of the CardSet, from which the '*' button <br />
was activated,  is saved.   On the   return to 'doAsteriskButton()', <br />
the reference node of the  CardSet is restored within the <br />
'initialNotecard' structure. </p>

<p>The node saved and then restored causes the CardSet, that initially <br />
activated the '*' button, to repeat.  </p>
