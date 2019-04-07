package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.MensagemDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Mensagem;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IMensagemService extends
		IGenericService<Long, MensagemDTO, Mensagem> {

	public void saveMensagemDoSindico(MensagemDTO mensagem)
			throws ServiceException;

	public void saveMensagemSindico(MensagemDTO mensagem)
			throws ServiceException;

	List<MensagemDTO> listarMensagensPorMorador(MoradorDTO morador)
			throws ServiceException;

	List<MensagemDTO> listarMensagensPorMorador(UsuarioDTO usuario)
			throws ServiceException;

	List<MensagemDTO> listarMensagensRecebidasUsuario(Long destinatarioId)
			throws ServiceException;
}
