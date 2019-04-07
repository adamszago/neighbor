package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.AnimalEstimacao;
import br.com.lphantus.neighbor.entity.Morador;

public class AnimalEstimacaoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private TipoAnimalDTO tipoAnimal;
	private String raca;
	private String porte;
	private boolean vacinado;
	private Date dataVistoriaCartao;
	private MoradorDTO dono;

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
	 * @return the tipoAnimal
	 */
	public TipoAnimalDTO getTipoAnimal() {
		return this.tipoAnimal;
	}

	/**
	 * @param tipoAnimal
	 *            the tipoAnimal to set
	 */
	public void setTipoAnimal(final TipoAnimalDTO tipoAnimal) {
		this.tipoAnimal = tipoAnimal;
	}

	/**
	 * @return the raca
	 */
	public String getRaca() {
		return this.raca;
	}

	/**
	 * @param raca
	 *            the raca to set
	 */
	public void setRaca(final String raca) {
		this.raca = raca;
	}

	/**
	 * @return the porte
	 */
	public String getPorte() {
		return this.porte;
	}

	/**
	 * @param porte
	 *            the porte to set
	 */
	public void setPorte(final String porte) {
		this.porte = porte;
	}

	/**
	 * @return the vacinado
	 */
	public boolean isVacinado() {
		return this.vacinado;
	}

	/**
	 * @param vacinado
	 *            the vacinado to set
	 */
	public void setVacinado(final boolean vacinado) {
		this.vacinado = vacinado;
	}

	/**
	 * @return the dataVistoriaCartao
	 */
	public Date getDataVistoriaCartao() {
		return this.dataVistoriaCartao;
	}

	/**
	 * @param dataVistoriaCartao
	 *            the dataVistoriaCartao to set
	 */
	public void setDataVistoriaCartao(final Date dataVistoriaCartao) {
		this.dataVistoriaCartao = dataVistoriaCartao;
	}

	/**
	 * @return the dono
	 */
	public MoradorDTO getDono() {
		return this.dono;
	}

	/**
	 * @param dono
	 *            the dono to set
	 */
	public void setDono(final MoradorDTO dono) {
		this.dono = dono;
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
		final AnimalEstimacaoDTO other = (AnimalEstimacaoDTO) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public static class Builder extends
			DTOBuilder<AnimalEstimacaoDTO, AnimalEstimacao> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public AnimalEstimacaoDTO create(final AnimalEstimacao animalEstimacao) {
			final AnimalEstimacaoDTO dto = new AnimalEstimacaoDTO();

			dto.setDataVistoriaCartao(animalEstimacao.getDataVistoriaCartao());
			dto.setId(animalEstimacao.getId());
			dto.setNome(animalEstimacao.getNome());
			dto.setPorte(animalEstimacao.getPorte());
			dto.setRaca(animalEstimacao.getRaca());
			if (null != animalEstimacao.getTipoAnimal()) {
				dto.setTipoAnimal(animalEstimacao.getTipoAnimal().createDto());
			}
			dto.setVacinado(animalEstimacao.isVacinado());

			// if (null != animalEstimacao.getDono()) {
			// dto.setDono(animalEstimacao.getDono().createDto());
			// }

			return dto;
		}

		@Override
		public AnimalEstimacao createEntity(final AnimalEstimacaoDTO outer) {
			final AnimalEstimacao entidade = new AnimalEstimacao();
			entidade.setDataVistoriaCartao(outer.getDataVistoriaCartao());

			if (null != outer.getDono()) {
				final Morador morador = new Morador();
				morador.setIdPessoa(outer.getDono().getPessoa().getIdPessoa());
				entidade.setDono(morador);
			}

			entidade.setId(outer.getId());
			entidade.setNome(outer.getNome());
			entidade.setPorte(outer.getPorte());
			entidade.setRaca(outer.getRaca());
			if (null != outer.getTipoAnimal()) {
				entidade.setTipoAnimal(TipoAnimalDTO.Builder.getInstance()
						.createEntity(outer.getTipoAnimal()));
			}
			entidade.setVacinado(outer.isVacinado());

			return entidade;
		}
	}

}
