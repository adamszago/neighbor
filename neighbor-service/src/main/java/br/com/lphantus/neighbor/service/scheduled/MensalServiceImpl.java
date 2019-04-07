package br.com.lphantus.neighbor.service.scheduled;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.scheduled.lancamento.ILancamentoMensal;

@Service
@Scope("singleton")
@Transactional(propagation = Propagation.SUPPORTS)
public class MensalServiceImpl extends RotinaMultiplasTarefas {

	@Autowired
	private ILancamentoMensal lancamentoMensal;

	// executa diariamente as 3:20
	@Scheduled(cron = "0 20 3 * * *")
	public void processaViaTimeout() throws ServiceException {
		// caso queira criar novas rotinas, baixa criar o componente e colocar no array abaixo
		executaRotinas(Arrays.asList(new IProcessamento[] { lancamentoMensal }));
	}
	
	@Override
	public String getTipoRotina() {
		return EnumTipoRotina.MENSAL.getNome();
	}

}
