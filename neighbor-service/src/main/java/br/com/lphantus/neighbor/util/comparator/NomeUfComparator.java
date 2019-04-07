package br.com.lphantus.neighbor.util.comparator;

import java.util.Comparator;

import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;

/**
 * Compara uf por nome (crescente)
 * 
 * @author elias
 * 
 */
public class NomeUfComparator implements Comparator<UnidadeFederativa> {

	@Override
	public int compare(final UnidadeFederativa o1, final UnidadeFederativa o2) {
		return o1.getSigla().compareTo(o2.getSigla());
	}

}
