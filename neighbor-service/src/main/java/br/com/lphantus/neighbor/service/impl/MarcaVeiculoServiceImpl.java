package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MarcaVeiculoDTO;
import br.com.lphantus.neighbor.entity.MarcaVeiculo;
import br.com.lphantus.neighbor.repository.IMarcaVeiculoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IMarcaVeiculoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MarcaVeiculoServiceImpl extends GenericService<Long, MarcaVeiculoDTO, MarcaVeiculo>
		implements IMarcaVeiculoService {

	@Autowired
	private IMarcaVeiculoDAO marcaVeiculoDAO;

	@Override
	public void save(final MarcaVeiculo marca) throws ServiceException {
		try {
			final CondominioDTO condominio = marca.getCondominio().createDto();
			if (this.marcaVeiculoDAO.existeNome(marca.getMarca(), condominio)) {
				throw new ServiceException("Marca ja existe");
			}
			this.marcaVeiculoDAO.save(marca);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<MarcaVeiculoDTO> findAtivos() throws ServiceException {
		try {
			return this.marcaVeiculoDAO.findAtivos();
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<MarcaVeiculoDTO> buscarPorCondominio(
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.marcaVeiculoDAO.buscarPorCondominio(condominio);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
