package br.com.lphantus.neighbor.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.VisitaDTO;
import br.com.lphantus.neighbor.common.VisitanteDTO;
import br.com.lphantus.neighbor.entity.Visita;
import br.com.lphantus.neighbor.entity.Visitante;
import br.com.lphantus.neighbor.enums.EnumTempoAcesso;
import br.com.lphantus.neighbor.repository.IVisitaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IConfiguracaoCondominioService;
import br.com.lphantus.neighbor.service.IVisitaService;
import br.com.lphantus.neighbor.service.IVisitanteService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class VisitaServiceImpl extends GenericService<Long, VisitaDTO, Visita> implements IVisitaService {

	private final IVisitaDAO daoVisita;
	private final IVisitanteService visitanteService;
	private final IConfiguracaoCondominioService configuracaoCondominioService;

	@Autowired
	public VisitaServiceImpl(final IVisitaDAO daoVisita, final IVisitanteService visitanteService, final IConfiguracaoCondominioService configuracaoCondominioService) {
		this.daoVisita = daoVisita;
		this.visitanteService = visitanteService;
		this.configuracaoCondominioService = configuracaoCondominioService;
	}

	/**
	 * @author Adams Método para visitas ativas e confirmadas do morador
	 * @since 16/03/2013
	 */
	@Override
	public List<VisitaDTO> buscaVisitasAtivasConfirmadasByMorador(final MoradorDTO morador) throws ServiceException {
		final List<VisitaDTO> visitasDoMorador = new ArrayList<VisitaDTO>();

		try {
			visitasDoMorador.addAll(daoVisita.buscaVisitasAtivasConfirmadasByMorador(morador));
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage(), e);
		}

		return visitasDoMorador;
	}

	/**
	 * @author Adams Método para visitas agendadas do morador
	 * @since 16/03/2013
	 */
	@Override
	public List<VisitaDTO> buscaVisitasAgendadasByMorador(final MoradorDTO morador) throws ServiceException {
		final List<VisitaDTO> visitasDoMorador = new ArrayList<VisitaDTO>();

		try {
			visitasDoMorador.addAll(daoVisita.buscaVisitasAgendadasByMorador(morador));
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage(), e);
		}

		return visitasDoMorador;
	}

	@Override
	public List<VisitaDTO> buscaVisitasAgendadas(CondominioDTO condominio) throws ServiceException {
		final List<VisitaDTO> allVisitas = new ArrayList<VisitaDTO>();
		try {
			allVisitas.addAll(daoVisita.buscaVisitasAgendadas(condominio));
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return allVisitas;
	}

	@Override
	public List<VisitaDTO> buscaVisitasByMorador(final MoradorDTO morador) throws ServiceException {
		final List<VisitaDTO> visitasDoMorador = new ArrayList<VisitaDTO>();

		try {
			visitasDoMorador.addAll(daoVisita.buscaVisitasByMorador(morador));
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage(), e);
		}

		return visitasDoMorador;
	}

	private void verificaEntradaLivreExcedido(MoradorDTO morador) throws ServiceException, DAOException {
		ConfiguracaoCondominioDTO configuracao = configuracaoCondominioService.buscarPorMorador(morador);
		if (null != configuracao && null != configuracao.getQtdeEntradaLivrePrestador()) {
			List<VisitaDTO> entradaLivre = daoVisita.buscaVisitasByMorador(morador);
			Iterator<VisitaDTO> iterator = entradaLivre.iterator();
			while (iterator.hasNext()) {
				VisitaDTO servico = iterator.next();
				if (!servico.isAtiva() || !EnumTempoAcesso.ENTRADA_LIVRE.getTipo().equals(servico.getTipoAcesso())) {
					iterator.remove();
				}
			}
			if (configuracao.getQtdeEntradaLivrePrestador() < entradaLivre.size()) {
				throw new ServiceException("Limite de visitantes livres excedido!");
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void salvarVisitaAgendada(final VisitaDTO visitaAgendada) throws ServiceException {
		try {
			if (visitaAgendada.getTipoAcesso().equals(EnumTempoAcesso.ENTRADA_LIVRE.getTipo())) {
				verificaEntradaLivreExcedido(visitaAgendada.getMorador());
			} else {
				if (visitaAgendada.getInicioAgendamentoVisita() == null) {
					throw new ServiceException("Uma data de agendamento é necessária!");
				}
			}

			if ((visitaAgendada.getVisitante() == null) || "".equals(visitaAgendada.getVisitante().getPessoa().getNome())) {
				throw new ServiceException("Favor informar o visitante agendado!");
			}
			if (visitaAgendada.getVisitante().getPessoa().getIdPessoa() == null) {
				final Visitante visitante = VisitanteDTO.Builder.getInstance().createEntity(visitaAgendada.getVisitante());
				visitanteService.save(visitante);

				final Visita entidade = VisitaDTO.Builder.getInstance().createEntity(visitaAgendada);
				entidade.setVisitante(visitante);
				daoVisita.save(entidade);
				visitaAgendada.setVisitante(visitante.createDto());
			} else {
				final Visitante visitante = VisitanteDTO.Builder.getInstance().createEntity(visitaAgendada.getVisitante());
				visitanteService.update(visitante);

				final Visita entidade = VisitaDTO.Builder.getInstance().createEntity(visitaAgendada);
				entidade.setVisitante(visitante);
				daoVisita.save(entidade);
			}

		} catch (final DAOException dao) {
			throw new ServiceException(dao.getMessage(), dao);
		}

	}

	@Override
	public void confirmarVisitaAgendada(final VisitaDTO visitaAgendada) throws ServiceException {
		try {
			// TODO: rever data agendamento servico (tornou-se data inicio e fim
			// agendamento)
			/*
			 * if (visitaAgendada.getDataAgendamentoVisita() == null) { throw
			 * new ServiceException( "Uma data de agendamento é necessária!"); }
			 */
			if ((visitaAgendada.getVisitante() == null) || "".equals(visitaAgendada.getVisitante().getPessoa().getNome())) {
				throw new ServiceException("Favor informar o visitante agendado!");
			}
			if (visitaAgendada.getVisitante().getPessoa().getIdPessoa() == null) {
				visitanteService.save(VisitanteDTO.Builder.getInstance().createEntity(visitaAgendada.getVisitante()));
			} else {
				visitanteService.update(VisitanteDTO.Builder.getInstance().createEntity(visitaAgendada.getVisitante()));
			}
			daoVisita.update(VisitaDTO.Builder.getInstance().createEntity(visitaAgendada));

		} catch (final DAOException dao) {
			throw new ServiceException(dao.getMessage());
		}

	}

	@Override
	public void removerVisitaAgendada(VisitaDTO visitaAgendada) throws ServiceException {
		Visita visita = findById(visitaAgendada.getId());
		visita.setAtiva(Boolean.FALSE);
		update(visita);
	}

}
