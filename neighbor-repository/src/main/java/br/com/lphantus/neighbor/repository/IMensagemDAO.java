package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MensagemDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Mensagem;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IMensagemDAO extends IGenericDAO<Mensagem> {

	public List<MensagemDTO> buscaPorCondominio(CondominioDTO condominio)
			throws DAOException;

	public List<MensagemDTO> listarMensagemPorMorador(MoradorDTO morador)
			throws DAOException;

	public List<MensagemDTO> listarMensagemPorMorador(UsuarioDTO usuario)
			throws DAOException;

	public List<MensagemDTO> listarMensagensRecebidasUsuario(Long destinatarioId)
			throws DAOException;
}
