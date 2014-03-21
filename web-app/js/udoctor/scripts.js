$(document).ready(function() {	

	$("#location").click(function() {
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(function(position) {
				$("#loca").html(position.coords.latitude + ", " + position.coords.longitude);
			}); 
		}
		$(this).css('opacity','0.6');
	});

	popclosed=false;

	$('#location').popover({
		placement: 'top',
		trigger: 'manual',
		html: true
	});

	$('#header #location').hover(function(){
		if($('.popover').css('display')!='block' && popclosed!=true || $('.popover').css('display')=='block'){
			$('#header #location').popover('toggle');
		}
		
		$('#header #location').click(function(){
			$('#header #location').popover('hide');
			popclosed=true;
		});

		$('.location-active').popover('hide');
	});

	$('#index input').hover(function(){
		if($('.popover').css('display')!='block' && popclosed!=true){
			$('#index #location').popover('show');
		}

		$('.popover, #index #location').click(function(){
			$('#index #location').popover('hide');
			popclosed=true;
		});

		$('.location-active').popover('hide');
	});

	$('.help').popover({
		placement: 'right',
		trigger: 'hover',
		html: true
	});

	$('#option-login .dropdown-toggle, #option-share .dropdown-toggle, #profile-edit-btn').tooltip({
		placement: 'left'
	});
	
	$('.pagination .previous a, .pagination .next a, #admin-listing .form-actions .btn, #admin-listing table i, .info-group i, #address-list i, #insert-address i').tooltip({
		placement: 'bottom'
	});

	$(".map").fancybox({
		'width': '90%',
		'height': '90%',
		'transitionIn': 'elastic',
		'transitionOut': 'elastic',
		'type': 'iframe',
		'overlayColor': '#000',
		'centerOnScroll': true
	});

	$(window).scroll(function() {
		if ($('#header').offset().top > 115) {
			$('#admin-listing .form-actions').addClass('form-actions-fixed');
		} else {
			$('#admin-listing .form-actions').removeClass('form-actions-fixed');
		}
	});
	
	$(".phone-field input").focusin(function(){$(this).mask("(99) 9999-9999",{placeholder:" "});});
	$(".date-field").focusin(function(){$(this).mask("99/99/9999",{placeholder:"  "});});
	$(".cep-field").focusin(function(){$(this).mask("99999-999",{placeholder:" "});});
	$(".cnpj-field").focusin(function(){$(this).mask("99.999.999/9999-99",{placeholder:" "});});

	$('#phone1 i').click(function() {
		elements = $('#count-phones').attr('value');
		elements++;
		$('#count-phones').attr('value',elements);
				
		$('#phone1').clone().appendTo($(this).parents('.info-group')).attr({id: 'phone'+elements});
		$('#phone'+elements).find('i').attr({'title':'Remover telefone','onclick':'remove(this)'}).removeClass('icon-plus').addClass('icon-remove').tooltip({placement:'bottom'});
		$('#phone'+elements).find('input').val('').attr({id: 'telefones['+(elements-1)+'].numeroCompleto', name: 'telefones['+(elements-1)+'].numeroCompleto', value: ""}).focusin(function(){$(this).mask("(99) 9999-9999",{placeholder:" "});});
		$('#phone'+elements).find('input:hidden').val('').attr({id: 'telefones['+(elements-1)+'].ativo', name: 'telefones['+(elements-1)+'].ativo', value: "X"});
	});

	$('#insert-address').click(function() {
		elements = $('#address-tabs li').length;
		$('#tab-address1').clone().appendTo('#address-tabs').attr('id','tab-address'+elements).removeClass('active').children().html(elements);
		$('#tab-address'+elements).find('a').attr('href','#address'+elements);

		if (elements == 5) {
			$('#insert-address').hide();
		}
		$('#count-address').attr('value',elements);
		$('#address'+elements+' .status').attr({
			value: 'X',
			id: 'enderecos['+(elements-1)+'].ativo',
			name: 'enderecos['+(elements-1)+'].ativo'
		});
		$('#tab-address'+elements).find('a').click();
	});

	$('#address-list i').click(function() {
		current = $(this).parent().find('.adressId').attr('value');
		elements = $('#address-tabs .tab').length;
		$('#tab-address'+current).remove();

		var lastAddress = $('#address-list').find('.tab-pane').eq(current-1);
		lastAddress.attr('id','address'+(elements));
		lastAddress.find('.adressId').val(elements);
		lastAddress.find('.status').val('');
		lastAddress.find('input:text').val('');
		lastAddress.find('.chzn-single').addClass('chzn-default').find('span').html('Selecione...');

		for (i=1;i<=elements;i++) {
			if (i > current) {
				$('#address-tabs').find('.tab').eq(i-2).attr('id','tab-address'+(i-1));
				$('#address-tabs').find('.tab a').eq(i-2).attr('href','#address'+(i-1)).html((i-1));
				$('#address-list').find('.tab-pane').eq(i-2).attr('id','address'+(i-1));
			}
		}

		if (elements <= 5) {
			$('#insert-address').show();
		}

//		current--;
		$('#tab-address1').find('a').click();
	});

	$('#link1 i').click(function() {
		elements = $('#count-links').attr('value');
		elements++;
		$('#count-links').attr('value',elements);

		$('#link1').clone().appendTo($(this).parents('.info-group')).attr({id: 'link'+(elements)});
		$('#link'+elements).find('i').attr({'title':'Remover link','onclick':'remove(this)'}).removeClass('icon-plus').addClass('icon-remove').tooltip({placement:'bottom'});
		$('#link'+elements).find('input').val('');
		$('#link'+elements).find('input:text:first').attr({id: 'links['+(elements-1)+'].link', name: 'links['+(elements-1)+'].link'});
		$('#link'+elements).find('input:text:last').attr({id: 'links['+(elements-1)+'].descricaoLink', name: 'links['+(elements-1)+'].descricaoLink'});
		$('#link'+elements).find('input:hidden').attr({id: 'links['+(elements-1)+'].ativo', name: 'links['+(elements-1)+'].ativo', value: 'X'});
	});
		
	$(".chzn-select").chosen();
	$(".chzn-select-deselect").chosen({allow_single_deselect:true});
});



function getEndereco(numEnd) {
	$("#ajax-loading").css('display','inline');
	if($.trim(document.getElementById('enderecos['+numEnd+'].cep').value) != ""){
		$.getScript("http://cep.republicavirtual.com.br/web_cep.php?cep="+(document.getElementById('enderecos['+numEnd+'].cep').value).replace("-", "")+"&formato=javascript", function(){
			if(resultadoCEP["resultado"] == 1){
	  			document.getElementById('enderecos['+numEnd+'].logradouro').value = unescape(resultadoCEP["tipo_logradouro"])+" "+unescape(resultadoCEP["logradouro"]);
	  			document.getElementById('enderecos['+numEnd+'].bairro').value = unescape(resultadoCEP["bairro"]);

	  			var cidades = document.getElementById('enderecos['+numEnd+'].cidade');
	  			for (var i = 0; i < cidades.options.length; i++) {
	  			   if (cidades.options[i].text == unescape(resultadoCEP["cidade"])) {
	  			       if (cidades.selectedIndex != i) {
	  			    	   
	  			    	    var index = $('#teste').prop("selectedIndex");
	  			    	    index += 1;
	  			    	    $('#teste option').eq(index).attr('selected', 'selected');
	  			    	    $('#teste').chosen().change();
	  			    	    $('#teste').trigger("liszt:updated");
	  			    	 
	  			       }
	  			       break;
	  			    }
	  			}
	  			 
	  			document.getElementById('enderecos['+numEnd+'].complemento').focus();

			}else{
				alert("Endereco nao encontrado");
			}
		});
	}else{
		alert("enderecos["+numEnd+"].cep");
	}
	$("#ajax-loading").hide();
};

function remove(id) {
	$(id).tooltip('hide');
	$(id).parent().remove();
};