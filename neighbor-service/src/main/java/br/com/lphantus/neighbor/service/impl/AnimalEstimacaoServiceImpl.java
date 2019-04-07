package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.AnimalEstimacaoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.AnimalEstimacao;
import br.com.lphantus.neighbor.repository.IAnimalEstimacaoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IAnimalEstimacaoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class AnimalEstimacaoServiceImpl extends GenericService<Long, AnimalEstimacaoDTO, AnimalEstimacao> implements IAnimalEstimacaoService {

	@Autowired
	private IAnimalEstimacaoDAO animalEstimacaoDAO;

	@Override
	public List<AnimalEstimacaoDTO> listarAnimaisMorador(final MoradorDTO morador) throws ServiceException {
		try {
			return this.animalEstimacaoDAO.listarAnimaisMorador(morador);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	
	@Override
	protected void saveValidate(AnimalEstimacao entity) throws ServiceException {
		if (entity.getDono() == null) {
			throw new ServiceException("O morador dono do animal não foi informado");
		}
		if ((entity.getTipoAnimal() == null) || "".equals(entity.getTipoAnimal())) {
			throw new ServiceException("O tipo do animal é obrigatório");
		}
	}
	
	@Override
	protected void updateValidate(AnimalEstimacao entity) throws ServiceException {
		if (entity.getDono() == null) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("SELECIONE_MORADOR"));
		}
		if ((entity.getTipoAnimal() == null) || "".equals(entity.getTipoAnimal())) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("TIPO_ANIMAL_NAO_INFORMADO"));
		}
	}
	
	@Override
	public List<AnimalEstimacaoDTO> findByCondominio(final CondominioDTO condominio) throws ServiceException {
		try {
			return this.animalEstimacaoDAO.findByCondominio(condominio);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar por condominio.", exception);
			throw new ServiceException("Erro ao buscar por condominio.", exception);
		}
	}
}
