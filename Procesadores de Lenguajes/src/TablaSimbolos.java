import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TablaSimbolos {

	static HashMap<String, Integer> map = new HashMap<>();
	List<Entrada> TS = new ArrayList<>();

	public TablaSimbolos() {
	}

	/**
	 * Devuelve la posicion de str si existe y -1 si no
	 * 
	 * @param str
	 * @return
	 */
	int buscarTS(String str) {
		Integer pos = map.get(str);
		return pos == null ? -1 : pos;
	}

	/**
	 * Inserta en ambas estructuras de datos y devuelve la nueva posicion. -1 Si
	 * falla algo aunque no debería
	 * 
	 * @param ent
	 * @return
	 */
	int insertarTS(Entrada ent) {
		// Ya deberia haberse comprobado esto pero bueno
		if (buscarTS(ent.getLexema()) == -1)
			return -1;
		int pos = TS.size();
		TS.add(ent);
		map.put(ent.getLexema(), pos);
		return pos;
	}

	/**
	 * Guardamos el estado actual de la TS en el fichero TS.txt
	 * 
	 * @throws IOException
	 */
	static void toFile() throws IOException {
		// Sobreescribe cualquier archivo anterior con el mismo nombre
		FileWriter myWriter = new FileWriter("TS.txt");
		myWriter.write("TABLA PRINCIPAL #1:");

		myWriter.close();
	}

}
