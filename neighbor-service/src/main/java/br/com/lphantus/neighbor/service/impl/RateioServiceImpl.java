package br.com.lphantus.neighbor.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.common.RateioDTO;
import br.com.lphantus.neighbor.entity.Rateio;
import br.com.lphantus.neighbor.repository.IRateioDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IConfiguracaoCondominioService;
import br.com.lphantus.neighbor.service.IRateioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class RateioServiceImpl extends GenericService<Long, RateioDTO, Rateio> implements
		IRateioService {

	@Autowired
	private IRateioDAO rateioDAO;

	@Autowired
	private IConfiguracaoCondominioService configuracaoCondominioService;

	@Override
	protected void saveValidate(final Rateio rateio) throws ServiceException {
		super.saveValidate(rateio);
		if (rateio.getValor() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("RATEIO_VALOR_NULO"));
		}

	}

	@Override
	protected void updateValidate(final Rateio rateio) throws ServiceException {
		super.updateValidate(rateio);
		if (rateio.getValor() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("RATEIO_VALOR_NULO"));
		}
	}

	@Override
	public List<RateioDTO> buscarPorMovimentacao(
			final MovimentacaoDTO movimentacao) throws ServiceException {
		try {
			return this.rateioDAO.buscarPorMovimentacao(movimentacao);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar rateios por movimentacao.", e);
			throw new ServiceException(
					"Erro ao buscar rateios por movimentacao", e);
		}
	}

	@Override
	public List<RateioDTO> buscarPorMesAtual(final CondominioDTO condominio,
			final Date selectedDate) throws ServiceException {
		try {
			final ConfiguracaoCondominioDTO configuracao = this.configuracaoCondominioService
					.buscarPorCondominio(condominio);
			return this.rateioDAO.buscarPorMesAtual(condominio, configuracao,
					selectedDate);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar rateios das movimentacoes do mes.", e);
			throw new ServiceException(
					"Erro ao buscar rateios das movimentacoes do mes.", e);
		}
	}
}
