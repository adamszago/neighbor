package br.com.lphantus.neighbor.service;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ContratoDTO;
import br.com.lphantus.neighbor.entity.Contrato;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IContratoService extends
		IGenericService<Long, ContratoDTO, Contrato> {

	ContratoDTO getContratoByCondominio(CondominioDTO condominio)
			throws ServiceException;

	/**
	 * Verifica se o condominio possui limite para cadastrar mais usuarios.
	 * 
	 * 
	 * @return boolean. True se possui limite, false não possui limite.
	 * @throws ServiceException
	 */
	boolean verificarLimiteUsuarioBycondominio(CondominioDTO condominio)
			throws ServiceException;

}
