<h1>Notecard Program</h1>

<p>The Notecard presents a window about the size of a note card.  The window displays text <br />
to and accepts input from the program user.  </p>

<p>The text and response capture are controlled by a script file of ten command types <br />
(see documents/Command directory).    </p>

<p>Command documents and Notecard window examples are shown at:</p>

<pre><code>    notecard.org/document
</code></pre>

<h2>Applications:  </h2>

<pre><code>    Survey collection  
    Interviews
    Learning application
</code></pre>

<h2>Language:  </h2>

<pre><code>    Scala
</code></pre>

<h2>Run (src directory):  </h2>

<pre><code>    scala card demo/card
</code></pre>

<pre>
                      'scala card' executes the Notecard program.  In the argument 
                      'demo/card/', 'card' is a script file ('card.struct) in the 
                      'demo' directory. Execution illustrates the capabilities of 
                      the script commands.
</pre>

<h2>Fast Scala Compiler (src directory)  </h2>

<pre><code>    fsc *.scala
</code></pre>

<h2>Script repository:</h2>

<p>The script program (see Script repository) converts the commands in files with <br />
the '.nc' extension to elements in files with the '.struct' extension. The <br />
Notecard program 'card' executes the '.struct' files.  </p>

<p>The script program's essential role is the validation of script commands.  </p>
