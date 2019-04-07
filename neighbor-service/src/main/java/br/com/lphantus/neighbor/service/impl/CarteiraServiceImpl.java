package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CarteiraDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.entity.Carteira;
import br.com.lphantus.neighbor.repository.ICarteiraDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.ICarteiraService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.utils.Constantes;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class CarteiraServiceImpl extends
		GenericService<Long, CarteiraDTO, Carteira> implements ICarteiraService {

	@Autowired
	private ICarteiraDAO carteiraDAO;

	@Override
	protected void saveValidate(final Carteira carteira)
			throws ServiceException {
		super.saveValidate(carteira);

		if ((carteira.getNome() == null) || carteira.getNome().isEmpty()) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("CARTEIRA_INF_NOME"));
		}

		// pode haver carteira que nao tem banco
		// if ((carteira.getBanco() == null)
		// || (carteira.getBanco().getCodigoBanco() == null)
		// || (carteira.getBanco().getCodigoBanco() == 0L)) {
		// throw new ServiceException(
		// GerenciadorMensagem.getMensagem("CARTEIRA_INF_BANCO"));
		// }

		validarCamposPadrao(carteira);

		validarCondominio(carteira);
	}

	@Override
	protected void updateValidate(final Carteira carteira)
			throws ServiceException {
		super.updateValidate(carteira);
		validarCamposPadrao(carteira);
		validarCondominio(carteira);
	}

	private void validarCamposPadrao(final Carteira carteira)
			throws ServiceException {
		if ((carteira.getNome() == null)
				|| carteira.getNome().trim().equals("")) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("CARTEIRA_NOME_OBRIGATORIO"));
		}

		if ((carteira.getDescricao() == null)
				|| carteira.getDescricao().isEmpty()) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("CARTEIRA_DESCRICAO_OBRIGATORIO"));
		}

		if (carteira.getDescricao().length() < Constantes.DESCRICAO_MIN_CARACTERES) {
			throw new ServiceException(GerenciadorMensagem.getMensagem(
					"CARTEIRA_DESCRICAO_MIN_CARACTERES",
					Constantes.DESCRICAO_MIN_CARACTERES));
		}

		if (carteira.getDataCadastro() == null) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("CARTEIRA_DATA_CADASTRO_NULL"));
		}
	}

	private void validarCondominio(final Carteira carteira)
			throws ServiceException {
		if ((carteira.getCondominio() == null)
				|| (carteira.getCondominio().getIdPessoa() == null)) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("CONDOMINIO_NULO"));
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void atualizarCaixa(final MovimentacaoDTO movimentacaoDTO,
			final boolean ehEntrada) throws ServiceException {
		try {
			this.carteiraDAO.atualizarCaixa(movimentacaoDTO, ehEntrada);
		} catch (final DAOException e) {
			getLogger().error("Erro ao atualizar caixa da carteira.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<CarteiraDTO> buscarPorParametros(
			final CondominioDTO condominio, final Boolean ativo)
			throws ServiceException {
		try {
			return this.carteiraDAO.buscarPorParametros(condominio, ativo);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar carteiras por condominio.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void atualizaBoleto(final CarteiraDTO carteira)
			throws ServiceException {
		try {
			this.carteiraDAO.atualizaBoleto(carteira);
		} catch (final DAOException e) {
			getLogger().error("Erro ao atualizar boleto da carteira.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

}
