package br.com.lphantus.neighbor.service;

import java.util.List;
import java.util.Map;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.common.LancamentoDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface ILancamentoService extends IGenericService<Long, LancamentoDTO, Lancamento> {

	List<LancamentoDTO> buscarPorCondominio(CondominioDTO condominioUsuarioLogado, Boolean status) throws ServiceException;

	List<LancamentoDTO> buscarPorCondominioPagar(CondominioDTO condominio, Boolean status) throws ServiceException;

	void gravarLancamentoEntrada(LancamentoDTO entidade, List<PessoaDTO> pessoas) throws ServiceException;

	void gravarLancamentoSaida(LancamentoDTO entidade, List<PessoaDTO> pessoas) throws ServiceException;

	List<LancamentoDTO> buscarNaoAssociados(PessoaFisicaDTO pessoaSelecionada, CondominioDTO condominio) throws ServiceException;

	List<LancamentoDTO> buscarNaoAssociadosPagar(PessoaFisicaDTO pessoaSelecionada, CondominioDTO condominio) throws ServiceException;

	void alterarStatus(Long id, boolean novoStatus) throws ServiceException;

	Map<PessoaDTO, List<LancamentoDTO>> buscarMapaEntradaAtivoPorCondominio(CondominioDTO condominio) throws ServiceException;

	List<LancamentoDTO> buscarPorFatura(FaturaDTO entity) throws ServiceException;

	List<LancamentoDTO> buscarPorDuplicata(DuplicataDTO duplicata) throws ServiceException;

	void desassociarFatura(List<LancamentoDTO> source) throws ServiceException;

	void associarFatura(List<LancamentoDTO> lancamentos, FaturaDTO fatura) throws ServiceException;

}