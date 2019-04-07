package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.TipoAnimalDTO;
import br.com.lphantus.neighbor.entity.TipoAnimal;
import br.com.lphantus.neighbor.repository.ITipoAnimalDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.ITipoAnimalService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class TipoAnimalServiceImpl extends
		GenericService<Long, TipoAnimalDTO, TipoAnimal> implements
		ITipoAnimalService {

	@Autowired
	private ITipoAnimalDAO tipoAnimalDAO;

	@Override
	public void save(final TipoAnimal tipoAnimal) throws ServiceException {
		try {

			final String query = String
					.format("FROM TipoAnimal as ta WHERE ta.tipoAnimal = '%s' AND ta.condominio.idPessoa = '%d'",
							tipoAnimal.getTipoAnimal(), tipoAnimal
									.getCondominio().getIdPessoa());
			if (!this.tipoAnimalDAO.findByQuery(query).isEmpty()) {
				throw new ServiceException("Tipo Animal ja existe");
			}
			this.tipoAnimalDAO.save(tipoAnimal);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<TipoAnimalDTO> findAtivos() throws ServiceException {
		try {
			return this.tipoAnimalDAO.findAtivos();
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<TipoAnimalDTO> buscarPorCondominio(
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.tipoAnimalDAO.buscarPorCondominio(condominio);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
