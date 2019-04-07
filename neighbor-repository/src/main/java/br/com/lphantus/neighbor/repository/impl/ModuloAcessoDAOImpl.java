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
import br.com.lphantus.neighbor.common.ModuloAcessoDTO;
import br.com.lphantus.neighbor.entity.ModuloAcesso;
import br.com.lphantus.neighbor.repository.IModuloAcessoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ModuloAcessoDAOImpl extends GenericDAOImpl<Long, ModuloAcessoDTO, ModuloAcesso> implements IModuloAcessoDAO {

	@Override
	public ModuloAcessoDTO findModuloByNome(final String nome) throws DAOException {
		ModuloAcessoDTO retorno = null;
		Query query = getEntityManager().createNamedQuery("ModuloAcesso.findModuloByNome");
		query.setParameter("nome", nome);

		try {
			final ModuloAcesso objeto = (ModuloAcesso) query.getSingleResult();
			retorno = objeto.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um modulo de acesso com o mesmo nome.");
		}

		return retorno;
	}

	@Override
	public List<ModuloAcessoDTO> buscarPorCondominio(final CondominioDTO condominio, final boolean buscarRoot) throws DAOException {

		final StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("SELECT DISTINCT modulo FROM ModuloAcesso modulo ");
		sBuilder.append("INNER JOIN modulo.permissoes permissoes ");
		sBuilder.append("LEFT JOIN modulo.condominio condominio ");
		sBuilder.append("WHERE 1 = 1 ");
		if (null != condominio) {
			sBuilder.append("AND condominio.idPessoa = :idCondominio ");
		}
		if (!buscarRoot) {
			sBuilder.append("AND permissoes.nome <> 'Root' ");
		}

		final Query query = getEntityManager().createQuery(sBuilder.toString());
		if (null != condominio) {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		@SuppressWarnings("unchecked")
		final List<ModuloAcesso> lista = query.getResultList();

		final List<ModuloAcessoDTO> retorno = new ArrayList<ModuloAcessoDTO>();
		retorno.addAll(ModuloAcessoDTO.Builder.getInstance().createList(lista));

		return retorno;
	}
}
