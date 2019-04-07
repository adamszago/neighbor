package br.com.lphantus.neighbor.component;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import br.com.lphantus.neighbor.common.PessoaFisicaDTO;

public class PessoaFisicaDataModel extends ListDataModel<PessoaFisicaDTO>
		implements SelectableDataModel<PessoaFisicaDTO> {

	@Override
	public PessoaFisicaDTO getRowData(final String rowKey) {

		@SuppressWarnings("unchecked")
		final List<PessoaFisicaDTO> pessoas = (List<PessoaFisicaDTO>) getWrappedData();

		final Long idPessoa = Long.valueOf(rowKey);
		for (final PessoaFisicaDTO pessoa : pessoas) {
			if (pessoa.getIdPessoa().equals(idPessoa)) {
				return pessoa;
			}
		}

		return null;
	}

	@Override
	public Object getRowKey(final PessoaFisicaDTO pessoa) {
		return pessoa.getIdPessoa();
	}

}
