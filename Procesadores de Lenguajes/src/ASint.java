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
	}

	private static void rellenarTerminalesYNoTerminales() {
		terminales.add("");
		noTerminales.add("");
	}

	// Funcion para validar la gramatica

}
