package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ServicoPrestadoDTO;
import br.com.lphantus.neighbor.entity.ServicoPrestado;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IServicoPrestadoService extends
		IGenericService<Long, ServicoPrestadoDTO, ServicoPrestado> {

	public List<ServicoPrestadoDTO> getPrestacaoServicoByMorador(
			final MoradorDTO morador) throws ServiceException;

	public List<ServicoPrestadoDTO> getServicosAtivosConfirmadosByMorador(
			final MoradorDTO morador) throws ServiceException;

	public List<ServicoPrestadoDTO> buscarServicosAgendadosMorador(
			final MoradorDTO morador) throws ServiceException;

	public List<ServicoPrestadoDTO> buscarServicosAgendados(
			CondominioDTO condominio) throws ServiceException;

	public void salvarServicoAgendado(final ServicoPrestadoDTO servicoAgendado)
			throws ServiceException;

	void confirmarServicoAgendado(final ServicoPrestadoDTO servicoPrestado)
			throws ServiceException;

	public void removerPrestadorAgendado(ServicoPrestadoDTO servicoAgendado) throws ServiceException;

}
