package br.com.lphantus.neighbor.repository.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.TelefoneDTO;
import br.com.lphantus.neighbor.entity.Telefone;
import br.com.lphantus.neighbor.repository.ITelefoneDAO;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class TelefoneDAOImpl extends
		GenericDAOImpl<Long, TelefoneDTO, Telefone> implements ITelefoneDAO {

}
