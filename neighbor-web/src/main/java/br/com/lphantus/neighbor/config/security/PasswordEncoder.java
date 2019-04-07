package br.com.lphantus.neighbor.config.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Codifica os passwords em formato md5
 * 
 * @author elias.policena@lphantus.com.br
 * @since 08/11/2014
 *
 */
public class PasswordEncoder implements
		org.springframework.security.crypto.password.PasswordEncoder {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private MessageDigest md5Encoder;

	public PasswordEncoder() {
		try {
			md5Encoder = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.error("Erro ao criar encoder md5", e);
		}
	}

	@Override
	public String encode(CharSequence rawPassword) {
		md5Encoder.update(rawPassword.toString().getBytes());
		return new BigInteger(1, md5Encoder.digest()).toString(16);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		md5Encoder.update(rawPassword.toString().getBytes());
		return new BigInteger(1, md5Encoder.digest()).toString(16).equals(
				encodedPassword);
	}

}
