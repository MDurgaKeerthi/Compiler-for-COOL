--rule violated: formal : ID : TYPE

class Main inherits IO {
   
    id :  ;  --error

    main() : SELF_TYPE {
	    out_string("I am a program with error in formal");
    };
};
