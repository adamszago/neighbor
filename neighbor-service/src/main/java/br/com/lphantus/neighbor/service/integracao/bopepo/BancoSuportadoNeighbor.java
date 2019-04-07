package br.com.lphantus.neighbor.service.integracao.bopepo;

import java.util.HashMap;

import org.jrimum.domkee.comum.pessoa.id.cprf.CNPJ;
import org.jrimum.domkee.financeiro.banco.febraban.Banco;
import org.jrimum.domkee.financeiro.banco.febraban.CodigoDeCompensacaoBACEN;

public enum BancoSuportadoNeighbor {

	/**
	 * Tipo enumerado que representa o <strong>Banco do Brasil</strong>, código
	 * de compensação <strong><tt>001</tt></strong> <a
	 * href="http://www.bb.com.br">site</a>.
	 * 
	 * 
	 */
	BANCO_DO_BRASIL("001", "00000000000191", "BANCO DO BRASIL S.A.",
			"Banco do Brasil - Banco Múltiplo"),

	/**
	 * Tipo enumerado que representa o Banestes, Banco <strong>do Estado do
	 * Espírito Santo</strong>, código de compensação <strong><tt>021</tt>
	 * </strong> <a href="http://www.banestes.com.br"> site</a>.
	 * 
	 * 
	 */
	BANCO_DO_ESTADO_DO_ESPIRITO_SANTO("021", "28127603000178",
			"BANCO DO ESTADO DO ESPIRITO SANTO S.A.", "Banco Múltiplo"),

	/**
	 * Tipo enumerado que representa o Santander, <strong>Banco Santander
	 * (Brasil) S.A.</strong>, código de compensação <strong><tt>021</tt>
	 * </strong> <a href="http://www.santander.com.br"> site</a>
	 */
	BANCO_SANTANDER("033", "90400888000142", "BANCO SANTANDER (BRASIL) S.A.",
			"Banco Múltiplo"),

	/**
	 * Tipo enumerado que representa o Banrisul, Banco <strong>do Estado do Rio
	 * Grande do Sul</strong>, código de compensação <strong><tt>041</tt>
	 * </strong> <a href="http://www.banrisul.com.br/"> site</a>.
	 * 
	 * 
	 */
	BANCO_DO_ESTADO_DO_RIO_GRANDE_DO_SUL("041", "92702067000196",
			"BANCO DO ESTADO DO RIO GRANDE DO SUL S.A.", "Banco Múltiplo"),

	/**
	 * Tipo enumerado que representa o Banco <strong>Caixa Econômica
	 * Federal</strong>, código de compensação <strong><tt>104</tt></strong> <a
	 * href="http://www.caixa.gov.br">site</a>.
	 * 
	 * 
	 */
	CAIXA_ECONOMICA_FEDERAL("104", "00360305000104", "CAIXA ECONOMICA FEDERAL",
			"Caixa Econômica Federal"),

	/**
	 * Tipo enumerado que representa o Banco <strong>Nossa Caixa</strong>,
	 * código de compensação <strong><tt>151</tt></strong> <a
	 * href="http://www.nossacaixa.com.br/">site</a>.
	 */
	NOSSA_CAIXA("151", "43073394000110", "BANCO NOSSA CAIXA S.A.",
			"Banco Múltiplo"),

	/**
	 * Tipo enumerado que representa o Banco <strong>Bradesco</strong>, código
	 * de compensação <strong><tt>237</tt></strong> <a
	 * href="http://www.bradesco.com.br">site</a>.
	 * 
	 * 
	 */
	BANCO_BRADESCO("237", "60746948000112", "BANCO BRADESCO S.A.",
			"Banco Múltiplo"),

	/**
	 * Tipo enumerado que representa o <strong>Banco Itaú</strong>, código de
	 * compensação <strong><tt>341</tt></strong> <a
	 * href="http://www.itau.com.br">site</a>.
	 * 
	 * 
	 */
	BANCO_ITAU("341", "60701190000104", "BANCO ITAÚ S.A.", "Banco Múltiplo"),

	/**
	 * Tipo enumerado que representa o <strong>Banco ABN AMRO Real</strong> (<a
	 * href="http://www.bancoreal.com.br">http://www.bancoreal.com.br</a>),
	 * código de compensação <strong><tt>356</tt></strong>. <br/>
	 * <p>
	 * Obs: Os bancos <strong>Sudameris</strong> e <strong>Bandepe</strong>
	 * foram incorporados ao Banco Real, portanto para gerar boletos bancários
	 * dos bancos citados utilize este tipo enumerado.
	 * </p>
	 * 
	 */
	BANCO_ABN_AMRO_REAL("356", "33066408000115", "BANCO ABN AMRO REAL S.A.",
			"Banco Múltiplo"),

	/**
	 * Tipo enumerado que representa o <strong>Banco Mercantil do
	 * Brasil</strong> (<a
	 * href="http://www.mercantildobrasil.com.br">http://www.
	 * mercantildobrasil.com.br</a>), código de compensação <strong><tt>389</tt>
	 * </strong>.
	 * 
	 */
	MERCANTIL_DO_BRASIL("389", "17184037000110",
			"BANCO MERCANTIL DO BRASIL S.A.", "Banco Múltiplo"),

	/**
	 * Tipo enumerado que representa o <strong>HSBC</strong>, código de
	 * compensação <strong><tt>399</tt></strong> <a
	 * href="http://www.hsbc.com.br">site</a>.
	 * 
	 * 
	 */
	HSBC("399", "01701201000189", "HSBC BANK BRASIL S.A.", "Banco Múltiplo"),

	/**
	 * Tipo enumerado que representa o <strong>Unibanco</strong>, código de
	 * compensação <strong><tt>409</tt></strong> <a
	 * href="http://www.unibanco.com.br">site</a>.
	 * 
	 * 
	 */
	UNIBANCO("409", "33700394000140",
			"UNIBANCO-UNIAO DE BANCOS BRASILEIROS S.A.", "Banco Múltiplo"),

	/**
	 * Tipo enumerado que representa o <strong>Unibanco</strong>, código de
	 * compensação <strong><tt>422</tt></strong> <a
	 * href="http://www.safra.com.br/">site</a>.
	 * 
	 * 
	 */
	BANCO_SAFRA("422", "58160789000128", "BANCO SAFRA S.A.", "Banco Múltiplo");

	/**
	 * Singleton <code>Map</code> para pesquisa por bancos suportados no
	 * componente.
	 * 
	 * 
	 */
	public static final HashMap<String, BancoSuportadoNeighbor> suportados = new HashMap<String, BancoSuportadoNeighbor>(
			BancoSuportadoNeighbor.values().length);

	static {

		suportados.put(BANCO_DO_BRASIL.codigoDeCompensacaoBACEN,
				BANCO_DO_BRASIL);

		suportados.put(CAIXA_ECONOMICA_FEDERAL.codigoDeCompensacaoBACEN,
				CAIXA_ECONOMICA_FEDERAL);

		suportados.put(BANCO_BRADESCO.codigoDeCompensacaoBACEN, BANCO_BRADESCO);

		suportados.put(BANCO_ABN_AMRO_REAL.codigoDeCompensacaoBACEN,
				BANCO_ABN_AMRO_REAL);

		suportados.put(UNIBANCO.codigoDeCompensacaoBACEN, UNIBANCO);

		suportados.put(HSBC.codigoDeCompensacaoBACEN, HSBC);

		suportados.put(BANCO_ITAU.codigoDeCompensacaoBACEN, BANCO_ITAU);

		suportados.put(BANCO_SAFRA.codigoDeCompensacaoBACEN, BANCO_SAFRA);

		suportados.put(
				BANCO_DO_ESTADO_DO_RIO_GRANDE_DO_SUL.codigoDeCompensacaoBACEN,
				BANCO_DO_ESTADO_DO_RIO_GRANDE_DO_SUL);

		suportados.put(MERCANTIL_DO_BRASIL.codigoDeCompensacaoBACEN,
				MERCANTIL_DO_BRASIL);

		suportados.put(NOSSA_CAIXA.codigoDeCompensacaoBACEN, NOSSA_CAIXA);

		suportados.put(
				BANCO_DO_ESTADO_DO_ESPIRITO_SANTO.codigoDeCompensacaoBACEN,
				BANCO_DO_ESTADO_DO_ESPIRITO_SANTO);
	}

	/**
	 * Códigos de instituições bancárias na compensação - COMPE <a
	 * href="http://www.bcb.gov.br/?CHEQUESCOMPE">BACEN</a>.
	 * 
	 * 
	 */
	private String codigoDeCompensacaoBACEN;

	/**
	 * CNPJ registrado na <a
	 * href="http://www.bcb.gov.br/?CHEQUESCOMPE">BACEN</a>.
	 * 
	 * 
	 */
	private String cNPJ;

	/**
	 * Nome da instituição registrado na <a
	 * href="http://www.bcb.gov.br/?CHEQUESCOMPE">BACEN</a>.
	 * 
	 * 
	 */
	private String instituicao;

	/**
	 * Segmento bancário da instituição registrado na <a
	 * href="http://www.bcb.gov.br/?CHEQUESCOMPE">BACEN</a>.
	 * 
	 * 
	 */
	private String segmento;

	/**
	 * <p>
	 * Construtor naturalmente <code>private</code> responsável por criar uma
	 * única instância para cada banco.
	 * </p>
	 * 
	 * @param codigoDeCompensacaoBACEN
	 * @param cNPJ
	 * @param instituicao
	 * @param segmento
	 * 
	 * @see java.lang.Enum
	 * @see <a
	 *      href="http://java.sun.com/j2se/1.5.0/docs/guide/language/enums.html">Enum
	 *      Guide</a>
	 * 
	 * 
	 * 
	 */
	private BancoSuportadoNeighbor(final String codigoDeCompensacaoBACEN,
			final String cnpj, final String instituicao, final String segmento) {
		this.codigoDeCompensacaoBACEN = codigoDeCompensacaoBACEN;
		this.cNPJ = cnpj;
		this.instituicao = instituicao;
		this.segmento = segmento;
	}

	/**
	 * <p>
	 * Verifica se exite suporte (implementação) de "Campos Livres" para o banco
	 * representado pelo <code>codigoDeCompensacao</code>.
	 * </p>
	 * 
	 * @param codigoDeCompensacao
	 * @return verdadeiro se existe implementação para o banco em questão.
	 */
	public static boolean isSuportado(final String codigoDeCompensacao) {
		return suportados.containsKey(codigoDeCompensacao);
	}

	/**
	 * <p>
	 * Cria uma instância para o banco representado pelo tipo enumerado.
	 * </p>
	 * <p>
	 * Cada instância retornada por este método contém:
	 * <ul>
	 * <li><tt>Código de componsação</tt></li>
	 * <li><tt>Nome da instituição</tt></li>
	 * <li><tt>CNPJ da instituição</tt></li>
	 * <li><tt>Segmento da instituição bancária</tt></li>
	 * </ul>
	 * </p>
	 * 
	 * @return Uma instância do respectivo banco.
	 * 
	 * @see br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Banco#Banco(CodigoDeCompensacaoBACEN,
	 *      String, CNPJ, String)
	 * @see <a href="http://www.bcb.gov.br/?CHEQUESCOMPE">Bancos supervisionados
	 *      pela BACEN</a>
	 */
	public Banco create() {
		return new Banco(new CodigoDeCompensacaoBACEN(
				this.codigoDeCompensacaoBACEN), this.instituicao, new CNPJ(
				this.cNPJ), this.segmento);
	}

	/**
	 * @return the codigoDeCompensacaoBACEN
	 * 
	 */
	public String getCodigoDeCompensacao() {
		return this.codigoDeCompensacaoBACEN;
	}

	/**
	 * @return the cNPJ
	 * 
	 */
	public String getCNPJ() {
		return this.cNPJ;
	}

	/**
	 * @return the instituicao
	 * 
	 */
	public String getInstituicao() {
		return this.instituicao;
	}

	/**
	 * @return the segmento
	 * 
	 */
	public String getSegmento() {
		return this.segmento;
	}

}
