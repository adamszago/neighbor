package br.com.lphantus.neighbor.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IdadeUtil {

	/**
	 * Calcula a idade. Recebe a data no formato yyyy-MM-dd
	 * 
	 * @param Date
	 *            data
	 * 
	 */
	public static int calculaIdade(Date data) {
		int idade = 0;
		try {
			final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if (data != null) {
				data = df.parse(data.toString());
				final Calendar cData = Calendar.getInstance();
				final Calendar cHoje = Calendar.getInstance();
				cData.setTime(data);
				cData.set(Calendar.YEAR, cHoje.get(Calendar.YEAR));
				idade = cData.after(cHoje) ? -1 : 0;
				cData.setTime(data);
				idade += cHoje.get(Calendar.YEAR) - cData.get(Calendar.YEAR);
			}
		} catch (final ParseException e) {
			System.out
					.println("Erro ao calcular idade IdadeUtil.calculaIdade() - \n"
							+ "esperava data no formato yyyy-MM-dd - exception: "
							+ e.getMessage());
		}

		return idade;
	}

	// Teste
	public static void main(final String[] args) {
		try {

			final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			final Date dataNascimento = fmt.parse("1989-03-24");

			System.out.println("data: " + dataNascimento);
			System.out.println("idade: " + calculaIdade(dataNascimento));

		} catch (final ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
