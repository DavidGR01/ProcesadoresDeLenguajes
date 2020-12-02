import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ASint {

	// Estructura para la gramatica
	private static HashMap<String, ArrayList<ArrayList<String>>> gram = new HashMap<>();

	// Terminales y no terminales
	private static ArrayList<String> terminales;
	private static ArrayList<String> noTerminales;

	private static int posToken = 0;
	private static Pair<String, String> sigToken = null;

	public static void execASint() {

		// Rellenamos la gramatica
		rellenarGramatica();
		rellenarTerminalesYNoTerminales();

		// Axioma
		P();
	}

	// First
	private static ArrayList<String> first(String s) {

		ArrayList<String> res = new ArrayList<>();

		if (terminales.contains(s)) {
			res.add(s);
		} else if (noTerminales.contains(s)) {
			ArrayList<ArrayList<String>> producciones = gram.get(s);
			for (ArrayList<String> prod : producciones) {
				if (prod.size() == 1 && prod.contains("lambda")) {
					res.add("lambda");
					producciones.remove(prod);
				}
				for (String elem : prod) {
					ArrayList<String> fir = first(elem); // Aqui llamar a first_rec o algo asi para
					// poder tener en cuenta que no se ha derivado lambda

					boolean seguir = false;
					seguir = fir.remove("lambda");
					res.addAll(fir);
					if (seguir)
						return res;
				}
			}
		}
		return res;
	}

	// Follow
	private static ArrayList<String> follow(String NOterminal) {
		return null;
	}

	// Producciones

	private static void P() {

		ArrayList<String> firstB = first("B");
		ArrayList<String> firstF = first("F");
		ArrayList<String> followP = follow("P");

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
	}

	private static void B() {

	}

	private static void rellenarGramatica() {
		ArrayList<ArrayList<String>> producciones = new ArrayList<>();
		ArrayList<String> prod = new ArrayList<>();
		
		//AXIOMA
		// P->BP|FP|lambda
		producciones.clear();
		prod.clear();
		prod.add("B");
		prod.add("P");
		producciones.add(prod);
		prod.clear();
		prod.add("F");
		prod.add("P");
		producciones.add(prod);
		prod.clear();
		prod.add("lambda");
		producciones.add(prod);
		gram.put("P", producciones);
		//FUNCIONES
		//F-> function H id(A){C}
		producciones.clear();
		prod.clear();
		prod.add("function");
		prod.add("H");
		prod.add("id");
		prod.add("(");
		prod.add("A");
		prod.add(")");
		prod.add("{");
		prod.add("C");
		prod.add("}");
		producciones.add(prod);
		gram.put("F",producciones);
		//H->T|lambda
		producciones.clear();
		prod.clear();
		prod.add("T");
		producciones.add(prod);
		prod.clear();
		prod.add("lambda");
		producciones.add(prod);
		gram.put("H",producciones);
		//A->TidK|lambda
		producciones.clear();
		prod.clear();
		prod.add("T");
		prod.add("id");
		prod.add("K");
		producciones.add(prod);
		prod.clear();
		prod.add("lambda");
		producciones.add(prod);
		gram.put("A",producciones);
		//K->,TidK|lambda
		producciones.clear();
		prod.clear();
		prod.add(",");
		prod.add("T");
		prod.add("id");
		prod.add("K");
		producciones.add(prod);
		prod.clear();
		prod.add("lambda");
		producciones.add(prod);
		gram.put("K",producciones);
		//C->BC|lambda
		producciones.clear();
		prod.clear();
		prod.add("B");
		prod.add("C");
		producciones.add(prod);
		prod.clear();
		prod.add("lambda");
		producciones.add(prod);
		gram.put("C",producciones);
		//SENTENCIAS COMPUESTAS Y DECLARACIONES
		producciones.clear();
		prod.clear();
		prod.add("if");
		prod.add("(");
		prod.add("E");
		prod.add(")");
		prod.add("S");
		producciones.add(prod);
		prod.clear();
		prod.add("S");
		//B->if(E)S|S|while(E){D}|letTid;
		producciones.add(prod);
		prod.clear();
		prod.add("while");
		prod.add("(");
		prod.add("E");
		prod.add(")");
		prod.add("{");
		prod.add("C"); //Cambio en la gramatica
		prod.add("}");
		producciones.add(prod);
		prod.clear();
		prod.add("let");
		prod.add("T");
		prod.add("id");
		prod.add(";");
		producciones.add(prod);
		gram.put("B",producciones);
		//T->number|boolean|string
		producciones.clear();
		prod.clear();
		prod.add("number");
		producciones.add(prod);
		prod.clear();
		prod.add("boolean");
		producciones.add(prod);
		prod.clear();
		prod.add("string");
		producciones.add(prod);
		gram.put("T",producciones);
		//SENTENCIAS SIMPLES
		
		
		
	
	}

	private static void rellenarTerminalesYNoTerminales() {
		//Lambda es un no terminal?
		//TERMINALES
		terminales.add("<");
		terminales.add("-");
		terminales.add("!");
		terminales.add("+");
		terminales.add("id");
		terminales.add("(");
		terminales.add(")");
		terminales.add("entero");
		terminales.add("cadena");
		terminales.add("=");
		terminales.add(";");
		terminales.add("alert");
		terminales.add("input");
		terminales.add("return");
		terminales.add(",");
		terminales.add("if");
		terminales.add("while");
		terminales.add("{");
		terminales.add("}");
		terminales.add("let");
		terminales.add("number");
		terminales.add("boolean");
		terminales.add("string");
		terminales.add("function");
		
		//NO TERMINALES
		noTerminales.add("A");
		noTerminales.add("B");
		noTerminales.add("C");
		noTerminales.add("D");
		noTerminales.add("E");
		noTerminales.add("F");
		noTerminales.add("H");
		noTerminales.add("K");
		noTerminales.add("L");
		noTerminales.add("P");
		noTerminales.add("Q");
		noTerminales.add("R");
		noTerminales.add("S");
		noTerminales.add("T");
		noTerminales.add("U");
		noTerminales.add("V");
		noTerminales.add("X");
	}

	// Funcion para validar la gramatica

}
