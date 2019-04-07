package br.com.lphantus.neighbor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ContratoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Usuario;
import br.com.lphantus.neighbor.repository.IUsuarioDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IContratoService;
import br.com.lphantus.neighbor.service.IModuloAcessoService;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.seguranca.Criptografia;
import br.com.lphantus.neighbor.service.seguranca.UsuarioSessaoUtil;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class UsuarioServiceImpl extends GenericService<Long, UsuarioDTO, Usuario> implements IUsuarioService {

	@Autowired
	private IUsuarioDAO usuarioDao;

	@Autowired
	private IMoradorService moradorService;

	@Autowired
	private IModuloAcessoService serviceModuloAcesso;

	@Autowired
	private IContratoService contratoService;

	@Autowired
	private UsuarioSessaoUtil usuarioSessaoUtil;

	private UsuarioDTO userLogado;

	@Override
	public void save(final Usuario usuario) throws ServiceException {
		try {

			if (usuario == null) {
				throw new ServiceException("Erro ao salvar entidade, parametro nulo.");
			}

			final CondominioDTO condominio = usuario.getCondominio().createDto();
			final UsuarioDTO objetoDto = UsuarioDTO.Builder.getInstance().create(usuario);

			if (usuario.isSindico() && (this.buscarSindico(condominio) != null)) {
				throw new ServiceException("Já existe um síndico cadastrado para este condomínio!");
			}
			// bug 42
			if (usuario.getModuloAcesso() == null) {
				throw new ServiceException("Favor selecionar um modulo de acesso.");
			}

			// TODO: revisar
			// if
			// (usuario.getModuloAcesso().getNome().equalsIgnoreCase("MORADOR")
			// && (usuario.getMorador() == null)) {
			// throw new ServiceException(
			// "Para cadastro de moradores utlize a tela de cadastro de moradores ou altere um registro existente");
			// }

			if (null != condominio) {
				final ContratoDTO contratoCondominio = this.contratoService.getContratoByCondominio(condominio);
				Long limiteUsuario = null;
				Long usuariosCadastrados = null;
				if (contratoCondominio != null) {
					limiteUsuario = contratoCondominio.getLimiteUsuarios();
					usuariosCadastrados = getTotalUsuariosByCondominio(condominio);
				} else {
					getLogger().info("O Condominio: (" + condominio.getPessoa().getIdPessoa() + "-" + condominio.getPessoa().getNome() + ") NAO POSSUI CONTRATO");
				}
				getLogger().info("limiteUsuario: " + limiteUsuario);
				getLogger().info("usuariosCadastrados: " + usuariosCadastrados);
				if ((limiteUsuario != null) && (usuariosCadastrados != null)) {
					if (usuariosCadastrados >= limiteUsuario) {
						getLogger().info(
								"Limite de usuarios do condominio " + "(" + condominio.getPessoa().getIdPessoa() + "-" + condominio.getPessoa().getNome() + ") foi atingido.");
						throw new ServiceException(GerenciadorMensagem.getMensagem("LIMITE_USUARIO_ATINGIDO", limiteUsuario));
					}
				}
			}

			if (this.usuarioDao.existeUsuario(objetoDto)) {
				throw new ServiceException(GerenciadorMensagem.getMensagem("LOGIN_JA_EXISTE"));
			} else {

				final String senhaCriptografada = Criptografia.criptografar(usuario.getSenha());
				usuario.setSenha(senhaCriptografada);
				usuario.setAtivo(true);
				this.usuarioDao.save(usuario);
			}
		} catch (final DAOException e) {
			getLogger().error("ERRO: " + e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void update(final UsuarioDTO usuario, final String loginAnterior) throws ServiceException {
		if (usuario == null) {
			throw new ServiceException("Erro ao salvar entidade, parametro nulo.");
		}

		// TODO: revisar
		// if (usuario.getMorador() != null) {
		// usuario.setModuloAcesso(this.serviceModuloAcesso
		// .getModuloPadraoMorador());
		// }

		try {
			if (!loginAnterior.equals(usuario.getLogin())) {
				if (this.usuarioDao.existeUsuario(usuario)) {
					throw new ServiceException(GerenciadorMensagem.getMensagem("LOGIN_JA_EXISTE"));
				}
			}
			if ((usuario.getSenha() == null) || "".equals(usuario.getSenha())) {
				Usuario u = (Usuario) this.usuarioDao.findById(Usuario.class, usuario.getPessoa().getIdPessoa());
				usuario.setSenha(u.getSenha());
				u = new Usuario();
			} else {
				final String senhaCriptografada = Criptografia.criptografar(usuario.getSenha());
				usuario.setSenha(senhaCriptografada);
			}

			this.usuarioDao.atualizarUsuario(usuario);

		} catch (final DAOException e) {
			getLogger().error("ERRO: " + e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void saveUsuarioMorador(final UsuarioDTO usuario) throws ServiceException {
		if (usuario == null) {
			throw new ServiceException("Erro ao salvar entidade, parametro nulo.");
		}

		if (this.usuarioDao.existeUsuario(usuario)) {
			throw new ServiceException("Login j\u00E1 Existe");
		} else {

			final String senhaCriptografada = Criptografia.criptografar(usuario.getSenha());
			usuario.setSenha(senhaCriptografada);
			usuario.setAtivo(true);

			usuario.setModuloAcesso(this.serviceModuloAcesso.getModuloPadraoMorador());

			Usuario entidade = UsuarioDTO.Builder.getInstance().createEntity(usuario);
			update(entidade);
		}
	}

	@Override
	public Boolean isLogado() {
		return this.usuarioSessaoUtil.isLogado();
	}

	@Override
	public UsuarioDTO getUsuarioLogado() {
		try {
			if (this.userLogado == null) {
				this.userLogado = this.usuarioSessaoUtil.getUsuarioLogado();
			} else if (this.userLogado.getLogin() != getLoginSessao()) {
				this.userLogado = this.usuarioSessaoUtil.getUsuarioLogado();
			}
		} catch (NullPointerException ex) {
			getLogger().info("Erro.", ex);
			ex.printStackTrace(System.out);
			this.userLogado = null;
		}

		return this.userLogado;
	}

	@Override
	public String getLoginSessao() {
		return this.usuarioSessaoUtil.getLoginSessao();
	}

	@Override
	public void logout() {
		this.usuarioSessaoUtil.logout();
	}

	@Override
	public Long getTotalUsuariosByCondominio(final CondominioDTO condominio) {
		return this.usuarioDao.getTotalUsuariosByCondominio(condominio);
	}

	@Override
	public List<MoradorDTO> buscaPersonalizada(final List<Integer> casas) throws ServiceException {
		List<MoradorDTO> moradores = new ArrayList<MoradorDTO>();
		if ((casas == null) || casas.isEmpty()) {
			moradores.addAll(MoradorDTO.Builder.getInstance().createList(this.moradorService.findAll()));
		} else {
			moradores = this.moradorService.findBuscaPersonalizada(casas);
		}
		return moradores;
	}

	@Override
	public UsuarioDTO buscarSindico(final CondominioDTO condominio) throws ServiceException {
		try {
			return this.usuarioDao.buscarSindico(condominio);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar sindico. ", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<UsuarioDTO> findAllWithoutRoot() throws ServiceException {
		try {
			return this.usuarioDao.findAllWithoutRoot();
		} catch (final DAOException e) {
			getLogger().error("ERRO: " + e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<UsuarioDTO> findAllByIdCondominioWithoutRoot(final CondominioDTO condominio) throws ServiceException {
		try {
			return this.usuarioDao.findAllByIdCondominioWithoutRoot(condominio);
		} catch (final Exception e) {
			getLogger().error("ERRO: " + e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public UsuarioDTO getUsuarioByLogin(final String login) throws ServiceException {
		if (login != null) {
			try {
				final UsuarioDTO retornoObjeto = this.usuarioDao.findByLogin(login);
				return retornoObjeto;
			} catch (DAOException e) {
				throw new ServiceException(e);
			}

		} else {
			getLogger().error("parametro login is null");
			throw new ServiceException("login null");
		}
	}

	@Override
	public UsuarioDTO getUsuarioByEmail(final String email) throws ServiceException {
		if (email != null) {
			UsuarioDTO retorno;
			try {
				retorno = usuarioDao.findByEmail(email);
			} catch (DAOException e) {
				getLogger().error("Erro ao pesquisar usuario por e-mail.", e);
				throw new ServiceException("Ocorreu um erro ao pesquisar usuario por e-mail.", e);
			}

			return retorno;
		} else {
			getLogger().error("parametro email is null");
			throw new ServiceException("email null");
		}
	}

	@Override
	public CondominioDTO getCondominioUsuario(final UsuarioDTO usuario) throws ServiceException {
		try {
			return this.usuarioDao.buscarCondominioUsuario(usuario);
		} catch (DAOException e) {
			getLogger().error("Erro ao pesquisar condominio do usuario.", e);
			throw new ServiceException("Ocorreu um erro ao pesquisar condominio do usuario.", e);
		}
	}

	@Override
	public UsuarioDTO buscaUsuarioRoot() throws ServiceException {
		try {
			return this.usuarioDao.findByLogin("root");
		} catch (DAOException e) {
			getLogger().error("Erro ao buscar usuario root.", e);
			throw new ServiceException(e);
		}
	}

	@Override
	public UsuarioDTO buscaUsuarioMorador(final Long idMorador) throws ServiceException {
		try {
			return this.usuarioDao.buscaUsuarioMorador(idMorador);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar usuario do morador.", exception);
			throw new ServiceException("Erro ao buscar usuario do morador.", exception);
		}
	}

	@Override
	public List<UsuarioDTO> buscarPorParametros(final CondominioDTO condominio) throws ServiceException {
		try {
			return this.usuarioDao.buscarPorParametros(condominio);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar usuarios por parametros.", exception);
			throw new ServiceException("Erro ao buscar usuarios por parametros.", exception);
		}
	}
}
