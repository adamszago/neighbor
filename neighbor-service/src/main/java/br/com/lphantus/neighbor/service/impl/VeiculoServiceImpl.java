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
import br.com.lphantus.neighbor.common.VeiculoDTO;
import br.com.lphantus.neighbor.entity.Veiculo;
import br.com.lphantus.neighbor.repository.IVeiculoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.ICondominioService;
import br.com.lphantus.neighbor.service.IVeiculoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class VeiculoServiceImpl extends GenericService<Long, VeiculoDTO, Veiculo> implements
		IVeiculoService {

	@Autowired
	private IVeiculoDAO veiculoDAO;

	@Autowired
	private ICondominioService condominioService;

	@Override
	public List<VeiculoDTO> listarVeiculosMorador(final MoradorDTO morador)
			throws ServiceException {
		try {
			return this.veiculoDAO.listarVeiculosMorador(morador);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void save(final Veiculo veiculo) throws ServiceException {
		if (veiculo.getProprietario() == null) {
			throw new ServiceException(
					"O morador proprietario do veiculo não foi informado.");
		}
		if (veiculo.getMarca() == null) {
			throw new ServiceException("Por favor escolha a marca.");
		}
		try {
			this.veiculoDAO.save(veiculo);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<VeiculoDTO> buscarPorCondominio(final CondominioDTO condominio)
			throws ServiceException {
		try {
			return this.veiculoDAO.buscarPorCondominio(condominio);
		} catch (final DAOException exception) {
			throw new ServiceException(exception);
		}
	}

}
