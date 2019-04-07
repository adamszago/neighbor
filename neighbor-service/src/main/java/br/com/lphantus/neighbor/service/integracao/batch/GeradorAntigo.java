package br.com.lphantus.neighbor.service.integracao.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.TotemDTO;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.FtpUtils;

@Service
@Scope(value = "prototype")
@Transactional(propagation = Propagation.SUPPORTS)
public class GeradorAntigo extends AbstractGerador {

	@Value("${neighbor.param.ftp.mor}")
	private String ftpFileMor;

	@Value("${neighbor.param.ftp.agr}")
	private String ftpFileAgr;

	@Value("${neighbor.param.ftp.file}")
	private String ftpFile;

	@Value("${neighbor.param.ftp.dest}")
	private String ftpFileDest;

	@Value("${neighbor.param.ftp.host}")
	private String ftpHost;

	@Value("${neighbor.param.ftp.port}")
	private Integer ftpPort;

	@Value("${neighbor.param.ftp.user}")
	private String ftpUser;

	@Value("${neighbor.param.ftp.passwd}")
	private String ftpPasswd;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private IMoradorService moradorService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public CondominioDTO obtemCondominio() throws ServiceException {
		TotemDTO totem = getTotem();
		// --------------------------------------------------------------
		// ARQUIVO
		CondominioDTO condominio = null;
		MoradorDTO morador;
		if (null != totem.getMorador()) {
			morador = totem.getMorador();
		} else if (null != totem.getAgregado()) {
			morador = totem.getAgregado().getMorador().get(0).getMorador();
		} else {
			morador = null;
		}
		if (null != morador) {
			if (morador.getUnidadeHabitacional().size() > 0) {
				condominio = morador.getUnidadeHabitacional().get(0)
						.getUnidadeHabitacional().getCondominio();
			} else {
				try {
					morador = moradorService.buscarDetalhesMorador(morador);
					condominio = morador.getUnidadeHabitacional().get(0)
							.getUnidadeHabitacional().getCondominio();
				} catch (ServiceException e) {
					throw e;
				}
			}
		}
		return condominio;
	}

	@Override
	public void geraArquivoTotem(final CondominioDTO condominio)
			throws IOException, ServiceException {

		final String caminhoMorador = ftpFileMor.replaceAll("file:", "");
		final String caminhoAgregado = ftpFileAgr.replaceAll("file:", "");
		final File arquivoMorador = new File(caminhoMorador);
		if (arquivoMorador.exists()) {
			arquivoMorador.delete();
		}
		final File arquivoAgregado = new File(caminhoAgregado);
		if (arquivoAgregado.exists()) {
			arquivoAgregado.delete();
		}

		// --------------------------------------------------------------
		final JobLauncher jobLauncher = (JobLauncher) this.context
				.getBean("jobLauncher");
		final Job job = (Job) this.context.getBean("totemJob");

		try {
			final JobParameters param = new JobParametersBuilder()
					.addString("arquivoMorador", ftpFileMor)
					.addString("arquivoAgregado", ftpFileAgr)
					.addLong("idCondominio",
							condominio.getPessoa().getIdPessoa())
					.addLong("horaExecucao", System.currentTimeMillis())
					.toJobParameters();
			final JobExecution execution = jobLauncher.run(job, param);
			if (execution.getStatus().equals(BatchStatus.COMPLETED)) {

				final String fileName = ftpFile;
				final File arquivo = new File(fileName);
				if (arquivo.exists()) {
					arquivo.delete();
				}

				FileOutputStream fosDest = new FileOutputStream(arquivo);
				final FileChannel dest = fosDest.getChannel();

				FileInputStream fosSrcMor = new FileInputStream(caminhoMorador);
				final FileChannel srcMor = fosSrcMor.getChannel();

				FileInputStream fosSrcArg = new FileInputStream(caminhoAgregado);
				final FileChannel srcArg = fosSrcArg.getChannel();

				srcMor.transferTo(0, srcMor.size(), dest);
				srcArg.transferTo(0, srcArg.size(), dest);

				fosDest.close();
				fosSrcMor.close();
				fosSrcArg.close();
			}else{
				logger.error("Job falhou com codigo: " + execution.getStatus());
				for (Throwable exception : execution.getAllFailureExceptions()) {
					logger.error("Erro lancado.", exception);
				}
			}

		} catch (final Exception e) {
			throw new ServiceException("Erro ao enviar arquivo de senhas.", e);
		}
		// --------------------------------------------------------------
	}

	@Override
	public void enviarArquivoTotemFTP() throws ServiceException {
		try {

			final String fileName = ftpFile;
			final String targetFileName = ftpFileDest;

			FtpUtils.send(ftpHost, ftpPort, ftpUser, ftpPasswd, fileName,
					targetFileName);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

}
