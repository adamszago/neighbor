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
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.entity.Duplicata;
import br.com.lphantus.neighbor.entity.Pessoa;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;
import br.com.lphantus.neighbor.repository.IDuplicataDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class DuplicataDAOImpl extends
		GenericDAOImpl<Long, DuplicataDTO, Duplicata> implements IDuplicataDAO {

	@SuppressWarnings("unchecked")
	private List<DuplicataDTO> buscarPorParametros(
			final CondominioDTO condominio, final FaturaDTO fatura)
			throws DAOException {
		Query query = getEntityManager().createNamedQuery(
				"Duplicata.buscarPorParametros");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}
		if (null == fatura) {
			query.setParameter("idFatura", null);
		} else {
			query.setParameter("idFatura", fatura.getId());
		}

		final List<Duplicata> listaTemp = new ArrayList<Duplicata>();
		try {
			listaTemp.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		// -----------------------------------------------------------------------------
		for (final Duplicata duplicata : listaTemp) {
			final Pessoa pessoa = duplicata.getFatura().getLancamentos().get(0)
					.getPessoa();
			if (null != pessoa) {
				final PessoaFisica item = (PessoaFisica) getEntityManager()
						.find(PessoaFisica.class, pessoa.getIdPessoa());
				if (item == null) {
					final PessoaJuridica itemJuridico = (PessoaJuridica) getEntityManager()
							.find(PessoaJuridica.class, pessoa.getIdPessoa());
					if (null != itemJuridico) {
						duplicata.getFatura().getLancamentos().get(0)
								.setPessoa(itemJuridico);
					}
				} else {
					duplicata.getFatura().getLancamentos().get(0)
							.setPessoa(item);
				}
			}
		}
		// -----------------------------------------------------------------------------

		final List<DuplicataDTO> retorno = new ArrayList<DuplicataDTO>();
		retorno.addAll(DuplicataDTO.Builder.getInstance().createList(listaTemp));
		return retorno;
	}

	@Override
	public List<DuplicataDTO> buscarPorCondominio(final CondominioDTO condominio)
			throws DAOException {
		return buscarPorParametros(condominio, null);
	}

	@Override
	public List<DuplicataDTO> buscarAtivasPorFatura(final FaturaDTO fatura)
			throws DAOException {
		return buscarPorParametros(null, fatura);
	}
}