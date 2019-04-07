package br.com.lphantus.neighbor.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.LancamentoTipoDTO;
import br.com.lphantus.neighbor.entity.LancamentoTipo;
import br.com.lphantus.neighbor.repository.ILancamentoTipoDAO;
import br.com.lphantus.neighbor.service.ILancamentoTipoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class LancamentoTipoServiceImpl extends
		GenericService<Long, LancamentoTipoDTO, LancamentoTipo> implements
		ILancamentoTipoService {

	@Autowired
	private ILancamentoTipoDAO lancamentoTipoDAO;

	@Override
	protected void saveValidate(final LancamentoTipo entity)
			throws ServiceException {
		super.saveValidate(entity);
		if ((entity.getNome() == null)
				&& StringUtils.deleteWhitespace(entity.getNome()).equals("")) {
			throw new ServiceException(
					GerenciadorMensagem
							.getMensagem("LANCAMENTOTIPO_NOME_VAZIO"));
		}

	}

	@Override
	protected void updateValidate(final LancamentoTipo entity)
			throws ServiceException {
		super.updateValidate(entity);
		saveValidate(entity);
	}

}