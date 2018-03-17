--rule violated : while expr loop expr pool

class Main inherits IO {
   
    id : Int <- 0 ;

    main() : Object {
	    (let y : Int <- 1 in
	       while  loop   --error 
	          {
	             out_string("No expression in while structure");
	             y <- y + 1;
	          }
	       pool
	    )
    };
};
