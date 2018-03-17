class Main inherits IO {
   flag : Bool <- true;
   i : Int <- 2;
   rem : Int <- 0;
   done : Int <- 0;
   size :Int;
	    
	    
    prime(s : Int) : IO {
    {
    while 
      if i <= s/2 then
         if 0 = done then
            true
          else
            false
          fi    
      else false
      fi
      loop
	    {
	    
	    rem <- s - (i * (s/i));
	        if rem = 0 then {
               done <- 1;
               i <- s;
               }
            else 
               i <- 1 + i 
            fi;    
        
             
	    }
	 pool;
	
	 if done = 0
	 then out_string("prime\n")
	 else out_string("not prime\n")
	 fi;
	 }
    };

   
   
    main() : Int {
	{
         
	    out_string("enter a number\n");
	    
	    size <- in_int();
	    if 1 < size  then 
	     	prime(size) 
	    else out_string("not a prime\n")
	    fi ;
	    0;
	    
	}
    };
};

