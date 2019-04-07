package br.com.lphantus.neighbor.service.files.impl;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.net.MediaType;

import br.com.lphantus.neighbor.common.DocumentoDTO;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.files.IValidadorConteudoArquivo;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ValidadorConteudoArquivoImpl implements IValidadorConteudoArquivo {
	
	private boolean validaTipo(MediaType tipo, String contentType){
		Pattern padrao = Pattern.compile(tipo.toString());
		Matcher matcher = padrao.matcher(contentType);
		return matcher.find();
	}
	
	private boolean validaImagem(String contentType){
		return validaTipo(MediaType.ANY_IMAGE_TYPE, contentType);
	}
	
	private boolean validaTexto(String contentType){
		return validaTipo(MediaType.ANY_TEXT_TYPE, contentType);
	}
	
	private boolean validaWord(String contentType){
		return validaTipo(MediaType.MICROSOFT_WORD, contentType);
	}
	
	private boolean validaExcel(String contentType){
		return validaTipo(MediaType.MICROSOFT_EXCEL, contentType);
	}
	
	private boolean validaPowerpoint(String contentType){
		return validaTipo(MediaType.MICROSOFT_POWERPOINT, contentType);
	}
	
	private boolean validaPdf(String contentType){
		return validaTipo(MediaType.PDF, contentType);
	}
	
	@Override
	public void validateFile(DocumentoDTO entity, InputStream stream, long tamanho, String contentType) throws ServiceException {
		if ( validaImagem(contentType) || validaTexto(contentType) 
				|| validaWord(contentType) || validaExcel(contentType) || validaPowerpoint(contentType) 
				|| validaPdf(contentType) ){
			throw new ServiceException(GerenciadorMensagem.getMensagem("ARQUIVO_ENVIADO_INVALIDO"));
		}
	}
	
}
