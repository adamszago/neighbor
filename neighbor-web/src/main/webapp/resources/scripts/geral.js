function onPageLoad() {

	$('.content').show();
	$('.campo-onload').focus();

	$('.btnNext').click(function() {
		var id = $(this).parent().parent().attr('id'), next, prev;
		next = parseInt(id) + 1;
		prev = parseInt(id) - 1;
		$('#' + id).fadeOut(600);
		$('#' + next).fadeIn(800);
		if (id < 7) {
			$('.ico_top' + prev).parent().animate({
				opacity : 0.4
			}, 500);
			$('.ico_top' + id).parent().animate({
				opacity : 0.99
			}, 500);
			$('.ico_top' + prev).parent().css("opacity", "");
		} else {
			$('.ico_top' + prev).animate({
				opacity : 0.4
			}, 500);
			$('.ico_top' + id).animate({
				opacity : 0.99
			}, 500);
		}
	});
	$('.btnBack')
			.click(
					function() {
						var id = $(this).parent().parent().attr('id'), next = parseInt(id) - 1, prev = parseInt(id) + 1;
						$('#' + id).fadeOut(600);
						$('#' + next).fadeIn(800);
						if (id < 7) {
							$('.ico_top' + (id - 1)).parent().animate({
								opacity : 0.4
							}, 500);
							$('.ico_top' + (next - 1)).parent().animate({
								opacity : 0.99
							}, 500);
							$('.ico_top' + (id - 1)).parent()
									.css("opacity", "");
						} else {
							$('.ico_top' + (id - 1)).animate({
								opacity : 0.4
							}, 500);
							$('.ico_top' + (next - 1)).animate({
								opacity : 0.99
							}, 500);
						}
					});
	$('.btnClose').click(function() {
		var id = $(this).parent().parent().attr('id');
		$('#' + id).fadeOut(1000);
	});

	$(document).ajaxComplete(function(event, xhr, options) {
		if (typeof xhr.responseXML != 'undefined') {
			fixViewState(xhr.responseXML);
		}
	});

	if (typeof jsf !== 'undefined') {
		jsf.ajax.addOnEvent(function(data) {
			if (data.status == "success") {
				fixViewState(data.responseXML);
			}
		});
	}
}

function dataTableSelectOneRadio(radio) {
	var radioId = radio.name.substring(radio.name.lastIndexOf(':')), i, element;

	for (i = 0; i < radio.form.elements.length; i++) {
		element = radio.form.elements[i];

		if (element.name.substring(element.name.lastIndexOf(':')) == radioId) {
			element.checked = false;
		}
	}

	radio.checked = true;
}

function aplicaMascaraMonetaria(hashtagIdCampo) {
	jQuery(hashtagIdCampo).maskMoney({
		showSymbol : true,
		symbol : "R$",
		decimal : ",",
		thousands : ".",
		allowZero : true
	});
}

PrimeFaces.locales['pt'] = {
	closeText : 'Fechar',
	prevText : 'Anterior',
	nextText : 'Próximo',
	currentText : 'Começo',
	monthNames : [ 'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
			'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro' ],
	monthNamesShort : [ 'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago',
			'Set', 'Out', 'Nov', 'Dez' ],
	dayNames : [ 'Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta',
			'Sábado' ],
	dayNamesShort : [ 'Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb' ],
	dayNamesMin : [ 'D', 'S', 'T', 'Q', 'Q', 'S', 'S' ],
	weekHeader : 'Semana',
	firstDay : 1,
	isRTL : false,
	showMonthAfterYear : false,
	yearSuffix : '',
	timeOnlyTitle : 'Só Horas',
	timeText : 'Tempo',
	hourText : 'Hora',
	minuteText : 'Minuto',
	secondText : 'Segundo',
	currentText : 'Data Atual',
	ampm : false,
	month : 'Mês',
	week : 'Semana',
	day : 'Dia',
	allDayText : 'Todo Dia'
};

function centerAndShowDialog(dialog) {
	$(dialog).css(
			"top",
			Math.max(0, (($(window).height() - $(dialog).outerHeight()) / 2)
					+ $(window).scrollTop())
					+ "px");
	$(dialog).css(
			"left",
			Math.max(0, (($(window).width() - $(dialog).outerWidth()) / 2)
					+ $(window).scrollLeft())
					+ "px");
	
	PF(dialog).show();
	
}

function autoResize(id) {
	var newheight, newwidth;

	if (document.getElementById) {
		newheight = document.getElementById(id).contentWindow.document.body.scrollHeight;
		newwidth = document.getElementById(id).contentWindow.document.body.scrollWidth;
	}

	document.getElementById(id).height = (newheight) + "px";
	document.getElementById(id).width = (newwidth) + "px";
}
