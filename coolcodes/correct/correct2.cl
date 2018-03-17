--CS15BTECH11024
--takes input and checks if it's a leap year
(* c code: 
   if((year%4 == 0 && year%100 != 0 )||(year%400 == 0){
      printf("leap year\n");
   else
      printf("not leap year\n");     
      *)

class Main inherits IO {
   
   size : Int <- 1;
   rem1 : Int <- 1;
   rem2 : Int <- 1;
   rem3 : Int <- 1;
   temp : Int <- 1;
   
   getrem(s : Int) : Int {   --function that returns remainder on dividing size with arguement s
      size - (size/s * s)
    };
   
   
    main() : SELF_TYPE {
	{
         
	    out_string("enter a number\n");
	    size <- in_int();
	    if 0 <size then {
	    rem1 <- getrem(4);
	    rem2 <- getrem(100);
	    rem3 <- getrem(400);
	    
	    if rem1 = 0  then   --year%4
	      if rem2 = 0 then  -- year%100
	         if rem3 = 0 then  --year%400
	            out_string("leapyear\n")
	         else out_string("not a leapyear\n")
	         fi
	      else out_string("leapyear\n")  
	      fi 
	    else out_string("not a leapyear\n")
	    fi;
	   }
	   else out_string("positive number needed\n")
	   fi;
	   
	   }
    };  --end of main
};
--end of program

