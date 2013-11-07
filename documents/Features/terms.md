<h1>Notecard Terms</h1>

<h2>Script Command</h2>

<pre>
        A Notecard instruction to display text or to capture a user's
        input.  There are eight command types. (see document/commands/)
</pre>

<h2>Script File</h2>

<pre>
        File containing Script Commands. The file's extension is '.nc'.
</pre>

<h2>Script Program</h2>

<pre>
        The program that validates and edits the commands of a Script File.
        A syntax error in a command terminates the program and prints the
        command line causing the error and a brief explanation of the error. 
        No output is created.  The Script Program,s output is a file whose 
        name is the same as that of the Script File but whose extension is 
        '.struct'.  The '.struct' is the input of the Notecard program. 
</pre>

<h2>Command Tag</h2>

<pre>
        One or two letters at the start of a Script Command that identifies
        the command.  For example, the tag 'd' denotes a Display command. 
</pre>

<h2>CardSet</h2>

<pre>
        Group of Script Commands beginning with the 'c' command and ending
        when the next 'c' command is encountered or when the end of the
        Script file is reached. The group consists of the following commands:
            Assignment  'a'
            Asterisk    '*'
            Clear       'c'
            Display     'd'
            Edit        'e'
            eXecute     'x'
            Group       'g' <or> 'ge'
        The CardSet displays a Notecard window. 
        The following Script File presents two Notecard windows:
            c
            d first window
            c
            d second window
</pre>

<h2>Card Session</h2>

<pre>
        A Card or Notecard Session is the execution of one or more Script Files 
        that is eventually terminated by the Asterisk command:  '* end'.
</pre>

<h2>Dollar Variable</h2>

<pre>
        A user's response to an input field is stored in a Dollar Variable whose
        name begins with the symbol '$'.  The Display command, for example,
        'd (# $myVariable)' creates an input field and assigns the entered value
        to '$myVariable'.  The Assign command is also a way to store a value. 
</pre>

<h2>Start File</h2>

<pre>
        A single CardSet, script file allowing the user to enter the name
        of a script file to begin or "start" the session.  The essential 
        commands of the CardSet are:

            c
            d (# $startFile)
            f $startFile

        Entry of 'myfile' in the input field causes the file command 'f $startFile'
        to execute 'myfile.struct'.   In the event 'myfile.struct' does not exist,
        then the 'start file' script repeates  (indicating that the file cannot be 
        found).

        The 'start file' commands are internal Notecard codes.
</pre>

<h2>Management File</h2>

<pre>
        A script file that is initiated, during a Card Session, by the '*' Button. 
        This file is termed the Management File and can be linked to other script
        files via the 'f' command. If an '* end' commands is encounter, the Card
        Session is terminated.  On the otherhand, a second activation of the
        '*' Button returns the session to the CardSet from which the '*' Button
        was first activated. 

        The Management File filename is established by the '* manage <filename>'
        command. In the event the session Files lack this command, activation of
        the '*' Button, invokes the Start File. The '*' Button is deactivated by
        the Asterisk Command: '* nomanage'.

        In an interview or survey session, the Management File feature is useful to 
        log the reason that the session was terminated before completion. 
</pre>

<h2>Appearance Features</h2>

<pre>
        The size, color, style, and font of text can be altered. The '*' and 
        'PRIOR' buttons can activated or deactivated. The length of the 
        response field as well as the number of allowable characters can be 
        specified.  See 'appearance.md'. 
</pre>  
