c
d 20/2/Edit Command
d 5/
d Suppose the correct response for the following
d 'd' command is (%%/color blue/cat)
d 10/------------script-------------------
d d Enter name of the animal \(# $input)
d ----------------------------------------
d 5/
d And we do not want to procede until (%%/color blue/cat)
d is entered.
* continue
d
d The (%%/color blue/Edit) or (%%/color blue/e) command requires
d the response to have a definite form, 
d such as, (%%/color blue/cat).



c
d 5/2/The Edit or (%%/color blue/e) command controls what
d the user may enter as a response or input.
d
d 10/------------script-------------------
d d Enter name of the animal \(# $input)
d e ($input)=(cat)
d ---------------------------------------
d 5/
d The above 'e' command causes any response
d other than (%%/color blue/cat) to be rejected.
d
* continue
d When rejected, The response field is cleared. 
d The user must  enter (%%/color blue/cat) to proceed 
d to the next CardSet.


c
d 10/3/------------script---------------
d
d d Enter cat \(# $animal)
d e ($animal)=(cat)
d
d -----------------------------------------
d
d 25/size 10/First enter 'xxx' to fail.
d 25/size 10/Then end 'cat' to succeed.
d 5/
d Enter cat (# $animal)
e ($animal)=(cat)
x
d
d
d Success


c
d 10/3/------------script---------------
d
d d Enter  \(# $animal)
d e ($animal)=(cat)
d
d -----------------------------------------
d 5/
d The previous script:
d        d Enter cat ...
d has been changed to
d        d Enter ...
d 5/The new script has no clue as to the 
d correct response.



c
d 5/2/The following 'e' command has a (%%/color blue/status=)
d tag specifying the required response.
d
d 10/--------------script------------------------
d d Enter animal name \(# $animal)
d e ($animal)=(cat) (%%/color blue/status=)enter cat
d -----------------------------------------------
d 5/
d The message follows the tag (%%/color blue/status=)
d consisting of 'enter cat'.

c
d 5/2/
d 20/size 10/First enter 'xxx' to fail.
d /size 10/Then enter 'cat' to succeed.
d 5/
d Enter animal name (# $animal2)
e ($animal2)=(cat) status=enter cat

c
d 5/2/In place of a logic expression, the 'e' 
d command may have a literal. There are two 
d literals:
d 10/(%%/color blue/letter) 
d (%%/color blue/number).
d
d 10/----------script-------------------------
d d Enter name \(# $name) 
d e (%%/color blue/letter)
d d Enter age  \(# $age)
d e (%%/color blue/number)
d --------------------------------------------
* continue
d 5/
d The (%%/color blue/e number) also allows a decimal '.'
d The (%%/color blue/e letter) also allows spaces '  '
d
d The default statuse for 'e number' is:
d 10/number required
d 5/The default statuse for 'e letter' is:
d 10/letter (non numeric) required

d 
c
d 10/3/-------------script----------------
d d Enter age \(# $age)
d e number
d Enter name \(# $name)
d e letter 
d --------------------------------------
d 5/
d 25/size 10/First enter letter for age.
d 25/size 10/First enter number of name
d 5/
d Enter age (# $age)
e number 
d Enter name (# $name) 
e letter

c
d 5/2/A field expression of the 'd' command may
d have more than one related 'e' commands.
d
d For example, age must be a numeric value,
d but not less than 1 or greater than 100.
d
d The following script will achieve this:
* continue
d
d 10/--------------------script------------------------
d d Enter age \(# $age)
d e number  
d e ($age)>(0)and($age)<=(100) status=1 to 100 
d -------------------------------------------------
d 5/
d The next CardSet executes the above script
c
d 5/2/
d 
d --------------------script------------------------
d d Enter age \(# $age)
d e number  status
d e ($age)>(0)and($age)<=(100) status=1 to 100 
d -------------------------------------------------
d 
d 20/size 10/Enter 200 to view status message.
d
d Enter age (# $age2)
e number  status=numbers only
e ($age2)>(0)and($age2)<=(100) status=1 to 100 
d
d Success 

c
d 5/3/The 'e' command examples showed
d a 'd' command, with one InputField,
d followed by a 'e' command.
d
d 10/---------script-------------
d 10/d Enter cat \(# $cat)
d e ($cat)=(cat) status=Enter cat
d ------------------------------
d 5/Both commands referenced the same 
d $cat variable.
* continue
d
d A 'd' command may have multiple
d Input Fields, for example:
d
d d \(# $first) \(# $second) \(# $third)
* continue
d
d Referencing a particular $<variable> 
d presents a problem.

c
d 5/3/$<variable> referencing is handled by
d beginning the 'e' command with the
d associated $<variable>, for example:
d
d ---------------script----------------------
d d \(# $first) \(# $second) \(# $third)
d e $first ($first)=(1) status=1 required
d e $second ($second)=(2) status=2 required
d e $third ($third)=(3) status=3 required
d --------------------------------------------
d
* continue
d
d The next CardSet executes the above.

c
d 5/2/
d ---------------script----------------------
d d \(# $first) \(# $second) \(# $third)
d e $first ($first)=(1) status=1 required
d e $second ($second)=(2) status=2 required
d e $third ($third)=(3) status=3 required
d --------------------------------------------
d
d 20/size 10/Make it fail before success
d
d (# /length 4/$first) (# /length 4/$second) (# /length 4/$third)
e $first ($first)=(1) status=1 required
e $second ($second)=(2) status=2 required
e $third ($third)=(3) status=3 required

c
d 5/3/Does the order matter?
d It should not since the $<varuabke> locks it in. 
d
d ---------------script----------------------
d d \(# $first) \(# $second) \(# $third)
d e $third ($third)=(3) status=3 required
d e $first ($first)=(1) status=1 required
d e $second ($second)=(2) status=2 required
d --------------------------------------------
d
d 20/size 10/Make it fail before success
d
d (# /length 4/$first2) (# /length 4/$second2) (# /length 4/$third2)
e $third2 ($third2)=(3) status=3 required
e $first2 ($first2)=(1) status=1 required
e $second2 ($second2)=(2) status=2 required



c
d 5/3/Referencing the $<variable> in the Edit
d command also works when the 'd' command
d has a single Input Field.
d
d 10/--------------script--------------------
d d \(# $one)
d e $one ($one)=(1)
d d \(# $two)
d e $two ($two)=(2)
d d \(# $three)
d e $three ($three)=(3)
d -------------------------------------------
d 5/
d (# $one)
e $one ($one)=(1)
d (# $two)
e $two ($two)=(2)
d (# $three)
e $three ($three)=(3)

c
d 5/3/Edit commands are grouped together
d and in mixed order.
d
d 10/--------------script--------------------
d d \(# $one)
d d \(# $two)
d d \(# $three)
d e $three ($three)=(3)
d e $one ($one)=(1)
d e $two ($two)=(2)
d -------------------------------------------
d 5/
d (# $one)
d (# $two)
d (# $three)
e $three ($three)=(3)
e $one ($one)=(1)
e $two ($two)=(2)


* end

