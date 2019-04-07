package br.com.lphantus.neighbor.service.seguranca;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptografia {

	private static MessageDigest md = null;

	/**
	 * Metodo estatico para a geracao do algoritmo de criptografia.
	 */
	static {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (final NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Criptografa a senha.
	 * 
	 * @param pwd
	 *            String A senha normal.
	 * @return String A senha criptografaga.
	 */
	public static String criptografar(final String pwd) {
		if ((md != null) && (pwd != null) && (pwd != "")) {
			return new String(hexCodes(md.digest(pwd.getBytes())))
					.toLowerCase();
		}
		return null;
	}

	private static char[] hexCodes(final byte[] text) {
		final char[] hexOutput = new char[text.length * 2];
		String hexString;

		for (int i = 0; i < text.length; i++) {
			hexString = "00" + Integer.toHexString(text[i]);
			hexString.toUpperCase().getChars(hexString.length() - 2,
					hexString.length(), hexOutput, i * 2);
		}
		return hexOutput;
	}

}
