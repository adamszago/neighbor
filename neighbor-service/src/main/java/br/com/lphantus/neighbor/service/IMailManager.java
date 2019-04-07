package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ReservaDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Totem;

public interface IMailManager {

	public void enviarEmailUsuarioTotem(final String textoCorpo, final String destinatario);

	public void enviarEmailUsuarioCadastrado(final String textoCorpo, final String destinatario);

	public void enviarEmailRecuperacao(final UsuarioDTO usuario, final String novaSenha);

	public void enviarEmailAvisoMensagemRecebida(final MoradorDTO morador);

	public void enviarEmailAvisoMensagemRecebida(final UsuarioDTO usuario);

	public void enviarEmailErroProcessamentoArquivo(Exception e);

	public void enviarEmailErroProcessamentoRotina(String tipoRotina, Exception e);

	public void enviarEmailTotemDuplicado(List<Totem> duplicados, int tipo);

	public void enviarEmailReservaAprovada(ReservaDTO reservaPedida, UnidadeHabitacionalDTO unidadePedida);
	
	public void enviarEmailErroOperacional(String stackTrace);

}
