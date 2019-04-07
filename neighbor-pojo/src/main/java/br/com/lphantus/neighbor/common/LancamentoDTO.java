package br.com.lphantus.neighbor.common;

import java.math.BigDecimal;
import java.util.Date;

import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.entity.Pessoa;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;

public class LancamentoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private BigDecimal valor;
	private Date data;
	private Date dataCadastro;
	private String obs;
	private boolean ativo = true;
	private boolean excluido = false;
	private LancamentoTipoDTO tipoLancamento;
	private PessoaDTO pessoa;
	private FaturaDTO fatura;
	private CondominioDTO condominio;
	private CentroCustoDTO centroCusto;
	private CarteiraDTO carteira;

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
	 * @return the tipoLancamento
	 */
	public LancamentoTipoDTO getTipoLancamento() {
		return this.tipoLancamento;
	}

	/**
	 * @param tipoLancamento
	 *            the tipoLancamento to set
	 */
	public void setTipoLancamento(final LancamentoTipoDTO tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	/**
	 * @return the pessoa
	 */
	public PessoaDTO getPessoa() {
		return this.pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(final PessoaDTO pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the fatura
	 */
	public FaturaDTO getFatura() {
		return this.fatura;
	}

	/**
	 * @param fatura
	 *            the fatura to set
	 */
	public void setFatura(final FaturaDTO fatura) {
		this.fatura = fatura;
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

	/**
	 * @return the centroCusto
	 */
	public CentroCustoDTO getCentroCusto() {
		return this.centroCusto;
	}

	/**
	 * @param centroCusto
	 *            the centroCusto to set
	 */
	public void setCentroCusto(final CentroCustoDTO centroCusto) {
		this.centroCusto = centroCusto;
	}

	/**
	 * @return the carteira
	 */
	public CarteiraDTO getCarteira() {
		return carteira;
	}

	/**
	 * @param carteira
	 *            the carteira to set
	 */
	public void setCarteira(CarteiraDTO carteira) {
		this.carteira = carteira;
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
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
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
		if (!(obj instanceof LancamentoDTO)) {
			return false;
		}
		final LancamentoDTO other = (LancamentoDTO) obj;
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
		return "LancamentoDTO [id=" + this.id + ", valor=" + this.valor + ", data=" + this.data + ", dataCadastro=" + this.dataCadastro + ", obs=" + this.obs + ", ativo="
				+ this.ativo + ", excluido=" + this.excluido + ", tipoLancamento=" + this.tipoLancamento + ", pessoa=" + this.pessoa
				// + ", fatura=" + this.fatura
				+ "]";
	}

	public static class Builder extends DTOBuilder<LancamentoDTO, Lancamento> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public LancamentoDTO create(final Lancamento lancamento) {
			final LancamentoDTO dto = new LancamentoDTO();
			dto.setAtivo(lancamento.isAtivo());
			dto.setData(lancamento.getData());
			dto.setDataCadastro(lancamento.getDataCadastro());
			dto.setExcluido(lancamento.isExcluido());

			if (null != lancamento.getFatura()) {
				dto.setFatura(lancamento.getFatura().createDto());
			}

			dto.setId(lancamento.getId());

			if (null != lancamento.getCondominio()) {
				dto.setCondominio(lancamento.getCondominio().createDto());
			}

			if (null != lancamento.getPessoa()) {
				if (lancamento.getPessoa() instanceof PessoaFisica) {
					dto.setPessoa(PessoaFisicaDTO.Builder.getInstance().create((PessoaFisica) lancamento.getPessoa()));
				} else if (lancamento.getPessoa() instanceof PessoaJuridica) {
					dto.setPessoa(PessoaJuridicaDTO.Builder.getInstance().create((PessoaJuridica) lancamento.getPessoa()));
				}
			}

			dto.setNome(lancamento.getNome());
			dto.setObs(lancamento.getObs());
			dto.setValor(lancamento.getValor());

			if (null != lancamento.getCentroCusto()) {
				dto.setCentroCusto(lancamento.getCentroCusto().createDto());
			}

			if (null != lancamento.getTipoLancamento()) {
				dto.setTipoLancamento(lancamento.getTipoLancamento().createDto());
			}

			if (null != lancamento.getCarteira()) {
				dto.setCarteira(lancamento.getCarteira().createDto());
			}
			return dto;
		}

		@Override
		public Lancamento createEntity(final LancamentoDTO outer) {
			final Lancamento entidade = new Lancamento();
			entidade.setAtivo(outer.isAtivo());
			entidade.setData(outer.getData());
			entidade.setDataCadastro(outer.getDataCadastro());
			entidade.setExcluido(outer.isExcluido());

			// if (null != outer.getFatura()) {
			// entidade.setFatura(FaturaDTO.Builder.getInstance()
			// .createEntity(outer.getFatura()));
			// }

			entidade.setId(outer.getId());

			if (null != outer.getPessoa()) {
				entidade.setPessoa(new Pessoa());
				entidade.getPessoa().setIdPessoa(outer.getPessoa().getIdPessoa());
			}

			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance().createEntity(outer.getCondominio()));
			}

			entidade.setNome(outer.getNome());
			entidade.setObs(outer.getObs());
			entidade.setValor(outer.getValor());

			if (null != outer.getCentroCusto()) {
				entidade.setCentroCusto(CentroCustoDTO.Builder.getInstance().createEntity(outer.getCentroCusto()));
			}

			if (null != outer.getTipoLancamento()) {
				entidade.setTipoLancamento(LancamentoTipoDTO.Builder.getInstance().createEntity(outer.getTipoLancamento()));
			}

			if (null != outer.getCarteira()) {
				entidade.setCarteira(CarteiraDTO.Builder.getInstance().createEntity(outer.getCarteira()));
			}

			return entidade;
		}
	}

}
