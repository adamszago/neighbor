package br.com.lphantus.neighbor.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public static Date add(final Date date, final int value, final int type) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(type, value);
		return cal.getTime();
	}

	public static Date addDays(final Date date, final int days) {
		return add(date, days, Calendar.DATE);
	}

	public static Date addMonths(final Date date, final int months) {
		return add(date, months, Calendar.MONTH);
	}

	public static Date addYear(final Date date, final int years) {
		return add(date, years, Calendar.YEAR);
	}

	public static boolean checkSystemDate(final Date data) {
		final Date now = new Date();
		if (now.after(DateUtil.addYear(data, Constantes.SYSTEM_MAX_DATE_YEAR))) {
			return false;
		}

		if (now.before(DateUtil.addYear(data, -Constantes.SYSTEM_MIN_DATE_YEAR))) {
			return false;
		}

		return true;
	}

	public static boolean dataEMenorQueHoje(final Date date) {

		final Calendar calendarioData = new GregorianCalendar();
		calendarioData.setTime(date);
		calendarioData.set(Calendar.HOUR, 0);
		calendarioData.set(Calendar.MINUTE, 0);
		calendarioData.set(Calendar.SECOND, 0);
		calendarioData.set(Calendar.MILLISECOND, 0);

		final Calendar calendarioHoje = new GregorianCalendar();
		calendarioHoje.setTime(new Date());
		calendarioHoje.set(Calendar.HOUR, 0);
		calendarioHoje.set(Calendar.MINUTE, 0);
		calendarioHoje.set(Calendar.SECOND, 0);
		calendarioHoje.set(Calendar.MILLISECOND, 0);

		return calendarioData.before(calendarioHoje);
	}

	public static Date obterDataSemHora(final Date date) {
		final Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.HOUR, 0);
		final Date retorno = cal.getTime();
		return retorno;
	}

	public static Date obterProximoDia(final Date date) {
		final Date semHora = obterDataSemHora(date);
		final Calendar cal = new GregorianCalendar();
		cal.setTime(semHora);
		cal.add(Calendar.DATE, 1);
		final Date retorno = cal.getTime();
		return retorno;
	}

	public static Date obterProximaSemana(final Date date) {
		final Date semHora = obterDataSemHora(date);
		final Calendar cal = new GregorianCalendar();
		cal.setTime(semHora);
		cal.add(Calendar.DATE, 7);
		final Date retorno = cal.getTime();
		return retorno;
	}

	public static Date obterProximoMes(final Date date) {
		final Date semHora = obterDataSemHora(date);
		final Calendar cal = new GregorianCalendar();
		cal.setTime(semHora);
		cal.add(Calendar.MONTH, 1);
		final Date retorno = cal.getTime();
		return retorno;
	}

	public static Date obterProximoSemestre(final Date date) {
		final Date semHora = obterDataSemHora(date);
		final Calendar cal = new GregorianCalendar();
		cal.setTime(semHora);
		cal.add(Calendar.MONTH, 6);
		final Date retorno = cal.getTime();
		return retorno;
	}

	public static Date obterProximoAno(final Date date) {
		final Date semHora = obterDataSemHora(date);
		final Calendar cal = new GregorianCalendar();
		cal.setTime(semHora);
		cal.add(Calendar.YEAR, 1);
		final Date retorno = cal.getTime();
		return retorno;
	}

	public static Date obterPrimeiroDiaMesPrimeiraHora(Date dataMes) {
		final Calendar cal = new GregorianCalendar();
		cal.setTime(dataMes);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		return cal.getTime();
	}

	public static Date obterUltimoDiaMesUltimaHora(Date dataMes) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(obterPrimeiroDiaMesPrimeiraHora(dataMes));
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.MILLISECOND, -1);
		return cal.getTime();
	}
	
}
