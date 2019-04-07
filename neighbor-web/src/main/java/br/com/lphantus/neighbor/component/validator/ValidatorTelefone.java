package br.com.lphantus.neighbor.component.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "br.com.lphantus.neighbor.component.validator.ValidatorTelefone")
public class ValidatorTelefone implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		final String telefoneOriginal = value.toString();
		final String telefonePontos = telefoneOriginal.replace("(", "")
				.replace(")", "").replace("-", "");
		final String telefoneEspacos = telefonePontos.replace(" ", "");

		final boolean numerosInvalidos = !isNumerico(telefoneEspacos);
		final boolean tamanhoInvalido = !tamanhoValido(telefoneEspacos);

		if (numerosInvalidos || tamanhoInvalido) {
			FacesMessage message = new FacesMessage("Telefone Invalido",
					"Telefone incorreto");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}

	}

	private boolean tamanhoValido(final String telefoneEspacos) {
		final Long tamanho = new Long(telefoneEspacos.length());
		return tamanho == 10L || tamanho == 11L;
	}

	private boolean isNumerico(final String telefoneEspacos) {
		final Pattern padrao = Pattern.compile("[0-9]+");
		final Matcher casador = padrao.matcher(telefoneEspacos);
		return casador.find();
	}

}
