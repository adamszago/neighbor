package br.com.lphantus.neighbor.service.integracao.batch;

import br.com.lphantus.neighbor.common.TotemDTO;

public interface IGeradorTotem extends Runnable {

	public void executar(TotemDTO totem);

}
