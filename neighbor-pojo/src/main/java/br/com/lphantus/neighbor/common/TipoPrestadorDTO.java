package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.TipoPrestador;

public class TipoPrestadorDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String tipoPrestador;
	private boolean ativo;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the tipoPrestador
	 */
	public String getTipoPrestador() {
		return tipoPrestador;
	}

	/**
	 * @param tipoPrestador
	 *            the tipoPrestador to set
	 */
	public void setTipoPrestador(String tipoPrestador) {
		this.tipoPrestador = tipoPrestador;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public static class Builder extends
			DTOBuilder<TipoPrestadorDTO, TipoPrestador> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public TipoPrestadorDTO create(TipoPrestador tipo) {
			TipoPrestadorDTO dto = new TipoPrestadorDTO();
			dto.setAtivo(tipo.isAtivo());
			dto.setId(tipo.getId());
			dto.setTipoPrestador(tipo.getTipoPrestador());
			return dto;
		}

		@Override
		public TipoPrestador createEntity(TipoPrestadorDTO outer) {
			TipoPrestador entidade = new TipoPrestador();
			entidade.setAtivo(outer.isAtivo());
			entidade.setId(outer.getId());
			entidade.setTipoPrestador(outer.getTipoPrestador());
			return entidade;
		}
	}

}
