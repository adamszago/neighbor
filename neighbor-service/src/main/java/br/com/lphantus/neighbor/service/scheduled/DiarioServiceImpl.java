package br.com.lphantus.neighbor.service.scheduled;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.scheduled.lancamento.ILancamentoDiario;
import br.com.lphantus.neighbor.service.scheduled.lancamento.IMovimentaLancamentoPagar;
import br.com.lphantus.neighbor.service.scheduled.manifold.ITotemDuplicadoAgregado;
import br.com.lphantus.neighbor.service.scheduled.manifold.ITotemDuplicadoMorador;
import br.com.lphantus.neighbor.service.scheduled.manifold.ITotemDuplicadoPrestador;
import br.com.lphantus.neighbor.service.scheduled.manifold.ITotemDuplicadoVisitante;

@Service
@Scope("singleton")
@Transactional(propagation = Propagation.SUPPORTS)
public class DiarioServiceImpl extends RotinaMultiplasTarefas {

	@Autowired
	private ILancamentoDiario lancamentoDiario;

	@Autowired
	private ITotemDuplicadoAgregado totemDuplicadoAgregado;

	@Autowired
	private ITotemDuplicadoMorador totemDuplicadoMorador;

	@Autowired
	private ITotemDuplicadoPrestador totemDuplicadoPrestador;

	@Autowired
	private ITotemDuplicadoVisitante totemDuplicadoVisitante;
	
	@Autowired
	private IMovimentaLancamentoPagar movimentaLancamentoPagar;

	// executa diariamente as 3:10
	@Scheduled(cron = "0 10 3 * * *")
	public void processaViaTimeout() throws ServiceException {
		// caso queira criar novas rotinas, baixa criar o componente e colocar
		// no array abaixo
		executaRotinas(Arrays.asList(new IProcessamento[] { lancamentoDiario, totemDuplicadoAgregado, 
				totemDuplicadoMorador, totemDuplicadoPrestador, totemDuplicadoVisitante, movimentaLancamentoPagar }));
	}

	@Override
	public String getTipoRotina() {
		return EnumTipoRotina.DIARIA.getNome();
	}

}
