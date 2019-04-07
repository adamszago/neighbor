package br.com.lphantus.neighbor.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

public class Utilitarios {

	/**
	 * Gera sequencia de numeros(casa ou blocos) a partir do texto digitado pelo
	 * usuario.
	 * 
	 * @param texto
	 *            Digitado pelo morador
	 * @return Lista de casas
	 */
	public static List<String> geraSequenciaNumerica(final String texto) {
		final List<String> result = new ArrayList<String>();
		if ((null != texto) && !texto.isEmpty()) {
			final Scanner s = new Scanner(texto);
			s.useDelimiter(",");
			while (s.hasNext()) {
				final String proximo = s.next();
				boolean primeiro = true;
				String valorInicial = Constantes.VAZIO;
				String valorFinal = Constantes.VAZIO;
				if (proximo.contains(Constantes.HIFEN)) {
					for (int i = 0; i < proximo.length(); i++) {

						if (proximo.charAt(i) == '-') {
							primeiro = false;
						}
						if (Character.isDigit(proximo.charAt(i)) && primeiro) {
							valorInicial += proximo.charAt(i);
						}
						if (Character.isDigit(proximo.charAt(i)) && !primeiro) {
							valorFinal += proximo.charAt(i);
						}
					}

					int valorUm = Integer.parseInt(valorInicial);
					final int valorDois = Integer.parseInt(valorFinal);
					while (valorUm <= valorDois) {
						result.add(valorUm + Constantes.VAZIO);
						// result += valorUm + ", ";
						valorUm++;
					}

				} else {
					result.add(proximo);
					// result += proximo + ", ";
				}
			}
			s.close();
		}
		return result;
	}

	/**
	 * Determina se o cnpj eh valido
	 * 
	 * @param entrada
	 *            O cnpj
	 * @return Um boolean
	 */
	public static boolean isCNPJ(final String entrada) {
		if (Strings.isNullOrEmpty(entrada)) {
			return false;
		}

		final String CNPJ = entrada.replaceAll(Pattern.compile("\\s").toString(), "").replaceAll(Pattern.compile("\\D").toString(), "");

		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") || CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") || CNPJ.equals("44444444444444")
				|| CNPJ.equals("55555555555555") || CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") || CNPJ.equals("88888888888888")
				|| CNPJ.equals("99999999999999") || (CNPJ.length() != 14)) {
			return (false);
		}

		char dig13, dig14;
		int sm, i, r, num, peso;

		// "try" - protege o código para eventuais erros de conversao de tipo
		// (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {
				// converte o i-ésimo caractere do CNPJ em um número:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posição de '0' na tabela ASCII)
				num = CNPJ.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10) {
					peso = 2;
				}
			}

			r = sm % 11;
			if ((r == 0) || (r == 1)) {
				dig13 = '0';
			} else {
				dig13 = (char) ((11 - r) + 48);
			}

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = CNPJ.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10) {
					peso = 2;
				}
			}

			r = sm % 11;
			if ((r == 0) || (r == 1)) {
				dig14 = '0';
			} else {
				dig14 = (char) ((11 - r) + 48);
			}

			// Verifica se os dígitos calculados conferem com os dígitos
			// informados.
			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13))) {
				return (true);
			} else {
				return (false);
			}
		} catch (final InputMismatchException erro) {
			return (false);
		}

	}

	/**
	 * Determina se o cpf eh valido
	 * 
	 * @param entrada
	 *            O cpf
	 * @return Um booleano
	 */
	public static boolean isCPF(final String entrada) {

		if (Strings.isNullOrEmpty(entrada)) {
			return false;
		}

		final String CPF = entrada.replaceAll("[^0-9]", "");

		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222") || CPF.equals("33333333333") || CPF.equals("44444444444")
				|| CPF.equals("55555555555") || CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888") || CPF.equals("99999999999")
				|| (CPF.length() != 11)) {
			return (false);
		}

		char dig10, dig11;
		int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo
		// (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posicao de '0' na tabela ASCII)
				num = CPF.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11)) {
				dig10 = '0';
			} else {
				dig10 = (char) (r + 48);
			} // converte no respectivo caractere numerico

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = CPF.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11)) {
				dig11 = '0';
			} else {
				dig11 = (char) (r + 48);
			}

			// Verifica se os digitos calculados conferem com os digitos
			// informados.
			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
				return (true);
			} else {
				return (false);
			}
		} catch (final InputMismatchException erro) {
			return (false);
		}
	}

	public static String diaSemBarras() {
		final Date data = new Date();
		final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(data).replaceAll("[^0-9]", "");
	}

	public static String diaCorrente() {
		return formatarDia(new Date());
	}

	public static String formatarDia(final Date dataQualquer) {
		final String retorno;
		if (null == dataQualquer) {
			retorno = Constantes.VAZIO;
		} else {
			final SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
			retorno = formatador.format(dataQualquer);
		}
		return retorno;
	}

	public static String formatarDiaHora(final Date dataQualquer) {
		final String retorno;
		if (dataQualquer == null) {
			retorno = Constantes.VAZIO;
		} else {
			final SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			retorno = formatador.format(dataQualquer);
		}
		return retorno;
	}

	public static String obterStackTrace(final Exception exception) {
		StringWriter errors = new StringWriter();
		exception.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

}
