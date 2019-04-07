package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.entity.Condominio;
import br.com.lphantus.neighbor.repository.ICondominioDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class CondominioDAOImpl extends
		GenericDAOImpl<Long, CondominioDTO, Condominio> implements
		ICondominioDAO {

	@Override
	public CondominioDTO buscarPorNome(final String nome) throws DAOException {

		CondominioDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery(
				"Condominio.buscarPorNome");
		query.setParameter("nome", nome);

		try {
			final Condominio objeto = (Condominio) query.getSingleResult();
			retorno = objeto.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException(
					"Foi encontrado mais de um condominio com o mesmo nome.");
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CondominioDTO> buscarTodos() throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Condominio.buscarTodos");

		final List<Condominio> lista = new ArrayList<Condominio>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<CondominioDTO> retorno = new ArrayList<CondominioDTO>();
		retorno.addAll(CondominioDTO.Builder.getInstance().createList(lista));

		return retorno;
	}

}
