package br.com.lphantus.neighbor.component.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "DefaultConverter")
public class DefaultConverter implements Converter {

	@Override
	public Object getAsObject(final FacesContext context,
			final UIComponent component, final String value) {
		if (value != null) {
			final Map<String, Object> map = component.getAttributes();
			return map.get(value);
		}
		return null;
	}

	@Override
	public String getAsString(final FacesContext context,
			final UIComponent component, final Object value) {
		if (value != null) {
			component.getAttributes().put(value.toString(), value);
			return value.toString();
		}
		return null;
	}

}
