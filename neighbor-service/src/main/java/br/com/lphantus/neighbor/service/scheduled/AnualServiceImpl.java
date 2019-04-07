package br.com.lphantus.neighbor.service.scheduled;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.scheduled.lancamento.ILancamentoAnual;

@Service
@Scope("singleton")
@Transactional(propagation = Propagation.SUPPORTS)
public class AnualServiceImpl extends RotinaMultiplasTarefas {

	@Autowired
	private ILancamentoAnual lancamentoAnual;

	// executa diariamente as 3:00
	@Scheduled(cron = "0 0 3 * * *")
	@Transactional(propagation = Propagation.SUPPORTS)
	public void processaViaTimeout() throws ServiceException {
		// caso queira criar novas rotinas, baixa criar o componente e colocar no array abaixo
		executaRotinas(Arrays.asList(new IProcessamento[] { lancamentoAnual }));
	}
	
	@Override
	public String getTipoRotina() {
		return EnumTipoRotina.ANUAL.getNome();
	}

}
