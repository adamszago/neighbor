package br.com.lphantus.neighbor.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;
import br.com.lphantus.neighbor.entity.PrestadorServico;
import br.com.lphantus.neighbor.entity.ServicoPrestado;

public class PrestadorServicoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long idPrestador;
	private PessoaDTO pessoa;
	private TipoPrestadorDTO tipoPrestador;
	private CondominioDTO condominio;
	private Set<ServicoPrestadoDTO> servicosPrestados;
	private Set<MoradorDTO> entradasLivres;
	private List<MoradorDTO> entradasLivresList;
	private ServicoPrestadoDTO servicoAgendado;
	private List<ServicoPrestadoDTO> servicosPrestadosList;

	/**
	 * @return the idPrestador
	 */
	public Long getIdPrestador() {
		return idPrestador;
	}

	/**
	 * @param idPrestador
	 *            the idPrestador to set
	 */
	public void setIdPrestador(final Long idPrestador) {
		this.idPrestador = idPrestador;
	}

	/**
	 * @return the pessoa
	 */
	public PessoaDTO getPessoa() {
		return pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(final PessoaDTO pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the servicosPrestados
	 */
	public Set<ServicoPrestadoDTO> getServicosPrestados() {
		return servicosPrestados;
	}

	/**
	 * @param servicosPrestados
	 *            the servicosPrestados to set
	 */
	public void setServicosPrestados(final Set<ServicoPrestadoDTO> servicosPrestados) {
		this.servicosPrestados = servicosPrestados;
	}

	/**
	 * @return the servicoAgendado
	 */
	public ServicoPrestadoDTO getServicoAgendado() {
		return servicoAgendado;
	}

	/**
	 * @param servicoAgendado
	 *            the servicoAgendado to set
	 */
	public void setServicoAgendado(final ServicoPrestadoDTO servicoAgendado) {
		this.servicoAgendado = servicoAgendado;
	}

	/**
	 * @return the servicosPrestadosList
	 */
	public List<ServicoPrestadoDTO> getServicosPrestadosList() {
		return servicosPrestadosList;
	}

	/**
	 * @param servicosPrestadosList
	 *            the servicosPrestadosList to set
	 */
	public void setServicosPrestadosList(final List<ServicoPrestadoDTO> servicosPrestadosList) {
		this.servicosPrestadosList = servicosPrestadosList;
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
	 * @return the tipoPrestador
	 */
	public TipoPrestadorDTO getTipoPrestador() {
		return tipoPrestador;
	}

	/**
	 * @param tipoPrestador
	 *            the tipoPrestador to set
	 */
	public void setTipoPrestador(final TipoPrestadorDTO tipoPrestador) {
		this.tipoPrestador = tipoPrestador;
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

	/**
	 * @return the documento
	 */
	public String getDocumento() {
		String retorno;
		if (pessoa instanceof PessoaFisicaDTO) {
			retorno = ((PessoaFisicaDTO) pessoa).getCpf();
		} else if (pessoa instanceof PessoaJuridicaDTO) {
			retorno = ((PessoaJuridicaDTO) pessoa).getCnpj();
		} else {
			retorno = "";
		}
		return retorno;
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
		result = (prime * result) + ((idPrestador == null) ? 0 : idPrestador.hashCode());
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
		if (!(obj instanceof PrestadorServicoDTO)) {
			return false;
		}
		final PrestadorServicoDTO other = (PrestadorServicoDTO) obj;
		if (idPrestador == null) {
			if (other.idPrestador != null) {
				return false;
			}
		} else if (!idPrestador.equals(other.idPrestador)) {
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
		return "PrestadorServicoDTO [idPrestador=" + idPrestador + ", pessoa=" + pessoa + ", tipoPrestador=" + tipoPrestador + ", condominio=" + condominio + "]";
	}

	public static class Builder extends DTOBuilder<PrestadorServicoDTO, PrestadorServico> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public PrestadorServicoDTO create(final PrestadorServico prestadorServico) {
			final PrestadorServicoDTO dto = new PrestadorServicoDTO();

			dto.setIdPrestador(prestadorServico.getIdPrestador());
			if (prestadorServico.getPessoa() != null) {
				if (prestadorServico.getPessoa() instanceof PessoaFisica) {
					dto.setPessoa(PessoaFisicaDTO.Builder.getInstance().create((PessoaFisica) prestadorServico.getPessoa()));
				} else if (prestadorServico.getPessoa() instanceof PessoaJuridica) {
					dto.setPessoa(PessoaJuridicaDTO.Builder.getInstance().create((PessoaJuridica) prestadorServico.getPessoa()));
				}
			}

			if (null == prestadorServico.getTipoPrestador()) {
				// LOGGER.error("Tipo do prestador nulo.");
				// LOGGER.error(prestadorServico);
			} else {
				dto.setTipoPrestador(prestadorServico.getTipoPrestador().createDto());
			}
			if (null != prestadorServico.getServicoAgendado()) {
				dto.setServicoAgendado(prestadorServico.getServicoAgendado().createDto());
				dto.getServicoAgendado().setPrestadorServico(dto);
			}

			final Set<MoradorDTO> entradasLivres = new HashSet<MoradorDTO>();
			entradasLivres.addAll(MoradorDTO.Builder.getInstance().createList(prestadorServico.getEntradasLivres()));
			dto.setEntradasLivres(entradasLivres);
			dto.setEntradasLivresList(new ArrayList<MoradorDTO>(entradasLivres));

			final Set<ServicoPrestadoDTO> servicosPrestados = new HashSet<ServicoPrestadoDTO>();
			servicosPrestados.addAll(ServicoPrestadoDTO.Builder.getInstance().createList(prestadorServico.getServicosPrestados()));
			if (!servicosPrestados.isEmpty()) {
				for (final ServicoPrestadoDTO servico : servicosPrestados) {
					servico.setPrestadorServico(dto);
				}
			}
			dto.setServicosPrestados(servicosPrestados);
			dto.setServicosPrestadosList(new ArrayList<ServicoPrestadoDTO>(servicosPrestados));

			if (null != prestadorServico.getCondominio()) {
				dto.setCondominio(prestadorServico.getCondominio().createDto());
			}

			return dto;
		}

		@Override
		public PrestadorServico createEntity(final PrestadorServicoDTO outer) {

			final PrestadorServico entidade = new PrestadorServico();

			entidade.setIdPrestador(outer.getIdPrestador());
			if (outer.getPessoa() != null) {
				if (outer.getPessoa() instanceof PessoaFisicaDTO) {
					entidade.setPessoa(PessoaFisicaDTO.Builder.getInstance().createEntity((PessoaFisicaDTO) outer.getPessoa()));
				} else if (outer.getPessoa() instanceof PessoaJuridicaDTO) {
					entidade.setPessoa(PessoaJuridicaDTO.Builder.getInstance().createEntity((PessoaJuridicaDTO) outer.getPessoa()));
				}
			}

			if (null == outer.getTipoPrestador()) {
				// LOGGER.error("Tipo do prestador nulo.");
				// LOGGER.error(outer);
			} else {
				entidade.setTipoPrestador(TipoPrestadorDTO.Builder.getInstance().createEntity(outer.getTipoPrestador()));
			}

			if (null != outer.getServicoAgendado()) {
				entidade.setServicoAgendado(ServicoPrestadoDTO.Builder.getInstance().createEntity(outer.getServicoAgendado()));
			}

			final Set<Morador> entradasLivres = new HashSet<Morador>();
			if (null != outer.getEntradasLivres()) {
				for (MoradorDTO livre : outer.getEntradasLivres()) {
					entradasLivres.add(PessoaFisicaDTO.Builder.getInstance().createEntityMorador(livre.getPessoa()));
				}
			}
			entidade.setEntradasLivres(entradasLivres);
			entidade.setEntradasLivresList(new ArrayList<Morador>(entradasLivres));

			final Set<ServicoPrestado> servicosPrestados = new HashSet<ServicoPrestado>();
			servicosPrestados.addAll(ServicoPrestadoDTO.Builder.getInstance().createListEntity(outer.getServicosPrestados()));
			if (!servicosPrestados.isEmpty()) {
				for (final ServicoPrestado servico : servicosPrestados) {
					servico.setPrestadorServico(entidade);
				}
			}
			entidade.setServicosPrestados(servicosPrestados);
			entidade.setServicosPrestadosList(new ArrayList<ServicoPrestado>(servicosPrestados));

			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance().createEntity(outer.getCondominio()));
			}

			return entidade;
		}
	}

}
