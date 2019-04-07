package br.com.lphantus.neighbor.service.integracao.mail;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ReservaDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Totem;
import br.com.lphantus.neighbor.service.IMailManager;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.utils.Utilitarios;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MailManager implements IMailManager {

	@Value("${neighbor.mail.smtp.host}")
	private String host;

	@Value("${neighbor.mail.smtp.port}")
	private Integer port;

	@Value("${neighbor.mail.emailsuporte}")
	private String emailSuporte;

	@Value("${neighbor.mail.emailcontato}")
	private String emailContato;

	@Value("${neighbor.mail.senhasuporte}")
	private String senhaSuporte;

	@Value("${neighbor.mail.suportenome}")
	private String suporteNome;

	@Value("${neighbor.mail.assuntousuario}")
	private String assuntoUsuario;

	@Value("${neighbor.mail.assuntototem}")
	private String assuntoTotem;

	@Value("${neighbor.mail.assuntorecuperacao}")
	private String assuntoRecuperacao;

	@Value("${neighbor.mail.assuntomensagem}")
	private String assuntoMensagem;

	@Value("${neighbor.mail.assuntoerro}")
	private String assuntoErro;

	@Value("${neighbor.mail.assuntoReservaAprovada}")
	private String assuntoReservaAprovada;

	@Autowired
	private IUsuarioService usuarioService;
	
	private EnviarEmailThread executavel = new EnviarEmailThread();
	private Thread threadEmail;
	
	@PostConstruct
	public void initialize(){
		threadEmail = new Thread(executavel);
		threadEmail.start();
	}

	public void enviarEmailUsuarioTotem(final String textoCorpo, final String destinatario) {
		final EmailTO enviarEmailUsuarioTotem = new EmailTO(assuntoTotem, textoCorpo, destinatario, 
				emailSuporte, suporteNome, emailSuporte, senhaSuporte, host, port);
		executavel.adicionaEmailFila(enviarEmailUsuarioTotem);
	}

	public void enviarEmailUsuarioCadastrado(final String textoCorpo, final String destinatario) {
		final EmailTO enviarEmailUsuarioCadastrado = new EmailTO(assuntoUsuario, textoCorpo, destinatario, 
				emailSuporte, suporteNome, emailSuporte, senhaSuporte, host, port);
		executavel.adicionaEmailFila(enviarEmailUsuarioCadastrado);
	}

	public void enviarEmailRecuperacao(final UsuarioDTO usuario, final String novaSenha) {

		final String textoCorpo = TextoMensagemMail.gerarTextoRecuperacaoUsuarioSenha(usuario, novaSenha);
		final String destinatario = usuario.getPessoa().getMail();

		final EmailTO enviarEmailRecuperacao = new EmailTO(assuntoRecuperacao, textoCorpo, destinatario, 
				emailSuporte, suporteNome, emailSuporte, senhaSuporte, host, port);
		executavel.adicionaEmailFila(enviarEmailRecuperacao);
	}

	public void enviarEmailAvisoMensagemRecebida(final MoradorDTO morador) {
		final String textoCorpo = TextoMensagemMail.gerarTextoAvisoMensagemRecebida(morador);
		final String destinatario = morador.getPessoa().getMail();

		final EmailTO enviarEmailAvisoMensagemRecebida = new EmailTO(assuntoMensagem, textoCorpo, destinatario, 
				emailSuporte, suporteNome, emailSuporte, senhaSuporte, host, port);
		executavel.adicionaEmailFila(enviarEmailAvisoMensagemRecebida);
	}

	public void enviarEmailAvisoMensagemRecebida(final UsuarioDTO usuario) {
		final String textoCorpo = TextoMensagemMail.gerarTextoAvisoMensagemRecebida(usuario);
		final String destinatario = usuario.getPessoa().getMail();

		final EmailTO enviarEmailAvisoMensagemRecebida = new EmailTO(assuntoMensagem, textoCorpo, destinatario, 
				emailSuporte, suporteNome, emailSuporte, senhaSuporte, host, port);
		executavel.adicionaEmailFila(enviarEmailAvisoMensagemRecebida);
	}

	@Override
	public void enviarEmailErroProcessamentoArquivo(Exception e) {
		String textoCorpo = TextoMensagemMail.gerarTextoErroProcessamentoTotem(Utilitarios.obterStackTrace(e));

		final EmailTO enviarEmailErroProcessamentoArquivo = new EmailTO(assuntoErro, textoCorpo, emailContato, 
				emailSuporte, suporteNome, emailSuporte, senhaSuporte, host, port);
		executavel.adicionaEmailFila(enviarEmailErroProcessamentoArquivo);
	}

	@Override
	public void enviarEmailErroProcessamentoRotina(String tipoRotina, Exception e) {
		String textoCorpo = TextoMensagemMail.gerarTextoErroProcessamentoRotina(tipoRotina, Utilitarios.obterStackTrace(e));

		final EmailTO enviarEmailErroProcessamentoRotina = new EmailTO(assuntoErro, textoCorpo, emailContato, 
				emailSuporte, suporteNome, emailSuporte, senhaSuporte, host, port);
		executavel.adicionaEmailFila(enviarEmailErroProcessamentoRotina);
	}

	@Override
	public void enviarEmailTotemDuplicado(List<Totem> duplicados, int tipo) {
		String textoCorpo = TextoMensagemMail.gerarTextoTotemDuplicado(duplicados, tipo);

		final EmailTO enviarEmailTotemDuplicado = new EmailTO(assuntoErro, textoCorpo, emailContato, 
				emailSuporte, suporteNome, emailSuporte, senhaSuporte, host, port);
		executavel.adicionaEmailFila(enviarEmailTotemDuplicado);
	}

	@Override
	public void enviarEmailReservaAprovada(ReservaDTO reservaPedida, UnidadeHabitacionalDTO unidadePedida) {
		try {
			UsuarioDTO sindico = usuarioService.buscarSindico(reservaPedida.getCondominio());
			String textoCorpo = TextoMensagemMail.gerarTextoReservaAprovada(reservaPedida, unidadePedida);

			final EmailTO enviarEmailReservaAprovada = new EmailTO(assuntoReservaAprovada, textoCorpo, 
					sindico.getPessoa().getMail(), emailSuporte, suporteNome, emailSuporte, senhaSuporte, host, port);
			executavel.adicionaEmailFila(enviarEmailReservaAprovada);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void enviarEmailErroOperacional(String stackTrace) {
		String textoCorpo = TextoMensagemMail.gerarTextoErroOperacional(stackTrace);
		final EmailTO enviarEmailErroOperacional = new EmailTO(assuntoErro, textoCorpo, "eliaspoliceno@gmail.com", 
				emailSuporte, suporteNome, emailSuporte, senhaSuporte, host, port);
		executavel.adicionaEmailFila(enviarEmailErroOperacional);
	}

}
