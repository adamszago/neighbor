package br.com.lphantus.neighbor.controller;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.service.IHistoricoService;
import br.com.lphantus.neighbor.service.IUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Component
public abstract class AbstractController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IHistoricoService historicoService;

	@Autowired
	private IUnidadeHabitacionalService habitacionalService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public AbstractController() {

	}

	private UsuarioDTO userLogado;

	protected UsuarioDTO usuarioLogado() {
		if (this.userLogado == null) {
			if (null != this.usuarioService) {
				this.userLogado = this.usuarioService.getUsuarioLogado();
			} else {

			}
		}
		return this.userLogado;
	}

	protected CondominioDTO condominioUsuarioLogado() {
		CondominioDTO retorno;
		if (null == usuarioLogado()) {
			retorno = null;
		} else {
			retorno = usuarioLogado().getCondominio();
		}
		return retorno;
	}

	protected void registrarHistorico(final String acao) throws ServiceException {
		try {
			final HistoricoDTO historico = new HistoricoDTO();
			historico.setAcaoExecutada(acao);

			final CondominioDTO condominio = condominioUsuarioLogado();
			if (null != condominio) {
				historico.setCondominio(condominio.getPessoa().getNome());
				historico.setIdCondominio(condominio.getPessoa().getIdPessoa());
			}
			historico.setDataHoraAcao(new Date());

			final UsuarioDTO usuario = usuarioLogado();
			if (null != usuario) {
				historico.setUsuario(usuario.getLogin());
			}

			this.historicoService.save(HistoricoDTO.Builder.getInstance().createEntity(historico));
		} catch (final ServiceException e) {
			logger.error("Ocorreu um erro ao registrar historico.", e);
			throw e;
		} catch (final Exception e) {
			logger.error("Ocorreu um erro ao registrar historico.", e);
			throw new ServiceException(e);
		}
	}

	protected void registrarHistorico(final String acao, final UsuarioDTO outroUsuario) throws ServiceException {
		final HistoricoDTO historico = new HistoricoDTO();
		historico.setAcaoExecutada(acao);
		if ( outroUsuario == null ) {
			historico.setCondominio(condominioUsuarioLogado().getPessoa().getNome());
			historico.setIdCondominio(condominioUsuarioLogado().getPessoa().getIdPessoa());
			historico.setUsuario("Nao identificado");
		} else {
			final CondominioDTO condominioOutro = this.usuarioService.getCondominioUsuario(outroUsuario);
			if (condominioOutro == null) {
				
			}else{
				historico.setCondominio(condominioOutro.getPessoa().getNome());
				historico.setIdCondominio(condominioOutro.getPessoa().getIdPessoa());
			}
			historico.setUsuario(outroUsuario.getLogin());
		}

		historico.setDataHoraAcao(new Date());
		this.historicoService.save(HistoricoDTO.Builder.getInstance().createEntity(historico));
	}

	protected UnidadeHabitacionalDTO buscarCasaMorador(final MoradorDTO moradorDTO) throws ServiceException {
		return this.habitacionalService.buscarUnidadeHabitacionalMorador(moradorDTO);
	}

	public int ordenacaoNumerica(final Object s1, final Object s2) {
		if (StringUtils.isNumeric(s1.toString()) && StringUtils.isNumeric(s2.toString())) {
			final Long valor1 = Long.valueOf(s1.toString());
			final Long valor2 = Long.valueOf(s2.toString());
			return valor1.compareTo(valor2);
		} else {
			return s1.toString().compareTo(s2.toString());
		}
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

}
