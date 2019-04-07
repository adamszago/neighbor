package br.com.lphantus.neighbor.component;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import br.com.lphantus.neighbor.common.LancamentoDTO;

public class LancamentoDataModel extends ListDataModel<LancamentoDTO> implements
		SelectableDataModel<LancamentoDTO> {

	@Override
	public LancamentoDTO getRowData(final String rowKey) {

		@SuppressWarnings("unchecked")
		final List<LancamentoDTO> lancamentos = (List<LancamentoDTO>) getWrappedData();

		final Long idLancamento = Long.valueOf(rowKey);
		for (final LancamentoDTO lancamento : lancamentos) {
			if (lancamento.getId().equals(idLancamento)) {
				return lancamento;
			}
		}

		return null;
	}

	@Override
	public Object getRowKey(final LancamentoDTO lancamento) {
		return lancamento.getId();
	}

}
