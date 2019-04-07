package br.com.lphantus.neighbor.comparator;

import java.util.Comparator;

import br.com.lphantus.neighbor.common.VisitaDTO;

public class OrdenaVisitasPorDataDecrescente implements Comparator<VisitaDTO> {

	@Override
	public int compare(VisitaDTO itemEsquerda, VisitaDTO itemDireita) {
		if (null == itemDireita.getInicioVisita()) {
			return 1;
		} else {
			return itemDireita.getInicioVisita().compareTo(itemEsquerda.getInicioVisita());
		}
	}

}
