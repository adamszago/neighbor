package br.com.lphantus.neighbor.common;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.lphantus.neighbor.entity.Movimentacao;

public class MovimentacaoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String textoLivre;
	private BigDecimal valor;
	private Date dataMovimentacao;
	private Date dataCadastro;
	private CarteiraDTO carteira;
	private boolean ativo = true;
	private boolean excluido = false;
	private List<DuplicataDTO> duplicatas;
	private LancamentoDTO lancamento;
	private boolean quitou;

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
	 * @return the textoLivre
	 */
	public String getTextoLivre() {
		return this.textoLivre;
	}

	/**
	 * @param textoLivre
	 *            the textoLivre to set
	 */
	public void setTextoLivre(final String textoLivre) {
		this.textoLivre = textoLivre;
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
	 * @return the dataMovimentacao
	 */
	public Date getDataMovimentacao() {
		return this.dataMovimentacao;
	}

	/**
	 * @param dataMovimentacao
	 *            the dataMovimentacao to set
	 */
	public void setDataMovimentacao(final Date dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
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
	 * @return the carteira
	 */
	public CarteiraDTO getCarteira() {
		return this.carteira;
	}

	/**
	 * @param carteira
	 *            the carteira to set
	 */
	public void setCarteira(final CarteiraDTO carteira) {
		this.carteira = carteira;
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
	 * @return the quitou
	 */
	public boolean isQuitou() {
		return this.quitou;
	}

	/**
	 * @param quitou
	 *            the quitou to set
	 */
	public void setQuitou(final boolean quitou) {
		this.quitou = quitou;
	}

	/**
	 * @return the lancamento
	 */
	public LancamentoDTO getLancamento() {
		return lancamento;
	}

	/**
	 * @param lancamento
	 *            the lancamento to set
	 */
	public void setLancamento(LancamentoDTO lancamento) {
		this.lancamento = lancamento;
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
		if (!(obj instanceof MovimentacaoDTO)) {
			return false;
		}
		final MovimentacaoDTO other = (MovimentacaoDTO) obj;
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
		return "MovimentacaoDTO [id=" + this.id + ", textoLivre=" + this.textoLivre + ", valor=" + this.valor + ", dataMovimentacao=" + this.dataMovimentacao + ", dataCadastro="
				+ this.dataCadastro + ", carteira=" + this.carteira + ", ativo=" + this.ativo + ", excluido=" + this.excluido + ", duplicatas=" + this.duplicatas + ", quitou="
				+ this.quitou + "]";
	}

	public static class Builder extends DTOBuilder<MovimentacaoDTO, Movimentacao> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public MovimentacaoDTO create(final Movimentacao movimentacao) {
			final MovimentacaoDTO dto = new MovimentacaoDTO();

			dto.setAtivo(movimentacao.isAtivo());
			if (null != movimentacao.getCarteira()) {
				dto.setCarteira(movimentacao.getCarteira().createDto());
			}
			if (null != movimentacao.getLancamento()) {
				dto.setLancamento(movimentacao.getLancamento().createDto());
			}
			dto.setDataCadastro(movimentacao.getDataCadastro());
			dto.setDataMovimentacao(movimentacao.getDataMovimentacao());
			dto.setExcluido(movimentacao.isExcluido());
			dto.setId(movimentacao.getId());
			dto.setValor(movimentacao.getValor());
			dto.setQuitou(movimentacao.isQuitou());
			dto.setTextoLivre(movimentacao.getTextoLivre());

			dto.setDuplicatas(DuplicataDTO.Builder.getInstance().createList(movimentacao.getDuplicatas()));

			return dto;
		}

		@Override
		public Movimentacao createEntity(final MovimentacaoDTO outer) {
			final Movimentacao entidade = new Movimentacao();

			entidade.setAtivo(outer.isAtivo());
			if (null != outer.getCarteira()) {
				entidade.setCarteira(CarteiraDTO.Builder.getInstance().createEntity(outer.getCarteira()));
			}
			entidade.setDataCadastro(outer.getDataCadastro());
			entidade.setDataMovimentacao(outer.getDataMovimentacao());
			entidade.setExcluido(outer.isExcluido());
			entidade.setId(outer.getId());
			entidade.setValor(outer.getValor());
			entidade.setTextoLivre(outer.getTextoLivre());
			if (null != outer.getLancamento()) {
				entidade.setLancamento(LancamentoDTO.Builder.getInstance().createEntity(outer.getLancamento()));
			}

			entidade.setDuplicatas(DuplicataDTO.Builder.getInstance().createListEntity(outer.getDuplicatas()));
			return entidade;
		}
	}

}
