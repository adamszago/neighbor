package br.com.lphantus.neighbor.service.integracao.batch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ftp.FTPClient;
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
import br.com.lphantus.neighbor.common.TotemDTO;
import br.com.lphantus.neighbor.service.IMailManager;
import br.com.lphantus.neighbor.service.ITotemService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.FtpUtils;
import br.com.lphantus.neighbor.utils.Constantes;

@Service
@Scope(value = "prototype")
@Transactional(propagation = Propagation.SUPPORTS)
public class GeradorNovo extends AbstractGerador {

	@Value("${neighbor.param.ftp.mor}")
	private String ftpFileMor;

	@Value("${neighbor.param.ftp.agr}")
	private String ftpFileAgr;

	@Value("${neighbor.param.ftp.vis}")
	private String ftpFileVis;

	@Value("${neighbor.param.ftp.pre}")
	private String ftpFilePre;

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
	
	@Value("${neighbor.param.ftp.retorno}")
	private String ftpRetorno;

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private ITotemService totemService;
	
	@Autowired
	private IMailManager manager;

	@Override
	public CondominioDTO obtemCondominio() {
		TotemDTO totem = getTotem();
		// --------------------------------------------------------------
		// ARQUIVO
		CondominioDTO condominio;
		if (null != totem.getMorador()) {
			condominio = totem.getMorador().getUnidadeHabitacional().get(0)
					.getUnidadeHabitacional().getCondominio();
		} else if (null != totem.getAgregado()) {
			condominio = totem.getAgregado().getMorador().get(0).getMorador()
					.getUnidadeHabitacional().get(0).getUnidadeHabitacional()
					.getCondominio();
		} else if (null != totem.getPrestador()) {
			condominio = totem.getPrestador().getCondominio();
		} else if (null != totem.getVisitante()) {
			condominio = totem.getVisitante().getCondominio();
		} else {
			condominio = null;
		}
		return condominio;
	}

	@Override
	public void geraArquivoTotem(CondominioDTO condominio) throws IOException,
			ServiceException {
		final String caminhoMorador = ftpFileMor.replaceAll("file:", "");
		final String caminhoAgregado = ftpFileAgr.replaceAll("file:", "");
		final String caminhoVisitante = ftpFileVis.replaceAll("file:", "");
		final String caminhoPrestador = ftpFilePre.replaceAll("file:", "");
		final File arquivoMorador = new File(caminhoMorador);
		if (arquivoMorador.exists()) {
			arquivoMorador.delete();
		}
		final File arquivoAgregado = new File(caminhoAgregado);
		if (arquivoAgregado.exists()) {
			arquivoAgregado.delete();
		}
		final File arquivoVisitante = new File(caminhoVisitante);
		if (arquivoVisitante.exists()) {
			arquivoVisitante.delete();
		}
		final File arquivoPrestador = new File(caminhoPrestador);
		if (arquivoPrestador.exists()) {
			arquivoPrestador.delete();
		}

		// --------------------------------------------------------------
		final JobLauncher jobLauncher = (JobLauncher) this.context
				.getBean("jobLauncher");
		final Job job = (Job) this.context.getBean("novoTotemJob");

		try {
			final JobParameters param = new JobParametersBuilder()
					.addString("arquivoMorador", ftpFileMor)
					.addString("arquivoAgregado", ftpFileAgr)
					.addString("arquivoVisitante", ftpFileVis)
					.addString("arquivoPrestador", ftpFilePre)
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

				FileInputStream fosSrcAgr = new FileInputStream(caminhoAgregado);
				final FileChannel srcAgr = fosSrcAgr.getChannel();

				FileInputStream fosSrcVis = new FileInputStream(
						caminhoVisitante);
				final FileChannel srcVis = fosSrcVis.getChannel();

				FileInputStream fosSrcPre = new FileInputStream(
						caminhoPrestador);
				final FileChannel srcPre = fosSrcPre.getChannel();

				srcMor.transferTo(0, srcMor.size(), dest);
				srcAgr.transferTo(0, srcAgr.size(), dest);
				srcAgr.transferTo(0, srcVis.size(), dest);
				srcAgr.transferTo(0, srcPre.size(), dest);

				fosDest.close();
				fosSrcMor.close();
				fosSrcAgr.close();
				fosSrcVis.close();
				fosSrcPre.close();
			}

		} catch (final Exception e) {
			throw new ServiceException("Erro ao enviar arquivo de senhas.", e);
		}
		// --------------------------------------------------------------
	}

	@Override
	public void enviarArquivoTotemFTP() throws IOException, ServiceException {
		final String fileName = ftpFile;
		final String targetFileName = ftpFileDest;

		FtpUtils.send(ftpHost, ftpPort, ftpUser, ftpPasswd, fileName,
				targetFileName);

		// le o arquivo de retorno
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				// espera por 5 minutos para ler o retorno
				try {
					TimeUnit.MINUTES.sleep(5);
				} catch (InterruptedException e) {
				}
				
				try {
					// conecta ao servidor
					FTPClient client = new FTPClient();
					client.connect(ftpHost, ftpPort);
					client.login(ftpUser, ftpPasswd);

					// tenta fazer o download do arquivo para a memoria do servidor
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					if (client.retrieveFile(ftpRetorno, stream)) {
						
						// le o conteudo do arquivo
						String conteudo = new String(stream.toByteArray(),
								java.nio.charset.StandardCharsets.US_ASCII);
						
						List<String[]> listaDadosArquivo = new ArrayList<String[]>();
						
						// processa o arquivo
						String[] dados = conteudo.split(Constantes.NOVA_LINHA);
						for(String registro:dados){
							String[] dadosRegistro = registro.split(Constantes.PONTO_VIRGULA);
							listaDadosArquivo.add(dadosRegistro);
						}
						
						// atualiza a nossa base
						totemService.atualizaVisitasPrestacoesServico(listaDadosArquivo);
						
						// remove o arquivo
						client.deleteFile(ftpRetorno);
					}
				} catch (IOException | ServiceException e) {
					manager.enviarEmailErroProcessamentoArquivo(e);
				}
			}
		}).start();
	}

}
