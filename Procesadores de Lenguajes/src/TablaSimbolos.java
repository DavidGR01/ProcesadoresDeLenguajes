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
	private int buscarTS(String str) {
		return map.get(str);
	}

	/**
	 * Inserta en ambas estructuras de datos y devuelve la nueva posicion.-1 Si falla algo aunque no deberia
	 * @param ent
	 * @return
	 */
	private int insertarTS(Entrada ent) {
		// Ya deberia haberse comprobado esto pero bueno
		if (buscarTS(ent.getLexema()) == -1)
			return -1;
		int pos = TS.size();
		TS.add(ent);
		map.put(ent.getLexema(), pos);
		return pos;
	}

}
