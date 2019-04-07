package br.com.lphantus.neighbor.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeMoeda;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.BoletaDTO;
import br.com.lphantus.neighbor.common.CarteiraDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Boleta;
import br.com.lphantus.neighbor.entity.Duplicata;
import br.com.lphantus.neighbor.entity.DuplicataParcela;
import br.com.lphantus.neighbor.entity.DuplicataParcelaPK;
import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.Pessoa;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;
import br.com.lphantus.neighbor.entity.Usuario;
import br.com.lphantus.neighbor.enums.StatusBoleta;
import br.com.lphantus.neighbor.repository.IBoletoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IBoletoService;
import br.com.lphantus.neighbor.service.ICarteiraService;
import br.com.lphantus.neighbor.service.IConfiguracaoCondominioService;
import br.com.lphantus.neighbor.service.IDuplicataParcelaService;
import br.com.lphantus.neighbor.service.IDuplicataService;
import br.com.lphantus.neighbor.service.IFaturaService;
import br.com.lphantus.neighbor.service.ILancamentoService;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IPessoaFisicaService;
import br.com.lphantus.neighbor.service.IUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.integracao.bopepo.BancoSuportadoNeighbor;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.utils.Constantes;
import br.com.lphantus.neighbor.utils.Utilitarios;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class BoletoServiceImpl extends GenericService<Long, BoletaDTO, Boleta> implements IBoletoService {

	@Autowired
	private IBoletoDAO boletoDAO;

	@Autowired
	private IMoradorService moradorService;

	@Autowired
	private IUnidadeHabitacionalService unidadeHabitacionalService;

	@Autowired
	private ICarteiraService carteiraService;

	@Autowired
	private IFaturaService faturaService;

	@Autowired
	private IDuplicataService duplicataService;

	@Autowired
	private IDuplicataParcelaService duplicataParcelaService;

	@Autowired
	private ILancamentoService lancamentoService;

	@Autowired
	private IPessoaFisicaService pessoaFisicaService;

	@Autowired
	private IConfiguracaoCondominioService configuracaoCondominioService;

	@Override
	public File gerarBoleto(final BoletaDTO boleta) throws ServiceException {

		// somente para testes
		// boleta.getEmissor().getPessoa().setCnpj("17.958.397/0001-21");

		final Boleta entidade = BoletaDTO.Builder.getInstance().createEntity(boleta);
		try {
			final BoletoViewer boletoViewer = new BoletoViewer(this.preencherBoleto(boleta));

			final File arquivoPdf = boletoViewer.getPdfAsFile("boleto_".concat(boleta.getDuplicata().getFatura().getLancamentos().get(0).getPessoa().getNome()).concat(Utilitarios.diaSemBarras())
					.concat(".pdf"));

			// mostreBoletoNaTela(arquivoPdf);

			this.boletoDAO.save(entidade);

			return arquivoPdf;
		} catch (final DAOException exception) {
			getLogger().error("Erro ao gerar Boleto.", exception);
			throw new ServiceException("Erro ao gerar Boleto.", exception);
		} catch (final Exception exception) {
			throw new ServiceException(exception.getMessage(), exception);
		}

	}

	@Override
	public File gerarBoleto(final CondominioDTO condominio, final CarteiraDTO carteiraSelecionada) throws ServiceException {
		try {

			validarInformacoes(carteiraSelecionada);

			final CarteiraDTO carteiraBuscada = this.carteiraService.findById(carteiraSelecionada.getId()).createDto();

			final List<Boleto> boletosPdf = new ArrayList<Boleto>();

			final String nome = String.format("boleto_cond_%s_%s.pdf", condominio.getNomeAbreviado(), Utilitarios.diaSemBarras());

			final List<FaturaDTO> faturas = this.faturaService.buscarFaturasSemDuplicata(condominio);

			for (final FaturaDTO fatura : faturas) {
				final Duplicata duplicata = salvarDuplicata(fatura, condominio);
				final DuplicataParcela duplicataParcela = salvarParcelaDuplicata(duplicata, fatura, condominio, carteiraBuscada);

				final BoletaDTO boletaDto = gerarBoleta(duplicata, duplicataParcela, fatura, condominio, carteiraBuscada);
				boletosPdf.add(preencherBoleto(boletaDto));
			}

			return BoletoViewer.groupInOnePDF(boletosPdf, nome);

		} catch (final DAOException exception) {
			getLogger().error("Erro ao gerar Boleto.", exception);
			throw new ServiceException("Erro ao gerar Boleto.", exception);
		} catch (final Exception exception) {
			getLogger().error("Erro ao gerar Boleto.", exception);
			throw new ServiceException(exception.getMessage(), exception);
		}
	}

	private void validarInformacoes(final CarteiraDTO carteiraSelecionada) throws ServiceException {
		if ((null == carteiraSelecionada) || (null == carteiraSelecionada.getId())) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("BOL_GER_SEL_CARTEIRA"));
		}
	}

	private BoletaDTO gerarBoleta(final Duplicata duplicata, final DuplicataParcela duplicataParcela, final FaturaDTO fatura, final CondominioDTO condominio, final CarteiraDTO carteiraSelecionada)
			throws DAOException, ServiceException {

		final BoletaDTO dto = new BoletaDTO();

		dto.setCarteira(carteiraSelecionada);
		dto.setDataDocumento(duplicataParcela.getDataPagamento());
		dto.setDataProcessamento(duplicata.getDataCadastro());
		dto.setDataVencimento(duplicataParcela.getDataPagamento());
		dto.setEmissor(condominio);
		dto.setSacado(fatura.getLancamentos().get(0).getPessoa());
		dto.setStatusBoleto(StatusBoleta.ABERTO);
		dto.setDuplicata(duplicata.createDto());
		dto.getDuplicata().setParcelas(new ArrayList<DuplicataParcelaDTO>(Arrays.asList(new DuplicataParcelaDTO[] { duplicataParcela.createDto() })));

		dto.setLocalParaPagamento(GerenciadorMensagem.getMensagem("BOL_LOCAL_PAGTO"));
		dto.setInstrucao1(GerenciadorMensagem.getMensagem("BOL_INSTRUC_1"));

		// somente para testes
		// dto.getEmissor().getPessoa().setCnpj("17.958.397/0001-21");

		final Boleta entidade = BoletaDTO.Builder.getInstance().createEntity(dto);
		this.boletoDAO.save(entidade);

		return entidade.createDto();
	}

	private DuplicataParcela salvarParcelaDuplicata(final Duplicata duplicata, final FaturaDTO fatura, final CondominioDTO condominio, final CarteiraDTO carteiraSelecionada) throws ServiceException {
		final DuplicataParcela retorno = new DuplicataParcela();

		final ConfiguracaoCondominioDTO configuracao = this.configuracaoCondominioService.buscarPorCondominio(condominio);

		retorno.setAbatimento(configuracao.getAbatimento());

		final Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(fatura.getData());
		calendar.add(Calendar.DAY_OF_YEAR, configuracao.getLimiteDias().intValue());
		retorno.setDataLimiteVencto(calendar.getTime());

		retorno.setDuplicataParcelaPK(new DuplicataParcelaPK());
		retorno.getDuplicataParcelaPK().setNumeroParcela(BigDecimal.ONE.longValue());
		retorno.setDataPagamento(fatura.getData());
		retorno.setDesconto(configuracao.getDesconto());
		retorno.setDuplicata(duplicata);
		retorno.setMoraOutrosRecebimentos(configuracao.getMoraOutrosRecebimentos());
		retorno.setMultaVencimento(configuracao.getMultaVencimento());
		retorno.setTaxaDia(configuracao.getTaxaDia());
		retorno.setValor(fatura.getValor());

		this.duplicataParcelaService.save(retorno);
		// this.duplicataService.save(duplicata);

		return retorno;
	}

	private Duplicata salvarDuplicata(final FaturaDTO fatura, final CondominioDTO condominio) throws ServiceException {

		final Duplicata duplicata = new Duplicata();

		duplicata.setFatura(FaturaDTO.Builder.getInstance().createEntity(fatura));
		duplicata.setCondominio(CondominioDTO.Builder.getInstance().createEntity(condominio));
		duplicata.setDataCadastro(new Date());
		duplicata.setValorPago(BigDecimal.ZERO);
		// this.duplicataService.save(duplicata);

		return duplicata;
	}

	@Override
	protected void saveValidate(final Boleta entity) throws ServiceException {

	}

	private Boleto preencherBoleto(final BoletaDTO boleto) throws ServiceException {
		Boleto bol = null;
		try {

			Cedente cedente = null;
			Sacado sacado = null;
			String documentoSacado = "";
			Pessoa pessoaSacado;

			// se houver o parametro de configuracao do condominio, o boleto eh
			// gerado com este documento
			String documentoCedente = obterDocumentoCedente(boleto);
			String nomeCedente = obterNomeCedente(boleto);
			if (StringUtils.isNotBlank(documentoCedente) && StringUtils.isNotBlank(nomeCedente)) {
				cedente = new Cedente(nomeCedente, documentoCedente);
			} else {
				throw new ServiceException("O Cedente precisa ser informado!");
			}

			final Duplicata dupl = this.duplicataService.findById(boleto.getDuplicata().getId());
			final Lancamento lancto = this.lancamentoService.findById(dupl.getFatura().getLancamentos().get(0).getId());
			// pessoaSacado = this.boletoDAO.buscarPessoa(boleto.getDuplicata()
			// .getFatura().getLancamentos().get(0).getPessoa()
			// .getIdPessoa());
			pessoaSacado = lancto.getPessoa();

			if (pessoaSacado == null) {
				throw new ServiceException("Nenhum Sacado foi identificado");
			} else {
				if (pessoaSacado instanceof Morador) {
					final PessoaFisica pessoa = (PessoaFisica) this.pessoaFisicaService.findById(((Morador) pessoaSacado).getIdPessoa());
					documentoSacado = pessoa.getCpf();
				} else if (pessoaSacado instanceof PessoaJuridica) {
					documentoSacado = ((PessoaJuridica) pessoaSacado).getCnpj();
				} else if (pessoaSacado instanceof PessoaFisica) {
					documentoSacado = ((PessoaFisica) pessoaSacado).getCpf();
				} else if (pessoaSacado instanceof Usuario) {
					final PessoaFisica pessoa = (PessoaFisica) this.pessoaFisicaService.findById(((Usuario) pessoaSacado).getIdPessoa());
					documentoSacado = pessoa.getCpf();
				} else {
					try {
						final PessoaFisica pessoa = (PessoaFisica) this.pessoaFisicaService.findById(pessoaSacado.getIdPessoa());
						documentoSacado = pessoa.getCpf();
					} catch (final Exception e) {
					}
				}
				sacado = new Sacado(pessoaSacado.getNome(), documentoSacado);
			}

			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.BANCO_ABN_AMRO_REAL)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.BANCO_BRADESCO)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.BANCO_DO_BRASIL)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.BANCO_DO_ESTADO_DO_ESPIRITO_SANTO)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.BANCO_DO_ESTADO_DO_RIO_GRANDE_DO_SUL)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.BANCO_ITAU)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.BANCO_SAFRA)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.BANCO_SANTANDER)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.CAIXA_ECONOMICA_FEDERAL)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.HSBC)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.MERCANTIL_DO_BRASIL)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.NOSSA_CAIXA)) {

			}
			if ((boleto.getCarteira().getBanco() != null) && boleto.getCarteira().getBanco().equals(BancoSuportadoNeighbor.UNIBANCO)) {

			}

			/**
			 * Precisa ser obtido atraves da Fatura
			 */
			// Elias no caso do condominio ter um so endereco, pode ser o
			// endereco do condominio associado as informacoes da unidade
			// habitacional

			// final PessoaDTO pessoa = boleto.getDuplicata().getFatura()
			// .getLancamentos().get(0).getPessoa();
			final PessoaDTO pessoa = new PessoaDTO();
			pessoa.setIdPessoa(pessoaSacado.getIdPessoa());
			final MoradorDTO morador = this.moradorService.buscarPorPessoa(pessoa);

			final UnidadeHabitacionalDTO unidade = this.unidadeHabitacionalService.buscarUnidadeHabitacionalMorador(morador);

			final Endereco enderecoSac = new Endereco();
			if (unidade != null) {
				enderecoSac.setUF(unidade.getEndereco().getUf());
				enderecoSac.setLocalidade(unidade.getEndereco().getCidade());
				enderecoSac.setCep(new CEP(unidade.getEndereco().getCep()));
				enderecoSac.setBairro(unidade.getEndereco().getBairro());
				enderecoSac.setLogradouro(unidade.getEndereco().getLogradouro());
				enderecoSac.setComplemento(unidade.getComplemento());
				enderecoSac.setNumero(String.valueOf(unidade.getNumero()));
			}
			sacado.addEndereco(enderecoSac);

			/**
			 * Dados ficticios, mas serao obtidos atraves da Carteira
			 */

			final ContaBancaria contaBancaria = new ContaBancaria();
			final String bancoCarteira = boleto.getCarteira().getBanco();

			for (final BancoSuportadoNeighbor banco : BancoSuportadoNeighbor.values()) {
				if (banco.getCodigoDeCompensacao().equals(bancoCarteira)) {
					contaBancaria.setBanco(banco.create());
					break;
				}
			}

			// Tambem obtido atraves da Carteira
			if (StringUtils.isNotBlank(boleto.getCarteira().getDigitoAgencia())) {
				contaBancaria.setAgencia(new Agencia(Integer.valueOf(boleto.getCarteira().getNumeroAgencia()), boleto.getCarteira().getDigitoAgencia()));
			} else {
				contaBancaria.setAgencia(new Agencia(Integer.valueOf(boleto.getCarteira().getNumeroAgencia()), BigDecimal.ZERO.setScale(0).toString()));
			}

			contaBancaria.setNumeroDaConta(new NumeroDaConta(Integer.valueOf(boleto.getCarteira().getNumeroConta()), boleto.getCarteira().getDigitoConta()));

			// Precisamos incluir este atributo na Carteira
			contaBancaria.setCarteira(new Carteira(Integer.valueOf(boleto.getCarteira().getNumeroCarteira())));

			final Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
			titulo.setNumeroDoDocumento(buscaProximoIdBoleto().toString());

			// sempre busca o nosso numero do banco, para garantir que duas
			// transacoes nao gerem o mesmo nosso numero
			final CarteiraDTO carteira = this.carteiraService.findById(boleto.getCarteira().getId()).createDto();
			titulo.setNossoNumero(carteira.getNossoNumero());
			this.carteiraService.atualizaBoleto(carteira);

			// Vamos colocar isso na carteira???
			titulo.setDigitoDoNossoNumero(boleto.getCarteira().getDigitoNossoNumero());

			titulo.setValor(boleto.getDuplicata().getFatura().getValor());
			titulo.setDataDoDocumento(boleto.getDataDocumento());
			titulo.setDataDoVencimento(boleto.getDataVencimento());
			titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);

			final DuplicataParcelaDTO parcela = boleto.getDuplicata().getParcelas().get(0);

			titulo.setDesconto(parcela.getDesconto());
			titulo.setAceite(Aceite.N);
			titulo.setTipoDeMoeda(TipoDeMoeda.REAL);

			bol = new Boleto(titulo);

			bol.setLocalPagamento(boleto.getLocalParaPagamento());

			bol.setInstrucao1(boleto.getInstrucao1());
			bol.setInstrucao2(boleto.getInstrucao2());
			bol.setInstrucao3(boleto.getInstrucao3());

		} catch (final Exception exception) {
			getLogger().error("Erro preencher dados do boleto.", exception);
			throw new ServiceException("Erro preencher dados do boleto.", exception);
		}
		return bol;
	}

	private String obterNomeCedente(BoletaDTO boleto) throws ServiceException {
		ConfiguracaoCondominioDTO config = configuracaoCondominioService.buscarPorCondominio(boleto.getDuplicata().getCondominio());
		if (StringUtils.isNotBlank(config.getNomeEmissorDocumentos())) {
			return config.getNomeEmissorDocumentos();
		} else if (StringUtils.isNotBlank(boleto.getEmissor().getPessoa().getNome())) {
			return boleto.getEmissor().getPessoa().getNome();
		}
		return Constantes.VAZIO;
	}

	private String obterDocumentoCedente(BoletaDTO boleto) throws ServiceException {
		ConfiguracaoCondominioDTO config = configuracaoCondominioService.buscarPorCondominio(boleto.getDuplicata().getCondominio());
		if (StringUtils.isNotBlank(config.getCpfEmissorDocumentos())) {
			return config.getCpfEmissorDocumentos();
		} else if (StringUtils.isNotBlank(boleto.getEmissor().getPessoa().getCnpj())) {
			return boleto.getEmissor().getPessoa().getCnpj();
		}
		return Constantes.VAZIO;
	}

	private Long buscaProximoIdBoleto() throws ServiceException {
		try {
			return this.boletoDAO.buscaProximoIdBoleto();
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar id do boleto.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<BoletaDTO> listarBoletos(UsuarioDTO sacado) throws ServiceException {
		try {
			return this.boletoDAO.listarBoletos(sacado);
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar id do boleto.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

}
