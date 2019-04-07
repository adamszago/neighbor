package br.com.lphantus.neighbor.component.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.StringUtils;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;

@FacesConverter(value = "ufConverter")
public class UnidadeFederativaConverter implements Converter {

	@Override
	public Object getAsObject(final FacesContext contexto,
			final UIComponent component, final String argumento) {
		UnidadeFederativa uf = null;
		if (StringUtils.isNotBlank(argumento)) {
			final String[] values = argumento.split(";");
			for (final UnidadeFederativa unidade : UnidadeFederativa.values()) {
				if (unidade.getSigla().equalsIgnoreCase(values[0])) {
					uf = unidade;
					break;
				}
			}
		}
		return uf;
	}

	@Override
	public String getAsString(final FacesContext contexto,
			final UIComponent component, final Object argumento) {
		final UnidadeFederativa unidade = UnidadeFederativa.class
				.cast(argumento);
		return String.format("%s;%s;%s", unidade.getSigla(),
				unidade.getCapital(), unidade.getCapital());
	}

}
