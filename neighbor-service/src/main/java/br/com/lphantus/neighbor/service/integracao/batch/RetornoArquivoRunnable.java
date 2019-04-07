package br.com.lphantus.neighbor.service.integracao.batch;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

import br.com.lphantus.neighbor.service.ITotemService;

public class RetornoArquivoRunnable implements Runnable {

	private ITotemService service;

	public RetornoArquivoRunnable(ITotemService service) {
		this.service = service;
	}

	@Override
	public void run() {
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isCompletelyWritten(File file) {
		RandomAccessFile stream = null;
		try {
			stream = new RandomAccessFile(file, "rw");
			return true;
		} catch (Exception e) {
			// nao esta completamente escrita
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// log.error("Exception during closing file " +
					// file.getName());
				}
			}
		}
		return false;
	}

}
