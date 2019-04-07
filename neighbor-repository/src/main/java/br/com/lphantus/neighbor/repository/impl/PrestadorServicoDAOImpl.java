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
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.PessoaJuridicaDTO;
import br.com.lphantus.neighbor.common.PrestadorServicoDTO;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.PrestadorServico;
import br.com.lphantus.neighbor.repository.IPrestadorServicoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class PrestadorServicoDAOImpl extends GenericDAOImpl<Long, PrestadorServicoDTO, PrestadorServico> implements IPrestadorServicoDAO {

	@Override
	public PrestadorServicoDTO buscarPorId(PrestadorServicoDTO prestador) throws DAOException {
		Query query;

		if (prestador.getPessoa() instanceof PessoaFisicaDTO) {
			query = getEntityManager().createNamedQuery("PrestadorServico.findByIdPf");
		} else {
			query = getEntityManager().createNamedQuery("PrestadorServico.findByIdPj");
		}
		query.setParameter("idPrestador", prestador.getIdPrestador());

		try {
			final PrestadorServico prestadorServidoBanco = (PrestadorServico) query.getSingleResult();
			return PrestadorServicoDTO.Builder.getInstance().create(prestadorServidoBanco);
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean existeDocumento(final PrestadorServicoDTO prestador) throws DAOException {
		Query query;
		String documento;

		if (prestador.getPessoa() instanceof PessoaFisicaDTO) {
			query = getEntityManager().createNamedQuery("PrestadorServico.findByDocumentoPf");
			documento = ((PessoaFisicaDTO) prestador.getPessoa()).getCpf();
		} else {
			query = getEntityManager().createNamedQuery("PrestadorServico.findByDocumentoPj");
			documento = ((PessoaJuridicaDTO) prestador.getPessoa()).getCnpj();
		}
		query.setParameter("documento", documento);
		if (null == prestador.getCondominio() || null == prestador.getCondominio().getPessoa()) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", prestador.getCondominio().getPessoa().getIdPessoa());
		}

		final List<PrestadorServico> prestadores = new ArrayList<PrestadorServico>();
		try {
			prestadores.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		if ((prestadores != null) && (prestadores.size() > 0)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PrestadorServicoDTO> buscarConfirmadosPorCondominio(final CondominioDTO condominio) throws DAOException {
		final List<PrestadorServicoDTO> retorno = new ArrayList<PrestadorServicoDTO>();

		Long id;
		if (null == condominio) {
			id = null;
		} else {
			id = condominio.getPessoa().getIdPessoa();
		}
		final Query query = getEntityManager().createNamedQuery("PrestadorServico.buscarConfirmadosPorCondominio");
		query.setParameter("idCondominio", id);

		final List<PrestadorServico> prestadores = new ArrayList<PrestadorServico>();
		try {
			prestadores.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		retorno.addAll(PrestadorServicoDTO.Builder.getInstance().createList(prestadores));

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PrestadorServicoDTO> buscarPorCondominio(final CondominioDTO condominio) throws DAOException {
		final List<PrestadorServicoDTO> retorno = new ArrayList<PrestadorServicoDTO>();

		Long id;
		if (null == condominio) {
			id = null;
		} else {
			id = condominio.getPessoa().getIdPessoa();
		}
		final Query query = getEntityManager().createNamedQuery("PrestadorServico.buscarPorCondominio");
		query.setParameter("idCondominio", id);

		final List<PrestadorServico> prestadores = new ArrayList<PrestadorServico>();
		try {
			prestadores.addAll(query.getResultList());
			
			for(PrestadorServico item:prestadores){
				List<Morador> lista = new ArrayList<Morador>();
				lista.addAll(item.getEntradasLivres());
				item.setEntradasLivresList(lista);
			}
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		retorno.addAll(PrestadorServicoDTO.Builder.getInstance().createList(prestadores));
		return retorno;
	}

}
