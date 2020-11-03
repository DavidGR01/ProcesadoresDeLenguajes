import java.util.ArrayList;
import java.util.List;

public class Entrada {

	private String lexema, tipo, desplazamiento, numParam, tipoDev, etiq;
	private List<String> nombres;
	private List<String> atributos;
	private List<String> tipoParam = new ArrayList<>();
	private List<String> modoPaso = new ArrayList<>();

	public Entrada(String lexema) {
		this.lexema = lexema;
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
	}

	public String getDesplazamiento() {
		return desplazamiento;
	}

	public void setDesplazamiento(String desplazamiento) {
		this.desplazamiento = desplazamiento;
	}

	public String getNumParam() {
		return numParam;
	}

	public void setNumParam(String numParam) {
		this.numParam = numParam;
	}

	public List<String> getTipoParam() {
		return tipoParam;
	}

	public void addTipoParam(String tipoParam) {
		this.tipoParam.add(tipoParam);
	}

	public String getTipoDev() {
		return tipoDev;
	}

	public void setTipoDev(String tipoDev) {
		this.tipoDev = tipoDev;
	}

	public String getEtiq() {
		return etiq;
	}

	public void setEtiq(String etiq) {
		this.etiq = etiq;
	}

	@Override
	public String toString() {
		String res = "*'" + lexema + "'\n";
		/*
		 * for(String s:atributos) { res += }
		 */
		return res;
	}
}
