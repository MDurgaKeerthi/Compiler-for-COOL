class Foo {
         (* missing esac and a:b should be a:B i.e id:type*)
	bar(a:Int,b:Int):Int{case a of a:b=>out_string("hello"); };
};
