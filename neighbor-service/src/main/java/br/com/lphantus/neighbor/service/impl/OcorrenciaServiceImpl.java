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
import br.com.lphantus.neighbor.common.OcorrenciaDTO;
import br.com.lphantus.neighbor.entity.Ocorrencia;
import br.com.lphantus.neighbor.repository.IOcorrenciaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IOcorrenciaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class OcorrenciaServiceImpl extends GenericService<Long, OcorrenciaDTO, Ocorrencia>
		implements IOcorrenciaService {

	@Autowired
	private IOcorrenciaDAO ocorrenciaDAO;

	@Override
	public List<OcorrenciaDTO> listarOcorrenciasPorMorador(
			final MoradorDTO morador) throws ServiceException {
		try {
			return this.ocorrenciaDAO.listarOcorrenciaMorador(morador);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<OcorrenciaDTO> listarOcorrenciasPorCondominio(
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.ocorrenciaDAO.listarOcorrenciaCondominio(condominio);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}
}
