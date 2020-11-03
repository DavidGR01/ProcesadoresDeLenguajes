import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ALex {

	@SuppressWarnings("unchecked")
	static Pair<Integer, String>[][] matriz = new Pair[21][22];
	static HashMap<String, Integer> columnas = new HashMap<>();
	static HashMap<String, String> tablaPR = new HashMap<>();

	public static void main(String[] args) throws IOException {

		rellenarMatriz();
		rellenarPR();

		// Vamos leyendo el archivo
		TablaSimbolos TS = new TablaSimbolos();
		File f = new File("files/input.txt"); // El archivo tiene que estar en la carpeta files
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);

		int estado = 0;
		int car = br.read();
		int col = 0;
		String accion = null;

		String lexema = null;
		int valor = 0;
		// Pair<String, String> tokenSimbolo = null, tokenIncrementador = null,
		// tokenCadena = null;
		// Pair<String, Integer> tokenEntero = null, tokenId = null;
		Pair<String, String> token = null;

		// Variables para llevar la cuenta de la linea en la que estamos
		int line = 1;

		// Mientras no leamos EOF
		while (car != -1) {

			do {
				col = columna(estado, car);
				accion = matriz[estado][col].getRight();
				System.out.println(accion);
				estado = matriz[estado][col].getLeft();

				if (estado == -2) {
					System.out.println("Error en la linea " + line);
					car = br.read();
				} else {
					switch (accion) {
					case "A":
						car = br.read();
						break;
					case "B":
						lexema = "";
						lexema += (char) car;
						car = br.read();
						break;
					case "C":
						valor = Character.getNumericValue((char) car);
						car = br.read();
						break;
					case "D":
						valor = valor * 10 + Character.getNumericValue((char) car);
						car = br.read();
						break;
					case "E":
						// Revisar rango
						token = new Pair<String, String>("entero", valor + "");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "F":
						token = new Pair<String, String>("incrementador", "-");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "G":
						lexema += (char) car;
						car = br.read();
						break;
					case "H":
						if (buscarPR(lexema)) {
							if (lexema == "true" || lexema == "false")
								token = new Pair<String, String>("logico", lexema);
							else
								token = new Pair<String, String>(lexema, "-");
						} else {
							int p = TS.buscarTS(lexema);
							if (p == -1)
								p = TS.insertarTS(new Entrada(lexema));

							token = new Pair<String, String>("id", p + "");
						}

						estado = 0;
						System.out.println(token);
						Tokens.guardarToken(token);
						break;
					case "I":
						lexema += (char) car;

						// Revisar Cadena
						token = new Pair<String, String>("cadena", lexema);

						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "J":
						token = new Pair<String, String>("abre-parentesis", "-");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "K":
						token = new Pair<String, String>("cierra-parentesis", "-");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "L":
						token = new Pair<String, String>("abre-corchete", "-");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "M":
						token = new Pair<String, String>("cierra-corchete", "-");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "N":
						token = new Pair<String, String>("menos", "-");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "Ñ":
						token = new Pair<String, String>("menor-estricto", "-");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "O":
						token = new Pair<String, String>("exclamación", "-");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "P":
						token = new Pair<String, String>("punto-y-coma", "-");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "Q":
						token = new Pair<String, String>("igual", "-");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "R":
						token = new Pair<String, String>("coma", "-");
						System.out.println(token);
						Tokens.guardarToken(token);
						car = br.read();
						estado = 0;
						break;
					case "S": // Estado para salir de los comentarios??
						// estado = 0;
						// car = br.read();
						break;
					}
				}

			} while (estado < 7 && estado >= 0);// && car != -1
		}
		// TablaSimbolos.toFile();
		Tokens.toFile();
		TS.toFile();
		br.close();
	}

	private static void rellenarMatriz() {

		// Inicialiar todo a -2 y null
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++)
				matriz[i][j] = new Pair<Integer, String>(-2, null);
		}

		// Rellenar con los datos buenos

		// Primera fila
		matriz[0][0] = new Pair<Integer, String>(0, "A");
		matriz[0][1] = new Pair<Integer, String>(1, "B");
		matriz[0][2] = new Pair<Integer, String>(2, "C");
		matriz[0][8] = new Pair<Integer, String>(6, "A");
		matriz[0][9] = new Pair<Integer, String>(11, "J");
		matriz[0][10] = new Pair<Integer, String>(12, "K");
		matriz[0][11] = new Pair<Integer, String>(13, "L");
		matriz[0][12] = new Pair<Integer, String>(14, "M");
		matriz[0][13] = new Pair<Integer, String>(15, "N");
		matriz[0][14] = new Pair<Integer, String>(16, "Ñ");
		matriz[0][15] = new Pair<Integer, String>(17, "O");
		matriz[0][16] = new Pair<Integer, String>(18, "P");
		matriz[0][17] = new Pair<Integer, String>(19, "Q");
		matriz[0][18] = new Pair<Integer, String>(20, "R");
		matriz[0][19] = new Pair<Integer, String>(3, "A");
		matriz[0][20] = new Pair<Integer, String>(5, "B");

		// Segunda fila
		matriz[1][1] = new Pair<Integer, String>(1, "G");
		matriz[1][2] = new Pair<Integer, String>(1, "G");
		matriz[1][5] = new Pair<Integer, String>(7, "H");
		matriz[1][7] = new Pair<Integer, String>(1, "G");

		// Tercera fila
		matriz[2][2] = new Pair<Integer, String>(2, "D");
		matriz[2][6] = new Pair<Integer, String>(8, "E");

		// Cuarta fila
		matriz[3][19] = new Pair<Integer, String>(4, "A");

		// Quinta fila
		matriz[4][3] = new Pair<Integer, String>(4, "G");
		matriz[4][21] = new Pair<Integer, String>(0, "S");

		// Sexta fila
		matriz[5][4] = new Pair<Integer, String>(5, "G");
		matriz[5][20] = new Pair<Integer, String>(9, "I");

		// Septima fila
		matriz[6][8] = new Pair<Integer, String>(10, "F");

	}

	private static void rellenarPR() {
		tablaPR.put("if", "");
		tablaPR.put("while", "");
		tablaPR.put("alert", "");
		tablaPR.put("input", "");
		tablaPR.put("let", "");
		tablaPR.put("number", "");
		tablaPR.put("boolean", "");
		tablaPR.put("string", "");
		tablaPR.put("true", "");
		tablaPR.put("false", "");
		tablaPR.put("function", "");
		tablaPR.put("return", "");
	}

	private static boolean buscarPR(String lexema) {
		return tablaPR.get(lexema) != null;
	}

	/**
	 * Devuelve la columna correspondiente a cada character
	 * 
	 * @param estado
	 * @param c
	 * @return
	 */
	private static int columna(int estado, int c) {
		// l:letras min y mayus
		// d:digitos
		// c:char - {<eol>}
		// p:char-{'}
		// r:o.c. - {d}
		// q:o.c. - {l,_,d}
		if (estado == 0 && (c == 9 || c == 32 || c == 10 || c == 13)) // del
			return 0;
		if ((estado == 0 || estado == 1) && ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)))
			return 1;
		if (0 <= estado && estado <= 2 && c >= 48 && c <= 57)
			return 2;

		if (estado == 4 && c != 10 && c != -1) // Saltando caracteres de los comentarios
			return 3;
		if (estado == 5 && c != 39)
			return 4;
		if (estado == 2 && (c < 48 || c > 57))
			return 6;
		if (estado == 1 && c == 95)
			return 7;
		if ((estado == 0 || estado == 6) && c == 43)
			return 8;
		if (estado == 0) {
			if (c == 40)
				return 9;
			if (c == 41)
				return 10;
			if (c == 123)
				return 11;
			if (c == 125)
				return 12;
			if (c == 45)
				return 13;
			if (c == 60)
				return 14;
			if (c == 33)
				return 15;
			if (c == 59)
				return 16;
			if (c == 61)
				return 17;
			if (c == 44)
				return 18;
			if (c == 47)
				return 19;
			if (c == 39)
				return 20;
		}
		if (estado == 3 && c == 47)
			return 19;
		if (estado == 5 && c == 39)
			return 20;
		if (estado == 4 && c == 10)
			return 21;
		return 5;
	}

}