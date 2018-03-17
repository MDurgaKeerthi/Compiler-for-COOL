--program for finding a power b
class Main inherits IO {
    
    x : Int;                            --numbers  be taken as input
    y:  Int;
    c: Int <-1;           --a power b is set to 1  initially
    i: Int <-1;
    res: Int;                --integer that is returned  the function

    --function for calculating a power b
    exponent(a : Int, b : Int) : Int {                --takes two integer arguments and returns an integer
  
       {

                  while i<=b loop
	          {
                     c <- c * a;                             --multiplying a b number of times to get a power b
	             i <- i + 1;             --incrementing the counter
                     
	          }
                
	 (*missing assignment operator*)     
     res  c;   --storing the value of a power b in the return value

}


 };


    main() : SELF_TYPE {
	{
	    out_string("Enter the first number which is positive\n");
            x <- in_int();                                     --taking first number as input
            out_string("Enter the second number which is positive\n");
            y <-in_int();                                      --taking second number as input
            out_string("The value of the power is: \n");
            out_int( exponent(x,y));                             --calling the function  calculate the value of a power b
            out_string("\n");
	}
    };
};

