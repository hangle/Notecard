c
d 20/3/size 20/color blue/style bold/Appearance Components.
d 5/
d Two types of Appearance Components:
d 10/1. Start of the 'd' command, 
d          such as: d /size 20/...
d 2. Embedded such as \(# /size 20/$abc)
d 13/or   \(%/color blue/ $abc)
d or   \(%%/style bold/now is)

c
d 20/3/size 20/color blue/style bold/size parameter
d 10/
d --------------------script----------------
d d /size 20/line size is 20
d ----------------------------------------
d
d
d /size 20/line size is 20

c
d 20/3/size 20/color blue/style bold/color parameter
d 10/
d -----------------------script----------------------
d d /color blue/color is blue
d ---------------------------------------------------
d
d
d /color blue/color is blue

c
d 20/3/size 20/color blue/style bold/style parameter
d 10/
d --------------------script-----------------------
d d /style bold/style is bold
d -------------------------------------------------
d
d
d /style bold/style is bold

c
d 20/3/size 20/color blue/style bold/style parameter
d 10/
d -------------------script--------------------
d d /style italic/style is italic
d ---------------------------------------------
d
d
d /style italic/style is italic
c
d 20/3/size 20/color blue/style bold/style parameter
d 10/
d ---------------------script-------------------------
d d /style bold+italic /style is bold and italic
d ---------------------------------------------------
d
d
d /style bold+italic/style is bold and italic


c
d 20/3/style bold/size 20/color blue/name parameter
d 10/
d ------------------script---------------------
d d /name Monospaced/now is the time
d ---------------------------------------------
d
d /name Monospaced/now is the time

c
d 5/3/Portions of the 'd' command text can
d be altered by the 'appearance parameter
d enclosed within '/' delimiters, that  
d follows the (%%/color blue/\(%%) tag, for
d example:
d 15/--------------------script------------------------
d d now is the \(%% /color blue/for all) good men
d ----------------------------------------------------
d
d now is the (%%   /color blue/for all) good men

c
d 15/3/style bold/size 20/color blue/color parameter
dk
d 1/------------------------script--------------------------
d d /color green/now \(%% /color magenta/the) time 
d -------------------------------------------------------
d
d 5/color green/now (%%     /color magenta/the) time 
d
* continue
d 5/Following the (%%/color magenta/magenta) collor
d change, the text color reverts to (%%/color green/green).

c
d 15/3/style bold/size 20/color blue/size parameter
d 10/
d ----------------------script------------------
d d now is \(%%/size 20/is the) time
d ----------------------------------------------
d
d now is (%%/size 20/is the) time

c
d 15/3/style bold/size 20/color blue/style parameter
d 10/
d ----------------------script------------------
d d now is \(%%/ style bold  /is the) time
d ----------------------------------------------
d
d now is (%%/style bold/is the) time

c
d 12/3/Change from TimesRoman to Monospaced and
d back to TimesRoman.
d 5/
d ----------------------script-------------------------
d d abcdef\(%%/ name Monospaced   /ghijkl)mnopqrs 
d -----------------------------------------------------
d
d abcdef(%%/name Monospaced/ghijkl)mnopqrs 


* end
