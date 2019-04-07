package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ItemReservaDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ReservaTempDTO;
import br.com.lphantus.neighbor.entity.ReservaTemp;
import br.com.lphantus.neighbor.repository.IReservaTempDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ReservaTempDAOImpl extends GenericDAOImpl<Long, ReservaTempDTO, ReservaTemp> implements IReservaTempDAO {

	@Override
	public ReservaTempDTO verificaDisponibilidade(final ReservaTempDTO reserva) throws DAOException {
		ReservaTempDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("ReservaTemp.verificaDisponibilidade");

		query.setParameter("itemReserva", reserva.getItemReserva().getId());
		query.setParameter("dataReserva", reserva.getDataReserva());
		query.setParameter("idPessoa", reserva.getCondominio().getPessoa().getIdPessoa());

		try {
			final ReservaTemp resultado = (ReservaTemp) query.getSingleResult();
			retorno = resultado.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um registro de reserva para o mesmo item na mesma data.");
		}

		return retorno;
	}

	@Override
	public ReservaTempDTO verificarCarencia(final ReservaTempDTO reserva) throws DAOException {
		ReservaTempDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("ReservaTemp.verificarCarencia");

		query.setParameter("idMorador", reserva.getMorador().getPessoa().getIdPessoa());

		final GregorianCalendar dataI = new GregorianCalendar();
		dataI.setTime(reserva.getDataReserva());
		dataI.add(Calendar.DATE, (reserva.getItemReserva().getCarenciaDiasReserva() * -1));

		query.setParameter("dataInicial", dataI.getTime());
		query.setParameter("dataFinal", reserva.getDataReserva());
		query.setParameter("idCondominio", reserva.getCondominio().getPessoa().getIdPessoa());

		try {
			final ReservaTemp resultado = (ReservaTemp) query.getSingleResult();
			retorno = resultado.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um registro de reserva para o mesmo periodo.");
		}

		return retorno;
	}

	@Override
	public ReservaTempDTO verificarReservaDataCliente(final ReservaTempDTO reserva) throws DAOException {
		ReservaTempDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("ReservaTemp.verificarReservaDataCliente");

		query.setParameter("idMorador", reserva.getMorador().getPessoa().getIdPessoa());
		query.setParameter("dataReserva", reserva.getDataReserva());
		query.setParameter("idCondominio", reserva.getCondominio().getPessoa().getIdPessoa());

		try {
			final ReservaTemp resultado = (ReservaTemp) query.getSingleResult();
			retorno = resultado.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um registro de reserva para o morador/data.");
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReservaTempDTO> listarReservaPorMorador(final MoradorDTO morador) throws DAOException {
		// + morador.getIdMorador()
		// + " and condominio.idPessoa =  :condominio ";
		final Query query = getEntityManager().createNamedQuery("ReservaTemp.listarReservaPorMorador");
		query.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		final List<ReservaTemp> reservas = new ArrayList<ReservaTemp>();
		try {
			reservas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return ReservaTempDTO.Builder.getInstance().createList(reservas);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean itemReservaEstaPendenteAprovacaoNaData(final ItemReservaDTO itemReserva, final Date data) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("ReservaTemp.itemReservaEstaPendenteAprovacaoNaData");
		query.setParameter("idReserva", itemReserva.getId());
		query.setParameter("data", data);
		query.setParameter("idPessoa", itemReserva.getCondominio().getPessoa().getIdPessoa());

		final List<ReservaTemp> reservas = new ArrayList<ReservaTemp>();
		try {
			reservas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		if (reservas.isEmpty()) {
			return false; // Item reserva nao esta resevado para esta data
		} else {
			return true; // Existe Item reseva pendente para este dia
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean itemReservaEstaPendenteMorador(final ItemReservaDTO itemReserva, final MoradorDTO morador) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("ReservaTemp.itemReservaEstaPendenteMorador");

		query.setParameter("idItem", itemReserva.getId());
		query.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		final List<ReservaTemp> reservas = new ArrayList<ReservaTemp>();
		try {
			reservas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		if (reservas.isEmpty()) {
			return false; // Morador ainda nao fez pedido de reserva deste
			// item
		} else {
			return true; // Morador ja fez pedido de reserva para este item
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReservaTempDTO> buscaReservasPendentesAprovacao(CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("ReservaTemp.buscaReservasPendentesAprovacao");

		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		final List<ReservaTemp> reservas = new ArrayList<ReservaTemp>();
		try {
			reservas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return ReservaTempDTO.Builder.getInstance().createList(reservas);
	}

}
