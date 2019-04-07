package br.com.lphantus.neighbor.component.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.lphantus.neighbor.common.LancamentoDTO;

@FacesConverter("lancamentoConverter")
public class LancamentoConverter implements Converter {

	@Override
	public Object getAsObject(final FacesContext context,
			final UIComponent component, final String string) {
		final LancamentoDTO objeto = new LancamentoDTO();
		objeto.setId(Long.valueOf(string));
		return objeto;
	}

	@Override
	public String getAsString(final FacesContext context,
			final UIComponent component, final Object objeto) {
		return String.format("%d", ((LancamentoDTO) objeto).getId());
	}
}
