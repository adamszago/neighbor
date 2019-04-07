package br.com.lphantus.neighbor.repository;

import java.util.Date;
import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.UtilizacaoDTO;
import br.com.lphantus.neighbor.entity.Historico;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IHistoricoDAO extends IGenericDAO<Historico> {

	List<HistoricoDTO> findAllByIdCondominio(Long idCondominio)
			throws DAOException;

	List<UtilizacaoDTO> consultaUtilizacao(Date dataInicio, Date dataFim, CondominioDTO condominio)
			throws DAOException;

}
