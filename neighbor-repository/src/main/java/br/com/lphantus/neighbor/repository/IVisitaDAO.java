package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.VisitaDTO;
import br.com.lphantus.neighbor.entity.Visita;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IVisitaDAO extends IGenericDAO<Visita> {

	List<VisitaDTO> buscaVisitasAtivasConfirmadasByMorador(
			final MoradorDTO morador) throws DAOException;

	List<VisitaDTO> buscaVisitasAgendadasByMorador(final MoradorDTO morador)
			throws DAOException;

	List<VisitaDTO> buscaVisitasAgendadas(CondominioDTO condominio)
			throws DAOException;

	List<VisitaDTO> buscaVisitasByMorador(final MoradorDTO morador)
			throws DAOException;

}
