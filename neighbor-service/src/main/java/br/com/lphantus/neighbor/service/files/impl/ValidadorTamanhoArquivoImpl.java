package br.com.lphantus.neighbor.service.files.impl;

import java.io.InputStream;
import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.DocumentoDTO;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.files.IValidadorTamanhoArquivo;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ValidadorTamanhoArquivoImpl implements IValidadorTamanhoArquivo {

	@Override
	public void validateFile(DocumentoDTO entity, InputStream stream, long tamanho, String contentType) throws ServiceException {
		if ( null == stream || BigDecimal.ZERO.longValue() == tamanho ) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("ARQUIVO_TAMANHO_ZERO"));
		}
	}
	
}
