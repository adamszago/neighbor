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
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.entity.ConfiguracaoCondominio;
import br.com.lphantus.neighbor.repository.IConfiguracaoCondominioDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ConfiguracaoCondominioDAOImpl extends GenericDAOImpl<Long, ConfiguracaoCondominioDTO, ConfiguracaoCondominio> implements IConfiguracaoCondominioDAO {

	@Override
	public ConfiguracaoCondominioDTO buscarPorCondominio(final CondominioDTO condominio) throws DAOException {
		ConfiguracaoCondominioDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("ConfiguracaoCondominio.buscarPorCondominio");
		query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		try {
			final ConfiguracaoCondominio entidade = (ConfiguracaoCondominio) query.getSingleResult();
			retorno = entidade.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrada mais de uma configuracao para o condominio.");
		}
		return retorno;
	}

	@Override
	public void salvarComId(final ConfiguracaoCondominio entidade) throws DAOException {
		save(entidade);
		getEntityManager().clear();
		getEntityManager().flush();
	}

	public ConfiguracaoCondominioDTO buscarPorMorador(final MoradorDTO morador) throws DAOException {
		ConfiguracaoCondominioDTO retorno = null;
		final Query query = getEntityManager().createNamedQuery("ConfiguracaoCondominio.buscarPorMorador");
		query.setParameter("morador", PessoaFisicaDTO.Builder.getInstance().createEntityMorador(morador.getPessoa()));
		try {
			final ConfiguracaoCondominio entidade = (ConfiguracaoCondominio) query.getSingleResult();
			retorno = entidade.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException("Foi encontrada mais de uma configuracao pro condominio do morador.");
		}
		return retorno;
	}

}