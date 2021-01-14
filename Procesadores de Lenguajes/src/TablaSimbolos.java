import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TablaSimbolos {

	private HashMap<String, Integer> map;
	private List<Entrada> TS;

	private int id;

	public TablaSimbolos() {
		this.map = new HashMap<>();
		this.TS = new ArrayList<>();
		id = TSCounter.getCounter();
	}

	/**
	 * Devuelve la posicion de str si existe y -1 si no
	 * 
	 * @param str
	 * @return
	 */
	public int buscarTS(String str) {
		Integer pos = map.get(str);
		return pos == null ? -1 : pos;
	}

	public Entrada buscarPos(int pos) {
		if (pos >= TS.size())
			return null;
		return TS.get(pos);
	}

	/**
	 * Inserta en ambas estructuras de datos y devuelve la nueva posicion. -1 Si
	 * falla algo aunque no debería
	 * 
	 * @param ent
	 * @return
	 */
	public int insertarLexemaTS(Entrada ent) {
		// Ya deberia haberse comprobado esto pero bueno
		if (buscarTS(ent.getLexema()) != -1)
			return -1;
		int pos = TS.size();
		TS.add(ent);
		map.put(ent.getLexema(), pos);
		return pos;
	}

	public void insertarTipoTS(int pos, String tipo) {
		TS.get(pos).setTipo(tipo);
	}

	public void insertarDesplazamientoTS(int pos, int despl) {
		TS.get(pos).setDesplazamiento("" + despl);
	}

	public void insertarNParamTS(int pos, int nParam) {
		TS.get(pos).setDesplazamiento("" + nParam);
	}

	public void insertarTipoParamTS(int pos, String tParam) {
		TS.get(pos).addTipoParam(tParam);
	}

	public void insertarTipoDevTS(int pos, String tDev) {
		TS.get(pos).setTipoDev(tDev);
	}

	public void insertarEtiqTS(int pos, String etiq) {
		TS.get(pos).setEtiq(etiq);
	}

	/**
	 * Guardamos el estado actual de la TS en el fichero TS.txt
	 * 
	 * @throws IOException
	 */
	public void toFile() {
		// Sobreescribe cualquier archivo anterior con el mismo nombre
		FileWriter writer;
		try {
			writer = new FileWriter("Resultados\\TS.txt", true);
			if (!TS.isEmpty())
				writer.write("TABLA #" + String.format("%02d", id) + ":\n");
			for (Entrada e : TS)
				writer.write(e.toString());
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Para limpiar los archivos de posibles pasadas ejecuciones
	 * 
	 * @throws IOException
	 */
	public static void clearFile() throws IOException {
		FileWriter myWriter = new FileWriter("Resultados\\TS.txt");
		myWriter.write("");
		myWriter.close();
	}

}
