import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ALex {

	static Pair<Integer, String>[][] matriz = new Pair[20][21];
	static HashMap<String, Integer> columnas = new HashMap<>();

	public static void main(String[] args) throws IOException {

		rellenarMatriz();

		// Vamos leyendo el archivo

		File f = new File(args[0]); // El archivo tiene que estar en la carpeta de files
		FileReader fr = new FileReader(f); // Creation of File Reader object
		BufferedReader br = new BufferedReader(fr); // Creation of BufferedReader object

		int estado = 0;
		char car = (char) br.read();
		String accion = null;
		// Mientras no leamos EOF
		while (car != -1) {

			do {

				accion = matriz[estado][columna(estado, car)].getRight();
				estado = matriz[estado][columna(estado, car)].getLeft();

				if (estado == -1)
					System.out.println("Error!");
				else {
					switch (accion) {
					case "A":
						car = (char) br.read();
					}
				}

			} while (estado < 8);
		}

		br.close();
	}

	private static void rellenarMatriz() {
		// Inicialiar todo a -1 y null
		// Rellenar con los datos buenos
	}

	/**
	 * Devuelve la columna apropiada a cada character
	 * 
	 * @param estado
	 * @param c
	 * @return
	 */
	private static int columna(int estado, char c) {
		// l:letras min y mayus
		// d:digitos
		// c:char - {<eol>}
		// p:char-{'}
		// r:o.c. - {d}
		// q:o.c. - {l,_,d}
		if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
			return 1;
		if (c >= 48 && c <= 57)
			return 2;
		if (estado == 19 && c != 10) // Renombrar estados PUTAS!
			return 3;
		if (estado == 6 && c != 39)
			return 4;
		if (estado == 3 && c < 48 && c > 57)
			return 6;
		return 5;
	}

}