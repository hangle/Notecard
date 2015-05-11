c
d 15/3/size 20/style bold/Letter Size Parameter
d 5/6/The above title was the result of the 
d following script.
d
d ---------------------------------------------
d d 15/3/size 20/Letter Size Parameter
d ---------------------------------------------
d 
* continue
d The typical default letter height is 14. The
d size parameter:
d 15/color blue/ /size 20/
d 5/changed the height from 14 to 20.
c
d 5/3/The script that tests the (%%/color blue/size) parameter
d is shown and is followed by the CardSet that
d executes the script:
d -----------------------------------------------
d d /size 10/ size 10
d d /size 12/ size 12
d d /size 14/ size 14
d d /size 16/ size 16
d d /size 18/ size 18
d d /size 20/ size 20
d -----------------------------------------------
c
d /size 10/ size 10
d /size 12/ size 12
d /size 14/ size 14
d /size 16/ size 16
d /size 18/ size 18
d /size 20/ size 20

c
d 5/3/In the following script, each 'd' command
d has two size values, the 'd' component with 
d the size parameter is followed by a 
d component having a different letter size 
d value.
d
d -----------------------------------------
d d /size 10/ size 10 \(%% /size 20/size 20)
d d /size 12/ size 12 \(%% /size 18/size 18)
d d /size 14/ size 14 \(%% /size 16/size 16)
d d /size 16/ size 16 \(%% /size 14/size 14)
d d /size 18/ size 18 \(%% /size 12/size 12)
d d /size 20/ size 20 \(%% /size 10/size 10)
d -----------------------------------------
c
d /size 10/ size 10 (%% /size 20/size 20)
d /size 12/ size 12 (%% /size 18/size 18)
d /size 14/ size 14 (%% /size 16/size 16)
d /size 16/ size 16 (%% /size 14/size 14)
d /size 18/ size 18 (%% /size 12/size 12)
d /size 20/ size 20 (%% /size 10/size 10)

c
d 5/3/The following script shows a 'd' command
d whose letter size changes three times.
d 1/
d ----------------------------------------------
d d /size 10/size 10 \(%%/size 18/size 18) size 10 
d ----------------------------------------------
d
* continue
d Text following the (%%/color blue/\(%%) component
d reverts to the initial (%%/color blue/ /size 10/ ) value.
c
d /size 10/size 10 (%%/size 18/size 18) size 10 


c 
d 5/3/The 'd' command component to display
d the contents of a $variable begins 
d with the tag (%%/color blue/\(%) followed by a
d (%%/color blue/$variable), for example:
d
d 10/------------------------
d d \(% $name)
d ------------------------
d
* continue
d 5/The text size can be altered with
d the (%%/color blue/size) argument, for example:
d
d 10/-----------------------
d d \(% /size 24/ $name)
d ----------------------
a $size22=size is 22
a $size10=size is 10
a $size14=size is 14
a $size18=size is 18
c
d 5/3/The next CardSet executes:
d 10/
d -------------------------
d d \(% /size 22/ $size22)
d d \(% /size 10/ $size10)
d d \(% /size 14/ $size14)
d d \(% /size 18/ $size18)
d -------------------------
d
* continue
d 5/
d $size22 contains the text 'size is 22'
d $size10 contains the text 'size is 10'
d and so on...

c
d  (% /size 22/ $size22)
d  (% /size 10/ $size10)
d  (% /size 14/ $size14)
d  (% /size 18/ $size18)

c
d 5/3/The size of the 'd' command text
d and the size of the $variable
d can be different as shown by the
d following script:
d ---------------------------------------------------
d d /size 10/size 10 \(% /size 22/ $size22) size 10
d d /size 22/size 22 \(% /size 11/ $size10) size 22
d d /size 18/size 18 \(% /size 14/ $size14) size 18
d d /size 14/size 14 \(% /size 18/ $size18) size 14
d ---------------------------------------------------
c
d /size 10/size 10 (% /size 22/ $size22) size 10
d /size 22/size 22 (% /size 11/ $size10) size 22
d /size 18/size 18 (% /size 14/ $size14) size 18
d /size 14/size 14 (% /size 18/ $size18) size 14

c
d 5/3/The 'd' command component using the
d tag (%%/color blue/\(%%) also allows special 
d treatment of text, for example:
d 1/
d --------------------------------------------------
d d /size 10/one\(%%/size 14/two)\(%%/size 18/three)
d d /size 18/one\(%%/size 14/two)\(%%/size 10/three)
d --------------------------------------------------

c
d /size 10/one (%%/size 14/two) (%%/size 18/three)
d /size 18/one (%%/size 14/two) (%%/size 10/three)

* end
