c
d 5/3/The Assign 'a' command copies a value to a
d $<variable>. For example:
d
d 10/----------Script------------
d c
d a $abc= 22
d ...
d -------------------------------
d 5/The string 22 is assigned to $abc
* continue
d
d The 'a' command has two required elements:
d      1. Target such as $abc
d      2. Source such as 22
d      3. And, an optional if condition, e.g.:
d
d 10/----------Script------------
d c
d a $abc= 22 if($abc)=(0)
d ...
d -------------------------------

c
d 10/3/------------Script--------------
d c
d a $abc= 22
d d $abc= \(% $abc)
d ------------------------------------
d
a $abc= 22
d $abc= (% $abc)
c
d 10/3/-------------Script-----------------
d a $abc=22 +2
d d $abc= \(% $abc)
d
d a $abc=22/11
d d $abc= \(% $abc)
d ---------------------------------------
d
a $abc=22 +2
d $abc= (% $abc)
d
a $abc=22 / 11
d $abc= (% $abc)

c
d 10/3/-------------Script-----------------
d a $abc=(22 +2) / (1*1)
d d $abc= \(% $abc)
d
d a $abc=(20+2) / (10+1)
d d $abc= \(% $abc)
d ---------------------------------------
d
a $abc=(22 +2) / (1*1)
d $abc= (% $abc)
d
a $abc=(20+2) / (10+1)
d $abc= (% $abc)

c 
d 5/3/The Assign's source element may be a $<variable>.
d First the target $abc is assigned 22, then $abc 
d becomes a source to copy 22 to $xyz
d
d 10/-------------Script-----------------
d a $abc=22
d a $xyz=$abc
d
d d $xyz=\(% $xyz)
d ---------------------------------------
d
a $abc=22
a $xyz=$abc
d
d $xyz=(% $xyz)

c
a $one=1
a $ten=10
d 5/3/The $<variable> employed in a math expression
d
d 10/-------------Script-----------------
d a $one=1
d a $ten=10
d
d a $abc=(20 + 2) / ($one + $one)
d d $abc= \(% $abc)
d
d a $xyz=(20 + 2) / ($ten + $one)
d d $xyz= \(% $xyz)
d ---------------------------------------
d
a $abc=(20 + 2) / ($one + $one)
d $abc= (% $abc)
d
a $xyz=(20 + 2) / (10 + 1)
d $xyz= (% $xyz)
* continue
d
d 5/Values should be:
d 10/
d $abc equals 11
d $xyz equals 2

c 
d 5/3/The source my be conditionally assigned, however,
d the logic must begin with an 'if' expression.
d
d 10/-------------Script-----------------
d a $abc= 99
d a $abc= 99 + 1 if(1)=(1)
d
d a $xyz= 99
d a $xyz= 99 + 1 if(1)=(2)
d
d $abc=\(% $abc)
d $xyz=\(% $xyz) 
d ---------------------------------------
d
a $abc= 99
a $abc= 99 + 1 if(1)=(1)
a $xyz= 99
a $xyz= 99 + 1 if(1)=(2)
d $abc=(% $abc)
d $xyz=(% $xyz) 

* end
