package br.com.lphantus.neighbor.component;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import br.com.lphantus.neighbor.common.MoradorDTO;

public class MoradorDataModel extends ListDataModel<MoradorDTO> implements
		SelectableDataModel<MoradorDTO> {

	@Override
	public MoradorDTO getRowData(final String rowKey) {

		@SuppressWarnings("unchecked")
		final List<MoradorDTO> pessoas = (List<MoradorDTO>) getWrappedData();

		final Long idPessoa = Long.valueOf(rowKey);
		for (final MoradorDTO pessoa : pessoas) {
			if (pessoa.getPessoa().getIdPessoa().equals(idPessoa)) {
				return pessoa;
			}
		}

		return null;
	}

	@Override
	public Object getRowKey(final MoradorDTO pessoa) {
		return pessoa.getPessoa().getIdPessoa();
	}

}
