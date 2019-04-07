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

import br.com.lphantus.neighbor.common.AnimalEstimacaoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.AnimalEstimacao;
import br.com.lphantus.neighbor.repository.IAnimalEstimacaoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class AnimalEstimacaoDAOImpl extends
		GenericDAOImpl<Long, AnimalEstimacaoDTO, AnimalEstimacao> implements
		IAnimalEstimacaoDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<AnimalEstimacaoDTO> listarAnimaisMorador(
			final MoradorDTO morador) throws DAOException {

		final Query query = getEntityManager().createNamedQuery(
				"AnimalEstimacao.listarAnimaisMorador");
		query.setParameter("idPessoa", morador.getPessoa().getIdPessoa());

		final List<AnimalEstimacao> animais = new ArrayList<AnimalEstimacao>();
		try {
			animais.addAll(query.getResultList());
		} catch (NoResultException e) {
			// nao fazer nada
		}
		return AnimalEstimacaoDTO.Builder.getInstance().createList(animais);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnimalEstimacaoDTO> findByCondominio(
			final CondominioDTO condominio) throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"AnimalEstimacao.findByCondominio");
		if (null == condominio) {
			query.setParameter("idCondominio", null);
		} else {
			query.setParameter("idCondominio", condominio.getPessoa()
					.getIdPessoa());
		}

		final List<AnimalEstimacao> animais = new ArrayList<AnimalEstimacao>();
		try {
			animais.addAll(query.getResultList());
		} catch (NoResultException e) {
			// nao fazer nada
		}

		final List<AnimalEstimacaoDTO> retorno = new ArrayList<AnimalEstimacaoDTO>();

		// converter a lista de animais
		if ((null != animais) && !animais.isEmpty()) {
			for (final AnimalEstimacao item : animais) {
				final MoradorDTO morador = item.getDono().createDto();
				final AnimalEstimacaoDTO animalDto = item.createDto();
				animalDto.setDono(morador);
				retorno.add(animalDto);
			}
		}
		return retorno;
	}

}
