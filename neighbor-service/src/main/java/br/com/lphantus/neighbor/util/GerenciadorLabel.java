package br.com.lphantus.neighbor.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class GerenciadorLabel {

	private static ResourceBundle labels = ResourceBundle
			.getBundle("br.com.neighbor.resources.messages");

	public static String getMensagem(final String key) {
		return labels.getString(key);
	}

	public static String getMensagem(final String key, final Object... params) {
		final String mensagemParametrizada = MessageFormat.format(
				labels.getString(key), params);
		return mensagemParametrizada;
	}

}
