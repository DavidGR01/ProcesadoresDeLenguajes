import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ASint {

	// Estructura para la gramatica
	private static HashMap<String, ArrayList<ArrayList<String>>> gram = new HashMap<>();

	// Terminales y no terminales
	private static ArrayList<String> terminales = new ArrayList<String>();
	private static ArrayList<String> noTerminales = new ArrayList<String>();

	private static int posToken = 0;
	private static Pair<String, String> sigToken = null;

	// First y follow
	private static ArrayList<String> firstB;
	private static ArrayList<String> firstF;
	private static ArrayList<String> firstR, firstU, firstV, firstE, firstS,firstT;

	private static ArrayList<String> followP;
	private static ArrayList<String> followC, followY, followZ, followX, followL, followQ,followA,followK,followH;

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
		rellenarGramatica("V", "id", "Z", "|", "(", "E", ")", "|", "entero", "|", "cadena");
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
		followX = follow("X");
		followP = follow("P");
		followC = follow("C");
		followY = follow("Y");
		followZ = follow("Z");
		followL = follow("L");
		followQ = follow("Q");
		firstT = first("T");
		followA = follow("A");
		followH = follow("H");
		followK = follow("K");

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
					System.out.println("Elemento  " + elem);
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
						if (s != "lambda")
							res.add(s);
					}
					cont += 1;
					res1 = first(prod.get(cont));
					for (String s : res1)
						if (s != "lambda")
							res.add(s);
						else if (cont == prod.size() - 1 && s == "lambda")
							res.add(s);
					return res;
				} else
					return first(prod.get(cont));

			} else
				res.add("lambda");
			cont++;
		}

		return res;
	}

	// Follow
	private static ArrayList<String> follow(String NOterminal) {
		return null;
	}

	// Producciones

	private static void P() {

		if (firstB.contains(sigToken.getLeft())) {
			Parse.add("1");
			B();
			P();
		} else if ((firstF.contains(sigToken.getLeft()))) {
			Parse.add("2");
			F();
			P();
		} else if (followP.contains(sigToken.getLeft())) {
			Parse.add("3");
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void F() {
		if (sigToken.getLeft() == "function") {
			Parse.add("4");
			equipara("function");
			H();
			equipara("id");
			equipara("(");
			A();
			equipara(")");
			equipara("{");
			C();
			equipara("}");
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void T() {
		if (sigToken.getLeft() == "number") {
			Parse.add("5");
			equipara("number");
		} else if (sigToken.getLeft() == "boolean") {
			Parse.add("6");
			equipara("boolean");
		} else if (sigToken.getLeft() == "string") {
			Parse.add("7");
			equipara("string");
		} else
			GestorErrores.addError("555", ALex.line, "Lexico");

	}

	private static void H() {

		if (firstT.contains(sigToken.getLeft())) {
			Parse.add("8");
			T();

		} else if (followH.contains(sigToken.getLeft()))
			Parse.add("9");
		else
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
	}

	private static void A() {

		if (firstT.contains(sigToken.getLeft())) {
			Parse.add("10");
			T();
			equipara("id");
			K();
		} else if (followA.contains(sigToken.getLeft()))
			Parse.add("11");
		else
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
	}

	private static void K() {
		if (sigToken.getLeft() == ",") {
			Parse.add("12");
			T();
			equipara("id");
			K();
		} else if (followK.contains(sigToken.getLeft()))
			Parse.add("13");
		else
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
	}

	private static void C() {

		if (firstB.contains(sigToken.getLeft())) {
			Parse.add("14");
			B();
			C();
		} else if (firstF.contains(sigToken.getLeft())) {
			Parse.add("15");
			F();
			C();
		} else if (followC.contains(sigToken.getLeft()))
			Parse.add("16");
		else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}
  
	private static void B() {
		if (sigToken.getLeft().equals("if")) {
			Parse.add("17");
			equipara("if");
			equipara("(");
			E();
			equipara(")");
			S();
		} else if (firstS.contains(sigToken.getLeft())) {
			Parse.add("18");
			S();
		} else if (sigToken.getLeft().equals("while")) {
			Parse.add("19");
			equipara("while");
			equipara("(");
			E();
			equipara(")");
			equipara("{");
			C();
			equipara("}");
		} else if (sigToken.getLeft().equals("let")) {
			Parse.add("20");
			equipara("let");
			T();
			equipara("id");
			equipara(";");
		} else if (sigToken.getLeft().equals("return")) {
			Parse.add("24");
			equipara("return");
			X();
			equipara(";");
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
			equipara("(");
			E();
			equipara(")");
			equipara(";");
		} else if (sigToken.getLeft().equals("input")) {
			Parse.add("23");
			equipara("input");
			equipara("(");
			equipara("id");
			equipara(")");
			equipara(";");
		} else if (sigToken.getLeft().equals("return")) {
			Parse.add("24");
			equipara("return");
			X();
			equipara(";");
		}
	}

	private static void W() {
		if (sigToken.getLeft().equals("=")) {
			Parse.add("25");
			equipara("=");
			E();
			equipara(";");
		} else if (sigToken.getLeft().equals("(")) {
			Parse.add("26");
			equipara("(");
			L();
			equipara(")");
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void L() {
		if (firstE.contains(sigToken.getLeft())) {
			Parse.add("27");
			E();
			Q();
		} else if (followL.contains(sigToken.getLeft())) {
			Parse.add("28");
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void Q() {
		if (sigToken.getLeft().equals(",")) {
			Parse.add("29");
			equipara(",");
			E();
			Q();
		} else if (followQ.contains(sigToken.getLeft())) {
			Parse.add("30");
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void X() {
		if (firstE.contains(sigToken.getLeft())) {
			Parse.add("31");
			E();
		} else if (followX.contains(sigToken.getLeft())) {
			Parse.add("32");
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void E() {
		if (firstR.contains(sigToken.getLeft())) {
			Parse.add("33");
			R();
			Y();
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void Y() {
		if (sigToken.getLeft().equals("<")) {
			Parse.add("34");
			equipara("<");
			R();
			Y();
		} else if (sigToken.getLeft().equals("-")) {
			Parse.add("35");
			equipara("-");
			R();
			Y();
		} else if (followY.contains(sigToken.getLeft())) {
			Parse.add("36");
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void R() {
		if (sigToken.getLeft().equals("!")) {
			Parse.add("37");
			equipara("!");
			R();
		} else if (firstU.contains(sigToken.getLeft())) {
			Parse.add("38");
			U();
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void U() {
		if (sigToken.getLeft().equals("++")) {
			Parse.add("39");
			equipara("++");
			U();
		} else if (firstV.contains(sigToken.getLeft())) {
			Parse.add("40");
			V();
		} else if (followY.contains(sigToken.getLeft())) {
			Parse.add("36");
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void V() {
		if (sigToken.getLeft().equals("id")) {
			Parse.add("41");
			equipara("id");
			Z();
		} else if (sigToken.getLeft().equals("(")) {
			Parse.add("42");
			equipara("(");
			E();
			equipara(")");
		} else if (sigToken.getLeft().equals("entero")) {
			Parse.add("43");
			equipara("entero");
		} else if (sigToken.getLeft().equals("cadena")) {
			Parse.add("44");
			equipara("cadena");
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void Z() {
		if (sigToken.getLeft().equals("(")) {
			Parse.add("45");
			equipara("(");
			L();
			equipara(")");
		} else if (followZ.contains(sigToken.getLeft())) {
			Parse.add("46");
		} else {
			GestorErrores.addError("555", ALex.line, "Léxico"); // Falta código de error
		}
	}

	private static void equipara(String t) {

		if (sigToken.getLeft() == t)
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
				"input", "return", "if", "while", "let", "number", "boolean", "string", "function", ",", "{", "}"));
		noTerminales.addAll(Arrays.asList("E", "R", "U", "V", "S", "L", "Q", "X", "B", "T", "F", "H", "A", "K", "C",
				"P", "W", "Y", "Z"));
	}

	// Funcion para validar la gramatica

}
