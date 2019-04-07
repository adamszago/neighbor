package br.com.lphantus.neighbor.repository.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.TipoTelefoneDTO;
import br.com.lphantus.neighbor.entity.TipoTelefone;
import br.com.lphantus.neighbor.repository.ITipoTelefoneDAO;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class TipoTelefoneDAOImpl extends
		GenericDAOImpl<Long, TipoTelefoneDTO, TipoTelefone> implements
		ITipoTelefoneDAO {

}
