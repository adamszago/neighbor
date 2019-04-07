package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.entity.DuplicataParcela;
import br.com.lphantus.neighbor.entity.DuplicataParcelaPK;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IDuplicataParcelaService
		extends
		IGenericService<DuplicataParcelaPK, DuplicataParcelaDTO, DuplicataParcela> {

	void baixarParcela(DuplicataParcelaDTO parcelaBaixa)
			throws ServiceException;

	List<DuplicataParcelaDTO> buscarPorDuplicata(DuplicataDTO duplicataOrigem)
			throws ServiceException;

}
