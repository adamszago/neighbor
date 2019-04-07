package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.common.LancamentoDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface ILancamentoDAO extends IGenericDAO<Lancamento> {

	List<LancamentoDTO> buscarPorCondominio(
			CondominioDTO condominioUsuarioLogado, Boolean status)
			throws DAOException;

	List<LancamentoDTO> buscarPorCondominioPagar(
			CondominioDTO condominioUsuarioLogado, Boolean status)
			throws DAOException;

	List<LancamentoDTO> buscarNaoAssociados(PessoaFisicaDTO pessoaSelecionada,
			CondominioDTO condominio) throws DAOException;

	List<LancamentoDTO> buscarNaoAssociadosPagar(
			PessoaFisicaDTO pessoaSelecionada, CondominioDTO condominio)
			throws DAOException;

	void alterarStatus(Long id, boolean novoStatus) throws DAOException;

	void gravaTodosMoradores(LancamentoDTO lancamento, List<PessoaDTO> pessoas);

	List<LancamentoDTO> buscarPorFatura(FaturaDTO fatura) throws DAOException;

	List<LancamentoDTO> buscarPorDuplicata(DuplicataDTO duplicata)
			throws DAOException;

	void desassociarFatura(List<LancamentoDTO> source) throws DAOException;

	void associarFatura(List<LancamentoDTO> lancamentos, FaturaDTO fatura)
			throws DAOException;

}