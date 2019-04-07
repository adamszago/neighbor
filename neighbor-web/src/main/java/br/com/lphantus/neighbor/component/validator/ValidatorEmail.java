package br.com.lphantus.neighbor.component.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;

@FacesValidator(value = "br.com.lphantus.neighbor.component.validator.ValidatorEmail")
public class ValidatorEmail implements Validator {

	@Override
	public void validate(final FacesContext context,
			final UIComponent component, final Object value)
			throws ValidatorException {
		FacesMessage message;

		String email;
		boolean emailPreenchido;
		if (null == value) {
			email = null;
			emailPreenchido = false;
		} else {
			email = value.toString();
			emailPreenchido = StringUtils.isNotBlank(email);
		}

		if (emailPreenchido) {

			final String padrao = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			final Pattern pattern = Pattern.compile(padrao);
			final Matcher casador = pattern.matcher(email);

			final boolean naoCasou = !casador.find();
			if (naoCasou) {
				message = new FacesMessage("E-mail Inválido", "Email incorreto");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}

	}
}
