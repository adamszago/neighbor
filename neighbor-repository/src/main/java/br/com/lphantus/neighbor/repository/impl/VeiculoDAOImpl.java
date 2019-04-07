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
import br.com.lphantus.neighbor.common.VeiculoDTO;
import br.com.lphantus.neighbor.entity.Veiculo;
import br.com.lphantus.neighbor.repository.IVeiculoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class VeiculoDAOImpl extends GenericDAOImpl<Long, VeiculoDTO, Veiculo>
		implements IVeiculoDAO {

	@Override
	@SuppressWarnings("unchecked")
	public List<VeiculoDTO> listarVeiculosMorador(final MoradorDTO morador)
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Veiculo.listarVeiculosMorador");
		query.setParameter("idPessoa", morador.getPessoa().getIdPessoa());

		List<Veiculo> lista = new ArrayList<Veiculo>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return VeiculoDTO.Builder.getInstance().createList(lista);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VeiculoDTO> buscarPorCondominio(final CondominioDTO condominio)
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Veiculo.buscarPorCondominio");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}

		List<Veiculo> lista = new ArrayList<Veiculo>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return VeiculoDTO.Builder.getInstance().createList(lista);
	}

}
