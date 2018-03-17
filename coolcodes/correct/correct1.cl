--CS15BTECH11024
--takes a number as input and prints that many fibonacci terms in order


class Main inherits IO {
   
   i : Int <- 1;
   nxt : Int ;
   size : Int;
   
   fib(prev : Int, num : Int) : Int {
    
      if i = size then 0   --base case
      else {
         nxt <- prev + num;  --calculating fib number
         prev <- num;
         num <- nxt;
         out_int(nxt);       --printing fib number
         out_string("  ");
         i <- i+1;
         fib(prev, num);     --going on recursion to base case
         }
       fi      
	 
    };  --end of fib function

   
   
    main() : SELF_TYPE {  --main fucntion
	{
         
      out_string("enter a number of fibonacci terms needed\n");
	   
	   size <- in_int();
	   if 0 < size  then{ 
	      out_string("1  "); --printing first fib
	      fib(0,1);          --calling fib with initial values
	      out_string("\n");
	      } 
	   else out_string("positive number needed\n")
	   fi ;
	   self;
	    
	   }
    };  --end of main
}; --end of program

