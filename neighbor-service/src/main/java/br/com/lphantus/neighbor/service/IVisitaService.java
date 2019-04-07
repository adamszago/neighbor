package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.VisitaDTO;
import br.com.lphantus.neighbor.entity.Visita;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IVisitaService extends
		IGenericService<Long, VisitaDTO, Visita> {

	public List<VisitaDTO> buscaVisitasByMorador(final MoradorDTO morador)
			throws ServiceException;

	public List<VisitaDTO> buscaVisitasAtivasConfirmadasByMorador(
			final MoradorDTO morador) throws ServiceException;

	public List<VisitaDTO> buscaVisitasAgendadasByMorador(
			final MoradorDTO morador) throws ServiceException;

	public List<VisitaDTO> buscaVisitasAgendadas(CondominioDTO condominio)
			throws ServiceException;

	public void salvarVisitaAgendada(final VisitaDTO visitaAgendada)
			throws ServiceException;

	public void confirmarVisitaAgendada(final VisitaDTO visita)
			throws ServiceException;

	public void removerVisitaAgendada(VisitaDTO visitaAgendada)
			throws ServiceException;

}
