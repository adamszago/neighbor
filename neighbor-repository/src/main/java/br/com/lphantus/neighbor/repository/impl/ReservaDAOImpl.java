package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
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
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ReservaDTO;
import br.com.lphantus.neighbor.entity.Reserva;
import br.com.lphantus.neighbor.repository.IReservaDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ReservaDAOImpl extends GenericDAOImpl<Long, ReservaDTO, Reserva> implements IReservaDAO {

	@Override
	public ReservaDTO verificaDisponibilidade(final ReservaDTO reserva) throws DAOException {
		ReservaDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Reserva.verificaDisponibilidade");

		query.setParameter("itemReserva", reserva.getItemReserva().getId());
		query.setParameter("dataReserva", reserva.getDataReserva());
		query.setParameter("idPessoa", reserva.getCondominio().getPessoa().getIdPessoa());

		try {
			final Reserva resultado = (Reserva) query.getSingleResult();
			retorno = resultado.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrada mais de uma reserva para este item, nesta mesma data.");
		}

		return retorno;
	}

	@Override
	public ReservaDTO verificarCarencia(final ReservaDTO reserva) throws DAOException {
		ReservaDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Reserva.verificarCarencia");

		query.setParameter("idPessoa", reserva.getMorador().getPessoa().getIdPessoa());

		final GregorianCalendar dataI = new GregorianCalendar();
		dataI.setTime(reserva.getDataReserva());
		dataI.add(Calendar.DATE, (reserva.getItemReserva().getCarenciaDiasReserva() * -1));

		query.setParameter("dataInicial", dataI.getTime());
		query.setParameter("dataFinal", reserva.getDataReserva());
		query.setParameter("idCondominio", reserva.getCondominio().getPessoa().getIdPessoa());

		try {
			final Reserva resultado = (Reserva) query.getSingleResult();
			retorno = resultado.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrada mais de uma reserva para o mesmo item na mesma data.");
		}

		return retorno;
	}

	@Override
	public ReservaDTO verificarReservaDataCliente(final ReservaDTO reserva) throws DAOException {
		ReservaDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Reserva.verificarReservaDataCliente");

		query.setParameter("idPessoa", reserva.getMorador().getPessoa().getIdPessoa());
		query.setParameter("dataReserva", reserva.getDataReserva());
		query.setParameter("idCondominio", reserva.getCondominio().getPessoa().getIdPessoa());

		try {
			final Reserva resultado = (Reserva) query.getSingleResult();
			retorno = resultado.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrada mais de uma reserva para o mesmo item na mesma data");
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReservaDTO> listarReservaPorMorador(final MoradorDTO morador, final CondominioDTO condominio) throws DAOException {

		final Query query = getEntityManager().createNamedQuery("Reserva.listarReservaPorMorador");
		if (null == morador || null == morador.getPessoa() || null == morador.getPessoa().getIdPessoa()) {
			query.setParameter("idMorador", null);
		} else {
			query.setParameter("idMorador", morador.getPessoa().getIdPessoa());
		}
		if ( null == condominio || null == condominio.getPessoa() || null == condominio.getPessoa().getIdPessoa() ){
			query.setParameter("idCondominio", null);
		}else{
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		final List<Reserva> reservas = new ArrayList<Reserva>();
		try {
			reservas.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return ReservaDTO.Builder.getInstance().createList(reservas);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReservaDTO> listarReservaPorCondominio(final CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Reserva.listarReservaPorCondominio");

		if (null == condominio) {
			query.setParameter("idPessoa", null);
		} else {
			query.setParameter("idPessoa", condominio.getPessoa().getIdPessoa());
		}

		List<Reserva> lista = new ArrayList<Reserva>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return ReservaDTO.Builder.getInstance().createList(lista);
	}
}
