c
d 5/3/Group Command Test Script
c
d 5/3/
d ---------script----------
d 10/g (1)=(1)
d This line is displayed
d 5/----------------------
d 
g (1)=(1)
d 10/This line is displayed
d 20/***success**
c
d 5/3/
d ---------script----------
d 10/g (1)=(2)
d This line is NOT displayed
d 5/----------------------
d 
g (1)=(2)
d 10/This line is NOT displayed
c
d 5/3/
d 'g' tag added to end the
d scope of the 'g' group.
d
d ---------script----------
d 10/g (1)=(1)
d d This line is displayed
d g
d d This line is always displayed
d 5/----------------------
d
g (1)=(1)
d 10/This line is displayed
g
d 10/This line is always displayed
c
d 5/3/
d 'g' tag added to end the
d scope of the 'g' group.
d
d ---------script----------
d 10/g (1)=(2)
d d This line is NOT displayed
d g
d d This line is always displayed
d 5/----------------------
d
g (1)=(2)
d 10/This line is NOT displayed
g
d 10/This line is always displayed
c
d 5/3/Two lines are displayed and two
d lines are not displayed
d
d ----------script---------
d 10/g (1)=(1)
d This line (1) is displayed
d g (1)=(2)
d This line (2) is NOT displayed
d g (1)=(1)
d This line (3) is displayed
d g (1)=(2)
d This line (4) is NOT displayed
d 5/----------------------
d 10/
g (1)=(1)
d This line (1) is displayed
g (1)=(2)
d This line (2) is NOT displayed
g (1)=(1)
d This line (3) is displayed
g (1)=(2)
d This line (4) is NOT displayed
c
d 5/3/Lines 'scope ended' will display
d two times.
d
d ----------script---------
d 10/g (1)=(1)
d d This line (1) is displayed
d g 
d d scope ended
d g (1)=(2)
d d This line (2) is NOT displayed
d g
d d scope ended
d 5/----------------------
d 10/
g (1)=(1)
d 10/d This line (1) is displayed
g
d scope ended
g (1)=(2)
d d This line (2) is NOT displayed
g
d scope ended

c
d 5/1/Test of two 'ge' groups.  In the 1st
d the 'if/then' succeeds, but in the second,
d the 'else' succeeds.
d ----------script---------
d 10/g (1)=(1)
d d The 'if/then' is successful
d ge
d d This line NOT displayed
d g
d d Second group
d 10/g (1)=(2)
d d This line NOT displayed
d ge
d d The 'else' is successful
d 5/-------------------------
g (1)=(1)
d 10/The 'if/then' is successful
ge
d This line is NOT displayed
g
d Second group
g (1)=(2)
d 10/This line is NOT displayed
ge
d 10/The 'else' is successful



c
d 5/3/Test of 'ge' tag.  If 'g <condition>'
d fails, then the 'ge' group is executed.
d
d ----------script---------
d 10/g (1)=(1)
d d This line is displayed
d ge
d d This line is NOT displayed
d 5/-------------------------
d
g (1)=(1)
d 10/This line is displayed
ge
d This line is NOT displayed

c
d 5/3/Test of 'ge' tag.  If 'g <condition>'
d fails, then the 'ge' group is executed.
d
d ----------script---------
d 10/g (1)=(2)
d d This line NOT displayed
d ge
d d This line is displayed
d 5/-------------------------
d
g (1)=(2)
d 10/This line is NOT displayed
ge
d 10/This line is displayed

c
d 5/3/Test of 'ge <condition>' with
d 10/ge (1)=(1)
d
d 5/----------script-----------
d 10/g (1)=(1)
d d This line is displayed
d ge (1)=(2)
d d This line is NOT displayed
d 5/---------------------------
d
g (1)=(1)
d 10/This line is displayed
ge (1)=(2)
d 10/This line is NOT displayed

c
d 5/3/Test of 'else' with <condition> with
d terminating 'ge' without <condition>
d
d 5/----------script-----------
d 10/g (1)=(2)
d d This line is NOT displayed
d ge (1)=(2)
d d This line is NOT displayed
d ge
d d This line is displayed
d 5/---------------------------
d
g (1)=(2)
d 10/This line (1) is NOT displayed
ge (1)=(2)
d 10/This line (2) is NOT displayed
ge
d This line is displayed
c
d 5/3/Repeat test of 'else' with <condition> 
d with terminating 'ge' without <condition>
d
d 5/----------script-----------
d 10/g (1)=(2)
d d This line is NOT displayed
d ge (1)=(1)
d d This line is displayed
d ge
d d This line is NOT displayed
d 5/---------------------------
d
g (1)=(2)
d 10/This line  is NOT displayed
ge (1)=(2)
d 10/This line is NOT displayed
ge
d This line is displayed

c
d 5/3/Completion of Group Command test
* end
