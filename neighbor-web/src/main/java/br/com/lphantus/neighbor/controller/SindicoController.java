package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.SindicoDTO;
import br.com.lphantus.neighbor.service.ISindicoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

/**
 * 
 * @author aalencar Anotacao
 * @ManageBean identifica como o controle sera conhecido na pagina.
 * @Scope de request determina que o controle fica disponivel apenas durante a
 *        requisicao. Esta classe faz a interface entre a visao (xhtml) e as
 *        demais classes de servico e DAO
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "sindicoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class SindicoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ISindicoService service;

	private SindicoDTO sindico;
	private List<SindicoDTO> sindicos = null;
	private String senhaConfirmacao;
	private MoradorDTO moradorSelecionado;
	private Date dataMinima;

	/**
	 * Construtor padrao
	 */
	public SindicoController() {

	}

	@PostConstruct
	public void atualizarTela() {
		this.sindico = new SindicoDTO();
		this.senhaConfirmacao = null;
		this.moradorSelecionado = new MoradorDTO();
		this.dataMinima = new Date();
		this.sindicos = new ArrayList<SindicoDTO>();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void gravar(final ActionEvent e) {
		try {
			this.service.save(SindicoDTO.Builder.getInstance().createEntity(
					this.sindico));
			atualizarTela();

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("SAVE_OK"));
		} catch (final ServiceException se) {
			JsfUtil.addErrorMessage(se.getMessage());
		}
	}

	public String selecionarMorador() {

		this.sindico.setPessoa(this.moradorSelecionado.getPessoa());
		// this.sindico.setUsuario(this.moradorSelecionado.getUsuario());;

		return null;
	}

	public String novoSindico() {
		atualizarTela();
		return "/pages/administracao/cadsindico.jsf";
	}

	public String editarSindico() {
		return "editarSindico";
	}

	/**
	 * Exclui um registro da tabela morador
	 * 
	 * @throws ServiceException
	 */
	/*
	 * public void excluir() { try { service.delete(sindico); //atualizarTela();
	 * JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("DELETE_OK"));
	 * } catch (ServiceException e) { JsfUtil.addSuccessMessage(e.getMessage());
	 * } atualizarTela(); }
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterar(final ActionEvent event) {
		try {
			this.service.update(SindicoDTO.Builder.getInstance().createEntity(
					this.sindico));
			atualizarTela();

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * Exclui um registro da tabela morador
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String ativarInativar() {
		try {

			if (this.sindico.getPessoa().isAtivo()) {
				this.sindico.getPessoa().setAtivo(false);
			} else {
				this.sindico.getPessoa().setAtivo(true);
			}

			this.service.update(SindicoDTO.Builder.getInstance().createEntity(
					this.sindico));
			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

			atualizarTela();

		} catch (final ServiceException e) {

			JsfUtil.addErrorMessage(e.getMessage());

		}

		return null;
	}

	@Secured({ "ROLE_ROOT" })
	public String pageListaSindico() {
		atualizarTela();
		return "/pages/administracao/listasindicos.jsf";
	}

	@Secured({ "ROLE_ROOT" })
	public String pageNovoSindico() {
		atualizarTela();
		return "/pages/administracao/cadsindico.jsf";
	}

	/**
	 * @return the dataMinima
	 */
	public Date getDataMinima() {
		return dataMinima;
	}

	/**
	 * @param dataMinima
	 *            the dataMinima to set
	 */
	public void setDataMinima(Date dataMinima) {
		this.dataMinima = dataMinima;
	}

	/**
	 * @return the sindico
	 */
	public SindicoDTO getSindico() {
		return sindico;
	}

	/**
	 * @param sindico
	 *            the sindico to set
	 */
	public void setSindico(SindicoDTO sindico) {
		this.sindico = sindico;
	}

	/**
	 * @return the senhaConfirmacao
	 */
	public String getSenhaConfirmacao() {
		return senhaConfirmacao;
	}

	/**
	 * @param senhaConfirmacao
	 *            the senhaConfirmacao to set
	 */
	public void setSenhaConfirmacao(String senhaConfirmacao) {
		this.senhaConfirmacao = senhaConfirmacao;
	}

	/**
	 * @return the sindicos
	 */
	public List<SindicoDTO> getSindicos() {
		return sindicos;
	}

	/**
	 * @param sindicos
	 *            the sindicos to set
	 */
	public void setSindicos(List<SindicoDTO> sindicos) {
		this.sindicos = sindicos;
	}

	/**
	 * @return the moradorSelecionado
	 */
	public MoradorDTO getMoradorSelecionado() {
		return moradorSelecionado;
	}

	/**
	 * @param moradorSelecionado
	 *            the moradorSelecionado to set
	 */
	public void setMoradorSelecionado(MoradorDTO moradorSelecionado) {
		this.moradorSelecionado = moradorSelecionado;
	}

}
