package br.com.lphantus.neighbor.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.PlanoDTO;
import br.com.lphantus.neighbor.entity.Plano;
import br.com.lphantus.neighbor.service.IPlanoService;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class PlanoServiceImpl extends GenericService<Long, PlanoDTO, Plano> implements
		IPlanoService {


}
