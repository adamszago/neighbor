package br.com.lphantus.neighbor.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.UtilizacaoDTO;
import br.com.lphantus.neighbor.entity.Historico;
import br.com.lphantus.neighbor.repository.IHistoricoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class HistoricoDAOImpl extends GenericDAOImpl<Long, HistoricoDTO, Historico> implements IHistoricoDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<HistoricoDTO> findAllByIdCondominio(Long idCondominio) throws DAOException {
		Query query = getEntityManager().createNamedQuery("Historico.findAllByCondominio");
		query.setParameter("idCondominio", idCondominio);

		List<Historico> lista = new ArrayList<Historico>();
		try {
			lista.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		return HistoricoDTO.Builder.getInstance().createList(lista);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UtilizacaoDTO> consultaUtilizacao(Date dataInicio, Date dataFim, CondominioDTO condominio) throws DAOException {
		Query query = getEntityManager().createNamedQuery("Historico.consultaUtilizacao");
//		query.setParameter(1, dataInicio);
//		query.setParameter(2, dataInicio);
//		query.setParameter(3, dataFim);
//		query.setParameter(4, dataFim);
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		if ( condominio == null || condominio.getPessoa() == null ){
//			query.setParameter(5, null);
//			query.setParameter(6, null);
			query.setParameter("idCondominio", null);
		}else{
//			query.setParameter(5, condominio.getPessoa().getIdPessoa());
//			query.setParameter(6, condominio.getPessoa().getIdPessoa());
			query.setParameter("idCondominio", condominio.getPessoa().getIdPessoa());
		}
		
		List<Object[]> lista = new ArrayList<Object[]>();
		List<UtilizacaoDTO> retorno = new ArrayList<UtilizacaoDTO>();
		try {
			lista.addAll(query.getResultList());
			obterUtilizacao(lista, retorno);
		} catch (NoResultException nre) {
			// nao fazer nada
		}
		
		return retorno;
	}

	private void obterUtilizacao(List<Object[]> lista, List<UtilizacaoDTO> retorno) {
		for(Object[] linha: lista){
			UtilizacaoDTO utilizacao = new UtilizacaoDTO();
			utilizacao.setIdCondominio(linha[0] == null ? 0L : ((BigInteger) linha[0]).longValue());
			
			utilizacao.setHora0(linha[1] == null ? 0L : ((BigDecimal) linha[1]).longValue());
			utilizacao.setHora1(linha[2] == null ? 0L : ((BigDecimal) linha[2]).longValue());
			utilizacao.setHora2(linha[3] == null ? 0L : ((BigDecimal) linha[3]).longValue());
			utilizacao.setHora3(linha[4] == null ? 0L : ((BigDecimal) linha[4]).longValue());
			utilizacao.setHora4(linha[5] == null ? 0L : ((BigDecimal) linha[5]).longValue());
			utilizacao.setHora5(linha[6] == null ? 0L : ((BigDecimal) linha[6]).longValue());
			utilizacao.setHora6(linha[7] == null ? 0L : ((BigDecimal) linha[7]).longValue());
			utilizacao.setHora7(linha[8] == null ? 0L : ((BigDecimal) linha[8]).longValue());
			utilizacao.setHora8(linha[9] == null ? 0L : ((BigDecimal) linha[9]).longValue());
			utilizacao.setHora9(linha[10] == null ? 0L : ((BigDecimal) linha[10]).longValue());
			utilizacao.setHora10(linha[11] == null ? 0L : ((BigDecimal) linha[11]).longValue());
			utilizacao.setHora11(linha[12] == null ? 0L : ((BigDecimal) linha[12]).longValue());
			utilizacao.setHora12(linha[13] == null ? 0L : ((BigDecimal) linha[13]).longValue());
			utilizacao.setHora13(linha[14] == null ? 0L : ((BigDecimal) linha[14]).longValue());
			utilizacao.setHora14(linha[15] == null ? 0L : ((BigDecimal) linha[15]).longValue());
			utilizacao.setHora15(linha[16] == null ? 0L : ((BigDecimal) linha[16]).longValue());
			utilizacao.setHora16(linha[17] == null ? 0L : ((BigDecimal) linha[17]).longValue());
			utilizacao.setHora17(linha[18] == null ? 0L : ((BigDecimal) linha[18]).longValue());
			utilizacao.setHora18(linha[19] == null ? 0L : ((BigDecimal) linha[19]).longValue());
			utilizacao.setHora19(linha[20] == null ? 0L : ((BigDecimal) linha[20]).longValue());
			utilizacao.setHora20(linha[21] == null ? 0L : ((BigDecimal) linha[21]).longValue());
			utilizacao.setHora21(linha[22] == null ? 0L : ((BigDecimal) linha[22]).longValue());
			utilizacao.setHora22(linha[23] == null ? 0L : ((BigDecimal) linha[23]).longValue());
			utilizacao.setHora23(linha[24] == null ? 0L : ((BigDecimal) linha[24]).longValue());
			
			retorno.add(utilizacao);
		}
	}

}
