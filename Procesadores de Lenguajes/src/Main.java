import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		ALex alex = new ALex();

		Pair<String, String> token = new Pair<String, String>("","");

		Tokens.clearFile();
		
		while (token.getLeft()!="$") {
			token = ALex.execALex();
			System.out.println(token.getLeft() + " " + token.getRight());
		}
		
		ALex.toFileGE();
		ALex.toFileTS();

		ALex.closeFile();
		
		// ASint.execASint();

	}

}
