import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class ASint {

	// Estructura para la gramatica
	static HashMap<String, ArrayList<ArrayList<String>>> gram = new HashMap<>();

	// Terminales y no terminales
	private static ArrayList<String> terminales = new ArrayList<String>();
	private static ArrayList<String> noTerminales = new ArrayList<String>();

	private static Pair<String, String> sigToken = null;

	// First y follow
	private static ArrayList<String> firstR, firstU, firstV, firstE, firstS, firstT, firstF, firstB, firstY;

	private static ArrayList<String> followP, followF, followC, followZ, followX, followL, followQ, followA, followK,
			followN, followM, followH, followT;

	// Tablas de Simbolos
	public static TablaSimbolos TSG, TSL;
	public static TablaSimbolos TSActual;
	private static int DespG, DespL;
	static boolean zonaDecl;

	// Constantes
	private static String TIPO_ERROR = "tipo_error", TIPO_OK = "tipo_ok";
	private static String SEMANTICO = "Semántico", SINTACTICO = "Sintáctico";

	// Variable auxiliar para guardar bien la linea del ultimo fallo
	private static int ultimaLineaFallo = -1;

	public static void cargarGramatica() {
		rellenarGramatica("P", "B", "P", "|", "F", "P", "|", "lambda", "|", "S", "P");
		rellenarGramatica("F", "function", "H", "id", "(", "A", ")", "{", "C", "}");
		rellenarGramatica("T", "number", "|", "boolean", "|", "string");
		rellenarGramatica("H", "T", "|", "lambda");
		rellenarGramatica("A", "T", "id", "K", "|", "lambda");
		rellenarGramatica("K", ",", "T", "id", "K", "|", "lambda");
		rellenarGramatica("C", "B", "C", "|", "S", "C", "|", "lambda");
		rellenarGramatica("B", "if", "(", "E", ")", "S", "|", "while", "(", "E", ")", "{", "C", "}", "|", "let", "T",
				"id", ";");
		rellenarGramatica("S", "id", "W", "|", "alert", "(", "E", ")", ";", "|", "input", "(", "id", ")", ";", "|",
				"return", "X", ";");
		rellenarGramatica("W", "=", "E", ";", "|", "(", "L", ")", ";");
		rellenarGramatica("L", "E", "Q", "|", "lambda");
		rellenarGramatica("Q", ",", "E", "Q", "|", "lambda");
		rellenarGramatica("X", "E", "|", "lambda");
		rellenarGramatica("E", "R", "M"); // E -> RY ----- E-> RM
		rellenarGramatica("M", "<", "R", "M", "|", "lambda"); // Y-> <RY|-RY|lambda ------- M -> <RM|lambda
		rellenarGramatica("R", "Y", "N"); // R -> !R|U --- R-> YN
		rellenarGramatica("N", "-", "Y", "N", "|", "lambda");
		rellenarGramatica("Y", "!", "Y", "|", "U");
		rellenarGramatica("U", "++", "U", "|", "V");
		rellenarGramatica("V", "id", "Z", "|", "(", "E", ")", "|", "entero", "|", "cadena", "|", "logico");
		rellenarGramatica("Z", "(", "L", ")", "|", "lambda");

		rellenarTerminalesYNoTerminales();
	}

	public static void execASint(String file) throws IOException {

		cargarGramatica();

		// Axioma
		firstB = first("B");
		firstF = first("F");
		firstR = first("R");
		firstU = first("U");
		firstV = first("V");
		firstE = first("E");
		firstS = first("S");
		firstT = first("T");
		firstY = first("Y");
		followX = follow("X");
		followP = follow("P");
		followF = follow("F");
		followC = follow("C");
		followZ = follow("Z");
		followL = follow("L");
		followQ = follow("Q");
		followA = follow("A");
		followH = follow("H");
		followK = follow("K");
		followM = follow("M");
		followN = follow("N");
		followT = follow("T");

		// Limpiamos los archivos de tokens y de Tabla de Simbolos
		Tokens.clearFile();
		TablaSimbolos.clearFile();

		// {TSG = CrearTS, TSactual = TSG, DespG = 0} P();
		TSG = new TablaSimbolos();
		TSActual = TSG;
		DespG = 0;

		ALex.inicializar(file);
		sigToken = ALex.execALex();

		P();

		// {destruir(TSG)}
		TSG.toFile();
		TSG = null;

		ALex.closeFile();
	}

	public static ArrayList<String> first(String s) {

		ArrayList<String> res = new ArrayList<String>();

		if (gram.get(s) == null)
			res.add(s);
		else {
			int cont = gram.get(s).size();
			int index = cont;
			while (cont > 0) {
				ArrayList<String> resRec = new ArrayList<String>();
				resRec = firstRec(gram.get(s).get(index - cont), resRec, 0);
				for (String elem : resRec) {
					if (!res.contains(elem))
						res.add(elem);
				}
				cont--;
			}
		}
		return res;
	}

	public static ArrayList<String> firstRec(ArrayList<String> prod, ArrayList<String> res, int cont) {
		if (cont == prod.size())
			return res;
		while (prod.size() > cont) {
			if (terminales.contains(prod.get(cont))) {
				res.add(prod.get(cont));
				return res;
			} else if (noTerminales.contains(prod.get(cont))) {
				if (first(prod.get(cont)).contains("lambda")) {
					ArrayList<String> res1 = first(prod.get(cont));
					for (String s : res1) {
						if (cont == prod.size() - 1)
							res.add(s);
						else {
							if (s != "lambda")
								res.add(s);
						}
					}
					if (cont != prod.size() - 1)
						return firstRec(prod, res, cont++);
					else
						return res;
				} else
					return first(prod.get(cont));
			} else
				res.add("lambda");
			cont++;
		}
		return res;
	}

	public static ArrayList<String> firstFollow(ArrayList<String> prod) {
		return firstRec(prod, new ArrayList<>(), 0);
	}

	// Follow
	static ArrayList<String> follow(String nt) {
		ArrayList<String> res = new ArrayList<>();
		if (nt.equals("P"))
			res.add("$");
		for (Entry<String, ArrayList<ArrayList<String>>> regla : gram.entrySet()) {
			for (ArrayList<String> prod : regla.getValue()) {
				if (!(regla.getKey().equals(prod.get(prod.size() - 1)) && nt.equals(regla.getKey()))) {
					if (prod.contains(nt)) {
						int indice = prod.indexOf(nt);
						if (indice == prod.size() - 1)
							res.addAll(follow(regla.getKey()));
						else {
							ArrayList<String> beta = beta(prod, nt);
							ArrayList<String> first = firstFollow(beta);
							if (first.remove("lambda")) {
								if (!regla.getKey().equals(nt)) {
									res.addAll(follow(regla.getKey()));
								}
							}
							res.addAll(first);
						}
					}
				}
			}
		}
		return sinDuplicados(res);
	}

	private static ArrayList<String> sinDuplicados(ArrayList<String> arr) {
		ArrayList<String> res = new ArrayList<>();
		for (String s : arr) {
			if (!res.contains(s))
				res.add(s);
		}
		return res;
	}

	private static ArrayList<String> beta(ArrayList<String> prod, String nt) {
		ArrayList<String> res = new ArrayList<>();
		int indice = prod.indexOf(nt);
		for (int i = indice + 1; i < prod.size(); i++)
			res.add(prod.get(i));
		return res;
	}

	// Producciones
	private static void P() {
		if (firstB.contains(traducir(sigToken.getLeft()))) {
			Parse.add("1");
			B();
			P();
		} else if (firstF.contains(traducir(sigToken.getLeft()))) {
			Parse.add("2");
			F();
			P();
		} else if (followP.contains(traducir(sigToken.getLeft())))
			Parse.add("3");
		else if (firstS.contains(traducir(sigToken.getLeft()))) {
			Parse.add("4");
			S();
			P();
		} else {
			GestorErrores.addError("100", ALex.line, SINTACTICO);
			while (!sigToken.getLeft().equals("$"))
				avanzarYParar();
		}
	}

	private static void F() {
		int lineaTemp = ALex.line;
		if (sigToken.getLeft().equals("function")) {
			Parse.add("5");
			zonaDecl = true;
			equipara("function");
			String tipoDev = H();
			int pos = Integer.parseInt(sigToken.getRight());
			equipara("id");

			TSL = new TablaSimbolos();
			TSActual = TSL;
			DespL = 0;
			TSG.insertarEtiqTS(pos, Etiquetas.getEtiqueta());
			TSG.insertarTipoTS(pos, "function");

			equipara("abreParentesis");
			ArrayList<String> tipoArgs = A();
			equipara("cierraParentesis");
			zonaDecl = false;

			for (String s : tipoArgs)
				TSG.insertarTipoParamTS(pos, s);
			TSG.insertarTipoDevTS(pos, tipoDev);

			equipara("abreCorchete");
			String[] tipos = C(); // 0: tipo(entero,logico,cadena,ok, error), 1:
									// tipoRet(entero,logico,cadena,void)
			equipara("cierraCorchete");

			if (!tipos[1].equals(tipoDev))
				GestorErrores.addError("200", lineaTemp, SEMANTICO, true); // Tipo Retorno Incorrecto

			// Destruye TSL
			TSActual.toFile();
			TSL = null;
			TSActual = TSG;
			DespL = 0;
		} else {
			GestorErrores.addError("101", lineaTemp, SINTACTICO);
			while (!followF.contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
	}

	private static String[] T() {
		String[] tipoYAncho = new String[2];
		if (sigToken.getLeft().equals("number")) {
			Parse.add("6");
			equipara("number");
			tipoYAncho[0] = "entero";
			tipoYAncho[1] = "1";
		} else if (sigToken.getLeft().equals("boolean")) {
			Parse.add("7");
			equipara("boolean");
			tipoYAncho[0] = "logico";
			tipoYAncho[1] = "1";
		} else if (sigToken.getLeft().equals("string")) {
			Parse.add("8");
			equipara("string");
			tipoYAncho[0] = "cadena";
			tipoYAncho[1] = "64";
		} else {
			GestorErrores.addError("102", ALex.line, SINTACTICO);// El tipo de dato introducido no existe
			while (!followT.contains(traducir(sigToken.getLeft())))
				avanzarYParar();
		}
		return tipoYAncho;
	}

	private static String H() {
		if (firstT.contains(traducir(sigToken.getLeft()))) {
			Parse.add("9");
			return T()[0];
		} else if (followH.contains(traducir(sigToken.getLeft()))) {
			Parse.add("10");
			return "void";
		} else {
			GestorErrores.addError("103", ALex.line, SINTACTICO); // Fallo en el tipo de la funcion
			while (!followT.contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return "FALLO"; // Nunca de llega a esta linea por lo que en este caso y otros similares
						// devolveremos esta cadena "FALLO"
	}

	private static ArrayList<String> A() {
		if (firstT.contains(traducir(sigToken.getLeft()))) {
			Parse.add("11");
			String[] tipos = T();
			int pos = Integer.parseInt(sigToken.getRight());
			equipara("id");

			ArrayList<String> aux = new ArrayList<String>();
			aux.add(tipos[0]);

			TSActual.insertarTipoTS(pos, tipos[0]);
			TSActual.insertarDesplazamientoTS(pos, DespL);
			DespL += Integer.parseInt(tipos[1]);

			ArrayList<String> res = K(aux);

			return res;
		} else if (followA.contains(traducir(sigToken.getLeft())))
			Parse.add("12");
		else {
			GestorErrores.addError("104", ALex.line, SINTACTICO); // Fallo en los argumentos de la funcion
			while (!followA.contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}

		}
		return new ArrayList<String>();
	}

	private static ArrayList<String> K(ArrayList<String> arr) {
		if (sigToken.getLeft().equals("coma")) {
			Parse.add("13");
			equipara("coma");
			String[] tipos = T();
			int pos = Integer.parseInt(sigToken.getRight());
			equipara("id");

			TSActual.insertarTipoTS(pos, tipos[0]);
			TSActual.insertarDesplazamientoTS(pos, DespL);
			DespL += Integer.parseInt(tipos[1]);

			arr.add(tipos[0]);
			return K(arr);
		} else if (followK.contains(traducir(sigToken.getLeft()))) {
			Parse.add("14");
			return arr;
		} else {
			GestorErrores.addError("104", ALex.line, SINTACTICO);
			while (!followK.contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return new ArrayList<String>();
	}

	private static String[] C() {

		String[] res = new String[2];

		if (firstB.contains(traducir(sigToken.getLeft()))) {
			Parse.add("15");
			String[] be = B();
			String[] ce1 = C();
			res[0] = ce1[0].equals(be[0]) && ce1[0].equals(TIPO_OK) ? TIPO_OK : TIPO_ERROR; // No se va a dar tipo error

			if (be[1].equals(ce1[1]))
				res[1] = be[1];
			else if (ce1[1].equals("void"))
				res[1] = be[1];
			else if (be[1].equals("void"))
				res[1] = ce1[1];
			else
				res[1] = TIPO_ERROR; // En la comparacion con H no coincidirian

		} else if (firstS.contains(traducir(sigToken.getLeft()))) {
			Parse.add("16");
			String[] ese = S();
			if (sigToken.getLeft().equals("$")) {
				return ese;
			}
			String[] ce = C();

			res[0] = ese[0].equals(ce[0]) && ce[0].equals(TIPO_OK) ? TIPO_OK : TIPO_ERROR;

			if (ese[1].equals(ce[1]))
				res[1] = ese[1];
			else if (ce[1].equals("void"))
				res[1] = ese[1];
			else if (ese[1].equals("void"))
				res[1] = ce[1];
			else
				res[1] = TIPO_ERROR; // En la comparacion con H no coincidirian

		} else if (followC.contains(traducir(sigToken.getLeft()))) {
			Parse.add("17");
			res[0] = TIPO_OK;
			res[1] = "void";
		} else {
			res[0] = "";
			res[1] = "";
			if (sigToken.getLeft().equals("function"))
				GestorErrores.addError("113", ALex.line, SINTACTICO, true);
			else
				GestorErrores.addError("100", ALex.line, SINTACTICO);
			while (!followC.contains(traducir(sigToken.getLeft())))
				avanzarYParar();
		}
		return res;
	}

	private static String[] B() {
		String[] res = new String[2];
		if (sigToken.getLeft().equals("if")) {
			Parse.add("18");
			equipara("if");
			equipara("abreParentesis");
			String tipoE = E();
			equipara("cierraParentesis");
			String[] tiposS = S();

			if (!tipoE.equals("logico"))
				GestorErrores.addError("201", ALex.line, SEMANTICO, true); // Condicion debe ser boolean
			res[0] = tipoE == "logico" && tiposS[0] == TIPO_OK ? TIPO_OK : TIPO_ERROR; // No se va a dar el tipo_error
			res[1] = tiposS[1];
		} else if (sigToken.getLeft().equals("while")) {
			Parse.add("19");
			equipara("while");
			equipara("abreParentesis");
			String tipoE = E();
			equipara("cierraParentesis");
			equipara("abreCorchete");
			String[] tiposC = C();
			equipara("cierraCorchete");

			if (!tipoE.equals("logico"))
				GestorErrores.addError("201", ALex.line, SEMANTICO, true); // Condicion debe ser boolean
			res[0] = tipoE == "logico" && tiposC[0] == TIPO_OK ? TIPO_OK : TIPO_ERROR; // No se va a dar el tipo_error
			res[1] = tiposC[1];
		} else if (sigToken.getLeft().equals("let")) {
			Parse.add("20");
			zonaDecl = true;
			equipara("let");
			String tipoYDesplazamiento[] = T();
			int pos = Integer.parseInt(sigToken.getRight());
			zonaDecl = false;
			equipara("id");
			equipara("puntoYcoma");

			TSActual.insertarTipoTS(pos, tipoYDesplazamiento[0]);
			if (TSL == null) {
				TSG.insertarDesplazamientoTS(pos, DespG);
				DespG += Integer.parseInt(tipoYDesplazamiento[1]);
			} else {
				TSL.insertarDesplazamientoTS(pos, DespL);
				DespL += Integer.parseInt(tipoYDesplazamiento[1]);
			}
			res[0] = TIPO_OK;
			res[1] = "void";
		} else {
			GestorErrores.addError("100", ALex.line, SINTACTICO);
			while (!follow("B").contains(traducir(sigToken.getLeft())))
				avanzarYParar();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	private static String[] S() {
		int lineaTemp = ALex.line;
		String[] res = new String[2];
		if (sigToken.getLeft().equals("id")) {
			Parse.add("21");
			int pos = Integer.parseInt(sigToken.getRight());
			String auxUltLex = ALex.ultLexema;
			equipara("id");
			Object[] uvedoble = W();

			Entrada entrada = TSActual.buscarPos(pos);
			if (entrada == null)
				entrada = TSG.buscarPos(pos);
			if (!entrada.getLexema().equals(auxUltLex))
				entrada = TSG.buscarPos(pos);
			if (entrada.getTipo().equals("function")) {
				if (!entrada.getTipoParam().equals((ArrayList<String>) uvedoble[1])) {
					GestorErrores.addError("202", lineaTemp, SEMANTICO, true); // Argumentos no coinciden. Deberian
																				// ser entero, string y son string,
																				// string
				} else {
					res[0] = TIPO_OK;
					res[1] = entrada.getTipoDev();
				}
			} else {
				if (uvedoble[0].equals(entrada.getTipo())) {
					res[0] = (String) uvedoble[0];
					res[1] = "void";
				} else {
					GestorErrores.addError("208", lineaTemp, SEMANTICO, true); // No coinciden los tipos en la
																				// asignacion.
				}
			}
		} else if (sigToken.getLeft().equals("alert")) {
			Parse.add("22");
			equipara("alert");
			equipara("abreParentesis");
			String tipo = E();
			equipara("cierraParentesis");
			equipara("puntoYcoma");
			if (tipo.equals("cadena") || tipo.equals("entero"))
				res[0] = TIPO_OK;
			else
				GestorErrores.addError("203", lineaTemp, SEMANTICO, true); // Alert solo puede tener argumentos tipo
																			// entero o cadena
			res[1] = "void";
		} else if (sigToken.getLeft().equals("input")) {
			Parse.add("23");
			equipara("input");
			equipara("abreParentesis");
			int pos = Integer.parseInt(sigToken.getRight());
			equipara("id");
			equipara("cierraParentesis");
			equipara("puntoYcoma");
			Entrada e = TSActual.buscarPos(pos);
			String tipo = "";
			if (e != null)
				tipo = e.getTipo();
			else {
				e = TSG.buscarPos(pos);
				if (e != null)
					tipo = e.getTipo();
			}
			if (tipo.equals("cadena") || tipo.equals("entero"))
				res[0] = TIPO_OK;
			else
				GestorErrores.addError("204", lineaTemp, SEMANTICO, true); // Input solo puede tener argumentos tipo
																			// entero o cadena
			res[1] = "void";
		} else if (sigToken.getLeft().equals("return")) {
			Parse.add("24");
			equipara("return");
			String tipo = X();
			equipara("puntoYcoma");
			res[0] = TIPO_OK;
			res[1] = tipo;
		} else {
			res[0] = "";
			res[1] = "";
			GestorErrores.addError("105", ALex.line, SINTACTICO);
			while (!follow("S").contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return res;
	}

	private static Object[] W() {
		Object[] res = new Object[2];
		if (sigToken.getLeft().equals("igual")) {
			Parse.add("25");
			equipara("igual");
			String tipo = E();
			equipara("puntoYcoma");
			res[0] = tipo;
			res[1] = new ArrayList<>();
		} else if (sigToken.getLeft().equals("abreParentesis")) {
			Parse.add("26");
			equipara("abreParentesis");
			ArrayList<String> tiposArgs = L();
			equipara("cierraParentesis");
			equipara("puntoYcoma");
			res[0] = TIPO_OK; // No se usa
			res[1] = tiposArgs;
		} else {
			GestorErrores.addError("106", ALex.line, SINTACTICO);
			while (!follow("W").contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return res;
	}

	private static ArrayList<String> L() {

		ArrayList<String> res = new ArrayList<>();
		if (firstE.contains(traducir(sigToken.getLeft()))) {
			Parse.add("27");
			String tipo = E();
			ArrayList<String> aux = new ArrayList<>();
			aux.add(tipo);
			res = Q(aux);
		} else if (followL.contains(traducir(sigToken.getLeft())))
			Parse.add("28");
		else {
			GestorErrores.addError("107", ALex.line, SINTACTICO); // Fallo en la llamada a la función
			while (!followL.contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return res;
	}

	private static ArrayList<String> Q(ArrayList<String> arr) {
		if (sigToken.getLeft().equals("coma")) {
			Parse.add("29");
			equipara("coma");
			String tipo = E();
			arr.add(tipo);
			return Q(arr);
		} else if (followQ.contains(traducir(sigToken.getLeft()))) {
			Parse.add("30");
			return arr;
		} else {
			GestorErrores.addError("108", ALex.line, SINTACTICO);
			while (!followQ.contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return new ArrayList<String>();
	}

	private static String X() {
		if (firstE.contains(traducir(sigToken.getLeft()))) {
			Parse.add("31");
			String tipo = E();
			return tipo;
		} else if (followX.contains(traducir(sigToken.getLeft()))) {
			Parse.add("32");
			return "void";
		} else {
			GestorErrores.addError("109", ALex.line, SINTACTICO); // Fallo en el return
			while (!followX.contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return "Fallo";
	}

	private static String E() { // Solo devuelve tipos de datos
		if (firstR.contains(traducir(sigToken.getLeft()))) {
			Parse.add("33");
			String tipoR = R();
			String tipoM = M();
			return tipoM.equals("logico") ? "logico" : tipoR;
		} else {
			GestorErrores.addError("110", ALex.line, SINTACTICO); // Fallo en la expresión
			while (!follow("E").contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return "FALLO";
	}

	private static String M() {
		if (traducir(sigToken.getLeft()).equals("<")) {
			Parse.add("34");
			equipara("menorEstricto");
			String tipoR = R();
			M();
			if (!tipoR.equals("entero"))
				GestorErrores.addError("205", ALex.line, SEMANTICO, true); // Se esperaba un entero
			else
				return "logico";
		} else if (followM.contains(traducir(sigToken.getLeft()))) {
			Parse.add("35");
		} else {
			GestorErrores.addError("110", ALex.line, SINTACTICO); // Fallo en la expresion
			while (!followM.contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return "FALLO";
	}

	private static String R() {
		if (firstY.contains(traducir(sigToken.getLeft()))) {
			Parse.add("36");
			String tipoY = Y();
			N(ALex.line);
			return tipoY;
		} else {
			GestorErrores.addError("110", ALex.line, SINTACTICO); // Fallo en la expresion
			while (!follow("R").contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return "FALLO";
	}

	private static void N(int lineaAnterior) {
		int lineaTemp = ALex.line != lineaAnterior ? lineaAnterior : ALex.line;
		int lineaAux = ALex.line;
		if (sigToken.getLeft().equals("menos")) {
			Parse.add("37");
			equipara("menos");
			String tipoY = Y();
			N(lineaAux);
			if (!tipoY.equals("entero"))
				GestorErrores.addError("205", lineaTemp, SEMANTICO, true); // Se esperaba un entero
		} else if (followN.contains(traducir(sigToken.getLeft()))) {
			Parse.add("38");
		} else {
			GestorErrores.addError("110", lineaTemp, SINTACTICO); // Fallo en la expresion
			while (!followN.contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
	}

	private static String Y() {
		if (sigToken.getLeft().equals("exclamacion")) {
			Parse.add("39");
			equipara("exclamacion");
			String tipo = Y();
			if (!tipo.equals("logico"))
				GestorErrores.addError("206", ALex.line, SEMANTICO, true); // Solo se puede usar negación con
																			// variables lógicas
			else
				return "logico";
		} else if (firstU.contains(traducir(sigToken.getLeft()))) {
			Parse.add("40");
			String tipo = U();
			return tipo;
		} else {
			GestorErrores.addError("110", ALex.line, SINTACTICO); // Fallo en la expresion
			while (!follow("Y").contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return "FALLO";
	}

	private static String U() {
		if (sigToken.getLeft().equals("incrementador")) {
			Parse.add("41");
			equipara("incrementador");
			String tipo = U();
			if (!tipo.equals("entero"))
				GestorErrores.addError("207", ALex.line, SEMANTICO, true); // Solo se puede usar autoincremento con
			// variables enteras
			else
				return "entero";
		} else if (firstV.contains(traducir(sigToken.getLeft()))) {
			Parse.add("42");
			String tipos[] = V();
			return tipos[0];
		} else {
			GestorErrores.addError("110", ALex.line, SINTACTICO); // Fallo en la expresion
			while (!follow("U").contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return "FALLO";
	}

	private static String[] V() { // Tipo y ancho
		String[] res = new String[2];
		if (sigToken.getLeft().equals("id")) {
			Parse.add("43");
			int pos = Integer.parseInt(sigToken.getRight());
			String auxUltLexema = ALex.ultLexema;
			equipara("id");
			Entrada entrada = TSActual.buscarPos(pos);
			if (entrada == null)
				entrada = TSG.buscarPos(pos);
			if (!entrada.getLexema().equals(auxUltLexema))
				entrada = TSG.buscarPos(pos);

			ArrayList<String> seta = Z();
			if (entrada.getTipo().equals("function")) {
				if (entrada.getTipoParam().equals(seta)) {
					res[0] = entrada.getTipoDev();
					res[1] = "FALLO";
				} else {
					GestorErrores.addError("202", ALex.line, SEMANTICO, true); // Argumentos no coinciden
				}
			} else {
				res[0] = entrada.getTipo();
				res[1] = "FALLO";
			}
		} else if (sigToken.getLeft().equals("abreParentesis")) {
			Parse.add("44");
			equipara("abreParentesis");
			String tipo = E();
			equipara("cierraParentesis");
			res[0] = tipo;
			res[1] = "FALLO";
		} else if (sigToken.getLeft().equals("entero")) {
			Parse.add("45");
			equipara("entero");
			res[0] = "entero";
			res[1] = "1";
		} else if (sigToken.getLeft().equals("cadena")) {
			Parse.add("46");
			equipara("cadena");
			res[0] = "cadena";
			res[1] = "64";
		} else if (sigToken.getLeft().equals("logico")) {
			Parse.add("47");
			equipara("logico");
			res[0] = "logico";
			res[1] = "1";
		} else {
			GestorErrores.addError("110", ALex.line, SINTACTICO); // Fallo en la expresion
			while (!follow("V").contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return res;
	}

	private static ArrayList<String> Z() {
		if (sigToken.getLeft().equals("abreParentesis")) {
			Parse.add("48");
			equipara("abreParentesis");
			ArrayList<String> res = L();
			equipara("cierraParentesis");
			return res;
		} else if (followZ.contains(traducir(sigToken.getLeft())))
			Parse.add("49");
		else {
			GestorErrores.addError("110", ALex.line, SINTACTICO); // Fallo en la expresion
			while (!follow("Z").contains(traducir(sigToken.getLeft()))) {
				avanzarYParar();
			}
		}
		return new ArrayList<>();
	}

	private static void equipara(String t) {
		int lineaTemp = ALex.line;
		if (sigToken.getLeft().equals(t)) {
			sigToken = ALex.execALex();
			ultimaLineaFallo = ALex.line != lineaTemp ? lineaTemp : ALex.line;
		} else {
			GestorErrores.addError2("Se esperaba un '" + traducir(t) + "'", ultimaLineaFallo, SINTACTICO);
			if (sigToken.getLeft().equals("$"))
				GestorErrores.salidaPrematura();
		}
	}

	private static void avanzarYParar() {
		sigToken = ALex.execALex();
		if (sigToken.getLeft().equals("$"))
			GestorErrores.salidaPrematura();
	}

	private static void rellenarGramatica(String... strings) {
		ArrayList<ArrayList<String>> producciones = new ArrayList<>();
		ArrayList<String> prod = new ArrayList<String>();
		String s = strings[1];
		int i = 1;
		while (i < strings.length - 1) {
			if (s != "|") {
				prod.add(s);
				s = strings[++i];
			} else {
				producciones.add(prod);
				prod = new ArrayList<String>();
				s = strings[++i];
			}
		}
		if (strings[i - 1] == "|")
			prod = new ArrayList<String>();
		prod.add(strings[i]);
		producciones.add(prod);
		gram.put(strings[0], producciones);
	}

	private static void rellenarTerminalesYNoTerminales() {
		terminales.addAll(Arrays.asList("<", "-", "!", "++", "id", "(", ")", "entero", "cadena", "=", ";", "alert",
				"input", "return", "if", "while", "let", "number", "boolean", "logico", "string", "function", ",", "{",
				"}"));
		noTerminales.addAll(Arrays.asList("E", "R", "U", "V", "S", "L", "Q", "X", "B", "T", "F", "H", "A", "K", "C",
				"P", "W", "Y", "Z", "N", "M"));
	}

	private static String traducir(String s) {
		String res = s;
		switch (s) {
		case "menorEstricto":
			res = "<";
			break;
		case "menos":
			res = "-";
			break;
		case "exclamacion":
			res = "!";
			break;
		case "incrementador":
			res = "++";
			break;
		case "abreParentesis":
			res = "(";
			break;
		case "cierraParentesis":
			res = ")";
			break;
		case "igual":
			res = "=";
			break;
		case "puntoYcoma":
			res = ";";
			break;
		case "coma":
			res = ",";
			break;
		case "abreCorchete":
			res = "{";
			break;
		case "cierraCorchete":
			res = "}";
			break;
		}
		return res;
	}

	/**
	 * Devuelve todas las combinaciones de dos elementos de la gramatica
	 * 
	 * @param nT Lista de no terminales que tienen mas de dos producciones
	 * @return Lista de Pairs con left = alpha y right = beta
	 */
	private static ArrayList<Pair<String, Pair<ArrayList<String>, ArrayList<String>>>> getCombinaciones(
			ArrayList<String> nT) {// (nT,(alpha,beta))
		ArrayList<Pair<String, Pair<ArrayList<String>, ArrayList<String>>>> res = new ArrayList<>();
		for (String s : nT) {
			ArrayList<ArrayList<String>> prods = gram.get(s); // s --> a | b | c
			for (int i = 0; i < prods.size() - 1; i++) {
				for (int j = i + 1; j < prods.size(); j++) {
					res.add(new Pair<String, Pair<ArrayList<String>, ArrayList<String>>>(s,
							new Pair<ArrayList<String>, ArrayList<String>>(prods.get(i), prods.get(j))));
					res.add(new Pair<String, Pair<ArrayList<String>, ArrayList<String>>>(s,
							new Pair<ArrayList<String>, ArrayList<String>>(prods.get(j), prods.get(i))));
				}
			}
		}
		return res;
	}

	/**
	 * Funcion para validar la gramatica
	 * 
	 * @return true/false
	 */
	public static boolean LL1() {
		boolean res = false;
		ArrayList<String> masDeDosProds = new ArrayList<>();

		// Sacamos las reglas con mas de dos producciones
		for (Entry<String, ArrayList<ArrayList<String>>> regla : gram.entrySet()) {
			if (regla.getValue().size() >= 2)
				masDeDosProds.add(regla.getKey());
		}
		ArrayList<Pair<String, Pair<ArrayList<String>, ArrayList<String>>>> combinaciones = getCombinaciones(
				masDeDosProds);
		int cont = 1;
		for (Pair<String, Pair<ArrayList<String>, ArrayList<String>>> par : combinaciones) {
			if (cont % 2 != 0)
				System.out.println("Regla: " + par.getLeft());
			ArrayList<String> firstLeft = firstFollow(par.getRight().getLeft()); // a,b, c
			ArrayList<String> firstRight = firstFollow(par.getRight().getRight()); // g,
			if (cont % 2 != 0)
				System.out.println("First(" + par.getRight().getLeft() + ") =" + firstLeft + " intersección" + " First("
						+ par.getRight().getRight() + ") =" + firstRight);
			res = !firstLeft.removeAll(firstRight);
			if (cont % 2 != 0)
				System.out.println("Intersección :" + !res);
			if (!res) {
				System.out.println("La regla " + par.getLeft() + " no cumple la condición LL(1)");
				break;
			}
			if (firstRight.contains("lambda")) {
				ArrayList<String> firstLeft2 = firstFollow(par.getRight().getLeft()); // a,b, c
				ArrayList<String> followRight = follow(par.getLeft()); // a,b, c
				System.out.println("Follow(" + par.getLeft() + ") =" + followRight + " intersección" + " First("
						+ par.getRight().getLeft() + ") =" + firstLeft);
				res = !firstLeft2.removeAll(followRight);
				System.out.println("Intersección :" + !res);
				if (!res) {
					System.out.println("La regla " + par.getLeft() + " no cumple la condición LL(1)");
					break;
				}
			}
			cont++;
		}
		return res;
	}

}
