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

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorAgregadoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.Agregado;
import br.com.lphantus.neighbor.entity.MoradorAgregado;
import br.com.lphantus.neighbor.repository.IAgregadoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class AgregadoDAOImpl extends GenericDAOImpl<Long, AgregadoDTO, Agregado> implements IAgregadoDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<AgregadoDTO> listarAgregadosMorador(final MoradorDTO morador) throws DAOException {

		final Query query = getEntityManager().createNamedQuery("Agregado.listarAgregadosMorador");
		query.setParameter("idPessoa", morador.getPessoa().getIdPessoa());

		final List<Agregado> agregados = new ArrayList<Agregado>();
		try {
			agregados.addAll(query.getResultList());
		} catch (NoResultException e) {
			// nao fazer nada
		}
		return AgregadoDTO.Builder.getInstance().createList(agregados);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AgregadoDTO> listarTodosAgregados(final CondominioDTO condominio) throws DAOException {

		final Query query = getEntityManager().createNamedQuery("Agregado.listarTodosAgregados");
		if (null != condominio) {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		} else {
			query.setParameter("idCondominio", null);
		}

		final List<Agregado> agregados = new ArrayList<Agregado>();
		try {
			agregados.addAll(query.getResultList());
		} catch (NoResultException e) {
			// nao fazer nada
		}

		final List<AgregadoDTO> retorno = new ArrayList<AgregadoDTO>();

		for (final Agregado agregado : agregados) {
			final AgregadoDTO item = AgregadoDTO.Builder.getInstance().create(agregado);
			item.setMorador(new ArrayList<MoradorAgregadoDTO>());
			for (final MoradorAgregado registro : agregado.getMorador()) {
				final MoradorAgregadoDTO relacionamento = new MoradorAgregadoDTO();
				if (null != registro) {
					relacionamento.setDataFim(registro.getDataFim());
					relacionamento.setDataInicio(registro.getDataInicio());
					relacionamento.setParentesco(registro.getParentesco());
				}
				relacionamento.setMorador(MoradorDTO.Builder.getInstance().create(registro.getMorador()));
				relacionamento.setAgregado(item);
				item.getMorador().add(relacionamento);
			}
			retorno.add(item);
		}

		return retorno;
	}

}
