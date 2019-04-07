package br.com.lphantus.neighbor.enums;

public enum EnumTipoAcesso {
	
	PEDESTRE(0L, "Pedestre"),
	MOTORISTA(1L, "Motorista");
	
	private Long tipo;
	private String descricao;

	private EnumTipoAcesso(Long tipo, String descricao){
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
