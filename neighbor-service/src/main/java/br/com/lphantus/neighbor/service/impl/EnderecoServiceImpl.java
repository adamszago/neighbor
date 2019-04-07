package br.com.lphantus.neighbor.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.EnderecoDTO;
import br.com.lphantus.neighbor.entity.Endereco;
import br.com.lphantus.neighbor.service.IEnderecoService;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class EnderecoServiceImpl extends GenericService<Long, EnderecoDTO, Endereco> implements IEnderecoService {

}
