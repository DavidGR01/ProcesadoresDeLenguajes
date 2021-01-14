import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parse {

	private static List<String> secuencia = new ArrayList<>();

	public static void add(String s) {
		secuencia.add(s);
	}

	public static void toFile() throws IOException {
		FileWriter myWriter = new FileWriter("Resultados\\parse.txt");
		myWriter.write("Descendente");
		for (String elem : secuencia)
			myWriter.write(" " + elem);
		myWriter.close();
	}
}
