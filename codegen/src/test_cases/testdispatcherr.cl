class Main {
	b:Int;
	a: A<-new A;
	main():Int {
	{   

		a@A.fun1();
		b<-4;
		0;
	}
	};
};
	
class A{
	a:C;
	c:Int;
	fun1():Int {
          {
		a@C.fun1();
		new IO.out_string("in fun");
		10;
		
         }
       };

};

class B inherits A{
	u:Int;
	fun2():Int {
          {
		a <- new C;
		
		new IO.out_string("in fun");
		10;
		
         }
       };

};

class C{
	c:Int;
	fun1():Int {
          {
		
		new IO.out_string("in fun");
		10;
		
         }
       };

};
