package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.VeiculoDTO;
import br.com.lphantus.neighbor.entity.Veiculo;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IVeiculoService extends IGenericService<Long, VeiculoDTO, Veiculo> {

	public List<VeiculoDTO> listarVeiculosMorador(final MoradorDTO morador)
			throws ServiceException;

	public List<VeiculoDTO> buscarPorCondominio(CondominioDTO condominio)
			throws ServiceException;

}
