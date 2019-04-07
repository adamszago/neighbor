package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CarteiraDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Carteira;
import br.com.lphantus.neighbor.service.IBancoService;
import br.com.lphantus.neighbor.service.ICarteiraService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.integracao.bopepo.BancoSuportadoNeighbor;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;
import br.com.lphantus.neighbor.util.comparator.BancoSuportadoComparator;

/**
 * @author Joander Vieira Candido
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "carteiraController")
@Transactional(propagation = Propagation.SUPPORTS)
public class CarteiraController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ICarteiraService carteiraService;

	@Autowired
	private IBancoService bancoService;

	private List<BancoSuportadoNeighbor> bancos;
	private List<CarteiraDTO> entities;
	private CarteiraDTO entity;

	private String valueSelectStatus;

	private final String TODOS = "TODOS";
	private final String ATIVOS = "ATIVOS";
	private final String INATIVOS = "INATIVOS";

	private TreeNode treeRootCarteiras;
	private TreeNode noteCarteiraSelecionado;

	private String emptyMessageLista;

	private Integer sizeCarteira;
	private Integer sizeNossoNumero;

	private boolean informacaoBancaria;
	private boolean boletaBancaria;

	/**
	 * Construtor padrao
	 */
	public CarteiraController() {

	}

	@PostConstruct
	public void initialize() {
		this.valueSelectStatus = this.ATIVOS;
	}

	public void limparObjetoCarteira() {
		this.entity = new CarteiraDTO();
		this.informacaoBancaria = false;
		this.boletaBancaria = false;
	}

	private void gerarListaCarteiras() {
		this.entities = new ArrayList<CarteiraDTO>();

		onSelectStatus();
	}

	public void listarCarteiraesInativos() {
		this.valueSelectStatus = this.INATIVOS;
		onSelectStatus();
	}

	public void listarCarteirasAtivos() {
		this.valueSelectStatus = this.ATIVOS;
		onSelectStatus();
	}

	private void gerarListaTodosBancos() {
		this.bancos = new ArrayList<BancoSuportadoNeighbor>();
		this.bancos.addAll(Arrays.asList(BancoSuportadoNeighbor.values()));
		Collections.sort(this.bancos, new BancoSuportadoComparator());
	}

	public void onSelectStatus() {
		if (this.valueSelectStatus.equals(this.TODOS)) {
			gerarListaCarteiraStatus(null);
		} else if (this.valueSelectStatus.equals(this.ATIVOS)) {
			gerarListaCarteiraStatus(true);
		} else if (this.valueSelectStatus.equals(this.INATIVOS)) {
			gerarListaCarteiraStatus(false);
		}
	}

	private void gerarListaCarteiraStatus(final Boolean ativo) {
		this.entities = new ArrayList<CarteiraDTO>();
		try {
			final UsuarioDTO user = usuarioLogado();
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (user.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.entities.addAll(this.carteiraService.buscarPorParametros(
						condominio, ativo));
			}
		} catch (final ServiceException e) {
			getLogger().debug("Erro ao gerar lista de todas carteiras.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void atualizaCamposBancarios(final ValueChangeEvent event) {
		this.sizeCarteira = this.bancoService.obtemSizeCarteira(this.entity
				.getBanco());
		if ((null != this.entity.getNumeroCarteira())
				&& !this.entity.getNumeroCarteira().isEmpty()) {
			this.sizeNossoNumero = this.bancoService.obtemSizeNossoNumero(
					this.entity.getBanco(), this.entity.getNumeroCarteira());
		}

		final Boolean valor = Boolean.valueOf(event.getNewValue().toString());
		if (!valor) {
			limparBoleta();
		}
	}

	private void limparBoleta() {
		final String vazio = "";
		this.entity.setNumeroCarteira(vazio);
		this.entity.setNossoNumero(vazio);
		this.entity.setDigitoNossoNumero(vazio);
	}

	public void desmarcarBoleto(final ValueChangeEvent event) {
		final String vazio = "";

		final Boolean valor = Boolean.valueOf(event.getNewValue().toString());
		if (!valor) {
			this.boletaBancaria = false;
			this.entity.setBanco(vazio);
			this.entity.setNumeroConta(vazio);
			this.entity.setDigitoConta(vazio);
			this.entity.setNumeroAgencia(vazio);
			this.entity.setDigitoAgencia(vazio);
			limparBoleta();
		}
	}

	@Secured({ "ROLE_ADICIONAR_CARTEIRA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String gravarCarteira() {
		final String retorno = "";
		try {

			this.entity.setCondominio(condominioUsuarioLogado());

			this.entity.setDataCadastro(new Date());

			final Carteira entidade = CarteiraDTO.Builder.getInstance()
					.createEntity(this.entity);

			this.carteiraService.save(entidade);

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("SAVE_OK"));

			return pageListaCarteira();
		} catch (final ServiceException e) {
			getLogger().debug("Erro ao gravar carteira.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().debug("Erro ao gravar carteira.", e);
		}
		return retorno;
	}

	@Secured({ "ROLE_EDITAR_CARTEIRA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterar() {
		final String retorno = "";
		try {
			final Carteira entidade = CarteiraDTO.Builder.getInstance()
					.createEntity(this.entity);
			this.carteiraService.update(entidade);

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

			return pageListaCarteira();
		} catch (final ServiceException e) {
			getLogger().debug("Erro ao atualizar carteira.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().debug("Erro ao atualizar carteira.", e);
		}
		return retorno;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void ativarDesativar() {
		try {
			if (this.entity != null) {
				if (this.entity.isAtivo()) {
					this.entity.setAtivo(false);
				} else {
					this.entity.setAtivo(true);
				}
				this.carteiraService.update(CarteiraDTO.Builder.getInstance()
						.createEntity(this.entity));
				gerarListaCarteiras();
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("ALTER_OK"));
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void setValueSelectStatus(final String selectStatusCarteira) {
		if ((selectStatusCarteira != null) && !selectStatusCarteira.equals("")) {
			this.valueSelectStatus = selectStatusCarteira;
		}
	}

	public String getValueSelectStatus() {
		return this.valueSelectStatus;
	}

	public String getEmptyMessageLista() {
		if (this.valueSelectStatus.equals(this.ATIVOS)) {
			this.emptyMessageLista = "Sem centros de custo ativos para exibir";
		} else if (this.valueSelectStatus.equals(this.INATIVOS)) {
			this.emptyMessageLista = "Sem centros de custo inativos para exibir";
		} else if (this.valueSelectStatus.equals(this.TODOS)) {
			this.emptyMessageLista = "Sem centros de custo cadastrados para exibir";
		}
		return this.emptyMessageLista;
	}

	public String pageListaCarteira() {
		this.valueSelectStatus = this.ATIVOS;
		gerarListaCarteiras();
		gerarListaTodosBancos();

		return "/pages/financeiro/listacarteira.jsf";
	}

	public String editarCarteira() {
		this.informacaoBancaria = (this.entity.getBanco() != null)
				&& !this.entity.getBanco().isEmpty();
		this.boletaBancaria = (this.entity.getNumeroCarteira() != null)
				&& !this.entity.getNumeroCarteira().isEmpty();

		return "/pages/financeiro/cadcarteira.jsf";
	}

	public String pageCadastroCarteira() {
		limparObjetoCarteira();
		gerarListaTodosBancos();

		return "/pages/financeiro/cadcarteira.jsf";
	}

	// GETTERS E SETTERS

	/**
	 * @return the entities
	 */
	public List<CarteiraDTO> getEntities() {
		return entities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(List<CarteiraDTO> entities) {
		this.entities = entities;
	}

	/**
	 * @return the entity
	 */
	public CarteiraDTO getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(CarteiraDTO entity) {
		this.entity = entity;
	}

	/**
	 * @return the treeRootCarteiras
	 */
	public TreeNode getTreeRootCarteiras() {
		return treeRootCarteiras;
	}

	/**
	 * @param treeRootCarteiras
	 *            the treeRootCarteiras to set
	 */
	public void setTreeRootCarteiras(TreeNode treeRootCarteiras) {
		this.treeRootCarteiras = treeRootCarteiras;
	}

	/**
	 * @return the noteCarteiraSelecionado
	 */
	public TreeNode getNoteCarteiraSelecionado() {
		return noteCarteiraSelecionado;
	}

	/**
	 * @param noteCarteiraSelecionado
	 *            the noteCarteiraSelecionado to set
	 */
	public void setNoteCarteiraSelecionado(TreeNode noteCarteiraSelecionado) {
		this.noteCarteiraSelecionado = noteCarteiraSelecionado;
	}

	/**
	 * @return the bancos
	 */
	public List<BancoSuportadoNeighbor> getBancos() {
		return this.bancos;
	}

	/**
	 * @param bancos
	 *            the bancos to set
	 */
	public void setBancos(final List<BancoSuportadoNeighbor> bancos) {
		this.bancos = bancos;
	}

	/**
	 * @return the informacaoBancaria
	 */
	public boolean isInformacaoBancaria() {
		return this.informacaoBancaria;
	}

	/**
	 * @param informacaoBancaria
	 *            the informacaoBancaria to set
	 */
	public void setInformacaoBancaria(final boolean informacaoBancaria) {
		this.informacaoBancaria = informacaoBancaria;
	}

	/**
	 * @return the boletaBancaria
	 */
	public boolean isBoletaBancaria() {
		return this.boletaBancaria;
	}

	/**
	 * @param boletaBancaria
	 *            the boletaBancaria to set
	 */
	public void setBoletaBancaria(final boolean boletaBancaria) {
		this.boletaBancaria = boletaBancaria;
	}

	/**
	 * @return the sizeCarteira
	 */
	public Integer getSizeCarteira() {
		return this.sizeCarteira;
	}

	/**
	 * @param sizeCarteira
	 *            the sizeCarteira to set
	 */
	public void setSizeCarteira(final Integer sizeCarteira) {
		this.sizeCarteira = sizeCarteira;
	}

	/**
	 * @return the sizeNossoNumero
	 */
	public Integer getSizeNossoNumero() {
		return this.sizeNossoNumero;
	}

	/**
	 * @param sizeNossoNumero
	 *            the sizeNossoNumero to set
	 */
	public void setSizeNossoNumero(final Integer sizeNossoNumero) {
		this.sizeNossoNumero = sizeNossoNumero;
	}
}