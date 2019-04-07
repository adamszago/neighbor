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
import br.com.lphantus.neighbor.common.ItemReservaDTO;
import br.com.lphantus.neighbor.entity.ItemReserva;
import br.com.lphantus.neighbor.repository.ItemReservaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ItemReservaDAOImpl extends GenericDAOImpl<Long, ItemReservaDTO, ItemReserva> implements ItemReservaDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemReservaDTO> findAtivos() throws DAOException {
		final Query query = getEntityManager().createNamedQuery("ItemReserva.findAtivos");

		final List<ItemReserva> itens = new ArrayList<ItemReserva>();
		try {
			itens.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<ItemReservaDTO> retorno = ItemReservaDTO.Builder.getInstance().createList(itens);
		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemReservaDTO> listarPorCondominio(final CondominioDTO condominio, final Boolean status) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("ItemReserva.listarPorCondominio");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}
		if (null == status) {
			query.setParameter("status", null);
		} else {
			query.setParameter("status", status.booleanValue());
		}

		final List<ItemReserva> itens = new ArrayList<ItemReserva>();
		try {
			itens.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<ItemReservaDTO> retorno = ItemReservaDTO.Builder.getInstance().createList(itens);
		return retorno;
	}

}
