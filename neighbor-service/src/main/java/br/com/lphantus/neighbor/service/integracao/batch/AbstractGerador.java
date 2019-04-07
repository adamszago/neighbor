package br.com.lphantus.neighbor.service.integracao.batch;

import java.io.IOException;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.TotemDTO;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public abstract class AbstractGerador implements IGeradorTotem {

	private TotemDTO totem;

	@Override
	public void executar(TotemDTO totem) {
		this.totem = totem;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			// --------------------------------------------------------------
			// ARQUIVO
			CondominioDTO condominio = obtemCondominio();

			if (null != condominio) {
				geraArquivoTotem(condominio);

				// --------------------------------------------------------------
				// FTP

				if (condominio.getNomeAbreviado().contains("terranova")) {
					enviarArquivoTotemFTP();
				}
			}

		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final ServiceException e) {
			e.printStackTrace();
		}
	}

	public abstract CondominioDTO obtemCondominio() throws ServiceException;

	public abstract void enviarArquivoTotemFTP() throws IOException,
			ServiceException;

	public abstract void geraArquivoTotem(CondominioDTO condominio)
			throws IOException, ServiceException;

	/**
	 * @return the totem
	 */
	public TotemDTO getTotem() {
		return totem;
	}

}
