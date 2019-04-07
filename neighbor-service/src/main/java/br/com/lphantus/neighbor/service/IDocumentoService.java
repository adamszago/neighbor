package br.com.lphantus.neighbor.service;

import java.io.InputStream;
import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DocumentoDTO;
import br.com.lphantus.neighbor.entity.Documento;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IDocumentoService extends
		IGenericService<Long, DocumentoDTO, Documento> {

	public List<DocumentoDTO> findByCondominio(CondominioDTO condominio)
			throws ServiceException;

	public void enviarArquivo(DocumentoDTO entity, InputStream inputstream,
			long tamanhoArquivo, String contentType) throws ServiceException;

	public InputStream baixarArquivo(DocumentoDTO entity)
			throws ServiceException;

}
