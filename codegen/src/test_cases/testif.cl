class Main {
	b:Int<-3;
	a:Int<-2;
    f:Int;
	main():Int {
	{   
		new IO.out_string("1.add, 2.sub, 3.mul, 4.div\n");
		
		f <- new IO.in_int();
		
		if(f=1) then 
			new IO.out_int(a+b)
		else
			if(f=2) then 
				new IO.out_int(a-b)
			else
				if(f=3) then 
					new IO.out_int(a*b)
				else
					if(f=4) then 
						new IO.out_int(a/b)
					else
						new IO.out_string("Oops! Invalid choice\n")
					fi
				fi
			fi
		fi;
			
		0;
	}
	};
};
