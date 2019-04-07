package br.com.lphantus.neighbor.repository.impl;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ContratoDTO;
import br.com.lphantus.neighbor.entity.Contrato;
import br.com.lphantus.neighbor.repository.IContratoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ContratoDAOImpl extends
		GenericDAOImpl<Long, ContratoDTO, Contrato> implements IContratoDAO {

	@Override
	public ContratoDTO buscarContratoPorCondominio(
			final CondominioDTO condominio) throws DAOException {
		ContratoDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery(
				"Contrato.buscarContratoPorCondominio");
		query.setParameter("idPessoa", condominio.getPessoa().getIdPessoa());
		try {
			final Contrato objeto = (Contrato) query.getSingleResult();
			retorno = objeto.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException(
					"Mais de um contrato encontrado para o condominio.");
		}
		return retorno;
	}

}
