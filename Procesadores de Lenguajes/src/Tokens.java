import java.io.FileWriter;
import java.io.IOException;

public class Tokens {

	public static Pair<String, String> toFile(Pair<String, String> token) {
		FileWriter myWriter;
		try {
			myWriter = new FileWriter("Resultados\\tokens.txt", true);
			myWriter.write("<" + token.getLeft() + "," + token.getRight() + ">\n");
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return token;
	}

	public static void clearFile() {
		FileWriter myWriter;
		try {
			myWriter = new FileWriter("Resultados\\tokens.txt");
			myWriter.write("");
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
