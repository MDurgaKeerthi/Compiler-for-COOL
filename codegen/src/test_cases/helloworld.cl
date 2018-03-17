class Main {
	b:Int<-3;
	a:Int<-2;
    f:Int;
    c:String <- "good";
	d:String <- "hhh";
	
	main():Int {
	{   
		new IO.out_string("enter a string: ");
		d <- new IO.in_string();
		new IO.out_string(c);

		if b<(1) then
		{	a<-3;
			new IO.out_int(10);
		}
		else 
		{	a<-9;
			new IO.out_string("elsebody\n");
		}
		fi;	
		
		while (b=3) loop
		{
			b<-b+1;
		}
		pool;	
			
		fun(5);
		(new B)@A.fun1(c);
		new IO.out_string("Hell\n world!");
		6;
	}
};
	fun(a:Int):Int {
          {
	
		new IO.out_int(a);
		7;
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

class B inherits A{
	d:Int;
	fun2(d:Int):Int {
          {
		--a<-10;
		--fun1(2);
		new IO.out_string("in fun2");
		8;
          }
          
          
          
        };
};

