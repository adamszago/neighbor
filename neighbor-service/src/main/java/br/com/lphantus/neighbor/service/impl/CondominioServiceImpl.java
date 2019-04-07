package br.com.lphantus.neighbor.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.entity.Condominio;
import br.com.lphantus.neighbor.repository.ICondominioDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.ICondominioService;
import br.com.lphantus.neighbor.service.IEnderecoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class CondominioServiceImpl extends GenericService<Long, CondominioDTO, Condominio>
		implements ICondominioService {

	@Autowired
	private ICondominioDAO condominioDAO;

	@Autowired
	private IEnderecoService enderecoService;
	
	@Override
	protected void saveValidate(Condominio entity) throws ServiceException {
		super.saveValidate(entity);
		if ((entity.getNome() == null)
				|| "".equals(entity.getNome())
				|| entity.getNome().equals(" ")
				|| (entity.getNome().length() < 3)) {
			throw new ServiceException("O nome do Condomínio é obrigatório");
		}
		
		entity.setDataCadastro(new Date());
		
		if (null != entity.getEndereco()) {
			this.enderecoService.save(entity.getEndereco());
		}
	}

	@Override
	public void update(final Condominio condominio) throws ServiceException {
		try {
			if (null == condominio.getEndereco().getIdEndereco()) {
				this.enderecoService.save(condominio.getEndereco());
			} else {
				this.enderecoService.update(condominio.getEndereco());
			}
			this.condominioDAO.update(condominio);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public CondominioDTO buscarPorNome(final String nome)
			throws ServiceException {
		try {
			return this.condominioDAO.buscarPorNome(nome);
		} catch (final DAOException daoException) {
			throw new ServiceException("Erro ao buscar condominio.",
					daoException);
		}
	}

	@Override
	public List<CondominioDTO> buscarTodos() throws ServiceException {
		try {
			return this.condominioDAO.buscarTodos();
		} catch (final DAOException daoException) {
			throw new ServiceException("Erro ao buscar condominios.",
					daoException);
		}
	}

}
