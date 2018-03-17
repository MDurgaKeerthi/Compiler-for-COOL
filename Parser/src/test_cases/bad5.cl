--rule vioalted : if expr then expr else expr fi

class Main inherits IO {
   
    id : Int <- 0 ;

    main() : SELF_TYPE {
	    if id = 0 then out_string("If is fine but")
	    else out_string("Else is not because there is no fi")   --error
    };
};
