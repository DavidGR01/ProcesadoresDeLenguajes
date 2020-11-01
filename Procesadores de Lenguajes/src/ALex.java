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

		// Inicialiar todo a -2 y null
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++)
				matriz[i][j] = new Pair<Integer, String>(-2, null);
		}

		// Rellenar con los datos buenos

		// Primera fila
		matriz[0][0] = new Pair<Integer, String>(0, "A");
		matriz[0][1] = new Pair<Integer, String>(1, "B");
		matriz[0][2] = new Pair<Integer, String>(3, "C");
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
		matriz[0][18] = new Pair<Integer, String>(3, "A");
		matriz[0][19] = new Pair<Integer, String>(5, "B");

		// Segunda fila
		matriz[1][1] = new Pair<Integer, String>(1, "G");
		matriz[1][2] = new Pair<Integer, String>(1, "G");
		matriz[1][5] = new Pair<Integer, String>(7, "H");
		matriz[1][7] = new Pair<Integer, String>(1, "G");

		// Tercera fila
		matriz[2][2] = new Pair<Integer, String>(3, "D");
		matriz[2][6] = new Pair<Integer, String>(8, "E");

		// Cuarta fila
		matriz[3][18] = new Pair<Integer, String>(4, "A");

		// Quinta fila
		matriz[4][3] = new Pair<Integer, String>(4, "G");
		matriz[4][20] = new Pair<Integer, String>(0, "R");

		// Sexta fila
		matriz[4][3] = new Pair<Integer, String>(4, "G");
		matriz[4][20] = new Pair<Integer, String>(0, "R");

		// Septima fila
		matriz[5][4] = new Pair<Integer, String>(5, "G");
		matriz[5][19] = new Pair<Integer, String>(9, "I");

		// Octava fila
		matriz[6][8] = new Pair<Integer, String>(10, "F");

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