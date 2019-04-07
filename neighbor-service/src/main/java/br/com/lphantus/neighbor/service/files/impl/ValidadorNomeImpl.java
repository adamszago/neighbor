package br.com.lphantus.neighbor.service.files.impl;

import java.io.InputStream;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.DocumentoDTO;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.files.IValidadorNome;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ValidadorNomeImpl implements IValidadorNome {

	@Override
	public void validateFile(DocumentoDTO entity, InputStream stream, long tamanho, String contentType) throws ServiceException {
		if ( null == entity.getNomeDocumento() || entity.getNomeDocumento().isEmpty() ) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("ARQUIVO_ENVIADO_NOME_VAZIO"));
		}
	}
	
}
