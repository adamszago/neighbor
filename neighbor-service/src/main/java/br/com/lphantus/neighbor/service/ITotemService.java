package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ServicoPrestadoDTO;
import br.com.lphantus.neighbor.common.TotemDTO;
import br.com.lphantus.neighbor.common.VisitaDTO;
import br.com.lphantus.neighbor.entity.Totem;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface ITotemService extends IGenericService<Long, TotemDTO, Totem> {

	void cadastrarSenhaTotem(TotemDTO totem) throws ServiceException;

	TotemDTO buscarAgregadoTotem(AgregadoDTO agregado) throws ServiceException;

	TotemDTO buscarMoradorTotem(MoradorDTO morador) throws ServiceException;

	List<TotemDTO> findAllAtivos() throws ServiceException;

	List<TotemDTO> buscarTodosAtivosPorCondominio(CondominioDTO condominio)
			throws ServiceException;

	List<TotemDTO> buscarMoradorSemTotemOuInativoPorCondominio(
			CondominioDTO condominio) throws ServiceException;

	List<TotemDTO> buscarAgregadoSemTotemOuInativoPorCondominio(
			CondominioDTO condominio) throws ServiceException;

	List<TotemDTO> buscarTodosAtivosPorMorador(MoradorDTO morador)
			throws ServiceException;

	List<TotemDTO> buscarMoradorSemTotemOuInativoPorMorador(MoradorDTO morador)
			throws ServiceException;

	List<TotemDTO> buscarAgregadoSemTotemOuInativoPorMorador(MoradorDTO morador)
			throws ServiceException;

	// ------------------------------------ agendamento

	void geraArquivoSenhas(TotemDTO totem);

	TotemDTO registrarTotemVisitaAgendada(VisitaDTO visitaAgendada)
			throws ServiceException;

	TotemDTO registrarTotemServicoAgendado(ServicoPrestadoDTO servicoAgendado)
			throws ServiceException;

	void atualizaVisitasPrestacoesServico(List<String[]> listaDadosArquivo)
			throws ServiceException;

}