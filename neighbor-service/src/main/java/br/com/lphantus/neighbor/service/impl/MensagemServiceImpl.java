package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.MensagemDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Mensagem;
import br.com.lphantus.neighbor.repository.IMensagemDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IMensagemService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MensagemServiceImpl extends
		GenericService<Long, MensagemDTO, Mensagem> implements IMensagemService {

	@Autowired
	private IMensagemDAO mensagemDAO;

	@Override
	public void saveMensagemDoSindico(final MensagemDTO mensagem)
			throws ServiceException {

		try {

			if ((mensagem.getDestinatario() == null)
					|| "".equals(mensagem.getDestinatario())) {
				throw new ServiceException(
						"Favor informar um destinatário para a mensagem");
			}
			if ((mensagem.getAssunto() == null)
					|| "".equals(mensagem.getAssunto())) {
				throw new ServiceException(
						"Favor informar um assunto para a mensagem");
			}

			if ((mensagem.getMensagem() == null)
					|| "".equals(mensagem.getMensagem())) {
				throw new ServiceException("Uma mensagem é obrigatória");
			}

			this.mensagemDAO.save(MensagemDTO.Builder.getInstance()
					.createEntity(mensagem));

		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public void saveMensagemSindico(final MensagemDTO mensagem)
			throws ServiceException {
		try {

			if ((mensagem.getAssunto() == null)
					|| "".equals(mensagem.getAssunto())) {
				throw new ServiceException(
						"Favor informar um assunto para a mensagem");
			}

			if ((mensagem.getMensagem() == null)
					|| "".equals(mensagem.getMensagem())) {
				throw new ServiceException("Uma mensagem é obrigatória");
			}

			if ((mensagem.getDestinatario() == null)
					|| "".equals(mensagem.getDestinatario().getPessoa()
							.getIdPessoa())) {
				throw new ServiceException("Condomínio sem síndico cadastrado!");
			}

			this.mensagemDAO.save(MensagemDTO.Builder.getInstance()
					.createEntity(mensagem));

		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<MensagemDTO> listarMensagensPorMorador(final MoradorDTO morador)
			throws ServiceException {
		try {
			return this.mensagemDAO.listarMensagemPorMorador(morador);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<MensagemDTO> listarMensagensPorMorador(final UsuarioDTO usuario)
			throws ServiceException {
		try {
			return this.mensagemDAO.listarMensagemPorMorador(usuario);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<MensagemDTO> listarMensagensRecebidasUsuario(
			final Long destinatarioId) throws ServiceException {
		try {
			return this.mensagemDAO
					.listarMensagensRecebidasUsuario(destinatarioId);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
