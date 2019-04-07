package br.com.lphantus.neighbor.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PrestadorServicoDTO;
import br.com.lphantus.neighbor.common.ServicoPrestadoDTO;
import br.com.lphantus.neighbor.entity.PrestadorServico;
import br.com.lphantus.neighbor.entity.ServicoPrestado;
import br.com.lphantus.neighbor.enums.EnumTempoAcesso;
import br.com.lphantus.neighbor.repository.IServicoPrestadoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IConfiguracaoCondominioService;
import br.com.lphantus.neighbor.service.IPrestadorServicoService;
import br.com.lphantus.neighbor.service.IServicoPrestadoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ServicoPrestadoServiceImpl extends
		GenericService<Long, ServicoPrestadoDTO, ServicoPrestado> implements IServicoPrestadoService {
	
	@Autowired
	private IServicoPrestadoDAO servicoPrestadoDAO;

	@Autowired
	private IPrestadorServicoService prestadorServicoService;
	
	@Autowired
	private IConfiguracaoCondominioService configuracaoCondominioService;

	@Override
	public List<ServicoPrestadoDTO> getPrestacaoServicoByMorador(
			final MoradorDTO morador) throws ServiceException {
		try {
			return this.servicoPrestadoDAO.buscarServicoPorMorador(morador);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar prestacao de servicos por morador.", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<ServicoPrestadoDTO> getServicosAtivosConfirmadosByMorador(
			final MoradorDTO morador) throws ServiceException {
		final List<ServicoPrestadoDTO> servicosDoMorador = new ArrayList<ServicoPrestadoDTO>();
		try {
			servicosDoMorador.addAll(this.servicoPrestadoDAO.buscarServicoPorMorador(morador));
			Iterator<ServicoPrestadoDTO> iterator = servicosDoMorador.iterator();
			while(iterator.hasNext()){
				ServicoPrestadoDTO servicoPrestado = iterator.next();
				if ( !servicoPrestado.isAtivo() || !servicoPrestado.isConfirmado() ){
					iterator.remove();
				}
			}
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar servicos ativos confirmados por morador.", e);
			throw new ServiceException(e.getMessage());
		}

		return servicosDoMorador;
	}

	@Override
	public List<ServicoPrestadoDTO> buscarServicosAgendadosMorador(
			final MoradorDTO morador) throws ServiceException {

		final List<ServicoPrestadoDTO> allServicos = new ArrayList<ServicoPrestadoDTO>();
		try {
			allServicos.addAll(this.servicoPrestadoDAO
					.buscarServicosAgendadosMorador(morador));
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar servicos agendados por morador.", e);
			throw new ServiceException(e.getMessage());
		}
		return allServicos;
	}

	@Override
	public List<ServicoPrestadoDTO> buscarServicosAgendados(
			final CondominioDTO condominio) throws ServiceException {
		final List<ServicoPrestadoDTO> allServicos = new ArrayList<ServicoPrestadoDTO>();
		try {
			allServicos.addAll(this.servicoPrestadoDAO
					.buscarServicosAgendados(condominio));
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar servicos agendados.", e);
			throw new ServiceException(e.getMessage());
		}
		return allServicos;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void salvarServicoAgendado(final ServicoPrestadoDTO servicoAgendado)
			throws ServiceException {
		try {

			if ( EnumTempoAcesso.ENTRADA_LIVRE.getTipo().equals(servicoAgendado.getTipoAcesso())){
				verificaEntradaLivreExcedido(servicoAgendado.getMorador());
			}else{
				if (servicoAgendado.getInicioAgendamentoServico() == null) {
					throw new ServiceException(
							"Uma data de agendamento é necessária!");
				}
			}
			if ((servicoAgendado.getPrestadorServico() == null)
					|| "".equals(servicoAgendado.getPrestadorServico()
							.getPessoa().getNome())) {
				throw new ServiceException(
						"Favor informar o prestador de serviço agendado!");
			}

			final PrestadorServico prestador = PrestadorServicoDTO.Builder
					.getInstance().createEntity(
							servicoAgendado.getPrestadorServico());
			if (prestador.getIdPrestador() == null || 
					servicoAgendado.getPrestadorServico().getPessoa().getIdPessoa() == null) {
				this.prestadorServicoService.save(prestador);
				servicoAgendado.setPrestadorServico(prestador.createDto());
			} else {
				this.prestadorServicoService.update(prestador);
			}
			
			final ServicoPrestado servico = ServicoPrestadoDTO.Builder
					.getInstance().createEntity(servicoAgendado);
			servico.setPrestadorServico(prestador);
			this.servicoPrestadoDAO.save(servico);

		} catch (final DAOException daoEx) {
			getLogger().error("Erro ao salvar servicos agendados.", daoEx);
			throw new ServiceException(daoEx.getMessage());
		}

	}

	private void verificaEntradaLivreExcedido(MoradorDTO morador) throws ServiceException, DAOException {
		ConfiguracaoCondominioDTO configuracao = configuracaoCondominioService.buscarPorMorador(morador);
		if ( null != configuracao && null != configuracao.getQtdeEntradaLivrePrestador() ){
			List<ServicoPrestadoDTO> entradaLivre = servicoPrestadoDAO.buscarServicoPorMorador(morador);
			Iterator<ServicoPrestadoDTO> iterator = entradaLivre.iterator();
			while(iterator.hasNext()){
				ServicoPrestadoDTO servico = iterator.next();
				if ( !servico.isAtivo() || !EnumTempoAcesso.ENTRADA_LIVRE.getTipo().equals(servico.getTipoAcesso()) ){
					iterator.remove();
				}
			}
			if ( configuracao.getQtdeEntradaLivrePrestador() < entradaLivre.size() ){
				throw new ServiceException("Limite de prestador de servico com entrada livre excedido!");
			}
		}
	}

	@Override
	public void confirmarServicoAgendado(
			final ServicoPrestadoDTO servicoPrestado) throws ServiceException {
		try {
			
			if ( servicoPrestado.getTipoAcesso().equals(EnumTempoAcesso.ENTRADA_LIVRE)){
				verificaEntradaLivreExcedido(servicoPrestado.getMorador());
			}else{
				if (servicoPrestado.getInicioAgendamentoServico() == null) {
					throw new ServiceException(
							"Uma data de agendamento é necessária!");
				}
			}
			if ((servicoPrestado.getPrestadorServico() == null)
					|| "".equals(servicoPrestado.getPrestadorServico()
							.getPessoa().getNome())) {
				throw new ServiceException(
						"Favor informar o visitante agendado!");
			}
			if (servicoPrestado.getPrestadorServico().getPessoa().getIdPessoa() == null) {
				this.prestadorServicoService.save(PrestadorServicoDTO.Builder
						.getInstance().createEntity(
								servicoPrestado.getPrestadorServico()));
			} else {
				this.prestadorServicoService.update(PrestadorServicoDTO.Builder
						.getInstance().createEntity(
								servicoPrestado.getPrestadorServico()));
			}
			this.servicoPrestadoDAO.update(ServicoPrestadoDTO.Builder
					.getInstance().createEntity(servicoPrestado));

		} catch (final DAOException daoEx) {
			getLogger().error("Erro ao confirmar servicos agendados.", daoEx);
			throw new ServiceException(daoEx.getMessage());
		}

	}

	@Override
	public void removerPrestadorAgendado(ServicoPrestadoDTO servicoAgendado)
			throws ServiceException {
		ServicoPrestado servico = findById(servicoAgendado.getId());
		servico.setAtivo(Boolean.FALSE);
		update(servico);
	}

}
