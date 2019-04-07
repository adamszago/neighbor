package br.com.lphantus.neighbor.service;

import java.io.File;
import java.util.List;

import br.com.lphantus.neighbor.common.BoletaDTO;
import br.com.lphantus.neighbor.common.CarteiraDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Boleta;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IBoletoService extends IGenericService<Long, BoletaDTO, Boleta> {

	File gerarBoleto(BoletaDTO boleto) throws ServiceException;

	File gerarBoleto(CondominioDTO condominio, CarteiraDTO carteiraBaixa) throws ServiceException;

	List<BoletaDTO> listarBoletos(UsuarioDTO sacado) throws ServiceException;

}
