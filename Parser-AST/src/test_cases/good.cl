(*this is the program which contains all possible statements in correct format from figure1 of cool syntax *)


class Main inherits IO {   --program contains class
   p1 : Int <-1;           --class contains features, also inherits 
   p2 : Int ;              --formal
   bool : Bool ;
   bool1 : Bool ;
   int : Int;
   str: String;
   
	main() : Object {        --a feature
		func(p1)              --rule: ID([expr[,expr]*])    
	};

	func(i : Int) : Int {    -- a feature
	{                        --rule : { [expr]+}    
	   --rule: if expr then expr else expr fi
	   if i<0 then out_string("I am negative\n") else out_string("I am not negative\n") fi;
	  
	   --rule: while expr loop expr pool 
	   while i<=5 loop {
	      i <- i+1;
	      }pool;
	   
	   --rule: let ID : TYPE [ <- expr] [,ID : TYPE [ <- expr ]]* in expr   
		(let num : Int <- 1 in {
		   i <- 2;	
		} );
		
		(let a : Int, b:Int  in {
		   a <- 2;	
		} );
		
		--rule: case expr of [ID : TYPE => expr;]+ esac
		case i of 
		   a : Int => out_string("I am Int\n");
		   esac;
		  
		isvoid p2;                --rule : isvoid expr  
		 
		int <- int + 1;           --rule : expr + expr          
		int <- int - 1;
		int <- int * 1;
		int <- int / 1;
		int <- ~int;             --rule : ~expr    
		int <- (0);              --rule : (expr) , rule : Integer    
		str <- "i am string";    --rule : string
		bool <- true;            --rule : bool  
		
		bool1 <- not bool;       --rule : not expr  
		new Int;                 --rule : new TYPE  
		}
	};
};




