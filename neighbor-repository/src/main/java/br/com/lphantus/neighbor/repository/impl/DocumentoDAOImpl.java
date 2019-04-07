package br.com.lphantus.neighbor.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DocumentoDTO;
import br.com.lphantus.neighbor.entity.Documento;
import br.com.lphantus.neighbor.repository.IDocumentoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class DocumentoDAOImpl extends
		GenericDAOImpl<Long, DocumentoDTO, Documento> implements IDocumentoDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentoDTO> findByCondominio(CondominioDTO condominio)
			throws DAOException {
		final Query query = getEntityManager().createNamedQuery(
				"Documento.findByCondominio");
		query.setParameter("condominio", CondominioDTO.Builder.getInstance()
				.createEntity(condominio));

		final List<Documento> documento = new ArrayList<Documento>();
		try {
			documento.addAll(query.getResultList());
		} catch (NoResultException nre) {
			// nao fazer nada
		}

		return DocumentoDTO.Builder.getInstance().createList(documento);
	}

	@Override
	public DocumentoDTO salvarNovoDocumento(DocumentoDTO entity)
			throws DAOException {
		Documento entidade = DocumentoDTO.Builder.getInstance().createEntity(
				entity);
		save(entidade);
		getEntityManager().flush();
		return entidade.createDto();
	}

}
