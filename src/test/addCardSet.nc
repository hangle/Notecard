
c
d 25/Test Add Card Set
d
d 5/A CardSet with a dependent AddCardSet script is
d shown:
d 10/--------script---------
d c
d d CardSet display command.
d
d +
d d AddCardSet display command
d
d -------------------------
* continue
d 5/
d (%%/color blue/+Add) button executes the AddCardSet; or it
d is skipped by activating the (%%/color blue/Next) button.
* continue
d
d The +Add button displays:
d 10/style bold/AddCardSet display command
d
d 5/Following the execution of the AddCardSet, the
d second activation of the (%%/color blue/+Add) button
d re-displays the CardSet with its display:
d 10/style bold/CardSet display command

c 
d 5/3/The next CardSet begins the following script:
d 10/--------script---------
d c
d d CardSet display command.
d
d +
d d AddCardSet display command
d
d -------------------------
* continue
d
d 5/First test the (%%/color blue/+Add) button

c
d 15/7/CardSet display command.
d
d 10/12/size 10/CardSet has an associated AddCardSet.
d /size 10/Thus, the (%%/color blue/size 10/+Add) button is highlighted.
d /size 10/Hit +Add button to execute the AddCardSet.
d 55/24/size 10/The (%%/color blue/size 10/Next) button
d /size 10/will skip the AddCardSet.

+
d 15/7/AddCardSet display command
d
d 10/12/size 10/Had you hit the (%%/color blue/size 10/Next) button, then
d /size 10/this window would have been skipped.
d /size 10/Now hit the +Add button to return to the
d /size 10/earlier CardSet.
d

c
d 5/3/This is a CardSet without a dependent 
d AddCardSet.  As such, the (%%/color blue/+Add) button
d is not highlighted.
d
* continue
d Employ the (%%/color green/Prior) button to backup to the 
d CardSet, having the associated AddCardSet, 
d to try the alternate execution.
c
d 5/The next CardSet begins the following 
d script.
d ---------script--------------
d c
d d CardSet with multiple dependents
d +
d d First AddCardSet of four.
d +
d d Second AddCardSet of four
d +
d d Third AddCardSet of four
d +
d d Forth and last AddCardSet
d ----------------------------
* continue
d The +Add button takes you to the forth
d AddCardSet. The subsequent +Add button
d returns the associated CardSet.
d
d Or, the (%%/color blue/Next) button in any AddCardSet
d returns the associated CardSet.

c
d 10/12/CardSet with multiple dependents

+
d 8/6/First AddCardSet of four.
d 40/17/size 10/Keep hitting (%%/color blue/size 10/+Add) button
d /size 10/until a return to the CardSet
d /size 10/to which this AddCardSet is
d /size 10/associated.
d
d /size 10/or return immdiately by activating
d /size 10/the (%%/color blue/size 10/Next) button.
+
d 8/6/Second AddCardSet of four
d 40/17/size 10/Keep hitting (%%/color blue/size 10/+Add) button
d /size 10/until a return to the CardSet
d /size 10/to which this AddCardSet is
d /size 10/associated.
d
d /size 10/or return immdiately by activating
d /size 10/the (%%/color blue/size 10/Next) button.

+
d 8/6/Third AddCardSet of four
d 40/17/size 10/Keep hitting (%%/color blue/size 10/+Add) button
d /size 10/until a return to the CardSet
d /size 10/to which this AddCardSet is
d /size 10/associated.
d
d /size 10/or return immdiately by activating
d /size 10/the (%%/color blue/size 10/Next) button.

+
d 8/6/Forth and last AddCardSet 
d 40/17/size 10/Keep hitting (%%/color blue/size 10/+Add) button
d /size 10/until a return to the CardSet
d /size 10/to which this AddCardSet is
d /size 10/associated.
d
d /size 10/or return immdiately by activating
d /size 10/the (%%/color blue/size 10/Next) button.

c
d 5/3/Use the Prior button to backup to
d the associated CardSet in order
d to try a different path.
c
d 5/3/The AddCardSet's function is to
d provide additional information to that
d of the associated CardSet.  This
d supplimental information can be a probe, 
d prompt, or hint.
* continue
d 10/
d ------------script---------------
d c
d What is the capital of Ohio \(# $Columbus)
d + 
d Discover of the New World
d ---------------------------------
d
d 5/This script follows.

c
d 5/3/The following CardSet executes the following 
d script--a more practical  application of 
d the AddCardSet:
d 10/----------script---------------
d c
d d What is the capital of Ohio \(# $Columbus)
d x
d g ($Columbus)=(Columbus)
d d Correct
d ge
d d Sorry, Columbus is the capital
d + 
d Discover of the New World
d +
d d Begins with 'C' and has 8 letters.
d ---------------------------------
d 
* continue
d 5/The user is provided two additional CardSets 
d that provide a hint as to the correct response.
c
d 5/3/What is the capital of Ohio (# $Columbus)
x
g ($Columbus)=(Columbus)
d Correct
ge
d Sorry, Columbus is the capital
+ 
d Discover of the New World
+
d Begins with 'C' and has 8 letters.

* end

