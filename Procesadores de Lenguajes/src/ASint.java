import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class ASint {

	// Estructura para la gramatica
	static HashMap<String, ArrayList<ArrayList<String>>> gram = new HashMap<>();

	// Terminales y no terminales
	private static ArrayList<String> terminales = new ArrayList<String>();
	private static ArrayList<String> noTerminales = new ArrayList<String>();

	private static int posToken = 0;
	private static Pair<String, String> sigToken = null;

	// First y follow
	private static ArrayList<String> firstR, firstU, firstV, firstE, firstS, firstT, firstF, firstB;

	private static ArrayList<String> followP, followC, followY, followZ, followX, followL, followQ, followA, followK,
			followH;

	public static void execASint() throws IOException {

		rellenarGramatica("P", "B", "P", "|", "F", "P", "|", "lambda");
		rellenarGramatica("F", "function", "H", "id", "(", "A", ")", "{", "C", "}");
		rellenarGramatica("T", "number", "|", "boolean", "|", "string");
		rellenarGramatica("H", "T", "|", "lambda");
		rellenarGramatica("A", "T", "id", "K", "|", "lambda");
		rellenarGramatica("K", ",", "T", "id", "K", "|", "lambda");
		rellenarGramatica("C", "B", "C", "|", "F", "C", "|", "lambda");
		rellenarGramatica("B", "if", "(", "E", ")", "S", "|", "S", "|", "while", "(", "E", ")", "{", "C", "}", "|",
				"let", "T", "id", ";");
		// Factorizacion corregida
		rellenarGramatica("S", "id", "W", "|", "alert", "(", "E", ")", ";", "|", "input", "(", "id", ")", ";", "|",
				"return", "X", ";");
		rellenarGramatica("W", "=", "E", ";", "|", "(", "L", ")");

		rellenarGramatica("L", "E", "Q", "|", "lambda");
		rellenarGramatica("Q", ",", "E", "Q", "|", "lambda");
		rellenarGramatica("X", "E", "|", "lambda");
		// Recursividad corregida
		rellenarGramatica("E", "R", "Y");
		rellenarGramatica("Y", "<", "R", "Y", "|", "-", "R", "Y", "|", "lambda");

		rellenarGramatica("R", "!", "R", "|", "U");
		rellenarGramatica("U", "++", "U", "|", "V");
		// Factorizacion corregida
		rellenarGramatica("V", "id", "Z", "|", "(", "E", ")", "|", "entero", "|", "cadena", "|", "logico");
		rellenarGramatica("Z", "(", "L", ")", "|", "lambda");
		rellenarTerminalesYNoTerminales();

		// Axioma
		firstB = first("B");
		firstF = first("F");
		firstR = first("R");
		firstU = first("U");
		firstV = first("V");
		firstE = first("E");
		firstS = first("S");
		firstT = first("T");
		followX = follow("X");
		followP = follow("P");
		followC = follow("C");
		followY = follow("Y");
		followZ = follow("Z");
		followL = follow("L");
		followQ = follow("Q");
		followA = follow("A");
		followH = follow("H");
		followK = follow("K");

		sigToken = ALex.execALex();

		P();

	}

	public static ArrayList<String> first(String s) {

		ArrayList<String> res = new ArrayList<String>();

		if (gram.get(s) == null) {
			res.add(s);
		} else {
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
						if (cont == prod.size() - 1) {
							res.add(s);
						} else {
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
		ArrayList<String> res = new ArrayList<>();
		return firstRec(prod, res, 0);
	}

	// Follow
	static ArrayList<String> follow(String nt) {
		ArrayList<String> res = new ArrayList<>();
		if (nt.equals("P"))
			res.add("$");
		for (Entry<String, ArrayList<ArrayList<String>>> regla : gram.entrySet()) {
			for (ArrayList<String> prod : regla.getValue()) {
				if (!regla.getKey().equals(prod.get(prod.size() - 1))) {
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

	public static ArrayList<String> beta(ArrayList<String> prod, String nt) {
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
		} else if (followP.contains(traducir(sigToken.getLeft()))) {
			Parse.add("3");
		} else {
			GestorErrores.addError("555", ALex.line, "1"); // Falta código de error
		}
	}

	private static void F() {
		if (sigToken.getLeft().equals("function")) {
			Parse.add("4");
			equipara("function");
			H();
			equipara("id");
			equipara("abreParentesis");
			A();
			equipara("cierraParentesis");
			equipara("abreCorchete");
			C();
			equipara("cierraCorchete");
		} else {
			GestorErrores.addError("555", ALex.line, "2"); // Falta código de error
		}
	}

	private static void T() {
		if (sigToken.getLeft().equals("number")) {
			Parse.add("5");
			equipara("number");
		} else if (sigToken.getLeft().equals("boolean")) {
			Parse.add("6");
			equipara("boolean");
		} else if (sigToken.getLeft().equals("string")) {
			Parse.add("7");
			equipara("string");
		} else
			GestorErrores.addError("555", ALex.line, "3");

	}

	private static void H() {

		if (firstT.contains(traducir(sigToken.getLeft()))) {
			Parse.add("8");
			T();
		} else if (followH.contains(traducir(sigToken.getLeft())))
			Parse.add("9");
		else
			GestorErrores.addError("555", ALex.line, "4"); // Falta código de error
	}

	private static void A() {

		if (firstT.contains(traducir(sigToken.getLeft()))) {
			Parse.add("10");
			T();
			equipara("id");
			K();
		} else if (followA.contains(traducir(sigToken.getLeft())))
			Parse.add("11");
		else
			GestorErrores.addError("555", ALex.line, "5"); // Falta código de error
	}

	private static void K() {
		if (sigToken.getLeft().equals("coma")) {
			Parse.add("12");
			equipara("coma");
			T();
			equipara("id");
			K();
		} else if (followK.contains(traducir(sigToken.getLeft())))
			Parse.add("13");
		else
			GestorErrores.addError("555", ALex.line, "6"); // Falta código de error
	}

	private static void C() {

		if (firstB.contains(traducir(sigToken.getLeft()))) {
			Parse.add("14");
			B();
			C();
		} else if (firstF.contains(traducir(sigToken.getLeft()))) {
			Parse.add("15");
			F();
			C();
		} else if (followC.contains(traducir(sigToken.getLeft())))
			Parse.add("16");
		else {
			GestorErrores.addError("555", ALex.line, "7"); // Falta código de error
		}
	}

	private static void B() {
		if (sigToken.getLeft().equals("if")) {
			Parse.add("17");
			equipara("if");
			equipara("abreParentesis");
			E();
			equipara("cierraParentesis");
			S();
		} else if (firstS.contains(traducir(sigToken.getLeft()))) {
			Parse.add("18");
			S();
		} else if (sigToken.getLeft().equals("while")) {
			Parse.add("19");
			equipara("while");
			equipara("abreParentesis");
			E();
			equipara("cierraParentesis");
			equipara("abreCorchete");
			C();
			equipara("cierraCorchete");
		} else if (sigToken.getLeft().equals("let")) {
			Parse.add("20");
			equipara("let");
			T();
			equipara("id");
			equipara("puntoYcoma");
		} else if (sigToken.getLeft().equals("return")) {
			Parse.add("24");
			equipara("return");
			X();
			equipara("puntoYcoma");
		}
	}

	private static void S() {
		if (sigToken.getLeft().equals("id")) {
			Parse.add("21");
			equipara("id");
			W();
		} else if (sigToken.getLeft().equals("alert")) {
			Parse.add("22");
			equipara("alert");
			equipara("abreParentesis");
			E();
			equipara("cierraParentesis");
			equipara("puntoYcoma");
		} else if (sigToken.getLeft().equals("input")) {
			Parse.add("23");
			equipara("input");
			equipara("abreParentesis");
			equipara("id");
			equipara("cierraParentesis");
			equipara("puntoYcoma");
		} else if (sigToken.getLeft().equals("return")) {
			Parse.add("24");
			equipara("return");
			X();
			equipara("puntoYcoma");
		}
	}

	private static void W() {
		if (sigToken.getLeft().equals("igual")) {
			Parse.add("25");
			equipara("igual");
			E();
			equipara("puntoYcoma");
		} else if (sigToken.getLeft().equals("abreParentesis")) {
			Parse.add("26");
			equipara("abreParentesis");
			L();
			equipara("cierraParentesis");
		} else {
			GestorErrores.addError("555", ALex.line, "8"); // Falta código de error
		}
	}

	private static void L() {
		if (firstE.contains(traducir(sigToken.getLeft()))) {
			Parse.add("27");
			E();
			Q();
		} else if (followL.contains(traducir(sigToken.getLeft()))) {
			Parse.add("28");
		} else {
			GestorErrores.addError("555", ALex.line, "9"); // Falta código de error
		}
	}

	private static void Q() {
		if (sigToken.getLeft().equals("coma")) {
			Parse.add("29");
			equipara("coma");
			E();
			Q();
		} else if (followQ.contains(traducir(sigToken.getLeft()))) {
			Parse.add("30");
		} else {
			GestorErrores.addError("555", ALex.line, "10"); // Falta código de error
		}
	}

	private static void X() {
		if (firstE.contains(traducir(sigToken.getLeft()))) {
			Parse.add("31");
			E();
		} else if (followX.contains(traducir(sigToken.getLeft()))) {
			Parse.add("32");
		} else {
			GestorErrores.addError("555", ALex.line, "11"); // Falta código de error
		}
	}

	private static void E() {
		if (firstR.contains(traducir(sigToken.getLeft()))) {
			Parse.add("33");
			R();
			Y();
		} else {
			GestorErrores.addError("555", ALex.line, "12"); // Falta código de error
		}
	}

	private static void Y() {
		if (sigToken.getLeft().equals("menorEstricto")) {
			Parse.add("34");
			equipara("menorEstricto");
			R();
			Y();
		} else if (sigToken.getLeft().equals("menos")) {
			Parse.add("35");
			equipara("menos");
			R();
			Y();
		} else if (followY.contains(traducir(sigToken.getLeft()))) {
			Parse.add("36");
		} else {
			GestorErrores.addError("555", ALex.line, "13"); // Falta código de error
		}
	}

	private static void R() {
		if (sigToken.getLeft().equals("exclamacion")) {
			Parse.add("37");
			equipara("exclamacion");
			R();
		} else if (firstU.contains(traducir(sigToken.getLeft()))) {
			Parse.add("38");
			U();
		} else {
			GestorErrores.addError("555", ALex.line, "14"); // Falta código de error
		}
	}

	private static void U() {
		if (sigToken.getLeft().equals("incrementador")) {
			Parse.add("39");
			equipara("incrementador");
			U();
		} else if (firstV.contains(traducir(sigToken.getLeft()))) {
			Parse.add("40");
			V();
		} else if (followY.contains(traducir(sigToken.getLeft()))) {
			Parse.add("36");
		} else {
			GestorErrores.addError("555", ALex.line, "15"); // Falta código de error
		}
	}

	private static void V() {
		if (sigToken.getLeft().equals("id")) {
			Parse.add("41");
			equipara("id");
			Z();
		} else if (sigToken.getLeft().equals("abreParentesis")) {
			Parse.add("42");
			equipara("abreParentesis");
			E();
			equipara("cierraParentesis");
		} else if (sigToken.getLeft().equals("entero")) {
			Parse.add("43");
			equipara("entero");
		} else if (sigToken.getLeft().equals("cadena")) {
			Parse.add("44");
			equipara("cadena");
		} else if (sigToken.getLeft().equals("logico")) {
			Parse.add("45");
			equipara("logico");
		} else {
			GestorErrores.addError("555", ALex.line, "16"); // Falta código de error
		}
	}

	private static void Z() {
		if (sigToken.getLeft().equals("abreParentesis")) {
			Parse.add("46");
			equipara("abreParentesis");
			L();
			equipara("cierraParentesis");
		} else if (followZ.contains(traducir(sigToken.getLeft()))) {
			Parse.add("47");
		} else {
			GestorErrores.addError("555", ALex.line, "17"); // Falta código de error
		}
	}

	private static void equipara(String t) {

		if (sigToken.getLeft().equals(t))
			try {
				sigToken = ALex.execALex();
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			GestorErrores.addError("555", ALex.line, "Sintactico");

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
				"P", "W", "Y", "Z"));
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

	// Funcion para validar la gramatica

}
