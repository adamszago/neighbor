package br.com.lphantus.neighbor.repository.impl;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.entity.UnidadeHabitacional;
import br.com.lphantus.neighbor.repository.IUnidadeHabitacionalDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class UnidadeHabitacionalDAOImpl extends
		GenericDAOImpl<Long, UnidadeHabitacionalDTO, UnidadeHabitacional>
		implements IUnidadeHabitacionalDAO {

	@Override
	public UnidadeHabitacionalDTO buscarUnidadeHabitacionalMorador(
			final MoradorDTO morador) throws DAOException {

		UnidadeHabitacionalDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery(
				"UnidadeHabitacional.buscarUnidadeHabitacionalMorador");
		query.setParameter("idPessoa", morador.getPessoa().getIdPessoa());

		try {
			final UnidadeHabitacional unidade = (UnidadeHabitacional) query
					.getSingleResult();
			retorno = unidade.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException(
					"Foi encontrada mais de uma unidade habitacional para o morador.");
		}

		return retorno;
	}

	@Override
	public Long obterUltimaUnidadeInserida() throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"UnidadeHabitacional.obterUltimaUnidadeInserida");
		try {
			return (Long) query.getSingleResult();
		} catch (PersistenceException e) {
			throw new DAOException(
					"Ocorreu um erro ao buscar ultima unidade inserida.", e);
		}
	}

}
