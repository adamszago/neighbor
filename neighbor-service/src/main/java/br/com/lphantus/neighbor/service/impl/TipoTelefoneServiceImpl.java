package br.com.lphantus.neighbor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.TipoTelefoneDTO;
import br.com.lphantus.neighbor.entity.TipoTelefone;
import br.com.lphantus.neighbor.repository.ITipoTelefoneDAO;
import br.com.lphantus.neighbor.service.ITipoTelefoneService;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class TipoTelefoneServiceImpl extends
		GenericService<Long, TipoTelefoneDTO, TipoTelefone> implements
		ITipoTelefoneService {

	@Autowired
	private ITipoTelefoneDAO tipoTelefoneDAO;

}
