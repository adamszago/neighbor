package br.com.lphantus.neighbor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ModuloAcessoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.MoradorUnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Agregado;
import br.com.lphantus.neighbor.entity.Historico;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.MoradorAgregado;
import br.com.lphantus.neighbor.entity.MoradorAgregadoPK;
import br.com.lphantus.neighbor.entity.MoradorUnidadeHabitacional;
import br.com.lphantus.neighbor.entity.MoradorUnidadeHabitacionalPK;
import br.com.lphantus.neighbor.entity.Telefone;
import br.com.lphantus.neighbor.entity.UnidadeHabitacional;
import br.com.lphantus.neighbor.repository.IMoradorDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IAgregadoService;
import br.com.lphantus.neighbor.service.IEnderecoService;
import br.com.lphantus.neighbor.service.IHistoricoService;
import br.com.lphantus.neighbor.service.IMailManager;
import br.com.lphantus.neighbor.service.IModuloAcessoService;
import br.com.lphantus.neighbor.service.IMoradorAgregadoService;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IMoradorUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.ITelefoneService;
import br.com.lphantus.neighbor.service.IUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.integracao.mail.TextoMensagemMail;
import br.com.lphantus.neighbor.service.seguranca.Criptografia;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MoradorServiceImpl extends GenericService<Long, MoradorDTO, Morador> implements IMoradorService {

	@Autowired
	private IMoradorDAO moradorDAO;

	@Autowired
	private IMoradorUnidadeHabitacionalService moradorUnidadeHabitacionalService;

	@Autowired
	private IUnidadeHabitacionalService unidadeHabitacionalService;

	@Autowired
	private IMoradorAgregadoService moradorAgregadoService;

	@Autowired
	private IAgregadoService agregadoService;

	@Autowired
	private IEnderecoService enderecoService;

	@Autowired
	private ITelefoneService telefoneService;

	@Autowired
	private IHistoricoService historicoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IMailManager mailManager;

	@Autowired
	private IModuloAcessoService serviceModuloAcesso;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(final Morador morador) throws ServiceException {
		try {

			Date dataCadastro = new Date();

			if (morador == null) {
				throw new ServiceException("Erro ao salvar entidade, parametro nulo.");
			}

			// valida se o morador ja existe na base
			if (this.moradorDAO.existeCpf(morador.createDto())) {
				throw new ServiceException("Este CPF já está cadastrado para um morador!");
			}
			
			// salva em uma variavel temporaria a lista de unidades
			// habitacionais e agregados
			final List<MoradorUnidadeHabitacional> unidades = morador.getUnidadeHabitacional();
			final List<MoradorAgregado> agregados = morador.getAgregados();
			
			// limpa unidade habitacional e agregados
			morador.setUnidadeHabitacional(null);
			morador.setAgregados(null);

			// valida infomacoes do relacionamento com unidades habitacionais
			for (final MoradorUnidadeHabitacional relacionamento : unidades) {
				final String identificacao = relacionamento.getUnidadeHabitacional().getIdentificacao();
				if ((identificacao == null) || identificacao.isEmpty() || identificacao.trim().equalsIgnoreCase(BigDecimal.ZERO.toString())) {
					throw new ServiceException("Favor selecionar uma casa para este morador!");
				}
				if (validaMoradorExistente(identificacao, relacionamento.getUnidadeHabitacional().getCondominio().createDto()) != null) {
					throw new ServiceException("Já existe um morador cadastrado para esta casa");
				}

				if (null == relacionamento.getChave()) {
					relacionamento.setChave(new MoradorUnidadeHabitacionalPK());
				}
			}

			// valida informacoes do relacionamento com agregados
			for (final MoradorAgregado relacionamento : agregados) {
				if (null == relacionamento.getChaveComposta()) {
					relacionamento.setChaveComposta(new MoradorAgregadoPK());
				}
			}

			morador.setDataCadastro(dataCadastro);
			cadastrarUsuario(morador, unidades.get(0).getUnidadeHabitacional());
			super.save(morador);

			// salva unidade habitacional
			for (final MoradorUnidadeHabitacional relacionamento : unidades) {
				final UnidadeHabitacional entidadeSalvar = relacionamento.getUnidadeHabitacional();
				this.unidadeHabitacionalService.save(entidadeSalvar);

				relacionamento.setResponsavelFinanceiro(true);
				relacionamento.setDataInicio(dataCadastro);
				relacionamento.setUnidadeHabitacional(null);

				relacionamento.getChave().setIdMorador(morador.getIdPessoa());
				relacionamento.getChave().setIdUnidade(entidadeSalvar.getIdUnidade());

				this.moradorUnidadeHabitacionalService.save(relacionamento);

				// salva historico
				this.historicoService.save(new Historico(dataCadastro, "Morador: \"" + morador.getNome() + "\" Casa " + entidadeSalvar.getIdentificacao() + " inserido!",
						(this.usuarioService.getUsuarioLogado() == null ? "Nao Identificado" : this.usuarioService.getUsuarioLogado().getLogin()), entidadeSalvar.getCondominio()
								.getNome(), entidadeSalvar.getCondominio().getIdPessoa()));

			}

			// salva agregados
			for (final MoradorAgregado relacionamento : agregados) {
				final Agregado entidadeSalvar = relacionamento.getAgregado();
				entidadeSalvar.setDataCadastro(dataCadastro);
				this.agregadoService.save(entidadeSalvar);

				relacionamento.setAgregado(null);
				relacionamento.setDataInicio(dataCadastro);

				relacionamento.getChaveComposta().setIdMorador(morador.getIdPessoa());
				relacionamento.getChaveComposta().setIdAgregado(entidadeSalvar.getIdPessoa());

				this.moradorAgregadoService.save(relacionamento);
			}

			// salva telefones
			for (final Telefone telefone : morador.getTelefones()) {
				telefone.setMorador(morador);
				this.telefoneService.save(telefone);
			}
		} catch (final DAOException e) {
			getLogger().error("Erro ao gravar entidade morador.", e);
			throw new ServiceException("Erro ao gravar entidade morador.", e);
		}
	}

	private void cadastrarUsuario(Morador morador, UnidadeHabitacional unidade) throws ServiceException {
		String login;
		if (null == unidade.getCondominio().getNomeAbreviado()) {
			login = String.format("%s.%s", unidade.getCondominio().getNomeFantasia(), unidade.getIdentificacao());
		} else {
			login = (unidade.getCondominio().getNomeAbreviado() + "." + unidade.getIdentificacao());
		}

		final Random rand = new Random();
		final String senha = rand.nextInt(9999) + 1000 + "";

		morador.setLogin(login);
		morador.setSenha(Criptografia.criptografar(senha));
		morador.setSindico(false);
		morador.setCondominio(unidade.getCondominio());
		morador.setAtivo(true);

		ModuloAcessoDTO modulo = serviceModuloAcesso.getModuloPadraoMorador();
		morador.setModuloAcesso(ModuloAcessoDTO.Builder.getInstance().createEntity(modulo));

		mailManager.enviarEmailUsuarioCadastrado(TextoMensagemMail.gerarTextoUsuarioCadastrado(morador.createDto(), login, senha), morador.getMail());
	}

	@Override
	public MoradorDTO buscarPrincipal(final String identificacao, final CondominioDTO condominio) throws ServiceException {
		try {
			final MoradorDTO m = this.moradorDAO.buscarPrincipal(identificacao, condominio);
			if (m == null) {
				throw new ServiceException("Morador Inexistente!");
			}
			if (!m.getPessoa().isAtivo()) {
				throw new ServiceException("Morador inativo, informar ao síndico!");
			}

			return m;
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<MoradorDTO> listarMoradores() throws ServiceException {
		try {
			return this.moradorDAO.listarMoradores();
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<MoradorDTO> findBuscaPersonalizada(final List<Integer> sequenciaCasas) throws ServiceException {
		List<MoradorDTO> moradores = new ArrayList<MoradorDTO>();
		try {
			moradores = this.moradorDAO.findBuscaPersonalizada(sequenciaCasas);
			return moradores;
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public MoradorDTO buscarPrincipalReserva(final String identificacao, final CondominioDTO condominio) throws ServiceException {
		try {
			final MoradorDTO m = this.moradorDAO.buscarPrincipal(identificacao, condominio);

			if (m == null) {
				throw new ServiceException("Morador Inexistente!");
			}

			final MoradorUnidadeHabitacionalDTO unidade = m.getUnidadeHabitacional().get(0);

			if (!m.getPessoa().isAtivo()) {
				throw new ServiceException(GerenciadorMensagem.getMensagem("MORADOR_INATIVO"));
			}
			if (!unidade.isAdimplente()) {
				throw new ServiceException(GerenciadorMensagem.getMensagem("RESERVA_IMPEDIDA"));
			}
			return m;
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public MoradorDTO validaMoradorExistente(final String identificacao, final CondominioDTO condominio) throws ServiceException {
		try {
			return this.moradorDAO.buscarPrincipal(identificacao, condominio);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void registrarInadimplencia(final List<MoradorDTO> moradores) throws ServiceException {
		try {
			if ((moradores == null) || moradores.isEmpty()) {
				throw new ServiceException("A lista de moradores está vazia!");
			}
			for (final MoradorDTO morador : moradores) {
				final MoradorUnidadeHabitacionalDTO unidade = morador.getUnidadeHabitacional().get(0);
				unidade.setAdimplente(false);
				this.moradorDAO.update(MoradorDTO.Builder.getInstance().createEntity(morador));
			}

		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public MoradorDTO buscarDetalhesMorador(final MoradorDTO morador) throws ServiceException {
		MoradorDTO result = null;
		try {
			if (morador == null) {
				throw new ServiceException("Morador nulo ou inválido");
			}
			result = this.moradorDAO.buscarDetalhesMorador(morador);
		} catch (final DAOException e) {
			getLogger().debug("Erro ao buscar detalhes do morador.", e);
			throw new ServiceException(e.getMessage(), e);
		}
		return result;
	}

	@Override
	public List<MoradorDTO> listarMoradoresCondominio(final CondominioDTO condominio, final Boolean status) throws ServiceException {
		try {
			return this.moradorDAO.listarMoradoresCondominio(condominio, status);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public MoradorDTO buscarMoradorUsuario(final UsuarioDTO usuarioLogado) throws ServiceException {
		try {
			return this.moradorDAO.buscarMoradorUsuario(usuarioLogado);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public MoradorDTO buscarPorPessoa(final PessoaDTO pessoa) throws ServiceException {
		try {
			return this.moradorDAO.buscarPorPessoa(pessoa);
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

}
