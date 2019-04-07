package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.TemplateLancamentoDTO;
import br.com.lphantus.neighbor.entity.TemplateLancamento;
import br.com.lphantus.neighbor.repository.ITemplateLancamentoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.ITemplateLancamentoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class TemplateLancamentoServiceImpl extends
		GenericService<Long, TemplateLancamentoDTO, TemplateLancamento>
		implements ITemplateLancamentoService {

	@Autowired
	private ITemplateLancamentoDAO templateLancamentoDAO;

	@Override
	public List<TemplateLancamentoDTO> buscarInativosCondominio(
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.templateLancamentoDAO
					.buscarInativosCondominio(condominio);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar inativos.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<TemplateLancamentoDTO> buscarAtivosCondominio(
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.templateLancamentoDAO
					.buscarAtivosCondominio(condominio);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar ativos.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<TemplateLancamentoDTO> buscarPorCondominio(
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.templateLancamentoDAO.buscarPorCondominio(condominio);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar por condominio.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void alterarStatus(final Long id, final boolean novoStatus)
			throws ServiceException {
		try {
			this.templateLancamentoDAO.alterarStatus(id, novoStatus);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar por condominio.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

}
