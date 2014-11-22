<h2>Backup  Mechanism</h2>

<p>The script file 'threeCardSets.nc' shows three CardSets:  </p>

<pre>
        c
        d First card set
        c
        d Second card set
        c
        d Third card set
</pre>

<p>The design document NodeLinker.md describes how the Notecard <br />
program processes these card sets in their proper order. <br />
The parent classes (Notecard, CardSet, RowerNode, BoxField <br />
and LoadDictionary) of the linked list hierarchy employ two <br />
traits Node and Linker to move from one card set to the next.  </p>

<pre>
        Notecard Class Hierarchy
                                                 Script Examples  
                         ---------------
               Notecard  
                  NotecardTask                   * end  
                  NextFile                       f maleScript  
                  CardSet                        c (1)=(2)  
                      Assign                     a $count=$count+1  
                      XNode                      x  
                      GroupNode                  g (1) = (1)  
                      CardSetTask                * continue  
                      RowerNode                  d 3/5/  
                          DisplayText            d now is  
                          DisplayVariable        d (% $count)  
                          BoxField               d (# $name)  
                              EditNode           e ($count) < (5)  
</pre>

<p>Every hierarchy class extends the trait Node. Every class, with <br />
the exception of Notecard, references its 'next' sibling of type <br />
Node.   </p>

<p>Every parent class (Notecard, LoadDictionary, CardSet, RowerNode, <br />
BoxField) references its 1st child or sibling of type Node.  The <br />
parent class also extends Linker allowing transition from one <br />
Node type to the next.    </p>

<p>The trait Linker has two functions (reset and iterate). These two <br />
functions are shown for the Notecard class to process its four <br />
children (LoadDictionary, NotecardTask, NextFile, and Cardset):  </p>

<pre>
        reset(getFirstChild)
        while(iterate) 
            node match {
                ld: LoadDictionary => ...
                nt: NotecardTask => ...
                nf: NextFile => ...
                cs: CardSet => ...
                    storePriorSiblingInBackupList
                    cs:startCardSet(...)
                ...
</pre>

<p>The two functions are shown below: The variable 'node' is the current <br />
sibling while 'iterator=iterator.next' assigns the next sibling to <br />
'iterator'. On the initial 'while(iterate)' loop, the variable <br />
'iterator' holds the 1st sibling instance, on the next loop, it holds <br />
the second sibling, and so on.  </p>

<pre>
        def reset(child:Node) { // Initialize the list
                    // child is the 1st child of the 
                    // Parent, that is 1st sibling 
            iterator=child
            }
        def iterate= {
            if(iterator==null)
                false
              else {
                    // store current sibling before 
                    // accessing the next sibling 
                node=iterator
                    // 'next' is the next sibling
                    // held by the current sibling
                iterator=iterator.next
                true
                }
            }
</pre>

<p>In the iterate loop, the CardSet nodes are handled by:  </p>

<pre>
        cs:CardSet=>
            storePriorSiblingInBackupList
            if(cs.isAddCardSet)
                buttonSet.armAddCardSet
            cs.startCardSet(notePanel, lock, buttonSet, statusLine)
            waitOverDoButtons(taskGather, 
                      cs,
                      notePanel,
                      lock,
                      buttonSet, 
                      statusLine)
</pre>

<p>Linker.storePriorSiblingInBackupList stores the "current" 'node' of the iterate <br />
function in a list (backupList). When the loop completes, the "current" become <br />
the "prior" node and 'iterator' becomes the new "current" while 'startCardSet' <br />
executes (loop not completed).  </p>

<p>The 'waitOverDoButton(...)' allows the user to activate the Prior button provided <br />
the there is a prior CardSet. Again the loop has not completed. Activation of <br />
the Prior button invokes the Linker function 'loadBackupWithIterator':  </p>

<p>The first task of this function is to remove the 'node' that was just placed <br />
on the 'backupList'.  Until the loop completes, the 'node' is not actually the <br />
"prior" CardSet.  When removed, than the head of the 'backupList' is the <br />
"prior" CardSet.  This "prior" CardSet is assigned to 'iterator' of the iterate <br />
function. When the new loop begins, this "prior" CardSet is executed.   </p>

<p>The 'loadBackupWithIterator' handles the case it is not possible to backup <br />
beyond the 1st CardSet.  Another case arises in the event a 'c' (clear) <br />
command has a logical condition that fails.  The logical condition is <br />
tested in the CardSet class, however, the CardSet's "prior" node is stored <br />
in the 'backupList' yet when its condition fails, then the CardSet is <br />
skipped.   </p>

<p>Suppose three adjacent CardSet 'c' command conditions fail.  In the CardSet <br />
class these sets are skipped, that is, the set of commands are not executed:  </p>

<pre>
    if(noConditionOrIsTrue(...)){
        <do CardSet commands>
        showPanel(notePanel) 
            // Allow button activation by user.
        haltCommandExecution(lock)  
        clearNotePanel(notePanel)   
        }
</pre>

<p>However, the nodes of the three are stored in the 'backupList'.  When <br />
the Prior button is activated,  the backup mechanism begins by loading <br />
the three failed CardSets into the 'iterate' function. But again the <br />
CardSet class skips the three because the halt or locking mechanism is <br />
also skips. The prior CardSet with no condition or a successful one <br />
will encounter the halt and locking mechanism.   </p>
