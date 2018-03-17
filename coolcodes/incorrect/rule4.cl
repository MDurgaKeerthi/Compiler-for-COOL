--section 10.4
--Except for the constants true and false, keywords are case insensitive.
--the first letter of true and false must be lowercase; the trailing letters may be upper or lower case.

cLaSs Main inherits IO {
   check(s : Int) : Bool {
	If 0 < s
	then tRUE
	else False   --error is at False
	fi
    };


    main() : SELF_TYPE {
	{
	    out_string("enter number\n");
	    if check(in_int())
	    then out_string("positive\n")
	    else out_string("non-positive\n")
	    fi;
	}
    };
};
