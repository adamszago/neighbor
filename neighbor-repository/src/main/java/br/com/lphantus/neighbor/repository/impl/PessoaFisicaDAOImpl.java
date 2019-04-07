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
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.repository.IPessoaFisicaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.utils.Utilitarios;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class PessoaFisicaDAOImpl extends
		GenericDAOImpl<Long, PessoaFisicaDTO, PessoaFisica> implements
		IPessoaFisicaDAO {

	@Override
	public List<MoradorDTO> buscarResponsaveisFinanceiros(final String casas,
			final String blocos, final CondominioDTO condominio)
			throws DAOException {
		final List<String> numeroCasas = Utilitarios.geraSequenciaNumerica(casas);
		final List<String> numeroBlocos = Utilitarios.geraSequenciaNumerica(blocos);

		String hql = "SELECT DISTINCT morador FROM Morador morador "
				+ "INNER JOIN morador.unidadeHabitacional relacionamento "
				+ "INNER JOIN relacionamento.unidadeHabitacional unidade "
				+ "INNER JOIN unidade.condominio condominio "
				+ "WHERE relacionamento.responsavelFinanceiro = true "
				+ "AND morador.ativo = true "
				+ "AND (:idCondominio IS NULL OR condominio.idPessoa = :idCondominio) "
				+ "AND unidade.ativo = true ";

		if (!numeroCasas.isEmpty()) {
			hql = hql + "AND unidade.identificacao IN (:numeroCasas) ";
		}
		if (!numeroBlocos.isEmpty()) {
			hql = hql + "AND unidade.bloco IN (:numeroBlocos) ";
		}

		final Query query = getEntityManager().createQuery(hql);

		if (!numeroCasas.isEmpty()) {
			query.setParameter("numeroCasas", numeroCasas);
		}
		if (!numeroBlocos.isEmpty()) {
			query.setParameter("numeroBlocos", numeroBlocos);
		}

		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		@SuppressWarnings("unchecked")
		final List<Morador> lista = query.getResultList();

		final List<MoradorDTO> listaRetorno = new ArrayList<MoradorDTO>();
		for (final Morador pessoa : lista) {
			listaRetorno.add(pessoa.createDto());
		}

		return listaRetorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MoradorDTO> buscarResponsaveisFinanceiros(
			final CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("PessoaFisica.buscarResponsaveisFinanceiros");

		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		final List<Morador> lista = new ArrayList<Morador>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<MoradorDTO> listaRetorno = new ArrayList<MoradorDTO>();
		for (final Morador pessoa : lista) {
			final MoradorDTO pessoaAdicionar = MoradorDTO.Builder.getInstance().create(pessoa);
			listaRetorno.add(pessoaAdicionar);
		}

		return listaRetorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PessoaFisicaDTO> buscarPessoasLancamentosAtivos()
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery("PessoaFisica.buscarPessoasLancamentosAtivos");

		final List<PessoaFisica> lista = new ArrayList<PessoaFisica>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<PessoaFisicaDTO> listaRetorno = new ArrayList<PessoaFisicaDTO>();
		for (final PessoaFisica pessoa : lista) {
			listaRetorno.add(PessoaFisicaDTO.Builder.getInstance().create(pessoa));
		}

		return listaRetorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PessoaFisicaDTO> buscarPessoasFaturaAberto(
			final CondominioDTO condominio) throws DAOException {

		final Query query = getEntityManager().createNamedQuery("PessoaFisica.buscarPessoasFaturaAberto");

		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		final List<PessoaFisica> lista = new ArrayList<PessoaFisica>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<PessoaFisicaDTO> listaRetorno = new ArrayList<PessoaFisicaDTO>();
		for (final PessoaFisica pessoa : lista) {
			listaRetorno.add(PessoaFisicaDTO.Builder.getInstance().create(pessoa));
		}

		return listaRetorno;
	}

}
