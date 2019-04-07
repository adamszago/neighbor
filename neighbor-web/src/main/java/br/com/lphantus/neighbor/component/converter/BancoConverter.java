package br.com.lphantus.neighbor.component.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.lphantus.neighbor.common.BancoDTO;

@FacesConverter("bancoConverter")
public class BancoConverter implements Converter {

	@Override
	public BancoDTO getAsObject(final FacesContext context, final UIComponent component, final String string) {
		if ((null == string) || string.trim().isEmpty()) {
			return null;
		} else {
			final String[] split = string.split(";");
			final BancoDTO retorno = new BancoDTO();
			retorno.setCodigoBanco(Long.valueOf(split[0]));
			retorno.setNomeBanco(split[1]);
			return retorno;
		}
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object banco) {
		if (null == banco) {
			return "";
		} else {
			if (banco instanceof BancoDTO) {
				final BancoDTO bancoObject = (BancoDTO) banco;
				return String.format("%d;%s", bancoObject.getCodigoBanco(), bancoObject.getNomeBanco());
			} else {
				return "";
			}
		}
	}
}
