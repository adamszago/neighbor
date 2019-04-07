package br.com.lphantus.neighbor.repository;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.ConfiguracaoCondominio;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IConfiguracaoCondominioDAO extends
		IGenericDAO<ConfiguracaoCondominio> {

	ConfiguracaoCondominioDTO buscarPorCondominio(CondominioDTO condominio)
			throws DAOException;

	void salvarComId(ConfiguracaoCondominio entidade) throws DAOException;
	
	ConfiguracaoCondominioDTO buscarPorMorador(final MoradorDTO morador) throws DAOException;

}