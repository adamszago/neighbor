package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.TemplateLancamentoDTO;
import br.com.lphantus.neighbor.entity.TemplateLancamento;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface ITemplateLancamentoService extends
		IGenericService<Long, TemplateLancamentoDTO, TemplateLancamento> {

	List<TemplateLancamentoDTO> buscarInativosCondominio(
			CondominioDTO condominioUsuarioLogado) throws ServiceException;

	List<TemplateLancamentoDTO> buscarAtivosCondominio(CondominioDTO condominio)
			throws ServiceException;

	List<TemplateLancamentoDTO> buscarPorCondominio(CondominioDTO condominio)
			throws ServiceException;

	void alterarStatus(Long id, boolean novoStatus) throws ServiceException;

}