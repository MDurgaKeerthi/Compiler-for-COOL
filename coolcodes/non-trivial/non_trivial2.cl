--simple calculator


class Main inherits IO {
    
   num2 : Int;                  --required variables
   res : Int;    
   oper : String;
   continue : Bool <- true;
   i : Int;
   pow : Int;
   
   power(num2 : Int) : Int {           --function that calculates power of res
   {
   i <- 0;
   pow <- 1;
   while i < num2 loop {
      pow <- res * pow;
      i <- i + 1;
      }pool;
      
      pow;
      }
    };
   
   printres() : SELF_TYPE {         --prints res
   {
      out_string("result is ");
	   out_int(res);
	   out_string("\n");
	   self;
	}
	};
   
   
   
    main() : SELF_TYPE {            --start of main
	{
	   out_string("calculator-----\n");
      out_string("+,-,*,/(quotient), %(remainder), ^(power), r(reset) operators are allowed, any other results in exit\n"); 
       
        
	    out_string("enter a number\n");
	    res <- in_int();
	    
	    out_string("From now on, enter operator number follwed by second operand\n");
	    while continue loop {  --starting of calculator
	    
	    oper <- in_string();
	    
	    if oper = "+" then {     --addition
	      num2 <- in_int();
	      res <- res + num2;
	      printres();
	      }
	    else if oper = "-" then {   --subtraction
	      num2 <- in_int();
	      res <- res - num2;
	      printres();
	      }
	    else if oper = "*" then {   --multiplication
	      num2 <- in_int();
	      res <- res * num2;
	      printres();
	      } 
	    else if oper = "/" then {    --division
	      num2 <- in_int();
	      if 0 = num2 then {
	         out_string("invalid operation\n");
	         continue <- false ;
	         }
	      else {   
	         res <- res / num2;
	         printres();
	         }
	      fi;   
	      }
	    else if oper = "%" then {   --modulus
	      num2 <- in_int();
	      if 0 = num2 then {
	         out_string("invalid operation\n");
	         continue <- false ;
	         }
	      else {   
	         res <- res - (res/num2 * num2);
	         printres();
	         }
	       fi;  
	      }
	    else if oper = "^" then {   --exponential
	      num2 <- in_int();
	      if 0 = res then{
	         out_string("invalid operation\n");
	         continue <- false ;
	         }
	      else {   
	         if 0 = num2 then res <- 1
	         else res <- power(num2)
	         fi;
	         printres();
	         }
	      fi;   
	      } 
	    else if oper = "r" then {      --reset
	      res <- 0;
	      printres();
	      }          
	    else {                          --end of operations
	      out_string("invalid operator\n");
	      continue <- false ;
	      }
	    fi fi fi fi fi fi fi;    --if else...
	    
	    }pool;        --end of while
	    self;
	    
	   }
    };  --end of main
};  --end of program

