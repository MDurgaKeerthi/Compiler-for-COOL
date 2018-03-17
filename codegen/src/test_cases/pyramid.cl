class Main inherits IO {
   
   i : Int <- 1;
   space : Int <- 1;
   k : Int <- 0;
   size :Int;

    pyramid(rows : Int) : Int {
    {
    while 
      if i <= rows 
      then true
      else false
      fi
      loop{
      
	    k <- 0;
	    space <- 1;
	     while 
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
	    
	    while 
         if k < 2*i-1 
         then true
         else false
         fi
         loop
	       {
	       out_string("* "); 
	       k <- k+1;  
                
	       }
	    pool;
	    
	   out_string("\n"); 
	   i <- i+1;    
      }       
	    
	 pool;
	 0;
	 }
	 
    };

   
   
    main() : Int {
	{
         
	    out_string("enter a number\n");
	    
	    size <- in_int();
	    if 0 < size  then 
	      pyramid(size) 
	    else out_string("positive number needed\n")
	    fi ;
	    0;
	    
	    
	}
    };
};

