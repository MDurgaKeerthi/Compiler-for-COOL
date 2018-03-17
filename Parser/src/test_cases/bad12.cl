class Main inherits IO {
   p1 : Int;
   
	main() : Object {
		func(p1,)   --"rule violated is ID([expr[,expr]*])"
	};

	func(i : Int) : Int {
		let num : Int <- 1 in {
		   i <- 2;	
		}
	};
};
