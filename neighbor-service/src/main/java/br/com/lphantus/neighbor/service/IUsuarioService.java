package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Usuario;
import br.com.lphantus.neighbor.service.exception.ServiceException;

/**
 * 
 * @author Joander Vieira
 * @param <T>
 * 
 */
public interface IUsuarioService extends IGenericService<Long, UsuarioDTO, Usuario> {

	public Boolean isLogado();

	public UsuarioDTO getUsuarioLogado();

	public CondominioDTO getCondominioUsuario(UsuarioDTO usuario) throws ServiceException;

	public void logout();

	public Long getTotalUsuariosByCondominio(CondominioDTO condominio);

	public String getLoginSessao();

	public List<MoradorDTO> buscaPersonalizada(List<Integer> casas) throws ServiceException;

	public UsuarioDTO buscarSindico(CondominioDTO condominio) throws ServiceException;

	public List<UsuarioDTO> findAllByIdCondominioWithoutRoot(CondominioDTO condominio) throws ServiceException;

	public List<UsuarioDTO> findAllWithoutRoot() throws ServiceException;

	public void saveUsuarioMorador(UsuarioDTO usuario) throws ServiceException;

	public UsuarioDTO getUsuarioByLogin(String login) throws ServiceException;

	public UsuarioDTO getUsuarioByEmail(String email) throws ServiceException;

	void update(UsuarioDTO usuario, String loginAnterior) throws ServiceException;

	public UsuarioDTO buscaUsuarioRoot() throws ServiceException;

	public UsuarioDTO buscaUsuarioMorador(Long idMorador) throws ServiceException;

	public List<UsuarioDTO> buscarPorParametros(CondominioDTO condominio) throws ServiceException;

}
