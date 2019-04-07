package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.entity.DuplicataParcela;
import br.com.lphantus.neighbor.entity.DuplicataParcelaPK;
import br.com.lphantus.neighbor.repository.IDuplicataParcelaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IDuplicataParcelaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class DuplicataParcelaServiceImpl extends GenericService<DuplicataParcelaPK, DuplicataParcelaDTO, DuplicataParcela> implements IDuplicataParcelaService {

	@Autowired
	private IDuplicataParcelaDAO duplicataParcelaDAO;

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void baixarParcela(final DuplicataParcelaDTO parcelaBaixa) throws ServiceException {
		try {
			this.duplicataParcelaDAO.baixarParcela(parcelaBaixa);
		} catch (final DAOException e) {
			getLogger().debug("Erro ao baixar parcela. ", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<DuplicataParcelaDTO> buscarPorDuplicata(final DuplicataDTO duplicataOrigem) throws ServiceException {
		try {
			return this.duplicataParcelaDAO.buscarPorDuplicata(duplicataOrigem);
		} catch (final DAOException exception) {
			getLogger().debug("Erro ao buscar por duplicata. ", exception);
			throw new ServiceException(exception.getMessage(), exception);
		}
	}
}
