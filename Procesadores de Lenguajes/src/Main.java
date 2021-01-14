import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);

		System.out.println("Introduce:");
		System.out.println("1. Para analizar un ficher fuente.");
		System.out.println("2. Para el proceso de verificación de la condicion LL(1).");
		System.out.println("3. Para imprimir los First y Follow de la gramática.");

		//int eleccion = Integer.parseInt(sc.nextLine());
		int eleccion = 1;
		switch (eleccion) {
		case 1:
			System.out.println(
					"Introduce el nombre del fichero fuente(En el mismo directorio del ejecutable y con extensión).");
			System.out.println("");
			String file = "";
			//file = sc.nextLine();
			file = System.getProperty("user.dir") + "\\" + file;
			file ="C:\\Users\\oscar\\git\\Procesadores-de-Lenguajes\\Procesadores de Lenguajes\\input.txt";
			File archivo = new File(file);
			if (!archivo.exists()) {
				System.out.println("No existe el archivo.");
				//System.exit(1);
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
		System.out.println();
		//sc.nextLine();
	}
}
