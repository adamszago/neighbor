package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.TotemDTO;
import br.com.lphantus.neighbor.entity.Agregado;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.Totem;
import br.com.lphantus.neighbor.repository.ITotemDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class TotemDAOImpl extends GenericDAOImpl<Long, TotemDTO, Totem> implements ITotemDAO {

	@Override
	public TotemDTO existeUsuarioCadastrado(final TotemDTO totem) throws DAOException {

		final Query query = getEntityManager().createNamedQuery("Totem.existeUsuarioCadastrado");
		if (null == totem.getMorador()) {
			query.setParameter("idMorador", null);
		} else {
			query.setParameter("idMorador", totem.getMorador().getPessoa().getIdPessoa());
		}
		if (null == totem.getAgregado()) {
			query.setParameter("idAgregado", null);
		} else {
			query.setParameter("idAgregado", totem.getAgregado().getPessoa().getIdPessoa());
		}

		try {
			final Totem result = (Totem) query.getSingleResult();
			return result.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return null;
	}

	@Override
	public TotemDTO buscarAgregadoTotem(final AgregadoDTO agregado) throws DAOException {

		TotemDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Totem.buscarAgregadoTotem");
		query.setParameter("idAgregado", agregado.getPessoa().getIdPessoa());

		try {
			final Totem totem = (Totem) query.getSingleResult();
			retorno = totem.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um totem para o agregado.");
		}

		return retorno;
	}

	@Override
	public TotemDTO buscarMoradorTotem(final MoradorDTO morador) throws DAOException {
		TotemDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("Totem.buscarMoradorTotem");
		query.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		try {
			final Totem totem = (Totem) query.getSingleResult();
			retorno = totem.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrado mais de um totem para o morador.");
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TotemDTO> findAllAtivos() throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Totem.findAllAtivos");

		final List<Totem> totemList = new ArrayList<Totem>();
		try {
			totemList.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return TotemDTO.Builder.getInstance().createList(totemList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TotemDTO> buscarTodosAtivosPorCondominio(final CondominioDTO condominio) throws DAOException {
		final Query queryMorador = getEntityManager().createNamedQuery("Totem.buscarTodosAtivosPorCondominio");
		if (null == condominio) {
			queryMorador.setParameter("idCondominio", null);
		} else {
			queryMorador.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		final List<Object[]> listaMorador = new ArrayList<Object[]>();
		try {
			listaMorador.addAll(queryMorador.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		final List<TotemDTO> listaParseMorador = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaMorador) {
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create((Totem) objetos[0]);
			adicionar.setNome((String) objetos[1]);
			adicionar.setCasa((String) objetos[2]);
			listaParseMorador.add(adicionar);
		}

		final Query queryAgregado = getEntityManager().createNamedQuery("Totem.buscarTodosAtivosPorCondominioAgregado");
		if (null == condominio) {
			queryAgregado.setParameter("idCondominio", null);
		} else {
			queryAgregado.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		final List<Object[]> listaAgregado = new ArrayList<Object[]>();
		try {
			listaAgregado.addAll(queryAgregado.getResultList());
		} catch (NoResultException nre) {

		}
		final List<TotemDTO> listaParseAgregado = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaAgregado) {
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create((Totem) objetos[0]);
			adicionar.setNome((String) objetos[1]);
			adicionar.setCasa((String) objetos[2]);
			listaParseAgregado.add(adicionar);
		}

		// System.out.println(String.format(
		// "Quantidade de totens de moradores ativos no condominio: %d",
		// listaParseMorador.size()));
		// System.out.println(String.format(
		// "Quantidade de totens de agregados ativos no condominio: %d",
		// listaParseAgregado.size()));

		final List<TotemDTO> conjunto = new ArrayList<TotemDTO>(listaParseMorador);
		conjunto.addAll(listaParseAgregado);

		return conjunto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TotemDTO> buscarMoradorSemTotemOuInativoPorCondominio(final CondominioDTO condominio) throws DAOException {
		final Query queryMorador = getEntityManager().createNamedQuery("Totem.buscarMoradorSemTotemOuInativoPorCondominio");
		if (null == condominio) {
			queryMorador.setParameter("idCondominio", null);
		} else {
			queryMorador.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		final List<Object[]> listaMorador = new ArrayList<Object[]>();
		try {
			listaMorador.addAll(queryMorador.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		final List<TotemDTO> listaParseMorador = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaMorador) {
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create((Totem) objetos[0]);
			adicionar.setNome((String) objetos[1]);
			adicionar.setCasa((String) objetos[2]);
			listaParseMorador.add(adicionar);
		}

		final Query queryMoradorSemTotem = getEntityManager().createNamedQuery("Totem.buscarMoradorSemTotemOuInativoPorCondominioSemTotem");
		if (null == condominio) {
			queryMoradorSemTotem.setParameter("idCondominio", null);
		} else {
			queryMoradorSemTotem.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		final List<Object[]> listaMoradorSemTotem = new ArrayList<Object[]>();
		try {
			listaMoradorSemTotem.addAll(queryMoradorSemTotem.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<TotemDTO> listaParseSemTotem = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaMoradorSemTotem) {
			final Morador morador = (Morador) objetos[0];
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create(new Totem(morador));
			adicionar.setNome(morador.getNome());
			adicionar.setCasa((String) objetos[1]);
			listaParseSemTotem.add(adicionar);
		}

		// System.out.println(String.format(
		// "Quantidade de moradores com totem inativo no condominio: %d",
		// listaParseMorador.size()));
		// System.out.println(String.format(
		// "Quantidade de moradores sem totem no condominio: %d",
		// listaParseSemTotem.size()));

		final List<TotemDTO> conjunto = new ArrayList<TotemDTO>(listaParseMorador);
		conjunto.addAll(listaParseSemTotem);

		return conjunto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TotemDTO> buscarAgregadoSemTotemOuInativoPorCondominio(final CondominioDTO condominio) throws DAOException {
		final Query queryAgregado = getEntityManager().createNamedQuery("Totem.buscarAgregadoSemTotemOuInativoPorCondominio");
		if (null == condominio) {
			queryAgregado.setParameter("idCondominio", null);
		} else {
			queryAgregado.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		final List<Object[]> listaMorador = new ArrayList<Object[]>();
		try {
			listaMorador.addAll(queryAgregado.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		final List<TotemDTO> listaParseAgregado = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaMorador) {
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create((Totem) objetos[0]);
			adicionar.setNome((String) objetos[1]);
			adicionar.setCasa((String) objetos[2]);
			listaParseAgregado.add(adicionar);
		}

		final Query queryAgregadoSemTotem = getEntityManager().createNamedQuery("Totem.buscarAgregadoSemTotemOuInativoPorCondominioSemTotem");
		if (null == condominio) {
			queryAgregadoSemTotem.setParameter("idCondominio", null);
		} else {
			queryAgregadoSemTotem.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		final List<Object[]> listaAgregadoSemTotem = new ArrayList<Object[]>();
		try {
			listaAgregadoSemTotem.addAll(queryAgregadoSemTotem.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<TotemDTO> listaParseSemTotem = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaAgregadoSemTotem) {
			final Agregado agregado = (Agregado) objetos[0];
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create(new Totem(agregado));
			adicionar.setNome(agregado.getNome());
			adicionar.setCasa((String) objetos[1]);
			listaParseSemTotem.add(adicionar);
		}

		// System.out.println(String.format(
		// "Quantidade de agregados com totem inativo no condominio: %d",
		// listaParseAgregado.size()));
		// System.out.println(String.format(
		// "Quantidade de agregados sem totem no condominio: %d",
		// listaParseSemTotem.size()));

		final List<TotemDTO> conjunto = new ArrayList<TotemDTO>(listaParseAgregado);
		conjunto.addAll(listaParseSemTotem);

		return conjunto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TotemDTO> buscarTodosAtivosPorMorador(final MoradorDTO morador) throws DAOException {
		final Query queryMorador = getEntityManager().createNamedQuery("Totem.buscarTodosAtivosPorMorador");
		queryMorador.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		final List<Object[]> listaMorador = new ArrayList<Object[]>();
		try {
			listaMorador.addAll(queryMorador.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		final List<TotemDTO> listaParseMorador = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaMorador) {
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create((Totem) objetos[0]);
			adicionar.setNome((String) objetos[1]);
			adicionar.setCasa((String) objetos[2]);
			listaParseMorador.add(adicionar);
		}

		final Query queryAgregado = getEntityManager().createNamedQuery("Totem.buscarTodosAtivosPorMoradorAgregado");
		queryAgregado.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		final List<Object[]> listaAgregado = new ArrayList<Object[]>();
		try {
			listaAgregado.addAll(queryAgregado.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		final List<TotemDTO> listaParseAgregado = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaAgregado) {
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create((Totem) objetos[0]);
			adicionar.setNome((String) objetos[1]);
			adicionar.setCasa((String) objetos[2]);
			listaParseAgregado.add(adicionar);
		}

		// System.out.println(String.format(
		// "Quantidade de totens de moradores ativos no condominio: %d",
		// listaParseMorador.size()));
		// System.out.println(String.format(
		// "Quantidade de totens de agregados ativos no condominio: %d",
		// listaParseAgregado.size()));

		final List<TotemDTO> conjunto = new ArrayList<TotemDTO>(listaParseMorador);
		conjunto.addAll(listaParseAgregado);

		return conjunto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TotemDTO> buscarMoradorSemTotemOuInativoPorMorador(final MoradorDTO morador) throws DAOException {
		final Query queryMorador = getEntityManager().createNamedQuery("Totem.buscarMoradorSemTotemOuInativoPorMorador");
		queryMorador.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		final List<Object[]> listaMorador = new ArrayList<Object[]>();
		try {
			listaMorador.addAll(queryMorador.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		final List<TotemDTO> listaParseMorador = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaMorador) {
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create((Totem) objetos[0]);
			adicionar.setNome((String) objetos[1]);
			adicionar.setCasa((String) objetos[2]);
			listaParseMorador.add(adicionar);
		}

		final Query queryMoradorSemTotem = getEntityManager().createNamedQuery("Totem.buscarMoradorSemTotemOuInativoPorMoradorSemTotem");
		queryMoradorSemTotem.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		final List<Object[]> listaMoradorSemTotem = new ArrayList<Object[]>();
		try {
			listaMoradorSemTotem.addAll(queryMoradorSemTotem.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<TotemDTO> listaParseSemTotem = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaMoradorSemTotem) {
			final Morador moradorBusca = (Morador) objetos[0];
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create(new Totem(moradorBusca));
			adicionar.setNome(moradorBusca.getNome());
			adicionar.setCasa((String) objetos[1]);
			listaParseSemTotem.add(adicionar);
		}

		// System.out.println(String.format(
		// "Quantidade de moradores com totem inativo por morador: %d",
		// listaParseMorador.size()));
		// System.out.println(String.format(
		// "Quantidade de moradores sem totem por morador: %d",
		// listaParseSemTotem.size()));

		final List<TotemDTO> conjunto = new ArrayList<TotemDTO>(listaParseMorador);
		conjunto.addAll(listaParseSemTotem);

		return conjunto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TotemDTO> buscarAgregadoSemTotemOuInativoPorMorador(final MoradorDTO morador) throws DAOException {
		final Query queryAgregado = getEntityManager().createNamedQuery("Totem.buscarAgregadoSemTotemOuInativoPorMorador");
		queryAgregado.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		final List<Object[]> listaMorador = new ArrayList<Object[]>();
		try {
			listaMorador.addAll(queryAgregado.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		final List<TotemDTO> listaParseAgregado = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaMorador) {
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create((Totem) objetos[0]);
			adicionar.setNome((String) objetos[1]);
			adicionar.setCasa((String) objetos[2]);
			listaParseAgregado.add(adicionar);
		}

		final Query queryAgregadoSemTotem = getEntityManager().createNamedQuery("Totem.buscarAgregadoSemTotemOuInativoPorMoradorSemTotem");
		queryAgregadoSemTotem.setParameter("idMorador", morador.getPessoa().getIdPessoa());

		final List<Object[]> listaAgregadoSemTotem = new ArrayList<Object[]>();
		try {
			listaAgregadoSemTotem.addAll(queryAgregadoSemTotem.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<TotemDTO> listaParseSemTotem = new ArrayList<TotemDTO>();
		for (final Object[] objetos : listaAgregadoSemTotem) {
			final Agregado agregado = (Agregado) objetos[0];
			final TotemDTO adicionar = TotemDTO.Builder.getInstance().create(new Totem(agregado));
			adicionar.setNome(agregado.getNome());
			adicionar.setCasa((String) objetos[1]);
			listaParseSemTotem.add(adicionar);
		}

		// System.out.println(String.format(
		// "Quantidade de agregados com totem inativo para o morador: %d",
		// listaParseAgregado.size()));
		// System.out.println(String.format(
		// "Quantidade de agregados sem totem para o morador: %d",
		// listaParseSemTotem.size()));

		final List<TotemDTO> conjunto = new ArrayList<TotemDTO>(listaParseAgregado);
		conjunto.addAll(listaParseSemTotem);

		return conjunto;
	}

}