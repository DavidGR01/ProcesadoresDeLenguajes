import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		Tokens.clearFile();

		ASint.execASint();

		//ASint.LL1();

		ALex.toFileGE();
		ALex.toFileTS();

		Parse.toFile();

		ALex.closeFile();

		for (String s : ASint.gram.keySet()) {
			System.out.println("First (" + s + ") " + ASint.first(s));
			System.out.println("Follow (" + s + ") " + ASint.follow(s));
		}

	}

}
