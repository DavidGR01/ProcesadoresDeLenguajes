import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ALex {

	@SuppressWarnings("unchecked")
	private static Pair<Integer, String>[][] matriz = new Pair[23][23];
	private static HashMap<String, String> tablaPR = new HashMap<>();
	private static FileReader fr;
	private static BufferedReader br;
	private static int car;
	private static TablaSimbolos TS;
	public static int line = 1; // Variables para llevar la cuenta de la linea en la que estamos

	// Variable auxiliar para los identificadores
	public static String ultLexema = "";

	public static void inicializar(String file) {
		rellenarMatriz();
		rellenarPR();

		// Vamos leyendo el archivo
		TS = ASint.TSActual;
		GestorErrores.rellenarMap();
		File f = new File(file);
		try {
			fr = new FileReader(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br = new BufferedReader(fr);
		car = leer();
	}

	public static Pair<String, String> execALex() {

		TS = ASint.TSActual;
		int estado = 0;
		int col = 0;
		String accion = null;
		String lexema = null;
		int valor = 0;
		Pair<String, String> token = null;

		do {
			// EOF
			if (car == -1)
				return new Pair<String, String>("$", "");

			if (car == 10)
				line++;
			col = columna(estado, car);
			accion = matriz[estado][col].getRight();
			estado = matriz[estado][col].getLeft();

			if (estado == -2) {
				GestorErrores.addError(accion, line, "Léxico");
				// Seguimos leyendo el fichero desde el siguiente caracter al erroneo
				if (accion.equals("51")) {
					car = leer();
					estado = 0;
				} else if (accion.equals("57")) {
					estado = 0;
				} else
					GestorErrores.salidaPrematura();
			} else {
				switch (accion) {
				case "A":
					car = leer();
					break;
				case "B":
					lexema = "";
					if (car != 39)
						lexema += (char) car;
					car = leer();
					break;
				case "C":
					valor = Character.getNumericValue((char) car);
					car = leer();
					break;
				case "D":
					valor = valor * 10 + Character.getNumericValue((char) car);
					car = leer();
					break;
				case "E":
					// Revisar rango
					if (valor > 32767) {
						GestorErrores.addError("56", line, "Léxico");
						token = new Pair<String, String>("entero", "0"); // Nos recuperamos del error devolviendo el
																			// token como se esperaba pero con el valor
																			// por defecto
					} else
						token = new Pair<String, String>("entero", valor + "");
					return Tokens.toFile(token);
				case "F":
					token = new Pair<String, String>("incrementador", "");
					car = leer();
					return Tokens.toFile(token);
				case "G":
					lexema += (char) car;
					car = leer();
					break;
				case "H":
					if (buscarPR(lexema)) {
						if (lexema.equals("true") || lexema.equals("false"))
							token = new Pair<String, String>("logico", lexema);
						else
							token = new Pair<String, String>(lexema, "");
					} else {
						if (ASint.zonaDecl) {
							int p = TS.buscarTS(lexema);
							if (p != -1) {
								GestorErrores.addError3("El identificador '" + lexema + "' ya está declarado",
										ALex.line, "Semántico", true); // El id ya está declarado
							} else {
								p = TS.insertarLexemaTS(new Entrada(lexema));
								token = new Pair<String, String>("id", p + "");
								ultLexema = lexema;
							}
						} else {
							int p1 = ASint.TSActual.buscarTS(lexema);
							int p2 = ASint.TSG.buscarTS(lexema);

							if (p1 == -1 && p2 == -1) {
								GestorErrores.addError3(
										"El identificador '" + lexema + "' no se ha declarado previamente", ALex.line,
										"Semántico", true); // El id no está declarado
							} else {
								if (p1 == -1) {
									token = new Pair<String, String>("id", p2 + "");
									ultLexema = lexema;
								} else {
									token = new Pair<String, String>("id", p1 + "");
									ultLexema = lexema;
								}
							}
						}
					}
					return Tokens.toFile(token);
				case "I":
					if (lexema.length() > 64) {
						GestorErrores.addError("55", line, "Léxico");
						token = new Pair<String, String>("cadena", "\"\""); // Nos recuperamos del error devolviendo el
																			// token como se esperaba pero con el valor
																			// por defecto
					} else
						token = new Pair<String, String>("cadena", "\"" + lexema + "\"");
					car = leer();
					return Tokens.toFile(token);
				case "J":
					token = new Pair<String, String>("abreParentesis", "");
					car = leer();
					return Tokens.toFile(token);
				case "K":
					token = new Pair<String, String>("cierraParentesis", "");
					car = leer();
					return Tokens.toFile(token);
				case "L":
					token = new Pair<String, String>("abreCorchete", "");
					car = leer();
					return Tokens.toFile(token);
				case "M":
					token = new Pair<String, String>("cierraCorchete", "");
					car = leer();
					return Tokens.toFile(token);
				case "N":
					token = new Pair<String, String>("menos", "");
					car = leer();
					return Tokens.toFile(token);
				case "Ñ":
					token = new Pair<String, String>("menorEstricto", "");
					car = leer();
					return Tokens.toFile(token);
				case "O":
					token = new Pair<String, String>("exclamacion", "");
					car = leer();
					return Tokens.toFile(token);
				case "P":
					token = new Pair<String, String>("puntoYcoma", "");
					car = leer();
					return Tokens.toFile(token);
				case "Q":
					token = new Pair<String, String>("igual", "");
					car = leer();
					return Tokens.toFile(token);
				case "R":
					token = new Pair<String, String>("coma", "");
					car = leer();
					return Tokens.toFile(token);
				case "S":
					break;
				}
			}

		} while (estado < 7 && estado >= 0);
		return null; // No se llega nunca aquí
	}

	public static void closeFile() throws IOException {
		br.close();
		fr.close();
	}

	private static int leer() {
		Integer res = null;
		try {
			res = br.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	private static void rellenarMatriz() {

		// Inicializar todo a -2 y null
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++)
				matriz[i][j] = new Pair<Integer, String>(-2, null);
		}

		// Rellenar con los datos buenos

		// Fila 0
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
		matriz[0][22] = new Pair<Integer, String>(21, "S");

		// Fila 1
		matriz[1][1] = new Pair<Integer, String>(1, "G");
		matriz[1][2] = new Pair<Integer, String>(1, "G");
		matriz[1][5] = new Pair<Integer, String>(7, "H");
		matriz[1][7] = new Pair<Integer, String>(1, "G");

		// Fila 2
		matriz[2][2] = new Pair<Integer, String>(2, "D");
		matriz[2][5] = new Pair<Integer, String>(8, "E");

		// Fila 3
		matriz[3][19] = new Pair<Integer, String>(4, "A");

		// Fila 4
		matriz[4][3] = new Pair<Integer, String>(4, "A");
		matriz[4][21] = new Pair<Integer, String>(0, "S");
		matriz[4][22] = new Pair<Integer, String>(22, "S");

		// Fila 5
		matriz[5][4] = new Pair<Integer, String>(5, "G");
		matriz[5][20] = new Pair<Integer, String>(9, "I");

		// Fila 6
		matriz[6][8] = new Pair<Integer, String>(10, "F");

		// Añadimos los errores

		Pair<Integer, String> par = null;

		// Fila 0
		par = new Pair<Integer, String>(-2, "50");
		matriz[0][3] = par;
		matriz[0][4] = par;
		matriz[0][5] = par;
		matriz[0][6] = par;
		matriz[0][7] = new Pair<Integer, String>(-2, "51");

		// Fila 2
		matriz[2][1] = new Pair<Integer, String>(-2, "57");
		matriz[2][7] = new Pair<Integer, String>(-2, "57");

		// Fila 3
		par = new Pair<Integer, String>(-2, "52");
		matriz[3][0] = par;
		matriz[3][1] = par;
		matriz[3][2] = par;
		for (int i = 7; i <= 18; i++)
			matriz[3][i] = par;
		matriz[3][20] = par;
		matriz[3][21] = par;
		matriz[3][22] = par;

		// Fila 5
		matriz[5][21] = new Pair<Integer, String>(-2, "53");
		matriz[5][22] = new Pair<Integer, String>(-2, "53");

		// Fila 6
		par = new Pair<Integer, String>(-2, "54");
		for (int i = 0; i <= 7; i++)
			matriz[6][i] = par;
		for (int i = 9; i <= 22; i++)
			matriz[6][i] = par;

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
		if (estado == 0) {
			switch (c) {
			case 95:
				return 7;
			case 43:
				return 8;
			case 40:
				return 9;
			case 41:
				return 10;
			case 123:
				return 11;
			case 125:
				return 12;
			case 45:
				return 13;
			case 60:
				return 14;
			case 33:
				return 15;
			case 59:
				return 16;
			case 61:
				return 17;
			case 44:
				return 18;
			case 47:
				return 19;
			case 39:
				return 20;
			case -1:
				return 22;
			default:
				if (c == -1 || c == 10 || c == 13 || c == 32 || c == 9)
					return 0;
				if ((c >= 97 && c <= 122) || (c >= 65 && c <= 90))
					return 1;
				if (c >= 48 && c <= 57)
					return 2;
				return 3;
			}
		}
		if (estado == 1 || estado == 2) {
			if ((c >= 97 && c <= 122) || (c >= 65 && c <= 90))
				return 1;
			if (c >= 48 && c <= 57)
				return 2;
			if (c == 95)
				return 7;
			return 5;
		}
		if (estado == 3) {
			if (c == 47)
				return 19;
			return 0;
		}
		if (estado == 4) {
			if (c == 10 || c == 13)
				return 21;
			if (c == -1)
				return 22;
			return 3;
		}
		if (estado == 5) {
			if (c == 39)
				return 20;
			if (c == -1 || c == 10 || c == 13)
				return 21;
			return 4;
		}
		if (estado == 6) {
			if (c == 43)
				return 8;
			return 0;
		}
		return -4;
	}
}