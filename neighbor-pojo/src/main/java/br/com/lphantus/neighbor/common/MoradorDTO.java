package br.com.lphantus.neighbor.common;

import java.util.ArrayList;
import java.util.List;

import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.MoradorAgregado;
import br.com.lphantus.neighbor.entity.MoradorUnidadeHabitacional;
import br.com.lphantus.neighbor.entity.Telefone;

public class MoradorDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private PessoaFisicaDTO pessoa;
	private List<AnimalEstimacaoDTO> animaisEstimacao;
	private List<VeiculoDTO> veiculos;
	private List<TelefoneDTO> telefones;
	private List<MoradorAgregadoDTO> agregados;
	private List<MoradorUnidadeHabitacionalDTO> unidadeHabitacional;

	/**
	 * @return the pessoa
	 */
	public PessoaFisicaDTO getPessoa() {
		if (null == this.pessoa) {
			this.pessoa = new PessoaFisicaDTO();
		}
		return this.pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(final PessoaFisicaDTO pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the animaisEstimacao
	 */
	public List<AnimalEstimacaoDTO> getAnimaisEstimacao() {
		return this.animaisEstimacao;
	}

	/**
	 * @param animaisEstimacao
	 *            the animaisEstimacao to set
	 */
	public void setAnimaisEstimacao(final List<AnimalEstimacaoDTO> animaisEstimacao) {
		this.animaisEstimacao = animaisEstimacao;
	}

	/**
	 * @return the veiculos
	 */
	public List<VeiculoDTO> getVeiculos() {
		return this.veiculos;
	}

	/**
	 * @param veiculos
	 *            the veiculos to set
	 */
	public void setVeiculos(final List<VeiculoDTO> veiculos) {
		this.veiculos = veiculos;
	}

	/**
	 * @return the telefones
	 */
	public List<TelefoneDTO> getTelefones() {
		return this.telefones;
	}

	/**
	 * @param telefones
	 *            the telefones to set
	 */
	public void setTelefones(final List<TelefoneDTO> telefones) {
		this.telefones = telefones;
	}

	/**
	 * @return the unidadeHabitacional
	 */
	public List<MoradorUnidadeHabitacionalDTO> getUnidadeHabitacional() {
		return this.unidadeHabitacional;
	}

	/**
	 * @param unidadeHabitacional
	 *            the unidadeHabitacional to set
	 */
	public void setUnidadeHabitacional(final List<MoradorUnidadeHabitacionalDTO> unidadeHabitacional) {
		this.unidadeHabitacional = unidadeHabitacional;
	}

	/**
	 * @return the agregados
	 */
	public List<MoradorAgregadoDTO> getAgregados() {
		return this.agregados;
	}

	/**
	 * @param agregados
	 *            the agregados to set
	 */
	public void setAgregados(final List<MoradorAgregadoDTO> agregados) {
		this.agregados = agregados;
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
		final MoradorDTO other = (MoradorDTO) obj;
		if (this.pessoa == null) {
			if (other.pessoa != null) {
				return false;
			}
		} else if (!this.pessoa.equals(other.pessoa)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 7919;
		int result = 1;
		result = (prime * result) + ((this.pessoa == null) ? 0 : this.pessoa.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MoradorDTO [pessoa=" + this.pessoa + ", agregados=" + this.agregados + ", animaisEstimacao=" + this.animaisEstimacao + ", veiculos=" + this.veiculos
				+ ", telefones=" + this.telefones + ", unidadeHabitacional=" + this.unidadeHabitacional + "]";
	}

	public static class Builder extends DTOBuilder<MoradorDTO, Morador> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public MoradorDTO create(final Morador morador) {
			final MoradorDTO dto = new MoradorDTO();

			dto.setPessoa(PessoaFisicaDTO.Builder.getInstance().create(morador));
			dto.setAgregados(MoradorAgregadoDTO.Builder.getInstance().createList(morador.getAgregados()));
			dto.setAnimaisEstimacao(AnimalEstimacaoDTO.Builder.getInstance().createList(morador.getAnimaisEstimacao()));
			dto.setVeiculos(VeiculoDTO.Builder.getInstance().createList(morador.getVeiculos()));
			dto.setTelefones(TelefoneDTO.Builder.getInstance().createList(morador.getTelefones()));

			dto.setUnidadeHabitacional(MoradorUnidadeHabitacionalDTO.Builder.getInstance().createList(morador.getUnidadeHabitacional()));
			for (final MoradorUnidadeHabitacionalDTO item : dto.getUnidadeHabitacional()) {
				item.setMorador(dto);
			}

			return dto;
		}

		@Override
		public Morador createEntity(final MoradorDTO outer) {
			final Morador entidade = PessoaFisicaDTO.Builder.getInstance().createEntityMorador(outer.getPessoa());

			entidade.setAgregados(MoradorAgregadoDTO.Builder.getInstance().createListEntity(outer.getAgregados()));
			entidade.setAnimaisEstimacao(AnimalEstimacaoDTO.Builder.getInstance().createListEntity(outer.getAnimaisEstimacao()));
			entidade.setVeiculos(VeiculoDTO.Builder.getInstance().createListEntity(outer.getVeiculos()));
			entidade.setTelefones(TelefoneDTO.Builder.getInstance().createListEntity(outer.getTelefones()));
			for (final Telefone telefone : entidade.getTelefones()) {
				telefone.setMorador(entidade);
			}
			entidade.setUnidadeHabitacional(MoradorUnidadeHabitacionalDTO.Builder.getInstance().createListEntity(outer.getUnidadeHabitacional()));

			return entidade;
		}

		public MoradorDTO createDetalhes(final Morador morador) {
			final MoradorDTO dto = new MoradorDTO();

			dto.setPessoa(PessoaFisicaDTO.Builder.getInstance().create(morador));

			dto.setAgregados(new ArrayList<MoradorAgregadoDTO>());
			for (final MoradorAgregado item : morador.getAgregados()) {
				final MoradorAgregadoDTO novo = MoradorAgregadoDTO.Builder.getInstance().create(item);
				novo.setAgregado(AgregadoDTO.Builder.getInstance().create(item.getAgregado()));
				novo.setMorador(dto);
				dto.getAgregados().add(novo);
			}

			dto.setAnimaisEstimacao(AnimalEstimacaoDTO.Builder.getInstance().createList(morador.getAnimaisEstimacao()));
			dto.setVeiculos(VeiculoDTO.Builder.getInstance().createList(morador.getVeiculos()));
			dto.setTelefones(TelefoneDTO.Builder.getInstance().createList(morador.getTelefones()));

			dto.setUnidadeHabitacional(new ArrayList<MoradorUnidadeHabitacionalDTO>());
			for (final MoradorUnidadeHabitacional item : morador.getUnidadeHabitacional()) {
				final MoradorUnidadeHabitacionalDTO novo = MoradorUnidadeHabitacionalDTO.Builder.getInstance().create(item);
				novo.setUnidadeHabitacional(UnidadeHabitacionalDTO.Builder.getInstance().create(item.getUnidadeHabitacional()));
				novo.setMorador(dto);
				dto.getUnidadeHabitacional().add(novo);
			}

			return dto;
		}
	}

}