package br.com.lphantus.neighbor.service.scheduled;

import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IProcessamento {

	void process() throws ServiceException;

}
