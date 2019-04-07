package br.com.lphantus.neighbor.service;

import br.com.lphantus.neighbor.entity.TemplateLancamento;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IGeraLancamentoService {

	void gerarLancamentos(TemplateLancamento templates) throws ServiceException;

}
