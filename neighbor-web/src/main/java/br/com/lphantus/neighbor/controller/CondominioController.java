package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.PessoaJuridicaDTO;
import br.com.lphantus.neighbor.service.ICondominioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;
import br.com.lphantus.neighbor.util.comparator.NomeUfComparator;

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
@ManagedBean(name = "condominioController")
@Transactional(propagation = Propagation.SUPPORTS)
public class CondominioController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private ICondominioService service;

	// Condominio
	private CondominioDTO condominio;
	private List<CondominioDTO> condominios = null;
	private boolean atualizarItemTela;
	private List<SelectItem> estados = new ArrayList<SelectItem>();

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor padrao
	 */
	public CondominioController() {

	}

	@PostConstruct
	public void initialize() {
		atualizarTela();
		gerarListaEstados();
	}

	private void gerarListaEstados() {
		this.estados = new ArrayList<SelectItem>();

		final List<UnidadeFederativa> listaEstados = Arrays
				.asList(UnidadeFederativa.values());
		Collections.sort(listaEstados, new NomeUfComparator());

		for (final UnidadeFederativa estado : listaEstados) {
			if (!estado.equals(UnidadeFederativa.DESCONHECIDO)) {
				this.estados.add(new SelectItem(estado, estado.getSigla()));
			}
		}
	}

	/**
	 * Metodo para início de cadastro. Este metodo e chamado no construtor do
	 * controller Tambem e responsavel por listar os registros existentes no
	 * banco e alimentar a lista
	 */
	private void atualizarTela() {
		this.condominio = new CondominioDTO();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void gravar(final ActionEvent event) {
		try {
			this.condominio.getPessoa().setDataCadastro(new Date());
			this.service.save(CondominioDTO.Builder.getInstance().createEntity(
					this.condominio));

			registrarHistorico("Gravou Condominio: - "
					+ this.condominio.getPessoa().getNome());

			// bug 43
			// condominio = new Condominio();
			// this.condominios = null;
			this.atualizarItemTela = true;

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("SAVE_OK"));

			this.condominio = new CondominioDTO();

		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void alterar(final ActionEvent event) {
		try {
			this.service.update(CondominioDTO.Builder.getInstance()
					.createEntity(this.condominio));

			registrarHistorico("Atualizou dados do Condominio: "
					+ this.condominio.getPessoa().getIdPessoa() + " - "
					+ this.condominio.getPessoa().getNome());

			// bug 43
			// condominio = new Condominio();
			// this.condominios = null;
			this.atualizarItemTela = true;

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

			this.condominio = new CondominioDTO();

		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * Exclui um registro da tabela morador
	 * 
	 * @throws ServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String ativarInativar() {
		String novoStatus = "";
		try {

			if (this.condominio.getPessoa().isAtivo()) {
				this.condominio.getPessoa().setAtivo(false);
				novoStatus = "INATIVO";
			} else {
				this.condominio.getPessoa().setAtivo(true);
				novoStatus = "ATIVO";
			}

			this.service.update(CondominioDTO.Builder.getInstance()
					.createEntity(this.condominio));

			registrarHistorico("Alterou Status do Condominio para : "
					+ novoStatus + " - "
					+ this.condominio.getPessoa().getIdPessoa() + " - "
					+ this.condominio.getNomeAbreviado());

			this.condominio = new CondominioDTO();
			this.condominios = null;

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

		} catch (final ServiceException e) {

			JsfUtil.addErrorMessage(e.getMessage());

		}

		return null;
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_LISTA_CONDOMINIO", "ROLE_ROOT" })
	public String pageListaCondominio() {
		this.condominios = null;
		return "/pages/administracao/listacondominio.jsf";
	}

	@Secured({ "ROLE_CADASTRO_CONDOMINIO", "ROLE_ROOT" })
	public String pageNovoCondominio() {
		this.condominio = new CondominioDTO();
		this.condominio.setPessoa(new PessoaJuridicaDTO());
		return "/pages/administracao/cadcondominio.jsf";
	}

	public String novo() {
		return null;
	}
	
	public String editarCondominio() {
		return "editarcondominio";
	}

	// GETTER AND SETTER

	/**
	 * @return the condominios
	 */
	public List<CondominioDTO> getCondominios() {
		if (this.condominios == null) {
			try {
				if (this.atualizarItemTela) {
					atualizarTela();
					this.atualizarItemTela = false;
				}
				this.condominios = this.service.buscarTodos();
			} catch (final ServiceException e) {
				JsfUtil.addErrorMessage(e.getMessage());
			}
		}
		return this.condominios;
	}

	/**
	 * @param condominios
	 *            the condominios to set
	 */
	public void setCondominios(final List<CondominioDTO> condominios) {
		this.condominios = condominios;
	}

	/**
	 * @return the condominio
	 */
	public CondominioDTO getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final CondominioDTO condominio) {
		this.condominio = condominio;
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

	/**
	 * @return the estados
	 */
	public List<SelectItem> getEstados() {
		return this.estados;
	}

}
