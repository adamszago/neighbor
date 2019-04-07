package br.com.lphantus.neighbor.repository.impl;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.BancoDTO;
import br.com.lphantus.neighbor.entity.Banco;
import br.com.lphantus.neighbor.repository.IBancoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class BancoDAOImpl extends GenericDAOImpl<Long, BancoDTO, Banco>
		implements IBancoDAO {

	@Override
	public boolean existeBanco(final Long id) throws DAOException {
		boolean result = false;
		final Query query = getEntityManager().createNamedQuery(
				"Banco.existeBanco");
		query.setParameter("id", id);

		try {
			query.getSingleResult();
			result = true;
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException(
					"Erro ao pesquisar banco: mais de um banco com o mesmo id");
		}

		return result;
	}

}
