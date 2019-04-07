package br.com.lphantus.neighbor.service.integracao.mail;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.lphantus.neighbor.utils.DateUtil;

public class EnviarEmailThread implements Runnable {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Map<EmailTO, Date> mapaEmailTempo = new HashMap<EmailTO, Date>();
	
	public EnviarEmailThread() {
		
	}

	@Override
	public void run() {
		while(true){
			try {
				final Date corrente = new Date();
				Iterator<Entry<EmailTO, Date>> iterator = mapaEmailTempo.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<EmailTO, Date> registro = iterator.next();
					enviarEmail(registro.getKey());
					if ( DateUtil.add(registro.getValue(), 5, Calendar.MINUTE).compareTo(corrente) > 0 ){
						iterator.remove();
					}
				}
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				logger.error("Erro ao executar envio de e-mails.", e);
			}
		}
	}
	
	private void enviarEmail(EmailTO emailEnviar) {
		try {
			if ( !emailEnviar.isEnviado() ){
				// Create the email message
				final HtmlEmail email = new HtmlEmail();
				email.setDebug(false);
				email.setHostName(emailEnviar.getHost());
				email.setSSL(true);
				email.setSmtpPort(emailEnviar.getPort());
				email.setAuthentication(emailEnviar.getUsuario(), emailEnviar.getSenha());
	
				email.addTo(emailEnviar.getDestinatario());
				email.setFrom(emailEnviar.getRemetente(), emailEnviar.getRemetenteNome());
				email.setSubject(emailEnviar.getAssunto());
				email.setHtmlMsg(emailEnviar.getTextoCorpo());
				// set the alternative message
				// email.setTextMsg("Your email client does not support HTML messages");
	
				email.send();
				
				emailEnviar.setEnviado(true);
			}

		} catch (final Exception e) {
			logger.error("\n Erro ao enviar email. \n" + " Erro:" + e.getMessage() 
					+ " \n" + " Destinatario: " + emailEnviar.getDestinatario() + " \n" 
					+ " Assunto: " + emailEnviar.getAssunto() + " \n" 
					+ " Mensagem: \n " + "\t " + emailEnviar.getTextoCorpo() + " \n\n", e);
		}
	}
	
	public void adicionaEmailFila(final EmailTO emailEnviar){
		if ( !mapaEmailTempo.containsKey(emailEnviar) ){
			mapaEmailTempo.put(emailEnviar, new Date());
		}
	}

}
