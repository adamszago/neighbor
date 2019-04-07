package br.com.lphantus.neighbor.util.comparator;

import java.util.Comparator;

import br.com.lphantus.neighbor.service.integracao.bopepo.BancoSuportadoNeighbor;

public class BancoSuportadoComparator implements
		Comparator<BancoSuportadoNeighbor> {

	@Override
	public int compare(final BancoSuportadoNeighbor esquerda,
			final BancoSuportadoNeighbor direita) {
		return esquerda.getCodigoDeCompensacao().compareTo(
				direita.getCodigoDeCompensacao());
	}

}
