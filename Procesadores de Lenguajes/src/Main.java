import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);

		System.out.println("Introduzca:");
		System.out.println("1. Para analizar un fichero fuente.");
		System.out.println("2. Para mostrar el proceso de verificación de la condicion LL(1).");
		System.out.println("3. Para imprimir los conjuntos First y Follow de todas las producciones de la gramática.");

		int eleccion = Integer.parseInt(sc.nextLine());
		// int eleccion = 1;
		switch (eleccion) {
		case 1:
			System.out.println("Introduzca el nombre del fichero fuente con su extensión ubicado en la carpeta input.");
			String file = "";
			file = sc.nextLine();
			file = System.getProperty("user.dir") + "\\input\\" + file;
			// file = "C:\\Users\\oscar\\git\\Procesadores-de-Lenguajes\\Procesadores de
			// Lenguajes\\input\\input.txt";
			File archivo = new File(file);
			if (!archivo.exists()) {
				System.out.println("No existe el archivo.");
				// System.exit(1);
				sc.nextLine();
			}
			ASint.execASint(file);
			GestorErrores.toFile();
			Parse.toFile();
			break;
		case 2:
			ASint.cargarGramatica();
			System.out.println("\nLuego es LL(1): " + ASint.LL1());
			break;
		case 3:
			ASint.cargarGramatica();
			for (String s : ASint.gram.keySet()) {
				System.out.println("First (" + s + ") " + ASint.first(s));
				System.out.println("Follow (" + s + ") " + ASint.follow(s));
			}
			break;
		default:
			System.err.println("Opción incorrecta.");
		}
		System.out.println(
				"Pulse cualquier tecla para terminar");
		limpiarFicherosEnCasoDeError();
		sc.nextLine();
		sc.close();
	}

	/**
	 * Ya que los ficheros estan a medias si salta un error que termine la ejecución
	 * los eliminamos en caso de error ya que no son útiles
	 */
	public static void limpiarFicherosEnCasoDeError() {
		File errores = new File("Resultados\\Errores.txt");
		File p, p1, p2;
		if (errores.length() != 0) {
			p = new File(System.getProperty("user.dir") + "\\Resultados\\TS.txt");
			p1 = new File(System.getProperty("user.dir") + "\\Resultados\\tokens.txt");
			p2 = new File(System.getProperty("user.dir") + "\\Resultados\\parse.txt");
			p.delete();
			p1.delete();
			p2.delete();
		}

	}
}
