package br.com.lphantus.neighbor.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DocumentoDTO;
import br.com.lphantus.neighbor.service.IDocumentoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;
import br.com.lphantus.neighbor.utils.IOUtils;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "documentoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class DocumentoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<DocumentoDTO> documentos = new ArrayList<DocumentoDTO>();
	private UploadedFile file;
	private DocumentoDTO entity;

	@Autowired
	private IDocumentoService documentoService;

	// TODO: envio de documento
	// TODO: alterar no header.xhtml o metodo que chama esse controller

	@Secured({ "ROLE_DISPONIBILIZAR_ARQUIVOS", "ROLE_ROOT" })
	public String pageDocumento() {

		try {
			this.entity = new DocumentoDTO();
			popularListaDocumentos();

		} catch (final ServiceException e) {
			getLogger().debug("Erro ao gerar balancete mensal.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().debug("Erro ao gerar balancete mensal.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		return "/pages/administracao/documentos.jsf";
	}

	private void popularListaDocumentos() throws ServiceException {
		CondominioDTO condominio = condominioUsuarioLogado();
		List<DocumentoDTO> lista = documentoService.findByCondominio(condominio);
		documentos.clear();
		documentos.addAll(lista);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String enviar() {
		try {

			if (file != null) {
				entity.setDataCadastro(new Date());
				entity.setCondominio(condominioUsuarioLogado());
				entity.setNomeDocumento(file.getFileName());
				documentoService.enviarArquivo(entity, file.getInputstream(), file.getSize(), file.getContentType());

				registrarHistorico("Disponibilizou arquivo no sistema.");
				popularListaDocumentos();
				
				// TODO: corrigir a pagina de documento
				// criar visualizacao na pagina do morador

				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));
				entity = new DocumentoDTO();
			}

		} catch (ServiceException e) {
			getLogger().error("Erro ao enviar arquivo.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (IOException e) {
			getLogger().error("Erro ao enviar arquivo.", e);
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERRO_ENVIO_ARQUIVO"));
		}
		return "";
	}

	public void download() {
		try {

			InputStream arquivo = documentoService.baixarArquivo(entity);
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();

			ec.responseReset();
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + entity.getNomeDocumento() + "\"");

			final OutputStream output = ec.getResponseOutputStream();
			IOUtils.copy(arquivo, output);

			fc.responseComplete();

			registrarHistorico("Baixou arquivo no sistema.");
		} catch (ServiceException e) {
			getLogger().error("Erro ao baixar arquivo.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (IOException e) {
			getLogger().error("Erro ao baixar arquivo.", e);
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERRO_BAIXAR_ARQUIVO"));
		}
	}

	/**
	 * @return the documentos
	 */
	public List<DocumentoDTO> getDocumentos() {
		return documentos;
	}

	/**
	 * @param documentos
	 *            the documentos to set
	 */
	public void setDocumentos(List<DocumentoDTO> documentos) {
		this.documentos = documentos;
	}

	/**
	 * @return the file
	 */
	public UploadedFile getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(UploadedFile file) {
		this.file = file;
	}

	/**
	 * @return the entity
	 */
	public DocumentoDTO getEntity() {
		if (null == entity) {
			entity = new DocumentoDTO();
		}
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(DocumentoDTO entity) {
		this.entity = entity;
	}

}
