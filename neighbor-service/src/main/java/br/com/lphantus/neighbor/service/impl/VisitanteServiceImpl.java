package br.com.lphantus.neighbor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.VisitanteDTO;
import br.com.lphantus.neighbor.entity.Visitante;
import br.com.lphantus.neighbor.repository.IVisitanteDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IVisitanteService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class VisitanteServiceImpl extends GenericService<Long, VisitanteDTO, Visitante> implements IVisitanteService {

	@Autowired
	private IVisitanteDAO daoVisitante;

	@Override
	public boolean existeCpf(final VisitanteDTO visitante) throws ServiceException {
		try {
			return this.daoVisitante.existeCpf(visitante);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void save(final Visitante visitante) throws ServiceException {
		if (existeCpf(visitante.createDto())) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("DOCTO_CADASTRADO"));
		} else {
			try {
				this.daoVisitante.save(visitante);
			} catch (final DAOException e) {
				throw new ServiceException(e.getMessage());
			}
		}
	}

	@Override
	public List<VisitanteDTO> getAllVisitantesConfirmados() throws ServiceException {
		final List<VisitanteDTO> visitasConfirmadas = new ArrayList<VisitanteDTO>();
		List<VisitanteDTO> allVisitantes = new ArrayList<VisitanteDTO>();
		try {
			allVisitantes = VisitanteDTO.Builder.getInstance().createList(this.daoVisitante.findAll());
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}

		for (final VisitanteDTO visitante : allVisitantes) {
			if (visitante.getPessoa().getCpf() != null) {
				visitasConfirmadas.add(visitante);
			}
		}
		return visitasConfirmadas;
	}

	@Override
	public List<VisitanteDTO> getAllVisitantesConfirmadosByCondominio(final CondominioDTO condominio) throws ServiceException {
		final List<VisitanteDTO> visitasConfirmadas = new ArrayList<VisitanteDTO>();
		List<VisitanteDTO> allVisitantes = new ArrayList<VisitanteDTO>();
		try {
			allVisitantes = this.daoVisitante.findAllAtivosByIdCondominio(condominio.getPessoa().getIdPessoa());
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}

		for (final VisitanteDTO visitante : allVisitantes) {
			if (visitante.getPessoa().getCpf() != null) {
				visitasConfirmadas.add(visitante);
			}
		}
		return visitasConfirmadas;
	}

	@Override
	public List<VisitanteDTO> buscarPorStatus(final CondominioDTO condominio, final Boolean status) throws ServiceException {
		try {
			return this.daoVisitante.buscarPorStatus(condominio, status);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar visitantes por status.", exception);
			throw new ServiceException("Erro ao buscar visitantes por status.", exception);
		}
	}
}
