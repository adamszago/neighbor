package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.TipoPrestadorDTO;
import br.com.lphantus.neighbor.entity.TipoPrestador;
import br.com.lphantus.neighbor.repository.ITipoPrestadorDAO;
import br.com.lphantus.neighbor.service.ITipoPrestadorService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class TipoPrestadorServiceImpl extends
		GenericService<Long, TipoPrestadorDTO, TipoPrestador> implements
		ITipoPrestadorService {

	@Autowired
	private ITipoPrestadorDAO tipoPrestadorDAO;

	@Override
	public List<TipoPrestadorDTO> findAllTipoPrestador()
			throws ServiceException {
		try {
			return TipoPrestadorDTO.Builder.getInstance().createList(
					this.tipoPrestadorDAO.findAll());
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
