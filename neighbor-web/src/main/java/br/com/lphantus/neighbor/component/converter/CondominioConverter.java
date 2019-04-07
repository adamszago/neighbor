package br.com.lphantus.neighbor.component.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.PessoaJuridicaDTO;

@FacesConverter("condominioConverter")
public class CondominioConverter implements Converter {

	@Override
	public CondominioDTO getAsObject(final FacesContext context, final UIComponent component, final String string) {
		if ((null == string) || string.trim().isEmpty()) {
			return null;
		} else {
			final String[] split = string.split(";");
			final CondominioDTO retorno = new CondominioDTO();
			retorno.setPessoa(new PessoaJuridicaDTO());
			retorno.getPessoa().setIdPessoa(Long.valueOf(split[0]));
			retorno.getPessoa().setNome(split[1]);
			return retorno;
		}
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object condominio) {
		if (null == condominio) {
			return "";
		} else {
			if (condominio instanceof CondominioDTO) {
				final CondominioDTO condominioObject = (CondominioDTO) condominio;
				return String.format("%d;%s", condominioObject.getPessoa().getIdPessoa(), condominioObject.getPessoa().getNome());
			} else {
				return "";
			}
		}
	}
}
