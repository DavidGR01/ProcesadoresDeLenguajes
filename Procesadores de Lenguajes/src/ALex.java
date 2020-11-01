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
		String lexema = null;
		int valor = 0;
		Pair<String, String> tokenSimbolo = null, tokenIncrementador = null, tokenCadena = null;
		Pair<String,Integer> tokenEntero = null, tokenId = null;
		
		//Mientras no leamos EOF
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
						break;
					case "B":
						lexema = "";
						lexema += car;
						car = (char) br.read();
						break;
					case "C":
						valor = Character.getNumericValue(car);
						car = (char) br.read();
						break;
					case "D":
						valor = valor*10 + Character.getNumericValue(car);
						car = (char) br.read();
						break;
					case "E":
						 tokenEntero = new Pair<String,Integer>("entero",valor);
						System.out.println(tokenEntero);
						car = (char) br.read();
						break;
					case "F":
						 tokenIncrementador = new Pair<String,String>("incrementador","-");
						System.out.println(tokenIncrementador);
						car = (char) br.read();
						break;
					case "G":
						lexema += car;
						car = (char) br.read();
						break;
					case "H":
						lexema += car;
						tokenId = new Pair<String,Integer>("id",posTS(lexema));
						System.out.println(tokenId);
						car = (char) br.read();
						break;
					case "I":
						lexema += car;
						tokenCadena = new Pair<String,String>("cadena",lexema);
						System.out.println(tokenCadena);
						car = (char) br.read();
						break;
					case "J":
						tokenSimbolo = new Pair<String,String>("abre-parentesis","-");
						System.out.println(tokenSimbolo);
						car = (char) br.read();
						break;
					case "K":
						tokenSimbolo = new Pair<String,String>("cierra-parentesis","-");
						System.out.println(tokenSimbolo);
						car = (char) br.read();
						break;
					case "L":
						tokenSimbolo = new Pair<String,String>("abre-corchete","-");
						System.out.println(tokenSimbolo);
						car = (char) br.read();
						break;
					case "M":
						tokenSimbolo = new Pair<String,String>("cierra-corchete","-");
						System.out.println(tokenSimbolo);
						car = (char) br.read();
						break;
					case "N":
						tokenSimbolo = new Pair<String,String>("menos","-");
						System.out.println(tokenSimbolo);
						car = (char) br.read();
						break;
					case "Ñ":
						tokenSimbolo = new Pair<String,String>("menor-estricto","-");
						System.out.println(tokenSimbolo);
						car = (char) br.read();
						break;
					case "O":
						tokenSimbolo = new Pair<String,String>("exclamación","-");
						System.out.println(tokenSimbolo);
						car = (char) br.read();
						break;
					case "P":
						tokenSimbolo = new Pair<String,String>("punto-y-coma","-");
						System.out.println(tokenSimbolo);
						car = (char) br.read();
						break;
					case "Q":
						tokenSimbolo = new Pair<String,String>("igual","-");
						System.out.println(tokenSimbolo);
						car = (char) br.read();
						break;
					case "R":
						break;
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
	private static int posTS(String lexema) {
		return 0;
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
		if( estado == 0 && ( c == 9 || c == 32))
			return 0;
		if ((estado == 0 || estado == 1 ) &&(c >= 65 && c <= 90) || (c >= 97 && c <= 122))
			return 1;
		if (-1 < estado && estado < 3 &&c >= 48 && c <= 57)
			return 2;
		if (estado == 4 && c != 10) 
			return 3;
		if (estado == 5 && c != 39)
			return 4;
		if (estado == 2 && c < 48 && c > 57)
			return 6;
		if ( estado == 1 && c == 95)
			return 7;
		if( (estado == 0 || estado == 6) && c == 43)
			return 8;
		if(estado == 0) {
			if(c == 40) return 9;
			if( c == 41) return 10;
			if( c == 123) return 11;
			if( c == 125) return 12;
			if( c == 45) return 13;
			if( c == 60) return 14;
			if( c == 33) return 15;
			if( c == 59) return 16;
			if( c == 61) return 17;
			if( c == 47) return 18;
			if( c == 39) return 119;
		}
		if(estado == 3 && c == 47)
			return 18;
		if(estado == 5 && c == 39)
			return 19;
		if(c == -1)
			return 20;
		return 5;
	}

}