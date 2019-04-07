package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.AgendaSindicoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.component.AgendaSindicoEvento;
import br.com.lphantus.neighbor.entity.AgendaSindico;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IAgendaSindicoService extends
		IGenericService<Long, AgendaSindicoDTO, AgendaSindico> {

	public void gravarAgendaSindico(AgendaSindicoEvento evento,
			CondominioDTO condominio) throws ServiceException;

	public void atualizarAgendaSindico(AgendaSindicoEvento evento)
			throws ServiceException;

	public List<AgendaSindico> listarPorCondominio(CondominioDTO condominio)
			throws ServiceException;

}
