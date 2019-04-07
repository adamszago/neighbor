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
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.UtilizacaoDTO;
import br.com.lphantus.neighbor.entity.Historico;
import br.com.lphantus.neighbor.repository.IHistoricoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.ICondominioService;
import br.com.lphantus.neighbor.service.IHistoricoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.utils.DateUtil;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class HistoricoServiceImpl extends
		GenericService<Long, HistoricoDTO, Historico> implements
		IHistoricoService {

	@Autowired
	private IHistoricoDAO historicoDao;
	
	@Autowired
	private ICondominioService condomioService;

	@Override
	public List<HistoricoDTO> findAllByIdCondominio(final Long idCondominio)
			throws ServiceException {
		try {
			return historicoDao.findAllByIdCondominio(idCondominio);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<UtilizacaoDTO> consultaUtilizacao(Date dataMes, CondominioDTO condominio) throws ServiceException {
		Date dataInicio, dataFim;
		if ( null == dataMes ){
			dataInicio = dataFim = null;
		}else{
			dataInicio = DateUtil.obterPrimeiroDiaMesPrimeiraHora(dataMes);
			dataFim = DateUtil.obterUltimoDiaMesUltimaHora(dataMes);
		}
		try {
			List<UtilizacaoDTO> retorno = historicoDao.consultaUtilizacao(dataInicio, dataFim, condominio);
			for(UtilizacaoDTO item:retorno){
				item.setCondominio(condomioService.findById(item.getIdCondominio()).createDto());
			}
			return retorno;
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
}
