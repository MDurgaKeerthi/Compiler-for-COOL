class Main {
	b:Int<-3;
	a:Int<-2;
    f:Int;
    c:String <- "It's me\n";
	d:String <- "hhh";
	
	main():Int {
	{   

		--d <- new IO.in_string();
		new IO.out_string(c);

		(new A)@A.fun1(c);
		(new A)@A.fun1("I came to fun1 to classA from main\n");
		new IO.out_string("Hell0 world! it's me again\n");
		
		6;
		}
	};
	
};

class A{
	
	fun1(a:String):Int {
          {
		
		new IO.out_string(a);
		10;
		
         }
        };
};


