
c
d 5/A practical application of the 'l' command 
d might be to provide a two question test where 
d the user must get both correct. The '.nc' 
d file loops until this criteria is reached.
d 10/ l
d a $g1=false
d a $g2=false
d
d c ($g1)=(false)
d d capital of Ohio \(# $columbus)
d g ($columbus)=(Columbus)
d a $g1=true
d
d c ($g2)=(false)
d d capital of New York \(# $albany)   
d g ($albany)=(Albany)
d a $g2=true
d
d f nextFile ($g1)=(true) and ($g2)=(true)
* continue
d 5/
d The Ohio question will not repeat when the correct 
d response is given and 'true' is assigned to $g1.   
d And similarly for $g2.
* end
