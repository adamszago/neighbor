package br.com.lphantus.neighbor.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CentroCustoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.common.LancamentoDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.common.RateioDTO;
import br.com.lphantus.neighbor.entity.Historico;
import br.com.lphantus.neighbor.entity.Movimentacao;
import br.com.lphantus.neighbor.entity.Rateio;
import br.com.lphantus.neighbor.repository.IMovimentacaoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.ICarteiraService;
import br.com.lphantus.neighbor.service.IDuplicataParcelaService;
import br.com.lphantus.neighbor.service.IFaturaService;
import br.com.lphantus.neighbor.service.IHistoricoService;
import br.com.lphantus.neighbor.service.ILancamentoService;
import br.com.lphantus.neighbor.service.IMovimentacaoService;
import br.com.lphantus.neighbor.service.IRateioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.utils.Constantes;
import br.com.lphantus.neighbor.utils.DateUtil;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MovimentacaoServiceImpl extends GenericService<Long, MovimentacaoDTO, Movimentacao> implements IMovimentacaoService {

	@Autowired
	private IMovimentacaoDAO movimentacaoDAO;

	@Autowired
	private ILancamentoService lancamentoService;

	@Autowired
	private IRateioService rateioService;

	@Autowired
	private IDuplicataParcelaService duplicataParcelaService;

	@Autowired
	private ICarteiraService carteiraService;

	@Autowired
	private IFaturaService faturaService;

	@Autowired
	private IHistoricoService historicoService;

	@Override
	protected void saveValidate(final Movimentacao entity) throws ServiceException {
		super.saveValidate(entity);

		// if (entity.getId() != null) {
		// throw new ServiceException(
		// GerenciadorMensagem.getMensagem("ID_NOTNULL"));
		// }

		if (entity.getValor() == null) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_VALOR_VAZIO"));
		}

		// somente valor de entrada tem duplicatas
		if (entity.getDuplicatas() != null && !entity.getDuplicatas().isEmpty()){
			if ( entity.getValor().compareTo(Constantes.DATABASE_MAX_DOUBLE) > 0) {
				throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_VALOR_GRANDE"));
			}
			
			if (entity.getValor().compareTo(BigDecimal.ZERO) < 0) {
				throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_VALOR_PEQUENO"));
			}
		}

		if (entity.getDataMovimentacao() == null) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_DATA_VAZIO"));
		}

		if (!DateUtil.checkSystemDate(entity.getDataMovimentacao())) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_DATA_INTERVALO_INVALIDO"));
		}

		if ((entity.getCarteira() == null) || (entity.getCarteira().getId() == null)) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_CARTEIRA_NULL"));
		}
		
		entity.setDataCadastro(new Date());
	}

	@Override
	protected void updateValidate(final Movimentacao entity) throws ServiceException {
		super.updateValidate(entity);
		if (entity.getId() == null) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("ID_NULO"));
		}

		if (entity.getValor() == null) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_VALOR_VAZIO"));
		}

		if (entity.getValor().compareTo(Constantes.DATABASE_MAX_DOUBLE) > 0) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_VALOR_GRANDE"));
		}

		if (entity.getValor().compareTo(BigDecimal.ZERO) < 0) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_VALOR_PEQUENO"));
		}

		if (entity.getDataMovimentacao() == null) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_DATA_VAZIO"));
		}

		if (!DateUtil.checkSystemDate(entity.getDataMovimentacao())) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_DATA_INTERVALO_INVALIDO"));
		}

		if ((entity.getCarteira() == null) || (entity.getCarteira().getId() == null)) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("MOVIMENTACAO_CARTEIRA_NULL"));
		}
	}

	@Override
	public List<MovimentacaoDTO> buscarPorCondominio(final CondominioDTO condominio) throws ServiceException {
		try {
			return this.movimentacaoDAO.buscarPorCondominio(condominio);
		} catch (final DAOException exception) {
			getLogger().debug("Erro ao buscar movimentacoes do condominio.", exception);
			throw new ServiceException(exception.getMessage());
		}
	}

	private List<RateioDTO> geraRateios(LancamentoDTO lancamentoPagar) {
		final RateioDTO novo = new RateioDTO();
		novo.setCentroCusto(lancamentoPagar.getCentroCusto());
		novo.setCondominio(lancamentoPagar.getCondominio());
		novo.setValor(lancamentoPagar.getValor());

		List<RateioDTO> lista = new ArrayList<RateioDTO>();
		lista.add(novo);
		return lista;
	}

	private List<RateioDTO> geraRateios(final DuplicataDTO duplicata) throws ServiceException {
		// cria um mapa cuja chave eh o centro de custo, e cujo item eh uma
		// lista de lancamentos
		final Map<CentroCustoDTO, BigDecimal> mapaCentroValor = new HashMap<CentroCustoDTO, BigDecimal>();
		final List<RateioDTO> rateios = new ArrayList<RateioDTO>();

		final List<LancamentoDTO> lancamentos = this.lancamentoService.buscarPorDuplicata(duplicata);
		for (final LancamentoDTO lancamento : lancamentos) {
			BigDecimal adicionar;
			if (mapaCentroValor.containsKey(lancamento.getCentroCusto())) {
				adicionar = mapaCentroValor.remove(lancamento.getCentroCusto());
			} else {
				adicionar = BigDecimal.ZERO;
			}
			mapaCentroValor.put(lancamento.getCentroCusto(), adicionar.add(lancamento.getValor()));
		}

		// para cada centro de custo, cria os rateios somando os valores dos
		// lancamentos
		for (final CentroCustoDTO centro : mapaCentroValor.keySet()) {
			final RateioDTO novo = new RateioDTO();
			novo.setCentroCusto(centro);
			novo.setCondominio(duplicata.getCondominio());
			novo.setValor(mapaCentroValor.get(centro));
			rateios.add(novo);
		}

		return rateios;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void finalizarBaixa(final MovimentacaoDTO movimentacao, final DuplicataParcelaDTO parcelaBaixa) throws ServiceException {

		if (null == parcelaBaixa) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("SELECIONE_PARCELA_BAIXA"));
		}

		if ((null == movimentacao.getCarteira()) || (movimentacao.getCarteira().getId() == null) || (movimentacao.getCarteira().getId() == 0L)) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("SELECIONE_CARTEIRA_BAIXA"));
		}

		if (null == movimentacao.getDataMovimentacao()) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("SELECIONE_DATA_BAIXA"));
		}

		final DuplicataDTO duplicataOrigem = parcelaBaixa.getDuplicata();

		// gera uma lista de rateios
		final List<RateioDTO> lista = geraRateios(duplicataOrigem);

		// cria a movimentacao
		final Movimentacao entidade = MovimentacaoDTO.Builder.getInstance().createEntity(movimentacao);
		save(entidade);

		// salva cada rateio
		for (final RateioDTO item : lista) {
			final Rateio rateioEntity = RateioDTO.Builder.getInstance().createEntity(item);
			rateioEntity.setMovimentacao(entidade);

			// calcula o valor do rateio em função do item da parcela
			final BigDecimal percentagem = item.getValor().divide(duplicataOrigem.getFatura().getValor(), 20, RoundingMode.HALF_DOWN);
			rateioEntity.setValor(percentagem.multiply(parcelaBaixa.getValor()));
			this.rateioService.save(rateioEntity);
		}

		this.duplicataParcelaService.baixarParcela(parcelaBaixa);
		this.carteiraService.atualizarCaixa(entidade.createDto(), Boolean.TRUE);
	}

	@Override
	public void finalizarBaixaLancamentoPagar(LancamentoDTO lancamentoPagar) throws ServiceException {
		if ((null == lancamentoPagar.getCarteira()) || (lancamentoPagar.getCarteira().getId() == null) 
				|| (lancamentoPagar.getCarteira().getId() == 0L)) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("SELECIONE_CARTEIRA_BAIXA"));
		}

		if (null == lancamentoPagar.getData()) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("SELECIONE_DATA_BAIXA"));
		}

		// gera uma lista de rateios
		final Date dataHora = new Date();
		final List<RateioDTO> lista = geraRateios(lancamentoPagar);

		// cria a movimentacao
		MovimentacaoDTO movimentacao = new MovimentacaoDTO();
		movimentacao.setLancamento(lancamentoPagar);
		movimentacao.setAtivo(Boolean.TRUE);
		movimentacao.setCarteira(lancamentoPagar.getCarteira());
		movimentacao.setDataCadastro(dataHora);
		movimentacao.setDataMovimentacao(lancamentoPagar.getData());
		movimentacao.setExcluido(Boolean.FALSE);
		movimentacao.setQuitou(Boolean.FALSE);
		movimentacao.setValor(BigDecimal.ZERO.subtract(BigDecimal.ONE).multiply(lancamentoPagar.getValor()));

		// salva a movimentacao
		final Movimentacao entidade = MovimentacaoDTO.Builder.getInstance().createEntity(movimentacao);
		save(entidade);

		// salva cada rateio
		for (final RateioDTO item : lista) {
			final Rateio rateioEntity = RateioDTO.Builder.getInstance().createEntity(item);
			rateioEntity.setMovimentacao(entidade);

			// calcula o valor do rateio em função do item da parcela
			final BigDecimal percentagem = item.getValor().divide(lancamentoPagar.getValor(), 20, RoundingMode.HALF_DOWN);
			rateioEntity.setValor(percentagem.multiply(lancamentoPagar.getValor()));
			this.rateioService.save(rateioEntity);
		}

		this.carteiraService.atualizarCaixa(entidade.createDto(), Boolean.TRUE);

		// salva historico
		Historico historico = new Historico();
		historico.setAcaoExecutada(String.format("Registro movimentacao de lancamento a pagar, valor de R$ \"%s\", carteira \"%s\".", 
			lancamentoPagar.getValor().toString(), lancamentoPagar.getCarteira().getNome()));
		historico.setCondominio(lancamentoPagar.getCondominio().getPessoa().getNome());
		historico.setIdCondominio(lancamentoPagar.getCondominio().getPessoa().getIdPessoa());
		historico.setDataHoraAcao(dataHora);
		historico.setUsuario("sistema");
		historicoService.save(historico);
	}

}
