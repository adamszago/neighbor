package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.common.LancamentoDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.entity.Movimentacao;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IMovimentacaoService extends IGenericService<Long, MovimentacaoDTO, Movimentacao> {

	void finalizarBaixa(MovimentacaoDTO entidade, DuplicataParcelaDTO parcelaBaixa) throws ServiceException;
	
	void finalizarBaixaLancamentoPagar(LancamentoDTO lancamentoPagar) throws ServiceException;

	List<MovimentacaoDTO> buscarPorCondominio(CondominioDTO condominio) throws ServiceException;

}