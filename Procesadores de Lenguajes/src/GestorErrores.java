import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GestorErrores {

	private static HashMap<String, String> map = new HashMap<>();
	private static List<String> errores = new ArrayList<>();

	public static void addError(String cod, int linea, String tipoError) {
		String s = "Error " + tipoError + ": Código: " + cod + ": " + map.get(cod) + " en la línea " + linea + "\n";
		errores.add(s);
		System.out.println(s);
	}

	public static void rellenarMap() {
		map.put("50", "Carácter no valido");
		map.put("51", "No puede empezar identificador con _");
		map.put("52", "Despues de / debe haber otra /");
		map.put("53", "Falta comilla de cierre");
		map.put("54", "Despues de un + debe haber un +");
		map.put("55", "Longitud maxima de string superada");
		map.put("56", "Max entero 32767");
		map.put("57", "Identificador no puede empezar con número");
	}

	public static void toFile() throws IOException {
		// Sobreescribe cualquier archivo anterior con el mismo nombre
		FileWriter writer = new FileWriter("Errores.txt");
		for (String error : errores)
			writer.write(error);
		writer.close();
	}

}
