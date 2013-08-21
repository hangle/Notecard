<h1>Notecard Program</h1>

<p>The Notecard presents a window about the size of a note card.  The window displays text <br />
to and accepts input from the program user (see example: www.notecard.org/document). <br />
The text and response capture are controlled by a script file of eight command types.  </p>

<h2>Applications:  </h2>

<pre><code>    Survey collection  
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

<p>These capabilities can also be viewed at: <br />
www.notecard.org:8090/cxm/background/intro<em>show</em>features.htm <br />
Note, the applet system, written in java, is no longer supported.  </p>

<h2>Compliation (src directory)  </h2>

<pre><code>    fsc *.scala
</code></pre>

<h2>Script repository:</h2>

<p>The command script is validated by the program 'script.scala' (see Script repository).
The output of 'script.scala' is a file with the extension '.struct' that serves
as the input to 'scala card'.</p>
