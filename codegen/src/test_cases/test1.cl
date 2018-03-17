class Main {
	b:Int<-3;
	a:Int<-2;
    f:Int;
	main():Int {
	{   f<-a+b/0;
		new IO.out_int(f);
		0;
	}
	};
};
