package br.com.lphantus.neighbor.repository;

import java.util.Date;
import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.common.RateioDTO;
import br.com.lphantus.neighbor.entity.Rateio;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IRateioDAO extends IGenericDAO<Rateio> {

	List<RateioDTO> buscarPorMovimentacao(MovimentacaoDTO movimentacao)
			throws DAOException;

	List<RateioDTO> buscarPorMesAtual(CondominioDTO condominio,
			ConfiguracaoCondominioDTO configuracao, Date selectedDate)
			throws DAOException;

}
