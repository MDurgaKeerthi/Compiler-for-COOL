class Main inherits IO {
   
    id : Int <- 0 ;

    main() : Object {
	    (let y : Int <- 1 in
	       while y<=0 loop
	          {  }    -- "{[expr,]+} rule is violated"
	       pool
	    )
    };
};
