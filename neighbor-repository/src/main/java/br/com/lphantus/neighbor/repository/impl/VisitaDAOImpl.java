package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.VisitaDTO;
import br.com.lphantus.neighbor.entity.Visita;
import br.com.lphantus.neighbor.repository.IVisitaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.utils.DateUtil;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class VisitaDAOImpl extends GenericDAOImpl<Long, VisitaDTO, Visita> implements IVisitaDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<VisitaDTO> buscaVisitasAtivasConfirmadasByMorador(final MoradorDTO morador) throws DAOException {
		final List<VisitaDTO> retorno = new ArrayList<VisitaDTO>();
		final Query query = getEntityManager().createNamedQuery("Visita.buscaVisitasAtivasConfirmadasByMorador");
		query.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		final List<Visita> visitas = new ArrayList<Visita>();
		try {
			visitas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		for (final Visita visita : visitas) {
			final VisitaDTO dto = visita.createDto();
			dto.setVisitante(visita.getVisitante().createDto());
			retorno.add(dto);
		}
		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisitaDTO> buscaVisitasAgendadasByMorador(final MoradorDTO morador) throws DAOException {
		final List<VisitaDTO> retorno = new ArrayList<VisitaDTO>();
		final Query query = getEntityManager().createNamedQuery("Visita.buscaVisitasAgendadasByMorador");
		query.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		final List<Visita> visitas = new ArrayList<Visita>();
		try {
			visitas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		for (final Visita visita : visitas) {
			if (null == visita.getInicioAgendamentoVisita() || !DateUtil.dataEMenorQueHoje(visita.getInicioAgendamentoVisita())) {
				final VisitaDTO dto = visita.createDto();
				dto.setVisitante(visita.getVisitante().createDto());
				retorno.add(dto);
			}
		}
		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisitaDTO> buscaVisitasAgendadas(CondominioDTO condominio) throws DAOException {
		final List<VisitaDTO> retorno = new ArrayList<VisitaDTO>();
		final Query query = getEntityManager().createNamedQuery("Visita.buscaVisitasAgendadas");
		if (null == condominio) {
			query.setParameter("condominio", null);
		} else {
			query.setParameter("condominio", CondominioDTO.Builder.getInstance().createEntity(condominio));
		}

		final List<Visita> visitas = new ArrayList<Visita>();
		try {
			visitas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		if (null != visitas) {
			for (final Visita visita : visitas) {
				if (null == visita.getInicioAgendamentoVisita() || !DateUtil.dataEMenorQueHoje(visita.getInicioAgendamentoVisita())) {
					final VisitaDTO dto = visita.createDto();
					dto.setVisitante(visita.getVisitante().createDto());
					retorno.add(dto);
				}
			}
		}
		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisitaDTO> buscaVisitasByMorador(final MoradorDTO morador) throws DAOException {
		final List<VisitaDTO> retorno = new ArrayList<VisitaDTO>();
		final Query query = getEntityManager().createNamedQuery("Visita.buscaVisitasByMorador");
		query.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		final List<Visita> visitas = new ArrayList<Visita>();
		try {
			visitas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		for (final Visita visita : visitas) {
			final VisitaDTO dto = visita.createDto();
			dto.setVisitante(visita.getVisitante().createDto());
			retorno.add(dto);
		}

		return retorno;
	}

}
