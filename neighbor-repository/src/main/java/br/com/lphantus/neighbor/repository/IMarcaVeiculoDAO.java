package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MarcaVeiculoDTO;
import br.com.lphantus.neighbor.entity.MarcaVeiculo;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IMarcaVeiculoDAO extends IGenericDAO<MarcaVeiculo> {

	List<MarcaVeiculoDTO> findAtivos() throws DAOException;

	boolean existeNome(String nomeMarca, CondominioDTO condominio)
			throws DAOException;

	List<MarcaVeiculoDTO> buscarPorCondominio(CondominioDTO condominio)
			throws DAOException;
}
