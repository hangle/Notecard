<h1>Appearance Features </h1>

<p>Appearance features cover the size, style, color, and font style <br />
of text.  It also includes the size and length of the input field, <br />
as well as the appearance features of input characters.  The <br />
number of input characters can be limited.  The window height <br />
and window width are Appearance features.  Activation and <br />
deactivation of the '*' and 'PRIOR' buttons are included <br />
as Appearance features.   </p>

<p>The Appearance features are established by the default values <br />
of the program as listed below:  </p>

<pre>
     key           value

    height      300     //window size argument
    width       400     //window size argument
    name        TimesRoman  // name of Font
    size        14      // pixel size of lettering
    color       black       // color of lettering
    style       1       // 1=normal, 2=bold, 3=italics
    length      10          // input field length
    limit       99      // limits the number of input characters 
    asteriskButton  on      //  "on" allows '* button' to be armed (active)
    priorButton     on      //  "on" allow 'PRIOR button' to be armed (active)
</pre>

<p>The script command file:</p>

<pre>
        c 
        d now is the time for all good men
        * end
</pre>

<p>The text of the 'd' command  is displayed in a window <br />
dimensioned as 300 x 400.  The   text color is black.  Its size <br />
is 14, and its font style is TimesRoman.  </p>

<h3>Ways to override default value.</h3>

<p>Asterisk Commands.   The '*' command  with an appearance <br />
key/value pair,  supercedes the   corresponding program <br />
default value.  </p>

<p>The script command file:</p>

<pre>
        * width 500
        * height 600
        * size 22
        c 
        d now is the time for all good men
        * end
</pre>

<p>The window size changes to 500 x 600, and the text size becomes <br />
'22'.  The  text color  is still black and the font size is still <br />
TimesRoman.  However, the introduction of the following '*' <br />
commands can change this.  </p>

<pre>
        * color  green
        * name   Arial
</pre>

<p>The '*' commands of the script file remains in effect until <br />
the next script file is executed.   </p>

<h3>The appearance.ini file.</h3>

<p>The 'appearance.ini' file provides values to the script program. <br />
It does not directly involve the Notecard program. The script <br />
program uses the 'appearance.ini' to create the '.struct' files <br />
that are employed by the Notecard program.    </p>

<p>The  'appearance.ini' file,  consisting of key/value pairs. <br />
These key/value pairs  change the default values for all <br />
script files in a session. The following is an example of an <br />
'appearance.ini' file.     </p>

<pre>
            appearance.ini
        ____________________________
        |    height 320
        |    width  260
        |    size     16
        |    color   green
</pre>

<p>The file changes four default, appearance values.  These <br />
values become new default values   overriding the program <br />
values.  They remain  in effect for the session's script <br />
files ( the '* end' command terminates a session).  </p>

<p>The directory in which the Notecard program is executed is <br />
termed the 'home' directory.  The 'appearance.ini' file in <br />
the 'home' directory is applied to the script files in 'home' <br />
directory and to all  script files in its subdirectories. <br />
An 'appearance.ini' directory within a 'home' subdirectory <br />
is applied to just the script files in that directory.   </p>

<p>In the event that 'appearance.ini' files are in both the <br />
'home' directory and in a 'home' subdirectory, then the <br />
subdirectory 'ini' file supercedes the 'home' 'ini'  file, <br />
but only for script files in the subdirectory.  </p>

<p>The Appearance feature of an '*' command overrides a <br />
corresponding feature of the 'appearance.ini' file. <br />
For example, the following 'appearance.ini' file in the <br />
'home' directory is:  </p>

<pre>
            appearance.ini
        __________________
        |  size  12
</pre>

<p>The initial default value of 14 is becomes 12 for all <br />
script files in the 'home' directory and its 
subdirectories.   </p>

<p>Next, an 'appeaance.ini' file in a subdirectory is:</p>

<pre>
            appearance.ini
        ____________________
        |  size 10
</pre>

<p>The previous defaults letter size of 14 and 12 become 10 for <br />
just the script files in the   subdirectory.  In the 'home' <br />
directory and its other subdirectories,  the default value <br />
is 12.  </p>

<p>Finally the following '*' command is added to a script file <br />
in the subdirectory whose 'appearance.ini' file specifies a <br />
size is 10 :  </p>

<pre>
        * size 22
</pre>

<p>The default letter size is 22 for text in the script file <br />
having the '*' size 22 command.  The other script files <br />
have a default letter size of 10.  </p>

<h3>Two Types of '*' commands.</h3>

<p>The '<em>'  Appearance commands, such as, '</em> size 22',  are <br />
not executed by the Notecard program.  The Notecard program <br />
executes the following '*' commands:  </p>

<pre>
        * end
        * continue
        * save
        * manage <filename>
</pre>

<p>The  '*'  Appearance commands are consumed in the Script <br />
program and are removed.  </p>
