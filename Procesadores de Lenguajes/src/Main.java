import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException  {

		ASint.execASint();

//		System.out.println(ASint.LL1());

		GestorErrores.toFile();
		Parse.toFile();

		
		
		
//		System.out.println(ASint.follow("F"));
//
//		for (String s : ASint.gram.keySet()) {
//			System.out.println("First (" + s + ") " + ASint.first(s));
//			System.out.println("Follow (" + s + ") " + ASint.follow(s));
//		}

	}
	
}
