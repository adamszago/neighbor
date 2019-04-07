package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DocumentoDTO;
import br.com.lphantus.neighbor.entity.Documento;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IDocumentoDAO extends IGenericDAO<Documento> {

	public List<DocumentoDTO> findByCondominio(CondominioDTO condominio)
			throws DAOException;

	public DocumentoDTO salvarNovoDocumento(DocumentoDTO entity) throws DAOException;

}
