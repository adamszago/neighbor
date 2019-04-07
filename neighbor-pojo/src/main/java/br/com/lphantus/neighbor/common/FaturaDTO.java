package br.com.lphantus.neighbor.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.lphantus.neighbor.entity.Fatura;
import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;

public class FaturaDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private BigDecimal valor;
	private BigDecimal valorPago;
	private BigDecimal valoraPagar;
	private Date data;
	private Date dataCadastro;
	private Date dataPagamento;
	private boolean aberto;
	private String obs;
	private boolean ativo = true;
	private boolean excluido = false;
	private List<LancamentoDTO> lancamentos;
	private List<DuplicataDTO> duplicatas;
	private CondominioDTO condominio;

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(final String nome) {
		this.nome = nome;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return this.valor;
	}

	/**
	 * @param valor
	 *            the valor to set
	 */
	public void setValor(final BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the valorPago
	 */
	public BigDecimal getValorPago() {
		return this.valorPago;
	}

	/**
	 * @param valorPago
	 *            the valorPago to set
	 */
	public void setValorPago(final BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	/**
	 * @return the valoraPagar
	 */
	public BigDecimal getValoraPagar() {
		return this.valoraPagar;
	}

	/**
	 * @param valoraPagar
	 *            the valoraPagar to set
	 */
	public void setValoraPagar(final BigDecimal valoraPagar) {
		this.valoraPagar = valoraPagar;
	}

	/**
	 * @return the data
	 */
	public Date getData() {
		return this.data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(final Date data) {
		this.data = data;
	}

	/**
	 * @return the dataCadastro
	 */
	public Date getDataCadastro() {
		return this.dataCadastro;
	}

	/**
	 * @param dataCadastro
	 *            the dataCadastro to set
	 */
	public void setDataCadastro(final Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return this.dataPagamento;
	}

	/**
	 * @param dataPagamento
	 *            the dataPagamento to set
	 */
	public void setDataPagamento(final Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @return the aberto
	 */
	public boolean isAberto() {
		return this.aberto;
	}

	/**
	 * @param aberto
	 *            the aberto to set
	 */
	public void setAberto(final boolean aberto) {
		this.aberto = aberto;
	}

	/**
	 * @return the obs
	 */
	public String getObs() {
		return this.obs;
	}

	/**
	 * @param obs
	 *            the obs to set
	 */
	public void setObs(final String obs) {
		this.obs = obs;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return this.ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(final boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the excluido
	 */
	public boolean isExcluido() {
		return this.excluido;
	}

	/**
	 * @param excluido
	 *            the excluido to set
	 */
	public void setExcluido(final boolean excluido) {
		this.excluido = excluido;
	}

	/**
	 * @return the lancamentos
	 */
	public List<LancamentoDTO> getLancamentos() {
		return this.lancamentos;
	}

	/**
	 * @param lancamentos
	 *            the lancamentos to set
	 */
	public void setLancamentos(final List<LancamentoDTO> lancamentos) {
		this.lancamentos = lancamentos;
	}

	/**
	 * @return the duplicatas
	 */
	public List<DuplicataDTO> getDuplicatas() {
		return this.duplicatas;
	}

	/**
	 * @param duplicatas
	 *            the duplicatas to set
	 */
	public void setDuplicatas(final List<DuplicataDTO> duplicatas) {
		this.duplicatas = duplicatas;
	}

	/**
	 * @return the condominio
	 */
	public CondominioDTO getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final CondominioDTO condominio) {
		this.condominio = condominio;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FaturaDTO)) {
			return false;
		}
		final FaturaDTO other = (FaturaDTO) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FaturaDTO [id=" + this.id + ", nome=" + this.nome + ", valor="
				+ this.valor + ", valorPago=" + this.valorPago
				+ ", valoraPagar=" + this.valoraPagar + ", data=" + this.data
				+ ", dataCadastro=" + this.dataCadastro + ", dataPagamento="
				+ this.dataPagamento + ", aberto=" + this.aberto + ", obs="
				+ this.obs + ", ativo=" + this.ativo + ", excluido="
				+ this.excluido + ", lancamentos=" + this.lancamentos
				+ ", duplicatas=" + this.duplicatas + ", condominio="
				+ this.condominio + "]";
	}

	public static class Builder extends DTOBuilder<FaturaDTO, Fatura> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public FaturaDTO create(final Fatura fatura) {
			final FaturaDTO dto = new FaturaDTO();
			dto.setAberto(fatura.isAberto());
			dto.setAtivo(fatura.getAtivo());
			dto.setCondominio(fatura.getCondominio().createDto());
			dto.setData(fatura.getData());
			dto.setDataCadastro(fatura.getDataCadastro());
			dto.setDataPagamento(fatura.getDataPagamento());
			dto.setExcluido(fatura.getExcluido());
			dto.setId(fatura.getId());
			dto.setNome(fatura.getNome());
			dto.setObs(fatura.getObs());
			dto.setValor(fatura.getValor());
			dto.setValoraPagar(fatura.getValoraPagar());
			dto.setValorPago(fatura.getValorPago());

			// TODO: review
			// dto.setDuplicatas(DuplicataDTO.Builder.getInstance().createList(
			// fatura.getDuplicatas()));

			dto.setLancamentos(new ArrayList<LancamentoDTO>());
			criarLancamentos(dto, fatura);

			return dto;
		}

		private void criarLancamentos(final FaturaDTO retorno,
				final Fatura fatura) {
			for (final Lancamento lancamento : fatura.getLancamentos()) {
				final LancamentoDTO dto = new LancamentoDTO();
				dto.setAtivo(lancamento.isAtivo());
				dto.setData(lancamento.getData());
				dto.setDataCadastro(lancamento.getDataCadastro());
				dto.setExcluido(lancamento.isExcluido());
				dto.setId(lancamento.getId());
				dto.setFatura(retorno);

				if (null != lancamento.getCondominio()) {
					dto.setCondominio(lancamento.getCondominio().createDto());
				}

				if (null != lancamento.getPessoa()) {
					if (lancamento.getPessoa() instanceof PessoaFisica) {
						dto.setPessoa(PessoaFisicaDTO.Builder.getInstance()
								.create((PessoaFisica) lancamento.getPessoa()));
					} else if (lancamento.getPessoa() instanceof PessoaJuridica) {
						dto.setPessoa(PessoaJuridicaDTO.Builder
								.getInstance()
								.create((PessoaJuridica) lancamento.getPessoa()));
					}
				}

				dto.setNome(lancamento.getNome());
				dto.setObs(lancamento.getObs());
				dto.setValor(lancamento.getValor());

				if (null != lancamento.getTipoLancamento()) {
					dto.setTipoLancamento(lancamento.getTipoLancamento()
							.createDto());
				}

				retorno.getLancamentos().add(dto);
			}
		}

		@Override
		public Fatura createEntity(final FaturaDTO outer) {
			final Fatura entidade = new Fatura();
			entidade.setAberto(outer.isAberto());
			entidade.setAtivo(outer.isAtivo());
			entidade.setCondominio(CondominioDTO.Builder.getInstance()
					.createEntity(outer.getCondominio()));
			entidade.setData(outer.getData());
			entidade.setDataCadastro(outer.getDataCadastro());
			entidade.setDataPagamento(outer.getDataPagamento());
			entidade.setExcluido(outer.isExcluido());
			entidade.setId(outer.getId());
			entidade.setNome(outer.getNome());
			entidade.setObs(outer.getObs());
			entidade.setValor(outer.getValor());
			entidade.setValoraPagar(outer.getValoraPagar());
			entidade.setValorPago(outer.getValorPago());

			entidade.setDuplicatas(DuplicataDTO.Builder.getInstance()
					.createListEntity(outer.getDuplicatas()));

			entidade.setLancamentos(LancamentoDTO.Builder.getInstance()
					.createListEntity(outer.getLancamentos()));
			for (final Lancamento lancamento : entidade.getLancamentos()) {
				lancamento.setFatura(entidade);
			}

			return entidade;
		}
	}

}
