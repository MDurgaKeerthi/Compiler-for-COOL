--CS15BTECH11024
--takes a number as input and prints a pyramid of that size
(*c code:
   for(i=1; i<=rows; ++i, k=0){
      for(space=1; space<=rows-i; ++space)
         printf("  ");
      while(k != 2*i-1) {
         printf("* ");
         ++k;
      }
      printf("\n");
    }
*)



class Main inherits IO {
   
   i : Int <- 1;
   space : Int <- 1;
   k : Int <- 0;
   
    pyramid(rows : Int) : SELF_TYPE {
    {
    while             --outer for loop in the above code
      if i <= rows 
      then true
      else false
      fi
      loop{
      
	    k <- 0;
	    space <- 1;
	     while         --inner for loop
         if space <= rows-i 
         then true
         else false
         fi
         loop
	       {
	       
	       out_string("  "); 
          space <- space + 1;      
	       }
	    pool;
	    
	    while               --inner while loop
         if k < 2*i-1 
         then true
         else false
         fi
         loop
	       {
	       out_string("* ");   --prints pattern
	       k <- k+1;    
                
	       }
	    pool;
	    
	   out_string("\n");   --new line
	   i <- i+1;    
      }       
	    
	 pool;
	 self;
	 }
	 
    };   --end of pyramid function

   
   
    main() : SELF_TYPE {    --start of main
	{
         
	    out_string("enter a number\n");
	    (let size :Int in
	    {
	    size <- in_int();       --taking input
	    if 0 < size  then 
	      pyramid(size)          --calling function
	    else out_string("positive number needed\n")
	    fi ;
	    self;
	    }
	    );
	}
    };   --end of main
};  --end of program

