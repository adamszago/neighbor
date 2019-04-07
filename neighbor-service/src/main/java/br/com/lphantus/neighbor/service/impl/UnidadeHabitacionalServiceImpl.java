package br.com.lphantus.neighbor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.entity.UnidadeHabitacional;
import br.com.lphantus.neighbor.repository.IUnidadeHabitacionalDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class UnidadeHabitacionalServiceImpl extends
		GenericService<Long, UnidadeHabitacionalDTO, UnidadeHabitacional>
		implements IUnidadeHabitacionalService {

	@Autowired
	private IUnidadeHabitacionalDAO unidadeHabitacionalDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(UnidadeHabitacional entity) throws ServiceException {
		super.save(entity);
	}

	@Override
	public UnidadeHabitacionalDTO buscarUnidadeHabitacionalMorador(
			final MoradorDTO morador) throws ServiceException {
		try {
			return this.unidadeHabitacionalDAO
					.buscarUnidadeHabitacionalMorador(morador);
		} catch (DAOException e) {
			getLogger().error("Erro ao buscar unidade habitacional.", e);
			throw new ServiceException(e);
		}
	}

	@Override
	public Long obterUltimaUnidadeInserida() throws ServiceException {
		try {
			return this.unidadeHabitacionalDAO.obterUltimaUnidadeInserida();
		} catch (DAOException e) {
			getLogger().error(
					"Erro ao buscar ultima unidade habitacional inserida.", e);
			throw new ServiceException(e);
		}
	}

}
