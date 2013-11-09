<h1>Appearance Features </h1>

<p>Appearance features cover the size, style, color, and font style of text.  It also <br />
includes the size and length of the input field, as well as the appearance features <br />
of input characters.  The number of input characters can be limited.  The window height <br />
and window width are Appearance features.  Activation and deactivation of the '*' and <br />
'PRIOR' buttons are included as Appearance features.   </p>

<p>The Appearance features are established by the default values of the program as <br />
listed below:  </p>

<pre>
         key           value

        height      300         //window size argument
        width       400         //window size argument
        name        TimesRoman  // name of Font
        size        14          // pixel size of lettering
        color       black       // color of lettering
        style       1           // 1=normal, 2=bold, 3=italics
        length      10          // input field length
        limit       99          // limits the number of input characters 
        asteriskButton  on      //  "on" allows '* button' to be armed (active)
        priorButton     on      //  "on" allow 'PRIOR button' to be armed (active)
</pre>

<p>The script command file:</p>

<pre>
        c 
        d now is the time for all good men
        * end
</pre>

<p>The text of the 'd' command  is displayed in a window dimensioned as 300 x 400.  The <br />
text color is black.  Its size is 14, and its font style is TimesRoman.  </p>

<h2>Ways to override default value.</h2>

<p>Asterisk Commands.   The '*' command  with an appearance key/value pair,  supercedes the <br />
corresponding program default value.  </p>

<p>The script command file:</p>

<pre>
        * width 500
        * height 600
        * size 22
        c 
        d now is the time for all good men
        * end
</pre>

<p>The window size changes to 500 x 600, and the text size becomes '22'.  The  text color <br />
is still black and the font size is still TimesRoman.  However, the introduction of the <br />
following '*' commands can change this.  </p>

<pre>
        * color  green
        * name   Arial
</pre>

<p>The '*' commands of the script file remains in effect until the next script file is executed.   </p>

<h3>The appearance.ini file.</h3>

<p>The  'appearance.ini' file,  consisting of key/value pairs. These key/value pairs  change the <br />
default values for all script files in a session. The following is an example of an <br />
'appearance.ini' file.     </p>

<pre>
            appearance.ini
        ____________________________
        |    height 320
        |    width  260
        |    size     16
        |    color   green
</pre>

<p>The file changes four default, appearance values.  These values become new default values <br />
overriding the program values.  They remain  in effect for the session's script files <br />
( the '* end' command terminates a session).  </p>

<p>The directory in which the Script program is executed is termed the 'home' directory. <br />
The 'appearance.ini' file in the 'home' directory is applied to the script files in 'home' <br />
directory and to all  script files in its subdirectories.  </p>

<p>An 'appearance.ini' directory within a 'home' subdirectory is applied to just the script <br />
files in that directory.   </p>

<p>In the event that 'appearance.ini' files are in both the 'home' directory and in a 'home' <br />
subdirectory, then the subdirectory 'ini' file supercedes the 'home' 'ini'  file, but <br />
only for script files in the subdirectory.  </p>

<p>The Appearance feature of an '*' command overrides a corresponding feature of the <br />
'appearance.ini' file.   For example, the following 'appearance.ini' file in the 'home' <br />
directory is:  </p>

<pre>
            appearance.ini
        __________________
        |  size  12
</pre>

<p>The initial default value of 14 is becomes 12 for all script files in the 'home' directory <br />
and its subdirectories.   </p>

<p>Next, an 'appeaance.ini' file in a subdirectory is:</p>

<pre>
            appearance.ini
        ____________________
        |  size 10
</pre>

<p>The previous defaults letter size of 14 and 12 become 10 for just the script files in the <br />
subdirectory.  In the 'home' directory and its other subdirectories,  the default value <br />
is 12.  </p>

<p>Finally the following '*' command is added to a script file in the subdirectory whose <br />
'appearance.ini' file specifies a size is 10 :  </p>

<pre>
        * size 22
</pre>

<p>The default letter size is 22 for text in the script file having the '*' size 22 command. <br />
The other script files have a default letter size of 10.  </p>

<h2>Two Types of '*' commands.</h2>

<p>The '<em>'  Appearance commands, such as, '</em> size 22',  are not executed by the Notecard <br />
program.  The Notecard program executes the following '*' commands:  </p>

<pre>
        * end
        * continue
        * save
        * manage <filename>
</pre>

<p>The  '*'  Appearance commands are consumed in the Script program and are removed. <br />
The 'appearance.ini' file within the directory structure of the Notecard program <br />
has no affect on the default appearance values.   </p>
