function mascara_hora(inputHora) {
	var hora = inputHora.value, myhora;
	myhora = '';
	myhora = myhora + hora;

	if (myhora.length == 2) {
		verifica_hora(inputHora);

	} else if (myhora.length == 5) {
		verifica_minuto(inputHora);
	}
}

// verifica hora
function verifica_hora(inputHora) {
	// hrs = (document.forms[0].hora.value.substring(0,2));
	hrs = inputHora.value.substring(0, 2);

	if (isNumeric(hrs)) {
		if ((hrs < 00) || (hrs > 23)) {
			// document.forms[0].hora.value = "";
			inputHora.value = "";
		} else {
			// document.forms[0].hora.value += ":";
			inputHora.value += ":";
		}
		// document.forms[0].hora.focus();
		inputHora.focus();
	} else {
		inputHora.value = "";
	}

}
// verifica minuto
function verifica_minuto(inputHora) {
	// hrs = (document.forms[0].hora.value.substring(0,2));
	hrs = inputHora.value.substring(0, 2);
	// min = (document.forms[0].hora.value.substring(3,5));
	min = inputHora.value.substring(3, 5);
	if (isNumeric(min)) {
		if ((min < 00) || (min > 59)) {
			// document.forms[0].hora.value=hrs+':';
			inputHora.value = hrs + ':';
		}
	} else {
		inputHora.value = hrs + ':';
	}

}

function isNumeric(valor) {
	var i;
	validChar = '0123456789';
	for (i = 0; i < valor.length; i++) {
		if (validChar.indexOf(valor.substr(i, 1)) < 0) {
			return false;
		}
	}
	return true;
}

function mascara_data(data) {
	var mydata = '';
	mydata = mydata + data;
	if (mydata.length == 2) {
		mydata = mydata + '/';
		document.forms[0].data.value = mydata;
	}
	if (mydata.length == 5) {
		mydata = mydata + '/';
		document.forms[0].data.value = mydata;
	}
	if (mydata.length == 10) {
		verifica_data();
	}
}

function verifica_data() {

	dia = (document.forms[0].data.value.substring(0, 2));
	mes = (document.forms[0].data.value.substring(3, 5));
	ano = (document.forms[0].data.value.substring(6, 10));

	situacao = "";
	// verifica o dia valido para cada mes
	// if ((dia < 01)||(dia < 01 || dia > 30) && ( mes == 04 || mes == 06 || mes
	// == 09 || mes == 11 ) || dia > 31) {
	// situacao = "falsa";
	// }

	// verifica se o mes e valido
	if (mes < 01 || mes > 12) {
		situacao = "falsa";
	}

	// verifica se e ano bissexto
	if (mes == 2
			&& (dia < 01 || dia > 29 || (dia > 28 && (parseInt(ano / 4) != ano / 4)))) {
		situacao = "falsa";
	}

	if (document.forms[0].data.value == "") {
		situacao = "falsa";
	}

	if (situacao == "falsa") {
		alert("Data inválida!");
		document.forms[0].data.focus();
	}
}

function MascaraMoeda(objTextBox, SeparadorMilesimo, SeparadorDecimal, e) {
	console.log(objTextBox.value);
	var i = j = 0, key = '', len = len2 = 0, strCheck = '0123456789', aux = aux2 = '', whichCode = (window.Event) ? e.which
			: e.keyCode;
	if (whichCode == 13)
		return true;
	key = String.fromCharCode(whichCode); // Valor para o código da Chave
	if (strCheck.indexOf(key) == -1)
		return false; // Chave inválida
	len = objTextBox.value.length;
	for (i = 0; i < len; i++)
		if ((objTextBox.value.charAt(i) != '0')
				&& (objTextBox.value.charAt(i) != SeparadorDecimal))
			break;
	aux = '';
	for (; i < len; i++)
		if (strCheck.indexOf(objTextBox.value.charAt(i)) != -1)
			aux += objTextBox.value.charAt(i);
	aux += key;
	len = aux.length;
	if (len == 0)
		objTextBox.value = '';
	if (len == 1)
		objTextBox.value = '0' + SeparadorDecimal + '0' + aux;
	if (len == 2)
		objTextBox.value = '0' + SeparadorDecimal + aux;
	if (len > 2) {
		aux2 = '';
		for (j = 0, i = len - 3; i >= 0; i--) {
			if (j == 3) {
				aux2 += SeparadorMilesimo;
				j = 0;
			}
			aux2 += aux.charAt(i);
			j++;
		}
		objTextBox.value = '';
		len2 = aux2.length;
		for (i = len2 - 1; i >= 0; i--)
			objTextBox.value += aux2.charAt(i);
		objTextBox.value += SeparadorDecimal + aux.substr(len - 2, len);
	}
	return false;
}

function mascara(o, f) {
	v_obj = o;
	v_fun = f;
	setTimeout("execmascara()", 1);
}

function execmascara() {
	v_obj.value = v_fun(v_obj.value);
}

function valor(v) {
	v = v.replace(/\D/g, "");
	v = v.replace(/[0-9]{15}/, "invÃ¡lido");
	v = v.replace(/(\d{1})(\d{11})$/, "$1,$2"); // coloca ponto antes dos
	// Ãºltimos 11 digitos
	v = v.replace(/(\d{1})(\d{8})$/, "$1,$2"); // coloca ponto antes dos
	// Ãºltimos 8 digitos
	v = v.replace(/(\d{1})(\d{5})$/, "$1,$2"); // coloca ponto antes dos
	// Ãºltimos 5 digitos
	v = v.replace(/(\d{1})(\d{1,2})$/, "$1.$2"); // coloca virgula antes dos
	// Ãºltimos 2 digitos
	return v;
}

/*
 * Remover acentos e caracteres especiais Ex: <input type="text"
 * onkeyup="this.value = removerCaracteresEspeciais(this.value)"> Cara
 */
function removerCaracteresEspeciais(strToReplace) {
	str_acento = "áàãâäéèêëíìîïóòõôöúùûüçÁÀÃÂÄÉÈÊËÍÌÎÏÓÒÕÖÔÚÙÛÜÇ~;,{}[]?!@#$%¨&*()_-+=^´''\"\"\ºª/|\§<>: ";
	str_sem_acento = "aaaaaeeeeiiiiooooouuuucAAAAAEEEEIIIIOOOOOUUUUC";
	var nova = "", i;
	for (i = 0; i < strToReplace.length; i++) {
		if (str_acento.indexOf(strToReplace.charAt(i)) != -1) {
			nova += str_sem_acento.substr(str_acento.search(strToReplace
					.substr(i, 1)), 1);
		} else {
			nova += strToReplace.substr(i, 1);
		}
	}
	return nova;
}

// mascara telefone 8 e 9 digitos
function maskTel(v) {
	v = v.replace(/\D/g, "");
	if (v.length <= 10) {
		v = v.replace(/^(\d\d)(\d)/g, "($1) $2");
		v = v.replace(/(\d{4})(\d)/, "$1-$2");
	} else {
		v = v.replace(/^(\d\d)(\d)/g, "($1) $2");
		v = v.replace(/(\d{5})(\d)/, "$1-$2");
	}
	return v;
}
