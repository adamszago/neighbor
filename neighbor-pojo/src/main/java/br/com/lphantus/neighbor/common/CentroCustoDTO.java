package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.CentroCusto;

public class CentroCustoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String descricao;
	private boolean lancavel = true;
	private CentroCustoDTO centroCustoPai;
	private boolean ativo = true;
	private boolean excluido = false;
	private CondominioDTO condominio;
	private Date dataCadastro;

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
	 * @return the lancavel
	 */
	public boolean isLancavel() {
		return this.lancavel;
	}

	public String getLancavelText() {
		if (this.lancavel) {
			return "Sim";
		} else {
			return "Não";
		}
	}

	/**
	 * @param lancavel
	 *            the lancavel to set
	 */
	public void setLancavel(final boolean lancavel) {
		this.lancavel = lancavel;
	}

	/**
	 * @return the centroCustoPai
	 */
	public CentroCustoDTO getCentroCustoPai() {
		return this.centroCustoPai;
	}

	/**
	 * @param centroCustoPai
	 *            the centroCustoPai to set
	 */
	public void setCentroCustoPai(final CentroCustoDTO centroCustoPai) {
		this.centroCustoPai = centroCustoPai;
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
		final CentroCustoDTO other = (CentroCustoDTO) obj;
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
		// return "CentroCustoDTO [id=" + id + ", nome=" + nome + ", descricao="
		// + descricao + ", lancavel=" + lancavel + ", centroCustoPai="
		// + centroCustoPai + ", ativo=" + ativo + ", excluido="
		// + excluido + ", condominio=" + condominio + ", dataCadastro="
		// + dataCadastro + "]";
		return this.nome;
	}

	public static class Builder extends DTOBuilder<CentroCustoDTO, CentroCusto> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public CentroCustoDTO create(final CentroCusto centroCusto) {
			final CentroCustoDTO dto = new CentroCustoDTO();
			if (null != centroCusto.getCentroCustoPai()) {
				dto.setCentroCustoPai(centroCusto.getCentroCustoPai()
						.createDto());
			}

			dto.setAtivo(centroCusto.isAtivo());
			dto.setCondominio(centroCusto.getCondominio().createDto());
			dto.setDataCadastro(centroCusto.getDataCadastro());
			dto.setDescricao(centroCusto.getDescricao());
			dto.setExcluido(centroCusto.isExcluido());
			dto.setId(centroCusto.getId());
			dto.setLancavel(centroCusto.isLancavel());
			dto.setNome(centroCusto.getNome());

			return dto;
		}

		@Override
		public CentroCusto createEntity(final CentroCustoDTO outer) {
			final CentroCusto entidade = new CentroCusto();

			if (null != outer.getCentroCustoPai()) {
				entidade.setCentroCustoPai(createEntity(outer
						.getCentroCustoPai()));
			}

			entidade.setAtivo(outer.isAtivo());
			entidade.setCondominio(CondominioDTO.Builder.getInstance()
					.createEntity(outer.getCondominio()));
			entidade.setDataCadastro(outer.getDataCadastro());
			entidade.setDescricao(outer.getDescricao());
			entidade.setExcluido(outer.isExcluido());
			entidade.setId(outer.getId());
			entidade.setLancavel(outer.isLancavel());
			entidade.setNome(outer.getNome());

			return entidade;
		}

	}

}
