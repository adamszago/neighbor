package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IPessoaFisicaDAO extends IGenericDAO<PessoaFisica> {

	List<MoradorDTO> buscarResponsaveisFinanceiros(CondominioDTO condominio)
			throws DAOException;

	List<MoradorDTO> buscarResponsaveisFinanceiros(String casas, String blocos,
			CondominioDTO condominio) throws DAOException;

	List<PessoaFisicaDTO> buscarPessoasLancamentosAtivos() throws DAOException;

	List<PessoaFisicaDTO> buscarPessoasFaturaAberto(CondominioDTO condominio)
			throws DAOException;

}
