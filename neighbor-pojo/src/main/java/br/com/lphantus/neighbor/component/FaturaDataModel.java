package br.com.lphantus.neighbor.component;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import br.com.lphantus.neighbor.common.FaturaDTO;

public class FaturaDataModel extends ListDataModel<FaturaDTO> implements
		SelectableDataModel<FaturaDTO> {

	@Override
	public FaturaDTO getRowData(final String rowKey) {

		@SuppressWarnings("unchecked")
		final List<FaturaDTO> faturas = (List<FaturaDTO>) getWrappedData();

		final Long idFatura = Long.valueOf(rowKey);
		for (final FaturaDTO fatura : faturas) {
			if (fatura.getId().equals(idFatura)) {
				return fatura;
			}
		}

		return null;
	}

	@Override
	public Object getRowKey(final FaturaDTO fatura) {
		return fatura.getId();
	}

}
