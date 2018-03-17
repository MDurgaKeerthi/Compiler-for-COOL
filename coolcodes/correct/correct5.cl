--CS15BTECH11024
--takes two strings and checks if latter is substring of first

class Main inherits IO {
   str : String;
   str1 : String;
   i : Int <- 0;
   j : Int <- 0;
   len1 : Int;
   len : Int;
   checked : Int;
   continue : Bool;
   
   
   main(): SELF_TYPE {              --start of main
   {
   out_string("Enter string1: ");   --input of two strings
   str <- in_string();
   out_string("Enter substring to find: ");
	str1 <- in_string();
	
	len <- str.length() - 1;       --length of strings
	len1 <- str1.length() - 1;
	
	if len < len1 then continue <- false  --if latter is longer
	else continue <- true
	fi;
	   
	while continue loop{  --checking if substring
	   
	   if str.substr(i,1) = str1.substr(j,1) then {   --  if str[i] == str[j] 
	      i <- i+ 1;
	      checked <- 1;                               -- checked  
	      if j = len1 then continue <- false          --reached end of string2
	      else {
	         j <- j+1;                                -- continuing further if prefix matches 
	         checked <- 0;
	          }
	      fi;
      }
	   else i <- i+ 1                        --keep incrementing i till you find first char of second string 
	   fi;
	
      if i = len then continue <- false              --reached end of string2
	   else continue <- true
	   fi;
	
	}pool;                                           --end of while                       
	
	
	if j = len1 then                                --printing result second string will be part of first only j reaches end of it
	   if 1 = checked then out_string("yes\n")
	   else out_string("no\n")
	   fi
	else  out_string("no\n")
   fi;
	
	self;
	}
   };          --end of main
};             --end of program
