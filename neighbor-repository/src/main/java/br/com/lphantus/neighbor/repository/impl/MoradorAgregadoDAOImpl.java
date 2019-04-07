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

import br.com.lphantus.neighbor.common.MoradorAgregadoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.MoradorAgregado;
import br.com.lphantus.neighbor.entity.MoradorAgregadoPK;
import br.com.lphantus.neighbor.repository.IMoradorAgregadoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MoradorAgregadoDAOImpl extends
		GenericDAOImpl<MoradorAgregadoPK, MoradorAgregadoDTO, MoradorAgregado>
		implements IMoradorAgregadoDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<MoradorAgregadoDTO> listarAgregadosAtivosMorador(
			final MoradorDTO morador) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"MoradorAgregado.listarAgregadosAtivosMorador");
		query.setParameter("idPessoa", morador.getPessoa().getIdPessoa());

		final List<MoradorAgregado> agregados = new ArrayList<MoradorAgregado>();
		try {
			agregados.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<MoradorAgregadoDTO> retorno = new ArrayList<MoradorAgregadoDTO>();
		for (final MoradorAgregado item : agregados) {
			final MoradorAgregadoDTO mDto = item.createDto();
			mDto.setAgregado(item.getAgregado().createDto());
			retorno.add(mDto);
		}

		return retorno;
	}
}