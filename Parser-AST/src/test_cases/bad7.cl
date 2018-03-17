--rule violated:let ID : TYPE [<-TYPE] [, ID:TYPE[<-expr]]* in expr

class Main inherits IO {
	main() : Object {
	   out_int(func(in_int()))
	};

	func(i : Int) : Int {
	   {
	   out_string("I am demonstrating grammatically wrong let structure");
		let num : Int <- 1 in ;  --error 
		}
	};
};
