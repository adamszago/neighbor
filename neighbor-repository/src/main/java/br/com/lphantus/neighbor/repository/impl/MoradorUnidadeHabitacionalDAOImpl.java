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
import br.com.lphantus.neighbor.common.MoradorUnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.entity.MoradorUnidadeHabitacional;
import br.com.lphantus.neighbor.entity.MoradorUnidadeHabitacionalPK;
import br.com.lphantus.neighbor.repository.IMoradorUnidadeHabitacionalDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MoradorUnidadeHabitacionalDAOImpl extends GenericDAOImpl<MoradorUnidadeHabitacionalPK, MoradorUnidadeHabitacionalDTO, MoradorUnidadeHabitacional> implements
		IMoradorUnidadeHabitacionalDAO {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<MoradorUnidadeHabitacionalDTO> listarMoradoresCondominio(final CondominioDTO condominio, final Boolean status) throws DAOException {
		Query query = getEntityManager().createNamedQuery("MoradorUnidadeHabitacional.listarMoradoresCondominio");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}

		if (null == status) {
			query.setParameter("parametroAtivo", null);
		} else {
			query.setParameter("parametroAtivo", status.booleanValue());
		}

		final List<MoradorUnidadeHabitacional> moradores = new ArrayList<MoradorUnidadeHabitacional>();
		try {
			moradores.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		// precisa ser o createFromEntity
		final List<MoradorUnidadeHabitacionalDTO> retorno = new ArrayList<MoradorUnidadeHabitacionalDTO>();
		for (final MoradorUnidadeHabitacional relacionamento : moradores) {
			retorno.add(MoradorUnidadeHabitacionalDTO.Builder.getInstance().createFromEntity(relacionamento));
		}

		return retorno;
	}
}