package br.com.lphantus.neighbor.enums;

import java.io.Serializable;

public enum StatusBoleta implements Serializable {

	ABERTO("Aberto"), VENCIDO("Vencido"), BAIXADO("Pago"), CANCELADO("Cancelado");

	private String status;

	private StatusBoleta(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
