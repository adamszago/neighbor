package br.com.lphantus.neighbor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.common.LancamentoDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.entity.LancamentoTipo;
import br.com.lphantus.neighbor.repository.ILancamentoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.ILancamentoService;
import br.com.lphantus.neighbor.service.ILancamentoTipoService;
import br.com.lphantus.neighbor.service.IPessoaFisicaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.utils.Constantes;
import br.com.lphantus.neighbor.utils.DateUtil;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class LancamentoServiceImpl extends
		GenericService<Long, LancamentoDTO, Lancamento> implements
		ILancamentoService {

	@Autowired
	private ILancamentoDAO lancamentoDAO;

	@Autowired
	private ILancamentoTipoService lancamentoTipoService;

	@Autowired
	private IPessoaFisicaService pessoaFisicaService;

	private void checagemPadrao(final Lancamento entity)
			throws ServiceException {
		if (entity.getTipoLancamento() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("LANCAMENTO_TIPO_VAZIO"));
		}

		if (entity.getValor() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("LANCAMENTO_VALOR_VAZIO"));
		}

		if (entity.getValor().compareTo(Constantes.DATABASE_MAX_DOUBLE) > 0) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("LANCAMENTO_VALOR_GRANDE"));
		}

		if (entity.getValor().compareTo(BigDecimal.ZERO) < 0) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("LANCAMENTO_VALOR_PEQUENO"));
		}

		if (entity.getData() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("LANCAMENTO_DATA_VAZIO"));
		}

		if (!DateUtil.checkSystemDate(entity.getData())) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("LANCAMENTO_DATA_INTERVALO_INVALIDO"));
		}
	}

	@Override
	protected void saveValidate(final Lancamento entity)
			throws ServiceException {
		super.saveValidate(entity);

		checagemPadrao(entity);

		if ((null == entity.getPessoa())
				|| (null == entity.getPessoa().getIdPessoa())) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("LANCAMENTO_SELECIONE_PESSOA"));
		}

	}

	@Override
	protected void updateValidate(final Lancamento entity)
			throws ServiceException {
		super.updateValidate(entity);
		if ((entity.getFatura() != null) && (entity.isAtivo() == false)) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("LANCAMENTO_FATURA_ATIVO"));
		}

		if ((entity.getFatura() != null) && (entity.isExcluido() == true)) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("LANCAMENTO_FATURA_EXCLUIDO"));
		}
	}

	@Override
	public List<LancamentoDTO> buscarPorCondominio(
			final CondominioDTO condominioUsuarioLogado, final Boolean status)
			throws ServiceException {
		try {
			return this.lancamentoDAO.buscarPorCondominio(
					condominioUsuarioLogado, status);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar lancamentos por condominio.",
					exception);
			throw new ServiceException(
					"Erro ao buscar lancamentos por condominio.", exception);
		}
	}

	@Override
	public List<LancamentoDTO> buscarPorCondominioPagar(
			final CondominioDTO condominioUsuarioLogado, final Boolean status)
			throws ServiceException {
		try {
			return this.lancamentoDAO.buscarPorCondominioPagar(
					condominioUsuarioLogado, status);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar lancamentos por condominio.",
					exception);
			throw new ServiceException(
					"Erro ao buscar lancamentos por condominio.", exception);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void gravarLancamentoEntrada(final LancamentoDTO lancamento,
			final List<PessoaDTO> pessoas) throws ServiceException {
		final LancamentoTipo tipoLancamento = this.lancamentoTipoService
				.findById(1L);
		final Lancamento entidade = LancamentoDTO.Builder.getInstance()
				.createEntity(lancamento);

		entidade.setTipoLancamento(tipoLancamento);

		checagemPadrao(entidade);

		if ((pessoas == null) || pessoas.isEmpty()) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("LANCAMENTO_SELECIONE_PESSOA"));
		}

		this.lancamentoDAO.gravaTodosMoradores(entidade.createDto(), pessoas);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void gravarLancamentoSaida(final LancamentoDTO lancamento,
			final List<PessoaDTO> pessoas) throws ServiceException {
		final LancamentoTipo tipoLancamento = this.lancamentoTipoService
				.findById(2L);
		final Lancamento entidade = LancamentoDTO.Builder.getInstance()
				.createEntity(lancamento);

		entidade.setTipoLancamento(tipoLancamento);

		checagemPadrao(entidade);

		if ((pessoas == null) || pessoas.isEmpty()) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("LANCAMENTO_SELECIONE_PESSOA"));
		}

		this.lancamentoDAO.gravaTodosMoradores(entidade.createDto(), pessoas);
	}

	@Override
	public List<LancamentoDTO> buscarNaoAssociados(
			final PessoaFisicaDTO pessoaSelecionada,
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.lancamentoDAO.buscarNaoAssociados(pessoaSelecionada,
					condominio);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar lancamentos por pessoa.",
					exception);
			throw new ServiceException(
					"Erro ao buscar lancamentos por pessoa.", exception);
		}
	}

	@Override
	public List<LancamentoDTO> buscarNaoAssociadosPagar(
			final PessoaFisicaDTO pessoaSelecionada,
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.lancamentoDAO.buscarNaoAssociadosPagar(
					pessoaSelecionada, condominio);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar lancamentos por pessoa.",
					exception);
			throw new ServiceException(
					"Erro ao buscar lancamentos por pessoa.", exception);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void alterarStatus(final Long id, final boolean novoStatus)
			throws ServiceException {
		try {
			this.lancamentoDAO.alterarStatus(id, novoStatus);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao alterar status.", exception);
			throw new ServiceException("Erro ao alterar status.", exception);
		}
	}

	@Override
	public Map<PessoaDTO, List<LancamentoDTO>> buscarMapaEntradaAtivoPorCondominio(
			final CondominioDTO condominio) throws ServiceException {
		final Map<PessoaDTO, List<LancamentoDTO>> retorno = new HashMap<PessoaDTO, List<LancamentoDTO>>();
		try {
			final List<LancamentoDTO> todos = this.lancamentoDAO
					.buscarNaoAssociados(null, condominio);

			for (final LancamentoDTO lancamento : todos) {
				if (!retorno.containsKey(lancamento.getPessoa())) {
					retorno.put(lancamento.getPessoa(),
							new ArrayList<LancamentoDTO>());
				}
				retorno.get(lancamento.getPessoa()).add(lancamento);
			}

		} catch (final DAOException e) {
			getLogger()
					.error("Erro ao buscar mapa lancamentos ativos por condominio.",
							e);
			throw new ServiceException(e.getMessage(), e);
		}
		return retorno;
	}

	@Override
	public List<LancamentoDTO> buscarPorFatura(final FaturaDTO fatura)
			throws ServiceException {
		try {
			return this.lancamentoDAO.buscarPorFatura(fatura);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar por fatura.", exception);
			throw new ServiceException("Erro ao buscar por fatura.", exception);
		}
	}

	@Override
	public List<LancamentoDTO> buscarPorDuplicata(final DuplicataDTO duplicata)
			throws ServiceException {
		try {
			return this.lancamentoDAO.buscarPorDuplicata(duplicata);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar por duplicata.", exception);
			throw new ServiceException("Erro ao buscar por duplicata.",
					exception);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void desassociarFatura(final List<LancamentoDTO> source)
			throws ServiceException {
		try {
			this.lancamentoDAO.desassociarFatura(source);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao desassociar faturas.", exception);
			throw new ServiceException("Erro ao desassociar faturas.",
					exception);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void associarFatura(final List<LancamentoDTO> lancamentos,
			final FaturaDTO fatura) throws ServiceException {
		try {
			this.lancamentoDAO.associarFatura(lancamentos, fatura);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao associar faturas.", exception);
			throw new ServiceException("Erro ao associar faturas.", exception);
		}
	}

}