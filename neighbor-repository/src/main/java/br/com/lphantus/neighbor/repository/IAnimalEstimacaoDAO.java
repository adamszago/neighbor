package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.AnimalEstimacaoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.AnimalEstimacao;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IAnimalEstimacaoDAO extends IGenericDAO<AnimalEstimacao> {

	public List<AnimalEstimacaoDTO> listarAnimaisMorador(MoradorDTO morador)
			throws DAOException;

	public List<AnimalEstimacaoDTO> findByCondominio(CondominioDTO condominio)
			throws DAOException;

}
