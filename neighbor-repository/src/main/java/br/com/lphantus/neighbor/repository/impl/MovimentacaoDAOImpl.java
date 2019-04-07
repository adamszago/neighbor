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
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.entity.Movimentacao;
import br.com.lphantus.neighbor.repository.IMovimentacaoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MovimentacaoDAOImpl extends
		GenericDAOImpl<Long, MovimentacaoDTO, Movimentacao> implements
		IMovimentacaoDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentacaoDTO> buscarPorCondominio(
			final CondominioDTO condominio) throws DAOException {
		Query query = getEntityManager().createNamedQuery(
				"Movimentacao.buscarPorCondominio");

		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}

		final List<Movimentacao> lista = new ArrayList<Movimentacao>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<MovimentacaoDTO> retorno = new ArrayList<MovimentacaoDTO>();
		retorno.addAll(MovimentacaoDTO.Builder.getInstance().createList(lista));

		return retorno;
	}
}