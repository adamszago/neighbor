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
import br.com.lphantus.neighbor.common.PlanoDTO;
import br.com.lphantus.neighbor.entity.GrupoPermissao;
import br.com.lphantus.neighbor.entity.Permissao;
import br.com.lphantus.neighbor.repository.IPermissaoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class PermissaoDAOImpl extends GenericDAOImpl<Long, PermissaoDTO, Permissao> implements IPermissaoDAO {

	@Override
	public PermissaoDTO buscaPorLabel(final String label) throws DAOException {
		PermissaoDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Permissao.buscaPorLabel");
		query.setParameter("label", label);

		try {
			final Permissao permissaoBanco = (Permissao) query.getSingleResult();
			retorno = permissaoBanco.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrada mais de uma permissao para o label especificado.");
		}

		return retorno;
	}

	@Override
	public PermissaoDTO buscaPorNome(final String nome) throws DAOException {

		PermissaoDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Permissao.buscaPorNome");
		query.setParameter("nome", nome);

		try {
			final Permissao entidade = (Permissao) query.getSingleResult();
			retorno = entidade.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrada mais de uma permissao para o nome especificado.");
		}

		return retorno;
	}

	@Override
	public void atualizaGrupo(final PermissaoDTO permissaoBanco, final GrupoPermissaoDTO grp) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("GrupoPermissao.buscaPorId");
		query.setParameter("idGrupo", grp.getIdGrupo());

		final GrupoPermissao grupo = (GrupoPermissao) query.getSingleResult();

		final Query queryUpdate = getEntityManager().createNamedQuery("Permissao.atualizaGrupo");
		queryUpdate.setParameter("grupo", grupo);
		queryUpdate.setParameter("idPermissao", permissaoBanco.getIdPermissao());

		queryUpdate.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PermissaoDTO> buscarTodas() throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Permissao.buscarTodas");

		final List<Permissao> lista = new ArrayList<Permissao>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<PermissaoDTO> retorno = new ArrayList<PermissaoDTO>();
		retorno.addAll(converteLista(lista));

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PermissaoDTO> findByPlano(final PlanoDTO plano) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Permissao.findByPlano");
		query.setParameter("idPlano", plano.getIdPlano());

		final List<Permissao> lista = new ArrayList<Permissao>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<PermissaoDTO> retorno = new ArrayList<PermissaoDTO>();
		retorno.addAll(converteLista(lista));

		return retorno;
	}

	private List<PermissaoDTO> converteLista(final List<Permissao> lista) {
		final List<PermissaoDTO> retorno = new ArrayList<PermissaoDTO>();
		for (final Permissao permissao : lista) {
			final PermissaoDTO item = PermissaoDTO.Builder.getInstance().create(permissao);
			final GrupoPermissao grupo = permissao.getGrupo();
			if (null != grupo) {
				item.setGrupo(GrupoPermissaoDTO.Builder.getInstance().create(permissao.getGrupo()));
			}
			retorno.add(item);
		}
		return retorno;
	}

}
