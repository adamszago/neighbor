package br.com.lphantus.neighbor.controller;

import static br.com.lphantus.neighbor.enums.ValueSelectStatus.ATIVOS;
import static br.com.lphantus.neighbor.enums.ValueSelectStatus.INATIVOS;
import static br.com.lphantus.neighbor.enums.ValueSelectStatus.TODOS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
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
import br.com.lphantus.neighbor.component.LancamentoDataModel;
import br.com.lphantus.neighbor.component.PessoaFisicaDataModel;
import br.com.lphantus.neighbor.entity.Fatura;
import br.com.lphantus.neighbor.service.IFaturaService;
import br.com.lphantus.neighbor.service.ILancamentoService;
import br.com.lphantus.neighbor.service.IPessoaFisicaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "faturaPagarController")
@Transactional(propagation = Propagation.SUPPORTS)
public class FaturaPagarController extends AbstractController {

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
	private final LancamentoDataModel lancamentos = new LancamentoDataModel();

	private PessoaFisicaDTO pessoaSelecionada;
	private FaturaDTO entity;
	private LancamentoDTO[] lancamentosSelecionados;
	private List<PessoaFisicaDTO> pessoasLista = new ArrayList<PessoaFisicaDTO>();

	/**
	 * Construtor padrao
	 */
	public FaturaPagarController() {

	}

	public void limparObjetoFatura() {
		this.entity = new FaturaDTO();
		this.entity.setDataCadastro(new Date());
		this.entity.setLancamentos(new ArrayList<LancamentoDTO>());
		this.pessoaSelecionada = null;
	}

	private void gerarListaAtivo() {
		try {
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (usuarioLogado().isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.entities = this.faturaService.buscarPorCondominio(
						condominio, Boolean.TRUE);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar de ativos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista de ativos.", e);
		}
	}

	private void gerarListaInativo() {
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
						condominio, Boolean.FALSE);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de inativos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista de inativos.", e);
		}
	}

	private void gerarListaTodos() {
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
						condominio, null);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista todos registros.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista todos registros.", e);
		}
	}

	public void gerarLista() {
		if (this.valueSelectStatus.equals(TODOS)) {
			gerarListaTodos();
		} else if (this.valueSelectStatus.equals(ATIVOS)) {
			gerarListaAtivo();
		} else if (this.valueSelectStatus.equals(INATIVOS)) {
			gerarListaInativo();
		}
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

	@Secured({ "ROLE_LISTAR_FATURA", "ROLE_ROOT" })
	public String pageListaFatura() {
		this.valueSelectStatus = TODOS;
		gerarLista();

		return "/pages/financeiro/listafaturapagar.jsf";
	}

	@Secured({ "ROLE_ADICIONAR_FATURA", "ROLE_ROOT" })
	public String pageCadastroFatura() {
		limparObjetoFatura();
		carregarListaMoradores();

		return "/pages/financeiro/cadfaturapagar.jsf";
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
	public String gravarFaturaPagar() {
		try {
			this.entity.setCondominio(condominioUsuarioLogado());
			final Fatura entidade = FaturaDTO.Builder.getInstance()
					.createEntity(this.entity);
			this.faturaService.save(entidade);

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("SAVE_OK"));

			this.valueSelectStatus = TODOS;
			gerarLista();

			return "/pages/financeiro/listafatura.jsf";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar fatura.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		return null;
	}

	@Secured({ "ROLE_EDITAR_FATURA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterar() {
		try {
			final Fatura entidade = FaturaDTO.Builder.getInstance()
					.createEntity(this.entity);
			this.faturaService.update(entidade);

			// TODO: rever possibilidade de adicionar outros lancamentos
			// (remover anteriores)

			this.valueSelectStatus = TODOS;
			gerarLista();

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

			return "/pages/financeiro/listafatura.jsf";
		} catch (final ServiceException e) {
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
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar faturas em aberto.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		this.valueSelectStatus = TODOS;
		gerarLista();
	}

	@Secured({ "ROLE_EDITAR_FATURA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String editarFatura() {

		final List<PessoaDTO> listaUnica = new ArrayList<PessoaDTO>();
		listaUnica.add(this.entity.getLancamentos().get(0).getPessoa());

		this.pessoas.setWrappedData(listaUnica);
		this.lancamentos.setWrappedData(this.entity.getLancamentos());

		return "/pages/financeiro/cadfaturapagar.jsf";
	}

	private void atualizaLancamentos() {
		final List<LancamentoDTO> lista = new ArrayList<LancamentoDTO>();

		try {
			lista.addAll(this.lancamentoService.buscarNaoAssociadosPagar(
					this.pessoaSelecionada, condominioUsuarioLogado()));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar lancamentos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		this.lancamentos.setWrappedData(lista);

		if (null == this.entity.getLancamentos()) {
			this.entity.setLancamentos(new ArrayList<LancamentoDTO>());
		}
		this.entity.getLancamentos().clear();
	}

	private void adicionaLancamentoFatura(final LancamentoDTO lancamento) {
		this.faturaService.adicionaLancamentoFatura(this.entity, lancamento);
	}

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

	public void onRowSelectLancamento(final SelectEvent event) {
		final LancamentoDTO lancamento = (LancamentoDTO) event.getObject();
		getLogger().debug("Lancamento selecionado " + lancamento);
		adicionaLancamentoFatura(lancamento);
	}

	public void onRowUnselectLancamento(final UnselectEvent event) {
		final LancamentoDTO lancamento = (LancamentoDTO) event.getObject();
		removeLancamentoFatura(lancamento);
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
	public LancamentoDataModel getLancamentos() {
		return this.lancamentos;
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