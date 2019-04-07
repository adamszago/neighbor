package br.com.lphantus.neighbor.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.LancamentoTipoDTO;
import br.com.lphantus.neighbor.service.ILancamentoTipoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.JsfUtil;

/**
 * @author Roque Bridi Jr
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "lancamentoTipoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class LancamentoTipoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ILancamentoTipoService lancamentoTipoService;

	private List<LancamentoTipoDTO> entities;

	/**
	 * Construtor padrao
	 */
	public LancamentoTipoController() {

	}

	@PostConstruct
	public void carregarListaLancamentoTipo() {
		try {
			this.entities = LancamentoTipoDTO.Builder.getInstance().createList(
					this.lancamentoTipoService.findAll());
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar tipos de lancamentos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao buscar tipos de lancamentos.", e);
		}
	}

	public String pageListaLancamentoTipo() {
		carregarListaLancamentoTipo();
		return "/pages/financeiro/listatipolancamento.jsf";
	}

	public String editarLancamentoTipo() {
		return "/pages/financeiro/cadlancamentotipo.jsf";
	}

	/**
	 * @return the entities
	 */
	public List<LancamentoTipoDTO> getEntities() {
		return entities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(List<LancamentoTipoDTO> entities) {
		this.entities = entities;
	}

}