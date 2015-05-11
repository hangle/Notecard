
c
d 5/3/ Test Load Dictionary Command.
d
d A CardSet whose only task is to 
d assigns values to $<variables>.
d
d This CardSet only contains
d assigment commands (%% /color blue/a), such as:
d
d 10/a $found=false
d a $max= 0
d 5/
d Besides the 'a' Assign command, 
d this CardSet begins with the '(%% /color blue/l)'
d symbol rather than the 'c' symbol.
c
d 5/3/The role of the 'l' or Load Dictionay
d command is to initialize $<variables>. 
d
d It is typically the 1st CardSet of the .nc
d file. 
d
d If this CardSet is encounter again within
d the same file execution, then its assign
d commands are not re-executed.  That is,
d the $<varables> are not again initialized. 
d
d The way to have the .nc file execute more
d than once is to not provide a Asterisk
d End command ((%% /color blue/* end)) to
d terminate the file.
c
d 5/3/The following CardSets of file 'loadLoop.nc'
d increments $max three times to cause the file
d 'f' command 'f escape ($max)=(3.0)' to execute.
d 15/
d  ---------file: (%% /color blue/loadLoop.nc) ------------
d l
d a $max=0.0
d c
d a $max= $max +1.0
d d max= \(% $max)
d f loadLoop  ($max) = (3.0)
d
d ---------------------------------------
d
* continue
d 5/The Next button or space bar activation will
d execute (%% /color blue/loadLoop.nc).
f loadLoop
* end
