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

import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.entity.DuplicataParcela;
import br.com.lphantus.neighbor.entity.DuplicataParcelaPK;
import br.com.lphantus.neighbor.repository.IDuplicataParcelaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class DuplicataParcelaDAOImpl extends GenericDAOImpl<DuplicataParcelaPK, DuplicataParcelaDTO, DuplicataParcela> implements IDuplicataParcelaDAO {

	@Override
	public void baixarParcela(final DuplicataParcelaDTO parcelaBaixa) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("DuplicataParcela.baixarParcela");
		query.setParameter("idDuplicata", parcelaBaixa.getIdDuplicata());
		query.setParameter("numeroParcela", parcelaBaixa.getNumeroParcela());
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DuplicataParcelaDTO> buscarPorDuplicata(final DuplicataDTO duplicataOrigem) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("DuplicataParcela.buscarPorDuplicata");
		query.setParameter("idDuplicata", duplicataOrigem.getId());

		final List<DuplicataParcela> lista = new ArrayList<DuplicataParcela>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<DuplicataParcelaDTO> retorno = new ArrayList<DuplicataParcelaDTO>();
		retorno.addAll(DuplicataParcelaDTO.Builder.getInstance().createList(lista));

		return retorno;
	}

}