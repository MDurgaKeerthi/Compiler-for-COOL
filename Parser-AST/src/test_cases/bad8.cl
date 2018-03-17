--rule violated : case expr of [ID:TYPE => expr;]+ esac

class Main inherits IO {
   
    id : Int <- 0 ;

    main() : SELF_TYPE {
	    case id of
	      a : Int => out_string("This case structure will be wrong because I am not going to put semicolon")  --error here
      esac
    };
};
