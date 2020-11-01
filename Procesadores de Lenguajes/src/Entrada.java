public class Entrada {

	private String lexema, tipo, desplazamiento, numParam, tipoParam, tipoDev, etiq;

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
		return "Lexema: " + lexema + ", Tipo: " + tipo + ", Desplazamiento: " + desplazamiento + ", Número Parámetros: "
				+ numParam + ", Tipo Parámetros: " + tipoParam + ", Tipo Devolución: " + tipoDev + ", Etiqueta: " + etiq
				+ ".";
	}
}
