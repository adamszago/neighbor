package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.entity.DuplicataParcela;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IDuplicataParcelaDAO extends IGenericDAO<DuplicataParcela> {

	void baixarParcela(DuplicataParcelaDTO parcelaBaixa) throws DAOException;

	List<DuplicataParcelaDTO> buscarPorDuplicata(DuplicataDTO duplicataOrigem)
			throws DAOException;

}