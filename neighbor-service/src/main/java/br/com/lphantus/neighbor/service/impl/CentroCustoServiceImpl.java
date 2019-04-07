package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CentroCustoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.entity.CentroCusto;
import br.com.lphantus.neighbor.repository.ICentroCustoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.ICentroCustoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class CentroCustoServiceImpl extends
		GenericService<Long, CentroCustoDTO, CentroCusto> implements
		ICentroCustoService {

	@Autowired
	private ICentroCustoDAO centroCustoDAO;

	@Override
	protected void saveValidate(final CentroCusto centroCusto)
			throws ServiceException {
		super.saveValidate(centroCusto);
		if (centroCusto.getId() != null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("ID_NOTNULL"));
		}

		if ((centroCusto.getNome() == null)
				|| centroCusto.getNome().trim().equals("")) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("CENTRO_CUSTO_NOME_OBRIGATORIO"));
		}

		validarCondominio(centroCusto);
	}

	@Override
	protected void updateValidate(final CentroCusto centroCusto)
			throws ServiceException {
		super.updateValidate(centroCusto);
		if (centroCusto.getId() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("ID_NULO"));
		}

		if ((centroCusto.getNome() == null)
				|| centroCusto.getNome().trim().equals("")) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("CENTRO_CUSTO_NOME_OBRIGATORIO"));
		}

		validarCondominio(centroCusto);

	}

	private void validarCondominio(final CentroCusto centroCusto)
			throws ServiceException {
		if ((centroCusto.getCondominio() == null)
				|| (centroCusto.getCondominio().getIdPessoa() == null)) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("CONDOMINIO_NULO"));
		}
	}

	@Override
	public List<CentroCustoDTO> findCentrosCustoRaizByCondominio(
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.centroCustoDAO
					.findCentrosCustoRaizByCondominio(condominio);
		} catch (final DAOException e) {
			getLogger()
					.error("Erro ao buscar centros de custo raizes por condominio.",
							e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<CentroCustoDTO> findCentrosCustoFilhos(
			final CentroCustoDTO centroCustoPai) throws ServiceException {
		try {

			return this.centroCustoDAO.findCentrosCustoFilhos(centroCustoPai);
		} catch (final DAOException e) {
			getLogger().error(
					"Erro ao buscar centros de custo por centro de custo pai.",
					e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<CentroCustoDTO> buscarPorCondominio(final Boolean status,
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.centroCustoDAO.buscarPorCondominio(status, condominio);
		} catch (final DAOException e) {
			getLogger().error(
					"Erro ao buscar centros de custo por condominio.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public boolean existeCentroCustoLancavel(final CondominioDTO condominio) {
		boolean retorno = false;
		try {
			final List<CentroCustoDTO> centros = buscarPorCondominio(
					Boolean.TRUE, condominio);
			for (final CentroCustoDTO item : centros) {
				if (item.isLancavel()) {
					retorno = true;
					break;
				}
			}
		} catch (final Exception e) {
			getLogger()
					.error("Erro ao buscar centros de custo lancaveis por condominio.",
							e);
		}
		return retorno;
	}

}
