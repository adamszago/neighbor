package br.com.lphantus.neighbor.service.files;

import java.io.InputStream;

import br.com.lphantus.neighbor.common.DocumentoDTO;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IValidadorArquivo {

	public void validateFile(DocumentoDTO documento, InputStream stream, long tamanho, String contentType) throws ServiceException;

}
