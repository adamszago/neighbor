package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
import java.util.Iterator;
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
import br.com.lphantus.neighbor.common.OcorrenciaDTO;
import br.com.lphantus.neighbor.entity.Agregado;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.Ocorrencia;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;
import br.com.lphantus.neighbor.entity.Visitante;
import br.com.lphantus.neighbor.repository.IOcorrenciaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class OcorrenciaDAOImpl extends
		GenericDAOImpl<Long, OcorrenciaDTO, Ocorrencia> implements
		IOcorrenciaDAO {

	private List<Ocorrencia> recuperaPessoas(final List<Ocorrencia> ocorrencias) {
		final List<Ocorrencia> todasOcorrencias = new ArrayList<Ocorrencia>();
		todasOcorrencias.addAll(ocorrencias);

		// busca pessoa, nao pessoa fisica ou juridica, precisa realizar nova
		// busca
		final Iterator<Ocorrencia> iterator = todasOcorrencias.iterator();
		while (iterator.hasNext()) {
			final Ocorrencia item = iterator.next();

			// se for morador
			final String hqlMorador = "SELECT mor FROM Morador mor WHERE mor.idPessoa = :idPessoa ";
			final Query queryMorador = getEntityManager().createQuery(
					hqlMorador);
			queryMorador.setParameter("idPessoa", item.getPessoa()
					.getIdPessoa());
			final Morador morador = (Morador) queryMorador.getSingleResult();

			if (null == morador) {

				// se for agregado
				final String hqlAgregado = "SELECT agr FROM Agregado agr WHERE agr.idPessoa = :idPessoa ";
				final Query queryAgregado = getEntityManager().createQuery(
						hqlAgregado);
				queryAgregado.setParameter("idPessoa", item.getPessoa()
						.getIdPessoa());
				final Agregado agregado = (Agregado) queryAgregado
						.getSingleResult();

				if (null == agregado) {

					// se for visitante
					final String hqlVisitante = "SELECT vis FROM Visitante vis WHERE vis.idPessoa = :idPessoa ";
					final Query queryVisitante = getEntityManager()
							.createQuery(hqlVisitante);
					queryVisitante.setParameter("idPessoa", item.getPessoa()
							.getIdPessoa());
					final Visitante visitante = (Visitante) queryVisitante
							.getSingleResult();

					if (null == visitante) {

						// se for prestador
						final String hqlPj = "SELECT pj FROM PessoaJuridica pj WHERE pj.idPessoa = :idPessoa ";
						final Query queryPj = getEntityManager().createQuery(
								hqlPj);
						queryPj.setParameter("idPessoa", item.getPessoa()
								.getIdPessoa());
						final PessoaJuridica pj = (PessoaJuridica) queryPj
								.getSingleResult();

						if (null == pj) {

							final String hqlPf = "SELECT pf FROM PessoaFisica pf WHERE pf.idPessoa = :idPessoa ";
							final Query queryPf = getEntityManager()
									.createQuery(hqlPf);
							queryPf.setParameter("idPessoa", item.getPessoa()
									.getIdPessoa());
							final PessoaFisica pf = (PessoaFisica) queryPf
									.getSingleResult();

							if (null == pf) {
								iterator.remove();
							} else {
								item.setPessoa(pf);
							}
						} else {
							item.setPessoa(pj);
						}

					} else {
						item.setPessoa(visitante);
					}

				} else {
					item.setPessoa(agregado);
				}
			} else {
				item.setPessoa(morador);
			}
		}
		return todasOcorrencias;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OcorrenciaDTO> listarOcorrenciaMorador(final MoradorDTO morador)
			throws DAOException {

		final Query query = getEntityManager().createNamedQuery(
				"Ocorrencia.listarOcorrenciaMorador");
		query.setParameter("idPessoa", morador.getPessoa().getIdPessoa());

		final List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
		try {
			ocorrencias.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<Ocorrencia> todasOcorrencias = recuperaPessoas(ocorrencias);
		return OcorrenciaDTO.Builder.getInstance().createList(todasOcorrencias);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OcorrenciaDTO> listarOcorrenciaCondominio(
			final CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Ocorrencia.listarOcorrenciaCondominio");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}

		final List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
		try {
			ocorrencias.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<Ocorrencia> todasOcorrencias = recuperaPessoas(ocorrencias);
		return OcorrenciaDTO.Builder.getInstance().createList(todasOcorrencias);
	}

}
