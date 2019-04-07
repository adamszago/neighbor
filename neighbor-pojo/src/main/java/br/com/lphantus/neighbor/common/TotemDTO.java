package br.com.lphantus.neighbor.common;

import java.util.ArrayList;

import br.com.lphantus.neighbor.entity.Agregado;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.MoradorAgregado;
import br.com.lphantus.neighbor.entity.PrestadorServico;
import br.com.lphantus.neighbor.entity.Totem;
import br.com.lphantus.neighbor.entity.Visitante;

/**
 * @author Elias
 * 
 */
public class TotemDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String senha;
	private MoradorDTO morador;
	private AgregadoDTO agregado;
	private Boolean ativo;
	private VisitanteDTO visitante;
	private PrestadorServicoDTO prestador;

	// estes atributos nao existem na entidade
	private String casa;
	private String nome;

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
	 * @return the senha
	 */
	public String getSenha() {
		return this.senha;
	}

	/**
	 * @param senha
	 *            the senha to set
	 */
	public void setSenha(final String senha) {
		this.senha = senha;
	}

	/**
	 * @return the morador
	 */
	public MoradorDTO getMorador() {
		return this.morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final MoradorDTO morador) {
		this.morador = morador;
	}

	/**
	 * @return the agregado
	 */
	public AgregadoDTO getAgregado() {
		return this.agregado;
	}

	/**
	 * @param agregado
	 *            the agregado to set
	 */
	public void setAgregado(final AgregadoDTO agregado) {
		this.agregado = agregado;
	}


	/**
	 * @return the visitante
	 */
	public VisitanteDTO getVisitante() {
		return visitante;
	}

	/**
	 * @param visitante the visitante to set
	 */
	public void setVisitante(VisitanteDTO visitante) {
		this.visitante = visitante;
	}

	/**
	 * @return the prestador
	 */
	public PrestadorServicoDTO getPrestador() {
		return prestador;
	}

	/**
	 * @param prestador the prestador to set
	 */
	public void setPrestador(PrestadorServicoDTO prestador) {
		this.prestador = prestador;
	}

	/**
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		return this.ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(final Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the casa
	 */
	public String getCasa() {
		if (null == this.casa) {
			this.casa = "";
		}
		return this.casa;
	}

	/**
	 * @param casa
	 *            the casa to set
	 */
	public void setCasa(final String casa) {
		this.casa = casa;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		if (null == this.nome) {
			this.nome = "";
		}
		return this.nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(final String nome) {
		this.nome = nome;
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
		if (!(obj instanceof TotemDTO)) {
			return false;
		}
		final TotemDTO other = (TotemDTO) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "TotemDTO [getId()=" + getId() + "]";
	}

	public static class Builder extends DTOBuilder<TotemDTO, Totem> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		/**
		 * @param totem
		 * @return a Instancia de {@link TotemDTO}
		 */
		@Override
		public TotemDTO create(final Totem totem) {
			final TotemDTO dto = new TotemDTO();
			if (null != totem.getAgregado()) {
				final AgregadoDTO agreg = totem.getAgregado().createDto();
				dto.setAgregado(agreg);
				dto.getAgregado().setMorador(
						new ArrayList<MoradorAgregadoDTO>());
				for (final MoradorAgregado moradorAgregado : totem
						.getAgregado().getMorador()) {
					final MoradorAgregadoDTO itemAdd = moradorAgregado
							.createDto();
					itemAdd.setAgregado(agreg);
					itemAdd.setMorador(moradorAgregado.getMorador().createDto());
					dto.getAgregado().getMorador().add(itemAdd);
				}
			} else if (null != totem.getMorador() ) {
				dto.setMorador(totem.getMorador().createDto());
			} else if ( null != totem.getPrestador() ){
				dto.setPrestador(totem.getPrestador().createDto());
			} else if ( null != totem.getVisitante() ){
				dto.setVisitante(totem.getVisitante().createDto());
			}
			dto.setAtivo(totem.getAtivo());
			dto.setId(totem.getId());
			dto.setSenha(totem.getSenha());
			return dto;
		}

		@Override
		public Totem createEntity(final TotemDTO outer) {
			final Totem entidade = new Totem();
			if ( null != outer.getAgregado() ) {
				final Agregado agregado = new Agregado();
				agregado.setIdPessoa(outer.getAgregado().getPessoa()
						.getIdPessoa());
				entidade.setAgregado(agregado);
			} else if ( null != outer.getMorador() ) {
				final Morador morador = new Morador();
				morador.setIdPessoa(outer.getMorador().getPessoa()
						.getIdPessoa());
				entidade.setMorador(morador);
			} else if ( null != outer.getPrestador() ){
				PrestadorServico prestador = new PrestadorServico();
				prestador.setIdPrestador(outer.getPrestador().getIdPrestador());
				entidade.setPrestador(prestador);
			} else if ( null != outer.getVisitante() ){
				Visitante visitante = new Visitante();
				visitante.setIdPessoa(outer.getVisitante().getPessoa().getIdPessoa());
				entidade.setVisitante(visitante);
			}
			entidade.setAtivo(outer.getAtivo());
			entidade.setId(outer.getId());
			entidade.setSenha(outer.getSenha());
			return entidade;
		}

	}

}
