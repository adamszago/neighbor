package br.com.lphantus.neighbor.component.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.lphantus.neighbor.enums.EnumTipoAcesso;

@FacesConverter(value="tipoAcessoConverter")
public class TipoAcessoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String valor) {
		EnumTipoAcesso objeto = null;
		try{
			Long valorNumerico = Long.valueOf(valor);
			for(EnumTipoAcesso item:EnumTipoAcesso.values()){
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
		if ( objeto instanceof EnumTipoAcesso ){
			retorno = String.format("%d", ((EnumTipoAcesso) objeto).getTipo());
		}else{
			retorno = "";
		}
		return retorno;
	}

}
