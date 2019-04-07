package br.com.lphantus.neighbor.repository;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ContratoDTO;
import br.com.lphantus.neighbor.entity.Contrato;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IContratoDAO extends IGenericDAO<Contrato> {

	ContratoDTO buscarContratoPorCondominio(CondominioDTO condominio)
			throws DAOException;

}
