package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PrestadorServicoDTO;
import br.com.lphantus.neighbor.common.ServicoPrestadoDTO;
import br.com.lphantus.neighbor.entity.ServicoPrestado;
import br.com.lphantus.neighbor.repository.IServicoPrestadoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.utils.DateUtil;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ServicoPrestadoDAOImpl extends
		GenericDAOImpl<Long, ServicoPrestadoDTO, ServicoPrestado> implements
		IServicoPrestadoDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<ServicoPrestadoDTO> buscarServicoPorMorador(
			final MoradorDTO morador) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"ServicoPrestado.buscarServicoPorMorador");
		query.setParameter("idPessoa", morador.getPessoa().getIdPessoa());

		final List<ServicoPrestado> servicos = new ArrayList<ServicoPrestado>();
		try {
			servicos.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return ServicoPrestadoDTO.Builder.getInstance().createList(servicos);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServicoPrestadoDTO> buscarServicosAgendados(
			final CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"ServicoPrestado.buscarServicosAgendados");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}

		final List<ServicoPrestado> servicos = new ArrayList<ServicoPrestado>();
		try {
			servicos.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<ServicoPrestadoDTO> allServicos = new ArrayList<ServicoPrestadoDTO>();

		for (final ServicoPrestado servicoPrestado : servicos) {
			if (null == servicoPrestado.getInicioAgendamentoServico()
					|| !DateUtil.dataEMenorQueHoje(servicoPrestado
							.getInicioAgendamentoServico())) {
				final PrestadorServicoDTO prestador = servicoPrestado
						.getPrestadorServico().createDto();
				final ServicoPrestadoDTO agendado = servicoPrestado.createDto();
				agendado.setPrestadorServico(prestador);
				allServicos.add(agendado);
			}
		}

		return allServicos;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ServicoPrestadoDTO> buscarServicosAgendadosMorador(
			final MoradorDTO morador) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"ServicoPrestado.buscarServicosAgendadosMorador");
		query.setParameter("idPessoa", morador.getPessoa().getIdPessoa());

		final List<ServicoPrestado> todosServicos = new ArrayList<ServicoPrestado>();
		try {
			todosServicos.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<ServicoPrestadoDTO> allServicos = new ArrayList<ServicoPrestadoDTO>();

		for (final ServicoPrestado servico : todosServicos) {
			if (servico.getMorador().getIdPessoa()
					.equals(morador.getPessoa().getIdPessoa())) {
				if (null == servico.getInicioAgendamentoServico()
						|| !DateUtil.dataEMenorQueHoje(servico
								.getInicioAgendamentoServico())) {
					final PrestadorServicoDTO prestador = servico
							.getPrestadorServico().createDto();
					final ServicoPrestadoDTO agendado = servico.createDto();
					agendado.setPrestadorServico(prestador);
					allServicos.add(agendado);
				}
			}
		}

		return allServicos;
	}

}
