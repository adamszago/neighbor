package br.com.lphantus.neighbor.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.resource.ListPreparedStatementSetter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.lphantus.neighbor.service.integracao.batch.TotemRowMapper;
import br.com.lphantus.neighbor.service.integracao.batch.TotemTO;
import br.com.lphantus.neighbor.utils.Constantes.ConstantesJPA;

/**
 * Classe responsável por habilitar o processamento de batch e criar os jobs da
 * aplicacao.
 * 
 * @author elias.policena@lphantus.com.br
 * @since 23/11/2014
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilders;

	@Autowired
	private StepBuilderFactory stepBuilders;

	// ------------------------------------------------------- ITENS BASICOS

	@Bean
	@Qualifier("jobRepository")
	@SuppressWarnings("deprecation")
	public JobRepository jobRepository(PlatformTransactionManager manager)
			throws Exception {
		// TODO: mudar job repository para database (simplejobrepository), em
		// vez de memoria (mapjobrepository)
		return new MapJobRepositoryFactoryBean(manager).getJobRepository();
	}

	@Bean
	@Qualifier("jobLauncher")
	public JobLauncher jobLauncher(PlatformTransactionManager manager)
			throws Exception {
		SimpleJobLauncher retorno = new SimpleJobLauncher();
		retorno.setJobRepository(jobRepository(manager));
		return retorno;
	}

	// ------------------------------------------------------- UTILITARIOS

	@Bean
	@StepScope
	@Qualifier("listPreparedStatementSetter")
	public ListPreparedStatementSetter listPreparedStatementSetter(
			@Value("#{jobParameters['idCondominio']}") String parametro) {
		ListPreparedStatementSetter retorno = new ListPreparedStatementSetter();
		retorno.setParameters(Arrays
				.asList(new String[] { parametro, parametro }));
		return retorno;
	}

	@Bean
	@Qualifier("beanWrapperFieldExtractor")
	public BeanWrapperFieldExtractor<TotemTO> beanWrapperFieldExtractor() {
		BeanWrapperFieldExtractor<TotemTO> retorno = new BeanWrapperFieldExtractor<TotemTO>();
		retorno.setNames(new String[] { "idPessoa", "identificacao", "senha" });
		return retorno;
	}

	@Bean
	@Qualifier("beanWrapperFieldExtractorNovo")
	public BeanWrapperFieldExtractor<TotemTO> beanWrapperFieldExtractorNovo() {
		BeanWrapperFieldExtractor<TotemTO> retorno = new BeanWrapperFieldExtractor<TotemTO>();
		retorno.setNames(new String[] { "idPessoa", "nome", "identificacao",
				"senha", "tipoCancela" });
		return retorno;
	}

	@Bean
	@Qualifier("lineAggregator")
	public DelimitedLineAggregator<TotemTO> lineAggregator() {
		DelimitedLineAggregator<TotemTO> retorno = new DelimitedLineAggregator<TotemTO>();
		retorno.setDelimiter(".");
		retorno.setFieldExtractor(beanWrapperFieldExtractor());
		return retorno;
	}

	@Bean
	@Qualifier("lineAggregatorNovo")
	public DelimitedLineAggregator<TotemTO> lineAggregatorNovo() {
		DelimitedLineAggregator<TotemTO> retorno = new DelimitedLineAggregator<TotemTO>();
		retorno.setDelimiter(";");
		retorno.setFieldExtractor(beanWrapperFieldExtractorNovo());
		return retorno;
	}

	// ------------------------------------------------------- EXTRATOR ANTIGO
	@Bean
	@StepScope
	@Qualifier("moradorItemReader")
	public JdbcCursorItemReader<TotemTO> moradorItemReader(
			@Qualifier("listPreparedStatementSetter") ListPreparedStatementSetter setter,
			@Qualifier("dataSource") DataSource dataSource) {
		JdbcCursorItemReader<TotemTO> retorno = new JdbcCursorItemReader<TotemTO>();
		retorno.setDataSource(dataSource);
		retorno.setSql(ConstantesJPA.SQL_MORADOR_ITEM_READER_ANTIGO);
		retorno.setRowMapper(new TotemRowMapper());
		retorno.setPreparedStatementSetter(setter);
		return retorno;
	}

	@Bean
	@StepScope
	@Qualifier("fileItemWriterMor")
	public FlatFileItemWriter<TotemTO> fileItemWriterMor(
			@Value("#{jobParameters['arquivoMorador']}") String fileName) {
		FlatFileItemWriter<TotemTO> retorno = new FlatFileItemWriter<TotemTO>();
		FileSystemResource resource = new FileSystemResource(fileName);
		retorno.setResource(resource);
		retorno.setLineAggregator(lineAggregator());
		retorno.setShouldDeleteIfExists(false);
		return retorno;
	}

	@Bean
	@Qualifier("step1")
	public Step step1(
			@Qualifier("moradorItemReader") ItemReader<TotemTO> itemReader,
			@Qualifier("fileItemWriterMor") ItemWriter<TotemTO> fileWriter) {
		return stepBuilders.get("step1")
				.<TotemTO, TotemTO> chunk(ConstantesJPA.CHUNK_SIZE_JOBS)
				.reader(itemReader).writer(fileWriter).build();
	}

	@Bean
	@StepScope
	@Qualifier("agregadoItemReader")
	public JdbcCursorItemReader<TotemTO> agregadoItemReader(
			@Qualifier("listPreparedStatementSetter") ListPreparedStatementSetter setter,
			@Qualifier("dataSource") DataSource dataSource) {
		JdbcCursorItemReader<TotemTO> retorno = new JdbcCursorItemReader<TotemTO>();
		retorno.setDataSource(dataSource);
		retorno.setSql(ConstantesJPA.SQL_AGREGADO_ITEM_READER_ANTIGO);
		retorno.setRowMapper(new TotemRowMapper());
		retorno.setPreparedStatementSetter(setter);
		return retorno;
	}

	@Bean
	@StepScope
	@Qualifier("fileItemWriterAgr")
	public FlatFileItemWriter<TotemTO> fileItemWriterAgr(
			@Value("#{jobParameters['arquivoAgregado']}") String fileName) {
		FlatFileItemWriter<TotemTO> retorno = new FlatFileItemWriter<TotemTO>();
		FileSystemResource resource = new FileSystemResource(fileName);
		retorno.setResource(resource);
		retorno.setLineAggregator(lineAggregator());
		retorno.setShouldDeleteIfExists(false);
		return retorno;
	}

	@Bean
	@Qualifier("step2")
	public Step step2(
			@Qualifier("agregadoItemReader") ItemReader<TotemTO> itemReader,
			@Qualifier("fileItemWriterAgr") ItemWriter<TotemTO> fileWriter) {
		return stepBuilders.get("step2")
				.<TotemTO, TotemTO> chunk(ConstantesJPA.CHUNK_SIZE_JOBS)
				.reader(itemReader).writer(fileWriter).build();
	}

	@Bean
	@Qualifier("totemJob")
	public Job totemJob(@Qualifier("step1") Step step1,
			@Qualifier("step2") Step step2) {
		return jobBuilders.get("totemJob").start(step1).next(step2).build();
	}

	// -------------------------------------------------------

	@Bean
	@StepScope
	@Qualifier("moradorNovoItemReader")
	public JdbcCursorItemReader<TotemTO> moradorNovoItemReader(
			@Qualifier("listPreparedStatementSetter") ListPreparedStatementSetter setter,
			@Qualifier("dataSource") DataSource dataSource) {
		JdbcCursorItemReader<TotemTO> retorno = new JdbcCursorItemReader<TotemTO>();
		retorno.setDataSource(dataSource);
		retorno.setSql(ConstantesJPA.SQL_MORADOR_ITEM_READER_NOVO);
		retorno.setRowMapper(new TotemRowMapper());
		retorno.setPreparedStatementSetter(setter);
		return retorno;
	}

	@Bean
	@StepScope
	@Qualifier("fileItemWriterMorador")
	public FlatFileItemWriter<TotemTO> fileItemWriterMorador(
			@Value("#{jobParameters['arquivoMorador']}") String fileName) {
		FlatFileItemWriter<TotemTO> retorno = new FlatFileItemWriter<TotemTO>();
		FileSystemResource resource = new FileSystemResource(fileName);
		retorno.setResource(resource);
		retorno.setShouldDeleteIfExists(true);
		retorno.setLineAggregator(lineAggregatorNovo());
		return retorno;
	}

	@Bean
	@Qualifier("step1Novo")
	public Step step1Novo(
			@Qualifier("moradorNovoItemReader") ItemReader<TotemTO> itemReader,
			@Qualifier("fileItemWriterMorador") ItemWriter<TotemTO> itemWriter) {
		return stepBuilders.get("step1Novo")
				.<TotemTO, TotemTO> chunk(ConstantesJPA.CHUNK_SIZE_JOBS)
				.reader(itemReader).writer(itemWriter).build();
	}

	@Bean
	@StepScope
	@Qualifier("agregadoNovoItemReader")
	public JdbcCursorItemReader<TotemTO> agregadoNovoItemReader(
			@Qualifier("listPreparedStatementSetter") ListPreparedStatementSetter setter,
			@Qualifier("dataSource") DataSource dataSource) {
		JdbcCursorItemReader<TotemTO> retorno = new JdbcCursorItemReader<TotemTO>();
		retorno.setDataSource(dataSource);
		retorno.setSql(ConstantesJPA.SQL_AGREGADO_ITEM_READER_NOVO);
		retorno.setRowMapper(new TotemRowMapper());
		retorno.setPreparedStatementSetter(setter);
		return retorno;
	}

	@Bean
	@StepScope
	@Qualifier("fileItemWriterAgregado")
	public FlatFileItemWriter<TotemTO> fileItemWriterAgregado(
			@Value("#{jobParameters['arquivoAgregado']}") String fileName) {
		FlatFileItemWriter<TotemTO> retorno = new FlatFileItemWriter<TotemTO>();
		FileSystemResource resource = new FileSystemResource(fileName);
		retorno.setResource(resource);
		retorno.setShouldDeleteIfExists(true);
		retorno.setLineAggregator(lineAggregatorNovo());
		return retorno;
	}

	@Bean
	@Qualifier("step2Novo")
	public Step step2Novo(
			@Qualifier("agregadoNovoItemReader") ItemReader<TotemTO> itemReader,
			@Qualifier("fileItemWriterAgregado") ItemWriter<TotemTO> itemWriter) {
		return stepBuilders.get("step2Novo")
				.<TotemTO, TotemTO> chunk(ConstantesJPA.CHUNK_SIZE_JOBS)
				.reader(itemReader).writer(itemWriter).build();
	}

	@Bean
	@StepScope
	@Qualifier("visitaItemReader")
	public JdbcCursorItemReader<TotemTO> visitaItemReader(
			@Qualifier("listPreparedStatementSetter") ListPreparedStatementSetter setter,
			@Qualifier("dataSource") DataSource dataSource) {
		JdbcCursorItemReader<TotemTO> retorno = new JdbcCursorItemReader<TotemTO>();
		retorno.setDataSource(dataSource);
		retorno.setSql(ConstantesJPA.SQL_VISITA_ITEM_READER_NOVO);
		retorno.setRowMapper(new TotemRowMapper());
		retorno.setPreparedStatementSetter(setter);
		return retorno;
	}

	@Bean
	@StepScope
	@Qualifier("fileItemWriterVisita")
	public FlatFileItemWriter<TotemTO> fileItemWriterVisita(
			@Value("#{jobParameters['arquivoVisitante']}") String fileName) {
		FlatFileItemWriter<TotemTO> retorno = new FlatFileItemWriter<TotemTO>();
		FileSystemResource resource = new FileSystemResource(fileName);
		retorno.setResource(resource);
		retorno.setShouldDeleteIfExists(true);
		retorno.setLineAggregator(lineAggregatorNovo());
		return retorno;
	}

	@Bean
	@Qualifier("step3Novo")
	public Step step3Novo(
			@Qualifier("visitaItemReader") ItemReader<TotemTO> itemReader,
			@Qualifier("fileItemWriterVisita") ItemWriter<TotemTO> itemWriter) {
		return stepBuilders.get("step3Novo")
				.<TotemTO, TotemTO> chunk(ConstantesJPA.CHUNK_SIZE_JOBS)
				.reader(itemReader).writer(itemWriter).build();
	}

	@Bean
	@StepScope
	@Qualifier("prestadorItemReader")
	public JdbcCursorItemReader<TotemTO> prestadorItemReader(
			@Qualifier("listPreparedStatementSetter") ListPreparedStatementSetter setter,
			@Qualifier("dataSource") DataSource dataSource) {
		JdbcCursorItemReader<TotemTO> retorno = new JdbcCursorItemReader<TotemTO>();
		retorno.setDataSource(dataSource);
		retorno.setSql(ConstantesJPA.SQL_PRESTADOR_ITEM_READER_NOVO);
		retorno.setRowMapper(new TotemRowMapper());
		retorno.setPreparedStatementSetter(setter);
		return retorno;
	}

	@Bean
	@StepScope
	@Qualifier("fileItemWriterPrestador")
	public FlatFileItemWriter<TotemTO> fileItemWriterPrestador(
			@Value("#{jobParameters['arquivoPrestador']}") String fileName) {
		FlatFileItemWriter<TotemTO> retorno = new FlatFileItemWriter<TotemTO>();
		FileSystemResource resource = new FileSystemResource(fileName);
		retorno.setResource(resource);
		retorno.setShouldDeleteIfExists(true);
		retorno.setLineAggregator(lineAggregatorNovo());
		return retorno;
	}

	@Bean
	@Qualifier("step4Novo")
	public Step step4Novo(
			@Qualifier("prestadorItemReader") ItemReader<TotemTO> itemReader,
			@Qualifier("fileItemWriterPrestador") ItemWriter<TotemTO> itemWriter) {
		return stepBuilders.get("step4Novo")
				.<TotemTO, TotemTO> chunk(ConstantesJPA.CHUNK_SIZE_JOBS)
				.reader(itemReader).writer(itemWriter).build();
	}

	@Bean
	@Qualifier("novoTotemJob")
	public Job novoTotemJob(@Qualifier("step1Novo") Step step1,
			@Qualifier("step2Novo") Step step2,
			@Qualifier("step3Novo") Step step3,
			@Qualifier("step4Novo") Step step4) {
		return jobBuilders.get("novoTotemJob").start(step1).next(step2)
				.next(step3).next(step4).build();
	}

}
