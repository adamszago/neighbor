package br.com.lphantus.neighbor.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CentroCustoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.common.RateioDTO;
import br.com.lphantus.neighbor.entity.Movimentacao;
import br.com.lphantus.neighbor.entity.Rateio;
import br.com.lphantus.neighbor.repository.IRateioDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class RateioDAOImpl extends GenericDAOImpl<Long, RateioDTO, Rateio>
		implements IRateioDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<RateioDTO> buscarPorMovimentacao(
			final MovimentacaoDTO movimentacao) throws DAOException {

		final List<RateioDTO> retorno = new ArrayList<RateioDTO>();

		final Query query = getEntityManager().createNamedQuery(
				"Rateio.buscarPorMovimentacao");
		if (null == movimentacao) {
			query.setParameter("idMovimentacao", null);
		} else {
			query.setParameter("idMovimentacao", movimentacao.getId());
		}

		final List<Rateio> itens = new ArrayList<Rateio>();
		try {
			itens.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		for (final Rateio item : itens) {
			retorno.add(item.createDto());
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RateioDTO> buscarPorMesAtual(final CondominioDTO condominio,
			final ConfiguracaoCondominioDTO configuracao,
			final Date selectedDate) throws DAOException {

		// Se a data das faturas do condominio nao for nula, busca todas
		// movimentacoes e rateios para aquela data. Se for nula, busca um mes
		// atras a partir da data atual

		Date dataInicio;
		if (null == condominio) {
			dataInicio = new Date();
		} else {
			if (null == configuracao.getDataFaturas()) {
				dataInicio = new Date();
			} else {
				final Calendar calendarioHoje = new GregorianCalendar();
				calendarioHoje.setTime(selectedDate);
				calendarioHoje.set(Calendar.DAY_OF_MONTH,
						configuracao.getDataFaturas());
				dataInicio = calendarioHoje.getTime();
			}
		}

		final Calendar cal = Calendar.getInstance();
		cal.setTime(dataInicio);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		final Date dataFim = cal.getTime();

		// + "AND movimentacao.ativo = true ";
		final Query query = getEntityManager().createNamedQuery(
				"Rateio.buscarPorMesAtual");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}

		final List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();
		try {
			movimentacoes.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<RateioDTO> retorno = new ArrayList<RateioDTO>();
		retorno.addAll(tratarMovimentacoes(movimentacoes, condominio));

		return retorno;
	}

	private List<RateioDTO> tratarMovimentacoes(
			final List<Movimentacao> movimentacoes,
			final CondominioDTO condominio) {

		final List<RateioDTO> retorno = new ArrayList<RateioDTO>();
		final RateioDTO naoCategorizadoEntrada = criaRateioSemCategoria(condominio);
		final RateioDTO naoCategorizadoSaida = criaRateioSemCategoria(condominio);

		for (final Movimentacao registro : movimentacoes) {

			if (null == registro.getRateios()) {
				if (BigDecimal.ZERO.compareTo(registro.getValor()) < 0) {
					naoCategorizadoEntrada.setValor(naoCategorizadoEntrada
							.getValor().abs().add(registro.getValor().abs()));
				} else {
					naoCategorizadoSaida.setValor(naoCategorizadoSaida
							.getValor().abs().add(registro.getValor().abs()));
				}
			} else {
				BigDecimal totalCategorizado = BigDecimal.ZERO;
				for (final Rateio rateio : registro.getRateios()) {
					retorno.add(rateio.createDto());
					totalCategorizado = totalCategorizado
							.add(rateio.getValor());
				}

				// se nem toda a movimentacao foi categorizada
				if (!totalCategorizado.equals(registro.getValor())) {
					final BigDecimal totalNaoCategorizado = registro.getValor()
							.abs().subtract(totalCategorizado.abs());
					if (BigDecimal.ZERO.compareTo(registro.getValor()) < 0) {
						naoCategorizadoEntrada.setValor(naoCategorizadoEntrada
								.getValor().abs()
								.add(totalNaoCategorizado.abs()));
					} else {
						naoCategorizadoSaida.setValor(naoCategorizadoSaida
								.getValor().abs()
								.add(totalNaoCategorizado.abs()));
					}
				}

			}

		}

		if (!naoCategorizadoEntrada.getValor().equals(BigDecimal.ZERO)) {
			retorno.add(naoCategorizadoEntrada);
		}

		if (!naoCategorizadoSaida.getValor().equals(BigDecimal.ZERO)) {
			// saida eh negativo
			naoCategorizadoSaida.setValor(BigDecimal.ZERO.subtract(
					BigDecimal.ONE).multiply(naoCategorizadoSaida.getValor()));
			retorno.add(naoCategorizadoSaida);
		}

		return retorno;
	}

	private RateioDTO criaRateioSemCategoria(final CondominioDTO condominio) {
		final RateioDTO retorno = new RateioDTO();
		retorno.setCondominio(condominio);
		final CentroCustoDTO centro = new CentroCustoDTO();
		centro.setNome("Nao categorizado");
		retorno.setCentroCusto(centro);
		retorno.setValor(BigDecimal.ZERO);
		return retorno;
	}
}
