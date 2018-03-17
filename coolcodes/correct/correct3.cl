--CS15BTECH11024
--take a number as input and says if it is prime or not
(* c code: 
   for(i=2; i<=s/2; i++){
      if(s%i==0){
         flag=1;
         break;
      }
   }
*)

class Main inherits IO {
   flag : Bool <- true;
   i : Int <- 2;
   rem : Int <- 0;
   done : Int <- 0;
   
    prime(s : Int) : Bool {
    {
    while 
      if i <= s/2 then         --checks i<=s/2 and flag=1 
         if 0 = done then
            true
          else
            false
          fi    
      else false
      fi
      loop   --loops until predicate is false
	    {
	    
	    rem <- s - (i * (s/i));  --s%i
	    if rem = 0 then {
         done <- 1;
         i <- s;
         }
       else i <- 1 + i   --i++
       fi;    
        
             
	    }pool;
	
	 if done = 0 then false --not prime
	 else true              --prime
	 fi;
	 
	 }
};

   
   
    main() : SELF_TYPE {
	{
         
	    out_string("enter a number\n");
	    (let size :Int in
	    {
	    size <- in_int();       --input 
	    if 1 < size  then 
	       if prime(size) then
	         out_string("not prime\n")   --result
	       else out_string("prime\n")
	       fi
	    else out_string("not a prime\n")
	    fi ;
	    self;
	    }
	    );
	}
    };  --end of main
};--end of program

