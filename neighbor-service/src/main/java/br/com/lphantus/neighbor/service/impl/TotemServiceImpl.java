package br.com.lphantus.neighbor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ServicoPrestadoDTO;
import br.com.lphantus.neighbor.common.TotemDTO;
import br.com.lphantus.neighbor.common.VisitaDTO;
import br.com.lphantus.neighbor.entity.Totem;
import br.com.lphantus.neighbor.repository.ITotemDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IMailManager;
import br.com.lphantus.neighbor.service.IPrestadorServicoService;
import br.com.lphantus.neighbor.service.ITotemService;
import br.com.lphantus.neighbor.service.IVisitanteService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.integracao.batch.IGeradorTotem;
import br.com.lphantus.neighbor.service.integracao.batch.RetornoArquivoRunnable;
import br.com.lphantus.neighbor.service.integracao.mail.TextoMensagemMail;
import br.com.lphantus.neighbor.util.TotemUtil;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class TotemServiceImpl extends GenericService<Long, TotemDTO, Totem> implements ITotemService {

	@Autowired
	private ITotemDAO totemDAO;

	@Autowired
	private IPrestadorServicoService prestadorServicoService;

	@Autowired
	private IVisitanteService visitanteService;

	@Autowired
	private IMailManager mailManager;

	@Autowired
	@Qualifier("geradorAntigo")
	// @Qualifier("geradorNovo")
	private IGeradorTotem geradorTotem;

	@PostConstruct
	public void initialize() {
		boolean isNovoGerador = true;
		if (isNovoGerador) {
			Thread thread = new Thread(new RetornoArquivoRunnable(this));
			thread.start();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(final Totem totem) throws ServiceException {
		totem.setAtivo(Boolean.FALSE);
		update(totem);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void geraArquivoSenhas(TotemDTO totem) {
		// --------------------------------------------------------------
		// EMAIL
		if (totem.getMorador() != null) {
			// Envia email morador
			mailManager.enviarEmailUsuarioTotem(TextoMensagemMail.informarSenhaTotem(totem.getMorador().getPessoa().getNome(), totem.getSenha()), totem.getMorador().getPessoa()
					.getMail());
		}
		if (totem.getAgregado() != null) {
			// Envia email agregado
			mailManager.enviarEmailUsuarioTotem(TextoMensagemMail.informarSenhaTotem(totem.getAgregado().getPessoa().getNome(), totem.getSenha()), totem.getAgregado().getPessoa()
					.getMail());
		}

		geradorTotem.executar(totem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void cadastrarSenhaTotem(final TotemDTO totem) throws ServiceException {
		try {

			if (totem.getAtivo() == null || !totem.getAtivo()) {
				totem.setAtivo(Boolean.TRUE);
			}
			executaValidacoesNegocio(totem);

			// Verifica se ja existe registro na base de dados, caso exista
			// executa update caso contrario executa save.
			final Totem totemEntity = TotemDTO.Builder.getInstance().createEntity(totem);

			TotemDTO registro = this.totemDAO.existeUsuarioCadastrado(totem);
			if (null == registro) {
				saveOrUpdate(totemEntity);
			} else {
				totemEntity.setId(registro.getId());
				update(totemEntity);
			}

		} catch (final ServiceException ex) {
			getLogger().error("Erro ao cadastrar senha totem.", ex);
			throw ex;
		} catch (final Exception ex) {
			getLogger().error("Erro ao cadastrar senha totem.", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	private void executaValidacoesNegocio(final TotemDTO totem) throws ServiceException, DAOException {

		if (totem.getMorador() != null) {
			if (StringUtils.isBlank(totem.getMorador().getPessoa().getMail())) {
				throw new ServiceException("Morador deve possuir e-mail cadastrado.");
			}
			for (final TotemDTO t : this.totemDAO.buscarTodosAtivosPorMorador(totem.getMorador())) {
				if (t.getMorador() != null) {
					if (t.getSenha().equals(totem.getSenha())) {
						throw new ServiceException("Esta é sua senha atual. Escolha uma nova senha, por favor.");
					}

				} else {
					if (t.getSenha().equals(totem.getSenha())) {
						throw new ServiceException("Não podem haver senhas iguais para mesma residência!");
					}
				}
			}
		} else {
			if (StringUtils.isBlank(totem.getAgregado().getPessoa().getMail())) {
				throw new ServiceException("Agregado deve possuir e-mail cadastrado.");
			}
			for (final TotemDTO t : this.totemDAO.buscarTodosAtivosPorMorador(totem.getAgregado().getMorador().get(0).getMorador())) {
				if (t.getAgregado() != null) {
					if (t.getSenha().equals(totem.getSenha())) {
						if (t.getAgregado().equals(totem.getAgregado())) {
							throw new ServiceException("Esta é sua senha atual. Escolha uma nova senha, por favor.");
						} else {
							throw new ServiceException("Não podem haver senhas iguais para mesma residência!");
						}

					}
				} else {
					if (t.getSenha().equals(totem.getSenha())) {
						throw new ServiceException("Não podem haver senhas iguais para mesma residência!");
					}
				}
			}
		}

		// senhas com menos de 3 caracteres
		if (StringUtils.isNotEmpty(totem.getSenha())) {
			if (totem.getSenha().length() < 4) {
				totem.setSenha(StringUtils.leftPad(totem.getSenha(), 4, BigDecimal.ZERO.toString()));
			}
		}
	}

	@Override
	public TotemDTO buscarAgregadoTotem(final AgregadoDTO agregado) throws ServiceException {
		try {
			return this.totemDAO.buscarAgregadoTotem(agregado);
		} catch (final DAOException ex) {
			getLogger().error("Erro ao buscar totem do agregado.", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	public TotemDTO buscarMoradorTotem(final MoradorDTO morador) throws ServiceException {
		try {
			return this.totemDAO.buscarMoradorTotem(morador);
		} catch (final DAOException ex) {
			getLogger().error("Erro ao buscar totem do morador.", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	public List<TotemDTO> findAllAtivos() throws ServiceException {
		try {
			final Set<TotemDTO> listaAux = new HashSet<TotemDTO>(this.totemDAO.findAllAtivos());
			return new ArrayList<TotemDTO>(listaAux);
		} catch (final DAOException ex) {
			getLogger().error("Erro ao buscar todos ativos.", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	public List<TotemDTO> buscarTodosAtivosPorCondominio(final CondominioDTO condominio) throws ServiceException {
		try {
			final List<TotemDTO> retorno = this.totemDAO.buscarTodosAtivosPorCondominio(condominio);
			return retorno;
		} catch (final DAOException ex) {
			getLogger().error("Erro ao buscar todos ativos por condominio.", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	public List<TotemDTO> buscarMoradorSemTotemOuInativoPorCondominio(final CondominioDTO condominio) throws ServiceException {
		try {
			final List<TotemDTO> retorno = this.totemDAO.buscarMoradorSemTotemOuInativoPorCondominio(condominio);
			return retorno;
		} catch (final DAOException ex) {
			getLogger().error("Erro ao buscar morador sem totem ou totem inativo por condominio.", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	public List<TotemDTO> buscarAgregadoSemTotemOuInativoPorCondominio(final CondominioDTO condominio) throws ServiceException {
		try {
			final List<TotemDTO> retorno = this.totemDAO.buscarAgregadoSemTotemOuInativoPorCondominio(condominio);
			return retorno;
		} catch (final DAOException ex) {
			getLogger().error("Erro ao buscar agregado sem totem ou com totem inativo por condominio.", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	public List<TotemDTO> buscarTodosAtivosPorMorador(final MoradorDTO morador) throws ServiceException {
		try {
			final List<TotemDTO> retorno = this.totemDAO.buscarTodosAtivosPorMorador(morador);
			return retorno;
		} catch (final DAOException ex) {
			getLogger().error("Erro ao buscar todos totens ativos por morador.", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	public List<TotemDTO> buscarMoradorSemTotemOuInativoPorMorador(final MoradorDTO morador) throws ServiceException {
		try {
			final List<TotemDTO> retorno = this.totemDAO.buscarMoradorSemTotemOuInativoPorMorador(morador);
			return retorno;
		} catch (final DAOException ex) {
			getLogger().error("Erro ao buscar morador sem totem ou com totem inativo por morador.", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	public List<TotemDTO> buscarAgregadoSemTotemOuInativoPorMorador(final MoradorDTO morador) throws ServiceException {
		try {
			final List<TotemDTO> retorno = this.totemDAO.buscarAgregadoSemTotemOuInativoPorMorador(morador);
			return retorno;
		} catch (final DAOException ex) {
			getLogger().error("Erro ao buscar agregado sem totem ou com totem inativo por morador.", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TotemDTO registrarTotemVisitaAgendada(VisitaDTO visitaAgendada) throws ServiceException {
		TotemDTO retorno = null;
		Totem entidade = new Totem();
		encontrarSenhaParaCasa(entidade, visitaAgendada.getMorador());

		entidade.setVisitante(visitanteService.findById(visitaAgendada.getVisitante().getPessoa().getIdPessoa()));

		save(entidade);
		retorno = entidade.createDto();

		return retorno;
	}

	private void encontrarSenhaParaCasa(Totem entidade, MoradorDTO morador) throws ServiceException {
		Set<String> conjuntoSenhas = TotemUtil.obterConjuntoSenhas();
		List<TotemDTO> totens = buscarTodosAtivosPorMorador(morador);
		for (TotemDTO senhaTotem : totens) {
			conjuntoSenhas.remove(senhaTotem.getSenha());
		}
		int posicao = (int) ((Math.random() * TotemUtil.MAX_SENHA) - (TotemUtil.MAX_SENHA - conjuntoSenhas.size()));
		entidade.setSenha((String) conjuntoSenhas.toArray()[posicao]);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TotemDTO registrarTotemServicoAgendado(ServicoPrestadoDTO servicoAgendado) throws ServiceException {
		TotemDTO retorno = null;
		Totem entidade = new Totem();
		encontrarSenhaParaCasa(entidade, servicoAgendado.getMorador());

		entidade.setPrestador(prestadorServicoService.findById(servicoAgendado.getPrestadorServico().getIdPrestador()));

		save(entidade);
		retorno = entidade.createDto();

		return retorno;
	}

	@Override
	public void atualizaVisitasPrestacoesServico(List<String[]> listaDadosArquivo) throws ServiceException {
		// TODO Tratar os dados do arquivo de acordo com o email registronic

	}

}