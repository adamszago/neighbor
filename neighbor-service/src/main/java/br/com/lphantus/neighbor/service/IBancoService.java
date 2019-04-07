package br.com.lphantus.neighbor.service;

import br.com.lphantus.neighbor.common.BancoDTO;
import br.com.lphantus.neighbor.entity.Banco;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IBancoService extends IGenericService<Long, BancoDTO, Banco> {

	void gravarBanco(BancoDTO entidade) throws ServiceException;

	boolean existeBanco(Long id) throws ServiceException;

	void alterarBanco(BancoDTO entidade) throws ServiceException;

	Integer obtemSizeCarteira(String banco);

	Integer obtemSizeNossoNumero(String banco, String numeroCarteira);
}
