package br.com.lphantus.neighbor.enums;

public enum TipoParcelamento {

	A_VISTA("AVISTA"), PARCELADO("PARCELADO");

	private String tipo;

	private TipoParcelamento(final String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return this.tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(final String tipo) {
		this.tipo = tipo;
	}

}
