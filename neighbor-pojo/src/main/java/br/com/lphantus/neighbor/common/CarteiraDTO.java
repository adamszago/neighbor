package br.com.lphantus.neighbor.common;

import java.math.BigDecimal;
import java.util.Date;

import br.com.lphantus.neighbor.entity.Carteira;

public class CarteiraDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String descricao;
	private BigDecimal saldo;
	private boolean ativo = true;
	private boolean excluido = false;
	private CondominioDTO condominio;
	private String banco;
	private Date dataCadastro;
	private String numeroConta;
	private String digitoConta;
	private String numeroAgencia;
	private String digitoAgencia;
	private String nossoNumero;
	private String digitoNossoNumero;
	private String numeroCarteira;

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
	 * @return the descricao
	 */
	public String getDescricao() {
		return this.descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the saldo
	 */
	public BigDecimal getSaldo() {
		return this.saldo;
	}

	/**
	 * @param saldo
	 *            the saldo to set
	 */
	public void setSaldo(final BigDecimal saldo) {
		this.saldo = saldo;
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
	 * @return the banco
	 */
	public String getBanco() {
		return this.banco;
	}

	/**
	 * @param banco
	 *            the banco to set
	 */
	public void setBanco(final String banco) {
		this.banco = banco;
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
	 * @return the numeroConta
	 */
	public String getNumeroConta() {
		return this.numeroConta;
	}

	/**
	 * @param numeroConta
	 *            the numeroConta to set
	 */
	public void setNumeroConta(final String numeroConta) {
		this.numeroConta = numeroConta;
	}

	/**
	 * @return the digitoConta
	 */
	public String getDigitoConta() {
		return this.digitoConta;
	}

	/**
	 * @param digitoConta
	 *            the digitoConta to set
	 */
	public void setDigitoConta(final String digitoConta) {
		this.digitoConta = digitoConta;
	}

	/**
	 * @return the numeroAgencia
	 */
	public String getNumeroAgencia() {
		return this.numeroAgencia;
	}

	/**
	 * @param numeroAgencia
	 *            the numeroAgencia to set
	 */
	public void setNumeroAgencia(final String numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	/**
	 * @return the digitoAgencia
	 */
	public String getDigitoAgencia() {
		return this.digitoAgencia;
	}

	/**
	 * @param digitoAgencia
	 *            the digitoAgencia to set
	 */
	public void setDigitoAgencia(final String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	/**
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		return this.nossoNumero;
	}

	/**
	 * @param nossoNumero
	 *            the nossoNumero to set
	 */
	public void setNossoNumero(final String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	/**
	 * @return the digitoNossoNumero
	 */
	public String getDigitoNossoNumero() {
		return this.digitoNossoNumero;
	}

	/**
	 * @param digitoNossoNumero
	 *            the digitoNossoNumero to set
	 */
	public void setDigitoNossoNumero(final String digitoNossoNumero) {
		this.digitoNossoNumero = digitoNossoNumero;
	}

	/**
	 * @return the numeroCarteira
	 */
	public String getNumeroCarteira() {
		return this.numeroCarteira;
	}

	/**
	 * @param numeroCarteira
	 *            the numeroCarteira to set
	 */
	public void setNumeroCarteira(final String numeroCarteira) {
		this.numeroCarteira = numeroCarteira;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CarteiraDTO other = (CarteiraDTO) obj;
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
		return "CarteiraDTO [id=" + this.id + ", nome=" + this.nome
				+ ", descricao=" + this.descricao + ", saldo=" + this.saldo
				+ ", ativo=" + this.ativo + ", excluido=" + this.excluido
				+ ", condominio=" + this.condominio + ", banco=" + this.banco
				+ ", dataCadastro=" + this.dataCadastro + ", numeroConta="
				+ this.numeroConta + ", digitoConta=" + this.digitoConta
				+ ", numeroAgencia=" + this.numeroAgencia + ", digitoAgencia="
				+ this.digitoAgencia + ", nossoNumero=" + this.nossoNumero
				+ ", digitoNossoNumero=" + this.digitoNossoNumero
				+ ", numeroCarteira=" + this.numeroCarteira + "]";
	}

	public static class Builder extends DTOBuilder<CarteiraDTO, Carteira> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public CarteiraDTO create(final Carteira carteira) {
			final CarteiraDTO dto = new CarteiraDTO();

			dto.setAtivo(carteira.isAtivo());

			if (null != carteira.getCondominio()) {
				dto.setCondominio(carteira.getCondominio().createDto());
			}

			dto.setDataCadastro(carteira.getDataCadastro());
			dto.setDescricao(carteira.getDescricao());
			dto.setExcluido(carteira.isExcluido());
			dto.setId(carteira.getId());
			dto.setNome(carteira.getNome());
			dto.setSaldo(carteira.getSaldo());

			dto.setBanco(carteira.getBanco());
			dto.setNumeroConta(carteira.getNumeroConta());
			dto.setDigitoConta(carteira.getDigitoConta());
			dto.setNumeroAgencia(carteira.getNumeroAgencia());
			dto.setDigitoAgencia(carteira.getDigitoAgencia());
			dto.setNossoNumero(carteira.getNossoNumero());
			dto.setDigitoNossoNumero(carteira.getDigitoNossoNumero());
			dto.setNumeroCarteira(carteira.getNumeroCarteira());

			return dto;
		}

		@Override
		public Carteira createEntity(final CarteiraDTO outer) {
			final Carteira entidade = new Carteira();

			entidade.setAtivo(outer.isAtivo());

			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}

			entidade.setBanco(outer.getBanco());
			entidade.setDataCadastro(outer.getDataCadastro());
			entidade.setDescricao(outer.getDescricao());
			entidade.setExcluido(outer.isExcluido());
			entidade.setId(outer.getId());
			entidade.setNome(outer.getNome());
			entidade.setSaldo(outer.getSaldo());

			entidade.setNumeroConta(outer.getNumeroConta());
			entidade.setDigitoConta(outer.getDigitoConta());
			entidade.setNumeroAgencia(outer.getNumeroAgencia());
			entidade.setDigitoAgencia(outer.getDigitoAgencia());
			entidade.setNossoNumero(outer.getNossoNumero());
			entidade.setDigitoNossoNumero(outer.getDigitoNossoNumero());
			entidade.setNumeroCarteira(outer.getNumeroCarteira());

			return entidade;
		}

	}

}
