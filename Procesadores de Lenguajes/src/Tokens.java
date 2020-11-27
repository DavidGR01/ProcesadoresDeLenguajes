import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tokens {

	public static Pair<String, String> toFile(Pair<String, String> token) throws IOException {
		FileWriter myWriter = new FileWriter("tokens.txt", true);
		myWriter.write("<" + token.getLeft() + "," + token.getRight() + ">\n");
		myWriter.close();
		return token;
	}

	public static void clearFile() throws IOException {
		FileWriter myWriter = new FileWriter("tokens.txt");
		myWriter.write("");
		myWriter.close();
	}
}
