package br.com.lphantus.neighbor.util.comparator;

import java.util.Comparator;

import br.com.lphantus.neighbor.common.MarcaVeiculoDTO;

public class ComparaPorString implements Comparator<MarcaVeiculoDTO> {

	@Override
	public int compare(final MarcaVeiculoDTO o1, final MarcaVeiculoDTO o2) {
		return o1.getMarca().compareTo(o2.getMarca());
	}

}
