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

import br.com.lphantus.neighbor.common.AgendaSindicoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.entity.AgendaSindico;
import br.com.lphantus.neighbor.repository.IAgendaSindicoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class AgendaSindicoDAOImpl extends
		GenericDAOImpl<Long, AgendaSindicoDTO, AgendaSindico> implements
		IAgendaSindicoDAO {

	@Override
	public void atualizarAgendaSindico(final AgendaSindico evento)
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"AgendaSindico.atualizarAgendaSindico");
		query.setParameter("idAgenda", evento.getId());
		query.setParameter("dataFim", evento.getDataFim());
		query.setParameter("dataInicio", evento.getDataInicio());
		query.setParameter("descricao", evento.getDescricao());
		query.setParameter("titulo", evento.getTitulo());
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AgendaSindico> listarPorCondominio(
			final CondominioDTO condominio) throws DAOException {

		final Query query = getEntityManager().createNamedQuery(
				"AgendaSindico.listarPorCondominio");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}

		final List<AgendaSindico> retorno = new ArrayList<AgendaSindico>();
		try {
			retorno.addAll(query.getResultList());
		} catch (NoResultException e) {
			// nao fazer nada
		}

		return retorno;
	}

}
