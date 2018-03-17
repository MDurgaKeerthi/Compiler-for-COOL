--section 10.5

class Main inherits IO {

    main() : SELF_TYPE {
	{
         --whitespace consists of any sequences of \n,\f,\r,\t,\v
	    out_string(" \n\f\r\t\v ");
	}
    };
};
