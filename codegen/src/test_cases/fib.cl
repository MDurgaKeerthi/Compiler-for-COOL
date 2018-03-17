class Main inherits IO {
   
   i : Int <- 1;
   nxt : Int ;
   
   size : Int;
   
    fib(prev : Int, num : Int) : Int {
    
    
    if i = size then
      0
    else {
      nxt <- prev + num;
      prev <- num;
      num <- nxt;
      out_int(nxt);
      out_string("  ");
      i <- i+1;
      fib(prev, num);
      }
    fi      
	 
    };

   
   
    main() : Int {
	{
         
	    out_string("enter a number\n");
	   
	    size <- in_int();
	    if 0 < size  then{ 
	      out_string("1  ");
	      fib(0,1);
	      out_string("\n");
	      } 
	    else out_string("positive number needed\n")
	    fi ;
	    0;
	    
	}
    };
};

