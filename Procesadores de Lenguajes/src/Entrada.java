import java.util.ArrayList;
import java.util.List;

public class Entrada {

	private String lexema, tipo, desplazamiento, numParam, tipoParam, tipoDev, etiq;
	private List<String> nombres;
	private List<String> atributos;

	public Entrada(String lexema) {
		this.lexema = lexema;
		this.atributos = new ArrayList<>();
		this.atributos.add(tipo);
		this.atributos.add(desplazamiento);
		this.atributos.add(numParam);
		this.atributos.add(tipoParam);
		this.atributos.add(tipoDev);
		this.atributos.add(etiq);

		this.nombres = new ArrayList<>();
		this.nombres.add("tipo");
		this.nombres.add("despl");
		this.nombres.add("numParam");
		this.nombres.add("TipoParamXX");
		this.nombres.add("TipoRetorno");
		this.nombres.add("EtiqFuncion");
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

	public String getTipoParam() {
		return tipoParam;
	}

	public void setTipoParam(String tipoParam) {
		this.tipoParam = tipoParam;
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
