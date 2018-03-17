# COOL Compiler #

Design
Lexer rules are regular expresions. we can even call a function on what to do on some expression for example incase of strings. Most of them are simple case matching or just symbol matching rules for example all of the operators like +,-, etc and keywords. INT_CONSTANTS cover range of 0-9, BOOL_CONSTANTS are just false and true. For string constants, we do pattern matching and then call the function and check for length, EOF, and set the string such that whitespace characters are inserted properly.

Testcases:
hello1.cl - shows invalid characters are unacceptable
hello2.cl - shows error for unterminated string
hello3.cl - simialr to hello2.cl but involves different example
hello4.cl - demonstrate what happens if string contains null charcter and EOF
hello5.cl - demonstrate nested comments, EOF in comments, Unmatched comment
hello6.cl - demonstrates what happens if string length is beyond range
helloworld.cl - demonstrates how "\0" is accepted

These examples cover all possible errors as given in assignment problem or cool manual
section 10 Lexical Structure Each of those rules are demonstrated. Strings in examples hello2.cl, hello3.cl, hello4.cl, hello6.cl
Comments in hello5.cl and so on

 

