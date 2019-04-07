package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.MoradorAgregadoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.MoradorAgregado;
import br.com.lphantus.neighbor.entity.MoradorAgregadoPK;
import br.com.lphantus.neighbor.repository.IMoradorAgregadoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IMoradorAgregadoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MoradorAgregadoServiceImpl extends
		GenericService<MoradorAgregadoPK, MoradorAgregadoDTO, MoradorAgregado>
		implements IMoradorAgregadoService {

	@Autowired
	private IMoradorAgregadoDAO moradorAgregadoDAO;

	@Override
	public List<MoradorAgregadoDTO> listarAgregadosAtivosMorador(
			final MoradorDTO morador) throws ServiceException {
		try {
			return this.moradorAgregadoDAO
					.listarAgregadosAtivosMorador(morador);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

}