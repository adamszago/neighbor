package br.com.lphantus.neighbor.component;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import br.com.lphantus.neighbor.common.PrestadorServicoDTO;

public class PrestadorServicoDataModel extends
		ListDataModel<PrestadorServicoDTO> implements
		SelectableDataModel<PrestadorServicoDTO> {

	@Override
	public PrestadorServicoDTO getRowData(final String rowKey) {

		@SuppressWarnings("unchecked")
		final List<PrestadorServicoDTO> pessoas = (List<PrestadorServicoDTO>) getWrappedData();

		final Long idPessoa = Long.valueOf(rowKey);
		for (final PrestadorServicoDTO pessoa : pessoas) {
			if (pessoa.getPessoa().getIdPessoa().equals(idPessoa)) {
				return pessoa;
			}
		}

		return null;
	}

	@Override
	public Object getRowKey(final PrestadorServicoDTO pessoa) {
		return pessoa.getPessoa().getIdPessoa();
	}

}
