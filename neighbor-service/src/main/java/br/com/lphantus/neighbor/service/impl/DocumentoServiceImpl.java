package br.com.lphantus.neighbor.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DocumentoDTO;
import br.com.lphantus.neighbor.entity.Documento;
import br.com.lphantus.neighbor.repository.IDocumentoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IDocumentoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.files.IValidadorArquivo;
import br.com.lphantus.neighbor.service.files.IValidadorConteudoArquivo;
import br.com.lphantus.neighbor.service.files.IValidadorEspacoDisponivel;
import br.com.lphantus.neighbor.service.files.IValidadorNome;
import br.com.lphantus.neighbor.service.files.IValidadorTamanhoArquivo;
import br.com.lphantus.neighbor.service.files.IValidadorTamanhoMaximoArquivo;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class DocumentoServiceImpl extends GenericService<Long, DocumentoDTO, Documento> implements IDocumentoService {

	@Value("${neighbor.caminho.arquivoscondominio}")
	private String caminhoArquivos;
	
	@Autowired
	private IDocumentoDAO documentoDao;

	// ------------------ validadores
	@Autowired
	private IValidadorNome validadorNome;
	
	@Autowired
	private IValidadorTamanhoArquivo validadorTamanho;
	
	@Autowired
	private IValidadorTamanhoMaximoArquivo validadorTamanhoMaximoArquivo;
	
	@Autowired
	private IValidadorEspacoDisponivel validadorEspacoDisponivel;
	
	@Autowired
	private IValidadorConteudoArquivo validadorConteudoArquivo;
	
	private void validarArquivo(DocumentoDTO entity, InputStream inputstream, 
			long tamanhoArquivo, String contentType, IValidadorArquivo ... validadores) throws ServiceException{
		for(IValidadorArquivo validador:validadores){
			validador.validateFile(entity, inputstream, tamanhoArquivo, contentType);
		}
	}

	@Override
	public List<DocumentoDTO> findByCondominio(CondominioDTO condominio) throws ServiceException {
		try {
			return documentoDao.findByCondominio(condominio);
		} catch (DAOException ex) {
			getLogger().error("Erro ao buscar por condominio.", ex);
			throw new ServiceException(ex.getMessage(), ex);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void enviarArquivo(DocumentoDTO entity, InputStream inputstream, long tamanhoArquivo, String contentType) throws ServiceException {

		validarArquivo(entity, inputstream, tamanhoArquivo, contentType,
				validadorNome, validadorTamanho, validadorTamanhoMaximoArquivo,
				validadorEspacoDisponivel, validadorConteudoArquivo);
		
		// TODO: ver possibilidade quantidade de documentos por condominio
		// (criar uma query)
		
		entity.setTipoDocumento(entity.getNomeDocumento().substring(entity.getNomeDocumento().lastIndexOf('.') + 1));

		try {
			salvarDocumento(inputstream, entity, tamanhoArquivo);

		} catch (DAOException e) {
			getLogger().error("Erro ao criar novo documento.", e);
			throw new ServiceException(e.getMessage(), e);
		}

	}

	private void salvarDocumento(InputStream inputstream, DocumentoDTO entity, Long tamanhoArquivo) throws ServiceException, DAOException {
		try {
			DocumentoDTO documento = documentoDao.salvarNovoDocumento(entity);

			File arquivo = new File(String.format("%s%d.gz", caminhoArquivos, documento.getId()));
			FileOutputStream fileOs = new FileOutputStream(arquivo);
			GZIPOutputStream gzipOS = new GZIPOutputStream(fileOs);
			WritableByteChannel out = Channels.newChannel(gzipOS);

			// le o arquivo de 1 em 1 M
			int tamanho = 1048576;
			byte[] dados = new byte[tamanho];
			while (true) {
				int i = inputstream.read(dados);
				if (i < 0) {
					break;
				} else {
					try {
						out.write(ByteBuffer.wrap(dados, 0, i));

					} catch (BufferOverflowException | IndexOutOfBoundsException ex) {
						// JDK 7: tratamento de varias excecoes em um mesmo
						// catch
						getLogger().error("Ocorreu um erro ao realizar upload de arquivo.", ex);
						throw new ServiceException(GerenciadorMensagem.getMensagem("EXCEDIDO_TAMANHO_ARQUIVO"));
					}
				}
			}

			gzipOS.flush();

			// JDK 7: fechamento automatico dos recursos
			out.close();
			gzipOS.close();
			fileOs.close();

		} catch (FileNotFoundException e) {
			getLogger().error("Erro no envio de arquivo.", e);
			throw new ServiceException(GerenciadorMensagem.getMensagem("ERRO_ENVIO_ARQUIVO"));
		} catch (IOException e) {
			getLogger().error("Erro no envio de arquivo.", e);
			throw new ServiceException(GerenciadorMensagem.getMensagem("ERRO_ENVIO_ARQUIVO"));
		}

	}

	@Override
	public InputStream baixarArquivo(DocumentoDTO entity) throws ServiceException {
		try {

			String caminho = String.format("%s%d.gz", caminhoArquivos, entity.getId());
			File arquivo = new File(caminho);

			if (arquivo.exists()) {
				FileInputStream arquivoIs = new FileInputStream(arquivo);
				GZIPInputStream gzipIs = new GZIPInputStream(arquivoIs);

				return gzipIs;
			}
			throw new ServiceException(GerenciadorMensagem.getMensagem("ARQUIVO_NOT_FOUND"));

		} catch (IOException e) {
			getLogger().error("Erro ao baixar arquivo.", e);
			throw new ServiceException(GerenciadorMensagem.getMensagem("ERRO_BAIXAR_ARQUIVO"));
		}
	}

}
