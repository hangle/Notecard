
c
d 5/3/The letter size within the Input Field
d can be altered the the argument (%%/color blue/size <n>),
d for example:
d 10/
d --------------------------
d d \(# /size 10/ $one)
d ----------------------------
c
d 5/3/
d d (# /size 10/ $one)
c
d 5/3/
d  (# /length 4/ $one)
c
d 15/3/Input or Response Fields
c
d 5/3/Following script is executed by
d the next CardSet.
d 10/
d ---------------------
d d \(# $one)
d ---------------------
c
d 5/3/(# $one)
c
d 5/3/
d 10/
d ------------------------------
d d \(# $one)  \(# $two)
d ------------------------------
c
d 5/3/
d (# $one)  (# $two)

c
d 5/3/
d 10/
d ------------------------------
d d \(# $one)  \(# $two)
d d \(# $three)  \(# $four)
d ------------------------------

c
d 5/3/ 
d  (# $one)  (# $two)
d  (# $three)  (# $four)
c
d 5/3/The length of the Input Field is
d set by the argument (%%/color blue/length <n>),
d for example:
d 10/
d ----------------------------
d  d \(# /length 2/ $one)
d ----------------------------
d
d (# /length 2/ $one1)
c
d very last card
* end
