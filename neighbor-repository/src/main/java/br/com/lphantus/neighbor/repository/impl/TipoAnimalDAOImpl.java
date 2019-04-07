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
import br.com.lphantus.neighbor.common.TipoAnimalDTO;
import br.com.lphantus.neighbor.entity.TipoAnimal;
import br.com.lphantus.neighbor.repository.ITipoAnimalDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class TipoAnimalDAOImpl extends
		GenericDAOImpl<Long, TipoAnimalDTO, TipoAnimal> implements
		ITipoAnimalDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoAnimalDTO> findAtivos() throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"TipoAnimal.findAtivos");
		List<TipoAnimal> lista = new ArrayList<TipoAnimal>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return TipoAnimalDTO.Builder.getInstance().createList(lista);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoAnimalDTO> buscarPorCondominio(
			final CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"TipoAnimal.buscarPorCondominio");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}
		List<TipoAnimal> lista = new ArrayList<TipoAnimal>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return TipoAnimalDTO.Builder.getInstance().createList(lista);
	}

}
