package br.com.lphantus.neighbor.component.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.lphantus.neighbor.utils.Utilitarios;

@FacesValidator("br.com.lphantus.neighbor.component.validator.ValidatorCNPJ")
public class ValidatorCNPJ implements Validator {

	@Override
	public void validate(final FacesContext context,
			final UIComponent component, final Object value)
			throws ValidatorException {
		FacesMessage message = null;

		if ((null == value) || !Utilitarios.isCNPJ(value.toString())) {
			message = new FacesMessage("CNPJ Inválido", "CNPJ incorreto");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
}
