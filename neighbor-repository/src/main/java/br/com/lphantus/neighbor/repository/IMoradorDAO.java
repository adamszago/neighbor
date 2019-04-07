package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IMoradorDAO extends IGenericDAO<Morador> {

	MoradorDTO buscarPrincipal(String identificacao, CondominioDTO condominio)
			throws DAOException;

	MoradorDTO buscarPorPessoa(PessoaDTO pessoa) throws DAOException;

	List<MoradorDTO> listarMoradores() throws DAOException;

	List<MoradorDTO> findBuscaPersonalizada(List<Integer> sequenciaCasas)
			throws DAOException;

	boolean existeCpf(MoradorDTO morador) throws DAOException;

	MoradorDTO buscarDetalhesMorador(MoradorDTO morador) throws DAOException;

	List<MoradorDTO> listarMoradoresCondominio(CondominioDTO condominio,
			Boolean status) throws DAOException;

	MoradorDTO buscarMoradorUsuario(UsuarioDTO usuarioLogado)
			throws DAOException;

}