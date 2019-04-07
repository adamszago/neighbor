package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.VisitanteDTO;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.Visitante;
import br.com.lphantus.neighbor.repository.IVisitaDAO;
import br.com.lphantus.neighbor.repository.IVisitanteDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class VisitanteDAOImpl extends GenericDAOImpl<Long, VisitanteDTO, Visitante> implements IVisitanteDAO {

	@Autowired
	private IVisitaDAO visitanteMoradorDAO;

	@SuppressWarnings("unchecked")
	@Override
	public boolean existeCpf(final VisitanteDTO visitante) throws DAOException {
		Query query = getEntityManager().createNamedQuery("Visitante.existeCpf");
		query.setParameter("cpf", visitante.getPessoa().getCpf());
		if (null == visitante.getCondominio() || null == visitante.getCondominio().getPessoa()) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", visitante.getCondominio().getPessoa().getIdPessoa());
		}

		final List<Visitante> visitantes = new ArrayList<Visitante>();
		try {
			visitantes.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		if (visitantes.size() > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisitanteDTO> findAllAtivosByIdCondominio(final Long id) throws DAOException {
		// TODO: revisar
		Query query = getEntityManager().createNamedQuery("Visitante.findAllAtivosByIdCondominio");
		List<Visitante> lista = new ArrayList<Visitante>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return VisitanteDTO.Builder.getInstance().createList(lista);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisitanteDTO> buscarPorStatus(final CondominioDTO condominio, final Boolean status) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Visitante.buscarPorStatus");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}
		query.setParameter("status", status);

		final List<Visitante> visitantes = new ArrayList<Visitante>();
		try {
			visitantes.addAll(query.getResultList());

			for (Visitante item : visitantes) {

				List<Morador> lista = new ArrayList<Morador>();
				lista.addAll(item.getEntradasLivres());
				item.setEntradasLivresList(lista);
			}
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<VisitanteDTO> retorno = new ArrayList<VisitanteDTO>();
		retorno.addAll(VisitanteDTO.Builder.getInstance().createList(visitantes));
		return retorno;
	}

}
