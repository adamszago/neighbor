package br.com.lphantus.neighbor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.TelefoneDTO;
import br.com.lphantus.neighbor.entity.Telefone;
import br.com.lphantus.neighbor.repository.ITelefoneDAO;
import br.com.lphantus.neighbor.service.ITelefoneService;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class TelefoneServiceImpl extends
		GenericService<Long, TelefoneDTO, Telefone> implements ITelefoneService {

	@Autowired
	private ITelefoneDAO telefoneDAO;

}
