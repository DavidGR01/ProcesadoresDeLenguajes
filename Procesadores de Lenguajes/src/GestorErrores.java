import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GestorErrores {

	private static HashMap<String, String> map = new HashMap<>();
	private static List<String> errores = new ArrayList<>();

	public static void addError(String cod, int linea, String tipoError) {
		String s = "Error " + tipoError + ": " + map.get(cod) + ". En la línea " + linea + "\n";
		errores.add(s);
		System.out.println(s);
	}

	/*
	 * Para errores Semánticos, que paran la ejecución del programa tras imprimir el
	 * fichero de errores
	 */
	// TODO Quizas deberiamos parar tambien al detectar errores en declaraciones ya
	// que no sabemos como de va a comportar despues de estos errores el
	// recuperador.
	public static void addError(String cod, int linea, String tipoError, boolean parar) {
		String s = "Error " + tipoError + ": " + map.get(cod) + ". En la línea " + linea + "\n";
		errores.add(s);
		System.out.println(s);
		salidaPrematura();
	}
	
	public static void addError3(String texto, int linea, String tipoError, boolean parar) {
		String s = "Error " + tipoError + ": " + texto + ". En la línea " + linea + "\n";
		errores.add(s);
		System.out.println(s);
		salidaPrematura();
	}

	/*
	 * Para errores en equipara()
	 */
	public static void addError2(String texto, int linea, String tipoError) {
		String s = "Error " + tipoError + ": " + texto + ". En la línea " + linea + "\n";
		errores.add(s);
		System.out.println(s);
	}

	public static void rellenarMap() {
		map.put("50", "Carácter no valido");
		map.put("51", "No puede empezar identificador con _");
		map.put("52", "Despues de / debe haber otra /");
		map.put("53", "Falta comilla de cierre");
		map.put("54", "Le falta un símbolo + al operador autoincremento.");
		map.put("55", "Longitud maxima de string superada");
		map.put("56", "Max entero 32767");
		map.put("57", "Identificador no puede empezar con número");
		map.put("100", "Símbolo en posición incorrecta"); // O faltante????
		map.put("101", "Fallo en la declaración de la función.");
		map.put("102", "El tipo de dato introducido no existe.");
		map.put("103", "Fallo en el tipo de retorno de la función.");
		map.put("104", "Fallo en los argumentos de la función.");
		map.put("105", "Error en la sentencia.");
		map.put("106", "Se esperaba parentesis de apertura o un igual.");
		map.put("107", "Fallo en los argumentos de la llamada a la función.");
		map.put("108", "Se esperaba una coma o un parentesis de cierre.");
		map.put("109", "Fallo en el return.");
		map.put("110", "Fallo en la expresión.");
		map.put("111", "Falta paréntesis de cierre");
		map.put("112", "Se esperaba un punto y coma");
		map.put("113", "No se permiten funciones anidadas");
		map.put("200", "El tipo de retorno de la función no coincide con el que se esperaba.");
		map.put("201", "La condición debe ser de tipo lógico.");
		map.put("202", "Los argumentos de la llamada a la función no coinciden con los esperados.");
		map.put("203", "La sentencia Alert solo puede tener argumentos tipo entero o cadena.");
		map.put("204", "La sentencia Input solo puede tener argumentos tipo entero o cadena.");
		map.put("205", "Se esperaba un entero.");
		map.put("206", "Solo se puede usar negación con variables lógicas.");
		map.put("207", "Solo se puede usar autoincremento con variables enteras.");
		map.put("208", "No coinciden los tipos en la asignación.");
		map.put("209", "El identificador ya está declarado.");
		map.put("210", "El identificador no se ha declarado previamente.");
	}

	public static void toFile() {
		// Sobreescribe cualquier archivo anterior con el mismo nombre
		FileWriter writer;
		try {
			writer = new FileWriter("Errores.txt");
			for (String error : errores)
				writer.write(error);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Para parar la ejecución ante errores que no podemos recuperar
	 */
	public static void salidaPrematura() {
		GestorErrores.toFile();
		System.exit(1);
	}

}
