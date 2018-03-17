--rule violted : ID([formal [, formal]*] ) : TYPE { expr }

class Main inherits IO {
   
   func(int : Int) : Bool ;  --error

    main() : SELF_TYPE {
	    out_string("I am a program with error in the feature rule");
    };
};
