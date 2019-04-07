package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.entity.Movimentacao;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IMovimentacaoDAO extends IGenericDAO<Movimentacao> {

	List<MovimentacaoDTO> buscarPorCondominio(CondominioDTO condominio)
			throws DAOException;

}