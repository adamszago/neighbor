package br.com.lphantus.neighbor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.PessoaJuridicaDTO;
import br.com.lphantus.neighbor.common.PrestadorServicoDTO;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;
import br.com.lphantus.neighbor.entity.PrestadorServico;
import br.com.lphantus.neighbor.repository.IPrestadorServicoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IPrestadorServicoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

import com.google.common.base.Strings;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class PrestadorServicoServiceImpl extends GenericService<Long, PrestadorServicoDTO, PrestadorServico> implements IPrestadorServicoService {

	@Autowired
	private IPrestadorServicoDAO prestadorServicoDao;

	@Override
	public PrestadorServicoDTO buscarPorId(PrestadorServicoDTO prestador) throws ServiceException {
		try {
			return this.prestadorServicoDao.buscarPorId(prestador);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public boolean existeDocumento(final PrestadorServicoDTO prestador) throws ServiceException {
		boolean retorno = false;
		try {
			retorno = this.prestadorServicoDao.existeDocumento(prestador);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
		return retorno;
	}

	@Override
	public void save(final PrestadorServico prestador) throws ServiceException {
		if (existeDocumento(prestador.createDto())) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("DOCTO_CADASTRADO"));
		} else {
			try {
				this.prestadorServicoDao.save(prestador);
			} catch (final DAOException e) {
				throw new ServiceException(e);
			}
		}
	}

	@Override
	public void update(final PrestadorServico prestador) throws ServiceException {
		try {
			this.prestadorServicoDao.update(prestador);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PrestadorServicoDTO> buscarConfirmadosPorCondominio(final CondominioDTO condominio) throws ServiceException {
		final List<PrestadorServicoDTO> servicosConfirmados = new ArrayList<PrestadorServicoDTO>();
		List<PrestadorServico> allPrestadores = new ArrayList<PrestadorServico>();
		try {
			allPrestadores = this.prestadorServicoDao.findAll();
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}

		for (final PrestadorServico prestadorServico : allPrestadores) {
			final boolean documentoCpfPreenchido = (prestadorServico.getPessoa() instanceof PessoaFisica)
					&& !Strings.isNullOrEmpty(((PessoaFisica) prestadorServico.getPessoa()).getCpf());
			final boolean documentoCnpjPreenchido = (prestadorServico.getPessoa() instanceof PessoaJuridica)
					&& !Strings.isNullOrEmpty(((PessoaJuridica) prestadorServico.getPessoa()).getCnpj());
			if (documentoCnpjPreenchido || documentoCpfPreenchido) {
				servicosConfirmados.add(prestadorServico.createDto());
			}
		}
		return servicosConfirmados;
	}

	@Override
	public List<PrestadorServicoDTO> buscarPorCondominio(final CondominioDTO condominio) throws ServiceException {

		final List<PrestadorServicoDTO> servicosConfirmados = new ArrayList<PrestadorServicoDTO>();
		List<PrestadorServicoDTO> allPrestadores = new ArrayList<PrestadorServicoDTO>();
		try {
			allPrestadores = this.prestadorServicoDao.buscarPorCondominio(condominio);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}

		for (final PrestadorServicoDTO prestador : allPrestadores) {
			final boolean documentoCpfPreenchido = (prestador.getPessoa() instanceof PessoaFisicaDTO) && !Strings.isNullOrEmpty(((PessoaFisicaDTO) prestador.getPessoa()).getCpf());
			final boolean documentoCnpjPreenchido = (prestador.getPessoa() instanceof PessoaJuridicaDTO)
					&& !Strings.isNullOrEmpty(((PessoaJuridicaDTO) prestador.getPessoa()).getCnpj());
			if (documentoCnpjPreenchido || documentoCpfPreenchido) {
				servicosConfirmados.add(prestador);
			}
		}

		return servicosConfirmados;
	}
}
