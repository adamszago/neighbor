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
import br.com.lphantus.neighbor.common.MensagemDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Mensagem;
import br.com.lphantus.neighbor.repository.IMensagemDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MensagemDAOImpl extends
		GenericDAOImpl<Long, MensagemDTO, Mensagem> implements IMensagemDAO {

	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	// TODO: depreciado
	public List<MensagemDTO> buscaPorCondominio(final CondominioDTO condominio)
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Mensagem.buscaPorCondominio");
		query.setParameter("condominio", condominio);
		List<Mensagem> lista = new ArrayList<Mensagem>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return MensagemDTO.Builder.getInstance().createList(lista);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MensagemDTO> listarMensagemPorMorador(final MoradorDTO morador)
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Mensagem.listarMensagemPorMorador");
		query.setParameter("idPessoa", morador.getPessoa().getIdPessoa());

		final List<Mensagem> mensagens = new ArrayList<Mensagem>();
		try {
			mensagens.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return MensagemDTO.Builder.getInstance().createList(mensagens);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MensagemDTO> listarMensagemPorMorador(final UsuarioDTO usuario)
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Mensagem.listarMensagemPorMoradorUsuario");
		query.setParameter("idPessoa", usuario.getPessoa().getIdPessoa());

		final List<Mensagem> mensagens = new ArrayList<Mensagem>();
		try {
			mensagens.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return MensagemDTO.Builder.getInstance().createList(mensagens);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MensagemDTO> listarMensagensRecebidasUsuario(
			final Long destinatarioId) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Mensagem.listarMensagensRecebidasUsuario");
		query.setParameter("idPessoa", destinatarioId);

		final List<Mensagem> mensagens = new ArrayList<Mensagem>();
		try {
			mensagens.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return MensagemDTO.Builder.getInstance().createList(mensagens);
	}

}
