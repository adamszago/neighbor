package br.com.lphantus.neighbor.service;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.ConfiguracaoCondominio;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IConfiguracaoCondominioService extends IGenericService<Long, ConfiguracaoCondominioDTO, ConfiguracaoCondominio> {

	ConfiguracaoCondominioDTO buscarPorCondominio(CondominioDTO condominio) throws ServiceException;

	ConfiguracaoCondominioDTO buscarPorMorador(final MoradorDTO morador) throws ServiceException;

}