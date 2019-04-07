package br.com.lphantus.neighbor.component.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.StringUtils;

@FacesConverter("moneyConverter")
public class MoneyConverter implements Converter {

	final private Locale locale = new Locale("pt", "BR");
	final private DecimalFormat decimalFormat = new DecimalFormat("##0,00",
			new DecimalFormatSymbols(this.locale));

	@Override
	public BigDecimal getAsObject(final FacesContext fc,
			final UIComponent component, final String value) {
		try {
			if (StringUtils.isEmpty(value)) {
				return BigDecimal.ZERO;
			} else {
				this.decimalFormat.setParseBigDecimal(true);
				return (BigDecimal) this.decimalFormat.parse(value);
			}
		} catch (final ParseException e) {
			throw new ConverterException("Error", e);
		}
	}

	@Override
	public String getAsString(final FacesContext fc,
			final UIComponent component, final Object value) {
		final DecimalFormat df = new DecimalFormat("###,###,##0.00");
		return df.format(value);
	}
}
