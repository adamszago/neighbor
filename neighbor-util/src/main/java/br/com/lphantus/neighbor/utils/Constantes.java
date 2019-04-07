package br.com.lphantus.neighbor.utils;

import java.math.BigDecimal;

/**
 * Contem as constantes da aplicação
 * 
 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
 * @since 14/10/2014
 * 
 */
public final class Constantes {

	/**
	 * Construtor privado
	 */
	private Constantes() {

	}

	/**
	 * Representa as constantes relacionadas à parte de mapeamento e jdbc.
	 * 
	 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
	 * @since 23/10/2014
	 *
	 */
	public interface ConstantesJPA {

		public static final int CHUNK_SIZE_JOBS = 1;
		
		public static final String JNDI_DATASOURCE = "jdbc/lpneighbor_ds";
		public static final String ENTITY_PACKAGE = "br.com.lphantus.neighbor.entity";
		public static final String PERSISTENCE_UNIT = "neighbor_pu";

		public static final String SQL_MORADOR_ITEM_READER_ANTIGO = "SELECT "
				+ "	totem.ID_TOTEM, totem.ATIVO, totem.SENHA, totem.ID_MORADOR AS ID_PESSOA, pessoa.NOME, unidade.IDENTIFICACAO, 1 AS TIPO_TOTEM "
				+ "FROM TOTEM totem "
				+ "	INNER JOIN MORADOR morador ON totem.ID_MORADOR = morador.ID_MORADOR "
				+ "	INNER JOIN MORADOR_UNIDADE_HABITACIONAL relacionamento ON morador.ID_MORADOR = relacionamento.ID_MORADOR "
				+ "	INNER JOIN UNIDADE_HABITACIONAL unidade ON relacionamento.ID_UNIDADE_HABITACIONAL = unidade.ID_UNIDADE_HABITACIONAL "
				+ "	INNER JOIN PESSOA pessoa ON morador.ID_MORADOR = pessoa.ID_PESSOA "
				+ "	INNER JOIN CONDOMINIO condominio ON unidade.ID_CONDOMINIO = condominio.ID_CONDOMINIO "
				+ "WHERE 1 = 1 "
				+ "	AND totem.ATIVO = true AND pessoa.STATUS = true "
				+ "	AND (? IS NULL OR condominio.ID_CONDOMINIO = ?) ";
		public static final String SQL_AGREGADO_ITEM_READER_ANTIGO = "SELECT "
				+ " totem.ID_TOTEM, totem.ATIVO, totem.SENHA, totem.ID_AGREGADO AS ID_PESSOA, pessoa.NOME, unidade.IDENTIFICACAO, 2 AS TIPO_TOTEM "
				+ "FROM TOTEM totem "
				+ "	INNER JOIN AGREGADO agregado ON totem.ID_AGREGADO = agregado.ID_AGREGADO "
				+ "	INNER JOIN MORADOR_AGREGADO relag ON agregado.ID_AGREGADO = relag.ID_AGREGADO "
				+ "	INNER JOIN MORADOR morador ON relag.ID_MORADOR = morador.ID_MORADOR "
				+ "	INNER JOIN MORADOR_UNIDADE_HABITACIONAL relacionamento ON morador.ID_MORADOR = relacionamento.ID_MORADOR "
				+ "	INNER JOIN UNIDADE_HABITACIONAL unidade ON relacionamento.ID_UNIDADE_HABITACIONAL = unidade.ID_UNIDADE_HABITACIONAL "
				+ "	INNER JOIN PESSOA pessoa ON agregado.ID_AGREGADO = pessoa.ID_PESSOA "
				+ "	INNER JOIN CONDOMINIO condominio ON unidade.ID_CONDOMINIO = condominio.ID_CONDOMINIO "
				+ "WHERE 1 = 1  "
				+ "	AND totem.ATIVO = true AND pessoa.STATUS = true "
				+ "	AND (? IS NULL OR condominio.ID_CONDOMINIO = ?) ";
		public static final String SQL_MORADOR_ITEM_READER_NOVO = "SELECT "
				+ "	totem.ID_TOTEM, totem.ATIVO, totem.SENHA, totem.ID_MORADOR AS ID_PESSOA, "
				+ "	pessoa.NOME, unidade.IDENTIFICACAO, 1 AS TIPO_TOTEM, "
				+ "	CASE (pf.CNH IS NULL) WHEN TRUE THEN 0 ELSE 1 END AS TIPO_CANCELA "
				+ "FROM TOTEM totem  "
				+ "	INNER JOIN MORADOR morador ON totem.ID_MORADOR = morador.ID_MORADOR "
				+ "	INNER JOIN MORADOR_UNIDADE_HABITACIONAL relacionamento ON morador.ID_MORADOR = relacionamento.ID_MORADOR "
				+ "	INNER JOIN UNIDADE_HABITACIONAL unidade ON relacionamento.ID_UNIDADE_HABITACIONAL = unidade.ID_UNIDADE_HABITACIONAL "
				+ "	INNER JOIN PESSOA pessoa ON morador.ID_MORADOR = pessoa.ID_PESSOA "
				+ "	INNER JOIN PESSOA_FISICA pf ON pessoa.ID_PESSOA = pf.ID_PESSOA_FISICA "
				+ "	INNER JOIN CONDOMINIO condominio ON unidade.ID_CONDOMINIO = condominio.ID_CONDOMINIO "
				+ "WHERE 1 = 1 "
				+ "	AND totem.ATIVO = true AND pessoa.STATUS = true "
				+ "	AND (? IS NULL OR condominio.ID_CONDOMINIO = ?) ";
		public static final String SQL_AGREGADO_ITEM_READER_NOVO = "SELECT "
				+ "	totem.ID_TOTEM, totem.ATIVO, totem.SENHA, totem.ID_AGREGADO AS ID_PESSOA, "
				+ "	pessoa.NOME, unidade.IDENTIFICACAO, 2 AS TIPO_TOTEM, "
				+ "	CASE (pf.CNH IS NULL) WHEN TRUE THEN 0 ELSE 1 END AS TIPO_CANCELA "
				+ "FROM TOTEM totem "
				+ "	INNER JOIN AGREGADO agregado ON totem.ID_AGREGADO = agregado.ID_AGREGADO "
				+ "	INNER JOIN MORADOR_AGREGADO relag ON agregado.ID_AGREGADO = relag.ID_AGREGADO "
				+ "	INNER JOIN MORADOR morador ON relag.ID_MORADOR = morador.ID_MORADOR "
				+ "	INNER JOIN MORADOR_UNIDADE_HABITACIONAL relacionamento ON morador.ID_MORADOR = relacionamento.ID_MORADOR "
				+ "	INNER JOIN UNIDADE_HABITACIONAL unidade ON relacionamento.ID_UNIDADE_HABITACIONAL = unidade.ID_UNIDADE_HABITACIONAL "
				+ "	INNER JOIN PESSOA pessoa ON agregado.ID_AGREGADO = pessoa.ID_PESSOA "
				+ "	INNER JOIN PESSOA_FISICA pf ON pessoa.ID_PESSOA = pf.ID_PESSOA_FISICA "
				+ "	INNER JOIN CONDOMINIO condominio ON unidade.ID_CONDOMINIO = condominio.ID_CONDOMINIO "
				+ "WHERE 1 = 1 "
				+ "	AND totem.ATIVO = true AND pessoa.STATUS = true "
				+ "	AND (? IS NULL OR condominio.ID_CONDOMINIO = ?) ";
		public static final String SQL_VISITA_ITEM_READER_NOVO = "SELECT "
				+ "	totem.ID_TOTEM, totem.ATIVO, totem.SENHA, visitante.ID_VISITANTE AS ID_PESSOA, "
				+ "	pessoa.NOME, unidade.IDENTIFICACAO, 3 AS TIPO_TOTEM, "
				+ "	CASE (pf.CNH IS NULL) WHEN TRUE THEN 0 ELSE 1 END AS TIPO_CANCELA, "
				+ "	visita.TIPO_ACESSO, visita.INICIO_AGENDAMENTO_VISITA as DATA_INICIAL, "
				+ "	visita.FIM_AGENDAMENTO_VISITA as DATA_FINAL "
				+ "FROM TOTEM totem "
				+ "	INNER JOIN MORADOR morador ON totem.ID_MORADOR = morador.ID_MORADOR "
				+ "	INNER JOIN MORADOR_UNIDADE_HABITACIONAL relacionamento ON morador.ID_MORADOR = relacionamento.ID_MORADOR "
				+ "	INNER JOIN UNIDADE_HABITACIONAL unidade ON relacionamento.ID_UNIDADE_HABITACIONAL = unidade.ID_UNIDADE_HABITACIONAL "
				+ "	INNER JOIN VISITA visita ON visita.ID_MORADOR = morador.ID_MORADOR "
				+ "	INNER JOIN VISITANTE visitante ON visitante.ID_VISITANTE = visita.ID_VISITANTE "
				+ "	INNER JOIN PESSOA pessoa ON visitante.ID_VISITANTE = pessoa.ID_PESSOA "
				+ "	INNER JOIN PESSOA_FISICA pf ON pessoa.ID_PESSOA = pf.ID_PESSOA_FISICA "
				+ "	INNER JOIN CONDOMINIO condominio ON unidade.ID_CONDOMINIO = condominio.ID_CONDOMINIO "
				+ "WHERE 1 = 1 "
				+ "	AND totem.ATIVO = true AND pessoa.STATUS = true "
				+ "	AND visita.VISITA_CONFIRMADA = FALSE AND visita.VISITA_ATIVA = TRUE "
				+ "	AND (? IS NULL OR condominio.ID_CONDOMINIO = ?) ";
		public static final String SQL_PRESTADOR_ITEM_READER_NOVO = "SELECT "
				+ "	totem.ID_TOTEM, totem.ATIVO, totem.SENHA, prestador.ID_PESSOA AS ID_PESSOA, "
				+ "	pessoa.NOME, unidade.IDENTIFICACAO, 4 AS TIPO_TOTEM,+ "
				+ "	CASE (pf.CNH IS NULL) WHEN TRUE THEN 0 ELSE 1 END AS TIPO_CANCELA, "
				+ "	servicoprestado.TIPO_ACESSO, servicoprestado.INICIO_AGENDAMENTO_SERVICO as DATA_INICIAL, "
				+ "	servicoprestado.FIM_AGENDAMENTO_SERVICO as DATA_FINAL "
				+ "FROM TOTEM totem "
				+ "	INNER JOIN MORADOR morador ON totem.ID_MORADOR = morador.ID_MORADOR "
				+ "	INNER JOIN MORADOR_UNIDADE_HABITACIONAL relacionamento ON morador.ID_MORADOR = relacionamento.ID_MORADOR "
				+ "	INNER JOIN UNIDADE_HABITACIONAL unidade ON relacionamento.ID_UNIDADE_HABITACIONAL = unidade.ID_UNIDADE_HABITACIONAL "
				+ "	INNER JOIN SERVICO_PRESTADO servicoprestado ON servicoprestado.ID_MORADOR = morador.ID_MORADOR "
				+ "	INNER JOIN PRESTADOR_SERVICO prestador ON servicoprestado.ID_PRESTADOR = prestador.ID_PRESTADOR "
				+ "	INNER JOIN PESSOA pessoa ON prestador.ID_PESSOA = pessoa.ID_PESSOA "
				+ "	INNER JOIN PESSOA_FISICA pf ON pessoa.ID_PESSOA = pf.ID_PESSOA_FISICA "
				+ "	INNER JOIN CONDOMINIO condominio ON unidade.ID_CONDOMINIO = condominio.ID_CONDOMINIO "
				+ "WHERE 1 = 1  "
				+ "	AND totem.ATIVO = true AND pessoa.STATUS = true "
				+ "	AND servicoprestado.SERVICO_CONFIRMADO = FALSE AND servicoprestado.SERVICO_ATIVO = TRUE "
				+ "	AND (? IS NULL OR condominio.ID_CONDOMINIO = ?) ";
	}

	/**
	 * Constantes relacionadas à parte de configuração do sistema.
	 * 
	 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
	 * @since 23/10/2014
	 *
	 */
	public interface ConstantesConfiguracao {

		public static final String ENCODE_SISTEMA = "UTF-8";
		public static final String SERVLET_MAPPING = "/";
		public static final String HTML_ESCAPE_KEY = "defaultHtmlEscape";

		public static final String SPRING_PROFILE_KEY = "spring.profiles.active";
		public static final String SPRING_PROFILE_VALUE = "default";

		public static final String CONF_CAMINHO_LOCATION = "/conf_caminho.properties";
		public static final String CONF_MAIL_LOCATION = "/conf_mail.properties";
		public static final String CONF_PARAMETROS_LOCATION = "/conf_parametros.properties";
		public static final String CONF_PERSISTENCE_LOCATION = "/conf_persistence.properties";

	}

	/**
	 * Constantes relacionadas à parte de configuração da view.
	 * 
	 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
	 * @since 23/10/2014
	 *
	 */
	public interface ConstantesView {

		public static final String VIEWS = "/WEB-INF/views/";
		public static final String SUFIXO_PAGINAS = ".xhtml";
		public static final String TEMPLATE_MODE = "HTML5";
		public static final String RESOURCES = "/resources/";
		public static final String RESOURCE_LOCATION = RESOURCES + "**";
		public static final String BUNDLE_LABEL = "messages";
		public static final String FACES_SERVLET = "Faces Servlet";

	}

	/**
	 * Constantes relacionadas à parte de serviços.
	 * 
	 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
	 * @since 23/10/2014
	 *
	 */
	public interface ConstantesService {

		public static final String BUNDLE_PROPERTIES = "servicemessage";

		public static final String COD_ERRO_001 = "COD_ERRO_001";
		public static final String MSG_ERRO_001 = "MSG_ERRO_001";

	}

	public static final String VAZIO = "";
	public static final BigDecimal DATABASE_MAX_DOUBLE = new BigDecimal("1e20");
	public static final String REGEX_DECIMAL = "\\d{1,3}+(\\,\\d{1,2})?";
	public static final int SYSTEM_MAX_DATE_YEAR = 5;
	public static final int SYSTEM_MIN_DATE_YEAR = 5;
	public static final int DESCRICAO_MIN_CARACTERES = 3;
	public static final String HIFEN = "-";
	public static final String NOVA_LINHA = "\n";
	public static final String PONTO_VIRGULA = ";";

}
