package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IPessoaFisicaService extends
		IGenericService<Long, PessoaFisicaDTO, PessoaFisica> {

	List<MoradorDTO> buscarResponsaveisFinanceiros(String casas, String blocos,
			CondominioDTO condominioDTO) throws ServiceException;

	List<MoradorDTO> buscarResponsaveisFinanceiros(CondominioDTO condominioDTO)
			throws ServiceException;

	List<PessoaFisicaDTO> buscarPessoasLancamentosAtivos()
			throws ServiceException;

	List<PessoaFisicaDTO> buscarPessoasFaturaAberto(CondominioDTO condominio)
			throws ServiceException;

}
