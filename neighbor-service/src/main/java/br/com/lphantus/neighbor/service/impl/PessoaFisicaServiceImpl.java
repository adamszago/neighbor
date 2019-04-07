package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.repository.IPessoaFisicaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IPessoaFisicaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class PessoaFisicaServiceImpl extends
		GenericService<Long, PessoaFisicaDTO, PessoaFisica> implements
		IPessoaFisicaService {

	@Autowired
	private IPessoaFisicaDAO pessoaFisicaDAO;

	@Override
	public List<MoradorDTO> buscarResponsaveisFinanceiros(
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.pessoaFisicaDAO.buscarResponsaveisFinanceiros(condominio);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar responsaveis financeiros.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<MoradorDTO> buscarResponsaveisFinanceiros(final String casas,
			final String blocos, final CondominioDTO condominio)
			throws ServiceException {
		try {
			return this.pessoaFisicaDAO.buscarResponsaveisFinanceiros(casas, blocos, condominio);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar responsaveis financeiros.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<PessoaFisicaDTO> buscarPessoasLancamentosAtivos()
			throws ServiceException {
		try {
			return this.pessoaFisicaDAO.buscarPessoasLancamentosAtivos();
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar pessoas com lancamentos ativos.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<PessoaFisicaDTO> buscarPessoasFaturaAberto(
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.pessoaFisicaDAO.buscarPessoasFaturaAberto(condominio);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar pessoas com faturas em aberto.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

}
