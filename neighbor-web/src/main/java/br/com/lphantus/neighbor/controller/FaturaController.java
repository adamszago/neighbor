package br.com.lphantus.neighbor.controller;

import static br.com.lphantus.neighbor.enums.ValueSelectStatus.ATIVOS;
import static br.com.lphantus.neighbor.enums.ValueSelectStatus.INATIVOS;
import static br.com.lphantus.neighbor.enums.ValueSelectStatus.TODOS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.common.LancamentoDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.component.PessoaFisicaDataModel;
import br.com.lphantus.neighbor.entity.Fatura;
import br.com.lphantus.neighbor.service.IFaturaService;
import br.com.lphantus.neighbor.service.ILancamentoService;
import br.com.lphantus.neighbor.service.IPessoaFisicaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

/**
 * @author Roque Bridi Jr
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "faturaController")
@Transactional(propagation = Propagation.SUPPORTS)
public class FaturaController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IFaturaService faturaService;

	@Autowired
	private ILancamentoService lancamentoService;

	@Autowired
	private IPessoaFisicaService pessoaFisicaService;

	private List<FaturaDTO> entities;
	private String valueSelectStatus;

	private final PessoaFisicaDataModel pessoas = new PessoaFisicaDataModel();
	// private final LancamentoDataModel lancamentos = new
	// LancamentoDataModel();
	private DualListModel<LancamentoDTO> lancamentos = new DualListModel<LancamentoDTO>();

	private PessoaFisicaDTO pessoaSelecionada;
	private FaturaDTO entity;
	private LancamentoDTO[] lancamentosSelecionados;
	private List<PessoaFisicaDTO> pessoasLista = new ArrayList<PessoaFisicaDTO>();

	/**
	 * Construtor padrao
	 */
	public FaturaController() {

	}

	public void limparObjetoFatura() {
		this.entity = new FaturaDTO();
		this.entity.setDataCadastro(new Date());
		this.entity.setLancamentos(new ArrayList<LancamentoDTO>());
		this.pessoaSelecionada = null;
	}

	private void gerarListaParametrizada(final Boolean parametro) {
		try {
			final UsuarioDTO user = usuarioLogado();
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (user.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.entities = this.faturaService.buscarPorCondominio(
						condominio, parametro);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de faturas.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista de fatura.", e);
		}
	}

	public void gerarLista() {
		Boolean parametro;
		if (this.valueSelectStatus.equals(ATIVOS)) {
			parametro = Boolean.TRUE;
		} else if (this.valueSelectStatus.equals(INATIVOS)) {
			parametro = Boolean.FALSE;
		} else {
			parametro = null;
		}
		gerarListaParametrizada(parametro);
	}

	@Secured({ "ROLE_LISTAR_FATURAS", "ROLE_ROOT" })
	public String pageListaFatura() {
		this.valueSelectStatus = TODOS;
		gerarLista();

		// this.lancamentos.setWrappedData(new ArrayList<LancamentoDTO>());
		this.lancamentos.setSource(new ArrayList<LancamentoDTO>());
		this.lancamentos.setTarget(new ArrayList<LancamentoDTO>());

		return "/pages/financeiro/listafatura.jsf";
	}

	@Secured({ "ROLE_ADICIONAR_FATURA", "ROLE_ROOT" })
	public String pageCadastroFatura() {
		limparObjetoFatura();
		carregarListaMoradores();

		// this.lancamentos.setWrappedData(new ArrayList<LancamentoDTO>());
		this.lancamentos.setSource(new ArrayList<LancamentoDTO>());
		this.lancamentos.setTarget(new ArrayList<LancamentoDTO>());

		return "/pages/financeiro/cadfatura.jsf";
	}

	private void carregarListaMoradores() {
		try {
			if (null == this.pessoasLista) {
				this.pessoasLista = new ArrayList<PessoaFisicaDTO>();
			}

			this.pessoasLista.clear();
			this.pessoasLista.addAll(this.pessoaFisicaService
					.buscarPessoasLancamentosAtivos());

			this.pessoas.setWrappedData(this.pessoasLista);
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar responsaveis financeiros.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao buscar responsaveis financeiros.", e);
		}
	}

	@Secured({ "ROLE_ADICIONAR_FATURA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String gravarFaturaReceber() {
		try {
			this.entity.setCondominio(condominioUsuarioLogado());
			this.entity.setLancamentos(this.lancamentos.getTarget());

			final Fatura entidade = FaturaDTO.Builder.getInstance()
					.createEntity(this.entity);
			this.faturaService.save(entidade);

			this.lancamentoService.associarFatura(this.lancamentos.getTarget(),
					entidade.createDto());

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("SAVE_OK"));

			this.valueSelectStatus = TODOS;
			gerarLista();

			return "/pages/financeiro/listafatura.jsf";
		} catch (final ServiceException e) {
			atualizaLancamentosBug();

			getLogger().error("Erro ao gravar fatura.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		return null;
	}

	public boolean isExistemNaoAssociados() {
		try {
			final List<LancamentoDTO> lista = this.lancamentoService
					.buscarNaoAssociados(null, condominioUsuarioLogado());
			return (null != lista) && !lista.isEmpty();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar lancamentos nao associados.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return false;
	}

	@Secured({ "ROLE_EDITAR_FATURA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterar() {
		try {
			this.entity.setLancamentos(this.lancamentos.getTarget());
			final Fatura entidade = FaturaDTO.Builder.getInstance()
					.createEntity(this.entity);
			this.entity.setLancamentos(null);
			this.faturaService.update(entidade);

			this.lancamentoService.associarFatura(this.lancamentos.getTarget(),
					entidade.createDto());
			this.lancamentoService.desassociarFatura(this.lancamentos
					.getSource());

			this.valueSelectStatus = TODOS;
			gerarLista();

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

			return "/pages/financeiro/listafatura.jsf";
		} catch (final ServiceException e) {
			atualizaLancamentosBug();
			getLogger().error("Erro ao gravar fatura.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		return null;
	}

	@Secured({ "ROLE_ADICIONAR_FATURA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gerarFaturasAberto() {
		try {
			this.faturaService.gerarFaturasAberto(condominioUsuarioLogado());

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("SAVE_OK"));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar faturas em aberto.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		this.valueSelectStatus = TODOS;
		gerarLista();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void ativarDesativar() {
		try {
			if (this.entity != null) {
				this.faturaService.alterarStatus(this.entity.getId(),
						!this.entity.isAtivo());
				gerarLista();
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("ALTER_OK"));
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao alterar status do registro.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_EDITAR_FATURA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String editarFatura() {

		final List<PessoaDTO> listaUnica = new ArrayList<PessoaDTO>();
		listaUnica.add(this.entity.getLancamentos().get(0).getPessoa());

		this.pessoaSelecionada = (PessoaFisicaDTO) this.entity.getLancamentos()
				.get(0).getPessoa();
		this.pessoas.setWrappedData(listaUnica);

		// this.lancamentos.setWrappedData(this.entity.getLancamentos());
		this.lancamentos = new DualListModel<LancamentoDTO>(
				new ArrayList<LancamentoDTO>(), this.entity.getLancamentos());
		atualizaLancamentos();

		return "/pages/financeiro/cadfatura.jsf";
	}

	private void atualizaLancamentos() {
		final List<LancamentoDTO> listaOrigem = new ArrayList<LancamentoDTO>();

		// busca os lancamentos nao associados a fatura alguma
		try {
			listaOrigem.addAll(this.lancamentoService.buscarNaoAssociados(
					this.pessoaSelecionada, condominioUsuarioLogado()));

		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar lancamentos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		this.lancamentos.setSource(listaOrigem);
	}

	private void atualizaLancamentosBug() {
		atualizaLancamentos();

		final List<LancamentoDTO> listaDestino = new ArrayList<LancamentoDTO>();

		try {
			listaDestino.addAll(this.lancamentoService
					.buscarPorFatura(this.entity));

			this.entity.setValor(BigDecimal.ZERO);
			for (final LancamentoDTO lancamento : listaDestino) {
				adicionaLancamentoFatura(lancamento);
			}

		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar lancamentos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		this.lancamentos.setTarget(listaDestino);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void adicionaLancamentoFatura(final LancamentoDTO lancamento) {
		this.faturaService.adicionaLancamentoFatura(this.entity, lancamento);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void removeLancamentoFatura(final LancamentoDTO lancamento) {
		this.faturaService.removeLancamentoFatura(this.entity, lancamento);
	}

	public void onRowSelect(final SelectEvent event) {
		final PessoaFisicaDTO selecionada = (PessoaFisicaDTO) event.getObject();
		this.pessoaSelecionada = selecionada;

		if (null != this.pessoaSelecionada) {
			atualizaLancamentos();
		}
	}

	public void onRowUnselect(final UnselectEvent event) {
		this.pessoaSelecionada = null;
	}

	public void onTransfer(final TransferEvent event) {
		try {
			@SuppressWarnings("unchecked")
			final List<LancamentoDTO> listaLancamento = (List<LancamentoDTO>) event
					.getItems();
			if (event.isAdd()) {

				final List<LancamentoDTO> listaNaoAssociados = this.lancamentoService
						.buscarNaoAssociados(this.pessoaSelecionada,
								condominioUsuarioLogado());

				// adiciona na lista
				for (final LancamentoDTO lancamento : listaLancamento) {
					for (final LancamentoDTO entidade : listaNaoAssociados) {
						if (lancamento.getId().equals(entidade.getId())) {
							adicionaLancamentoFatura(entidade);
							break;
						}
					}
				}
			} else {
				// remove da lista
				for (final LancamentoDTO lancamento : listaLancamento) {
					for (final LancamentoDTO entidade : this.entity
							.getLancamentos()) {
						if (lancamento.getId().equals(entidade.getId())) {
							removeLancamentoFatura(entidade);
							break;
						}
					}
				}
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao adicionar lancamento na fatura.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * @return the entities
	 */
	public List<FaturaDTO> getEntities() {
		return this.entities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(final List<FaturaDTO> entities) {
		this.entities = entities;
	}

	/**
	 * @return the valueSelectStatus
	 */
	public String getValueSelectStatus() {
		return this.valueSelectStatus;
	}

	/**
	 * @param valueSelectStatus
	 *            the valueSelectStatus to set
	 */
	public void setValueSelectStatus(final String valueSelectStatus) {
		this.valueSelectStatus = valueSelectStatus;
	}

	/**
	 * @return the entity
	 */
	public FaturaDTO getEntity() {
		return this.entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(final FaturaDTO entity) {
		this.entity = entity;
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
	 * @return the pessoas
	 */
	public PessoaFisicaDataModel getPessoas() {
		return this.pessoas;
	}

	/**
	 * 
	 * @return the lancamentos
	 */
	public DualListModel<LancamentoDTO> getLancamentos() {
		return this.lancamentos;
	}

	/**
	 * @param lancamentos
	 *            the lancamentos to set
	 */
	public void setLancamentos(final DualListModel<LancamentoDTO> lancamentos) {
		this.lancamentos = lancamentos;
	}

	/**
	 * @return the lancamentosSelecionados
	 */
	public LancamentoDTO[] getLancamentosSelecionados() {
		return this.lancamentosSelecionados;
	}

	/**
	 * @param lancamentosSelecionados
	 *            the lancamentosSelecionados to set
	 */
	public void setLancamentosSelecionados(
			final LancamentoDTO[] lancamentosSelecionados) {
		this.lancamentosSelecionados = lancamentosSelecionados;
	}
}