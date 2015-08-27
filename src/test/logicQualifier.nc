c
d 5/3/Logic qualification allows unequal
d operands to match one another.
d
d The tag 'nc' (no case), following the equal 

d '=' operator, converts the two operands
d to lower case.
d
d 10/---------Script------------
d g (ABC)=nc(aBc)
d d ABC equals aBc
d -------------------------------
d
d 5/Next button will execute the above.
* continue
d
g (ABC)=nc(aBc)
d 10/ABC equals aBc

c
d 5/3/One operands is $<variable>.
d The other is a string.
d
d 10/-----------Script-----------
d a $abc=ABC
d g ($abc)=nc(aBc)
d d ABC equals aBc
d -------------------------------
d
d 5/Next button will execute the above.
* continue
d
a $abc=ABC
g ($abc)=nc(aBc)
d 10/ABC equals aBc


c
d 5/3/Operands are $<variable>s.
d
d 10/-----------Script-----------
d a $abc=ABC
d a $xyz=aBc
d g ($abc)=nc($xyz)
d d ABC equals aBc
d -------------------------------
d
d 5/Next button will execute the above.
* continue
d
a $abc=ABC
a $xyz=aBc
g ($abc)=nc($xyz)
d 10/ABC equals aBc

c
d 5/3/Again the operands are $<variable>s.
d but the logic fails
d
d 10/-----------Script-----------
d a $abc=ABC
d a $xyz=aBx
d g ($abc)=nc($xyz)
d d ABC equals aBx
d ge
d d ABC Not equal to aBx
d -------------------------------
d
d 5/Next button will execute the above.
* continue
d
a $abc=ABC
a $xyz=aBx
g ($abc)=nc($xyz)
d 10/ABC equals aBx
ge
d 10/ABC Not equal to aBx

c
d 5/3/The tag 'ns' (no spaces), following the equal 
d '=' operator, removes all spaces from the two 
d operands.
d
d 10/---------Script------------
d g (a  b c)=ns( a    b    c)
d d 'a  b c' equals ' a    b    c'
d -------------------------------
d
d 5/Next button will execute the above.
* continue
d
g (a  b c)=ns( a    b    c)
d 10/'a  b c' equals ' a    b    c'

c
d 5/3/The right operand, with spaces,  is made a 
d $<variable>
d
d 10/---------Script------------
d a $right= a    b    c
d g (a  b c)=ns($right)
d d 'a  b c' equals ' a    b    c'
d -------------------------------
d
d 5/Next button will execute the above.
* continue
d
a $right= a    b    c
g (a  b c)=ns($right)
d 10/'a  b c' equals ' a    b    c'

c
d 5/3/Both operands are made  $<variable>s.
d
d 10/---------Script------------
d a $left=a  b c
d a $right= a    b    c
d g ($left)=ns($right)
d d 'a  b c' equals ' a    b    c'
d -------------------------------
d
d 5/Next button will execute the above.
* continue
d
a $left=a  b c
a $right= a    b    c
g ($left)=ns($right)
d 10/'a  b c' equals ' a    b    c'

c
d 5/3/Both operands are  $<variable>s but
d the logic is made to fail when 'c' becomes
d 'x'
d
d 10/---------Script------------
d a $left=a  b c
d a $right= a    b    x
d g ($left)=ns($right)
d d 'a  b c' equals ' a    b    c'
ge
d d 'a  b c' Not equal to  ' a    b    x'
d -------------------------------
d
d 5/Next button will execute the above.
* continue
d
a $left=a  b c
a $right= a    b    x
g ($left)=ns($right)
d 10/'a  b c' equals ' a    b    c'
ge
d 10/'a  b c' Not equal to  ' a    b    x'

c
d 5/3/The tag '1s' (one space), following the equal 
d '=' operator, reduces all consecutive spaces to
d one space.
d
d 10/---------Script------------
d g (a  b c)=1s( a    b           c)
d d 'a  b c' equals ' a    b           c'
d -------------------------------
d
d 5/Next button will execute the above.
* continue
d
g (a  b c)=ns(a    b         c)
d 10/'a  b c' equals ' a    b           c'

c
d 5/The following illustrates the difference
d between the 'ns' and '1s' tags when a 
d a space in missing in 'nowis'.
d
d 10/---------Script------------
d g (nowis the time)=ns(now is the time)
d d 'nowis the time' equals 'now is the time'
d -------------------------------
d 5/Next button will execute the above.
* continue
d
g (nowis the time)=ns(now is the time)
d 10/'nowis the time' equals 'now is the time'
* continue
d
d 10/---------Script------------
d g (nowis the time)=1s(now is the time)
d d 'nowis the time' equals 'now is the time'
d ge
d d 'nowis the time' Not equals 'now is the time'
d -------------------------------
d
d 5/Next button will execute the above.
* continue
d 10/
g (nowis the time)=1s(now is the time)
d 10/'nowis the time' equals 'now is the time'
ge
d 10/'nowis the time' Not equals 'now is the time'

c
d 5/3/The tags 'nc' and 'ns' may be used jointly as
d well as the tags  'nc' and '1s'.
d
d 10/-----------Script--------------
d g (ABC)=ncns(a  B    c)
d d 'ABC' equals 'a  B   c'
d -------------------------------
d
d 5/Next button will execute the above.
* continue
d
g (ABC)=ncns(a  B    c)
d 10/'ABC' equals 'a  B   c'


c
d 5/3/The logic operator '%' allows a partial match
d to be successful, for example:
d
d 10/-------------Script-------------
d a $city=Philedelphia
d g ($city) 80% (Philadelphia)
d d Match
d ge
d d No Match
d ----------------------------------
a $city=Philedelphia
g ($city) 80% (Philadelphia)
d Match (partial)
ge
d No Match
g
d 5/
d 
d $city is misspelled.
d
d Eleven letters out of 12 are the same.
d Both operands match with respect to:
d 10/Phil  <and>  delphia  such 
d that 11/12= 92.6% exceeds 80%.

c
d 5/1/Assume $city was captured by:
d 10/d Enter: \(# $city)
d 5/The spelling mistake still occurred, but
d leading spaces were entered and 'p' was
d not capitalized.
d
d 10/-------------Script-------------
d a $city=   philedelphia
d g ($city) 80% nc ns (Philadelphia)
d d Match (partial)
d ge
d d No Match
d ----------------------------------
a $city=Philedelphia
g ($city) 80% nc ns (Philadelphia)
d Match (partial)
ge
d No Match
g
d 5/
d 
d $city is misspelled.
d
d Qualifier 'ns' removed leading spaces.
d Qualifier 'nc' changed 'P' to 'p'. 

c
d 5/The Match 'm' operator applies a 
d single left-hand  value to a list 
d of right-hand values.
d 10/
d ----------Script-----------
d a $abc=west
d g ($abc) m (north south east west)
d ---------------------------------
d 5/
d In the case of a qualifier (nc ns 1s)
d conversion to, for example, (ns & nc)
d are applied only to $<variable> values
d
d 10/-------Script----------------------
d a $abc= W es  t
d g ($abc) m nc ns (north south east west)
d -------------------------------------
d
d The contents of $abc matches "west" in
d spite of spaces and capitalization. 
d 5/
d The next CardSet will test this arrangement.

c
d 5/2/
d ----------Script-----------
d a $abc= W es  t
d g ($abc) m nc ns (north south east west)
d d $abc equals west
d ge
d d $abc does Not equal west
d ---------------------------------
d
a $abc= W es  t
g ($abc) m nc ns  (north south east west)
d $abc equals west
ge
d $abc does Not equal west

c
d 5/3/$<variable>s in the left-hand list are
d also subjects to qualification of 
d no spaces, no capitalizaton and single
d space (1s).
d 10/ ------------Script----------------
d a $abc=   W es   T
d a $west=w   ES  t  
d g ($abc) m ns nc (north south east $west)
d d $abc equals $west
d ge
d d $abc Not equal to $west
d ------------------------------------
d
a $abc=   W es   T
a $west=w   ES  t  
g ($abc) m ns nc (north south east $west)
d $abc equals $west
ge
d $abc Not equal to $west



* end

