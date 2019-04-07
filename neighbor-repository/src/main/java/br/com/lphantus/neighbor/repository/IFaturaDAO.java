package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.entity.Fatura;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IFaturaDAO extends IGenericDAO<Fatura> {

	List<FaturaDTO> buscarPorCondominio(CondominioDTO condominio, Boolean status)
			throws DAOException;

	List<FaturaDTO> buscarPorCondominioPagar(CondominioDTO condominio,
			Boolean status) throws DAOException;

	List<FaturaDTO> buscarEmAbertoPorPessoa(PessoaDTO pessoa)
			throws DAOException;

	void alterarStatus(Long id, boolean novoStatus) throws DAOException;

	List<FaturaDTO> buscarFaturasSemDuplicata(CondominioDTO condominio)
			throws DAOException;

}