package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.BoletaDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Boleta;
import br.com.lphantus.neighbor.entity.Pessoa;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IBoletoDAO extends IGenericDAO<Boleta> {

	Pessoa buscarPessoa(Long idPessoa) throws DAOException;

	Long buscaProximoIdBoleto() throws DAOException;

	List<BoletaDTO> listarBoletos(UsuarioDTO sacado) throws DAOException;

}
