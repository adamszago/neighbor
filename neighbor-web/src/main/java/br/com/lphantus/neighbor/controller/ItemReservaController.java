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

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.ItemReservaDTO;
import br.com.lphantus.neighbor.service.ItemReservaService;
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
@ManagedBean(name = "itemReservaController")
@Transactional(propagation = Propagation.SUPPORTS)
public class ItemReservaController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private ItemReservaService service;

	// Item Reserva
	private List<ItemReservaDTO> itensReserva = null;
	private ItemReservaDTO itemReserva;

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor padrao
	 */
	public ItemReservaController() {

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
		this.itemReserva = new ItemReservaDTO();
		this.itensReserva = new ArrayList<ItemReservaDTO>();

		gerarListaItemReserva();
	}

	private void gerarListaItemReserva() {
		try {
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				Boolean status;
				if (usuarioLogado().isRoot()) {
					condominio = null;
					status = null;
				} else {
					condominio = condominioUsuarioLogado();
					status = Boolean.TRUE;
				}
				this.itensReserva = this.service.listarPorCondominio(condominio, status);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de item de reserva.", e);
		}
	}

	/**
	 * Método de Salvar registro Este método utilizar o objeto Service obtido
	 * atarvés de injeção pelo construtor. A classe JsfUtil providencia
	 * mensagens do tipo FacesMessage para serem enviadas A classe
	 * GerenciadoMensagem usa mensagens pre-definidas em arquivo properties para
	 * serem enviadas.
	 */
	@Secured({ "ROLE_CADASTRO_ITEM_RESERVA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarItemReserva(final ActionEvent event) {
		try {
			if (condominioUsuarioLogado() != null) {
				this.itemReserva.setCondominio(condominioUsuarioLogado());
			}
			this.service.save(ItemReservaDTO.Builder.getInstance().createEntity(this.itemReserva));

			registrarHistorico(String.format("Gravou Item Reserva: \"%s\"", this.itemReserva.getNome()));

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_EDITAR_ITEM_RESERVA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterarItemReserva(final ActionEvent event) {
		try {
			this.service.update(ItemReservaDTO.Builder.getInstance().createEntity(this.itemReserva));

			registrarHistorico("Atualizou dados do Item Reserva: " + this.itemReserva.getId() + " - " + this.itemReserva.getNome());

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * Exclui um registro da tabela morador
	 * 
	 * @throws ServiceException
	 */
	@Secured({ "ROLE_ALTERAR_STATUS_ITEM_RESERVA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String ativacao() {
		String novoStatus = "";
		try {
			if (this.itemReserva.isAtivo()) {
				this.itemReserva.setAtivo(false);
				novoStatus = "INATIVO";
			} else {
				this.itemReserva.setAtivo(true);
				novoStatus = "ATIVO";
			}
			this.service.update(ItemReservaDTO.Builder.getInstance().createEntity(this.itemReserva));

			registrarHistorico("Alterou status do Item Reserva para: " + novoStatus + " - " + this.itemReserva.getId() + " - " + this.itemReserva.getNome());

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
		return null;
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_CADASTRO_ITEM_RESERVA", "ROLE_ROOT" })
	public String novoItemReserva() {
		this.atualizarTela();
		return "/pages/administracao/caditemreserva.jsf";
	}

	@Secured({ "ROLE_CADASTRO_ITEM_RESERVA", "ROLE_ROOT" })
	public String pageListaItemReserva() {
		this.atualizarTela();
		return "/pages/administracao/listaitemreserva.jsf";
	}

	@Secured({ "ROLE_EDITAR_ITEM_RESERVA", "ROLE_ROOT" })
	public String editarItemReserva() {
		return "editarItemReserva";
	}

	// GETTERS E SETTERS

	/**
	 * @return the itensReserva
	 */
	public List<ItemReservaDTO> getItensReserva() {
		return itensReserva;
	}

	/**
	 * @param itensReserva
	 *            the itensReserva to set
	 */
	public void setItensReserva(List<ItemReservaDTO> itensReserva) {
		this.itensReserva = itensReserva;
	}

	/**
	 * @return the itemReserva
	 */
	public ItemReservaDTO getItemReserva() {
		return itemReserva;
	}

	/**
	 * @param itemReserva
	 *            the itemReserva to set
	 */
	public void setItemReserva(ItemReservaDTO itemReserva) {
		this.itemReserva = itemReserva;
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
