package br.com.lphantus.neighbor.service.seguranca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.GrupoPermissaoDTO;
import br.com.lphantus.neighbor.common.ModuloAcessoDTO;
import br.com.lphantus.neighbor.common.PermissaoDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.PessoaJuridicaDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.GrupoPermissao;
import br.com.lphantus.neighbor.entity.LancamentoTipo;
import br.com.lphantus.neighbor.entity.Permissao;
import br.com.lphantus.neighbor.service.ICondominioService;
import br.com.lphantus.neighbor.service.IGrupoPermissaoService;
import br.com.lphantus.neighbor.service.ILancamentoTipoService;
import br.com.lphantus.neighbor.service.IModuloAcessoService;
import br.com.lphantus.neighbor.service.IPermissaoService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

/**
 * 
 * @author Joander V. Candido Classe com escopo de aplicacao (singleton),
 *         metodos serao executos uma unica vez ao inicializar a aplicacao. A
 *         finalidade desta classe e verificar as condicoes minimas de seguranca
 *         para o correto funcionamento do sistema, serao verificados dentre
 *         outras coisas, se todas as permissoes estao corretamente cadastradas
 *         no Banco e cadastra-las\atualiza-las caso houver necessidade, alem de
 *         verificar o acesso do usuario Root e cadastra-lo\atualiza-lo tbm se
 *         houver necessidade.
 */
@Component
@Scope(value = "singleton")
@Transactional(propagation = Propagation.SUPPORTS)
public class VerificarRequisitosSeguranca {

	private static Logger logger = LoggerFactory.getLogger(VerificarRequisitosSeguranca.class);

	@Autowired
	private IPermissaoService permissaoService;

	@Autowired
	private IGrupoPermissaoService grupoPermissaoService;

	@Autowired
	private IModuloAcessoService moduloAcessoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private ILancamentoTipoService lancamentoTipoService;

	@Autowired
	private ICondominioService condominioService;

	private ArrayList<ArrayList<String>> permissoes;
	private ArrayList<ArrayList<String>> grupos;
	private int NOME_PERMISSAO = 0;
	private int LABEL_PERMISSAO = 1;
	private int GRUPO_PERMISSAO = 2;
	private int DESCRICAO_PERMISSAO = 3;
	private int NOME_GRUPO = 0;

	@PostConstruct
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void initialize() {
		this.permissoes = new ArrayList<ArrayList<String>>();
		this.grupos = new ArrayList<ArrayList<String>>();

		setarNomesGrupos();
		setarNomesPermissoes();
		verificarGrupoPermissao();
		verificarPermissoes();
		verificarUsuarioRoot();
		verificarPermissoesMorador();
		verificarTiposNecessarios();

		logger.info("VerificarRequisitosSeguranca : SUCESSO");
	}

	private void verificarTiposNecessarios() {
		try {

			final LancamentoTipo tipoEntrada = this.lancamentoTipoService.findById(1L);
			if (null == tipoEntrada) {
				final LancamentoTipo tipoEntradaCadastro = new LancamentoTipo();
				tipoEntradaCadastro.setAtivo(Boolean.TRUE);
				tipoEntradaCadastro.setDescricao("Entrada");
				tipoEntradaCadastro.setExcluido(Boolean.FALSE);
				tipoEntradaCadastro.setNome("Entrada");
				this.lancamentoTipoService.save(tipoEntradaCadastro);
			}

			final LancamentoTipo tipoSaida = this.lancamentoTipoService.findById(2L);
			if (null == tipoSaida) {
				final LancamentoTipo tipoEntradaCadastro = new LancamentoTipo();
				tipoEntradaCadastro.setAtivo(Boolean.TRUE);
				tipoEntradaCadastro.setDescricao("Saida");
				tipoEntradaCadastro.setExcluido(Boolean.FALSE);
				tipoEntradaCadastro.setNome("Saida");
				this.lancamentoTipoService.save(tipoEntradaCadastro);
			}
		} catch (final ServiceException e) {
			e.printStackTrace();
		}

		logger.info("TIPOS DE LANCAMENTOS VERIFICADOS");
	}

	public void verificarGrupoPermissao() {
		try {
			for (final ArrayList<String> grupo : this.grupos) {

				GrupoPermissaoDTO grupoPermissaoBanco = this.grupoPermissaoService.buscaPorNome(grupo.get(this.NOME_GRUPO));

				if (grupoPermissaoBanco == null) {
					GrupoPermissao novoGrupo = new GrupoPermissao();
					novoGrupo.setNome(grupo.get(this.NOME_GRUPO));

					this.grupoPermissaoService.save(novoGrupo);
					logger.info(String.format("GRUPO INSERIDO: %s", novoGrupo.getNome()));
					novoGrupo = null;
				}

				grupoPermissaoBanco = null;

			}
			logger.info("GRUPOS VERIFICADOS");
		} catch (final ServiceException e) {
			logger.error("Erro ao verificar grupo de permissoes.", e);
		}
	}

	public void verificarPermissoes() {
		try {
			for (final ArrayList<String> permissao : this.permissoes) {

				PermissaoDTO permissaoBanco = this.permissaoService.buscaPorNome(permissao.get(this.NOME_PERMISSAO));

				// Verifica se a permissao existe se nao insere no banco
				if (permissaoBanco == null) {
					Permissao novaPermissao = new Permissao();

					novaPermissao.setNome(permissao.get(this.NOME_PERMISSAO));
					novaPermissao.setLabel(permissao.get(this.LABEL_PERMISSAO));
					novaPermissao.setDescricao(permissao.get(this.DESCRICAO_PERMISSAO));

					this.permissaoService.save(novaPermissao);
					
					final GrupoPermissaoDTO grp = this.grupoPermissaoService.buscaPorNome(permissao.get(this.GRUPO_PERMISSAO));
					permissaoService.atualizaGrupo(novaPermissao.createDto(), grp);
					
					logger.info(String.format("PERMISSAO INSERIDA: %s", novaPermissao.getNome()));
					novaPermissao = null;

					// Verifica se a permissao tem o grupo e se esta correto,
					// caso contrario atualiza a permissao com o grupo correto
				} else {
					if (permissaoBanco.getGrupo() == null) {

						final GrupoPermissaoDTO grp = this.grupoPermissaoService.buscaPorNome(permissao.get(this.GRUPO_PERMISSAO));

						this.permissaoService.atualizaGrupo(permissaoBanco, grp);
						logger.info("GRUPO DA PERMISSAO ATUALIZADO null - PERMISSAO: " + permissaoBanco.getNome() + " GRUPO: " + grp.getNome());

					} else if (!permissaoBanco.getGrupo().getNome().equals(permissao.get(this.GRUPO_PERMISSAO))) {

						final GrupoPermissaoDTO grp = this.grupoPermissaoService.buscaPorNome(permissao.get(this.GRUPO_PERMISSAO));

						this.permissaoService.atualizaGrupo(permissaoBanco, grp);
						logger.info("GRUPO DA PERMISSAO ATUALIZADO != - PERMISSAO: " + permissaoBanco.getNome() + " GRUPO: " + grp.getNome());
					}
				}

				permissaoBanco = null;

			}

			logger.info("PERMISSOES VERIFICADAS");
		} catch (final ServiceException e) {
			e.printStackTrace();
		}
	}

	public void verificarUsuarioRoot() {
		try {
			CondominioDTO condominioRoot = this.condominioService.buscarPorNome("Lphantus Root");

			if (null == condominioRoot) {
				logger.info("Adicionando condominio root...");

				condominioRoot = new CondominioDTO();
				final PessoaJuridicaDTO pessoa = new PessoaJuridicaDTO();
				condominioRoot.setPessoa(pessoa);
				condominioRoot.getPessoa().setAtivo(true);
				condominioRoot.getPessoa().setDataCadastro(new Date());
				condominioRoot.getPessoa().setNome("Lphantus Root");
				condominioRoot.getPessoa().setNomeFantasia("lproot");
				condominioRoot.setNomeAbreviado("lproot");
				condominioRoot.getPessoa().setCnpj("11.111.111/1111-11");

				this.condominioService.save(CondominioDTO.Builder.getInstance().createEntity(condominioRoot));

				condominioRoot = this.condominioService.buscarPorNome("Lphantus Root");

				logger.info("Condominio root adicionado!");
			}

			ModuloAcessoDTO moduloAcessoRoot = this.moduloAcessoService.buscaModuloRoot();

			final PermissaoDTO permissaoRoot = this.permissaoService.buscaPermissaoRoot();

			if (moduloAcessoRoot == null) {
				logger.info("Adicionando modulo acesso root...");

				moduloAcessoRoot = new ModuloAcessoDTO();
				moduloAcessoRoot.setNome("Root");
				moduloAcessoRoot.setDescricao("Acesso Root");

				final Set<PermissaoDTO> permissoesDoModulo = new HashSet<PermissaoDTO>();
				permissoesDoModulo.add(permissaoRoot);
				moduloAcessoRoot.setPermissoes(permissoesDoModulo);

				this.moduloAcessoService.save(ModuloAcessoDTO.Builder.getInstance().createEntity(moduloAcessoRoot));

				moduloAcessoRoot = this.moduloAcessoService.buscaModuloRoot();
				logger.info("Modulo Acesso inserido: " + moduloAcessoRoot.getNome());
			} else {
				Boolean existePermissaoRoot = false;
				for (final PermissaoDTO p : moduloAcessoRoot.getPermissoes()) {
					if (p.getNome().equals(permissaoRoot.getNome())) {
						existePermissaoRoot = true;
					}
				}
				if (!existePermissaoRoot) {
					moduloAcessoRoot.getPermissoes().add(permissaoRoot);
					this.moduloAcessoService.update(ModuloAcessoDTO.Builder.getInstance().createEntity(moduloAcessoRoot));
					logger.info("Modulo Acesso atualizado: " + moduloAcessoRoot.getNome() + " - permissao root setada");
				}

			}

			UsuarioDTO usuarioRoot = this.usuarioService.buscaUsuarioRoot();

			if (usuarioRoot == null) {
				logger.info("Adicionando usuario root...");

				usuarioRoot = new UsuarioDTO();
				final PessoaFisicaDTO pessoa = new PessoaFisicaDTO();
				usuarioRoot.setPessoa(pessoa);
				usuarioRoot.setLogin("root");
				usuarioRoot.getPessoa().setNome("Usuário Root");
				usuarioRoot.setSenha("2t4rtuplphantus2013");
				usuarioRoot.setCondominio(condominioRoot);
				usuarioRoot.setAtivo(true);
				usuarioRoot.setRoot(true);

				logger.info("Modulo de Acesso: Root - setado para usuario root");
				usuarioRoot.setModuloAcesso(moduloAcessoRoot);

				this.usuarioService.save(UsuarioDTO.Builder.getInstance().createEntity(usuarioRoot));
				usuarioRoot = this.usuarioService.buscaUsuarioRoot();
				logger.info("Usuario adicionado: " + usuarioRoot.getLogin());

			} else {

				if ((usuarioRoot.getModuloAcesso() == null) || !usuarioRoot.getModuloAcesso().getNome().equals("Root")) {
					logger.info("Modulo de Acesso: Root - setado para usuario root");
					usuarioRoot.setModuloAcesso(moduloAcessoRoot);
					this.usuarioService.update(UsuarioDTO.Builder.getInstance().createEntity(usuarioRoot));
					usuarioRoot = this.usuarioService.buscaUsuarioRoot();
				}

				if (usuarioRoot.isRoot() != true) {
					usuarioRoot.setRoot(true);
					this.usuarioService.update(UsuarioDTO.Builder.getInstance().createEntity(usuarioRoot));
					logger.info("Usuario atualizado: " + usuarioRoot.getLogin() + " - setIsRoot(true)");
					usuarioRoot = this.usuarioService.buscaUsuarioRoot();

				} else {
					if (usuarioRoot.isAtivo() != true) {
						usuarioRoot.setAtivo(true);
						this.usuarioService.update(UsuarioDTO.Builder.getInstance().createEntity(usuarioRoot));
						logger.info("Usuario atualizado: " + usuarioRoot.getLogin() + " - setEnable(true)");
						usuarioRoot = this.usuarioService.buscaUsuarioRoot();
					}
				}
			}

			if (usuarioRoot.getCondominio() == null) {

				final CondominioDTO con = this.condominioService.buscarPorNome(condominioRoot.getPessoa().getNome());

				usuarioRoot.setCondominio(con);
				this.usuarioService.update(UsuarioDTO.Builder.getInstance().createEntity(usuarioRoot));
				logger.info("Condominio: " + condominioRoot.getPessoa().getNome() + " - setado para usuario root");
				usuarioRoot = this.usuarioService.buscaUsuarioRoot();

			}

		} catch (final ServiceException e) {
			logger.error("Erro.", e);
		} catch (final Exception e) {
			logger.error("Erro.", e);
		}

		logger.info("USUARIO ROOT VERIFICADO");
	}

	public void verificarPermissoesMorador() {
		try {
			final String NOME_MODULO_MORADOR_PADRAO = "Morador";
			final String DESCRICAO_MODULO_MORADOR_PARDAO = "Módulo de Acesso do Morador";
			final String NOME_PERMISSAO_MORADOR_PADRAO = "ROLE_PAGINA_MORADOR";
			final String NOME_PERMISSAO_MINIMA = "ROLE_USER";

			ModuloAcessoDTO moduloMorador = this.moduloAcessoService.buscaPorNome(NOME_MODULO_MORADOR_PADRAO);

			final PermissaoDTO permissaoMorador = this.permissaoService.buscaPorNome(NOME_PERMISSAO_MORADOR_PADRAO);

			final PermissaoDTO permissaoMinima = this.permissaoService.buscaPorNome(NOME_PERMISSAO_MINIMA);

			boolean atualizarPermissao = true;
			boolean atualizarPermissao2 = true;

			if (moduloMorador == null) {
				moduloMorador = new ModuloAcessoDTO();
				moduloMorador.setNome(NOME_MODULO_MORADOR_PADRAO);
				moduloMorador.setDescricao(DESCRICAO_MODULO_MORADOR_PARDAO);
				final Set<PermissaoDTO> permissoesMorador = new HashSet<PermissaoDTO>();
				permissoesMorador.add(permissaoMorador);
				permissoesMorador.add(permissaoMinima);
				moduloMorador.setPermissoes(permissoesMorador);
				this.moduloAcessoService.save(ModuloAcessoDTO.Builder.getInstance().createEntity(moduloMorador));
				logger.info("Modulo morador cadastrado");
			} else if (moduloMorador.getPermissoes() == null) {
				atualizarPermissao = true;
			} else {
				for (final PermissaoDTO p : moduloMorador.getPermissoes()) {
					if (p.getNome().equals(NOME_PERMISSAO_MORADOR_PADRAO)) {
						atualizarPermissao = false;
					}
					if (p.getNome().equals(NOME_PERMISSAO_MINIMA)) {
						atualizarPermissao2 = false;
					}
				}

				if (atualizarPermissao || atualizarPermissao2) {
					final Set<PermissaoDTO> permissoesMorador = new HashSet<PermissaoDTO>();
					permissoesMorador.add(permissaoMorador);
					permissoesMorador.add(permissaoMinima);
					moduloMorador.setPermissoes(permissoesMorador);
					this.moduloAcessoService.update(ModuloAcessoDTO.Builder.getInstance().createEntity(moduloMorador));
					logger.info("Ppermissao morador atualizada");
				}

			}

		} catch (final ServiceException e) {
			e.printStackTrace();
		}

	}

	public void setarNomesPermissoes() {

		// ======================================================== ROLE_USER -
		// Permissao Minima do sistema, apenas acessar home
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_USER", "Usuario", this.NAME_GRUPO_MINIMO, "Permissao Mínima do sistema. Acesso apenas para home")));

		// ========================================================
		// ROLE_LISTA_TOTEM_ADM - Permissao Geral para alteracao Totem
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_TOTEM_ADM", "Totem Admin", this.NAME_GRUPO_TOTEN,
				"Permissao Geral para alteracao Totem. Acesso Administrador")));

		// ======================================================== ROOT -
		// Controle total Sistema
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ROOT", "Acesso Root", this.NAME_GRUPO_ROOT,
				"Permissão para acessar todas as áreas do Sistema (permissão máxima)")));

		// ======================================================== COMUNICACAO
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_MENSAGEM", "Listar Mensagens", this.NAME_GRUPO_COMUNICACAO, "Permissão para Listar Mensagens")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_MENSAGEM_PARA_SINDICO", "Mensagem para Síndico", this.NAME_GRUPO_COMUNICACAO,
				"Permissão para Enviar Mensagens para o Síndico")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_MENSAGEM_DO_SINDICO", "Mensagem do Síndico", this.NAME_GRUPO_COMUNICACAO,
				"Permissão para o Síndico Enviar Mensagens")));

		// ======================================================== CONDOMINIO
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_CONDOMINIO", "Cadastrar Condomínio", this.NAME_GRUPO_CONDOMINIO,
				"Permissão para Cadastrar Condomínios")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_CONDOMINIO", "Listar Condomínio", this.NAME_GRUPO_CONDOMINIO, "Permissão para Listar Condomínios")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_CONDOMINIO", "Editar Condomínio", this.NAME_GRUPO_CONDOMINIO, "Permissão para Editar Condomínios")));

		// ======================================================== SINDICO
		/*
		 * this.permissoes.add(new ArrayList<String>(Arrays.asList
		 * ("ROLE_CADASTRO_SINDICO","Cadastrar Síndico",NAME_GRUPO_SINDICO,
		 * "Permissão para Cadastrar Síndico")));
		 * 
		 * this.permissoes.add(new ArrayList<String>(Arrays.asList
		 * ("ROLE_LISTA_SINDICO"
		 * ,"Listar Síndico",NAME_GRUPO_SINDICO,"Permissão para Listar Síndicos"
		 * )));
		 * 
		 * this.permissoes.add(new ArrayList<String>(Arrays.asList
		 * ("ROLE_EDITAR_SINDICO"
		 * ,"Editar Síndico",NAME_GRUPO_SINDICO,"Permissão para Editar Síndicos"
		 * )));
		 */

		// ======================================================== FINANCEIRO
		// -------------------------------------------------------- Centro Custo
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ADICIONAR_CENTRO", "Adicionar Centro Custo", this.NAME_GRUPO_FINANCEIRO,
				"Permissão para adicionar Centro Custo")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_CENTRO", "Editar Centro Custo", this.NAME_GRUPO_FINANCEIRO, "Permissão para Editar Centro Custo")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTAR_CENTRO", "Listar Centro Custo", this.NAME_GRUPO_FINANCEIRO, "Permissão para listar Centros Custo")));

		// Carteira
		this.permissoes
				.add(new ArrayList<String>(Arrays.asList("ROLE_ADICIONAR_CARTEIRA", "Adicionar Carteira", this.NAME_GRUPO_FINANCEIRO, "Permissão para  adicionar Carteira")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_CARTEIRA", "Editar Carteira", this.NAME_GRUPO_FINANCEIRO, "Permissão para Editar Carteira")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTAR_CARTEIRA", "Listar Carteira", this.NAME_GRUPO_FINANCEIRO, "Permissão para listar Carteira")));

		// --------------------------------------------------------------- Banco
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ADICIONAR_BANCO", "Adicionar Banco", this.NAME_GRUPO_FINANCEIRO, "Permissão para adicionar Banco")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_BANCO", "Editar Banco", this.NAME_GRUPO_FINANCEIRO, "Permissão para Editar Banco")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTAR_BANCO", "Listar Banco", this.NAME_GRUPO_FINANCEIRO, "Permissão para listar Bancos")));

		// ---------------------------------------------------------------
		// Lancamento

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ADICIONAR_LANCAMENTO", "Adicionar Lancamento", this.NAME_GRUPO_FINANCEIRO,
				"Permissão para adicionar Lancamento")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_LANCAMENTO", "Editar Lancamentos", this.NAME_GRUPO_FINANCEIRO, "Permissão para Editar Lancamentos")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTAR_LANCAMENTO", "Listar Lancamentos", this.NAME_GRUPO_FINANCEIRO, "Permissão para listar Lancamentos")));

		// ---------------------------------------------------------------
		// Lancamento Automatico

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ADICIONAR_AUTOMATICO", "Adicionar Lancamento Automatico", this.NAME_GRUPO_FINANCEIRO,
				"Permissão para adicionar Lancamento Automatico")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_AUTOMATICO", "Editar Lancamentos Automaticos", this.NAME_GRUPO_FINANCEIRO,
				"Permissão para Editar Lancamentos Automaticos")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTAR_AUTOMATICO", "Listar Lancamentos Automaticos", this.NAME_GRUPO_FINANCEIRO,
				"Permissão para listar Lancamentos Automaticos")));

		// ---------------------------------------------------------------
		// Fatura

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ADICIONAR_FATURA", "Adicionar Fatura", this.NAME_GRUPO_FINANCEIRO, "Permissão para adicionar Fatura")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_FATURA", "Editar Faturas", this.NAME_GRUPO_FINANCEIRO, "Permissão para Editar Faturas")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTAR_FATURAS", "Listar Faturas", this.NAME_GRUPO_FINANCEIRO, "Permissão para listar Faturas")));

		// ---------------------------------------------------------------
		// Duplicata

		this.permissoes.add(new ArrayList<String>(Arrays
				.asList("ROLE_ADICIONAR_DUPLICATA", "Adicionar Duplicata", this.NAME_GRUPO_FINANCEIRO, "Permissão para adicionar Duplicata")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_DUPLICATA", "Editar Duplicata", this.NAME_GRUPO_FINANCEIRO, "Permissão para Editar Duplicatas")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTAR_DUPLICATAS", "Listar Duplicatas", this.NAME_GRUPO_FINANCEIRO, "Permissão para listar Duplicatas")));

		// ---------------------------------------------------------------
		// Movimentacao

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ADICIONAR_MOVIMENTACAO", "Adicionar Movimentacao", this.NAME_GRUPO_FINANCEIRO,
				"Permissão para adicionar Movimentacao")));

		this.permissoes.add(new ArrayList<String>(Arrays
				.asList("ROLE_EDITAR_MOVIMENTACAO", "Editar Movimentacao", this.NAME_GRUPO_FINANCEIRO, "Permissão para Editar Movimentacao")));

		this.permissoes.add(new ArrayList<String>(Arrays
				.asList("ROLE_LISTAR_MOVIMENTACAO", "Listar Movimentacao", this.NAME_GRUPO_FINANCEIRO, "Permissão para listar Movimentacao")));

		// ---------------------------------------------------------------
		// Balancete

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_BALANCETE", "Ver Balancete Mensal", this.NAME_GRUPO_FINANCEIRO,
				"Permissão para visualizar o balancete mensal")));

		// ---------------------------------------------------------------
		// Parametros financeiros

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CONFIGURAR_CONDOMINIO", "Configurar parametros financeiros", this.NAME_GRUPO_FINANCEIRO,
				"Permissão para configurar os parametros de geracao de boleto, data da fatura e outros.")));
		
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_2A_VIA_BOLETOS", "Gerar segunda via de boletos", this.NAME_GRUPO_FINANCEIRO,
				"Permissão para gerar segunda via de boletos na pagina do morador.")));

		// ======================================================== AGREGADO
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ALTERAR_STATUS_AGREGADO", "Alterar Status Agregado", this.NAME_GRUPO_AGREGADO,
				"Permissão para Alterar o Status do Agregado")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EXCLUIR_AGREGADO", "Excluir Agregado", this.NAME_GRUPO_AGREGADO, "Permissão para Excluir o Agregado")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_AGREGADO", "Editar Agregado", this.NAME_GRUPO_AGREGADO, "Permissão para Editar o Agregado")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_AGREGADOS", "Listar Agregados", this.NAME_GRUPO_AGREGADO, "Permissão para Listar os Agregados")));

		// ======================================================== ANIMAL
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_ANIMAL", "Cadastro de Animal", this.NAME_GRUPO_ANIMAL, "Permissão para Cadastrar os Animais")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_TIPO_ANIMAL", "Cadastro Tipo de Animal", this.NAME_GRUPO_ANIMAL,
				"Permissão para Cadastrar Tipos de Animais")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_ANIMAL", "Editar Animal", this.NAME_GRUPO_ANIMAL, "Permissão para Editar Animais")));

		this.permissoes.add(new ArrayList<String>(Arrays
				.asList("ROLE_EDITAR_TIPO_ANIMAL", "Editar Tipo de Animal", this.NAME_GRUPO_ANIMAL, "Permissão para Editar Tipo de Animais")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_ANIMAL", "Listar Animais", this.NAME_GRUPO_ANIMAL, "Permissão para Listar os Animais")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_ANIMAL_MORADOR", "Listar Animais do Morador", this.NAME_GRUPO_ANIMAL,
				"Permissão para Listar os Animais do Morador")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EXCLUIR_TIPO_ANIMAL", "Excluir Tipo de Animal", this.NAME_GRUPO_ANIMAL,
				"Permissão para Excluir os Tipos de Animais")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EXCLUIR_ANIMAL", "Excluir Animal", this.NAME_GRUPO_ANIMAL, "Permissão para Excluir os Animais")));

		// ======================================================== CONTAS
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_REGISTRO_PAGAMENTO", "Registrar Pagamento", this.NAME_GRUPO_CONTAS, "Permissão para Registrar Pagamentos")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CONSULTAR_REGISTRO_PAGAMENTO", "Consultar Registro de Pagamento", this.NAME_GRUPO_CONTAS,
				"Permissão para Consultar Registro de Pagamentos")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ALTERAR_PARA_INADIMPLENTE", "Alterar para inadimplente", this.NAME_GRUPO_CONTAS,
				"Permissão para Alterar o Morador para Inadimplente")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ALTERAR_PARA_ADIMPLENTE", "Alterar para Adimplente", this.NAME_GRUPO_CONTAS,
				"Permissão para Alterar o Morador para Adimplente")));

		// ======================================================== ITEM RESERVA
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_ITEM_RESERVA", "Cadastrar Item Reserva", this.NAME_GRUPO_ITEM_RESERVA,
				"Permissão para Cadastrar Itens Reservas")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_ITEM_RESERVA", "Listar Itens Reserva", this.NAME_GRUPO_ITEM_RESERVA,
				"Permissão para Listar Itens Reservas")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_ITEM_RESERVA", "Editar Item Reserva", this.NAME_GRUPO_ITEM_RESERVA,
				"Permissão para Editar Itens Reservas")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ALTERAR_STATUS_ITEM_RESERVA", "Alterar Status do Item Reserva", this.NAME_GRUPO_ITEM_RESERVA,
				"Permissão para Alterar Status dos Itens Reserva")));

		// ======================================================== MORADOR
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_MORADOR_ADM", "Listar Todos os Moradores Inativos e Ativos", this.NAME_GRUPO_MORADOR,
				"Permissão para Listar todos os Moradores Ativos e Inativos")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_MORADOR", "Cadastrar Morador", this.NAME_GRUPO_MORADOR, "Permissão para Cadastrar Moradores")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_MORADOR", "Editar Morador", this.NAME_GRUPO_MORADOR, "Permissão para Editar Moradores")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_MORADOR", "Listar Moradores", this.NAME_GRUPO_MORADOR, "Permissão para Listar Moradores")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_DETALHES_MORADOR", "Listar Detalhes do Morador", this.NAME_GRUPO_MORADOR,
				"Permissão para Listar Detalhes do Morador")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ALTERAR_STATUS_INTERFONE", "Alterar Status do Interfone do Morador", this.NAME_GRUPO_MORADOR,
				"Permissão para Alterar Status do Interfone do Morador")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ALTERAR_STATUS_MORADOR", "Alterar Status do Morador", this.NAME_GRUPO_MORADOR,
				"Permissão para Alterar o Status do Morador")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_PAGINA_MORADOR", "Pagina do Morador", this.NAME_GRUPO_MORADOR,
				"Permissão para visualizar a página do Morador")));

		// ======================================================== OCORRENCIA
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_OCORRENCIA", "Cadastro de OcorrÃªncia", this.NAME_GRUPO_OCORRENCIA,
				"Permissão para Cadastrar Ocorrências")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_OCORRENCIA", "Editar OcorrÃªncia", this.NAME_GRUPO_OCORRENCIA, "Permissão para Editar Ocorrência")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_OCORRENCIA", "Listar Ocorrências", this.NAME_GRUPO_OCORRENCIA, "Permissão para Listar Ocorrências")));

		// ======================================================== PRESTADOR
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_PRESTADOR", "Editar Prestador de Serviços", this.NAME_GRUPO_PRESTADOR,
				"Permissão para Editar Prestadores de Serviços")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_PRESTADOR", "Cadastrar Prestador de Serviços", this.NAME_GRUPO_PRESTADOR,
				"Permissão para Cadastrar Prestadores de Serviços")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_TIPO_PRESTADOR", "Cadastro Tipo de Prestador de Serviços", this.NAME_GRUPO_PRESTADOR,
				"Permissão para Cadastrar Tipos de Prestador de Serviços")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_PRESTADOR", "Listar Prestadores de Serviços", this.NAME_GRUPO_PRESTADOR,
				"Permissão para Listar os Prestadores de Serviços")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_REGISTRAR_PRESTACAO_SERVICO", "Registrar Prestação de Serviços", this.NAME_GRUPO_PRESTADOR,
				"Permissão para Registrar Prestação de Serviços")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ADICIONAR_ENTRADA_LIVRE_PRESTADOR", "Adicionar Entrada Livre Prestador", this.NAME_GRUPO_PRESTADOR,
				"Permissão para Adicionar Entrada Livre")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_REMOVER_ENTRADA_LIVRE_PRESTADOR", "Remover Entrada Livre Prestador", this.NAME_GRUPO_PRESTADOR,
				"Permissão para Remover Entrada Livre")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EXCLUIR_TIPO_PRESTADOR", "Excluir Tipo de Prestador de Serviços", this.NAME_GRUPO_PRESTADOR,
				"Permissão para Excluir os Tipos de Prestador de Serviços")));

		// ======================================================== RESERVA
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_RESERVA", "Cadastrar Reserva", this.NAME_GRUPO_RESERVA, "Permissão para Cadastrar Itens Reserva")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_RESERVA", "Listar Reservas", this.NAME_GRUPO_RESERVA, "Permissão para Listar as Reservas")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_RESERVA", "Editar Reserva", this.NAME_GRUPO_RESERVA, "Permissão para Editar as Reservas")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EXCLUIR_RESERVA", "Excluir Reserva", this.NAME_GRUPO_RESERVA, "Permissão para Excluir as Reservas")));

		this.permissoes
				.add(new ArrayList<String>(Arrays.asList("ROLE_PAGAR_RESERVA", "Pagar Reserva", this.NAME_GRUPO_RESERVA, "Permissão para Efetuar o Pagamento das Reservas")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_APROVAR_RESERVA", "Aprovar Reserva", this.NAME_GRUPO_RESERVA,
				"Permissão para Aprovar Reservas pendentes de aprovação")));

		// ======================================================== VEICULO
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_VEICULO", "Cadastrar Veículo", this.NAME_GRUPO_VEICULO, "Permissão para Cadastrar Veículos")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_VEICULO", "Editar Veículo", this.NAME_GRUPO_VEICULO, "Permissão para Editar Veículos")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EXCLUIR_VEICULO", "Excluir Veículo", this.NAME_GRUPO_VEICULO, "Permissão para Excluir Veículos")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_VEICULOS", "Listar Veículos", this.NAME_GRUPO_VEICULO, "Permissão para Listar os Veículos")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_MARCA_VEICULO", "Cadastrar Marca do Veículo", this.NAME_GRUPO_VEICULO,
				"Permissão para Cadastrar Marcas de Veículos")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EXCLUIR_MARCA_VEICULO", "Excluir Marca do Veículo", this.NAME_GRUPO_VEICULO,
				"Permissão para Excluir Marcas de Veículos")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_MARCA_VEICULO", "Editar Marca do Veículo", this.NAME_GRUPO_VEICULO,
				"Permissão para Editar Marcas de Veículos")));

		// ======================================================== VISITANTE
		this.permissoes
				.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_VISITANTE", "Cadastrar Visitante", this.NAME_GRUPO_VISITANTE, "Permissão para Cadastrar Visitantes")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_VISITANTE", "Editar Visitante", this.NAME_GRUPO_VISITANTE, "Permissão para Editar Visitantes")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_VISITANTE", "Listar Visitantes", this.NAME_GRUPO_VISITANTE, "Permissão para Listar Visitante")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_REGISTRAR_VISITA", "Registrar Visita", this.NAME_GRUPO_VISITANTE, "Permissão para Registrar Visitas")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ADICIONAR_ENTRADA_LIVRE_VISITANTE", "Adicionar Entrada Livre Visitante", this.NAME_GRUPO_VISITANTE,
				"Permissão para Adicionar Entrada Livre")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_REMOVER_ENTRADA_LIVRE_VISITANTE", "Remover Entrada Livre Visitante", this.NAME_GRUPO_VISITANTE,
				"Permissão para Remover Entrada Livre")));

		// ======================================================== SEGURANCA
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_MODULOACESSO", "Cadastrar Módulo de Acesso", this.NAME_GRUPO_SEGURANCA,
				"Permissão para Cadastar Módulos de Acesso")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_MODULOACESSO", "Editar Módulo de Acesso", this.NAME_GRUPO_SEGURANCA,
				"Permissão para Editar Módulos de Acesso")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_MODULOACESSO", "Listar Módulos de Acesso", this.NAME_GRUPO_SEGURANCA,
				"Permissão para Listar os Módulos de Acesso")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_CADASTRO_USUARIO", "Cadastrar Usuário", this.NAME_GRUPO_SEGURANCA,
				"Permissão para Cadastrar Usuários do Sistema")));

		this.permissoes
				.add(new ArrayList<String>(Arrays.asList("ROLE_LISTA_USUARIO", "Listar Usuários", this.NAME_GRUPO_SEGURANCA, "Permissão para Listar os Usuários do Sistema")));

		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_ALTERAR_STATUS_USUARIO", "Alterar Status do Usuário", this.NAME_GRUPO_SEGURANCA,
				"Permissão para Alterar o Status dos Usuários do Sistema")));

		this.permissoes
				.add(new ArrayList<String>(Arrays.asList("ROLE_EDITAR_USUARIO", "Editar Usuário", this.NAME_GRUPO_SEGURANCA, "Permissão para Editar os Usuários do Sistema")));

		// ======================================================== RELATORIO
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_RELATORIOS", "Gerar Relatórios", this.NAME_GRUPO_RELATORIOS, "Permissão para Gerar Relatórios")));

		// ======================================================== HISTORICO
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_VER_HISTORICO", "Ver Histórico", this.NAME_GRUPO_HISTORICO, "Permissão para visualizar o Histórico")));

		// ======================================================== TOTEM
		// MORADOR
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_TOTEN", "Alterar Toten", this.NAME_GRUPO_TOTEN, "Permissão para visualizar e alterar senha Toten")));

		// ======================================================== AGENDA
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_AGENDA_SINDICO", "Agenda do síndico", this.NAME_GRUPO_FERRAMENTAS,
				"Permissão para visualizar e alterar eventos na agenda do síndico")));
		
		// ======================================================== RELATÓRIO DE UTILIZAÇÃO
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_UTILIZACAO", "Relatório de Utilização", this.NAME_GRUPO_FERRAMENTAS,
				"Permissão para visualizar relatório de utilização do sistema")));

		// ======================================================== ENVIAR
		// ARQUIVOS
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_DISPONIBILIZAR_ARQUIVOS", "Disponibilizar Arquivos", this.NAME_GRUPO_FERRAMENTAS,
				"Permissão para enviar arquivos para moradores")));
		
		this.permissoes.add(new ArrayList<String>(Arrays.asList("ROLE_BAIXAR_ARQUIVOS", "Baixar Arquivos", this.NAME_GRUPO_FERRAMENTAS,
				"Permissão para baixar os arquivos na pagina do morador")));

	}

	String NAME_GRUPO_MINIMO = "Mínimo", NAME_GRUPO_ROOT = "Root", NAME_GRUPO_COMUNICACAO = "Comunicação", NAME_GRUPO_CONDOMINIO = "Condomínio", NAME_GRUPO_SINDICO = "Síndico",
			NAME_GRUPO_AGREGADO = "Agregado", NAME_GRUPO_ANIMAL = "Animal", NAME_GRUPO_CONTAS = "Contas", NAME_GRUPO_ITEM_RESERVA = "Item Reserva", NAME_GRUPO_MORADOR = "Morador",
			NAME_GRUPO_OCORRENCIA = "Ocorrência", NAME_GRUPO_PRESTADOR = "Prestador", NAME_GRUPO_RESERVA = "Reserva", NAME_GRUPO_VEICULO = "Veículo",
			NAME_GRUPO_VISITANTE = "Visitante", NAME_GRUPO_SEGURANCA = "Segurança", NAME_GRUPO_RELATORIOS = "Relatorios", NAME_GRUPO_HISTORICO = "Histórico",
			NAME_GRUPO_TOTEN = "Toten", NAME_GRUPO_FINANCEIRO = "Financeiro", NAME_GRUPO_FERRAMENTAS = "Ferramentas";

	public void setarNomesGrupos() {

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_MINIMO)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_ROOT)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_COMUNICACAO)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_CONDOMINIO)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_SINDICO)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_AGREGADO)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_ANIMAL)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_CONTAS)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_ITEM_RESERVA)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_MORADOR)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_OCORRENCIA)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_PRESTADOR)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_RESERVA)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_VEICULO)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_VISITANTE)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_SEGURANCA)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_RELATORIOS)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_HISTORICO)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_TOTEN)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_FINANCEIRO)));

		this.grupos.add(new ArrayList<String>(Arrays.asList(this.NAME_GRUPO_FERRAMENTAS)));

	}

}
