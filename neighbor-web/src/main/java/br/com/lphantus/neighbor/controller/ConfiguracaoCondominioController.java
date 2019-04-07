package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.entity.Condominio;
import br.com.lphantus.neighbor.entity.ConfiguracaoCondominio;
import br.com.lphantus.neighbor.service.ICondominioService;
import br.com.lphantus.neighbor.service.IConfiguracaoCondominioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "configuracaoCondominioController")
@Transactional(propagation = Propagation.SUPPORTS)
public class ConfiguracaoCondominioController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IConfiguracaoCondominioService configuracaoCondominioService;
	
	@Autowired
	private ICondominioService condominioService;

	private List<CondominioDTO> condominios;
	private CondominioDTO condominio;
	private Boolean contaSuspensa;
	
	private ConfiguracaoCondominioDTO entity;

	/**
	 * Construtor padrao
	 */
	public ConfiguracaoCondominioController() {

	}

	@Secured({ "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String pageConfigurar() {
		String retorno = "/pages/cadastros/cadconfiguracoes.jsf";
		try {
			
			condominios = new ArrayList<CondominioDTO>();
			condominios.addAll(condominioService.buscarTodos());
		} catch (final ServiceException e) {
			getLogger().debug("Erro ao obter configuracao do condominio.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		return retorno;
	}

	@Secured({ "ROLE_CONFIGURAR_CONDOMINIO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String configurarCondominio() {
		String retorno = "";
		try {
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				condominio = condominioUsuarioLogado();
				this.entity = this.configuracaoCondominioService.buscarPorCondominio(condominio);

				retorno = "/pages/financeiro/cadconfiguracoes.jsf";
			}
		} catch (final ServiceException e) {
			getLogger().debug("Erro ao obter configuracao do condominio.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		if (StringUtils.isBlank(retorno)) {
			JsfUtil.addErrorMessage("Erro ao obter configuracao do condominio.");
		}

		return retorno;
	}

	@Secured({ "ROLE_CONFIGURAR_CONDOMINIO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravar() {
		try {
			// altera os dados da tela
			final ConfiguracaoCondominio entidade;
			entidade = ConfiguracaoCondominioDTO.Builder.getInstance().createEntity(this.entity);
			this.configuracaoCondominioService.update(entidade);

			// altera os dados de conta suspensa
			Condominio condominioBanco = condominioService.findById(this.entity.getCondominio().getPessoa().getIdPessoa());
			condominioBanco.setContaSuspensa(contaSuspensa);
			condominioService.update(condominioBanco);

			registrarHistorico("Alterou os dados da configuracao do condominio.");

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			getLogger().debug("Erro ao gravar configuração do condomínio.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().debug("Erro ao gravar configuração do condomínio.", e);
		}
	}
	
	public void condominioChange(final SelectEvent event){
		try {
			CondominioDTO dto = (CondominioDTO) event.getObject();
			entity = configuracaoCondominioService.buscarPorCondominio(dto);
			
			Condominio condominioBanco = condominioService.findById(dto.getPessoa().getIdPessoa());
			contaSuspensa = condominioBanco.isContaSuspensa();
		} catch (ServiceException e) {
			getLogger().debug("Erro ao obter configuração do condomínio.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	/**
	 * @return the condominios
	 */
	public List<CondominioDTO> getCondominios() {
		return condominios;
	}

	/**
	 * @param condominios the condominios to set
	 */
	public void setCondominios(List<CondominioDTO> condominios) {
		this.condominios = condominios;
	}

	/**
	 * @return the condominio
	 */
	public CondominioDTO getCondominio() {
		return condominio;
	}

	/**
	 * @param condominio the condominio to set
	 */
	public void setCondominio(CondominioDTO condominio) {
		this.condominio = condominio;
	}

	/**
	 * @return the entity
	 */
	public ConfiguracaoCondominioDTO getEntity() {
		return this.entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(final ConfiguracaoCondominioDTO entity) {
		this.entity = entity;
	}

	/**
	 * @return the contaSuspensa
	 */
	public Boolean getContaSuspensa() {
		return contaSuspensa;
	}

	/**
	 * @param contaSuspensa the contaSuspensa to set
	 */
	public void setContaSuspensa(Boolean contaSuspensa) {
		this.contaSuspensa = contaSuspensa;
	}

}