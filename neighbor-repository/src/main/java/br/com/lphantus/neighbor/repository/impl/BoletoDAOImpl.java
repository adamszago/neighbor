package br.com.lphantus.neighbor.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.BoletaDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Boleta;
import br.com.lphantus.neighbor.entity.Pessoa;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;
import br.com.lphantus.neighbor.repository.IBoletoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class BoletoDAOImpl extends GenericDAOImpl<Long, BoletaDTO, Boleta> implements IBoletoDAO {

	@Override
	public Pessoa buscarPessoa(final Long idPessoa) throws DAOException {
		Pessoa p = null;
		p = (PessoaFisica) getEntityManager().find(PessoaFisica.class, idPessoa);
		if (p == null) {
			p = (PessoaJuridica) getEntityManager().find(PessoaJuridica.class, idPessoa);
		}
		return p;
	}

	@Override
	public Long buscaProximoIdBoleto() throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Boleta.buscaProximoIdBoleto");
		final Long valor = (Long) query.getSingleResult();

		Long retorno;
		if ((null == valor) || valor.equals(BigDecimal.ZERO.intValue())) {
			retorno = BigDecimal.ONE.longValue();
		} else {
			retorno = valor + BigDecimal.ONE.longValue();
		}

		return retorno;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<BoletaDTO> listarBoletos(UsuarioDTO sacado) throws DAOException {
		final Query query = getEntityManager().createNamedQuery("Boleta.listarPorPessoa");
		query.setParameter("condominio", CondominioDTO.Builder.getInstance().createEntity(sacado.getCondominio()));
		query.setParameter("sacado", PessoaFisicaDTO.Builder.getInstance().createEntity((PessoaFisicaDTO) sacado.getPessoa()));
		List<BoletaDTO> retorno = new ArrayList<BoletaDTO>();
		try {
			@SuppressWarnings("unchecked")
			List<Boleta> boletas = query.getResultList();
			retorno.addAll(BoletaDTO.Builder.getInstance().createList(boletas));
		} catch (NoResultException nore) {
			// nao fazer nada
		}

		return retorno;
	}
}
