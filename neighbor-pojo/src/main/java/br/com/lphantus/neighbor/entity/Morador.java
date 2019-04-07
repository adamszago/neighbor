package br.com.lphantus.neighbor.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.MoradorDTO;

@Entity
@Table(name = "MORADOR")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@PrimaryKeyJoinColumn(name = "ID_MORADOR", referencedColumnName = "ID_USUARIO")
public class Morador extends Usuario implements IEntity<Long, MoradorDTO> {

	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST }, targetEntity = AnimalEstimacao.class, mappedBy = "dono")
	private List<AnimalEstimacao> animaisEstimacao;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST }, targetEntity = Veiculo.class, mappedBy = "proprietario")
	private List<Veiculo> veiculos;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST }, targetEntity = Telefone.class, mappedBy = "morador")
	private List<Telefone> telefones;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST }, targetEntity = MoradorUnidadeHabitacional.class, mappedBy = "morador")
	private List<MoradorUnidadeHabitacional> unidadeHabitacional;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, targetEntity = MoradorAgregado.class, mappedBy = "morador")
	private List<MoradorAgregado> agregados;

	@Transient
	private int visitantesLivresCadastrados;

	// METODOS ADICIONAIS
	public void adicionarTelefone(final Telefone telefone) {
		telefones.add(telefone);
	}

	public void adicionarAnimalEstimacao(final AnimalEstimacao animalEstimacao) {
		animaisEstimacao.add(animalEstimacao);
	}

	public void adicionarVeiculo(final Veiculo veiculo) {
		veiculos.add(veiculo);
	}

	// FIM METODOS ADICIONAIS

	/**
	 * @return the animaisEstimacao
	 */
	public List<AnimalEstimacao> getAnimaisEstimacao() {
		return animaisEstimacao;
	}

	/**
	 * @return the visitantesLivresCadastrados
	 */
	public int getVisitantesLivresCadastrados() {
		return visitantesLivresCadastrados;
	}

	/**
	 * @param visitantesLivresCadastrados
	 *            the visitantesLivresCadastrados to set
	 */
	public void setVisitantesLivresCadastrados(int visitantesLivresCadastrados) {
		this.visitantesLivresCadastrados = visitantesLivresCadastrados;
	}

	/**
	 * @param animaisEstimacao
	 *            the animaisEstimacao to set
	 */
	public void setAnimaisEstimacao(final List<AnimalEstimacao> animaisEstimacao) {
		this.animaisEstimacao = animaisEstimacao;
	}

	/**
	 * @return the veiculos
	 */
	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	/**
	 * @param veiculos
	 *            the veiculos to set
	 */
	public void setVeiculos(final List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	/**
	 * @return the telefones
	 */
	public List<Telefone> getTelefones() {
		return telefones;
	}

	/**
	 * @param telefones
	 *            the telefones to set
	 */
	public void setTelefones(final List<Telefone> telefones) {
		this.telefones = telefones;
	}

	/**
	 * @return the agregados
	 */
	public List<MoradorAgregado> getAgregados() {
		return agregados;
	}

	/**
	 * @param agregados
	 *            the agregados to set
	 */
	public void setAgregados(final List<MoradorAgregado> agregados) {
		this.agregados = agregados;
	}

	/**
	 * @return the unidadeHabitacional
	 */
	public List<MoradorUnidadeHabitacional> getUnidadeHabitacional() {
		return unidadeHabitacional;
	}

	/**
	 * @param unidadeHabitacional
	 *            the unidadeHabitacional to set
	 */
	public void setUnidadeHabitacional(
			final List<MoradorUnidadeHabitacional> unidadeHabitacional) {
		this.unidadeHabitacional = unidadeHabitacional;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 7919;
		int result = 1;
		result = (prime * result)
				+ ((this.getIdPessoa() == null) ? 0 : this.getIdPessoa()
						.hashCode());
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
		if (!(obj instanceof Morador)) {
			return false;
		}
		final Morador other = (Morador) obj;
		if (this.getIdPessoa() == null) {
			if (other.getIdPessoa() != null) {
				return false;
			}
		} else if (!this.getIdPessoa().equals(other.getIdPessoa())) {
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
		return "Morador [animaisEstimacao=" + animaisEstimacao + ", veiculos="
				+ veiculos + ", telefones=" + telefones + ", agregados="
				+ agregados + ", unidadeHabitacional=" + unidadeHabitacional
				+ "]";
	}

	@Override
	public MoradorDTO createDto() {
		return MoradorDTO.Builder.getInstance().create(this);
	}

}
