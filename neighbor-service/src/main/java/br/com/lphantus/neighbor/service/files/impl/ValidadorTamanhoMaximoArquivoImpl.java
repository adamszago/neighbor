package br.com.lphantus.neighbor.service.files.impl;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.DocumentoDTO;
import br.com.lphantus.neighbor.service.IConfiguracaoCondominioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.files.IValidadorTamanhoMaximoArquivo;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ValidadorTamanhoMaximoArquivoImpl implements IValidadorTamanhoMaximoArquivo {

	@Autowired
	private IConfiguracaoCondominioService configuracaoCondominioService;

	@Override
	public void validateFile(DocumentoDTO entity, InputStream stream, long tamanho, String contentType) throws ServiceException {
		ConfiguracaoCondominioDTO configuracao;
		configuracao = configuracaoCondominioService.buscarPorCondominio(entity.getCondominio());
		if (null == configuracao.getTamanhoMaximoArquivos()) {
			int tamanhoMaximo = 25;
			configuracao.setTamanhoMaximoArquivos(tamanhoMaximo);
			configuracaoCondominioService.update(ConfiguracaoCondominioDTO.Builder.getInstance().createEntity(configuracao));
		}

		int total = configuracao.getTamanhoMaximoArquivos() * 1048576;
		if (tamanho > total) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("EXCEDIDO_TAMANHO_ARQUIVO"));
		}
	}

}
