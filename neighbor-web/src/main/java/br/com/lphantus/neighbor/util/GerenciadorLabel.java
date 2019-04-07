package br.com.lphantus.neighbor.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import br.com.lphantus.neighbor.utils.Constantes.ConstantesView;

public class GerenciadorLabel {

	private static ResourceBundle labels = ResourceBundle
			.getBundle(ConstantesView.BUNDLE_LABEL);

	public static String getMensagem(final String key) {
		return labels.getString(key);
	}

	public static String getMensagem(final String key, final Object... params) {
		final String mensagemParametrizada = MessageFormat.format(
				labels.getString(key), params);
		return mensagemParametrizada;
	}

}
