package br.com.lphantus.neighbor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.jibble.simpleftp.SimpleFTP;

/**
 * 
 * @author Fábio Borges de Oliveira Vilela
 */
public class FtpUtils {

	public static void send(final String ip, final Integer port,
			final String user, final String pass, final String sourcePath,
			final String targetFileName) throws IOException {
		Log log = new SystemStreamLog();
		final SimpleFTP ftp = new SimpleFTP(log);

		// Connect to an FTP server on port 21.
		ftp.connect(ip, port, user, pass);
		
		// ascii: converter EOL windows/linux
		ftp.ascii();

		// Change to a new working directory on the FTP server.
		// ftp.cwd(targetPath);

		// You can also upload from an InputStream, e.g.
		ftp.stor(new FileInputStream(new File(sourcePath)), targetFileName);
		
		// Quit from the FTP server.
		ftp.disconnect();
	}
	
}