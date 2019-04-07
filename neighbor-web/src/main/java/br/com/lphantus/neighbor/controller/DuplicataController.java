package br.com.lphantus.neighbor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CarteiraDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.component.DuplicataParcelaDataModel;
import br.com.lphantus.neighbor.component.FaturaDataModel;
import br.com.lphantus.neighbor.component.PessoaFisicaDataModel;
import br.com.lphantus.neighbor.entity.Duplicata;
import br.com.lphantus.neighbor.entity.DuplicataParcela;
import br.com.lphantus.neighbor.enums.TipoParcelamento;
import br.com.lphantus.neighbor.service.IBoletoService;
import br.com.lphantus.neighbor.service.ICarteiraService;
import br.com.lphantus.neighbor.service.IDuplicataParcelaService;
import br.com.lphantus.neighbor.service.IDuplicataService;
import br.com.lphantus.neighbor.service.IFaturaService;
import br.com.lphantus.neighbor.service.IPessoaFisicaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;
import br.com.lphantus.neighbor.utils.IOUtils;

/**
 * @author Roque Bridi Jr
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "duplicataController")
@Transactional(propagation = Propagation.SUPPORTS)
public class DuplicataController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IDuplicataService duplicataService;

	@Autowired
	private IPessoaFisicaService pessoaFisicaService;

	@Autowired
	private IFaturaService faturaService;

	@Autowired
	private IDuplicataParcelaService duplicataParcelaService;

	@Autowired
	private ICarteiraService carteiraService;

	@Autowired
	private IBoletoService boletoService;

	private List<DuplicataDTO> entities;
	private DuplicataDTO entity;

	private PessoaFisicaDTO pessoaSelecionada;
	private FaturaDTO faturasSelecionadas;

	private List<PessoaFisicaDTO> pessoasLista = new ArrayList<PessoaFisicaDTO>();
	private final PessoaFisicaDataModel pessoas = new PessoaFisicaDataModel();
	private final FaturaDataModel faturas = new FaturaDataModel();
	private final DuplicataParcelaDataModel parcelaModelo = new DuplicataParcelaDataModel();

	private String tipoParcelamento;
	private Integer numeroParcelas;
	private Integer limiteDias;
	private Date dataPrimeiraParcela;

	private BigDecimal moraOutrosRecebimentos;
	private BigDecimal multaVencimento;
	private BigDecimal taxaDia;
	private BigDecimal desconto;
	private BigDecimal abatimento;

	private DuplicataDTO itemSelecionado;
	private CarteiraDTO carteiraBaixa, carteiraBoleto;
	private List<CarteiraDTO> carteiras;

	/**
	 * Construtor padrao
	 */
	public DuplicataController() {

	}

	public void carregarListaDuplicata() {
		try {
			CondominioDTO condominio;
			if (usuarioLogado().isRoot()) {
				condominio = null;
			} else {
				condominio = condominioUsuarioLogado();
			}

			this.entities = this.duplicataService
					.buscarPorCondominio(condominio);
		} catch (final ServiceException e) {
			getLogger().error("Erro ao carregar lista de duplicatas.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private void carregarListaMoradores() {
		try {
			if (null == this.pessoasLista) {
				this.pessoasLista = new ArrayList<PessoaFisicaDTO>();
			}

			CondominioDTO condominio;
			if (usuarioLogado().isRoot()) {
				condominio = null;
			} else {
				condominio = condominioUsuarioLogado();
			}

			this.pessoasLista.clear();
			this.pessoasLista.addAll(this.pessoaFisicaService
					.buscarPessoasFaturaAberto(condominio));

			this.pessoas.setWrappedData(this.pessoasLista);
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar responsaveis financeiros.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao buscar responsaveis financeiros.", e);
		}
	}

	public void limparObjetoDuplicata() {
		this.entity = new DuplicataDTO();
		this.faturas.setWrappedData(Arrays.asList(new FaturaDTO[] {}));
		this.faturasSelecionadas = null;
		this.pessoaSelecionada = null;
		this.itemSelecionado = null;
	}

	private void atualizaFaturas() {
		final List<FaturaDTO> lista = new ArrayList<FaturaDTO>();

		try {
			lista.addAll(this.faturaService
					.buscarEmAbertoPorPessoa(this.pessoaSelecionada));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar lancamentos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		this.faturas.setWrappedData(lista);
	}

	public void itemSelecionado(final ValueChangeEvent event) {
		this.itemSelecionado = JsfUtil.avaliarExpressao("#{duplicata}",
				DuplicataDTO.class);
	}

	public void onRowSelect(final SelectEvent event) {
		final PessoaFisicaDTO selecionada = (PessoaFisicaDTO) event.getObject();
		this.pessoaSelecionada = selecionada;

		atualizaFaturas();
	}

	public void onRowUnselect(final UnselectEvent event) {
		this.pessoaSelecionada = null;
	}

	public void onRowSelectFatura(final SelectEvent event) {
		final FaturaDTO fatura = (FaturaDTO) event.getObject();
		this.entity.setFatura(fatura);
		this.dataPrimeiraParcela = fatura.getData();
	}

	public void onRowUnselectFatura(final UnselectEvent event) {
		this.entity.setFatura(null);
	}

	public void onChangeTipoParcelamento() {
		if (this.tipoParcelamento.equalsIgnoreCase(TipoParcelamento.A_VISTA
				.getTipo())) {
			this.numeroParcelas = 1;
		}
	}

	@SuppressWarnings("unchecked")
	@Secured({ "ROLE_ADICIONAR_DUPLICATA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String gravarDuplicataReceber() {
		try {

			this.entity.setCondominio(condominioUsuarioLogado());
			this.entity
					.setParcelas((List<DuplicataParcelaDTO>) this.parcelaModelo
							.getWrappedData());

			this.entity.setCondominio(condominioUsuarioLogado());

			final Duplicata entidade = DuplicataDTO.Builder.getInstance()
					.createEntity(this.entity);
			this.duplicataService.save(entidade);

			final BigDecimal valor = this.entity.getFatura().getValor()
					.divide(new BigDecimal(this.entity.getParcelas().size()));

			for (final DuplicataParcelaDTO parcela : this.entity.getParcelas()) {
				final DuplicataParcela parcelaEntidade = DuplicataParcelaDTO.Builder
						.getInstance().createEntity(parcela);
				parcelaEntidade.setDuplicata(entidade);
				parcelaEntidade.setValor(valor);
				this.duplicataParcelaService.save(parcelaEntidade);
			}

			limparObjetoDuplicata();
			carregarListaDuplicata();

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("SAVE_OK"));

			return "/pages/financeiro/listaduplicata.xhtml";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar duplicata.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "";
	}

	@Secured({ "ROLE_EDITAR_DUPLICATA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterar() {
		try {

			final Duplicata entidade = DuplicataDTO.Builder.getInstance()
					.createEntity(this.entity);
			this.duplicataService.save(entidade);

			// TODO: revisar (possibilidade de regerar parcelas)

			final BigDecimal valor = this.entity.getFatura().getValor()
					.divide(new BigDecimal(this.entity.getParcelas().size()));

			for (final DuplicataParcelaDTO parcela : this.entity.getParcelas()) {
				final DuplicataParcela parcelaEntidade = DuplicataParcelaDTO.Builder
						.getInstance().createEntity(parcela);
				parcelaEntidade.setDuplicata(entidade);
				parcelaEntidade.setValor(valor);
				this.duplicataParcelaService.save(parcelaEntidade);
			}

			limparObjetoDuplicata();
			carregarListaDuplicata();

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

			return "/pages/financeiro/listaduplicata.xhtml";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar duplicata.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "";
	}

	public void gerarParcelas() {

		if (null == this.dataPrimeiraParcela) {
			JsfUtil.addErrorMessage(GerenciadorMensagem
					.getMensagem("SELEC_PRI_PARC"));
		} else {

			zerarCamposNulos();

			final List<DuplicataParcelaDTO> adicionar = new ArrayList<DuplicataParcelaDTO>();
			for (int i = 0; i < this.numeroParcelas; ++i) {
				final DuplicataParcelaDTO dto = new DuplicataParcelaDTO();
				dto.setAbatimento(this.abatimento);
				dto.setDataLimiteVencto(calculaVencimento(i + 1));
				dto.setDataPagamento(calculaPagamento(i + 1));
				dto.setDesconto(this.desconto);
				// dto.setDuplicata(duplicata);
				// dto.setIdDuplicata(idDuplicata);
				dto.setMoraOutrosRecebimentos(this.moraOutrosRecebimentos);
				dto.setMultaVencimento(this.multaVencimento);
				dto.setNumeroParcela(new Long(i + 1));
				dto.setTaxaDia(this.taxaDia);
				adicionar.add(dto);
			}

			this.parcelaModelo.setWrappedData(adicionar);
			this.entity.setParcelas(adicionar);
		}
	}

	private void validaItemSelecionado() throws ServiceException {
		if (null == this.itemSelecionado) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("SELECIONE_DUPLICATA"));
		}
	}

	public String gerarBoleto() {
		String retorno = null;
		try {
			validaItemSelecionado();

			final BoletoController boletoController = JsfUtil.avaliarExpressao(
					"#{boletoController}", BoletoController.class);

			boletoController.setDuplicataOrigem(this.itemSelecionado);

			retorno = boletoController.paginaGerar();
			getLogger().debug("Gerar boleto.");
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar boleto da duplicata.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return retorno;
	}

	public String baixa() {
		String retorno = null;
		try {
			validaItemSelecionado();

			final MovimentacaoController movimentacaoController = JsfUtil
					.avaliarExpressao("#{movimentacaoController}",
							MovimentacaoController.class);

			movimentacaoController.setDuplicataOrigem(this.itemSelecionado);

			retorno = movimentacaoController.paginaBaixa();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao dar baixa da duplicata.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao dar baixa da duplicata.", e);
		}
		return retorno;
	}

	private void zerarCamposNulos() {
		if (null == this.abatimento) {
			this.abatimento = BigDecimal.ZERO;
		}

		if (null == this.desconto) {
			this.desconto = BigDecimal.ZERO;
		}

		if (null == this.moraOutrosRecebimentos) {
			this.moraOutrosRecebimentos = BigDecimal.ZERO;
		}

		if (null == this.multaVencimento) {
			this.multaVencimento = BigDecimal.ZERO;
		}

		if (null == this.taxaDia) {
			this.taxaDia = BigDecimal.ZERO;
		}
	}

	private Date calculaPagamento(final int numeroParcela) {
		Date retorno;
		if (numeroParcela == 1) {
			retorno = this.dataPrimeiraParcela;
		} else {
			final Calendar calendario = new GregorianCalendar();
			calendario.setTime(this.dataPrimeiraParcela);
			calendario.add(Calendar.MONTH, 1);
			retorno = calendario.getTime();
		}
		return retorno;
	}

	private Date calculaVencimento(final int numeroParcela) {
		final Calendar calendario = new GregorianCalendar();
		final Date pagamento = calculaPagamento(numeroParcela);

		calendario.setTime(pagamento);
		calendario.add(Calendar.DAY_OF_MONTH, this.limiteDias);
		return calendario.getTime();
	}

	public void limparParcelas() {
		final List<DuplicataParcelaDTO> lista = new ArrayList<DuplicataParcelaDTO>();
		this.parcelaModelo.setWrappedData(lista);
		this.entity.setParcelas(lista);
	}

	private void gerarListaCarteiras() throws ServiceException {
		this.carteiras = this.carteiraService.buscarPorParametros(
				condominioUsuarioLogado(), Boolean.TRUE);
	}

	@Secured({ "ROLE_ADICIONAR_DUPLICATA", "ROLE_ROOT" })
	public String pageCadastroDuplicata() {
		limparObjetoDuplicata();
		carregarListaMoradores();
		return "/pages/financeiro/cadduplicata.jsf";
	}

	@Secured({ "ROLE_LISTAR_DUPLICATAS", "ROLE_ROOT" })
	public String pageListaDuplicata() {
		try {
			limparObjetoDuplicata();
			carregarListaDuplicata();
			gerarListaCarteiras();
		} catch (final ServiceException e) {
			getLogger().debug("Erro ao listar duplicatas.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().debug("Erro ao listar duplicatas.", e);
		}

		return "/pages/financeiro/listaduplicata.jsf";
	}

	@Secured({ "ROLE_EDITAR_DUPLICATA", "ROLE_ROOT" })
	public String editarDuplicata() {

		preencheCampos();

		return "/pages/financeiro/cadduplicata.jsf";
	}

	private void preencheCampos() {
		final DuplicataParcelaDTO parcelaZero = this.entity.getParcelas()
				.get(0);

		this.abatimento = parcelaZero.getAbatimento();
		this.dataPrimeiraParcela = parcelaZero.getDataPagamento();
		this.desconto = parcelaZero.getDesconto();

		// TODO: revisar
		// this.limiteDias = parcelaZero.get

		this.moraOutrosRecebimentos = parcelaZero.getMoraOutrosRecebimentos();
		this.multaVencimento = parcelaZero.getMultaVencimento();
		this.numeroParcelas = this.entity.getParcelas().size();

		this.taxaDia = parcelaZero.getTaxaDia();

		// TODO: REVISAR
		// this.tipoParcelamento =

		this.parcelaModelo.setWrappedData(this.entity.getParcelas());
		this.faturas.setWrappedData(Arrays.asList(new FaturaDTO[] { this.entity
				.getFatura() }));
		this.pessoas.setWrappedData(Arrays.asList(new PessoaDTO[] { this.entity
				.getFatura().getLancamentos().get(0).getPessoa() }));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void gerarTodosBoletos() {

		try {

			final CondominioDTO condominio = condominioUsuarioLogado();

			final File arquivo = this.boletoService.gerarBoleto(condominio,
					this.carteiraBoleto);

			// --------------------------------------------------------------

			final FacesContext fc = FacesContext.getCurrentInstance();
			final ExternalContext ec = fc.getExternalContext();

			final InputStream input = new FileInputStream(arquivo);
			final OutputStream output = ec.getResponseOutputStream();
			IOUtils.copy(input, output);

			fc.responseComplete();

			// --------------------------------------------------------------

			limparObjetoDuplicata();
			carregarListaDuplicata();
			gerarListaCarteiras();

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

	public boolean isExistemNaoAssociados() {
		boolean retorno = false;
		try {
			CondominioDTO condominio;
			if (usuarioLogado().isRoot()) {
				condominio = null;
			} else {
				condominio = condominioUsuarioLogado();
			}
			final List<FaturaDTO> faturas = this.faturaService
					.buscarFaturasSemDuplicata(condominio);
			retorno = !faturas.isEmpty();
		} catch (final ServiceException e) {
			getLogger().debug(
					"Erro ao listar faturas sem duplicatas associadas.", e);
		} catch (final Exception e) {
			getLogger().debug(
					"Erro ao listar faturas sem duplicatas associadas.", e);
		}
		return retorno;
	}

	/**
	 * @return the entities
	 */
	public List<DuplicataDTO> getEntities() {
		return this.entities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(final List<DuplicataDTO> entities) {
		this.entities = entities;
	}

	/**
	 * @return the entity
	 */
	public DuplicataDTO getEntity() {
		return this.entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(final DuplicataDTO entity) {
		this.entity = entity;
	}

	/**
	 * @return the pessoas
	 */
	public PessoaFisicaDataModel getPessoas() {
		return this.pessoas;
	}

	/**
	 * @return the faturas
	 */
	public FaturaDataModel getFaturas() {
		return this.faturas;
	}

	/**
	 * @return the parcelaModelo
	 */
	public DuplicataParcelaDataModel getParcelaModelo() {
		return this.parcelaModelo;
	}

	/**
	 * @return the pessoaSelecionada
	 */
	public PessoaFisicaDTO getPessoaSelecionada() {
		return this.pessoaSelecionada;
	}

	/**
	 * @param pessoaSelecionada
	 *            the pessoaSelecionada to set
	 */
	public void setPessoaSelecionada(final PessoaFisicaDTO pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}

	/**
	 * @return the faturasSelecionadas
	 */
	public FaturaDTO getFaturasSelecionadas() {
		return this.faturasSelecionadas;
	}

	/**
	 * @param faturasSelecionadas
	 *            the faturasSelecionadas to set
	 */
	public void setFaturasSelecionadas(final FaturaDTO faturasSelecionadas) {
		this.faturasSelecionadas = faturasSelecionadas;
	}

	/**
	 * @return the tipoParcelamento
	 */
	public String getTipoParcelamento() {
		return this.tipoParcelamento;
	}

	/**
	 * @param tipoParcelamento
	 *            the tipoParcelamento to set
	 */
	public void setTipoParcelamento(final String tipoParcelamento) {
		this.tipoParcelamento = tipoParcelamento;
	}

	/**
	 * @return the numeroParcelas
	 */
	public Integer getNumeroParcelas() {
		return this.numeroParcelas;
	}

	/**
	 * @param numeroParcelas
	 *            the numeroParcelas to set
	 */
	public void setNumeroParcelas(final Integer numeroParcelas) {
		this.numeroParcelas = numeroParcelas;
	}

	/**
	 * @return the dataPrimeiraParcela
	 */
	public Date getDataPrimeiraParcela() {
		return this.dataPrimeiraParcela;
	}

	/**
	 * @param dataPrimeiraParcela
	 *            the dataPrimeiraParcela to set
	 */
	public void setDataPrimeiraParcela(final Date dataPrimeiraParcela) {
		this.dataPrimeiraParcela = dataPrimeiraParcela;
	}

	/**
	 * @return the limiteDias
	 */
	public Integer getLimiteDias() {
		return this.limiteDias;
	}

	/**
	 * @param limiteDias
	 *            the limiteDias to set
	 */
	public void setLimiteDias(final Integer limiteDias) {
		this.limiteDias = limiteDias;
	}

	/**
	 * @return the moraOutrosRecebimentos
	 */
	public BigDecimal getMoraOutrosRecebimentos() {
		return this.moraOutrosRecebimentos;
	}

	/**
	 * @param moraOutrosRecebimentos
	 *            the moraOutrosRecebimentos to set
	 */
	public void setMoraOutrosRecebimentos(
			final BigDecimal moraOutrosRecebimentos) {
		this.moraOutrosRecebimentos = moraOutrosRecebimentos;
	}

	/**
	 * @return the multaVencimento
	 */
	public BigDecimal getMultaVencimento() {
		return this.multaVencimento;
	}

	/**
	 * @param multaVencimento
	 *            the multaVencimento to set
	 */
	public void setMultaVencimento(final BigDecimal multaVencimento) {
		this.multaVencimento = multaVencimento;
	}

	/**
	 * @return the taxaDia
	 */
	public BigDecimal getTaxaDia() {
		return this.taxaDia;
	}

	/**
	 * @param taxaDia
	 *            the taxaDia to set
	 */
	public void setTaxaDia(final BigDecimal taxaDia) {
		this.taxaDia = taxaDia;
	}

	/**
	 * @return the desconto
	 */
	public BigDecimal getDesconto() {
		return this.desconto;
	}

	/**
	 * @param desconto
	 *            the desconto to set
	 */
	public void setDesconto(final BigDecimal desconto) {
		this.desconto = desconto;
	}

	/**
	 * @return the abatimento
	 */
	public BigDecimal getAbatimento() {
		return this.abatimento;
	}

	/**
	 * @param abatimento
	 *            the abatimento to set
	 */
	public void setAbatimento(final BigDecimal abatimento) {
		this.abatimento = abatimento;
	}

	/**
	 * @return the carteiraBaixa
	 */
	public CarteiraDTO getCarteiraBaixa() {
		return this.carteiraBaixa;
	}

	/**
	 * @param carteiraBaixa
	 *            the carteiraBaixa to set
	 */
	public void setCarteiraBaixa(final CarteiraDTO carteiraBaixa) {
		this.carteiraBaixa = carteiraBaixa;
	}

	/**
	 * @return the carteiraBoleto
	 */
	public CarteiraDTO getCarteiraBoleto() {
		return this.carteiraBoleto;
	}

	/**
	 * @param carteiraBoleto
	 *            the carteiraBoleto to set
	 */
	public void setCarteiraBoleto(final CarteiraDTO carteiraBoleto) {
		this.carteiraBoleto = carteiraBoleto;
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

}