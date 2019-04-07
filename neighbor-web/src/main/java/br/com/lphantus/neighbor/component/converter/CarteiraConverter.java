package br.com.lphantus.neighbor.component.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.lphantus.neighbor.common.CarteiraDTO;

@FacesConverter(value = "carteiraConverter")
public class CarteiraConverter implements Converter {

	@Override
	public CarteiraDTO getAsObject(final FacesContext context,
			final UIComponent component, final String string) {
		if ((null == string) || string.trim().isEmpty()) {
			return null;
		} else {
			final CarteiraDTO retorno = new CarteiraDTO();
			retorno.setId(Long.valueOf(string));
			return retorno;
		}
	}

	@Override
	public String getAsString(final FacesContext context,
			final UIComponent component, final Object banco) {
		if (null == banco) {
			return "";
		} else {
			if (banco instanceof CarteiraDTO) {
				final CarteiraDTO carteiraObject = (CarteiraDTO) banco;
				return String.format("%d", carteiraObject.getId());
			} else {
				return "";
			}
		}
	}
}
