package br.com.lphantus.neighbor.service.scheduled.lancamento.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import br.com.lphantus.neighbor.entity.TemplateLancamento;
import br.com.lphantus.neighbor.service.IGeraLancamentoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.scheduled.lancamento.ILancamentoMensal;
import br.com.lphantus.neighbor.utils.Constantes.ConstantesJPA;
import br.com.lphantus.neighbor.utils.DateUtil;

@Component
@Scope("singleton")
@Transactional(propagation = Propagation.SUPPORTS)
public class LancamentoMensalComponent implements ILancamentoMensal {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext(unitName = ConstantesJPA.PERSISTENCE_UNIT)
	private EntityManager manager;

	@Autowired
	private IGeraLancamentoService geraLancamentoService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void process() throws ServiceException {
		try {
			final DateFormat dfBrl = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			final Query query = this.manager.createNamedQuery("TemplateLancamento.findAllMensal");
			@SuppressWarnings("unchecked")
			final List<TemplateLancamento> templates = query.getResultList();

			final Date dataHoje = DateUtil.obterDataSemHora(new Date());

			for (final TemplateLancamento registro : templates) {
				if (DateUtil.obterDataSemHora(registro.getParamRepeticao()).equals(dataHoje)) {
					this.geraLancamentoService.gerarLancamentos(registro);
					registro.setParamRepeticao(DateUtil.obterProximoMes(dataHoje));
					this.manager.merge(registro);
				} else {
					logger.info(String.format("%s: %s", "Data objeto", dfBrl.format(registro.getParamRepeticao())));
					logger.info(String.format("%s: %s", "Data hoje", dfBrl.format(dataHoje)));
				}
			}
		} catch (ServiceException e) {
			logger.error("Ocorreu um erro ao realizar processamento dos lancamentos mensais.", e);
			throw e;
		}
	}

}
