class A {

   var : Int <- 0;

   value() : Int { var };

   set_var(num : Int) : Int {
      {
         var <- num;
         var+1;
      }
   };
};   


class Main inherits IO {
   
    id : Int <- 0 ;
    avar : Int ;
    
    main() : Object {
      {
	    out_string("Rule violated is: expr[@TYPE].ID([expr[,expr]*])");
	    avar <- (new Int)@.set_var(id) ;
	    }
    };
};
