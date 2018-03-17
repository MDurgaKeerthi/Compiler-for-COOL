class Main {
	b:Int<-10;
	a:Int<-2;
   f:Int;
   
	main():Int {
	{   
		new IO.out_string("While loop will start\n");
		while(0<b) loop
			{
				b <- b-1;
				new IO.out_int(b);	
				new IO.out_string("\n");		
			}
		pool;
		new IO.out_string("While loop ended\n");
		0;
	}
	};
};
