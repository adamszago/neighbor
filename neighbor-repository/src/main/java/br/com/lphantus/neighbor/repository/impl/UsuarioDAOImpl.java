package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
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
import br.com.lphantus.neighbor.common.ModuloAcessoDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.PlanoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Condominio;
import br.com.lphantus.neighbor.entity.Usuario;
import br.com.lphantus.neighbor.repository.IUsuarioDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class UsuarioDAOImpl extends GenericDAOImpl<Long, UsuarioDTO, Usuario> implements IUsuarioDAO {

	@SuppressWarnings("unchecked")
	@Override
	public boolean existeUsuario(final UsuarioDTO usuario) {
		final Query query = getEntityManager().createNamedQuery("Usuario.existeUsuario");
		query.setParameter("login", usuario.getLogin());
		List<Usuario> lista = new ArrayList<Usuario>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return lista.size() > 0;
	}

	@Override
	public UsuarioDTO findByLogin(final String userName) throws DAOException {
		UsuarioDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Usuario.findByLogin");
		query.setParameter("login", userName);

		try {
			final Usuario objeto = ((Usuario) query.getSingleResult());
			retorno = UsuarioDTO.Builder.getInstance().create(objeto);
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um usuario para o login informado.");
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getTotalUsuariosByCondominio(final CondominioDTO condominio) {
		final Query query = getEntityManager().createNamedQuery("Usuario.findTotalUsuariosByCondominio");
		query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		List<Usuario> lista = new ArrayList<Usuario>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return Long.valueOf(lista.size());
	}

	@Override
	public UsuarioDTO buscarSindico(final CondominioDTO condominio) throws DAOException {
		UsuarioDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Usuario.buscarSindico");
		query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());

		try {
			final Usuario objeto = ((Usuario) query.getSingleResult());
			retorno = UsuarioDTO.Builder.getInstance().create(objeto);
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um sindico pro condominio.");
		}

		return retorno;
	}

	private CondominioDTO buscaCondominioMorador(final UsuarioDTO usuario) throws DAOException {
		CondominioDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Usuario.buscaCondominioMorador");
		query.setParameter("idPessoa", usuario.getPessoa().getIdPessoa());

		try {
			final Condominio objeto = (Condominio) query.getSingleResult();
			retorno = objeto.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um sindico pro condominio.");
		}

		return retorno;
	}

	@Override
	public CondominioDTO buscarCondominioUsuario(final UsuarioDTO usuario) throws DAOException {
		CondominioDTO retorno;
		retorno = buscaCondominioMorador(usuario);
		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioDTO> findAllWithoutRoot() throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Usuario.findAllWithoutRoot");

		final List<Usuario> lista = new ArrayList<Usuario>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<UsuarioDTO> retorno = new ArrayList<UsuarioDTO>();
		retorno.addAll(UsuarioDTO.Builder.getInstance().createList(lista));

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioDTO> findAllByIdCondominioWithoutRoot(final CondominioDTO condominio) throws DAOException {
		Query query;
		Long idCondominio;
		if (condominio == null) {
			idCondominio = null;
		} else {
			idCondominio = condominio.getPessoa().getIdPessoa();
		}
		query = getEntityManager().createNamedQuery("Usuario.findAllByIdCondominioWithoutRoot");
		query.setParameter("idCondominio", idCondominio);

		final List<Usuario> lista = new ArrayList<Usuario>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<UsuarioDTO> retorno = new ArrayList<UsuarioDTO>();
		retorno.addAll(UsuarioDTO.Builder.getInstance().createList(lista));

		return retorno;
	}

	@Override
	public UsuarioDTO buscaUsuarioMorador(final Long idMorador) throws DAOException {
		UsuarioDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Usuario.buscaUsuarioMorador");
		query.setParameter("idMorador", idMorador);

		try {
			final Usuario usuario = (Usuario) query.getSingleResult();
			retorno = UsuarioDTO.Builder.getInstance().create(usuario);
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um usuario pro morador especificado.");
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioDTO> buscarPorParametros(final CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Usuario.buscarPorParametros");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		final List<Usuario> usuarios = new ArrayList<Usuario>();
		try {
			usuarios.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<UsuarioDTO> retorno = new ArrayList<UsuarioDTO>();
		if ((null != usuarios) && !usuarios.isEmpty()) {
			for (final Usuario item : usuarios) {
				retorno.add(UsuarioDTO.Builder.getInstance().create(item));
			}
		}

		return retorno;
	}

	@Override
	public void atualizarUsuario(final UsuarioDTO usuario) throws DAOException {

		final Query queryPessoa = getEntityManager().createNamedQuery("Usuario.atualizarUsuarioPessoa");
		queryPessoa.setParameter("nome", usuario.getPessoa().getNome());
		queryPessoa.setParameter("mail", usuario.getPessoa().getMail());
		queryPessoa.setParameter("cpf", ((PessoaFisicaDTO) usuario.getPessoa()).getCpf());
		queryPessoa.setParameter("idPessoa", usuario.getPessoa().getIdPessoa());
		queryPessoa.executeUpdate();

		final Query queryUsuario = getEntityManager().createNamedQuery("Usuario.atualizarUsuario");
		queryUsuario.setParameter("sindico", usuario.isSindico());
		queryUsuario.setParameter("login", usuario.getLogin());
		queryUsuario.setParameter("senha", usuario.getSenha());
		queryUsuario.setParameter("condominio", CondominioDTO.Builder.getInstance().createEntity(usuario.getCondominio()));
		if (null == usuario.getPlano()) {
			queryUsuario.setParameter("plano", null);
		} else {
			queryUsuario.setParameter("plano", PlanoDTO.Builder.getInstance().createEntity(usuario.getPlano()));
		}
		queryUsuario.setParameter("moduloAcesso", ModuloAcessoDTO.Builder.getInstance().createEntity(usuario.getModuloAcesso()));
		queryUsuario.setParameter("idPessoa", usuario.getPessoa().getIdPessoa());
		queryUsuario.executeUpdate();
	}

	@Override
	public UsuarioDTO findByEmail(String email) throws DAOException {
		UsuarioDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Usuario.findByEmail");
		query.setParameter("email", email);
		try {
			Usuario usuario = (Usuario) query.getSingleResult();
			retorno = UsuarioDTO.Builder.getInstance().create(usuario);
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um usuario com o mesmo e-mail.");
		}
		return retorno;
	}

}
