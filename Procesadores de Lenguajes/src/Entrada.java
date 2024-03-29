import java.util.ArrayList;
import java.util.List;

public class Entrada {

	private String lexema, tipo, desplazamiento, tipoDev, etiq;
	private int numParam;
	private List<Pair<String, String>> toPrint = new ArrayList<>();
	private ArrayList<String> tipoParam = new ArrayList<>();

	public Entrada(String lexema) {
		this.lexema = lexema;
		numParam = 0;
	}

	public String getLexema() {
		return lexema;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
		toPrint.add(new Pair<String, String>("Tipo", tipo));
	}

	public String getDesplazamiento() {
		return desplazamiento;
	}

	public void setDesplazamiento(String desplazamiento) {
		this.desplazamiento = desplazamiento;
		toPrint.add(new Pair<String, String>("Despl", desplazamiento));
	}

	public int getNumParam() {
		return numParam;
	}

	public ArrayList<String> getTipoParam() {
		return tipoParam;
	}

	public void addTipoParam(String tipoParam) {
		this.tipoParam.add(tipoParam);
		toPrint.add(new Pair<String, String>("TipoParam" + ++numParam, tipoParam));
	}

	public String getTipoDev() {
		return tipoDev;
	}

	public void setTipoDev(String tipoDev) {
		this.tipoDev = tipoDev;
		toPrint.add(new Pair<String, String>("TipoRetorno", tipoDev));
	}

	public String getEtiq() {
		return etiq;
	}

	public void setEtiq(String etiq) {
		this.etiq = etiq;
		toPrint.add(new Pair<String, String>("EtiqFuncion", etiq));
	}

	@Override
	public String toString() {
		String res = "*'" + lexema + "'\n";

		if (tipo != null && tipo.equals("function"))
			toPrint.add(new Pair<String, String>("NumParam", "" + numParam));

		for (Pair<String, String> par : toPrint)
			res += "+" + par.getLeft() + ":'" + par.getRight() + "'\n";
		return res;
	}
}
