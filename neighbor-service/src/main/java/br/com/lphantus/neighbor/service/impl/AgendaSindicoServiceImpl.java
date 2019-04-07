package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.AgendaSindicoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.component.AgendaSindicoEvento;
import br.com.lphantus.neighbor.entity.AgendaSindico;
import br.com.lphantus.neighbor.entity.Condominio;
import br.com.lphantus.neighbor.repository.IAgendaSindicoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IAgendaSindicoService;
import br.com.lphantus.neighbor.service.ICondominioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class AgendaSindicoServiceImpl extends GenericService<Long, AgendaSindicoDTO, AgendaSindico>
		implements IAgendaSindicoService {

	@Autowired
	private IAgendaSindicoDAO agendaDAO;

	@Autowired
	private ICondominioService condominioService;

	@Override
	public void gravarAgendaSindico(final AgendaSindicoEvento evento,
			final CondominioDTO condominio) throws ServiceException {

		if (evento.getStartDate() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("AGND_INF_INI"));
		}

		if (StringUtils.isBlank(evento.getTitle())) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("AGND_INF_TIT"));
		}

		final Condominio entidadeCondominio = this.condominioService
				.findById(condominio.getPessoa().getIdPessoa());
		final AgendaSindico entidade = evento.criarEntidade();
		entidade.setCondominio(entidadeCondominio);
		save(entidade);
	}

	@Override
	public void atualizarAgendaSindico(final AgendaSindicoEvento evento)
			throws ServiceException {
		if (evento.getStartDate() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("AGND_INF_INI"));
		}

		if (StringUtils.isBlank(evento.getTitle())) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("AGND_INF_TIT"));
		}

		try {
			this.agendaDAO.atualizarAgendaSindico(evento.criarEntidade());
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<AgendaSindico> listarPorCondominio(
			final CondominioDTO condominio) throws ServiceException {
		try {
			return this.agendaDAO.listarPorCondominio(condominio);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

}
