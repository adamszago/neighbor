package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MarcaVeiculoDTO;
import br.com.lphantus.neighbor.entity.MarcaVeiculo;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IMarcaVeiculoService extends
		IGenericService<Long, MarcaVeiculoDTO, MarcaVeiculo> {

	public List<MarcaVeiculoDTO> findAtivos() throws ServiceException;

	public List<MarcaVeiculoDTO> buscarPorCondominio(CondominioDTO condominio)
			throws ServiceException;

}
