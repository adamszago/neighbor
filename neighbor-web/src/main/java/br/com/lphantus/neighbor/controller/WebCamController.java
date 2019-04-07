package br.com.lphantus.neighbor.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.CaptureEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Historico;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.util.JsfUtil;

/**
 * 
 * @author Joander Vieira
 * @ManageBean identifica como o controle sera conhecido na pagina.
 * @Scope de request determina que o controle fica disponivel apenas durante a
 *        requisicao. Esta classe faz a interface entre a visao (xhtml) e as
 *        demais classes de servico e DAO
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "webCamController")
@Transactional(propagation = Propagation.SUPPORTS)
public class WebCamController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IUsuarioService usuarioService;

	// historico
	private Historico historico;

	// Imagem

	@Value("${neighbor.caminho.fotos}")
	private String diretorioFotos;

	@Value("${neighbor.caminho.imagemsemfoto}")
	private String imagemSemFoto;

	private StreamedContent fotoCapturada;
	private byte[] bytesFotoCapturada;
	private StreamedContent fotoTelaCadastro;

	/**
	 * Construtor de classe
	 */
	public WebCamController() {

	}

	@PostConstruct
	public void initialize() {
		try {
			final FileInputStream semFoto;
			if (null == imagemSemFoto) {
				semFoto = new FileInputStream(new File(imagemSemFoto));
				// this.imagemSemFoto = new DefaultStreamedContent(semFoto,
				// "image/png");
				if (null != semFoto) {
					semFoto.close();
				}
			} else {
				semFoto = null;
			}
		} catch (final FileNotFoundException e) {
			getLogger().error("Erro ao inicializar.", e);
			JsfUtil.addErrorMessage(e.getLocalizedMessage());
		} catch (final IOException e) {
			getLogger().error("Erro ao inicializar.", e);
			JsfUtil.addErrorMessage(e.getLocalizedMessage());
		}
		atualizarTela();
	}

	/**
	 * Metodo para início de cadastro. Este metodo e chamado no construtor do
	 * controller Tambem e responsavel por listar os registros existentes no
	 * banco e alimentar as listas
	 */
	private void atualizarTela() {
		this.historico = new Historico();
		this.fotoCapturada = null;
		this.fotoTelaCadastro = null;
		this.bytesFotoCapturada = null;
	}

	public void oncapture(final CaptureEvent captureEvent) {

		final byte[] bytesImagem = captureEvent.getData();

		final ByteArrayInputStream bais = new ByteArrayInputStream(bytesImagem);
		this.fotoCapturada = new DefaultStreamedContent(bais, "image/png");
		this.bytesFotoCapturada = bytesImagem;

		// ServletContext servletContext = (ServletContext)
		// FacesContext.getCurrentInstance().getExternalContext().getContext();
		// String nomeAbsolutoImagemTemp = DIRETORIO_FOTOS + this.nomeImagemTemp
		// + ".png";

		/*
		 * try { FileImageOutputStream imageOutput = new
		 * FileImageOutputStream(new File(nomeAbsolutoImagemTemp));
		 * imageOutput.write(bytesImagem, 0, bytesImagem.length);
		 * imageOutput.close(); }
		 * 
		 * catch(Exception e) { throw new
		 * FacesException("Erro ao capturar foto."); }
		 */
	}

	public void atualizarFotoTelaCadastro() {
		final ByteArrayInputStream bais = new ByteArrayInputStream(
				this.bytesFotoCapturada);
		this.fotoTelaCadastro = new DefaultStreamedContent(bais, "image/png");
	}

	public void novaFoto() {
		atualizarTela();
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_CADASTRO_USUARIO", "ROLE_ROOT" })
	public String novoUsuario() {
		atualizarTela();
		return "/pages/administracao/cadusuario.jsf";
	}

	// GETTER AND SETTER

	public UsuarioDTO getUserLogado() {
		return this.usuarioService.getUsuarioLogado();
	}

	/**
	 * @return the historico
	 */
	public Historico getHistorico() {
		return historico;
	}

	/**
	 * @param historico the historico to set
	 */
	public void setHistorico(Historico historico) {
		this.historico = historico;
	}

	/**
	 * @return the fotoCapturada
	 */
	public StreamedContent getFotoCapturada() {
		return fotoCapturada;
	}

	/**
	 * @param fotoCapturada the fotoCapturada to set
	 */
	public void setFotoCapturada(StreamedContent fotoCapturada) {
		this.fotoCapturada = fotoCapturada;
	}

	/**
	 * @return the bytesFotoCapturada
	 */
	public byte[] getBytesFotoCapturada() {
		return bytesFotoCapturada;
	}

	/**
	 * @param bytesFotoCapturada the bytesFotoCapturada to set
	 */
	public void setBytesFotoCapturada(byte[] bytesFotoCapturada) {
		this.bytesFotoCapturada = bytesFotoCapturada;
	}

	/**
	 * @return the fotoTelaCadastro
	 */
	public StreamedContent getFotoTelaCadastro() {
		return fotoTelaCadastro;
	}

	/**
	 * @param fotoTelaCadastro the fotoTelaCadastro to set
	 */
	public void setFotoTelaCadastro(StreamedContent fotoTelaCadastro) {
		this.fotoTelaCadastro = fotoTelaCadastro;
	}

}
