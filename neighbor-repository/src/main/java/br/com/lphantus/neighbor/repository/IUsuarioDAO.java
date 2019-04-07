package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Usuario;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IUsuarioDAO extends IGenericDAO<Usuario> {

	public boolean existeUsuario(final UsuarioDTO usuario);

	public UsuarioDTO findByLogin(final String userName) throws DAOException;

	public UsuarioDTO findByEmail(final String userName) throws DAOException;

	/**
	 * Retorna o total de Usuarios Cadastrados por determinado condominio.
	 * 
	 * @param condominio
	 * @param Condominio
	 *            Condominio a ser avaliado o total de usuarios cadastrados.
	 * @return Long Total de Usuarios Cadastrados
	 */
	Long getTotalUsuariosByCondominio(final CondominioDTO condominio);

	public UsuarioDTO buscarSindico(final CondominioDTO condominio)
			throws DAOException;

	/**
	 * Encontra o condominio de um usuario
	 * 
	 * @param usuario
	 *            O usuario
	 * @return O condominio
	 * @throws DAOException
	 */
	public CondominioDTO buscarCondominioUsuario(final UsuarioDTO usuario) throws DAOException;

	public List<UsuarioDTO> findAllWithoutRoot() throws DAOException;

	public List<UsuarioDTO> findAllByIdCondominioWithoutRoot(
			CondominioDTO condominio) throws DAOException;

	public UsuarioDTO buscaUsuarioMorador(Long idMorador) throws DAOException;

	public List<UsuarioDTO> buscarPorParametros(CondominioDTO condominio)
			throws DAOException;

	public void atualizarUsuario(UsuarioDTO usuario) throws DAOException;

}
