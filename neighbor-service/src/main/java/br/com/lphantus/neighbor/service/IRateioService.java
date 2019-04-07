package br.com.lphantus.neighbor.service;

import java.util.Date;
import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.common.RateioDTO;
import br.com.lphantus.neighbor.entity.Rateio;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IRateioService extends IGenericService<Long, RateioDTO, Rateio> {

	List<RateioDTO> buscarPorMovimentacao(MovimentacaoDTO movimentacao)
			throws ServiceException;

	List<RateioDTO> buscarPorMesAtual(CondominioDTO condominioDTO,
			Date selectedDate) throws ServiceException;

}
