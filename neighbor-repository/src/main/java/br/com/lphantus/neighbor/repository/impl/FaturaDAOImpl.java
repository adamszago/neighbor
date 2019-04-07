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
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.entity.Fatura;
import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;
import br.com.lphantus.neighbor.repository.IFaturaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class FaturaDAOImpl extends GenericDAOImpl<Long, FaturaDTO, Fatura>
		implements IFaturaDAO {

	@SuppressWarnings("unchecked")
	public List<FaturaDTO> buscarPorParametros(final CondominioDTO condominio,
			final Boolean status, final Long tipoLancamento)
			throws DAOException {

		final Query query = getEntityManager().createNamedQuery(
				"Fatura.buscarPorParametros");

		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}
		query.setParameter("status", status);
		query.setParameter("tipoLancamento", tipoLancamento);

		final List<Fatura> faturas = new ArrayList<Fatura>();
		try {
			faturas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		// -----------------------------------------------------------------------------
		for (final Fatura fat : faturas) {
			final Lancamento lancamento = fat.getLancamentos().get(0);
			final PessoaFisica item = (PessoaFisica) getEntityManager().find(
					PessoaFisica.class, lancamento.getPessoa().getIdPessoa());
			if (item == null) {
				final PessoaJuridica itemJuridico = (PessoaJuridica) getEntityManager()
						.find(PessoaJuridica.class,
								lancamento.getPessoa().getIdPessoa());
				if (null != itemJuridico) {
					lancamento.setPessoa(itemJuridico);
				}
			} else {
				lancamento.setPessoa(item);
			}
		}
		// -----------------------------------------------------------------------------

		final List<FaturaDTO> retorno = new ArrayList<FaturaDTO>();
		retorno.addAll(FaturaDTO.Builder.getInstance().createList(faturas));

		return retorno;
	}

	@Override
	public List<FaturaDTO> buscarPorCondominio(final CondominioDTO condominio,
			final Boolean status) throws DAOException {
		return buscarPorParametros(condominio, status, 1L);
	}

	@Override
	public List<FaturaDTO> buscarPorCondominioPagar(
			final CondominioDTO condominio, final Boolean status)
			throws DAOException {

		return buscarPorParametros(condominio, status, 2L);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FaturaDTO> buscarEmAbertoPorPessoa(final PessoaDTO pessoa)
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Fatura.buscarEmAbertoPorPessoa");

		if (pessoa == null) {
			query.setParameter("idPessoa", null);
		} else {
			query.setParameter("idPessoa", pessoa.getIdPessoa());
		}

		final List<Fatura> faturas = new ArrayList<Fatura>();
		try {
			faturas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<FaturaDTO> retorno = new ArrayList<FaturaDTO>();
		retorno.addAll(FaturaDTO.Builder.getInstance().createList(faturas));

		return retorno;
	}

	@Override
	public void alterarStatus(final Long id, final boolean novoStatus)
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Fatura.alterarStatus");

		query.setParameter("status", novoStatus);
		query.setParameter("idFatura", id);

		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FaturaDTO> buscarFaturasSemDuplicata(
			final CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Fatura.buscarFaturasSemDuplicata");

		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}

		final List<Fatura> faturas = new ArrayList<Fatura>();
		try {
			faturas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<FaturaDTO> retorno = new ArrayList<FaturaDTO>();
		retorno.addAll(FaturaDTO.Builder.getInstance().createList(faturas));

		return retorno;
	}

}