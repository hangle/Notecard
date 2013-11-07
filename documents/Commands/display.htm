<h1>Display Command</h1>

<p>The Display command renders text within the Notecard window.  It also captures <br />
the user's input or response.  The 'd' command's text occupies one line of the <br />
Notecard window.  The 'd' command is line oriented; it does not wrap.  A space <br />
must follow the 'd' tag if subsequent components are included (see optional <br />
components).  </p>

<p>The Display or 'd' command has six optional components:  </p>

<pre><code>    (1) Column/Row  placement  of a line of text  
    (2) Text appearance arguments covering the line of text  
    (3) Text line  
    (4) Input field  
    (5) Display of $(variables)  
    (6) Text appearance of a subset of the text line
</code></pre>

<p>The simplest 'd' command is:  </p>

<pre><code>    d
</code></pre>

<p>This command has just  the 'd' tag (no trailing space required).  It renders an <br />
empty text line.  </p>

<hr />

<p>Column/Row  Placement component  </p>

<hr />

<p>This component has two optional arguments:  row and column values. <br />
Row value controls the text line placement within the Notecard window. <br />
Column value controls the column where the text line begins.  </p>

<p>A component having both Column and Row requires the column value to begin <br />
without a slash '/' but to end with a slash.  </p>

<p>The row value has both a beginning and ending slashes:  </p>

<pre><code>    d 20/5/this text is displayed on the 5th row, beginning in column 20
</code></pre>

<p>A component having just the column value.  </p>

<pre><code>    d 15/this text begins in column 15
</code></pre>

<p>A component with a column value establishes the column position of subsequent <br />
'd' commands or until a 'd' commands establishes a new column position:  </p>

<pre><code>    d 20/begins in column 20  
    d begins in column 20, next row  
    d begins in column 20, next row   
    d 15/begins in column 15, next row  
    d begins in column 15, next row
</code></pre>

<p>A component having just the row value:  </p>

<pre><code>    d /14/this text is on row 14, beginning in column 1.
</code></pre>

<p>A display command without the row value  has its row value automatically <br />
incremented by the system.   </p>

<pre><code>    c  
    d this line begins on row 1  
    d this line begins on row 2  
    d this line begins on row 3
</code></pre>

<hr />

<p>Text appearance arguments covering the line of text  </p>

<hr />

<p>The Appearance  component consist of  a "type" "value" pair where <br />
the pair is enclosed in slashes.  The component may consist of multiple <br />
pairs.   </p>

<pre>
              Type                           Value

             name             font name   e.g., TimeRoman
             size             size of lettering,   e.g., 14
             color            color of lettering   e.g., black 
             style            normal, bold, or italic
</pre>

<p>Component with one type/value pair:  </p>

<pre><code>    d /color blue/all letters are colored blue  
    d /size 16/all letter have the same size  
    d /style bold/all letter are in bold type
</code></pre>

<p>Component with multiple type/value pairs:  </p>

<pre><code>    d /color blue/size  16/style bold/ a line with color, size, and style
</code></pre>

<p>The appearance features of:  </p>

<pre><code>    name  
    size  
    color  
    style
</code></pre>

<p>can be applied to one or more words of the text line.  </p>

<p>In the script example, the appearance pair (color, blue) is applied <br />
to (for all).  </p>

<pre><code>    d now is the (%% /color blue/ for all) good men
</code></pre>

<p>Again (color, blue) is enclosed in slashes, i.e.,  /color blue/. <br />
The appearance subset is enclosed in parentheses and the open parenthesis is <br />
followed by the tag '%%'.  </p>

<p>Multiple appearance pairs can be applied to a subset of line words. <br />
Multiple subsets of line words can be incorporated, for example:  </p>

<pre><code>    d (%%/color blue/now is) the (%%/style bold/time for) all good men.
</code></pre>

<hr />

<p>Display of $(variables)  </p>

<hr />

<p>Variables that have been captured or assigned by the Assigned 'a' command can <br />
be displayed on the text line.  Suppose in one Card set that the value 'male' <br />
was assigned to $gender via the command:  </p>

<pre><code>    a $gender=male
</code></pre>

<p>Another Card set can display  'male' with:  </p>

<pre><code>d The client's gender is (% $gender)
</code></pre>

<p>The component is enclosed in parentheses and the open parenthesis is followed <br />
by '%'.  </p>

<p>The appearance features of:  </p>

<pre><code>    name  
    size  
    color  
    style
</code></pre>

<p>can be applied to the displayed $(variable), for example:  </p>

<pre><code>    d The client's gender is (% /color blue/size 20/ $gender)
</code></pre>

<hr />

<p>Input Field component  </p>

<hr />

<p>In the Display command, enclosing a $(variable) expression in parentheses <br />
where the open parenthesis is followed by the '#' symbol, for example:  </p>

<pre><code>    d Enter your gender (# $gender)
</code></pre>

<p>causes an input field to be rendered on the text line. Following the execution <br />
of the Card set, control is passed to the input mechanism. Focus is given to <br />
the input field, and the user types a response. The 'enter' key causes the <br />
response value to be stored in $gender.  A display command may have up to 32 <br />
input fields. The following has two input fields:  </p>

<pre><code>    d Enter your gender (# $gender) and age (# $age)
</code></pre>

<p>The appearance features of:  </p>

<pre><code>    name  
    size  
    color  
    style
</code></pre>

<p>can be applied to one or more words of the text line.  </p>

<pre><code>    d Enter your gender (# /color red/ $gender)
</code></pre>

<p>The color red is applied to the letters the user types.  The Input field component <br />
has two additional type-value pairs. These are:  </p>

<pre>
            length              size of the input field
            limit               number of input character allowed.
</pre>

<p>All appearance features have default values. The name/value components overrides <br />
these default values. In the case of the limit type-value pair, a limit value of <br />
1 forces the processing of the input mechanism after the user' s first letter is <br />
entered. For example:  </p>

<pre><code>    d Enter 'y' or 'n' (# /limit 1/length 1/ $yesNoAnswer)
</code></pre>

<p>The problem with the above 'd' command is that an entry of letters other than <br />
'y' and 'n' would be captured. The Edit command would resolve this problem. <br />
The following commands display a field length of 1 and limits the user input to <br />
a single letter. It also allows the single letter to be either a 'y' or 'n'.  </p>

<pre><code>    d (#yn $male) Are you a male  
    d (#yn $over60) Are you over 60
</code></pre>

<p>The Input Field component's open parenthesis '(' is followed by the tag '#yn'. <br />
A specialized Input Field component is available to handle multiple choice <br />
answers where the choices are numbered from 1 to n. In this case, the Input <br />
Field component's open parenthesis '(' , it is followed by #n, where n is the <br />
number of choices. The input mechanism rejects any answer that is not in the <br />
range of 1 to n. For example:  </p>

<pre><code>    d Indicate the direction (#4 $directon)  
    d 20/1. North  
    d 2. East  
    d 3. West  
    d 4. South
</code></pre>
