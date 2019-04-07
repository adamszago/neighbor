package br.com.lphantus.neighbor.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.ItemReservaDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ReservaDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.service.IConfiguracaoCondominioService;
import br.com.lphantus.neighbor.service.IMailManager;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IReservaService;
import br.com.lphantus.neighbor.service.IUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.ItemReservaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;
import br.com.lphantus.neighbor.utils.Utilitarios;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "reservaController")
@Transactional(propagation = Propagation.SUPPORTS)
public class ReservaController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Service
	@Autowired
	private IReservaService serviceReserva;

	@Autowired
	private ItemReservaService itemService;

	@Autowired
	private IMoradorService serviceMorador;

	@Autowired
	private IUsuarioService serviceUsuario;

	@Autowired
	private IUnidadeHabitacionalService serviceUnidade;

	@Autowired
	private IConfiguracaoCondominioService serviceConfiguracaoCondominio;

	@Autowired
	private IMailManager serviceMailManager;

	// Reserva
	private ReservaDTO reserva;
	private List<ReservaDTO> reservas;

	// Item Reserva
	private String itemReservaSelecionado;
	private List<ItemReservaDTO> listaItemReserva;
	private List<SelectItem> listaSelectItem;

	// Controle
	private String casa;
	private Date dataInicial;
	private boolean solicitarConfirmacao;
	private boolean confirmado;

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor padrao
	 */
	public ReservaController() {

	}

	@PostConstruct
	@Transactional(propagation = Propagation.REQUIRED)
	public void initialize() {
		this.atualizarTela();
	}

	/**
	 * Atualiza todos os componentes da view
	 * 
	 * @throws ServiceException
	 */
	private void atualizarTela() {
		this.reserva = new ReservaDTO();
		this.reservas = new ArrayList<ReservaDTO>();
		this.listaItemReserva = new ArrayList<ItemReservaDTO>();
		this.dataInicial = new Date();
		this.casa = BigDecimal.ZERO.toString();
		this.solicitarConfirmacao = false;
		this.confirmado = false;
		
		this.gerarListaItemReserva();
		this.gerarListaReserva();
		this.carregarItensReserva();
	}

	private void gerarListaReserva() {

		this.reserva = new ReservaDTO();
		this.reservas = new ArrayList<ReservaDTO>();
		this.dataInicial = new Date();
		this.casa = BigDecimal.ZERO.toString();
		final UsuarioDTO userLogado = usuarioLogado();

		try {
			if (userLogado != null) {

				final MoradorDTO morador = this.serviceMorador.buscarMoradorUsuario(userLogado);
				final ConfiguracaoCondominioDTO configuracao = serviceConfiguracaoCondominio.buscarPorCondominio(condominioUsuarioLogado());

				if (userLogado.isRoot()) {
					this.reservas = this.serviceReserva.listarReservaPorCondominio(null);
				} else {
					if (configuracao.isExibeTodasReservas()) {
						this.reservas = this.serviceReserva.listarReservaPorCondominio(condominioUsuarioLogado());
					} else {
						this.reservas = this.serviceReserva.listarReservaPorMorador(morador, condominioUsuarioLogado());
					}
				}
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de reservas.", e);
		}
	}

	private void gerarListaItemReserva() {

		this.listaItemReserva = new ArrayList<ItemReservaDTO>();
		this.dataInicial = new Date();
		this.casa = BigDecimal.ZERO.toString();
		final UsuarioDTO userLogado = usuarioLogado();

		try {

			if (userLogado != null) {
				CondominioDTO condominio;
				Boolean status;
				if (usuarioLogado().isRoot()) {
					condominio = null;
					status = null;
				} else {
					condominio = condominioUsuarioLogado();
					status = Boolean.TRUE;
				}
				this.listaItemReserva = this.itemService.listarPorCondominio(condominio, status);
			}

		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de itens de reservas.", e);
		}
	}

	public void buscarMorador() {
		try {
			final MoradorDTO moradorTemp = this.serviceMorador.buscarPrincipalReserva(this.casa, condominioUsuarioLogado());

			final UsuarioDTO usuarioMorador = this.serviceUsuario.buscaUsuarioMorador(moradorTemp.getPessoa().getIdPessoa());

			final CondominioDTO condominio = usuarioMorador.getCondominio();

			if ((moradorTemp != null) && (condominio != null)) {
				if (condominio.getPessoa().getIdPessoa().equals(condominioUsuarioLogado().getPessoa().getIdPessoa())) {
					if ( null == this.reserva ){
						atualizarTela();
					}else{
						this.reserva.setMorador(moradorTemp);
					}
				} else {
					JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("CASA_INVALIDA"));
				}
			} else {
				JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("CASA_INVALIDA"));
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

	}

	public void limparReserva(final ActionEvent e) {
	}

	@Secured({ "ROLE_EXCLUIR_RESERVA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluir(final ActionEvent event) {
		try {

			final UnidadeHabitacionalDTO unidade = this.serviceUnidade.buscarUnidadeHabitacionalMorador(this.reserva.getMorador());

			// precisa copiar os campos antes de deletar
			String nome = reserva.getItemReserva().getNome();
			Date data = reserva.getDataReserva();
			String nroUnidade = unidade.getIdentificacao();
			String nomeMorador = reserva.getMorador().getPessoa().getNome();
			Date dataSolicitacao = reserva.getDataSolicitacao();

			// removendo as entidade atachadas
			this.reserva.setItemReserva(null);
			this.reserva.setMorador(null);
			this.serviceReserva.delete(ReservaDTO.Builder.getInstance().createEntity(this.reserva));

			// registra historico com os campos previamente salvos
			registrarHistorico("Reserva Excluida - ITEM: " + nome + " - DATA RESERVA: " + data + " | MORADOR: casa: " + nroUnidade + " - " + nomeMorador + " | DATA CADASTRO: "
					+ dataSolicitacao);

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("DELETE_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
		atualizarTela();
		// return "Excluido";
	}

	public void limpar() {
		final FacesContext facesContext = FacesContext.getCurrentInstance();

		final UIInput dropdown = (UIInput) facesContext.getViewRoot().findComponent("cadastroReservaForm:itensReserva");
		dropdown.setValue("-1");

		this.reserva = new ReservaDTO();
	}

	public void carregarItensReserva() {
		this.listaSelectItem = new ArrayList<SelectItem>();
		for (final ItemReservaDTO itemReserva : this.listaItemReserva) {
			final SelectItem item = new SelectItem(itemReserva.getId(), itemReserva.getNome());
			this.listaSelectItem.add(item);
		}
	}

	public String limparFormulario() {
		this.atualizarTela();
		return "sucesso";
	}

	public void valueChangeListener(final ValueChangeEvent event) {
		if (event.getNewValue().toString().equalsIgnoreCase("-1")) {
			return;
		} else {
			this.itemReservaSelecionado = event.getNewValue().toString();
		}
	}

	@Secured({ "ROLE_PAGAR_RESERVA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String pagarReserva() {
		if (!this.reserva.isPago()) {
			this.reserva.setPago(true);
		}
		try {
			this.serviceReserva.update(ReservaDTO.Builder.getInstance().createEntity(this.reserva));

			final UnidadeHabitacionalDTO unidade = this.serviceUnidade.buscarUnidadeHabitacionalMorador(this.reserva.getMorador());

			registrarHistorico("Reserva paga - ID RESERVA: " + this.reserva.getId() + " - ITEM: " + this.reserva.getItemReserva().getNome() + " - Data Reserva: "
					+ this.reserva.getDataReserva() + " - Morador: Casa: " + unidade.getIdentificacao() + " - Nome: " + this.reserva.getMorador().getPessoa().getNome());

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("RESERVA_PAGA"));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao realizar pagamento de reserva.", e);
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
		}
		this.atualizarTela();
		return "reservaPaga";
	}

	/*
	 * ultima alteracao em 14/04/2012 Adams - inclusao synchronized e comentario
	 * verificaDisponibilidade
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String confirmarReserva() {
		/*
		 * se o usuario logado for Morador ele sera o morador que esta
		 * solicitando a reserva, caso contrario sera o morador selecionado pelo
		 * usuario
		 */
		try {
			final UsuarioDTO userLogado = usuarioLogado();
			final MoradorDTO morador = this.serviceMorador.buscarMoradorUsuario(userLogado);

			if (morador != null) {
				this.reserva.setMorador(morador);
			}
			efetuarCadastroReserva();
		} catch (final ServiceException serviceException) {
			getLogger().error("Erro ao confirmar reserva.", serviceException);
		}

		return null;

	}

	private void efetuarCadastroReserva() {
		try {
			if ((this.itemReservaSelecionado == null) || this.itemReservaSelecionado.isEmpty()) {
				JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("SELECIONE_ITEM_RESERVA"));
			} else {

				final List<ItemReservaDTO> lista = this.itemService.listarPorCondominio(condominioUsuarioLogado(), Boolean.TRUE);
				final Long valor = Long.valueOf(this.itemReservaSelecionado);
				for (final ItemReservaDTO item : lista) {
					if (valor.equals(item.getId())) {
						this.reserva.setItemReserva(item);
						break;
					}
				}

				this.reserva.setDataSolicitacao(new Date());
				final UsuarioDTO userLogado = usuarioLogado();
				if (condominioUsuarioLogado() != null) {
					this.reserva.setCondominio(condominioUsuarioLogado());
				}

				final MoradorDTO morador = this.serviceMorador.buscarMoradorUsuario(userLogado);

				synchronized (this.reserva) {
					if (morador != null) {
						this.solicitarConfirmacao = false;

						if (this.serviceReserva.itemReservaEstaPendenteAprovacaoNaData(this.reserva)) {
							this.solicitarConfirmacao = true;
						} else {
							this.confirmado = true;
						}

						if (this.confirmado) {
							this.solicitarConfirmacao = false;
							this.confirmado = false;

							// aprovacao automatica: caso o item nao precise de
							// pagamento
							this.serviceReserva.save(this.reserva, this.reserva.getItemReserva().isNecessitaAprovar());
						}

					} else {
						this.serviceReserva.save(this.reserva, false);
					}

					final UnidadeHabitacionalDTO unidade = this.serviceUnidade.buscarUnidadeHabitacionalMorador(this.reserva.getMorador());

					if (!this.solicitarConfirmacao) {
						registrarHistorico(String.format("Reserva efetuada. Item: %s | Data reserva: %s | Casa: %s | Nome: %s", this.reserva.getItemReserva().getNome(),
								Utilitarios.formatarDia(this.reserva.getDataReserva()), unidade.getIdentificacao(), this.reserva.getMorador().getPessoa().getNome()));

						final ReservaDTO reservaPedida = this.reserva;
						final UnidadeHabitacionalDTO unidadePedida = unidade;
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									
									try {
										TimeUnit.SECONDS.sleep(2);
									} catch (InterruptedException e) {
									}
									
									// aprovacao automatica
									if (!reservaPedida.getItemReserva().isNecessitaAprovar()) {
										registrarHistorico(String.format("Aprovou reserva. Item: %s | Data reserva: %s | Casa: %s | Nome: %s", 
												reservaPedida.getItemReserva().getNome(), Utilitarios.formatarDia(reservaPedida.getDataReserva()), 
												unidadePedida.getIdentificacao(), reservaPedida.getMorador().getPessoa().getNome()), null);

										serviceMailManager.enviarEmailReservaAprovada(reservaPedida, unidadePedida);
									}
								} catch (ServiceException  e) {
									getLogger().error("Erro ao registrar historico de reserva pedida.", e);
								}
							}
						}).start();
					}

				}

				if (!this.solicitarConfirmacao) {
					if (morador != null) {
						if (this.reserva.getItemReserva().isNecessitaPagamento()) {
							JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK") + "\n" + GerenciadorMensagem.getMensagem("AGUARDANDO_APROVACAO"));
						} else {
							JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK") + "\n");
						}
					} else {
						JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));
					}

					this.atualizarTela();
				}

			}

		} catch (final ServiceException e) {
			getLogger().error("Erro ao efetuar cadastro de reserva.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void confirmadoTrue() {
		this.confirmado = true;
		efetuarCadastroReserva();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void confirmadoFalse() {
		this.confirmado = false;
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_CADASTRO_RESERVA", "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String novaReserva() {
		this.atualizarTela();
		return "/pages/cadastros/reserva.jsf";
	}

	@Secured({ "ROLE_EDITAR_RESERVA", "ROLE_ROOT" })
	public String editarReserva() {
		return "editarReserva";
	}

	@Secured({ "ROLE_LISTA_RESERVA", "ROLE_ROOT" })
	public String listarReserva() {
		gerarListaReserva();
		return "/pages/listagem/listareserva.jsf";
	}

	// GETTER AND SETTER

	/**
	 * @return the reserva
	 */
	public ReservaDTO getReserva() {
		return this.reserva;
	}

	/**
	 * @param reserva
	 *            the reserva to set
	 */
	public void setReserva(final ReservaDTO reserva) {
		this.reserva = reserva;
	}

	/**
	 * @return the listaItemReserva
	 */
	public List<ItemReservaDTO> getListaItemReserva() {
		return this.listaItemReserva;
	}

	/**
	 * @param listaItemReserva
	 *            the listaItemReserva to set
	 */
	public void setListaItemReserva(final List<ItemReservaDTO> listaItemReserva) {
		this.listaItemReserva = listaItemReserva;
	}

	/**
	 * @return the reservas
	 */
	public List<ReservaDTO> getReservas() {
		return this.reservas;
	}

	/**
	 * @param reservas
	 *            the reservas to set
	 */
	public void setReservas(final List<ReservaDTO> reservas) {
		this.reservas = reservas;
	}

	/**
	 * @return the itemReservaSelecionado
	 */
	public String getItemReservaSelecionado() {
		return this.itemReservaSelecionado;
	}

	/**
	 * @param itemReservaSelecionado
	 *            the itemReservaSelecionado to set
	 */
	public void setItemReservaSelecionado(final String itemReservaSelecionado) {
		this.itemReservaSelecionado = itemReservaSelecionado;
	}

	/**
	 * @return the listaSelectItem
	 */
	public List<SelectItem> getListaSelectItem() {
		return this.listaSelectItem;
	}

	/**
	 * @param listaSelectItem
	 *            the listaSelectItem to set
	 */
	public void setListaSelectItem(final List<SelectItem> listaSelectItem) {
		this.listaSelectItem = listaSelectItem;
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
	 * @return the dataInicial
	 */
	public Date getDataInicial() {
		return this.dataInicial;
	}

	/**
	 * @param dataInicial
	 *            the dataInicial to set
	 */
	public void setDataInicial(final Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	/**
	 * @return the confirmado
	 */
	public boolean isConfirmado() {
		return this.confirmado;
	}

	/**
	 * @param confirmado
	 *            the confirmado to set
	 */
	public void setConfirmado(final boolean confirmado) {
		this.confirmado = confirmado;
	}

	/**
	 * @return the solicitarConfirmacao
	 */
	public boolean isSolicitarConfirmacao() {
		return this.solicitarConfirmacao;
	}

	/**
	 * @param solicitarConfirmacao
	 *            the solicitarConfirmacao to set
	 */
	public void setSolicitarConfirmacao(final boolean solicitarConfirmacao) {
		this.solicitarConfirmacao = solicitarConfirmacao;
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

}
