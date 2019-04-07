package br.com.lphantus.neighbor.component.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("br.com.lphantus.neighbor.component.validator.ValidatorCNH")
public class ValidatorCNH implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String cnh = value.toString().trim();

		Pattern padrao = Pattern.compile("[0-9]*");
		Matcher casador = padrao.matcher(cnh);

		if (!casador.matches()) {
			FacesMessage message = new FacesMessage("CNH Inválida",
					"CNH incorreta");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}

}
