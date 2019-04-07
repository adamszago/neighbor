package br.com.lphantus.neighbor.repository.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.TemplateLancamentoDTO;
import br.com.lphantus.neighbor.entity.TemplateLancamento;
import br.com.lphantus.neighbor.repository.ITemplateLancamentoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class TemplateLancamentoDAOImpl extends
		GenericDAOImpl<Long, TemplateLancamentoDTO, TemplateLancamento>
		implements ITemplateLancamentoDAO {

	private List<TemplateLancamentoDTO> buscaGenerico(final Boolean status,
			final CondominioDTO condominio) {
		final StringBuilder sb = new StringBuilder();
		sb.append("SELECT template FROM TemplateLancamento template ");
		sb.append("INNER JOIN template.condominio condominio ");
		sb.append("WHERE 1 = 1 ");
		if (null != status) {
			sb.append("AND template.ativo = :status ");
		}
		if (null != condominio) {
			sb.append("AND condominio.idPessoa = :idCondominio ");
		}

		final String hql = sb.toString();
		Query query;

		query = getEntityManager().createQuery(hql);
		if (null != status) {
			query.setParameter("status", status.booleanValue());
		}
		if (null != condominio) {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}

		@SuppressWarnings("unchecked")
		final List<TemplateLancamento> lista = query.getResultList();

		final List<TemplateLancamentoDTO> retorno = TemplateLancamentoDTO.Builder
				.getInstance().createList(lista);

		return retorno;
	}

	@Override
	public List<TemplateLancamentoDTO> buscarInativosCondominio(
			final CondominioDTO condominio) throws DAOException {
		return buscaGenerico(Boolean.FALSE, condominio);
	}

	@Override
	public List<TemplateLancamentoDTO> buscarAtivosCondominio(
			final CondominioDTO condominio) throws DAOException {
		return buscaGenerico(Boolean.TRUE, condominio);
	}

	@Override
	public List<TemplateLancamentoDTO> buscarPorCondominio(
			final CondominioDTO condominio) throws DAOException {
		return buscaGenerico(null, condominio);
	}

	@Override
	public void alterarStatus(final Long id, final boolean novoStatus)
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"TemplateLancamento.alterarStatus");
		query.executeUpdate();
	}

}