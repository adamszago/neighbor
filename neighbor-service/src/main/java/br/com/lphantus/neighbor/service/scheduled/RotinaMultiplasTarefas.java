package br.com.lphantus.neighbor.service.scheduled;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.lphantus.neighbor.service.IMailManager;

/**
 * Executa uma rotina com multiplas tarefas, uma por vez, sem influenciar nas
 * demais.
 * 
 * @author elias.policena@lphantus.com.br
 * @since 25/11/2014
 *
 */
@Component
public class RotinaMultiplasTarefas {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IMailManager manager;
	
	public final void executaRotinas(List<IProcessamento> tarefas) {
		for (IProcessamento tarefa : tarefas) {
			try {
				tarefa.process();
			} catch (Exception e) {
				logger.error(String.format(
						"Ocorreu um erro nao tratado na rotina %s", e
								.getClass().getName()), e);
				manager.enviarEmailErroProcessamentoRotina(getTipoRotina(), e);
			}
		}
	}

	public String getTipoRotina() {
		return EnumTipoRotina.ABSTRATA.getNome();
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

}
