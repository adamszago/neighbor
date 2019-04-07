package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.VeiculoDTO;
import br.com.lphantus.neighbor.entity.Veiculo;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IVeiculoDAO extends IGenericDAO<Veiculo> {

	public List<VeiculoDTO> listarVeiculosMorador(MoradorDTO morador)
			throws DAOException;

	public List<VeiculoDTO> buscarPorCondominio(CondominioDTO condominio)
			throws DAOException;

}
