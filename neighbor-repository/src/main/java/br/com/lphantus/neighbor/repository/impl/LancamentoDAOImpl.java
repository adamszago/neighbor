package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.common.LancamentoDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;
import br.com.lphantus.neighbor.repository.ILancamentoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class LancamentoDAOImpl extends GenericDAOImpl<Long, LancamentoDTO, Lancamento> implements ILancamentoDAO {

	@SuppressWarnings("unchecked")
	private List<LancamentoDTO> buscarLancamentos(final CondominioDTO condominioUsuarioLogado, final Boolean status, final Long idTipoLancamento) throws DAOException {

		final Query query = getEntityManager().createNamedQuery("Lancamento.buscarLancamentos");

		if (null != condominioUsuarioLogado) {
			query.setParameter("idCondominio", condominioUsuarioLogado.getPessoa().getIdPessoa());
		} else {
			query.setParameter("idCondominio", null);
		}
		query.setParameter("status", status);
		query.setParameter("idTipoLancamento", idTipoLancamento);

		final List<Lancamento> listaTemp = new ArrayList<Lancamento>();
		try {
			listaTemp.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		// -----------------------------------------------------------------------------
		for (final Lancamento lance : listaTemp) {
			PessoaFisica item = null;
			try {
				item = (PessoaFisica) getEntityManager().find(PessoaFisica.class, lance.getPessoa().getIdPessoa());
			} catch (EntityNotFoundException nex) {
				// nao fazer nada
			}
			if (null == item) {
				PessoaJuridica itemJuridico = null;
				try {
					itemJuridico = (PessoaJuridica) getEntityManager().find(PessoaJuridica.class, lance.getPessoa().getIdPessoa());
					lance.setPessoa(itemJuridico);
				} catch (EntityNotFoundException nex) {
					// nao fazer nada
				}
			} else {
				lance.setPessoa(item);
			}
		}
		// -----------------------------------------------------------------------------

		final List<LancamentoDTO> lista = new ArrayList<LancamentoDTO>();
		lista.addAll(LancamentoDTO.Builder.getInstance().createList(listaTemp));
		return lista;
	}

	@Override
	public List<LancamentoDTO> buscarPorCondominio(final CondominioDTO condominioUsuarioLogado, final Boolean status) throws DAOException {
		return buscarLancamentos(condominioUsuarioLogado, status, 1L);
	}

	@Override
	public List<LancamentoDTO> buscarPorCondominioPagar(final CondominioDTO condominioUsuarioLogado, final Boolean status) throws DAOException {
		return buscarLancamentos(condominioUsuarioLogado, status, 2L);
	}

	@SuppressWarnings("unchecked")
	private List<LancamentoDTO> buscarLancamentosNaoAssociados(final PessoaFisicaDTO pessoaSelecionada, final CondominioDTO condominio, final Long tipoLancamento) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Lancamento.buscarLancamentosNaoAssociados");

		if (null == pessoaSelecionada) {
			query.setParameter("idPessoa", null);
		} else {
			query.setParameter("idPessoa", pessoaSelecionada.getIdPessoa());
		}
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}
		query.setParameter("tipoLancamento", tipoLancamento);

		final List<Lancamento> listaTemp = new ArrayList<Lancamento>();
		try {
			listaTemp.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		// -----------------------------------------------------------------------------
		for (final Lancamento lance : listaTemp) {
			final PessoaFisica item = (PessoaFisica) getEntityManager().find(PessoaFisica.class, lance.getPessoa().getIdPessoa());
			if (item == null) {
				final PessoaJuridica itemJuridico = (PessoaJuridica) getEntityManager().find(PessoaJuridica.class, lance.getPessoa().getIdPessoa());
				if (null != itemJuridico) {
					lance.setPessoa(itemJuridico);
				}
			} else {
				lance.setPessoa(item);
			}
		}
		// -----------------------------------------------------------------------------

		final List<LancamentoDTO> lista = new ArrayList<LancamentoDTO>();
		lista.addAll(LancamentoDTO.Builder.getInstance().createList(listaTemp));

		return lista;
	}

	@Override
	public List<LancamentoDTO> buscarNaoAssociados(final PessoaFisicaDTO pessoaSelecionada, final CondominioDTO condominio) throws DAOException {
		return buscarLancamentosNaoAssociados(pessoaSelecionada, condominio, 1L);
	}

	@Override
	public List<LancamentoDTO> buscarNaoAssociadosPagar(final PessoaFisicaDTO pessoaSelecionada, final CondominioDTO condominio) throws DAOException {
		return buscarLancamentosNaoAssociados(pessoaSelecionada, condominio, 2L);
	}

	@Override
	public void alterarStatus(final Long id, final boolean novoStatus) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Lancamento.alterarStatus");

		query.setParameter("novoStatus", novoStatus);
		query.setParameter("id", id);

		final int numero = query.executeUpdate();
		getLogger().debug("Numero de linhas alteradas: " + numero);
	}

	@Override
	public void gravaTodosMoradores(final LancamentoDTO lancamento, final List<PessoaDTO> pessoas) {
		for (final PessoaDTO pessoa : pessoas) {
			final LancamentoDTO aGravar = lancamento;
			aGravar.setPessoa(pessoa);
			final Lancamento entidade = LancamentoDTO.Builder.getInstance().createEntity(aGravar);
			getEntityManager().persist(entidade);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LancamentoDTO> buscarPorFatura(final FaturaDTO fatura) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Lancamento.buscarPorFatura");
		query.setParameter("idFatura", fatura.getId());

		final List<Lancamento> listaTemp = new ArrayList<Lancamento>();
		try {
			listaTemp.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<LancamentoDTO> lista = new ArrayList<LancamentoDTO>();
		lista.addAll(LancamentoDTO.Builder.getInstance().createList(listaTemp));
		return lista;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LancamentoDTO> buscarPorDuplicata(final DuplicataDTO duplicata) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Lancamento.buscarPorDuplicata");
		query.setParameter("idDuplicata", duplicata.getId());

		final List<Lancamento> listaTemp = new ArrayList<Lancamento>();
		try {
			listaTemp.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<LancamentoDTO> lista = new ArrayList<LancamentoDTO>();
		lista.addAll(LancamentoDTO.Builder.getInstance().createList(listaTemp));
		return lista;
	}

	private void alteraFaturaLancamento(final LancamentoDTO lancamento, final FaturaDTO fatura) {
		final Query query = getEntityManager().createNamedQuery("Lancamento.alteraFaturaLancamento");
		query.setParameter("idLancamento", lancamento.getId());
		if (null == fatura) {
			query.setParameter("fatura", null);
		} else {
			query.setParameter("fatura", FaturaDTO.Builder.getInstance().createEntity(fatura));
		}
		query.executeUpdate();
	}

	@Override
	public void desassociarFatura(final List<LancamentoDTO> source) throws DAOException {
		for (final LancamentoDTO lancamento : source) {
			alteraFaturaLancamento(lancamento, null);
		}
	}

	@Override
	public void associarFatura(final List<LancamentoDTO> lancamentos, final FaturaDTO fatura) throws DAOException {
		for (final LancamentoDTO lancamento : lancamentos) {
			alteraFaturaLancamento(lancamento, fatura);
		}
	}

}