package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.common.LancamentoDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.entity.Fatura;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IFaturaService extends
		IGenericService<Long, FaturaDTO, Fatura> {

	List<FaturaDTO> buscarPorCondominio(CondominioDTO condominio, Boolean status)
			throws ServiceException;

	List<FaturaDTO> buscarPorCondominioPagar(CondominioDTO condominio,
			Boolean status) throws ServiceException;

	List<FaturaDTO> buscarEmAbertoPorPessoa(PessoaDTO pessoa)
			throws ServiceException;

	void gerarFaturasAberto(CondominioDTO condominio) throws ServiceException;

	void adicionaLancamentoFatura(FaturaDTO entity, LancamentoDTO lancamento);

	void removeLancamentoFatura(FaturaDTO entity, LancamentoDTO lancamento);

	void alterarStatus(Long id, boolean novoStatus) throws ServiceException;

	List<FaturaDTO> buscarFaturasSemDuplicata(CondominioDTO condominio)
			throws ServiceException;

}