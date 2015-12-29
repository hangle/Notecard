c
d 5/3/Logic is tested with AND condition
 
   
c
d 5/3/-------------script---------------
d g (1)=(1)and(1)=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)=(1)and(1)=(1)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (1)<>(2)and(1)<>(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<>(2)and(1)<>(2)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (1)!=(2)and(1)!=(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)!=(2)and(1)!=(2)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (2)>(1)and(2)>(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (2)>(1)and(2)>(1)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (1)<(2)and(1)<(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<(2)and(1)<(2)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (1)>=(1)and(1)>=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)>=(1)and(1)>=(1)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (2)>=(1)and(2)>=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (2)>=(1)and(2)>=(1)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (1)<=(1)and(1)<=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<=(1)and(1)<=(1)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (1)<=(2)and(1)<=(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<=(2)and(1)<=(2)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (east)m(north east)and(east)m(north east)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (east)m(north east)and(east)m(north east)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (other)!m(north east)and(other)!m(north east)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (other)!m(north east)and(other)!m(north east)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (philadelphia)100%(philadelphia)and(1)=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (philadelphia)100%(philadelphia)and(1)=(1)
d Success
ge
d failure


c
d 5/3/'x' char results in a 92% match
d
d -------------script---------------
d g (philadelphia)90%(philxdelphia)and(1)=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (philadelphia)90%(philxdelphia)and(1)=(1)
d Success
ge
d failure

f logicOr
* end
