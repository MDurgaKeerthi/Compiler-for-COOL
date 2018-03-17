class Main {
	b:Int<-3;
	a:Int<-2;
   f:Int;
    
   changea(chan: Int): Int {
   {
   	a <- a + chan ;
   	0;
   	}
   };
    
	main():Int {
	{   

		new IO.out_string("Enter the num to be added");
				
		f <- new IO.in_int();
	    
		changea(f);
		
		new IO.out_int(a);
			
		0;
	}
	};
};
