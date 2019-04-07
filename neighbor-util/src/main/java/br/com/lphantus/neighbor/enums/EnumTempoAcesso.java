package br.com.lphantus.neighbor.enums;

public enum EnumTempoAcesso {
	
	ENTRADA_LIVRE(0L, "Entrada Livre"),
	ENTRADA_UNICA(1L, "Um dia"),
	PERIODO_DIAS(2L, "Per\u00EDodo de dias");
	
	private Long tipo;
	private String descricao;

	private EnumTempoAcesso(Long tipo, String descricao){
		this.tipo = tipo;
		this.descricao = descricao;
	}

	/**
	 * @return the tipo
	 */
	public Long getTipo() {
		return tipo;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	
}
