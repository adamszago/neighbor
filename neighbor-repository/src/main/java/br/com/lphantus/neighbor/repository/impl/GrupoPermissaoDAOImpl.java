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

import br.com.lphantus.neighbor.common.GrupoPermissaoDTO;
import br.com.lphantus.neighbor.common.PermissaoDTO;
import br.com.lphantus.neighbor.entity.GrupoPermissao;
import br.com.lphantus.neighbor.repository.IGrupoPermissaoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class GrupoPermissaoDAOImpl extends
		GenericDAOImpl<Long, GrupoPermissaoDTO, GrupoPermissao> implements
		IGrupoPermissaoDAO {

	@Override
	public GrupoPermissaoDTO buscaPorNome(final String nome)
			throws DAOException {
		GrupoPermissaoDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery(
				"GrupoPermissao.buscaPorNome");
		query.setParameter("nome", nome);

		try {
			final GrupoPermissao grupo = (GrupoPermissao) query
					.getSingleResult();
			retorno = grupo.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException(
					"Foi encontrado mais de um grupo de permissoes com o mesmo nome.");
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GrupoPermissaoDTO> buscarTodos() throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"GrupoPermissao.buscarTodos");

		final List<GrupoPermissao> lista = new ArrayList<GrupoPermissao>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<GrupoPermissaoDTO> retorno = new ArrayList<GrupoPermissaoDTO>();

		if ((null != lista) && !lista.isEmpty()) {
			for (final GrupoPermissao item : lista) {
				final List<PermissaoDTO> listaPermissoes = PermissaoDTO.Builder
						.getInstance().createList(item.getPermissoes());
				final GrupoPermissaoDTO grupo = item.createDto();
				grupo.setPermissoes(listaPermissoes);
				retorno.add(grupo);
			}
		}
		return retorno;
	}

}
