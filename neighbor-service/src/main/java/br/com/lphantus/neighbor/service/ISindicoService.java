package br.com.lphantus.neighbor.service;

import br.com.lphantus.neighbor.common.SindicoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Sindico;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface ISindicoService extends IGenericService<Long, SindicoDTO, Sindico> {

	public SindicoDTO buscarSindico(final UsuarioDTO usuario)
			throws ServiceException;

}
