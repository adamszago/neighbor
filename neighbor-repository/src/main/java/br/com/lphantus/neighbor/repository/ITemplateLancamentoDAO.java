package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.TemplateLancamentoDTO;
import br.com.lphantus.neighbor.entity.TemplateLancamento;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface ITemplateLancamentoDAO extends IGenericDAO<TemplateLancamento> {

	List<TemplateLancamentoDTO> buscarInativosCondominio(
			CondominioDTO condominioUsuarioLogado) throws DAOException;

	List<TemplateLancamentoDTO> buscarAtivosCondominio(CondominioDTO condominio)
			throws DAOException;

	List<TemplateLancamentoDTO> buscarPorCondominio(CondominioDTO condominio)
			throws DAOException;

	void alterarStatus(Long id, boolean novoStatus) throws DAOException;

}