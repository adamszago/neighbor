package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ModuloAcessoDTO;
import br.com.lphantus.neighbor.entity.ModuloAcesso;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IModuloAcessoDAO extends IGenericDAO<ModuloAcesso> {

	ModuloAcessoDTO findModuloByNome(String nome) throws DAOException;

	List<ModuloAcessoDTO> buscarPorCondominio(CondominioDTO condominio,
			boolean buscarRoot) throws DAOException;

}
