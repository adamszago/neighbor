package br.com.lphantus.neighbor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.BoletaDTO;
import br.com.lphantus.neighbor.common.CarteiraDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.component.DuplicataParcelaDataModel;
import br.com.lphantus.neighbor.enums.StatusBoleta;
import br.com.lphantus.neighbor.service.IBoletoService;
import br.com.lphantus.neighbor.service.ICarteiraService;
import br.com.lphantus.neighbor.service.IDuplicataParcelaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;
import br.com.lphantus.neighbor.utils.IOUtils;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "boletoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class BoletoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IBoletoService boletoService;

	@Autowired
	private IDuplicataParcelaService duplicataParcelaService;

	@Autowired
	private ICarteiraService carteiraService;

	private List<BoletaDTO> entities;

	private BoletaDTO entity;
	private DuplicataDTO duplicataOrigem;
	private DuplicataDTO duplicataEntity;

	private List<CarteiraDTO> carteiras;
	private CarteiraDTO carteiraSelecionada;

	private DuplicataParcelaDTO parcelaBoleto;
	private final DuplicataParcelaDataModel parcelaModelo = new DuplicataParcelaDataModel();

	/**
	 * Construtor padrao
	 */
	public BoletoController() {
	}

	@PostConstruct
	public void initialize() {
		this.entity = new BoletaDTO();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void gerarBoleto() {
		try {
			this.entity.setDuplicata(this.duplicataOrigem);
			this.entity.setEmissor(this.duplicataOrigem.getCondominio());

			// converter de carteira pega somente o id, para recuperar as demais
			// informacoes tem de fazer a busca
			if ((null != this.carteiraSelecionada)
					&& (this.carteiraSelecionada.getId() > 0L)) {
				for (final CarteiraDTO carteira : this.carteiras) {
					if (carteira.equals(this.carteiraSelecionada)) {
						this.entity.setCarteira(carteira);
						break;
					}
				}
			}

			this.entity.setLocalParaPagamento(GerenciadorMensagem
					.getMensagem("BOL_LOCAL_PAGTO"));
			this.entity.setInstrucao1(GerenciadorMensagem
					.getMensagem("BOL_INSTRUC_1"));

			// somente para testes
			// this.entity.getEmissor().getPessoa().setCnpj("17.958.397/0001-21");

			this.entity.setDataProcessamento(this.duplicataOrigem
					.getDataCadastro());
			this.entity
					.setDataVencimento(this.parcelaBoleto.getDataPagamento());
			this.entity.setSacado(this.duplicataOrigem.getFatura()
					.getLancamentos().get(0).getPessoa());
			this.entity.setStatusBoleto(StatusBoleta.ABERTO);

			final File arquivo = this.boletoService.gerarBoleto(this.entity);

			// --------------------------------------------------------------

			final FacesContext fc = FacesContext.getCurrentInstance();
			final ExternalContext ec = fc.getExternalContext();

			final InputStream input = new FileInputStream(arquivo);
			final OutputStream output = ec.getResponseOutputStream();
			IOUtils.copy(input, output);

			fc.responseComplete();

			// --------------------------------------------------------------

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("SAVE_OK"));

		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar boletos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar boleto.", e);
		}

	}

	/**
	 * Invocado na listagem de duplicatas para geracao de boleto.
	 * 
	 * @return /pages/financeiro/gerarboleto.jsf
	 */
	public String paginaGerar() {
		try {
			this.gerarListaCarteiras();

			this.parcelaModelo.setWrappedData(this.duplicataParcelaService
					.buscarPorDuplicata(this.duplicataOrigem));

			this.entity = new BoletaDTO();

			return "/pages/financeiro/gerarboleto.jsf";
		} catch (final ServiceException e) {
			getLogger().debug("Erro ao gerar boleto.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().debug("Erro ao gerar boleto.", e);
		}
		return "";
	}
	
	/**
	 * Invocado na listagem de duplicatas para geracao de boleto.
	 * 
	 * @return /pages/financeiro/listarboleto.jsf
	 */
	public String paginaListar(){
		try {
			this.entities = boletoService.listarBoletos(usuarioLogado());

			return "/pages/financeiro/listaboleto.jsf";
		} catch (final ServiceException e) {
			getLogger().debug("Erro ao listar boletos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().debug("Erro ao listar boletos.", e);
		}
		return "";
	}

	private void gerarListaCarteiras() throws ServiceException {
		this.carteiras = this.carteiraService.buscarPorParametros(
				condominioUsuarioLogado(), Boolean.TRUE);
	}

	public void onRowSelect(final SelectEvent event) {
		final DuplicataParcelaDTO selecionada = (DuplicataParcelaDTO) event
				.getObject();
		this.entity.setDataDocumento(selecionada.getDataPagamento());
		// TODO complementar acao para selecao
	}

	public void onRowUnselect(final UnselectEvent event) {
		// TODO complementar acao para retirada de selecao
	}
	
	// GETTERS E SETTERS

	/**
	 * @return the carteiraSelecionada
	 */
	public CarteiraDTO getCarteiraSelecionada() {
		return this.carteiraSelecionada;
	}

	/**
	 * @param carteiraSelecionada
	 *            the carteiraSelecionada to set
	 */
	public void setCarteiraSelecionada(final CarteiraDTO carteiraSelecionada) {
		this.carteiraSelecionada = carteiraSelecionada;
	}

	/**
	 * @return the entities
	 */
	public List<BoletaDTO> getEntities() {
		return this.entities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(final List<BoletaDTO> entities) {
		this.entities = entities;
	}

	/**
	 * @return the entity
	 */
	public BoletaDTO getEntity() {
		return this.entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(final BoletaDTO entity) {
		this.entity = entity;
	}

	/**
	 * @return the duplicataOrigem
	 */
	public DuplicataDTO getDuplicataOrigem() {
		return this.duplicataOrigem;
	}

	/**
	 * @param duplicataOrigem
	 *            the duplicataOrigem to set
	 */
	public void setDuplicataOrigem(final DuplicataDTO duplicataOrigem) {
		this.duplicataOrigem = duplicataOrigem;
	}

	/**
	 * @return the duplicataEntity
	 */
	public DuplicataDTO getDuplicataEntity() {
		return this.duplicataEntity;
	}

	/**
	 * @param duplicataEntity
	 *            the duplicataEntity to set
	 */
	public void setDuplicataEntity(final DuplicataDTO duplicataEntity) {
		this.duplicataEntity = duplicataEntity;
	}

	/**
	 * @return the carteiras
	 */
	public List<CarteiraDTO> getCarteiras() {
		return this.carteiras;
	}

	/**
	 * @param carteiras
	 *            the carteiras to set
	 */
	public void setCarteiras(final List<CarteiraDTO> carteiras) {
		this.carteiras = carteiras;
	}

	/**
	 * @return the parcelaBoleto
	 */
	public DuplicataParcelaDTO getParcelaBoleto() {
		return this.parcelaBoleto;
	}

	/**
	 * @param parcelaBoleto
	 *            the parcelaBoleto to set
	 */
	public void setParcelaBoleto(final DuplicataParcelaDTO parcelaBoleto) {
		this.parcelaBoleto = parcelaBoleto;
	}

	/**
	 * @return the parcelaModelo
	 */
	public DuplicataParcelaDataModel getParcelaModelo() {
		return this.parcelaModelo;
	}

}