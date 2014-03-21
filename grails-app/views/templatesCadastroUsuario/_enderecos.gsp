	<g:set var="contadorEnderecos" value="${0}" />
	<g:set var="quantidadeEnderecos" value="${enderecos?.size()}" />
		
	<g:if test="${!quantidadeEnderecos}">
	<g:set var="quantidadeEnderecos" value="${1}" />
	</g:if>
	
	<ul class="nav nav-tabs" id="address-tabs">			
	<g:while test="${contadorEnderecos < quantidadeEnderecos}">
		<g:if test="${contadorEnderecos == 0}">
		<li class="tab active" id="tab-address1">
			<a href="#address1" data-toggle="tab">1</a>
		</li>
		<li id="insert-address">
			<i class="icon-plus" title="Inserir endereço"></i>
		</li>	
		</g:if>
		<g:else>
		<li class="tab" id="tab-address${contadorEnderecos?.next()}">
			<a href="#address${contadorEnderecos?.next()}" data-toggle="tab">${contadorEnderecos?.next()}</a>
		</li>
		</g:else>
		<% contadorEnderecos++ %>
	</g:while>								
	</ul>
			
	<g:set var="contadorEnderecos" value="${0}" />
		
	<div class="tab-content" id="address-list">
		<input type="hidden" id="count-address">
		<g:each status="i" in="${enderecos}" var="endereco">	
			<g:render template="/templatesCadastroUsuario/endereco" model='["tipoUsuario": "${tipoUsuario}", "estados": estados, "endereco": endereco, "enderecoIndice": "${i}", "enderecoAtivo": "X"]'/> 
			<% contadorEnderecos++ %>
		</g:each>
				
		<g:while test="${contadorEnderecos < 5}">
			<g:render template="/templatesCadastroUsuario/endereco" model='["tipoUsuario": "${tipoUsuario}", "estados": estados, "enderecoIndice": "${contadorEnderecos}", "enderecoAtivo": ""]'/> 
			<% contadorEnderecos++ %>
		</g:while>							
	</div>