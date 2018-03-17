class Main {
	b:Int<-3;
   f:Int;
   c:String <- "good";
	d:String;
	h: Bool <- true;
	main():Int {
	{   
		
		(new IO)@IO.out_string(c);
		new IO.out_string("\n");
		new IO.out_int(b);
		
		new IO.out_string("enter a string: ");
		d <- new IO.in_string();
		new IO.out_string(d);
		
		new IO.out_string("\nenter an integer: ");
		f <- new IO.in_int();
		new IO.out_int(f);
		
		d<-"coffee";
		c<-"choco";
		--f<-71;
		--b<-8;
		
		new IO.out_string("\nResult of ~\n");
		b<- ~f;
		new IO.out_int(b);
		
		new IO.out_string("\nResult of not expr\n");
		h <- not h;
		if h then
			new IO.out_string("ok\n")
		else
			new IO.out_string("okay one\n")
		fi;
		
		--new IO.out_string(c);
		--new IO.out_string(d);
		
		
		
		6;
		}
	};
};
