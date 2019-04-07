package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
import java.util.Iterator;
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
import br.com.lphantus.neighbor.common.MoradorAgregadoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.MoradorUnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.repository.IMoradorDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MoradorDAOImpl extends GenericDAOImpl<Long, MoradorDTO, Morador> implements IMoradorDAO {

	@Override
	public MoradorDTO buscarPrincipal(final String casa, final CondominioDTO condominio) throws DAOException {

		MoradorDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Morador.buscarPrincipal");
		query.setParameter("identificacao", casa);
		query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());

		try {
			final Morador m = (Morador) query.getSingleResult();
			retorno = m.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um morador para a casa especificada.");
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MoradorDTO> listarMoradores() throws DAOException {
		Query query = getEntityManager().createNamedQuery("Morador.listarMoradores");
		List<Morador> moradores = new ArrayList<Morador>();
		try {
			moradores.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return MoradorDTO.Builder.getInstance().createList(moradores);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MoradorDTO> findBuscaPersonalizada(final List<Integer> sequenciaCasas) throws DAOException {

		final List<Long> sequencia = new ArrayList<Long>();
		for (final Integer numero : sequenciaCasas) {
			sequencia.add(numero.longValue());
		}

		final Query query;
		if (sequencia.isEmpty()) {
			query = getEntityManager().createNamedQuery("Morador.findBuscaPersonalizada");
		} else {
			query = getEntityManager().createNamedQuery("Morador.findBuscaPersonalizadaSequencia");
			query.setParameter("sequencia", sequencia);
		}

		List<Morador> lista = new ArrayList<Morador>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return MoradorDTO.Builder.getInstance().createList(lista);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean existeCpf(final MoradorDTO morador) throws DAOException {
		Query query = getEntityManager().createNamedQuery("Morador.existeCpf");
		query.setParameter("cpf", morador.getPessoa().getCpf());
		
		// filtrar por condominio
		boolean existeUnidade = false;
		if ( null != morador.getUnidadeHabitacional() && !morador.getUnidadeHabitacional().isEmpty() ){
			List<MoradorUnidadeHabitacionalDTO> lista = morador.getUnidadeHabitacional();
			MoradorUnidadeHabitacionalDTO relacionamento = lista.get(0);
			UnidadeHabitacionalDTO unidade = relacionamento.getUnidadeHabitacional();
			if ( null != unidade ){
				CondominioDTO condominio = unidade.getCondominio();
				if ( null != condominio ){
					query.setParameter("condominio", CondominioDTO.Builder.getInstance().createEntity(condominio));
					existeUnidade = true;
				}
			}
		}
		
		if ( !existeUnidade ){
			query.setParameter("condominio", null);
		}

		final List<Morador> mrd = new ArrayList<Morador>();
		try {
			mrd.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		if (mrd.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public MoradorDTO buscarDetalhesMorador(final MoradorDTO morador) throws DAOException {

		final MoradorDTO moradorRetorno;

		final Query query = getEntityManager().createNamedQuery("Morador.buscarDetalhesMorador");
		query.setParameter("idPessoa", morador.getPessoa().getIdPessoa());

		final Morador entidade = (Morador) query.getSingleResult();
		moradorRetorno = MoradorDTO.Builder.getInstance().createDetalhes(entidade);

		final Iterator<MoradorAgregadoDTO> iterator = moradorRetorno.getAgregados().iterator();
		while (iterator.hasNext()) {
			final MoradorAgregadoDTO item = iterator.next();
			if ((item.getDataFim() != null) || (item.getAgregado().getPessoa().isAtivo() == false)) {
				iterator.remove();
			}
		}

		return moradorRetorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MoradorDTO> listarMoradoresCondominio(final CondominioDTO condominio, final Boolean status) throws DAOException {
		Query query = getEntityManager().createNamedQuery("Morador.listarMoradoresCondominio");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}
		if (null == status) {
			query.setParameter("status", null);
		} else {
			query.setParameter("status", status.booleanValue());
		}

		final List<Morador> lista = new ArrayList<Morador>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return MoradorDTO.Builder.getInstance().createList(lista);
	}

	@Override
	public MoradorDTO buscarMoradorUsuario(final UsuarioDTO usuarioLogado) throws DAOException {
		MoradorDTO retorno;
		if ((usuarioLogado == null) || (usuarioLogado.getPessoa() == null)) {
			retorno = null;
		} else {

			final Query query = getEntityManager().createNamedQuery("Morador.buscarMoradorUsuario");
			query.setParameter("idPessoa", usuarioLogado.getPessoa().getIdPessoa());

			retorno = null;
			try {
				final Morador objeto = ((Morador) query.getSingleResult());
				retorno = objeto.createDto();
			} catch (NoResultException nre) {
				// nao fazer nada
			} catch (NonUniqueResultException nure) {
				throw new DAOException("Foi encontrado mais de um morador para este usuario.");
			}
		}

		return retorno;
	}

	@Override
	public MoradorDTO buscarPorPessoa(final PessoaDTO pessoa) throws DAOException {
		MoradorDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Morador.buscarPorPessoa");
		query.setParameter("idPessoa", pessoa.getIdPessoa());

		try {
			final Morador objeto = ((Morador) query.getSingleResult());
			retorno = objeto.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um morador para a pessoa especificada.");
		}
		return retorno;
	}

}
