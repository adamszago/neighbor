package br.com.lphantus.neighbor.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;

/**
 * Classe responsável por traduzir as entidades em um script ddl.
 * 
 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
 * @since 14/10/2014
 *
 */
public class SchemaTranslator {

	private Configuration config = null;

	/**
	 * Construtor
	 */
	public SchemaTranslator() {
		this.config = new Configuration();
	}

	/**
	 * Configura o dialeto
	 * 
	 * @param dialect
	 *            O dialeto
	 * @return Uma instancia desta classe.
	 */
	public SchemaTranslator setDialect(final String dialect) {
		this.config.setProperty(AvailableSettings.DIALECT, dialect);
		return this;
	}

	/**
	 * Method determines classes which will be used for DDL generation.
	 * 
	 * @param annotatedClasses
	 *            - entities annotated with Hibernate annotations.
	 */
	public SchemaTranslator addAnnotatedClasses(
			@SuppressWarnings("rawtypes") final Class[] annotatedClasses) {
		for (@SuppressWarnings("rawtypes")
		final Class clazz : annotatedClasses) {
			this.config.addAnnotatedClass(clazz);
		}
		return this;
	}

	/**
	 * Method performs translation of entities in table schemas. It generates
	 * 'CREATE' and 'DELETE' scripts for the Hibernate entities. Current
	 * implementation involves usage of
	 * {@link #write(FileOutputStream, String[], Formatter)} method.
	 * 
	 * @param outputStream
	 *            - stream will be used for *.sql file creation.
	 * @throws IOException
	 */
	public SchemaTranslator translate(final FileOutputStream outputStream)
			throws IOException {
		final Dialect requiredDialect = Dialect.getDialect(this.config
				.getProperties());
		String[] query = null;

		query = this.config.generateDropSchemaScript(requiredDialect);
		write(outputStream, query, FormatStyle.DDL.getFormatter());

		query = this.config.generateSchemaCreationScript(requiredDialect);
		write(outputStream, query, FormatStyle.DDL.getFormatter());

		return this;
	}

	/**
	 * Method writes line by line DDL scripts in the output stream. Also each
	 * line logs in the console.
	 * 
	 * @throws IOException
	 */
	private void write(final FileOutputStream outputStream,
			final String[] lines, final Formatter formatter) throws IOException {
		String tempStr = null;

		for (final String line : lines) {
			tempStr = formatter.format(line) + ";";
			// out println
			outputStream.write(tempStr.getBytes());
		}
	}

	/**
	 * Método main para gerar o script ddl.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		final SchemaTranslator translator = new SchemaTranslator();
		// TODO: colocar classes da aplicação
		@SuppressWarnings("rawtypes")
		final Class[] entityClasses = {};

		translator.setDialect("org.hibernate.dialect.MySQL5Dialect")
				.addAnnotatedClasses(entityClasses)
				.translate(new FileOutputStream(new File("db-schema.sql")));

	}

}