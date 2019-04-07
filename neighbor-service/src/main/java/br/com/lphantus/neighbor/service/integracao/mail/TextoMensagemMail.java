package br.com.lphantus.neighbor.service.integracao.mail;

import java.util.List;

import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ReservaDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Totem;
import br.com.lphantus.neighbor.utils.Constantes;

public class TextoMensagemMail {

	public static String gerarTextoUsuarioCadastrado(final MoradorDTO morador, final String login, final String senha) {

		String tratamento;
		if (morador.getPessoa().getSexo() == null) {
			tratamento = "Sr(a)";
		} else if (morador.getPessoa().getSexo().equals("F")) {
			tratamento = "Sr.ª";
		} else {
			tratamento = "Sr.";
		}

		final String textoCorpo = tratamento + " " + morador.getPessoa().getNome() + "<br/><br/>" +

		"Efetuamos o cadastro do seu usuário para acesso ao sistema Neighbor, " + "segue abaixo os dados de acesso: <br/><br/>" +

		"Endereço de Acesso: http://www.lpncondominio.com.br <br/>" + "Usuário: <strong>" + login + "</strong><br/>" + "Senha: <strong>" + senha + "</strong>" + "<br/><br/>" +

		"Para treinamento, dúvidas ou solicitações fique a vontade para nos contatar. <br/>" + "http://www.lphantus.com.br" + "<br/><br/>" +

		"Att,<br/>" + "<img src=\"" + getAssinaturaSuporte() + "\"> <br/> ";

		return textoCorpo;
	}

	public static String informarSenhaTotem(final String nomeUsuario, final String novaSenha) {
		final String textoCorpo = "Sr(a) " + nomeUsuario + "<br/><br/>" +

		"Segue abaixo a sua senha para o sistema de TOTEM, mantenha a mesma em segurança pois é a sua garantia de entrada no condomínio." + " <br/><br/>" +

		"Senha: <strong>" + novaSenha + "</strong>" + "<br/><br/>" +

		"Para treinamento, dúvidas ou solicitações fique a vontade para nos contatar. <br/>" + "http://www.lphantus.com.br" + "<br/><br/>" +

		"Att,<br/>" + "<img src=\"" + getAssinaturaSuporte() + "\"> <br/> ";

		return textoCorpo;
	}

	public static String gerarTextoRecuperacaoUsuarioSenha(final UsuarioDTO usuario, final String novaSenha) {
		final String textoCorpo = "Sr(a) " + usuario.getPessoa().getNome() + "<br/><br/>" +

		"Segue abaixo os seus dados de acesso ao sistema Neighbor, " + " <br/><br/>" +

		"Endereço de Acesso: http://www.lpncondominio.com.br <br/>" + "Usuario: <strong>" + usuario.getLogin() + "</strong><br/>" + "Senha: <strong>" + novaSenha + "</strong>"
				+ "<br/><br/>" +

				"Para treinamento, dúvidas ou solicitações fique a vontade para nos contatar. <br/>" + "http://www.lphantus.com.br" + "<br/><br/>" +

				"Att,<br/>" + "<img src=\"" + getAssinaturaSuporte() + "\"> <br/> ";

		return textoCorpo;
	}

	public static String gerarTextoAvisoMensagemRecebida(final MoradorDTO morador) {
		final String textoCorpo = "Sr(a) " + morador.getPessoa().getNome() + "<br/><br/>" +

		"Este aviso é para lembrá-lo(a) que acabou de receber uma mensagem. Acesse o sistema para vê-la. " + " <br/><br/>" +

		"Endereço de Acesso: http://www.lpncondominio.com.br <br/>" + "Morador: <strong>" + morador.getPessoa().getNome() + "</strong><br/>" + "<br/><br/>" +

		"Para treinamento, dúvidas ou solicitações fique a vontade para nos contatar. <br/>" + "http://www.lphantus.com.br" + "<br/><br/>" +

		"Att,<br/>" + "<img src=\"" + getAssinaturaSuporte() + "\"> <br/> ";

		return textoCorpo;
	}

	public static String gerarTextoAvisoMensagemRecebida(final UsuarioDTO usuario) {
		final String textoCorpo = "Sr(a) " + usuario.getPessoa().getNome() + "<br/><br/>" +

		"Este aviso é para lembrá-lo(a) que acabou de receber uma mensagem. Acesse o sistema para vê-la. " + " <br/><br/>" +

		"Endereço de Acesso: http://www.lpncondominio.com.br <br/>" + "Usuario: <strong>" + usuario.getLogin() + "</strong><br/>" + "<br/><br/>" +

		"Para treinamento, dúvidas ou solicitações fique a vontade para nos contatar. <br/>" + "http://www.lphantus.com.br" + "<br/><br/>" +

		"Att,<br/>" + "<img src=\"" + getAssinaturaSuporte() + "\"> <br/> ";

		return textoCorpo;
	}

	public static String gerarTextoErroProcessamentoTotem(final String stackTrace) {
		final String textoCorpo = "Senhores, " + "<br/><br/>" +

		"Ocorreu um erro ao processar o arquivo de retorno do sistema de cancelas. Segue detalhamento abaixo: <br/><br/>" +

		"<strong>" + (stackTrace.replaceAll(Constantes.NOVA_LINHA, "<br/>")) + "</strong>" + "<br/><br/>" +

		"Att,<br/>" + "<img src=\"" + getAssinaturaSuporte() + "\"> <br/> ";

		return textoCorpo;
	}

	public static String gerarTextoErroProcessamentoRotina(String tipoRotina, String stackTrace) {
		final String textoCorpo = "Senhores, " + "<br/><br/>" +

		"Ocorreu um erro ao processar rotina cadastrada na aplicacao. Segue detalhamento abaixo: <br/><br/>" +

		"Tipo da rotina: <strong>" + tipoRotina + "</strong><br/>" + "Erro informado: <strong>" + (stackTrace.replaceAll(Constantes.NOVA_LINHA, "<br/>")) + "</strong>"
				+ "<br/><br/>" +

				"Att,<br/>" + "<img src=\"" + getAssinaturaSuporte() + "\"> <br/> ";

		return textoCorpo;
	}

	public static String gerarTextoTotemDuplicado(List<Totem> duplicados, int tipo) {

		String tipoTexto, totens = Constantes.VAZIO;
		switch (tipo) {
		case 1:
			tipoTexto = "agregado";
			break;
		case 2:
			tipoTexto = "morador";
			break;
		case 3:
			tipoTexto = "visitante";
			break;
		case 4:
		default:
			tipoTexto = "prestador";
			break;
		}

		for (Totem registro : duplicados) {
			totens = "Totem [id=" + registro.getId() + "]<br/>";
		}

		final String textoCorpo = "Senhores, " + "<br/><br/>" +

		"Ha senhas de totem duplicadas, do tipo " + tipoTexto + ". Segue detalhamento abaixo: <br/><br/>" +

		"Totens: <br/><strong>" + totens + "</strong><br/><br/>" +

		"Att,<br/>" + "<img src=\"" + getAssinaturaSuporte() + "\"> <br/> ";

		return textoCorpo;
	}

	public static String gerarTextoReservaAprovada(ReservaDTO reservaPedida, UnidadeHabitacionalDTO unidadePedida) {
		String textoCorpo = "A reseva do item \"" + reservaPedida.getItemReserva().getNome() + "\" solicitada pelo morador da unidade habitacional \""

		+ unidadePedida.getIdentificacao()

		+ "\" foi automaticamente aprovada pelo sistema, por nao necessitar pagamento. <br/><br/>"

		+ "Para mudar estas configuracoes, edite o item de reserva, modificando a necessidade de pagamento, e coloque o valor do item.<br/><br/>" + "Att,<br/>"

		+ "<img src=\"" + getAssinaturaSuporte() + "\"> <br/> ";
		;
		return textoCorpo;
	}

	public static String gerarTextoErroOperacional(String stackTrace) {
		final String textoCorpo = "Senhores, " + "<br/><br/>" +

		"Ocorreu um erro ao utilizar a aplicacao. Segue detalhamento abaixo: <br/><br/>" +

		"Erro informado: <strong>" + (stackTrace.replaceAll(Constantes.NOVA_LINHA, "<br/>")) + "</strong>" + "<br/><br/>" +

		"Att,<br/>" + "<img src=\"" + getAssinaturaSuporte() + "\"> <br/> ";

		return textoCorpo;
	}

	public static String getAssinaturaSuporte() {
		return ("http://lphantus.com.br/site/images/assinaturas/assinatura_suporte.jpg");
	}

}
