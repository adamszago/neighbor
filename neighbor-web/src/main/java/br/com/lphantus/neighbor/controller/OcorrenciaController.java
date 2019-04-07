package br.com.lphantus.neighbor.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import br.com.lphantus.neighbor.common.OcorrenciaDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IOcorrenciaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "ocorrenciaController")
@Transactional(propagation = Propagation.SUPPORTS)
public class OcorrenciaController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IOcorrenciaService serviceOcorrencia;

	@Autowired
	private IMoradorService serviceMorador;

	// Ocorrencia
	private List<OcorrenciaDTO> ocorrencias;
	private OcorrenciaDTO ocorrencia;
	private String horaOcorrencia;

	// Controle
	private Date dataInicial;
	private String casa;

	// Prestador Servico
	private boolean prestadorServico;

	// Visitante
	private boolean visitante;

	// Agregado
	private boolean agregado;
	private AgregadoDTO agregadoOcorrencia;

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor padrao
	 */
	public OcorrenciaController() {

	}

	@PostConstruct
	public void initialize() {
		atualizarTela();
	}

	private void atualizarTela() {
		this.casa = BigDecimal.ZERO.toString();
		this.ocorrencia = new OcorrenciaDTO();
		this.ocorrencias = new ArrayList<OcorrenciaDTO>();
		this.agregadoOcorrencia = new AgregadoDTO();
		this.horaOcorrencia = "";
		this.dataInicial = new Date();

		gerarListaOcorrencia();

	}

	private void gerarListaOcorrencia() {
		this.casa = BigDecimal.ZERO.toString();
		this.ocorrencia = new OcorrenciaDTO();
		this.ocorrencias = new ArrayList<OcorrenciaDTO>();
		this.agregadoOcorrencia = new AgregadoDTO();
		this.horaOcorrencia = "";
		this.dataInicial = new Date();
		final UsuarioDTO userLogado = usuarioLogado();
		try {
			if (userLogado != null) {
				CondominioDTO condominio;
				if (userLogado.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.ocorrencias = this.serviceOcorrencia
						.listarOcorrenciasPorCondominio(condominio);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de ocorrencias.", e);
		}
	}

	public String buscarResponsavel() {
		try {
			this.ocorrencia.setPessoa(this.serviceMorador.buscarPrincipal(
					this.casa, condominioUsuarioLogado()).getPessoa());
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(GerenciadorMensagem
					.getMensagem("ERROR_MESSAGE"));
		}
		return null;
	}

	@Secured({ "ROLE_CADASTRO_OCORRENCIA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravar() {
		final RequestContext context = RequestContext.getCurrentInstance();
		boolean resultOk = false;
		try {
			this.ocorrencia.setDataRegistroOcorrencia(new Date());
			final SimpleDateFormat formatData = new SimpleDateFormat(
					"dd/MM/yyyy");
			final String data = formatData.format(this.ocorrencia
					.getDataOcorrencia());

			// bug 14
			try {
				final SimpleDateFormat formatDatahora = new SimpleDateFormat(
						"dd/MM/yyyy HH:mm");
				final Date dataHora = formatDatahora.parse(data + " "
						+ this.horaOcorrencia);
				this.ocorrencia.setDataOcorrencia(dataHora);
				if (condominioUsuarioLogado() != null) {
					this.ocorrencia.setCondominio(condominioUsuarioLogado());
				}

				this.serviceOcorrencia.save(OcorrenciaDTO.Builder.getInstance()
						.createEntity(this.ocorrencia));

				registrarHistorico("Gravou Ocorrencia: - "
						+ this.ocorrencia.getDescricao());
				resultOk = true;
				context.addCallbackParam("resultOk", resultOk);
				atualizarTela();
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("SAVE_OK"));
			} catch (final ParseException e) {
				JsfUtil.addWarnMessage(GerenciadorMensagem
						.getMensagem("DATA_INVALIDA"));
			}

		} catch (final Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_CADASTRO_OCORRENCIA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String entregarOcorrencia() {
		String novoStatus = "";
		try {
			if (this.ocorrencia.isEntregue()) {
				this.ocorrencia.setEntregue(false);
				novoStatus = "INATIVO";
			} else {
				this.ocorrencia.setEntregue(true);
				novoStatus = "ATIVO";
			}
			this.serviceOcorrencia.update(OcorrenciaDTO.Builder.getInstance()
					.createEntity(this.ocorrencia));

			registrarHistorico("Alterou status de entrega da ocorrencia para:  "
					+ novoStatus
					+ ". Ocorrência: "
					+ this.ocorrencia.getDescricao());

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
		return null;
	}

	@Secured({ "ROLE_EDITAR_OCORRENCIA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterarOcorrencia(final ActionEvent event) {
		try {
			this.ocorrencia.setDataRegistroOcorrencia(new Date());
			final SimpleDateFormat formatData = new SimpleDateFormat(
					"dd/MM/yyyy");
			final String data = formatData.format(this.ocorrencia
					.getDataOcorrencia());

			final SimpleDateFormat formatDatahora = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm");
			final Date dataHora = formatDatahora.parse(data + " "
					+ this.horaOcorrencia);

			this.ocorrencia.setDataOcorrencia(dataHora);
			this.serviceOcorrencia.update(OcorrenciaDTO.Builder.getInstance()
					.createEntity(this.ocorrencia));

			registrarHistorico("Atualizou dados da Ocorrencia: "
					+ this.ocorrencia.getId() + " - "
					+ this.ocorrencia.getDescricao());

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));
		} catch (final Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_LISTA_OCORRENCIA", "ROLE_ROOT" })
	public String listarOcorrencias() {
		gerarListaOcorrencia();
		return "/pages/listagem/listaocorrencias.jsf";
	}

	@Secured({ "ROLE_CADASTRO_OCORRENCIA", "ROLE_ROOT" })
	public String novaOcorrencia() {
		atualizarTela();
		return "novaOcorrencia";
	}

	// GETTER AND SETTER

	public OcorrenciaDTO getOcorrencia() {
		return this.ocorrencia;
	}

	@SuppressWarnings("deprecation")
	public void setOcorrencia(final OcorrenciaDTO ocorrencia) {
		this.ocorrencia = ocorrencia;
		if (ocorrencia.getDataOcorrencia() != null) {
			this.horaOcorrencia = ""
					+ ocorrencia.getDataOcorrencia().getHours() + ":"
					+ ocorrencia.getDataOcorrencia().getMinutes();
		}
	}

	/**
	 * @return the ocorrencias
	 */
	public List<OcorrenciaDTO> getOcorrencias() {
		return ocorrencias;
	}

	/**
	 * @param ocorrencias
	 *            the ocorrencias to set
	 */
	public void setOcorrencias(List<OcorrenciaDTO> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	/**
	 * @return the horaOcorrencia
	 */
	public String getHoraOcorrencia() {
		return horaOcorrencia;
	}

	/**
	 * @param horaOcorrencia
	 *            the horaOcorrencia to set
	 */
	public void setHoraOcorrencia(String horaOcorrencia) {
		this.horaOcorrencia = horaOcorrencia;
	}

	/**
	 * @return the dataInicial
	 */
	public Date getDataInicial() {
		return dataInicial;
	}

	/**
	 * @param dataInicial
	 *            the dataInicial to set
	 */
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	/**
	 * @return the casa
	 */
	public String getCasa() {
		return casa;
	}

	/**
	 * @param casa
	 *            the casa to set
	 */
	public void setCasa(String casa) {
		this.casa = casa;
	}

	/**
	 * @return the prestadorServico
	 */
	public boolean isPrestadorServico() {
		return prestadorServico;
	}

	/**
	 * @param prestadorServico
	 *            the prestadorServico to set
	 */
	public void setPrestadorServico(boolean prestadorServico) {
		this.prestadorServico = prestadorServico;
	}

	/**
	 * @return the visitante
	 */
	public boolean isVisitante() {
		return visitante;
	}

	/**
	 * @param visitante
	 *            the visitante to set
	 */
	public void setVisitante(boolean visitante) {
		this.visitante = visitante;
	}

	/**
	 * @return the agregado
	 */
	public boolean isAgregado() {
		return agregado;
	}

	/**
	 * @param agregado
	 *            the agregado to set
	 */
	public void setAgregado(boolean agregado) {
		this.agregado = agregado;
	}

	/**
	 * @return the agregadoOcorrencia
	 */
	public AgregadoDTO getAgregadoOcorrencia() {
		return agregadoOcorrencia;
	}

	/**
	 * @param agregadoOcorrencia
	 *            the agregadoOcorrencia to set
	 */
	public void setAgregadoOcorrencia(AgregadoDTO agregadoOcorrencia) {
		this.agregadoOcorrencia = agregadoOcorrencia;
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
