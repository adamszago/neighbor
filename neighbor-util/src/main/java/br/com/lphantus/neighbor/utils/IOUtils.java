package br.com.lphantus.neighbor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {

	public static void copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = input.read(buffer)) != -1) {
			output.write(buffer, 0, len);
		}
	}

}
