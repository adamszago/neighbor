package br.com.lphantus.neighbor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.PermissaoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

/**
 * Classe de integracao entre o spring security e as permissoes neighbor
 * 
 * @author elias.policena@lphantus.com.br
 * @since 08/11/2014
 *
 */
@Service("neighborUserDetailsService")
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class NeighborUserDetailService implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUsuarioService usuarioService;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			UsuarioDTO usuario = usuarioService.getUsuarioByLogin(username);
			if (null != usuario) {
				User retorno = buildUserForAuthentication(usuario);
				return retorno;
			}
		} catch (ServiceException e) {
			logger.error("Ocorreu um erro na aplicacao.", e);
		}
		throw new UsernameNotFoundException("Usuário não encontrado.");
	}

	private User buildUserForAuthentication(UsuarioDTO usuario) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		buildAuthorities(usuario.getModuloAcesso().getPermissoes(), authorities);
		User retorno = new User(usuario.getLogin(), usuario.getSenha(), authorities);
		return retorno;
	}

	private void buildAuthorities(Set<PermissaoDTO> permissoes, List<GrantedAuthority> authorities) {
		for (PermissaoDTO permissao : permissoes) {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissao.getNome());
			authorities.add(authority);
		}
	}

}
