package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.Agregado;
import br.com.lphantus.neighbor.repository.IAgregadoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IAgregadoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class AgregadoServiceImpl extends
		GenericService<Long, AgregadoDTO, Agregado> implements IAgregadoService {

	@Autowired
	private IAgregadoDAO agregadoDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(Agregado entity) throws ServiceException {
		super.save(entity);
	}

	@Override
	public List<AgregadoDTO> listarAgregadosMorador(final MoradorDTO morador)
			throws ServiceException {
		try {
			return this.agregadoDAO.listarAgregadosMorador(morador);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<AgregadoDTO> listarTodosAgregados(final CondominioDTO condominio)
			throws ServiceException {
		try {
			return this.agregadoDAO.listarTodosAgregados(condominio);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
