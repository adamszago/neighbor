package br.com.lphantus.neighbor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.common.LancamentoDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.entity.Fatura;
import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.repository.IFaturaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IConfiguracaoCondominioService;
import br.com.lphantus.neighbor.service.IDuplicataService;
import br.com.lphantus.neighbor.service.IFaturaService;
import br.com.lphantus.neighbor.service.ILancamentoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.utils.Constantes;
import br.com.lphantus.neighbor.utils.DateUtil;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class FaturaServiceImpl extends GenericService<Long, FaturaDTO, Fatura>
		implements IFaturaService {

	@Autowired
	private IFaturaDAO faturaDAO;

	@Autowired
	private ILancamentoService lancamentoService;

	@Autowired
	private IDuplicataService duplicataService;

	@Autowired
	private IConfiguracaoCondominioService configuracaoCondominioService;

	@Override
	protected void saveValidate(final Fatura entity) throws ServiceException {
		super.saveValidate(entity);
		if (entity == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("FATURA_PARAMETRO_NULO"));
		}

		if (entity.getValor() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("FATURA_VALOR_VAZIO"));
		}

		if (entity.getValor().compareTo(Constantes.DATABASE_MAX_DOUBLE) > 0) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("FATURA_VALOR_GRANDE"));
		}

		if (entity.getValor().compareTo(BigDecimal.ZERO) < 0) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("FATURA_VALOR_PEQUENO"));
		}

		if (entity.getData() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("FATURA_DATA_VAZIO"));
		}

		if (!DateUtil.checkSystemDate(entity.getData())) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("FATURA_DATA_INTERVALO_INVALIDO"));
		}

		if ((entity.getLancamentos() == null)
				|| (entity.getLancamentos().size() == 0)) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("FATURA_SEM_LANCAMENTO"));
		}

		// Trocar pela validação da Moradia

		/*
		 * Long id_morador =
		 * entity.getLancamentos().get(0).getMorador().getIdMorador(); for
		 * (Lancamento lancamento : entity.getLancamentos()) if
		 * (lancamento.getMorador().getIdMorador() != id_morador) throw new
		 * ServiceException
		 * (GerenciadorMensagem.getMensagem("FATURA_LANCAMENTO_MORADOR_DIFERENTE"
		 * ));
		 */
	}

	@Override
	protected void updateValidate(final Fatura entity) throws ServiceException {
		super.updateValidate(entity);
		saveValidate(entity);
	}

	@Override
	public void update(final Fatura entity) throws ServiceException {
		try {
			entity.setValor(BigDecimal.ZERO);
			for (final Lancamento lancamento : entity.getLancamentos()) {
				final Lancamento item = this.lancamentoService
						.findById(lancamento.getId());
				if (null != item) {
					entity.setValor(entity.getValor().add(item.getValor()));
				}
			}

			updateValidate(entity);
			entity.setLancamentos(new ArrayList<Lancamento>());
			this.faturaDAO.update(entity);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		} catch (final ServiceException e) {
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public void save(final Fatura entity) throws ServiceException {
		try {
			saveValidate(entity);
			entity.setLancamentos(new ArrayList<Lancamento>());
			this.faturaDAO.save(entity);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		} catch (final ServiceException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	private void preencherValorPagoAPagar(final List<FaturaDTO> listaFaturas)
			throws ServiceException {
		for (final FaturaDTO item : listaFaturas) {
			BigDecimal valorPago = BigDecimal.ZERO;
			final List<DuplicataDTO> listaDuplicatas = this.duplicataService
					.buscarAtivasPorFatura(item);
			for (final DuplicataDTO duplicata : listaDuplicatas) {
				for (final DuplicataParcelaDTO parcelaDuplicata : duplicata
						.getParcelas()) {
					if (parcelaDuplicata.isQuitada()) {
						valorPago = valorPago.add(parcelaDuplicata.getValor());
					}
				}
			}
			item.setValorPago(valorPago);
			item.setValoraPagar(item.getValor().subtract(valorPago));
		}
	}

	@Override
	public List<FaturaDTO> buscarPorCondominio(final CondominioDTO condominio,
			final Boolean status) throws ServiceException {
		try {
			final List<FaturaDTO> retorno = this.faturaDAO.buscarPorCondominio(
					condominio, status);
			preencherValorPagoAPagar(retorno);
			return retorno;
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar faturas por condominio.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<FaturaDTO> buscarPorCondominioPagar(
			final CondominioDTO condominio, final Boolean status)
			throws ServiceException {
		try {
			return this.faturaDAO.buscarPorCondominioPagar(condominio, status);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar faturas por condominio.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<FaturaDTO> buscarEmAbertoPorPessoa(final PessoaDTO pessoa)
			throws ServiceException {
		try {
			return this.faturaDAO.buscarEmAbertoPorPessoa(pessoa);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar faturas por pessoa.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void gerarFaturasAberto(final CondominioDTO condominio)
			throws ServiceException {
		try {
			final Map<PessoaDTO, List<LancamentoDTO>> mapaLancamentos = this.lancamentoService
					.buscarMapaEntradaAtivoPorCondominio(condominio);

			final ConfiguracaoCondominioDTO configuracao = this.configuracaoCondominioService
					.buscarPorCondominio(condominio);

			for (final PessoaDTO pessoa : mapaLancamentos.keySet()) {
				final FaturaDTO fatura = new FaturaDTO();
				fatura.setCondominio(condominio);

				final List<LancamentoDTO> lancamentosPessoa = mapaLancamentos
						.get(pessoa);

				for (final LancamentoDTO lancamento : lancamentosPessoa) {
					adicionaLancamentoFatura(fatura, lancamento);
				}

				fatura.setData(dataMensal(configuracao.getDataFaturas()));
				fatura.setDataCadastro(new Date());
				fatura.setNome("Fatura automatica");

				// final List<LancamentoDTO> listaLancamentosAtualizar = fatura
				// .getLancamentos();
				// fatura.setLancamentos(null);

				final Fatura entidade = FaturaDTO.Builder.getInstance()
						.createEntity(fatura);

				this.faturaDAO.save(entidade);

				for (final LancamentoDTO lancamento : fatura.getLancamentos()) {
					final Lancamento entidadeLancamento = this.lancamentoService
							.findById(lancamento.getId());
					entidadeLancamento.setFatura(entidade);
					this.lancamentoService.update(entidadeLancamento);
				}

			}
		} catch (final DAOException e) {
			getLogger().debug("Erro ao gravar faturas automaticas.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	private Date dataMensal(final Integer dataFaturas) {
		final Calendar calendarioHoje = new GregorianCalendar();
		calendarioHoje.setTime(new Date());
		calendarioHoje.add(Calendar.MONTH, 1);
		calendarioHoje.set(Calendar.DAY_OF_MONTH, dataFaturas);
		return calendarioHoje.getTime();
	}

	@Override
	public void adicionaLancamentoFatura(final FaturaDTO entity,
			final LancamentoDTO lancamento) {

		if (entity.getLancamentos() == null) {
			entity.setLancamentos(new ArrayList<LancamentoDTO>());
		}

		if (!entity.getLancamentos().contains(lancamento)) {
			entity.getLancamentos().add(lancamento);
			if (null == entity.getValor()) {
				entity.setValor(lancamento.getValor());
			} else {
				BigDecimal soma = BigDecimal.ZERO;
				for (final LancamentoDTO lancamentoLista : entity
						.getLancamentos()) {
					soma = soma.add(lancamentoLista.getValor());
				}
				entity.setValor(soma);
			}
		}
	}

	@Override
	public void removeLancamentoFatura(final FaturaDTO entity,
			final LancamentoDTO lancamento) {
		entity.setValor(entity.getValor().subtract(lancamento.getValor()));
		entity.getLancamentos().remove(lancamento);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void alterarStatus(final Long id, final boolean novoStatus)
			throws ServiceException {
		try {
			this.faturaDAO.alterarStatus(id, novoStatus);
		} catch (final DAOException e) {
			getLogger().debug("Erro ao alterar status do registro.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<FaturaDTO> buscarFaturasSemDuplicata(
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.faturaDAO.buscarFaturasSemDuplicata(condominio);
		} catch (final DAOException e) {
			getLogger().debug("Erro ao alterar status do registro.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

}