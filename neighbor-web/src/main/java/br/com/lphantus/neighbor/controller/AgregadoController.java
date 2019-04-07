package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
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

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.service.IAgregadoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "agregadoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class AgregadoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IAgregadoService service;

	// Agregado
	private List<AgregadoDTO> agregados = null;
	private AgregadoDTO agregado;

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor padrao
	 */
	public AgregadoController() {

	}

	@PostConstruct
	public void initialize() {
		atualizarTela();
	}

	/**
	 * Metodo para início de cadastro. Este metodo e chamado no construtor do
	 * controller Tambem e responsavel por listar os registros existentes no
	 * banco e alimentar a lista
	 */
	private void atualizarTela() {
		this.agregado = new AgregadoDTO();
		this.agregados = new ArrayList<AgregadoDTO>();

		gerarListaAgregados();
	}

	private void gerarListaAgregados() {
		try {
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (usuarioLogado().isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.agregados = this.service.listarTodosAgregados(condominio);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de agregados.", e);
		}
	}

	@Secured({ "ROLE_ALTERAR_STATUS_AGREGADO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterarStatus() {
		String novoStatus = "";
		try {
			if (this.agregado.getPessoa().isAtivo()) {
				this.agregado.getPessoa().setAtivo(false);
				novoStatus = "ATIVO";
			} else {
				this.agregado.getPessoa().setAtivo(true);
				novoStatus = "INATIVO";
			}
			this.service.update(AgregadoDTO.Builder.getInstance().createEntity(
					this.agregado));

			registrarHistorico("Alterou status do agregado para: " + novoStatus
					+ " " + this.agregado.getPessoa().getIdPessoa() + " - "
					+ this.agregado.getPessoa().getNome());

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
		return null;
	}

	/**
	 * Exclui um registro da tabela morador
	 * 
	 * @throws ServiceException
	 */
	@Secured({ "ROLE_EXCLUIR_AGREGADO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluir() {
		try {
			this.service.delete(AgregadoDTO.Builder.getInstance().createEntity(
					this.agregado));

			registrarHistorico("Excluiu agregado: "
					+ this.agregado.getPessoa().getIdPessoa() + " - "
					+ this.agregado.getPessoa().getNome());

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("DELETE_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
		atualizarTela();
	}

	@Secured({ "ROLE_EDITAR_AGREGADO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterar(final ActionEvent event) {
		try {
			this.service.update(AgregadoDTO.Builder.getInstance().createEntity(
					this.agregado));

			registrarHistorico("Atualizou Dados do Agregado: "
					+ this.agregado.getPessoa().getIdPessoa() + " - "
					+ this.agregado.getPessoa().getNome());

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));
		} catch (final Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_LISTA_AGREGADOS", "ROLE_ROOT" })
	public String listarAgregados() {
		this.agregado = new AgregadoDTO();
		gerarListaAgregados();

		return "/pages/listagem/listaagregados.jsf";
	}

	public String editarAgregado() {
		return null;
	}

	public void atualizar() {
		this.atualizarTela();
	}

	public void getAgregadosMorador() {
		atualizarTela();
	}

	// GETTERS E SETTERS

	/**
	 * @return the agregados
	 */
	public List<AgregadoDTO> getAgregados() {
		return agregados;
	}

	/**
	 * @param agregados
	 *            the agregados to set
	 */
	public void setAgregados(List<AgregadoDTO> agregados) {
		this.agregados = agregados;
	}

	/**
	 * @return the agregado
	 */
	public AgregadoDTO getAgregado() {
		return agregado;
	}

	/**
	 * @param agregado
	 *            the agregado to set
	 */
	public void setAgregado(AgregadoDTO agregado) {
		this.agregado = agregado;
	}

	/**
	 * @return the historico
	 */
	public HistoricoDTO getHistorico() {
		return historico;
	}

	/**
	 * @param historico
	 *            the historico to set
	 */
	public void setHistorico(HistoricoDTO historico) {
		this.historico = historico;
	}

}
