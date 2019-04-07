package br.com.lphantus.neighbor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ContratoDTO;
import br.com.lphantus.neighbor.entity.Contrato;
import br.com.lphantus.neighbor.repository.IContratoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IContratoService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ContratoServiceImpl extends
		GenericService<Long, ContratoDTO, Contrato> implements IContratoService {

	@Autowired
	private IContratoDAO contratoDao;

	@Autowired
	private IUsuarioService usuarioService;

	@Override
	public ContratoDTO getContratoByCondominio(final CondominioDTO condominio)
			throws ServiceException {
		try {
			return this.contratoDao.buscarContratoPorCondominio(condominio);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public boolean verificarLimiteUsuarioBycondominio(
			final CondominioDTO condominio) throws ServiceException {
		boolean isValido = true;
		if (condominio != null) {
			final ContratoDTO contrato = getContratoByCondominio(condominio);
			if (contrato != null) {
				final CondominioDTO condominioUsuario = this.usuarioService
						.getCondominioUsuario(this.usuarioService
								.getUsuarioLogado());
				final Long usuariosCadasrtrados = this.usuarioService
						.getTotalUsuariosByCondominio(condominioUsuario);
				final Long limiteUsuarios = contrato.getLimiteUsuarios();
				getLogger().info(
						"usuariosCadasrtrados condominio: ("
								+ condominioUsuario.getPessoa().getNome()
								+ ") - " + usuariosCadasrtrados);
				getLogger().info(
						"limiteUsuarios: ("
								+ condominioUsuario.getPessoa().getNome()
								+ ") - " + limiteUsuarios);
				if ((limiteUsuarios != null) && (usuariosCadasrtrados != null)) {
					if (usuariosCadasrtrados >= limiteUsuarios) {
						isValido = false;
						getLogger().info(
								"Limite de usuarios do condominio "
										+ "("
										+ condominioUsuario.getPessoa()
												.getIdPessoa()
										+ "-"
										+ condominioUsuario.getPessoa()
												.getNome() + ") foi atingido.");
					}
				}
			}
		} else {
			getLogger().error("Parametro Condominio null");
			throw new ServiceException("Parametro Condominio null");
		}

		return isValido;
	}

}
