package br.com.lphantus.neighbor.service.scheduled.lancamento.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.service.IHistoricoService;
import br.com.lphantus.neighbor.service.IMovimentacaoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.scheduled.lancamento.IMovimentaLancamentoPagar;
import br.com.lphantus.neighbor.utils.Constantes.ConstantesJPA;

@Component
@Scope("singleton")
@Transactional(propagation = Propagation.SUPPORTS)
public class MovimentaLancamentoPagarComponent implements IMovimentaLancamentoPagar {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext(unitName = ConstantesJPA.PERSISTENCE_UNIT)
	private EntityManager manager;
	
	@Autowired
	private IMovimentacaoService movimentacaoService;
	
	@Autowired
	private IHistoricoService historicoService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void process() throws ServiceException {
		try {
			final Query query = this.manager.createNamedQuery("Lancamento.findLancamentoPagarSemMovimentacao");

			@SuppressWarnings("unchecked")
			final List<Lancamento> lancamentos = query.getResultList();

			for (final Lancamento registro : lancamentos) {
				movimentacaoService.finalizarBaixaLancamentoPagar(registro.createDto());
			}

		} catch (Exception e) {
			logger.error("Ocorreu um erro ao realizar processamento dos lancamentos sem movimentacao.", e);
			throw e;
		}
	}

}
