package br.com.lphantus.neighbor.repository;

import br.com.lphantus.neighbor.common.SindicoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Sindico;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface ISindicoDAO extends IGenericDAO<Sindico> {

	SindicoDTO buscarSindico(UsuarioDTO usuario) throws DAOException;

}
