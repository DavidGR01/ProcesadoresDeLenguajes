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
		map.put("100", "Símbolo en posición incorrecta");
		map.put("101", "Falta el prefijo let para la declaración de variables");
		map.put("102", "Falta parte izquierda de la asignación");
		map.put("103", "Falta corchete de apertura");
		map.put("104", "Falta paréntesis de apertura");
		map.put("105", "Se esperaba un tipo de variable");
		map.put("106", "Se esperaba una coma");
		map.put("107", "Se esperaba un parentesis de apertura o un igual tras el identificador");
		map.put("108", "Se esperaba un menor estricto o un menos");
		map.put("109", "Se esperaba un identificador");
		map.put("110", "Falta corchete de cierre");
		map.put("111", "Falta paréntesis de cierre");
		map.put("112", "Se esperaba un punto y coma");
		map.put("113", "No se permiten funciones anidadas");
	}

	public static void toFile() throws IOException {
		// Sobreescribe cualquier archivo anterior con el mismo nombre
		FileWriter writer = new FileWriter("Errores.txt");
		for (String error : errores)
			writer.write(error);
		writer.close();
	}

}
