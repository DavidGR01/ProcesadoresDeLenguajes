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
	private static ArrayList<String> firstT;

	private static ArrayList<String> followP;
	private static ArrayList<String> followC;
	private static ArrayList<String> followA;
	private static ArrayList<String> followK;
	private static ArrayList<String> followH;

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
		followP = follow("P");
		followC = follow("C");
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
		// TODO Auto-generated method stub
		
	}

	private static void equipara(String t) {

		if (sigToken.getLeft() == t)
			try {
				sigToken = ALex.execALex();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
