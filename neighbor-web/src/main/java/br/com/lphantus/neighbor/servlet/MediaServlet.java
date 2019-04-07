package br.com.lphantus.neighbor.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Servlet implementation class MediaServlet
 */
@Component
@WebServlet("/mediaservlet")
public class MediaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// -----------------------------------------------------------
	// TODO: NAO ROLOU PROPERTY PLACE HOLDER, VERIFICAR O PORQUE 
	// -----------------------------------------------------------
	
	@Value("${neighbor.caminho.fotos}")
	private String diretorioFotos;

	@Value("${neighbor.caminho.imagemsemfoto}")
	private String imagemSemFoto;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		try {

			final String PREFIXO_FOTO_PRESTADOR = "prestador_";
			final String PREFIXO_FOTO_VISITANTE = "visitante_";
			final String EXTENSAO_ARQUIVO = ".png";
			final ServletContext servletContext = getServletContext();

			final String idPrestador = request.getParameter("idPrestador");
			final String idVisitante = request.getParameter("idVisitante");
			String filename;
			String fullPath;

			if (StringUtils.isNotBlank(idPrestador)) {
				filename = PREFIXO_FOTO_PRESTADOR + idPrestador
						+ EXTENSAO_ARQUIVO;
				fullPath = diretorioFotos + filename;
			} else if (StringUtils.isNotBlank(idVisitante)) {
				filename = PREFIXO_FOTO_VISITANTE + idVisitante
						+ EXTENSAO_ARQUIVO;
				fullPath = diretorioFotos + filename;
			} else {
				filename = null;
				fullPath = null;
			}

			filename = diretorioFotos + filename;

			File file;
			if (fullPath == null) {
				file = new File(imagemSemFoto);
				filename = imagemSemFoto;
			} else {
				file = new File(fullPath);
				if (null == file || !file.exists()) {
					file = new File(imagemSemFoto);
				}
			}

			final String mimeType = servletContext.getMimeType(filename);
			if (mimeType == null) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			response.setContentType(mimeType);
			response.setContentLength((int) file.length());

			final FileInputStream in = new FileInputStream(file);
			final ServletOutputStream out = response.getOutputStream();

			final byte[] buf = new byte[1024];
			int count = 0;

			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}

			out.flush();
			in.close();
			out.close();

		} catch (final IOException exception) {
			exception.printStackTrace();
			throw exception;
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		// TODO Auto-generated method stub
	}

}
