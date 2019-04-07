package br.com.lphantus.neighbor.repository.impl;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.SindicoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Sindico;
import br.com.lphantus.neighbor.repository.ISindicoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class SindicoDAOImpl extends GenericDAOImpl<Long, SindicoDTO, Sindico>
		implements ISindicoDAO {

	@Override
	public SindicoDTO buscarSindico(final UsuarioDTO usuario)
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Sindico.buscarSindico");
		query.setParameter("cpf",
				((PessoaFisicaDTO) usuario.getPessoa()).getCpf());
		query.setParameter("email", usuario.getPessoa().getMail());
		SindicoDTO retorno = null;
		try {
			Sindico item = (Sindico) query.getSingleResult();
			retorno = item.createDto();
		} catch (NoResultException nre) {
			// nao fazer nada
		} catch (NonUniqueResultException nure) {
			throw new DAOException(
					"Foi encontrado mais de um sindico para o usuario.");
		}
		return retorno;
	}

}
