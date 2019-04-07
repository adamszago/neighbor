package br.com.lphantus.neighbor.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ItemReservaDTO;
import br.com.lphantus.neighbor.entity.ItemReserva;
import br.com.lphantus.neighbor.repository.ItemReservaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.ItemReservaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.utils.Constantes;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ItemReservaServiceImpl extends GenericService<Long, ItemReservaDTO, ItemReserva> implements ItemReservaService {

	@Autowired
	private ItemReservaDAO itemReservaDAO;

	@Override
	public List<ItemReservaDTO> findAtivos() throws ServiceException {
		try {
			return this.itemReservaDAO.findAtivos();
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	protected void saveValidate(ItemReserva entity) throws ServiceException {
		super.saveValidate(entity);
		if (entity.isNecessitaPagamento() && (entity.getPreco().equals(BigDecimal.ZERO) || Constantes.VAZIO.equals(entity.getPreco()))) {
			throw new ServiceException("Um item pago não pode ter valor R$0,00!");
		}
	}

	@Override
	public List<ItemReservaDTO> listarPorCondominio(final CondominioDTO condominio, final Boolean status) throws ServiceException {
		try {
			return this.itemReservaDAO.listarPorCondominio(condominio, status);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}
	
}
