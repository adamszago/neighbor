package br.com.lphantus.neighbor.repository.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.LancamentoTipoDTO;
import br.com.lphantus.neighbor.entity.LancamentoTipo;
import br.com.lphantus.neighbor.repository.ILancamentoTipoDAO;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class LancamentoTipoDAOImpl extends
		GenericDAOImpl<Long, LancamentoTipoDTO, LancamentoTipo> implements
		ILancamentoTipoDAO {

}