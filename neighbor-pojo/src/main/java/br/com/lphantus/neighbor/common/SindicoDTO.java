package br.com.lphantus.neighbor.common;

import java.util.List;

import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;
import br.com.lphantus.neighbor.entity.Sindico;

public class SindicoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private PessoaDTO pessoa;
	private List<SindicoCondominioDTO> condominio;

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
	public List<SindicoCondominioDTO> getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final List<SindicoCondominioDTO> condominio) {
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
		if (!(obj instanceof SindicoDTO)) {
			return false;
		}
		final SindicoDTO other = (SindicoDTO) obj;
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
		return "SindicoDTO [id=" + this.id + ", pessoa=" + this.pessoa
				+ ", condominio=" + this.condominio + "]";
	}

	public static class Builder extends DTOBuilder<SindicoDTO, Sindico> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public SindicoDTO create(final Sindico sindico) {
			final SindicoDTO dto = new SindicoDTO();
			dto.setId(sindico.getId());

			if (null == sindico.getPessoa()) {
				LOGGER.error(String.format("Sindico sem pessoa associada: %s", sindico.toString()));
			} else {
				if (sindico.getPessoa() instanceof PessoaFisica) {
					dto.setPessoa(PessoaFisicaDTO.Builder.getInstance().create(
							(PessoaFisica) sindico.getPessoa()));
				} else {
					dto.setPessoa(PessoaJuridicaDTO.Builder.getInstance()
							.create((PessoaJuridica) sindico.getPessoa()));
				}
			}

			dto.setCondominio(SindicoCondominioDTO.Builder.getInstance()
					.createList(sindico.getCondominio()));

			return dto;
		}

		@Override
		public Sindico createEntity(final SindicoDTO outer) {
			final Sindico entidade = new Sindico();

			entidade.setId(outer.getId());

			if (outer.getPessoa() instanceof PessoaFisicaDTO) {
				entidade.setPessoa(PessoaFisicaDTO.Builder.getInstance()
						.createEntity((PessoaFisicaDTO) outer.getPessoa()));
			} else {
				entidade.setPessoa(PessoaJuridicaDTO.Builder.getInstance()
						.createEntity((PessoaJuridicaDTO) outer.getPessoa()));
			}

			// TODO: revisar
			// entidade.setCondominio(SindicoCondominioDTO.Builder.getInstance().c)

			return entidade;
		}
	}

}
