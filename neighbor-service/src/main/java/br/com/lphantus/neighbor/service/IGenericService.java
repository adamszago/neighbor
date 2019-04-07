package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.AbstractDTO;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IGenericService<Chave, TipoDTO extends AbstractDTO, Entidade> {

	public void save(Entidade entity) throws ServiceException;

	public void saveOrUpdate(Entidade entity) throws ServiceException;

	public void update(Entidade entity) throws ServiceException;

	public void delete(Entidade entity) throws ServiceException;

	public Entidade findById(Chave id) throws ServiceException;

	public void refresh(Entidade entity) throws ServiceException;

	public List<Entidade> findAll() throws ServiceException;

}
