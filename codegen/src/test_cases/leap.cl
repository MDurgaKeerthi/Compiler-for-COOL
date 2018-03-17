class Main inherits IO {
   
   size : Int <- 1;
   rem1 : Int <- 1;
   rem2 : Int <- 1;
   rem3 : Int <- 1;
   temp : Int <- 1;
   
   getrem(s : Int) : Int {
   size - (size/4 * 4)
   
    };
   
   
    main() : Int {
	{
         
	    out_string("enter a number\n");
	  
	    size <- in_int();
	    rem1 <- getrem(4);
	    rem2 <- getrem(100);
	    rem3 <- getrem(400);
	    
	    if rem1 = 0  then 
	      if rem2 = 0 then
	         if rem3 = 0 then
	            out_string("leapyear\n")
	         else out_string("not a leapyear\n")
	         fi
	      else out_string("leapyear\n")  
	      fi 
	    else out_string("not a leapyear\n")
	    fi;
	    0;
	}
    };
};

(*

if((year%4 == 0 && year%100 != 0 )||(year%400 == 0){
      printf("leap year\n");
   else
      printf("not leap year\n");        
    *)
