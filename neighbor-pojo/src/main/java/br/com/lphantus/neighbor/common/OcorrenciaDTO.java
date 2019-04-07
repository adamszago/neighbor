package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.Agregado;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.Ocorrencia;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;
import br.com.lphantus.neighbor.entity.Visitante;

public class OcorrenciaDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataOcorrencia;
	private Date dataRegistroOcorrencia;
	private String descricao;
	private String gravidade;
	private boolean gerouNotificacao;
	private boolean gerouMulta;
	private boolean entregue;
	private PessoaDTO pessoa;
	private CondominioDTO condominio;

	private boolean visitante;
	private boolean morador;
	private boolean prestador;
	private boolean agregado;

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
	 * @return the dataOcorrencia
	 */
	public Date getDataOcorrencia() {
		return this.dataOcorrencia;
	}

	/**
	 * @param dataOcorrencia
	 *            the dataOcorrencia to set
	 */
	public void setDataOcorrencia(final Date dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}

	/**
	 * @return the dataRegistroOcorrencia
	 */
	public Date getDataRegistroOcorrencia() {
		return this.dataRegistroOcorrencia;
	}

	/**
	 * @param dataRegistroOcorrencia
	 *            the dataRegistroOcorrencia to set
	 */
	public void setDataRegistroOcorrencia(final Date dataRegistroOcorrencia) {
		this.dataRegistroOcorrencia = dataRegistroOcorrencia;
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
	 * @return the gravidade
	 */
	public String getGravidade() {
		return this.gravidade;
	}

	/**
	 * @param gravidade
	 *            the gravidade to set
	 */
	public void setGravidade(final String gravidade) {
		this.gravidade = gravidade;
	}

	/**
	 * @return the gerouNotificacao
	 */
	public boolean isGerouNotificacao() {
		return this.gerouNotificacao;
	}

	/**
	 * @param gerouNotificacao
	 *            the gerouNotificacao to set
	 */
	public void setGerouNotificacao(final boolean gerouNotificacao) {
		this.gerouNotificacao = gerouNotificacao;
	}

	/**
	 * @return the gerouMulta
	 */
	public boolean isGerouMulta() {
		return this.gerouMulta;
	}

	/**
	 * @param gerouMulta
	 *            the gerouMulta to set
	 */
	public void setGerouMulta(final boolean gerouMulta) {
		this.gerouMulta = gerouMulta;
	}

	/**
	 * @return the entregue
	 */
	public boolean isEntregue() {
		return this.entregue;
	}

	/**
	 * @param entregue
	 *            the entregue to set
	 */
	public void setEntregue(final boolean entregue) {
		this.entregue = entregue;
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
	 * @return the visitante
	 */
	public boolean isVisitante() {
		return this.visitante;
	}

	/**
	 * @param visitante
	 *            the visitante to set
	 */
	public void setVisitante(final boolean visitante) {
		this.visitante = visitante;
	}

	/**
	 * @return the morador
	 */
	public boolean isMorador() {
		return this.morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final boolean morador) {
		this.morador = morador;
	}

	/**
	 * @return the prestador
	 */
	public boolean isPrestador() {
		return this.prestador;
	}

	/**
	 * @param prestador
	 *            the prestador to set
	 */
	public void setPrestador(final boolean prestador) {
		this.prestador = prestador;
	}

	/**
	 * @return the agregado
	 */
	public boolean isAgregado() {
		return this.agregado;
	}

	/**
	 * @param agregado
	 *            the agregado to set
	 */
	public void setAgregado(final boolean agregado) {
		this.agregado = agregado;
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
		if (!(obj instanceof OcorrenciaDTO)) {
			return false;
		}
		final OcorrenciaDTO other = (OcorrenciaDTO) obj;
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
		return "OcorrenciaDTO [id=" + this.id + ", dataOcorrencia="
				+ this.dataOcorrencia + ", dataRegistroOcorrencia="
				+ this.dataRegistroOcorrencia + ", descricao=" + this.descricao
				+ ", gravidade=" + this.gravidade + ", gerouNotificacao="
				+ this.gerouNotificacao + ", gerouMulta=" + this.gerouMulta
				+ ", entregue=" + this.entregue + ", pessoa=" + this.pessoa
				+ ", condominio=" + this.condominio + ", visitante="
				+ this.visitante + ", morador=" + this.morador + ", prestador="
				+ this.prestador + ", agregado=" + this.agregado + "]";
	}

	public static class Builder extends DTOBuilder<OcorrenciaDTO, Ocorrencia> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public OcorrenciaDTO create(final Ocorrencia ocorrencia) {
			final OcorrenciaDTO dto = new OcorrenciaDTO();
			dto.setDataOcorrencia(ocorrencia.getDataOcorrencia());
			dto.setDataRegistroOcorrencia(ocorrencia.getDataOcorrencia());
			dto.setDescricao(ocorrencia.getDescricao());
			dto.setEntregue(ocorrencia.isEntregue());
			dto.setGerouMulta(ocorrencia.isGerouMulta());
			dto.setGerouNotificacao(ocorrencia.isGerouNotificacao());
			dto.setGravidade(ocorrencia.getGravidade());
			dto.setId(ocorrencia.getId());

			if (null == ocorrencia.getPessoa()) {
				// LOGGER.error("Ocorrencia sem pessoa associada.");
				// LOGGER.error(ocorrencia);
			} else {

				if (ocorrencia.getPessoa() != null) {
					if (ocorrencia.getPessoa() instanceof PessoaFisica) {
						dto.setPessoa(PessoaFisicaDTO.Builder.getInstance()
								.create((PessoaFisica) ocorrencia.getPessoa()));
					} else if (ocorrencia.getPessoa() instanceof PessoaJuridica) {
						dto.setPessoa(PessoaJuridicaDTO.Builder
								.getInstance()
								.create((PessoaJuridica) ocorrencia.getPessoa()));
					}

					if (ocorrencia.getPessoa() instanceof Visitante) {
						dto.setVisitante(true);
					} else if (ocorrencia.getPessoa() instanceof Morador) {
						dto.setMorador(true);
					} else if (ocorrencia.getPessoa() instanceof Agregado) {
						dto.setAgregado(true);
					} else if ((ocorrencia.getPessoa() instanceof PessoaFisica)
							|| (ocorrencia.getPessoa() instanceof PessoaJuridica)) {
						dto.setPrestador(true);
					}
				}

			}

			if (null == ocorrencia.getCondominio()) {
				// LOGGER.error("Ocorrencia sem condominio associado.");
				// LOGGER.error(ocorrencia);
			} else {
				dto.setCondominio(ocorrencia.getCondominio().createDto());
			}

			return dto;
		}

		@Override
		public Ocorrencia createEntity(final OcorrenciaDTO outer) {
			final Ocorrencia entidade = new Ocorrencia();

			entidade.setDataOcorrencia(outer.getDataOcorrencia());
			entidade.setDataRegistroOcorrencia(outer.getDataOcorrencia());
			entidade.setDescricao(outer.getDescricao());
			entidade.setEntregue(outer.isEntregue());
			entidade.setGerouMulta(outer.isGerouMulta());
			entidade.setGerouNotificacao(outer.isGerouNotificacao());
			entidade.setGravidade(outer.getGravidade());
			entidade.setId(outer.getId());

			if (null == outer.getPessoa()) {
				// LOGGER.error("Ocorrencia sem pessoa associada.");
				// LOGGER.error(outer);
			} else {
				if (outer.getPessoa() instanceof PessoaJuridicaDTO) {
					entidade.setPessoa(PessoaJuridicaDTO.Builder
							.getInstance()
							.createEntity((PessoaJuridicaDTO) outer.getPessoa()));
				} else if (outer.getPessoa() instanceof PessoaFisicaDTO) {
					entidade.setPessoa(PessoaFisicaDTO.Builder.getInstance()
							.createEntity((PessoaFisicaDTO) outer.getPessoa()));
				}
			}

			if (null == outer.getCondominio()) {
				// LOGGER.error("Ocorrencia sem condominio associado.");
				// LOGGER.error(outer);
			} else {
				entidade.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}

			return entidade;
		}
	}

}
