package br.com.lphantus.neighbor.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.ItemReservaDTO;
import br.com.lphantus.neighbor.utils.Capitalize;

/**
 * @author Adams Zago Classe de gerencia de itens a serem resevados
 */
@Entity
@Table(name = "ITEM_RESERVA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemReserva implements IEntity<Long, ItemReservaDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_ITEM_RESERVA")
	private Long id;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "PRECO")
	private BigDecimal preco;

	@Column(name = "CARENCIA_DIAS_RESERVA")
	private int carenciaDiasReserva;

	@Column(name = "ITEM_ATIVO")
	private boolean ativo = true;

	@Column(name = "NECESSITA_PAGAMENTO")
	private boolean necessitaPagamento = false;
	
	@Column(name = "NECESSITA_APROVAR")
	private boolean necessitaAprovar = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

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
		this.nome = Capitalize.capitalizeAll(nome);
	}

	/**
	 * @return the preco
	 */
	public BigDecimal getPreco() {
		return this.preco;
	}

	/**
	 * @param preco
	 *            the preco to set
	 */
	public void setPreco(final BigDecimal preco) {
		this.preco = preco;
	}

	/**
	 * @return the carenciaDiasReserva
	 */
	public int getCarenciaDiasReserva() {
		return this.carenciaDiasReserva;
	}

	/**
	 * @param carenciaDiasReserva
	 *            the carenciaDiasReserva to set
	 */
	public void setCarenciaDiasReserva(final int carenciaDiasReserva) {
		this.carenciaDiasReserva = carenciaDiasReserva;
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
	 * @return the necessitaPagamento
	 */
	public boolean isNecessitaPagamento() {
		return this.necessitaPagamento;
	}

	/**
	 * @param necessitaPagamento
	 *            the necessitaPagamento to set
	 */
	public void setNecessitaPagamento(final boolean necessitaPagamento) {
		this.necessitaPagamento = necessitaPagamento;
	}

	/**
	 * @return the necessitaAprovar
	 */
	public boolean isNecessitaAprovar() {
		return necessitaAprovar;
	}

	/**
	 * @param necessitaAprovar the necessitaAprovar to set
	 */
	public void setNecessitaAprovar(boolean necessitaAprovar) {
		this.necessitaAprovar = necessitaAprovar;
	}

	/**
	 * @return the condominio
	 */
	public Condominio getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final Condominio condominio) {
		this.condominio = condominio;
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
		final ItemReserva other = (ItemReserva) obj;
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
	public ItemReservaDTO createDto() {
		return ItemReservaDTO.Builder.getInstance().create(this);
	}

}
