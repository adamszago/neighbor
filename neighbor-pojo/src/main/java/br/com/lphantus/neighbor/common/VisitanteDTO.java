package br.com.lphantus.neighbor.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.Visita;
import br.com.lphantus.neighbor.entity.Visitante;

/**
 * @author Elias
 */
public class VisitanteDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private PessoaFisicaDTO pessoa;
	private Set<VisitaDTO> visitas;
	private VisitaDTO visitaAgendada;
	private List<VisitaDTO> visitasList;
	private Set<MoradorDTO> entradasLivres;
	private List<MoradorDTO> entradasLivresList;
	private CondominioDTO condominio;

	/**
	 * @return the pessoa
	 */
	public PessoaFisicaDTO getPessoa() {
		if (null == pessoa) {
			pessoa = new PessoaFisicaDTO();
		}
		return pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(final PessoaFisicaDTO pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the visitas
	 */
	public Set<VisitaDTO> getVisitas() {
		return visitas;
	}

	/**
	 * @param visitas
	 *            the visitas to set
	 */
	public void setVisitas(final Set<VisitaDTO> visitas) {
		this.visitas = visitas;
	}

	/**
	 * @return the visitaAgendada
	 */
	public VisitaDTO getVisitaAgendada() {
		return visitaAgendada;
	}

	/**
	 * @param visitaAgendada
	 *            the visitaAgendada to set
	 */
	public void setVisitaAgendada(final VisitaDTO visitaAgendada) {
		this.visitaAgendada = visitaAgendada;
	}

	/**
	 * @return the visitasList
	 */
	public List<VisitaDTO> getVisitasList() {
		return visitasList;
	}

	/**
	 * @param visitasList
	 *            the visitasList to set
	 */
	public void setVisitasList(final List<VisitaDTO> visitasList) {
		this.visitasList = visitasList;
	}

	/**
	 * @return the entradasLivres
	 */
	public Set<MoradorDTO> getEntradasLivres() {
		return entradasLivres;
	}

	/**
	 * @param entradasLivres
	 *            the entradasLivres to set
	 */
	public void setEntradasLivres(final Set<MoradorDTO> entradasLivres) {
		this.entradasLivres = entradasLivres;
	}

	/**
	 * @return the entradasLivresList
	 */
	public List<MoradorDTO> getEntradasLivresList() {
		return entradasLivresList;
	}

	/**
	 * @param entradasLivresList
	 *            the entradasLivresList to set
	 */
	public void setEntradasLivresList(final List<MoradorDTO> entradasLivresList) {
		this.entradasLivresList = entradasLivresList;
	}

	/**
	 * @return the condominio
	 */
	public CondominioDTO getCondominio() {
		return condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final CondominioDTO condominio) {
		this.condominio = condominio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((pessoa == null) ? 0 : pessoa.hashCode());
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
		if (!(obj instanceof VisitanteDTO)) {
			return false;
		}
		final VisitanteDTO other = (VisitanteDTO) obj;
		if (pessoa == null) {
			if (other.pessoa != null) {
				return false;
			}
		} else if (!pessoa.equals(other.pessoa)) {
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
		return "VisitanteDTO [pessoa=" + pessoa + ", visitas=" + visitas + ", visitaAgendada=" + visitaAgendada + ", visitasList=" + visitasList + ", entradasLivres="
				+ entradasLivres + ", entradasLivresList=" + entradasLivresList + ", condominio=" + condominio + "]";
	}

	public static class Builder extends DTOBuilder<VisitanteDTO, Visitante> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		/**
		 * @param visitante
		 * @return Uma instancia de {@link VisitanteDTO}
		 */
		@Override
		public VisitanteDTO create(final Visitante visitante) {
			final VisitanteDTO dto = new VisitanteDTO();
			if (null != visitante.getCondominio()) {
				dto.setCondominio(visitante.getCondominio().createDto());
			}

			dto.setPessoa(PessoaFisicaDTO.Builder.getInstance().create(visitante));

			if (null != visitante.getVisitaAgendada()) {
				dto.setVisitaAgendada(visitante.getVisitaAgendada().createDto());
				dto.getVisitaAgendada().setVisitante(dto);
			}

			final Set<MoradorDTO> entradasLivres = new HashSet<MoradorDTO>();
			entradasLivres.addAll(MoradorDTO.Builder.getInstance().createList(visitante.getEntradasLivres()));
			dto.setEntradasLivres(entradasLivres);
			dto.setEntradasLivresList(new ArrayList<MoradorDTO>(entradasLivres));

			final Set<VisitaDTO> visitas = new HashSet<VisitaDTO>();
			visitas.addAll(VisitaDTO.Builder.getInstance().createList(visitante.getVisitas()));
			if (!visitas.isEmpty()) {
				for (final VisitaDTO visita : visitas) {
					visita.setVisitante(dto);
				}
			}
			dto.setVisitas(visitas);

			dto.setVisitasList(new ArrayList<VisitaDTO>(visitas));
			return dto;
		}

		@Override
		public Visitante createEntity(final VisitanteDTO outer) {
			final Visitante entidade = PessoaFisicaDTO.Builder.getInstance().createEntityVisitante(outer.getPessoa());

			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance().createEntity(outer.getCondominio()));
			}

			if (null != outer.getVisitaAgendada()) {
				entidade.setVisitaAgendada(VisitaDTO.Builder.getInstance().createEntity(outer.getVisitaAgendada()));
			}

			final Set<Morador> entradasLivres = new HashSet<Morador>();
			if (null != outer.getEntradasLivres()) {
				for (MoradorDTO entradaLivre : outer.getEntradasLivres()) {
					entradasLivres.add(PessoaFisicaDTO.Builder.getInstance().createEntityMorador(entradaLivre.getPessoa()));
				}
			}
			entidade.setEntradasLivres(entradasLivres);
			entidade.setEntradasLivresList(new ArrayList<Morador>(entradasLivres));

			final Set<Visita> visitas = new HashSet<Visita>();
			visitas.addAll(VisitaDTO.Builder.getInstance().createListEntity(outer.getVisitas()));
			if (!visitas.isEmpty()) {
				for (final Visita visita : visitas) {
					visita.setVisitante(entidade);
				}
			}
			entidade.setVisitas(visitas);

			entidade.setVisitasList(new ArrayList<Visita>(visitas));
			return entidade;
		}

	}

}
