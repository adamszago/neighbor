package br.com.lphantus.neighbor.component.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.lphantus.neighbor.enums.EnumTempoAcesso;

@FacesConverter(value="tempoAcessoConverter")
public class TempoAcessoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String valor) {
		EnumTempoAcesso objeto = null;
		try{
			Long valorNumerico = Long.valueOf(valor);
			for(EnumTempoAcesso item:EnumTempoAcesso.values()){
				if ( item.getTipo().equals(valorNumerico)){
					objeto = item;
					break;
				}
			}
		}catch(NumberFormatException nex){
			// faz nada
		}
		return objeto;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object objeto) {
		final String retorno;
		if ( objeto instanceof EnumTempoAcesso ){
			retorno = String.format("%d", ((EnumTempoAcesso) objeto).getTipo());
		}else{
			retorno = "";
		}
		return retorno;
	}

}
