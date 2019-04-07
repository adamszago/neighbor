package br.com.lphantus.neighbor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.SindicoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.Sindico;
import br.com.lphantus.neighbor.entity.SindicoCondominio;
import br.com.lphantus.neighbor.repository.ISindicoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.ISindicoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class SindicoServiceImpl extends
		GenericService<Long, SindicoDTO, Sindico> implements ISindicoService {

	@Autowired
	private ISindicoDAO sindicoDAO;

	@Override
	protected void saveValidate(Sindico entity) throws ServiceException {
		super.saveValidate(entity);
		final PessoaFisica pessoa = (PessoaFisica) entity.getPessoa();
		if ((pessoa.getNome() == null) || "".equals(pessoa.getNome())) {
			throw new ServiceException("Favor informar o nome do Síndico");
		}
		if ((pessoa.getMail() == null) || "".equals(pessoa.getMail())) {
			throw new ServiceException("Favor informar o e-mail do Síndico");
		}
		if ((pessoa.getCpf() == null) || "".equals(pessoa.getCpf())) {
			throw new ServiceException("Favor informar o CPF do Síndico");
		}
		final SindicoCondominio sindicoCondominio = entity.getCondominio().get(
				0);
		if ((sindicoCondominio.getInicioMandato() == null)
				|| "".equals(sindicoCondominio.getInicioMandato())) {
			throw new ServiceException(
					"Favor informar a data do início do mandato");
		}
		if ((sindicoCondominio.getFimMandato() == null)
				|| "".equals(sindicoCondominio.getFimMandato())) {
			throw new ServiceException(
					"Favor informar a data do fim do mandato");
		}
		if (sindicoCondominio.getFimMandato().before(
				sindicoCondominio.getInicioMandato())) {
			throw new ServiceException(
					"A data do fim do mandato não pode ser anterior ao início do mandato");
		}
	}

	@Override
	public SindicoDTO buscarSindico(final UsuarioDTO usuario)
			throws ServiceException {
		try {
			return this.sindicoDAO.buscarSindico(usuario);
		} catch (final DAOException e) {
			getLogger().error("ERRO: " + e);
			throw new ServiceException(e.getMessage());
		}
	}

}
