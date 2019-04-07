package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.MoradorAgregadoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.TotemDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Totem;
import br.com.lphantus.neighbor.service.IAgregadoService;
import br.com.lphantus.neighbor.service.IMoradorAgregadoService;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.ITotemService;
import br.com.lphantus.neighbor.service.IUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

/**
 * @author Fábio Borges de Oliveira Vilela
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "totemController")
@Transactional(propagation = Propagation.SUPPORTS)
public class TotemController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private ITotemService totemService;

	@Autowired
	private IMoradorService moradorService;

	@Autowired
	private IAgregadoService agregadoService;

	@Autowired
	private IUnidadeHabitacionalService unidadeHabitacionalService;

	@Autowired
	private IMoradorAgregadoService moradorAgregadoService;

	// historico
	private HistoricoDTO historico;

	// Cadastro Senha
	private TotemDTO totem;
	private List<TotemDTO> usuariosTotem;
	private String casa;
	private MoradorDTO morador;
	private AgregadoDTO agregado;
	private String senha;
	private String confirmarSenha;

	/**
	 * Construtor de classe
	 */
	public TotemController() {

	}

	@PostConstruct
	public void initialize() {
		this.totem = new TotemDTO();
		this.usuariosTotem = new ArrayList<TotemDTO>();
	}

	@Secured({ "ROLE_LISTA_TOTEM_ADM", "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String pageTotem() {

		buscarSenhaTotem();

		return "/pages/listagem/listausuariototem.jsf";
	}

	@Secured({ "ROLE_LISTA_TOTEM_ADM", "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String pageAddTotem() {
		return "/pages/administracao/cadsenhatotem.jsf";
	}

	/**
	 * @author Adams
	 * @since 07/10/2013 Teste para alteracao TOTEM sem necessidade duas
	 *        listagens
	 */
	@Secured({ "ROLE_LISTA_TOTEM_ADM", "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String editarTotem() {
		final RequestContext context = RequestContext.getCurrentInstance();
		boolean isMorador = false;
		if ((this.totem != null) && (this.totem.getMorador() != null)) {
			isMorador = true;
			this.morador = this.totem.getMorador();
		} else {
			this.agregado = this.totem.getAgregado();
		}
		context.addCallbackParam("isMorador", isMorador);
		return null;
	}

	/**
	 * TODO Nao utilizado em 07/10/2013
	 * 
	 * @return
	 */
	/*
	 * @Secured({"ROLE_PAGINA_MORADOR", "ROLE_ROOT"}) public String editar() {
	 * this.casa = totem.getCasa(); buscarProprietario(); return
	 * "/pages/administracao/cadsenhatotem.jsf"; }
	 */

	@Secured({ "ROLE_LISTA_TOTEM_ADM", "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluir() {
		try {
			Totem entidade = totemService.findById(this.totem.getId());
			this.totemService.delete(entidade);

			String mensagem = "Excluiu senha de totem. Id senha: %d - %s";
			if (null != totem.getMorador()) {
				mensagem = String.format(mensagem, entidade.getId(), totem.getMorador().getPessoa().getNome());
			} else {
				if (null != totem.getAgregado()) {
					mensagem = String.format(mensagem, entidade.getId(), totem.getAgregado().getPessoa().getNome());
				} else {
					mensagem = String.format("Excluiu senha de totem. Id senha: %d", entidade.getId());
				}
			}
			registrarHistorico(mensagem);

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("DELETE_OK"));

			buscarSenhaTotem();
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
	}

	private void buscarSenhaTotem() {
		try {
			final UsuarioDTO userLogado = usuarioLogado();

			final MoradorDTO moradorTemp = this.moradorService.buscarMoradorUsuario(userLogado);

			if (moradorTemp != null) {
				this.usuariosTotem = new ArrayList<TotemDTO>();
				this.usuariosTotem.addAll(this.totemService.buscarTodosAtivosPorMorador(moradorTemp));

				this.usuariosTotem.addAll(this.totemService.buscarMoradorSemTotemOuInativoPorMorador(moradorTemp));

				this.usuariosTotem.addAll(this.totemService.buscarAgregadoSemTotemOuInativoPorMorador(moradorTemp));

			} else {

				CondominioDTO condominio;
				if (userLogado.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}

				this.usuariosTotem = new ArrayList<TotemDTO>();
				this.usuariosTotem.addAll(this.totemService.buscarTodosAtivosPorCondominio(condominio));

				this.usuariosTotem.addAll(this.totemService.buscarMoradorSemTotemOuInativoPorCondominio(condominio));

				this.usuariosTotem.addAll(this.totemService.buscarAgregadoSemTotemOuInativoPorCondominio(condominio));

			}
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
	}

	public String buscarProprietario() {
		try {
			final MoradorDTO moradorTemp = this.moradorService.buscarPrincipal(this.casa, condominioUsuarioLogado());

			final UnidadeHabitacionalDTO unidade = this.unidadeHabitacionalService.buscarUnidadeHabitacionalMorador(moradorTemp);

			if ((moradorTemp != null) && (unidade.getCondominio() != null)) {
				if (unidade.getCondominio().getPessoa().getIdPessoa().equals(condominioUsuarioLogado().getPessoa().getIdPessoa())) {
					this.setMorador(moradorTemp);
					this.morador.setAgregados(this.moradorAgregadoService.listarAgregadosAtivosMorador(this.morador));
				} else {
					JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("CASA_INVALIDA"));
				}
			} else {
				JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("CASA_INVALIDA"));
			}

			this.totem = null;
			if (this.morador != null) {

				buscarMoradorTotem();
				if ((this.totem != null) && (this.totem.getId() != null)) {
					this.morador.getPessoa().setPossuiSenhaTotem(Boolean.TRUE);
				} else {
					this.morador.getPessoa().setPossuiSenhaTotem(Boolean.FALSE);
				}

				if (this.morador.getAgregados() != null) {
					for (final MoradorAgregadoDTO agregadoMorador : this.morador.getAgregados()) {

						this.agregado = agregadoMorador.getAgregado();
						buscarAgregadoTotem();

						if ((this.totem != null) && (this.totem.getId() != null)) {
							agregadoMorador.getAgregado().getPessoa().setPossuiSenhaTotem(Boolean.TRUE);
						} else {
							agregadoMorador.getAgregado().getPessoa().setPossuiSenhaTotem(Boolean.FALSE);
						}
					}
				}
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("CASA_INVALIDA"));
			this.morador = null;
		}
		return null;
	}

	@Secured({ "ROLE_LISTA_TOTEM_ADM", "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String buscarAgregadoTotem() {
		try {
			this.totem = this.totemService.buscarAgregadoTotem(this.agregado);
			if (this.totem == null) {
				this.totem = new TotemDTO();
				this.totem.setAgregado(this.agregado);
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getLocalizedMessage());
		}
		return null;
	}

	@Secured({ "ROLE_LISTA_TOTEM_ADM", "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String buscarMoradorTotem() {
		try {
			this.totem = this.totemService.buscarMoradorTotem(this.morador);
			if (this.totem == null) {
				this.totem = new TotemDTO();
				this.totem.setMorador(this.morador);
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getLocalizedMessage());
		}
		return null;
	}

	@Secured({ "ROLE_LISTA_TOTEM_ADM", "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarMorador(final ActionEvent event) {
		final RequestContext context = RequestContext.getCurrentInstance();
		boolean resultOk = false;
		try {
			// this.totem.setAgregado(null);
			// this.totem.setMorador(this.morador);

			if (validarSenha(this.senha, this.confirmarSenha)) {
				this.totem.setSenha(this.senha);
			}

			this.totemService.cadastrarSenhaTotem(this.totem);
			totemService.geraArquivoSenhas(totem);

			registrarHistorico("Cadastro de Senha Totem: " + this.getMorador().getUnidadeHabitacional().get(0).getUnidadeHabitacional().getIdentificacao() + " - "
					+ this.morador.getPessoa().getNome());

			this.morador.getPessoa().setPossuiSenhaTotem(Boolean.TRUE);

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
			this.senha = null;
			this.confirmarSenha = null;
			resultOk = true;
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		} finally {
			context.addCallbackParam("resultOk", resultOk);
		}
	}

	@Secured({ "ROLE_LISTA_TOTEM_ADM", "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarAgregado(final ActionEvent event) {
		final RequestContext context = RequestContext.getCurrentInstance();
		boolean resultOk = false;
		try {
			this.totem.setAgregado(this.agregado);
			this.totem.setMorador(null);
			if (validarSenha(this.senha, this.confirmarSenha)) {
				this.totem.setSenha(this.senha);
			}

			this.totemService.cadastrarSenhaTotem(this.totem);
			totemService.geraArquivoSenhas(totem);

			registrarHistorico("Cadastro de Senha Totem: " + this.getMorador().getUnidadeHabitacional().get(0).getUnidadeHabitacional().getIdentificacao() + " - "
					+ this.agregado.getPessoa().getNome());

			this.agregado.getPessoa().setPossuiSenhaTotem(Boolean.TRUE);

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
			this.senha = null;
			this.confirmarSenha = null;
			resultOk = true;
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		} finally {
			context.addCallbackParam("resultOk", resultOk);
		}
	}

	public boolean validarSenha(final String senha, final String contraSenha) throws ServiceException {
		if ((senha == null) || "".equals(senha)) {
			throw new ServiceException("Nenhuma senha informada!");
		}
		if ((contraSenha == null) || "".equals(contraSenha)) {
			throw new ServiceException("Senha de confirmação não foi informada!");
		}
		if (!senha.equals(contraSenha)) {
			throw new ServiceException("Senha diferente da confirmação!");
		}
		return true;
	}

	/**
	 * @return the casa
	 */
	public String getCasa() {
		return this.casa;
	}

	/**
	 * @param casa
	 *            the casa to set
	 */
	public void setCasa(final String casa) {
		this.casa = casa;
	}

	/**
	 * @return the morador
	 */
	public MoradorDTO getMorador() {
		return this.morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final MoradorDTO morador) {
		this.morador = morador;
	}

	/**
	 * @return the senha
	 */
	public String getSenha() {
		return this.senha;
	}

	/**
	 * @param senha
	 *            the senha to set
	 */
	public void setSenha(final String senha) {
		this.senha = senha;
	}

	/**
	 * @return the confirmarSenha
	 */
	public String getConfirmarSenha() {
		return this.confirmarSenha;
	}

	/**
	 * @param confirmarSenha
	 *            the confirmarSenha to set
	 */
	public void setConfirmarSenha(final String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	/**
	 * @return the historico
	 */
	public HistoricoDTO getHistorico() {
		return this.historico;
	}

	/**
	 * @param historico
	 *            the historico to set
	 */
	public void setHistorico(final HistoricoDTO historico) {
		this.historico = historico;
	}

	/**
	 * @return the totem
	 */
	public TotemDTO getTotem() {
		return this.totem;
	}

	/**
	 * @param totem
	 *            the totem to set
	 */
	public void setTotem(final TotemDTO totem) {
		this.totem = totem;
	}

	/**
	 * @return the usuariosTotem
	 */
	public List<TotemDTO> getUsuariosTotem() {
		return this.usuariosTotem;
	}

	/**
	 * @param usuariosTotem
	 *            the usuariosTotem to set
	 */
	public void setUsuariosTotem(final List<TotemDTO> usuariosTotem) {
		this.usuariosTotem = usuariosTotem;
	}

	/**
	 * @return the agregado
	 */
	public AgregadoDTO getAgregado() {
		return this.agregado;
	}

	/**
	 * @param agregado
	 *            the agregado to set
	 */
	public void setAgregado(final AgregadoDTO agregado) {
		this.agregado = agregado;
	}

}