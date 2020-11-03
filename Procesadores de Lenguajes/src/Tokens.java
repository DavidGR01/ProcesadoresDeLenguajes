import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tokens {

	private static List<Pair<String, String>> tokens = new ArrayList<>();

	static void generarToken(Pair<String, String> token) {
		tokens.add(token);
	}

	static void toFile() throws IOException {

		FileWriter myWriter = new FileWriter("tokens.txt");
		for (Pair<String, String> token : tokens)
			myWriter.write("<" + token.getLeft() + "," + token.getRight() + ">\n");
		myWriter.close();
	}

}
