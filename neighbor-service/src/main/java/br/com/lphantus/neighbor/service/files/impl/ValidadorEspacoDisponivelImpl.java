package br.com.lphantus.neighbor.service.files.impl;

import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.DocumentoDTO;
import br.com.lphantus.neighbor.service.IConfiguracaoCondominioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.files.IValidadorEspacoDisponivel;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ValidadorEspacoDisponivelImpl implements IValidadorEspacoDisponivel {

	@Value("${neighbor.caminho.arquivoscondominio}")
	private String caminhoArquivos;
	
	@Autowired
	private IConfiguracaoCondominioService configuracaoCondominioService;

	@Override
	public void validateFile(DocumentoDTO entity, InputStream stream, long tamanho, String contentType) throws ServiceException {
		File caminho = new File(caminhoArquivos);

		if ( caminho.getFreeSpace() < tamanho ) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("DISCO_EXCEDIDO"));
		}
	}

}
