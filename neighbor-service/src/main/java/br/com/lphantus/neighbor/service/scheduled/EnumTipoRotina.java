package br.com.lphantus.neighbor.service.scheduled;

public enum EnumTipoRotina {

	ANUAL("Anual"), DIARIA("Diaria"), MENSAL("Mensal"), SEMANAL("Semanal"), SEMESTRAL(
			"Semestral"), ABSTRATA("Abstrata");

	private final String nome;

	private EnumTipoRotina(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

}
