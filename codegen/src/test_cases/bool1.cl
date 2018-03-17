class Main {
	b:Bool;
	a: Int <-10;
	main():Int {
	{   b<-true;
		while(b) loop{
			new IO.out_int(a);
			a <- a-1;
			if a=0 then
				b<-false
			else
				b<- true
			fi;		
		}
		pool;
		0;
	}
	};
};
