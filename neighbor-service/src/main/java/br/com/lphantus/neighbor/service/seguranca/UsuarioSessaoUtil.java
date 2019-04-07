package br.com.lphantus.neighbor.service.seguranca;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Permissao;
import br.com.lphantus.neighbor.repository.IUsuarioDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
public class UsuarioSessaoUtil {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUsuarioDAO daoUsuario;

	Authentication authentication;

	private Authentication getAutenticacaoUserLogado() {
		this.authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authenticationIsAnonymousOrNull()) {
			this.authentication = null;
		}

		return this.authentication;
	}

	private boolean authenticationIsAnonymousOrNull() {
		if (this.authentication != null) {
			if (this.authentication.getAuthorities().equals("ROLE_ANONYMOUS")
					|| this.authentication.getName().equals("anonymousUser")) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public List<String> getPermissoesUserLogado() {
		final Authentication authentication = getAutenticacaoUserLogado();
		List<String> roles = null;

		if (authentication != null) {
			roles = new ArrayList<String>();

			for (final GrantedAuthority a : authentication.getAuthorities()) {
				roles.add(a.getAuthority());
			}
		}
		return roles;
	}

	public Boolean isPermitido(final String nomePermissao) {
		return verificarIsPermitido(nomePermissao);
	}

	public Boolean isPermitido(final Permissao permissao) {
		return verificarIsPermitido(permissao.getNome());
	}

	public Boolean verificarIsPermitido(final String nomePermissao) {
		Boolean permitido = false;
		for (final String p : getPermissoesUserLogado()) {
			if (p.equals(nomePermissao)) {
				permitido = true;
			}
		}
		return permitido;
	}

	public UsuarioDTO getUsuarioLogado() {
		try {
			final Authentication authentication = getAutenticacaoUserLogado();
			String userName = null;
			UsuarioDTO usuario = null;
			if (authentication != null) {
				userName = authentication.getName();
				usuario = this.daoUsuario.findByLogin(userName);
			}
			return usuario;
		} catch (DAOException ex) {
			logger.info("Erro.", ex);
			return null;
		}
	}

	public String getLoginSessao() {
		final Authentication authentication = getAutenticacaoUserLogado();
		if (authentication != null) {
			return authentication.getName();
		} else {
			return null;
		}
	}

	public Boolean isLogado() {
		final Authentication authentication = getAutenticacaoUserLogado();

		if (authentication == null) {
			return false;
		} else {
			return true;
		}
	}

	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		invalidateSession();
		this.authentication = null;
	}

	private void invalidateSession() {
		final FacesContext fc = FacesContext.getCurrentInstance();
		fc.getExternalContext().invalidateSession();
	}

}
