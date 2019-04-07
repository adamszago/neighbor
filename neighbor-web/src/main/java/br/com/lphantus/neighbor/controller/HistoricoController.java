package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.service.IHistoricoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
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
@ManagedBean(name = "historicoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class HistoricoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IHistoricoService historicoService;

	// historico
	private HistoricoDTO registroHistorico;
	private List<HistoricoDTO> historico;

	// Controle
	private Date minDate;
	private Date maxDate;

	/**
	 * Construtor padrao
	 */
	public HistoricoController() {

	}

	@PostConstruct
	public void initialize() {
		atualizarTela();
	}

	/**
	 * Metodo para início de cadastro. Este metodo e chamado no construtor do
	 * controller Tambem e responsavel por listar os registros existentes no
	 * banco e alimentar as listas
	 */
	private void atualizarTela() {
		this.historico = new ArrayList<HistoricoDTO>();
		this.registroHistorico = new HistoricoDTO();
		this.minDate = null;
		this.maxDate = null;
	}

	@Secured({ "ROLE_VER_HISTORICO", "ROLE_ROOT" })
	public void listarHistorico() {
		this.historico = new ArrayList<HistoricoDTO>();
		final UsuarioDTO userLogado = usuarioLogado();
		try {
			Long idCondominio;
			if (userLogado.isRoot()) {
				idCondominio = null;
			} else {
				idCondominio = condominioUsuarioLogado().getPessoa()
						.getIdPessoa();
			}
			historico = historicoService.findAllByIdCondominio(idCondominio);
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	private void filtrarHistoricoByData() {
		final List<HistoricoDTO> historicoFiltrado = new ArrayList<HistoricoDTO>();

		if ((this.maxDate != null) && (this.minDate != null)) {
			for (final HistoricoDTO regHist : this.historico) {
				if ((regHist.getDataHoraAcao().after(this.minDate) || (regHist
						.getDataHoraAcao().getDate() == this.minDate.getDate()))
						&&

						(regHist.getDataHoraAcao().before(this.maxDate) || (regHist
								.getDataHoraAcao().getDate() == this.maxDate
								.getDate()))) {

					historicoFiltrado.add(regHist);
				}
			}
			this.historico = historicoFiltrado;
		} else if (this.maxDate != null) {
			for (final HistoricoDTO regHist : this.historico) {
				if (regHist.getDataHoraAcao().before(this.maxDate)
						|| (regHist.getDataHoraAcao().getDate() == this.maxDate
								.getDate())) {

					historicoFiltrado.add(regHist);
				}
			}
			this.historico = historicoFiltrado;
		} else if (this.minDate != null) {
			for (final HistoricoDTO regHist : this.historico) {
				if (regHist.getDataHoraAcao().after(this.minDate)
						|| (regHist.getDataHoraAcao().getDate() == this.minDate
								.getDate())) {

					historicoFiltrado.add(regHist);
				}
			}
			this.historico = historicoFiltrado;
		}
	}

	public void filtrarHistoricoByDataInicial(final SelectEvent event) {
		listarHistorico();
		this.minDate = (Date) event.getObject();
		filtrarHistoricoByData();
	}

	public void filtrarHistoricoByDataFim(final SelectEvent event) {
		this.maxDate = (Date) event.getObject();
		filtrarHistoricoByData();
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_VER_HISTORICO", "ROLE_ROOT" })
	public String pageHistorico() {
		listarHistorico();
		return "/pages/administracao/historico.jsf";
	}

	// GETTERS E SETTERS

	/**
	 * @return the registroHistorico
	 */
	public HistoricoDTO getRegistroHistorico() {
		return registroHistorico;
	}

	/**
	 * @param registroHistorico
	 *            the registroHistorico to set
	 */
	public void setRegistroHistorico(HistoricoDTO registroHistorico) {
		this.registroHistorico = registroHistorico;
	}

	/**
	 * @return the historico
	 */
	public List<HistoricoDTO> getHistorico() {
		return historico;
	}

	/**
	 * @param historico
	 *            the historico to set
	 */
	public void setHistorico(List<HistoricoDTO> historico) {
		this.historico = historico;
	}

	/**
	 * @return the minDate
	 */
	public Date getMinDate() {
		return minDate;
	}

	/**
	 * @param minDate
	 *            the minDate to set
	 */
	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	/**
	 * @return the maxDate
	 */
	public Date getMaxDate() {
		return maxDate;
	}

	/**
	 * @param maxDate
	 *            the maxDate to set
	 */
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

}
