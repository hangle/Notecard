c
d 5/3/Logic is tested by the Group command.
d 
d The tested script is shown. When executed,
d 'success' or 'failure' is displayed.
c
d 5/3/-------------script---------------
d g (1)=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)=(1)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (1)<>(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<>(2)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (1)!=(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)!=(2)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (2)>(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (2)>(1)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (1)<(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<(2)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (1)>=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)>=(1)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (2)>=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (2)>=(1)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (1)<=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<=(1)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (1)<=(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<=(2)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (east)m(north east west south)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (east)m(north east west south)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (other)!m(north east west south)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (other)!m(north east west south)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (philadelphia)100%(philadelphia)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (philadelphia)100%(philadelphia)
d Success
ge
d failure


c
d 5/3/'x' char results in a 92% match
d
d -------------script---------------
d g (philadelphia)90%(philxdelphia)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (philadelphia)90%(philxdelphia)
d Success
ge
d failure


c
d 5/3/'x' 'z' chars results in a 83% match
d
d -------------script---------------
d g (philadelphia)83%(philxdelphza)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (philadelphia)83%(philadelphza)
d Success
ge
d failure

f logicAnd
* end
