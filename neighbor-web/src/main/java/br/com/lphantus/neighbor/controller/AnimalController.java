package br.com.lphantus.neighbor.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.AnimalEstimacaoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.TipoAnimalDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.AnimalEstimacao;
import br.com.lphantus.neighbor.entity.TipoAnimal;
import br.com.lphantus.neighbor.service.IAnimalEstimacaoService;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.ITipoAnimalService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

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
@ManagedBean(name = "animalController")
@Transactional(propagation = Propagation.SUPPORTS)
public class AnimalController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IAnimalEstimacaoService service;

	@Autowired
	private IMoradorService serviceMorador;

	@Autowired
	private ITipoAnimalService tipoAnimalService;

	@Autowired
	private IUsuarioService usuarioService;

	// Animal
	private AnimalEstimacaoDTO animalEstimacao;
	private List<AnimalEstimacaoDTO> animais;
	private String tipoAnimalSelecionado;
	private List<TipoAnimalDTO> tipoAnimais;
	private TipoAnimalDTO tipoAnimal;
	private List<AnimalEstimacaoDTO> animaisMorador;
	private List<SelectItem> listaSelectItem;
	private Date dataMaximaVistoria = new Date();
	private boolean atualizarItemTela;

	// Morador
	private String casa;
	private MoradorDTO morador;

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor padrao
	 */
	public AnimalController() {

	}

	@PostConstruct
	public void initialize() {
		atualizarTela();
	}

	/**
	 * Metodo para início de cadastro. Este metodo e chamado no construtor do
	 * controller Tambem e responsavel por listar os registros existentes no
	 * banco e alimentar a lista
	 */
	private void atualizarTela() {
		this.animalEstimacao = new AnimalEstimacaoDTO();
		this.animais = new ArrayList<AnimalEstimacaoDTO>();
		this.tipoAnimal = new TipoAnimalDTO();
		this.casa = BigDecimal.ZERO.toString();
		this.morador = new MoradorDTO();
		this.animaisMorador = new ArrayList<AnimalEstimacaoDTO>();
		this.historico = new HistoricoDTO();

		this.gerarListaAnimais();
		this.gerarListaTiposAnimais();
		this.carregarTipoAnimais();

	}

	private void gerarListaAnimais() {
		this.animalEstimacao = new AnimalEstimacaoDTO();
		this.animais = new ArrayList<AnimalEstimacaoDTO>();
		this.tipoAnimal = new TipoAnimalDTO();
		this.casa = BigDecimal.ZERO.toString();
		this.morador = new MoradorDTO();

		try {
			if (usuarioLogado() != null) {
				CondominioDTO condominio;

				if (usuarioLogado().isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.animais.addAll(this.service.findByCondominio(condominio));
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de animais.", e);
		}
	}

	/**
	 * Método de Salvar registro Este método utilizar o objeto Service obtido
	 * atarvés de injeção pelo construtor. A classe JsfUtil providencia
	 * mensagens do tipo FacesMessage para serem enviadas A classe
	 * GerenciadoMensagem usa mensagens pre-definidas em arquivo properties para
	 * serem enviadas.
	 */
	@Secured({ "ROLE_CADASTRO_ANIMAL", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarAnimal() {
		try {

			final AnimalEstimacao entidade = AnimalEstimacaoDTO.Builder.getInstance().createEntity(this.animalEstimacao);

			if (this.tipoAnimalSelecionado.equals("-1")) {
				entidade.setTipoAnimal(null);
			} else {
				final TipoAnimal tpAnimal = this.tipoAnimalService.findById(Long.parseLong(this.tipoAnimalSelecionado));
				entidade.setTipoAnimal(tpAnimal);
			}

			this.service.save(entidade);

			MoradorDTO detalhesMorador = serviceMorador.buscarDetalhesMorador(animalEstimacao.getDono());
			UnidadeHabitacionalDTO unidade = detalhesMorador.getUnidadeHabitacional().get(0).getUnidadeHabitacional();
			registrarHistorico(String.format("Gravou Animal: Tipo: \"%s\" Nome: \"%s\" Casa \"%s\"", 
					entidade.getTipoAnimal().getTipoAnimal(), animalEstimacao.getNome(), unidade.getIdentificacao()));

			// bug 43
			// animalEstimacao = new AnimalEstimacao();
			this.atualizarItemTela = true;
			// gerarListaAnimais();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_EXCLUIR_TIPO_ANIMAL", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterarTipo() {
		try {
			if (this.tipoAnimal.isAtivo()) {
				this.tipoAnimal.setAtivo(false);
			} else {
				this.tipoAnimal.setAtivo(true);
			}

			this.tipoAnimalService.update(TipoAnimalDTO.Builder.getInstance().createEntity(this.tipoAnimal));
			// tipoAnimalService.delete(tipoAnimal);

			registrarHistorico("Alterou status Tipo Animal: " + this.tipoAnimal.getId() + " - " + this.tipoAnimal.getTipoAnimal() + "para " + this.tipoAnimal.isAtivo());

			this.tipoAnimal = new TipoAnimalDTO();
			gerarListaTiposAnimais();

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
			this.tipoAnimais = TipoAnimalDTO.Builder.getInstance().createList(this.tipoAnimalService.findAll());
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
		return null;
	}

	@Secured({ "ROLE_EDITAR_ANIMAL", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterarAnimal() {
		try {
			AnimalEstimacao animal = service.findById(animalEstimacao.getId());
			if (this.tipoAnimalSelecionado.equals("-1")) {
				animal.setTipoAnimal(null);
			} else {
				final TipoAnimal tpAnimal = this.tipoAnimalService.findById(Long.parseLong(this.tipoAnimalSelecionado));
				animal.setTipoAnimal(tpAnimal);
			}
			this.service.update(animal);

			registrarHistorico("Alterou Animal Estimacao: " + this.animalEstimacao.getId() + " - " + this.animalEstimacao.getTipoAnimal());

			// bug 43
			// animalEstimacao = new AnimalEstimacao();
			// gerarListaAnimais();
			this.atualizarItemTela = true;
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_EXCLUIR_ANIMAL", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String excluir() {
		try {
			this.service.delete(AnimalEstimacaoDTO.Builder.getInstance().createEntity(this.animalEstimacao));

			registrarHistorico("Excluiu Animal: " + this.animalEstimacao.getId() + " - " + this.animalEstimacao.getNome());

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("DELETE_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
		atualizarTela();
		return null;
	}

	public String buscarDono() {
		try {
			final MoradorDTO moradorTemp = this.serviceMorador.buscarPrincipal(this.casa, condominioUsuarioLogado());

			final UsuarioDTO usuarioMorador = this.usuarioService.buscaUsuarioMorador(moradorTemp.getPessoa().getIdPessoa());

			final CondominioDTO condominio = usuarioMorador.getCondominio();

			if ((moradorTemp != null) && (condominio != null)) {
				if (condominio.getPessoa().getIdPessoa().equals(condominioUsuarioLogado().getPessoa().getIdPessoa())) {
					this.animalEstimacao.setDono(moradorTemp);
				} else {
					JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("CASA_INVALIDA"));
				}
			} else {
				JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("CASA_INVALIDA"));
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("CASA_INVALIDA"));
		}
		return null;
	}

	private void gerarListaTiposAnimais() {
		this.tipoAnimais = new ArrayList<TipoAnimalDTO>();
		try {
			if (usuarioLogado() != null) {

				CondominioDTO condominio;

				if (usuarioLogado().isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.tipoAnimais = this.tipoAnimalService.buscarPorCondominio(condominio);
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void carregarTipoAnimais() {
		gerarListaTiposAnimais();

		this.listaSelectItem = new ArrayList<SelectItem>();
		if (this.tipoAnimais != null) {
			for (final TipoAnimalDTO tpa : this.tipoAnimais) {
				if (tpa.isAtivo()) {
					final SelectItem item = new SelectItem(tpa.getId(), tpa.getTipoAnimal());
					this.listaSelectItem.add(item);
				}
			}
		}
	}

	@Secured({ "ROLE_CADASTRO_TIPO_ANIMAL", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarTipoAnimal() {
		try {
			if (condominioUsuarioLogado() != null) {
				this.tipoAnimal.setCondominio(condominioUsuarioLogado());
			}

			final TipoAnimal entidade = TipoAnimalDTO.Builder.getInstance().createEntity(this.tipoAnimal);

			this.tipoAnimalService.save(entidade);

			registrarHistorico(String.format("Gravou novo tipo de animal de estimação: ", this.tipoAnimal.getTipoAnimal()));

			// bug 78
			this.tipoAnimalSelecionado = String.format("%d", entidade.getId());
			getLogger().debug("Tipo animal: " + entidade.getId());

			this.tipoAnimal = new TipoAnimalDTO();
			this.gerarListaTiposAnimais();
			this.carregarTipoAnimais();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void valueChangeListener(final ValueChangeEvent event) {
		if (event.getNewValue().toString().equalsIgnoreCase("-1")) {
			return;
		} else {
			this.tipoAnimalSelecionado = event.getNewValue().toString();
		}
	}

	@Secured({ "ROLE_LISTA_ANIMAL_MORADOR", "ROLE_ROOT" })
	public String listaAnimaisMorador() {
		try {
			this.animaisMorador = this.service.listarAnimaisMorador(this.morador);
		} catch (final Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "sucesso";
	}

	// direcionamento de paginas

	@Secured({ "ROLE_LISTA_ANIMAL", "ROLE_ROOT" })
	public String listarAnimais() {
		gerarListaAnimais();
		if (this.atualizarItemTela) {
			atualizarTela();
			this.atualizarItemTela = false;
		}
		return "/pages/listagem/listaanimais.jsf";
	}

	@Secured({ "ROLE_CADASTRO_ANIMAL", "ROLE_ROOT" })
	public String novoAnimal() {
		this.carregarTipoAnimais();
		this.animalEstimacao = new AnimalEstimacaoDTO();
		return "/pages/cadastros/cadanimalestimacao.jsf";
	}

	@Secured({ "ROLE_CADASTRO_TIPO_ANIMAL", "ROLE_ROOT" })
	public String novoTipoAnimal() {
		this.tipoAnimal = new TipoAnimalDTO();
		return "/pages/cadastros/manutencaotipoanimal.jsf";
	}

	@Secured({ "ROLE_EDITAR_ANIMAL", "ROLE_ROOT" })
	public String editarAnimal() {
		return "editarAnimal";
	}

	// GETTERS E SETTERS

	/**
	 * @return the animalEstimacao
	 */
	public AnimalEstimacaoDTO getAnimalEstimacao() {
		return animalEstimacao;
	}

	/**
	 * @param animalEstimacao
	 *            the animalEstimacao to set
	 */
	public void setAnimalEstimacao(AnimalEstimacaoDTO animalEstimacao) {
		this.animalEstimacao = animalEstimacao;
	}

	/**
	 * @return the animais
	 */
	public List<AnimalEstimacaoDTO> getAnimais() {
		return animais;
	}

	/**
	 * @param animais
	 *            the animais to set
	 */
	public void setAnimais(List<AnimalEstimacaoDTO> animais) {
		this.animais = animais;
	}

	/**
	 * @return the tipoAnimalSelecionado
	 */
	public String getTipoAnimalSelecionado() {
		return tipoAnimalSelecionado;
	}

	/**
	 * @param tipoAnimalSelecionado
	 *            the tipoAnimalSelecionado to set
	 */
	public void setTipoAnimalSelecionado(String tipoAnimalSelecionado) {
		this.tipoAnimalSelecionado = tipoAnimalSelecionado;
	}

	/**
	 * @return the tipoAnimais
	 */
	public List<TipoAnimalDTO> getTipoAnimais() {
		return tipoAnimais;
	}

	/**
	 * @param tipoAnimais
	 *            the tipoAnimais to set
	 */
	public void setTipoAnimais(List<TipoAnimalDTO> tipoAnimais) {
		this.tipoAnimais = tipoAnimais;
	}

	/**
	 * @return the tipoAnimal
	 */
	public TipoAnimalDTO getTipoAnimal() {
		return tipoAnimal;
	}

	/**
	 * @param tipoAnimal
	 *            the tipoAnimal to set
	 */
	public void setTipoAnimal(TipoAnimalDTO tipoAnimal) {
		this.tipoAnimal = tipoAnimal;
	}

	/**
	 * @return the animaisMorador
	 */
	public List<AnimalEstimacaoDTO> getAnimaisMorador() {
		return animaisMorador;
	}

	/**
	 * @param animaisMorador
	 *            the animaisMorador to set
	 */
	public void setAnimaisMorador(List<AnimalEstimacaoDTO> animaisMorador) {
		this.animaisMorador = animaisMorador;
	}

	/**
	 * @return the listaSelectItem
	 */
	public List<SelectItem> getListaSelectItem() {
		return listaSelectItem;
	}

	/**
	 * @param listaSelectItem
	 *            the listaSelectItem to set
	 */
	public void setListaSelectItem(List<SelectItem> listaSelectItem) {
		this.listaSelectItem = listaSelectItem;
	}

	/**
	 * @return the dataMaximaVistoria
	 */
	public Date getDataMaximaVistoria() {
		return dataMaximaVistoria;
	}

	/**
	 * @param dataMaximaVistoria
	 *            the dataMaximaVistoria to set
	 */
	public void setDataMaximaVistoria(Date dataMaximaVistoria) {
		this.dataMaximaVistoria = dataMaximaVistoria;
	}

	/**
	 * @return the atualizarItemTela
	 */
	public boolean isAtualizarItemTela() {
		return atualizarItemTela;
	}

	/**
	 * @param atualizarItemTela
	 *            the atualizarItemTela to set
	 */
	public void setAtualizarItemTela(boolean atualizarItemTela) {
		this.atualizarItemTela = atualizarItemTela;
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
	 * @return the morador
	 */
	public MoradorDTO getMorador() {
		return morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(MoradorDTO morador) {
		this.morador = morador;
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
