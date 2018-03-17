--stack using list implementation



class List {              --base list class for empty list
   first : Int <- 0;	     --head element 

   head()  : Int {first };     --gets head function
   tail()  : List { self };    --gets tail
   add(i : Int) : List {         --uses inheritance to add to itself
      (new Cons).init(i, self)
   };

};


class Cons inherits List {
   tail_list : List;	         -- The tail of the list
   
   tail()  : List { tail_list };
   init(i : Int, rest : List) : List {    --adding element
   {
	    first <- i;
	    tail_list <- rest;
	    self;
       }
   };

};   --end of inheritance class


class Main inherits IO {                --main class

   stack : List <- new List;              --required variables
   size : Int <- 0; 
   printed : Int <- 0;
   choice : Int ;
   num : Int;
   continue : Bool <- true;
   
   pop() : SELF_TYPE {                       --pop function of stack
   {
      num <- stack.head();
	   size <- size - 1;
	   stack <- stack.tail();                    --popping
	   out_string("Number popped is ");
	   out_int(num);
	   out_string("\n");
      self;
      }
   };
   
   print_list(l : List) : Object {                    --prints stack on recursion
      if size = 0 then out_string("Stack is empty\n")
      else if printed = size then out_string("\n")
      else {
			   out_int(l.head());
			   out_string("  ");
			   printed <- printed + 1;
			   print_list(l.tail());
		        }
      fi fi
      
   };

  
   main() : Object {                --start of main function
      {
      out_string("---STACK----\n"); 
      
      while continue loop {         --start of operations
	    out_string("1.Push   2.Pop   3.View stack   4.Quit\n");  
	    choice <- in_int();
	    
	    if choice = 1 then {                        --push
	      out_string("Enter the number to push: ");
	      num <- in_int();
	      stack <- stack.add(num);
	      size <- size + 1;
	      }
	    else if choice = 2 then                           --pop
	      if size = 0 then out_string("no elements to pop\n")
	      else pop()
	      fi
	    else if choice = 3 then {                      --print
	      printed <- 0;
	      print_list(stack);  
	      }
	    else if choice = 4 then {                      --end
	      out_string("The end\n");
	      printed <- 0;
	      continue <- false ;
	      }
	    else out_string("Enter valid index\n")  
	    fi fi fi fi;
	    
	    }pool;           --end of while loop
    
      }
   };       --main of main 

};
--end of program


