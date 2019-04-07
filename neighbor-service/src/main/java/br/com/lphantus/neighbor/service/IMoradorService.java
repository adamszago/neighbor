package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.service.exception.ServiceException;

/**
 * 
 * @author aalencar
 * @param <T>
 * 
 */
public interface IMoradorService extends IGenericService<Long, MoradorDTO, Morador> {

	MoradorDTO buscarPrincipal(final String identificao, final CondominioDTO condominio) throws ServiceException;

	MoradorDTO buscarPrincipalReserva(final String identificacao, final CondominioDTO condominio) throws ServiceException;

	MoradorDTO buscarPorPessoa(PessoaDTO pessoa) throws ServiceException;

	MoradorDTO validaMoradorExistente(final String identificacao, final CondominioDTO condominio) throws ServiceException;

	List<MoradorDTO> listarMoradores() throws ServiceException;

	List<MoradorDTO> findBuscaPersonalizada(final List<Integer> list) throws ServiceException;

	void registrarInadimplencia(final List<MoradorDTO> moradores) throws ServiceException;

	MoradorDTO buscarDetalhesMorador(final MoradorDTO morador) throws ServiceException;

	List<MoradorDTO> listarMoradoresCondominio(final CondominioDTO condominio, Boolean status) throws ServiceException;

	MoradorDTO buscarMoradorUsuario(UsuarioDTO usuarioLogado) throws ServiceException;

}
