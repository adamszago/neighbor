package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.DragDropEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.PermissaoDTO;
import br.com.lphantus.neighbor.common.PlanoDTO;
import br.com.lphantus.neighbor.service.IPermissaoService;
import br.com.lphantus.neighbor.service.IPlanoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
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
@ManagedBean(name = "planoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class PlanoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IPlanoService planoService;

	@Autowired
	private IPermissaoService permissaoService;

	// Plano
	private PlanoDTO plano;
	private List<PlanoDTO> planos;

	// Permissao
	private PermissaoDTO permissaoSelecionada;
	private List<PermissaoDTO> permissoesDisponiveis;
	private List<PermissaoDTO> permissoesSelecionadas;

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor padrao
	 */
	public PlanoController() {

	}

	@PostConstruct
	public void initialize() {
		atualizarTela();
	}

	/**
	 * Metodo para início de cadastro. Este metodo e chamado no construtor do
	 * controller Tambem e responsavel por listar os registros existentes no
	 * banco e inicializar as variaveis
	 */
	private void atualizarTela() {
		this.plano = new PlanoDTO();
		this.planos = new ArrayList<PlanoDTO>();

		this.permissoesDisponiveis = new ArrayList<PermissaoDTO>();
		this.permissoesSelecionadas = new ArrayList<PermissaoDTO>();
		this.permissaoSelecionada = new PermissaoDTO();

		listarPlanos();
		listarPermissoesDisponiveis();
	}

	/**
	 * Método de Salvar registro Este método utilizar o objeto Service obtido
	 * atarvés de injeção pelo construtor. A classe JsfUtil providencia
	 * mensagens do tipo FacesMessage para serem enviadas A classe
	 * GerenciadoMensagem usa mensagens pre-definidas em arquivo properties para
	 * serem enviadas.
	 * 
	 */
	@Secured({ "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarPlano() {
		if (this.permissoesSelecionadas.isEmpty()) {
			JsfUtil.addErrorMessage(GerenciadorMensagem
					.getMensagem("SELECIONE_PERMISSAO"));
		} else {
			try {
				this.planoService.save(PlanoDTO.Builder.getInstance()
						.createEntity(this.plano));

				registrarHistorico("Gravou Plano: - " + this.plano.getNome());

				atualizarPermissoes();
				atualizarTela();
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("SAVE_OK"));
			} catch (final Exception e) {// catch (ServiceException e) {
				JsfUtil.addErrorMessage(e.getMessage());
			}
		}
	}

	@Secured({ "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterarPlano() {
		try {
			this.planoService.update(PlanoDTO.Builder.getInstance()
					.createEntity(this.plano));

			registrarHistorico("Atualizou dados do Plano: "
					+ this.plano.getIdPlano() + " - " + this.plano.getNome());

			atualizarPermissoes();
			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_ROOT" })
	public void prepararPlanoSelecionadosParaEditar() {
		try {
			this.permissoesDisponiveis = PermissaoDTO.Builder.getInstance()
					.createList(this.permissaoService.findAll());
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		this.permissoesSelecionadas = new ArrayList<PermissaoDTO>();
		final Iterator<PermissaoDTO> iteratorPermissoesDisponiveis = this.permissoesDisponiveis
				.iterator();
		while (iteratorPermissoesDisponiveis.hasNext()) {
			final PermissaoDTO per = iteratorPermissoesDisponiveis.next();
			for (final PlanoDTO plan : per.getPlanosList()) {
				if (plan.getIdPlano().equals(this.plano.getIdPlano())) {
					this.permissoesSelecionadas.add(per);
					iteratorPermissoesDisponiveis.remove();
				}
			}
		}
	}

	@Secured({ "ROLE_ROOT" })
	public void listarPlanos() {
		try {
			this.planos = PlanoDTO.Builder.getInstance().createList(
					this.planoService.findAll());
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_ROOT" })
	public void listarPermissoesDisponiveis() {
		try {
			this.permissoesDisponiveis = PermissaoDTO.Builder.getInstance()
					.createList(this.permissaoService.findAll());
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void onPermissaoDrop(final DragDropEvent ddEvent) {
		final PermissaoDTO perSelecionada = ((PermissaoDTO) ddEvent.getData());
		Boolean isSelecionada = false;
		for (final PermissaoDTO p : this.permissoesSelecionadas) { // verificar
			// se
			// a
			// permissao
			// ja foi selecionada. Se
			// sim, nao adiciona
			// novamente
			if (p.getNome().equals(perSelecionada.getNome())) {
				isSelecionada = true;
			}
		}
		if (!isSelecionada) {
			this.permissoesSelecionadas.add(perSelecionada); // adicionar
			final Iterator<PermissaoDTO> iteratorPermissoesDisponiveis = this.permissoesDisponiveis
					.iterator(); // remover das disponiveis
			while (iteratorPermissoesDisponiveis.hasNext()) {
				final PermissaoDTO perDisponivel = iteratorPermissoesDisponiveis
						.next();
				if (perSelecionada.getIdPermissao().equals(
						perDisponivel.getIdPermissao())) {
					iteratorPermissoesDisponiveis.remove(); // removendo
				}
			}
		}
	}

	public void onRemoverPermissaoDrop(final DragDropEvent ddEvent) {
		final PermissaoDTO perRemover = ((PermissaoDTO) ddEvent.getData());
		final Iterator<PermissaoDTO> iteratorPermissoesSelcionadas = this.permissoesSelecionadas
				.iterator();// remover das selecionadas
		while (iteratorPermissoesSelcionadas.hasNext()) {
			final PermissaoDTO perSelecionada = iteratorPermissoesSelcionadas
					.next();
			if (perSelecionada.getIdPermissao().equals(
					perRemover.getIdPermissao())) {
				iteratorPermissoesSelcionadas.remove(); // removendo
			}
		}

		final Iterator<PermissaoDTO> iteratorPermissoesDisponiveis = this.permissoesDisponiveis
				.iterator(); // adicionar nas disponiveis
		Boolean temDisponivel = false;
		while (iteratorPermissoesDisponiveis.hasNext()) {
			final PermissaoDTO perDisponivel = iteratorPermissoesDisponiveis
					.next();
			if (perDisponivel.getIdPermissao().equals(
					perRemover.getIdPermissao())) {
				temDisponivel = true;
			}
		}
		if (!temDisponivel) {
			this.permissoesDisponiveis.add(perRemover);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void atualizarPermissoes() {
		if (this.permissoesSelecionadas != null) {
			for (final PermissaoDTO per : this.permissoesSelecionadas) {
				if ((this.plano != null) && (this.plano.getIdPlano() != null)) {
					if (per.getPlanos() == null) {
						final Set<PlanoDTO> pl = new HashSet<PlanoDTO>();
						per.setPlanos(pl);
					}
					per.getPlanos().add(this.plano);
					try {
						this.permissaoService.update(PermissaoDTO.Builder
								.getInstance().createEntity(per));

						registrarHistorico("Adicionou Plano: "
								+ this.plano.getIdPlano() + " - "
								+ this.plano.getNome() + " na permissao: "
								+ per.getIdPermissao() + " - " + per.getNome());

					} catch (final ServiceException e) {
						JsfUtil.addErrorMessage(e.getMessage());
					}
				} else {
					JsfUtil.addErrorMessage("Plano is Null");
				}
			}
		} else {
			JsfUtil.addErrorMessage(GerenciadorMensagem
					.getMensagem("SELECIONE_PERMISSAO"));
		}
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_ROOT" })
	public String pagNovoPlano() {
		atualizarTela();
		return "/pages/root/cadplano.jsf";
	}

	@Secured({ "ROLE_ROOT" })
	public String pageListaPlano() {
		atualizarTela();
		return "/pages/root/listaplano.jsf";
	}

	@Secured({ "ROLE_ROOT" })
	public String pagEditarPlano() {
		prepararPlanoSelecionadosParaEditar();
		return "/pages/root/cadplano.jsf";
	}

	// GETTER AND SETTER

	/**
	 * @return the plano
	 */
	public PlanoDTO getPlano() {
		return plano;
	}

	/**
	 * @param plano
	 *            the plano to set
	 */
	public void setPlano(PlanoDTO plano) {
		this.plano = plano;
	}

	/**
	 * @return the planos
	 */
	public List<PlanoDTO> getPlanos() {
		return planos;
	}

	/**
	 * @param planos
	 *            the planos to set
	 */
	public void setPlanos(List<PlanoDTO> planos) {
		this.planos = planos;
	}

	/**
	 * @return the permissaoSelecionada
	 */
	public PermissaoDTO getPermissaoSelecionada() {
		return permissaoSelecionada;
	}

	/**
	 * @param permissaoSelecionada the permissaoSelecionada to set
	 */
	public void setPermissaoSelecionada(PermissaoDTO permissaoSelecionada) {
		this.permissaoSelecionada = permissaoSelecionada;
	}

	/**
	 * @return the permissoesDisponiveis
	 */
	public List<PermissaoDTO> getPermissoesDisponiveis() {
		return permissoesDisponiveis;
	}

	/**
	 * @param permissoesDisponiveis
	 *            the permissoesDisponiveis to set
	 */
	public void setPermissoesDisponiveis(
			List<PermissaoDTO> permissoesDisponiveis) {
		this.permissoesDisponiveis = permissoesDisponiveis;
	}

	/**
	 * @return the permissoesSelecionadas
	 */
	public List<PermissaoDTO> getPermissoesSelecionadas() {
		return permissoesSelecionadas;
	}

	/**
	 * @param permissoesSelecionadas
	 *            the permissoesSelecionadas to set
	 */
	public void setPermissoesSelecionadas(
			List<PermissaoDTO> permissoesSelecionadas) {
		this.permissoesSelecionadas = permissoesSelecionadas;
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
