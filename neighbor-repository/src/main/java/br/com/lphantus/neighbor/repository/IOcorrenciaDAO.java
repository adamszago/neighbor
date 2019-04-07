package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.OcorrenciaDTO;
import br.com.lphantus.neighbor.entity.Ocorrencia;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IOcorrenciaDAO extends IGenericDAO<Ocorrencia> {

	List<OcorrenciaDTO> listarOcorrenciaMorador(MoradorDTO morador)
			throws DAOException;

	List<OcorrenciaDTO> listarOcorrenciaCondominio(CondominioDTO condominio)
			throws DAOException;

}
