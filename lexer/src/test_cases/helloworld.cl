-- just string "\0" -> "0"

class Main {
	main():IO {
		new IO.out_string("Hello\t\" world!\0$%^\n")
	};
};
