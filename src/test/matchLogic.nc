
c
d 20/3/Match Logic Operator 'm'
d 5/
d The Match 'm' operator compares its left-hand
d value against a list of right-hand values.
d
d For example:
d
d 10/---------Script----------------
d a $abc=west
d g ($abc) m (north south east west)
d -----------------------------------
d 5/
d The $abc content (west) is compared to each
d element in the list. A match is found with
d the list element 'west'.
* continue
d
d The next CardSet executes the above.
c
d 5/3/
d 10/---------Script----------------
d c
d a $abc=west
d g ($abc) m (north south east west)
d d Match was successful
d -----------------------------------
d
a $abc=west
g ($abc) m (north south east west)
d Match was successful
d
* continue
d The next CardSet shows, with some additional
d code, a match failure.


c
d 5/3/The match operator (%%/color blue/!m) returns
d (%%/color blue/false) when the match succeeds.
d
d 10/---------Script----------------
d c
d a $abc=west
d g ($abc) !m (north south east west)
d d Match failed but returned true.
d ge
d d Match succeeded but returned false
d -----------------------------------
d
a $abc=west
g ($abc) !m (north south east west)
d Match failed but returned true.
ge
d Match succeeded but returned false

c
d 5/3/The match operator (%%/color blue/!m) returns
d (%%/color blue/false) when the match fails.
d 10/---------Script----------------
d c
d a $abc=xxxxx
d g ($abc) !m (north south east west)
d d Match failed but returned true.
d ge
d d Match succeeded but returned false
d -----------------------------------
d
a $abc=xxxxx
g ($abc) !m (north south east west)
d Match failed but returned true.
ge
d Match succeeded but returned false

c
d 5/3/
d 10/---------Script----------------
d c
d a $abc=xxxxx
d g ($abc) m (north south east west)
d d Match was successful
d ge
d d Match was Not successful
d -----------------------------------
d
a $abc=xxxxx
g ($abc) m (north south east west)
d Match was successful
ge
d Match was Not successful

c
d 5/3/The script shows a $<variable> as a left
d operand and one $<variable> as a right
d operand.
d 10/----------Script-----------
d c
d a $left=west
d a $north=north
d g ($left) m ($north south east west) 
d d match Successful
d ge
d d match Unsuccessful
d -------------------------------
d
a $left=west
a $north=north
g ($left) m ($north south east west) 
d match Successful
ge
d match Unsuccessful

c
d 5/3/The script shows a match between two
d $<variable>s.
d
d 10/----------Script-----------
d c
d a $left=west
d a $west=west
d g ($left) m (north south east $west) 
d d match Successful
d ge
d d match Unsuccessful
d -------------------------------
d
a $left=west
a $west=west
g ($left) m (north south east $west) 
d match Successful
ge
d match Unsuccessful

c
d 5/3/The script shows all elements of the
d match statement are $<variable>s.
d
d 10/----------Script-----------
d c
d a $left=west
d a $north=north
d a $south=south
d a $east=east
d a $west=west
d g ($left) m ($north $south $east $west) 
d d match Successful
d ge
d d match Unsuccessful
d -------------------------------
d
a $left=west
a $north=north
a $south=south
a $east=east
a $west=west
g ($left) m ($north $south $east $west) 
d match Successful
ge
d match Unsuccessful




* end

