package br.com.lphantus.neighbor.comparator;

import java.util.Comparator;

import br.com.lphantus.neighbor.common.ServicoPrestadoDTO;

public class OrdenaServicosPorDataDecrescente implements Comparator<ServicoPrestadoDTO> {

	@Override
	public int compare(ServicoPrestadoDTO itemEsquerda, ServicoPrestadoDTO itemDireita) {
		if (null == itemDireita.getDataServico()) {
			return 1;
		} else {
			return itemDireita.getDataServico().compareTo(itemEsquerda.getDataServico());
		}
	}

}
