package br.com.lphantus.neighbor.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CarteiraDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.entity.Carteira;
import br.com.lphantus.neighbor.repository.ICarteiraDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class CarteiraDAOImpl extends GenericDAOImpl<Long, CarteiraDTO, Carteira> implements ICarteiraDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<CarteiraDTO> buscarPorParametros(final CondominioDTO condominio, final Boolean ativo) throws DAOException {

		final Query query = getEntityManager().createNamedQuery("Carteira.buscarPorParametros");

		Long idCondominio;
		if (null == condominio) {
			idCondominio = null;
		} else {
			idCondominio = condominio.getPessoa().getIdPessoa();
		}

		query.setParameter("ativo", ativo);
		query.setParameter("idCondominio", idCondominio);

		final List<Carteira> listaTemp = new ArrayList<Carteira>();
		try {
			listaTemp.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		final List<CarteiraDTO> retorno = new ArrayList<CarteiraDTO>();
		retorno.addAll(CarteiraDTO.Builder.getInstance().createList(listaTemp));

		return retorno;

	}

	@Override
	public void atualizarCaixa(final MovimentacaoDTO movimentacao, final boolean ehEntrada) throws DAOException {
		// achar a carteira da movimentacao
		final Query query = getEntityManager().createNamedQuery("Carteira.buscaCarteiraMovimentacao");
		query.setParameter("idMovimentacao", movimentacao.getId());

		final BigDecimal bd = (BigDecimal) query.getSingleResult();

		final BigDecimal soma;
		if (ehEntrada) {
			soma = bd.add(movimentacao.getValor());
		} else {
			soma = bd.subtract(movimentacao.getValor());
		}

		final Query queryUpdate = getEntityManager().createNamedQuery("Carteira.atualizarCaixa");
		queryUpdate.setParameter("novoSaldo", soma);
		queryUpdate.setParameter("idCarteira", movimentacao.getCarteira().getId());

		queryUpdate.executeUpdate();
	}

	@Override
	public void atualizaBoleto(final CarteiraDTO carteira) throws DAOException {
		final Carteira cart = findById(Carteira.class, carteira.getId());
		Long numero = Long.valueOf(cart.getNossoNumero()) + 1L;
		int size = cart.getNossoNumero().length();
		cart.setNossoNumero(StringUtils.leftPad(numero.toString(), size, BigDecimal.ZERO.longValue() + ""));
		update(cart);
	}
	
}