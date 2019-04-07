package br.com.lphantus.neighbor.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.ConfiguracaoCondominio;
import br.com.lphantus.neighbor.repository.IConfiguracaoCondominioDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IConfiguracaoCondominioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ConfiguracaoCondominioServiceImpl extends GenericService<Long, ConfiguracaoCondominioDTO, ConfiguracaoCondominio> implements IConfiguracaoCondominioService {

	@Autowired
	private IConfiguracaoCondominioDAO configuracaoCondominioDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ConfiguracaoCondominioDTO buscarPorCondominio(final CondominioDTO condominio) throws ServiceException {
		ConfiguracaoCondominioDTO retorno;
		try {
			retorno = this.configuracaoCondominioDAO.buscarPorCondominio(condominio);

			if (null == retorno) {
				final ConfiguracaoCondominio entidade = new ConfiguracaoCondominio();
				entidade.setAbatimento(BigDecimal.ZERO);
				entidade.setCondominio(CondominioDTO.Builder.getInstance().createEntity(condominio));
				entidade.setDataFaturas(15);
				entidade.setDesconto(BigDecimal.ZERO);
				entidade.setLimiteDias(3L);
				entidade.setMoraOutrosRecebimentos(BigDecimal.ZERO);
				entidade.setMultaVencimento(BigDecimal.ZERO);
				entidade.setTaxaDia(BigDecimal.ZERO);
				entidade.setQtdeDiasServico(5);
				entidade.setQtdeDiasVisita(5);

				save(entidade);
				// this.configuracaoCondominioDAO.salvarComId(entidade);

				retorno = entidade.createDto();
			}

		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar configuracoes do condominio.", e);
			throw new ServiceException("Erro ao buscar configuracoes do condominio.", e);
		}
		return retorno;
	}

	@Override
	public ConfiguracaoCondominioDTO buscarPorMorador(MoradorDTO morador) throws ServiceException {
		try {
			return configuracaoCondominioDAO.buscarPorMorador(morador);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao pesquisar configuracao por morador.", exception);
			throw new ServiceException(exception.getLocalizedMessage());
		}
	}

}
