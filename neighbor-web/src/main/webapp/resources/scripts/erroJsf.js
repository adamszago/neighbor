function fixViewState(responseXML) {
	var viewState = getViewState(responseXML), i, form;

	if (viewState) {
		for (i = 0; i < document.forms.length; i++) {
			form = document.forms[i];

			if (form.method == "post") {
				if (!hasViewState(form)) {
					createViewState(form, viewState);
				}
			} else { // PrimeFaces also adds them to GET forms!
				removeViewState(form);
			}
		}
	}
}

function getViewState(responseXML) {
	var updates = responseXML.getElementsByTagName("update"), i, update;

	for (i = 0; i < updates.length; i++) {
		update = updates[i];

		if (update.getAttribute("id").match(
				/^([\w]+:)?javax\.faces\.ViewState(:[0-9]+)?$/)) {
			return update.firstChild.nodeValue;
		}
	}

	return null;
}

function hasViewState(form) {
	var i;
	for (i = 0; i < form.elements.length; i++) {
		if (form.elements[i].name == "javax.faces.ViewState") {
			return true;
		}
	}

	return false;
}

function createViewState(form, viewState) {
	var hidden;

	try {
		hidden = document.createElement("<input name='javax.faces.ViewState'>"); // IE6-8.
	} catch (e) {
		hidden = document.createElement("input");
		hidden.setAttribute("name", "javax.faces.ViewState");
	}

	hidden.setAttribute("type", "hidden");
	hidden.setAttribute("value", viewState);
	hidden.setAttribute("autocomplete", "off");
	form.appendChild(hidden);
}

function removeViewState(form) {
	var i, element;
	for (i = 0; i < form.elements.length; i++) {
		element = form.elements[i];
		if (element.name == "javax.faces.ViewState") {
			element.parentNode.removeChild(element);
		}
	}
}