l
a $g1=false
a $g2=false
a $once=false
c ($once)=(false)
a $once=true
d 5/A practical application of the 'l' command 
d might be to provide a two question test where 
d the user must get both correct. The script
d loops until this criteria is reached.
d 10/ l
d a $g1=false
d a $g2=false
d
d c ($g1)=(false)
d d Capital of Ohio \(# $columbus)
d x
d g ($columbus)=(Columbus)
d a $g1=true
d
d c ($g2)=(false)
d d Capital of New York \(# $albany)   
d x
d g ($albany)=(Albany)
d a $g2=true
d
d f nextFile ($g1)=(true) and ($g2)=(true)
c ($g1)=(false)
d 3/5/This CardSet and the next will execute
d questions of the Ohio and New York capitals.
d
d Enter   Columbus <or> columbus
d
d Capital of Ohio (# $columbus)
x
g ($Columbus)=nc(Columbus)
a $g1=true
ge
d Sorry. Columbus is the capital
d
d Additional code was added in case of
d a mistake.
g
c ($g2)=(false)
d 5/3/Enter Albany <or> albany
d
d
d Capital of New York (# $albany)   
x
g ($Albany)=nc(Albany)
a $g2=true
ge
d Sorry. Albany is the capital
d
d
d Additional code was added in case of
d a mistake.
g

f nextFile ($g1)=(true) and ($g2)=(true)
