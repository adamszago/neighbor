package br.com.lphantus.neighbor.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.MarcaVeiculoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.common.VeiculoDTO;
import br.com.lphantus.neighbor.entity.MarcaVeiculo;
import br.com.lphantus.neighbor.entity.Veiculo;
import br.com.lphantus.neighbor.service.IMarcaVeiculoService;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.IVeiculoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;
import br.com.lphantus.neighbor.util.comparator.ComparaPorString;

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
@ManagedBean(name = "veiculoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class VeiculoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IVeiculoService service;

	@Autowired
	private IMoradorService serviceMorador;

	@Autowired
	private IMarcaVeiculoService marcaService;

	@Autowired
	private IUsuarioService serviceUsuario;

	// Veiculo
	private List<VeiculoDTO> veiculos = null;
	private VeiculoDTO veiculo;

	// Marca
	private MarcaVeiculoDTO marcaVeiculo;
	private List<SelectItem> listaSelectItem;
	private String marcaSelecionada;
	private List<MarcaVeiculoDTO> marcas = null;

	// Morador
	private MoradorDTO morador;
	private List<VeiculoDTO> veiculosMorador;
	private String casa;

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor de classe
	 */
	public VeiculoController() {

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
		this.veiculo = new VeiculoDTO();
		this.veiculos = new ArrayList<VeiculoDTO>();
		this.marcaVeiculo = new MarcaVeiculoDTO();
		this.morador = new MoradorDTO();
		this.veiculosMorador = new ArrayList<VeiculoDTO>();
		this.carregarMarcas();
		this.casa = BigDecimal.ZERO.toString();
	}

	private void gerarListaVeiculo() {
		this.veiculo = new VeiculoDTO();
		this.veiculos = new ArrayList<VeiculoDTO>();
		this.marcaVeiculo = new MarcaVeiculoDTO();
		this.morador = new MoradorDTO();
		this.casa = BigDecimal.ZERO.toString();
		try {
			final UsuarioDTO userLogado = usuarioLogado();
			if (userLogado != null) {
				CondominioDTO condominio;
				if (userLogado.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.veiculos = this.service.buscarPorCondominio(condominio);
			}
		} catch (final ServiceException e) {
			e.printStackTrace();
		}
	}

	private void buscarMarcas() {
		try {
			this.marcas = new ArrayList<MarcaVeiculoDTO>();
			final UsuarioDTO userLogado = usuarioLogado();
			if (userLogado != null) {
				CondominioDTO condominio;
				if (userLogado.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.marcas = this.marcaService.buscarPorCondominio(condominio);
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * Método de Salvar registro Este método utilizar o objeto Service obtido
	 * atarvés de injeção pelo construtor. A classe JsfUtil providencia
	 * mensagens do tipo FacesMessage para serem enviadas A classe
	 * GerenciadoMensagem usa mensagens pre-definidas em arquivo properties para
	 * serem enviadas.
	 */
	@Secured({ "ROLE_CADASTRO_VEICULO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarVeiculo(final ActionEvent event) {
		try {

			final Veiculo entidade = VeiculoDTO.Builder.getInstance().createEntity(this.veiculo);

			if (this.marcaSelecionada.equals("-1")) {
				entidade.setMarca(null);
			} else {
				final MarcaVeiculo marca = this.marcaService.findById(Long.parseLong(this.marcaSelecionada));
				entidade.setMarca(marca);
			}

			this.service.save(entidade);

			MoradorDTO detalhesMorador = serviceMorador.buscarDetalhesMorador(veiculo.getProprietario());
			UnidadeHabitacionalDTO unidade = detalhesMorador.getUnidadeHabitacional().get(0).getUnidadeHabitacional();
			registrarHistorico(String.format("Gravou Veiculo: \"%s\" \"%s\" - Cor \"%s\", placa \"%s\" Casa.: %s", 
				entidade.getMarca().getMarca(), this.veiculo.getModelo(), veiculo.getCor(), veiculo.getPlaca(), unidade.getIdentificacao()));

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_EDITAR_VEICULO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterarVeiculo(final ActionEvent event) {
		try {
			if (this.marcaSelecionada.equals("-1")) {
				this.veiculo.setMarca(null);
			} else {
				final MarcaVeiculo marca = this.marcaService.findById(Long.parseLong(this.marcaSelecionada));
				this.veiculo.setMarca(marca.createDto());
			}
			this.service.update(VeiculoDTO.Builder.getInstance().createEntity(this.veiculo));

			registrarHistorico("Alterou Veiculo: " + this.veiculo.getId() + " - " + this.veiculo.getModelo());

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_EXCLUIR_MARCA_VEICULO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterarMarca() {
		try {
			final UsuarioDTO userLogado = usuarioLogado();
			if (this.marcaVeiculo.isAtivo()) {
				this.marcaVeiculo.setAtivo(false);
			} else {
				this.marcaVeiculo.setAtivo(true);
			}

			this.marcaService.update(MarcaVeiculoDTO.Builder.getInstance().createEntity(this.marcaVeiculo));

			registrarHistorico("Alterou status Marca Veiculo: " + this.marcaVeiculo.getId() + " - " + this.marcaVeiculo.getMarca() + "para " + this.marcaVeiculo.isAtivo());

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
			this.marcas = new ArrayList<MarcaVeiculoDTO>();
			if (userLogado != null) {
				CondominioDTO condominio;
				if (userLogado.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.marcas = this.marcaService.buscarPorCondominio(condominio);
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	/*
	 * public String novoVeiculo(){ atualizarTela(); return "novoVeiculo"; }
	 */

	/**
	 * Exclui um registro da tabela veiculo
	 * 
	 * @throws ServiceException
	 */
	@Secured({ "ROLE_EXCLUIR_VEICULO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String excluir() {
		try {
			this.service.delete(VeiculoDTO.Builder.getInstance().createEntity(this.veiculo));

			registrarHistorico("Excluiu Veiculo: " + this.veiculo.getId() + " - " + this.veiculo.getModelo());

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("DELETE_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
		atualizarTela();
		return null;
	}

	public String buscarProprietario() {
		try {
			final MoradorDTO moradorTemp = this.serviceMorador.buscarPrincipal(this.casa, condominioUsuarioLogado());

			final UsuarioDTO usuarioMorador = this.serviceUsuario.buscaUsuarioMorador(moradorTemp.getPessoa().getIdPessoa());

			final CondominioDTO condominio = usuarioMorador.getCondominio();

			if ((moradorTemp != null) && (condominio != null)) {
				if (condominio.getPessoa().getIdPessoa().equals(condominioUsuarioLogado().getPessoa().getIdPessoa())) {
					this.veiculo.setProprietario(moradorTemp);
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

	public void carregarMarcas() {
		this.listaSelectItem = new ArrayList<SelectItem>();
		buscarMarcas();

		final ComparaPorString comp = new ComparaPorString();
		Collections.sort(this.marcas, comp);
		for (final MarcaVeiculoDTO marca : this.marcas) {
			if (marca.isAtivo()) {
				final SelectItem item = new SelectItem(marca.getId(), marca.getMarca());
				this.listaSelectItem.add(item);
			}
		}
		// Collections.sort(listaSelectItem);
	}

	@Secured({ "ROLE_CADASTRO_MARCA_VEICULO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarMarca() {
		try {

			if (condominioUsuarioLogado() != null) {
				this.marcaVeiculo.setCondominio(condominioUsuarioLogado());
			}

			this.marcaService.save(MarcaVeiculoDTO.Builder.getInstance().createEntity(this.marcaVeiculo));

			registrarHistorico("Gravou Marca Veiculo: - " + this.marcaVeiculo.getMarca());

			this.marcaVeiculo = new MarcaVeiculoDTO();
			this.buscarMarcas();
			this.carregarMarcas();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void valueChangeListener(final ValueChangeEvent event) {
		if (event.getNewValue().toString().equalsIgnoreCase("-1")) {
			return;
		} else {
			this.marcaSelecionada = event.getNewValue().toString();
		}
	}

	@Secured({ "ROLE_LISTA_DETALHES_MORADOR", "ROLE_ROOT" })
	public String listaVeiculosMorador() {
		try {
			this.veiculosMorador = this.service.listarVeiculosMorador(this.morador);
		} catch (final Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "sucesso";
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_CADASTRO_VEICULO", "ROLE_ROOT" })
	public String novoVeiculo() {
		atualizarTela();
		return "/pages/cadastros/cadveiculo.jsf";
	}

	@Secured({ "ROLE_CADASTRO_MARCA_VEICULO", "ROLE_ROOT" })
	public String novaMarcaVeiculo() {
		this.carregarMarcas();
		this.marcaVeiculo = new MarcaVeiculoDTO();
		return "/pages/cadastros/manutencaomarca.jsf";
	}

	@Secured({ "ROLE_EDITAR_VEICULO", "ROLE_ROOT" })
	public String editarVeiculo() {
		return "editarVeiculo";
	}

	@Secured({ "ROLE_LISTA_VEICULOS", "ROLE_ROOT" })
	public String listarVeiculos() {
		gerarListaVeiculo();
		return "/pages/listagem/listaveiculos.jsf";
	}

	// GETTER AND SETTER

	/**
	 * @return the veiculos
	 */
	public List<VeiculoDTO> getVeiculos() {
		return this.veiculos;
	}

	/**
	 * @param veiculos
	 *            the veiculos to set
	 */
	public void setVeiculos(final List<VeiculoDTO> veiculos) {
		this.veiculos = veiculos;
	}

	/**
	 * @return the veiculo
	 */
	public VeiculoDTO getVeiculo() {
		return this.veiculo;
	}

	/**
	 * @param veiculo
	 *            the veiculo to set
	 */
	public void setVeiculo(final VeiculoDTO veiculo) {
		this.veiculo = veiculo;
	}

	/**
	 * @return the marcaVeiculo
	 */
	public MarcaVeiculoDTO getMarcaVeiculo() {
		return this.marcaVeiculo;
	}

	/**
	 * @param marcaVeiculo
	 *            the marcaVeiculo to set
	 */
	public void setMarcaVeiculo(final MarcaVeiculoDTO marcaVeiculo) {
		this.marcaVeiculo = marcaVeiculo;
	}

	/**
	 * @return the listaSelectItem
	 */
	public List<SelectItem> getListaSelectItem() {
		return this.listaSelectItem;
	}

	/**
	 * @param listaSelectItem
	 *            the listaSelectItem to set
	 */
	public void setListaSelectItem(final List<SelectItem> listaSelectItem) {
		this.listaSelectItem = listaSelectItem;
	}

	/**
	 * @return the marcaSelecionada
	 */
	public String getMarcaSelecionada() {
		return this.marcaSelecionada;
	}

	/**
	 * @param marcaSelecionada
	 *            the marcaSelecionada to set
	 */
	public void setMarcaSelecionada(final String marcaSelecionada) {
		this.marcaSelecionada = marcaSelecionada;
	}

	/**
	 * @return the marcas
	 */
	public List<MarcaVeiculoDTO> getMarcas() {
		return this.marcas;
	}

	/**
	 * @param marcas
	 *            the marcas to set
	 */
	public void setMarcas(final List<MarcaVeiculoDTO> marcas) {
		this.marcas = marcas;
	}

	/**
	 * @return the morador
	 */
	public MoradorDTO getMorador() {
		return this.morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final MoradorDTO morador) {
		this.morador = morador;
	}

	/**
	 * @return the veiculosMorador
	 */
	public List<VeiculoDTO> getVeiculosMorador() {
		return this.veiculosMorador;
	}

	/**
	 * @param veiculosMorador
	 *            the veiculosMorador to set
	 */
	public void setVeiculosMorador(final List<VeiculoDTO> veiculosMorador) {
		this.veiculosMorador = veiculosMorador;
	}

	/**
	 * @return the casa
	 */
	public String getCasa() {
		return this.casa;
	}

	/**
	 * @param casa
	 *            the casa to set
	 */
	public void setCasa(final String casa) {
		this.casa = casa;
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

}
