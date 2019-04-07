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
import br.com.lphantus.neighbor.common.MarcaVeiculoDTO;
import br.com.lphantus.neighbor.entity.MarcaVeiculo;
import br.com.lphantus.neighbor.repository.IMarcaVeiculoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MarcaVeiculoDAOImpl extends
		GenericDAOImpl<Long, MarcaVeiculoDTO, MarcaVeiculo> implements
		IMarcaVeiculoDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<MarcaVeiculoDTO> findAtivos() throws DAOException {
		Query query = getEntityManager().createNamedQuery(
				"MarcaVeiculo.findAtivos");
		List<MarcaVeiculo> marcas = new ArrayList<MarcaVeiculo>();
		try {
			marcas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return MarcaVeiculoDTO.Builder.getInstance().createList(marcas);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean existeNome(final String nomeMarca,
			final CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"MarcaVeiculo.existeNome");
		query.setParameter("nome", nomeMarca);
		query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		List<MarcaVeiculo> lista = new ArrayList<MarcaVeiculo>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return lista.size() > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MarcaVeiculoDTO> buscarPorCondominio(
			final CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"MarcaVeiculo.buscarPorCondominio");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}

		final List<MarcaVeiculo> lista = new ArrayList<MarcaVeiculo>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return MarcaVeiculoDTO.Builder.getInstance().createList(lista);
	}

}
