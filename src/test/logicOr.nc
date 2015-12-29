c
d 5/3/Logic is tested with OR condition
 
   
c
d 5/3/-------------script---------------
d g (1)=(0)or(1)=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)=(0)or(1)=(1)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (1)<>(0)or(1)<>(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<>(0)or(1)<>(2)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (1)!=(1)or(1)!=(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)!=(1)or(1)!=(2)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (2)>(2)or(2)>(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (2)>(2)or(2)>(1)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (1)<(1)or(1)<(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<(1)or(1)<(2)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (1)>=(0)or(1)>=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)>=(0)or(1)>=(1)
d Success
ge
d failure

c
d 5/3/-------------script---------------
d g (0)>=(1)or(2)>=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (0)>=(1)or(2)>=(1)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (1)<=(0)or(1)<=(1)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<=(0)or(1)<=(1)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (1)<=(0)or(1)<=(2)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)<=(0)or(1)<=(2)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (other)m(north east)or(east)m(north east)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (other)m(north east)or(east)m(north east)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (east)!m(north east)or(other)!m(north east)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (east)!m(north east)or(other)!m(north east)
d Success
ge
d failure


c
d 5/3/-------------script---------------
d g (1)=(2)or(philadelphia)100%(philadelphia)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)=(2)or(philadelphia)100%(philadelphia)
d Success
ge
d failure


c
d 5/3/'x' char results in a 92% match
d
d -------------script---------------
d g (1(=(2)or(philadelphia)90%(philxdelphia)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)=(2)or(philadelphia)90%(philxdelphia)
d Success
ge
d failure


c
d 5/3/'x' 'z' chars results in a 83% match
d
d -------------script---------------
d g (1)=(2)or(philadelphia)83%(philxdelphza)
d d Success
d ge
d d failure
d * continue
d ----------------------------------------
* continue
g (1)=(2)or(philadelphia)83%(philxdelphza)
d Success
ge
d failure

* end
