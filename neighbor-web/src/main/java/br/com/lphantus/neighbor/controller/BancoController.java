package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.BancoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Banco;
import br.com.lphantus.neighbor.service.IBancoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;
import br.com.lphantus.neighbor.utils.Constantes;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "bancoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class BancoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IBancoService bancoService;

	private List<BancoDTO> entities;

	private BancoDTO entity;

	private String valueSelectStatus;

	private final String TODOS = "TODOS";
	private final String ATIVOS = "ATIVOS";
	private final String INATIVOS = "INATIVOS";

	private String emptyMessageLista;

	/**
	 * Construtor padrao
	 */
	public BancoController() {
	}

	@PostConstruct
	public void initialize() {
		this.entity = new BancoDTO();
		this.valueSelectStatus = this.ATIVOS;
	}

	public void limparObjetoBanco() {
		this.entity = new BancoDTO();
	}

	public void gerarListaBancos() {
		this.entities = new ArrayList<BancoDTO>();

		if (this.valueSelectStatus.equals(this.TODOS)) {
			gerarListaTodosBancos();
		} else if (this.valueSelectStatus.equals(this.ATIVOS)) {
			gerarListaBancosAtivos();
		} else if (this.valueSelectStatus.equals(this.INATIVOS)) {
			gerarListaBancosInativos();
		}
	}

	public void listarBancosInativos() {
		this.valueSelectStatus = this.INATIVOS;
		gerarListaBancosInativos();
	}

	public void listarBancosAtivos() {
		this.valueSelectStatus = this.ATIVOS;
		gerarListaBancosAtivos();
	}

	private void gerarListaBancosAtivos() {
		this.entities = new ArrayList<BancoDTO>();
		try {
			if (usuarioLogado() != null) {
				// TODO: preencher entidades de acordo com o usuario logado
				// if (usuarioLogado().isRoot()) {
				// this.entities = BancoDTO.Builder.getInstance().createList(
				// this.bancoService.findAll(new ParamQuery(true)));
				// } else {
				// this.entities = BancoDTO.Builder.getInstance().createList(
				// this.bancoService.findAll(new ParamQuery(
				// condominioUsuarioLogado().getPessoa()
				// .getIdPessoa(), true)));
				// }
				if (false) {
					throw new ServiceException(Constantes.VAZIO);
				}
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de bancos ativos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private void gerarListaBancosInativos() {
		this.entities = new ArrayList<BancoDTO>();
		try {
			final UsuarioDTO user = usuarioLogado();
			if (usuarioLogado() != null) {
				// TODO: preencher entidades de acordo com o usuario logado
				// if (user.isRoot()) {
				// this.entities = BancoDTO.Builder.getInstance().createList(
				// this.bancoService.findAll(new ParamQuery(false)));
				// } else {
				// this.entities = BancoDTO.Builder.getInstance().createList(
				// this.bancoService.findAll(new ParamQuery(
				// condominioUsuarioLogado().getPessoa()
				// .getIdPessoa(), false)));
				// }
				if (false) {
					throw new ServiceException(Constantes.VAZIO);
				}
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de bancos inativos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private void gerarListaTodosBancos() {
		this.entities = new ArrayList<BancoDTO>();
		try {
			final UsuarioDTO user = usuarioLogado();
			if (usuarioLogado() != null) {
				// TODO: preencher entidades de acordo com o usuario logado
				// if (user.isRoot()) {
				// this.entities = BancoDTO.Builder.getInstance().createList(
				// this.bancoService.findAll());
				// } else {
				// this.entities = BancoDTO.Builder.getInstance().createList(
				// this.bancoService.findAll(new ParamQuery(
				// condominioUsuarioLogado().getPessoa()
				// .getIdPessoa(), true)));
				// }
				if (false) {
					throw new ServiceException(Constantes.VAZIO);
				}
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de todos os bancos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public String getEmptyMessageLista() {
		if (this.valueSelectStatus.equals(this.ATIVOS)) {
			this.emptyMessageLista = "Sem bancos ativos para exibir";
		} else if (this.valueSelectStatus.equals(this.INATIVOS)) {
			this.emptyMessageLista = "Sem bancos inativos para exibir";
		} else if (this.valueSelectStatus.equals(this.TODOS)) {
			this.emptyMessageLista = "Sem bancos cadastrados para exibir";
		}
		return this.emptyMessageLista;
	}

	public void setValueSelectStatus(final String selectStatusBanco) {
		if ((selectStatusBanco != null) && !selectStatusBanco.equals("")) {
			this.valueSelectStatus = selectStatusBanco;
		}
	}

	public String getValueSelectStatus() {
		return this.valueSelectStatus;
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
				this.bancoService.update(BancoDTO.Builder.getInstance()
						.createEntity(this.entity));
				gerarListaBancos();
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("ALTER_OK"));
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public String pageListaBanco() {
		this.valueSelectStatus = this.ATIVOS;
		gerarListaBancos();
		return "/pages/financeiro/listabanco.jsf";
	}

	@Secured({ "ROLE_ROOT", "ROLE_EDITAR_BANCO" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterar() {
		try {
			final Banco entidade = BancoDTO.Builder.getInstance().createEntity(
					this.entity);
			this.bancoService.update(entidade);

			this.limparObjetoBanco();
			this.gerarListaTodosBancos();

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

			return "/pages/financeiro/listabanco.jsf";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar banco.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gravar banco.", e);
		}
		return null;
	}

	public String pageCadastroBanco() {
		return "/pages/financeiro/cadbanco.jsf";
	}

	@Secured({ "ROLE_ROOT", "ROLE_ADICIONAR_BANCO" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String gravar() {
		try {

			this.entity.setCondominio(condominioUsuarioLogado());
			this.entity.setDatacadastro(new Date());

			this.bancoService.gravarBanco(this.entity);

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("SAVE_OK"));

			this.limparObjetoBanco();
			this.gerarListaTodosBancos();
			return "/pages/financeiro/listabanco.jsf";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar banco.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gravar banco.", e);
		}
		return null;
	}

	@Secured({ "ROLE_ROOT", "ROLE_EDITAR_BANCO" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterarBanco() {
		try {
			this.bancoService.alterarBanco(this.entity);

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

			this.limparObjetoBanco();
			this.gerarListaTodosBancos();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao alterar banco.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao alterar banco.", e);
		}
		return "/pages/financeiro/listabanco.jsf";
	}

	// GETTERS E SETTERS

	/**
	 * @return the entities
	 */
	public List<BancoDTO> getEntities() {
		return entities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(List<BancoDTO> entities) {
		this.entities = entities;
	}

	/**
	 * @return the entity
	 */
	public BancoDTO getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(BancoDTO entity) {
		this.entity = entity;
	}

}
