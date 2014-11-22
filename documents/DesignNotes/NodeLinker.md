<hr />

<h2>Node And Linker Traits</h2>

<p>The following lists the class of the linked list hierachy. The Notecard <br />
class has four children (NotecardTask, NextFile, CardSet, LoadDictionary). <br />
The CardSet class has 5 children, beginning with the Assign Class. The <br />
RowerNode class has 3 classes. The BoxField has only one child (EditNode).  </p>

<pre>
        Notecard Class Hierarchy

                                                 Script Examples  
                         ---------------
               Notecard  
          LoadDictionary         l
            Assign           a $value=5
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

<p>Every hierarchy class extends the trait Node. Thus, every class instance <br />
can be treated as a Node type.  In the Node trait, every hierarchy class, <br />
with the exception of Notecard, has a 'sibling' address that references <br />
the next sibling.    </p>

<p>A parent class (Notecard, CardSet, RowerNode, and BoxField) instance <br />
has a 'child' address that references the first child or first sibling. <br />
A parent's list of successive siblings is handled by the parent <br />
providing its child reference as the first sibling, then following <br />
the 'sibling' addresses until the list ends.  </p>

<p>The Linker trait provides two functions (reset(...) and iterate) to <br />
process a parent's list of instances.  Every parent class with children <br />
extends the trait Linker.  Linker employes 'reset(...)' and 'iterate' <br />
functions to follow successive siblings until the list ends.  In the <br />
Notecard class show an abstract description of these two functions <br />
(code has been removed to show critical parts)  </p>

<pre>
                // Every parent class references its
                // 1st child or first sibling. 
    reset(getFirstChild)
                // iterate() returns a boolean and
                // assigns the 'current' sibling to
                // 'node'.
    while(iterate) {
        node match {
            case cs:CardSet=>
                cs.startCardSet(...)
            case ft:NotecardTask =>
                ft:startNotecardTask(...)
            case nf:NextFile => 
                nf:startNextFile
            case ld:LoadDictionary =>
                ld.startLoadDictionary(...) 
            }
</pre>

<p>The reset function hand the iterate function the first sibling and <br />
it is assigned to 'node'  The match statement determines the node <br />
type ( CardSet, NotecardTask, or NextFile). When the while statement <br />
loops a second time, 'node' holds the reference to the next sibling <br />
in the list.   </p>

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

<p>The following example employees a script file 'fourCards.nc' that <br />
was processed by the Script program whose output is 'fourCards.struct'. <br />
The Script output  'fourCards.struct' is the input to the Notecard <br />
program.  </p>

<p>The initial loop of 'while(iterate)' passes the address 2002 to 'node' <br />
which is the first CardSet.  The 'iterator' value has also been assigned <br />
the addresss 2008 of the second CardSet.  When 'while(iterate)' loops a <br />
second time, then 2008 is assigned to 'node'.   </p>

<pre>
        %Notecard
        child   2002
        ...
        %%
        %CardSet
        address 2002
        child   2003    ---> RowerNode ---> DisplayText->"Card one"
        sibling 2005
        ...
        %%
        %CardSet
        address 2005
        child   2006    ---> RowerNode ---> DisplayText->"Card two"
        sibling 2008
        ...
        %%
        %CardSet
        address 2008
        child   2009    ---> RowerNode ---> DisplayText->"Card three"
        sibling 2011
        ...
        %%
        %CardSet
        address 2011
        child   2012    ---> RowerNode ---> DisplayText->"Card four"
        sibling 2014
        ...
        %%
        %NotecardTask
        address 2014
        ...
</pre>
