package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.PrestadorServicoDTO;
import br.com.lphantus.neighbor.entity.PrestadorServico;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IPrestadorServicoDAO extends IGenericDAO<PrestadorServico> {

	PrestadorServicoDTO buscarPorId(PrestadorServicoDTO prestador)
			throws DAOException;

	boolean existeDocumento(PrestadorServicoDTO prestador) throws DAOException;

	List<PrestadorServicoDTO> buscarPorCondominio(CondominioDTO condominio)
			throws DAOException;

	List<PrestadorServicoDTO> buscarConfirmadosPorCondominio(
			CondominioDTO condominio) throws DAOException;

}
