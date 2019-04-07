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

import br.com.lphantus.neighbor.common.CentroCustoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.entity.CentroCusto;
import br.com.lphantus.neighbor.repository.ICentroCustoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class CentroCustoDAOImpl extends
		GenericDAOImpl<Long, CentroCustoDTO, CentroCusto> implements
		ICentroCustoDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<CentroCustoDTO> buscarPorCondominio(final Boolean status,
			final CondominioDTO condominio) throws DAOException {

		final Query query = getEntityManager().createNamedQuery(
				"CentroCusto.buscarPorCondominio");

		query.setParameter("ativo", status);
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}

		final List<CentroCusto> lista = new ArrayList<CentroCusto>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<CentroCustoDTO> retorno = new ArrayList<CentroCustoDTO>();
		retorno.addAll(CentroCustoDTO.Builder.getInstance().createList(lista));

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CentroCustoDTO> findCentrosCustoRaizByCondominio(
			final CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"CentroCusto.findCentrosCustoRaizByCondominio");
		query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());

		final List<CentroCusto> lista = new ArrayList<CentroCusto>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<CentroCustoDTO> retorno = new ArrayList<CentroCustoDTO>();
		retorno.addAll(CentroCustoDTO.Builder.getInstance().createList(lista));

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CentroCustoDTO> findCentrosCustoFilhos(
			final CentroCustoDTO centroCustoPai) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"CentroCusto.findCentrosCustoFilhos");
		query.setParameter("idPai", centroCustoPai.getId());

		final List<CentroCusto> lista = new ArrayList<CentroCusto>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<CentroCustoDTO> retorno = new ArrayList<CentroCustoDTO>();
		retorno.addAll(CentroCustoDTO.Builder.getInstance().createList(lista));

		return retorno;
	}

}
