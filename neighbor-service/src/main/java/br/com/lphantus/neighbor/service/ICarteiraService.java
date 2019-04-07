package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CarteiraDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.entity.Carteira;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface ICarteiraService extends IGenericService<Long, CarteiraDTO, Carteira> {

	void atualizarCaixa(MovimentacaoDTO movimentacaoDTO, final boolean ehEntrada) throws ServiceException;

	List<CarteiraDTO> buscarPorParametros(CondominioDTO condominio, Boolean ativo) throws ServiceException;

	void atualizaBoleto(CarteiraDTO carteira) throws ServiceException;

}