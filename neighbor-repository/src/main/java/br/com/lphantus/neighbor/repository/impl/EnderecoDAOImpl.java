package br.com.lphantus.neighbor.repository.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.EnderecoDTO;
import br.com.lphantus.neighbor.entity.Endereco;
import br.com.lphantus.neighbor.repository.IEnderecoDAO;

@Repository
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class EnderecoDAOImpl extends
		GenericDAOImpl<Long, EnderecoDTO, Endereco> implements IEnderecoDAO {

}
