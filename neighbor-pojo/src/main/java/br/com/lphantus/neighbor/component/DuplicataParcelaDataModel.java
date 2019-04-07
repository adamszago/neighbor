package br.com.lphantus.neighbor.component;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;

public class DuplicataParcelaDataModel extends
		ListDataModel<DuplicataParcelaDTO> implements
		SelectableDataModel<DuplicataParcelaDTO> {

	private static Long calculaChave(final DuplicataParcelaDTO duplicataParcela) {
		return new Long(duplicataParcela.getIdDuplicata().toString()
				+ duplicataParcela.getNumeroParcela().toString());
	}

	@Override
	public DuplicataParcelaDTO getRowData(final String rowKey) {

		@SuppressWarnings("unchecked")
		final List<DuplicataParcelaDTO> faturas = (List<DuplicataParcelaDTO>) getWrappedData();

		final Long chaveArgumento = Long.valueOf(rowKey);
		for (final DuplicataParcelaDTO fatura : faturas) {
			final Long chaveFatura = calculaChave(fatura);
			if (chaveFatura.equals(chaveArgumento)) {
				return fatura;
			}
		}

		return null;
	}

	@Override
	public Object getRowKey(final DuplicataParcelaDTO fatura) {
		return calculaChave(fatura);
	}

}
