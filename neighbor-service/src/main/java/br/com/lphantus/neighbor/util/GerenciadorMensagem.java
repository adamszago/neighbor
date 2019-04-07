package br.com.lphantus.neighbor.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import br.com.lphantus.neighbor.utils.Constantes;

public class GerenciadorMensagem {

	private static final ResourceBundle bundle = ResourceBundle
			.getBundle(Constantes.ConstantesService.BUNDLE_PROPERTIES);

	public static String getMensagem(final String key) {
		return bundle.getString(key);
	}

	public static String getMensagem(final String key, final Object... params) {
		final String mensagemParametrizada = MessageFormat.format(
				bundle.getString(key), params);
		return mensagemParametrizada;
	}

}
