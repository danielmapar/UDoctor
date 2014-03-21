			<%@page import="br.com.udoctor.tipo.Enum.UsuarioType"%>
			
			<g:if test="${tipoUsuario.toString() != UsuarioType.CLIENTE.toString() && enderecoIndice > 0}">
			<div class="tab-pane fade" id="address${enderecoIndice.next()}">	
				<input type="hidden" class="addressId" value="${enderecoIndice.next()}"> <!-- 2 a 5 -->
				<i class="icon-remove pull-right" title="Remover endereço"></i>
				<input type="hidden" class="status" id="enderecos[${enderecoIndice}].ativo" name="enderecos[${enderecoIndice}].ativo" value="${enderecoAtivo}">
			</g:if>
	
			<g:if test="${tipoUsuario.toString() != UsuarioType.CLIENTE.toString() && enderecoIndice <= 0}">
			<div class="tab-pane fade in active" id="address${enderecoIndice.next()}">
			<input type="hidden" class="status" id="enderecos[0].ativo" name="enderecos[0].ativo" value="X">
			</g:if>
	
			<p>CEP:</p>
			<g:textField name="enderecos[${enderecoIndice}].cep" class="cep-field" value="${endereco?.cep}" onblur="getEndereco(${enderecoIndice})" />
		    
			<p>Logradouro:</p>
			<g:textField name="enderecos[${enderecoIndice}].logradouro" value="${endereco?.logradouro}" />
			
			<p>Número / Complemento:</p>
			<g:textField name="enderecos[${enderecoIndice}].complemento" value="${endereco?.complemento}" />
			
			<p>Bairro:</p>
			<g:textField name="enderecos[${enderecoIndice}].bairro" value="${endereco?.bairro}"/>

			<p>Cidade: 
				<a class="help" href="" title="Cidade:" data-content="Não encontrou sua cidade? <strong>Clique e faça uma sugestão</strong>.">[?]</a>
			</p>
			<g:render template="/templatesCadastroUsuario/cidade" model='["nome": "enderecos[${enderecoIndice}].cidade", "id": "enderecos${enderecoIndice}cidade", "estados" : estados, "cidadeSelecionada": endereco?.cidade, "estadoSelecionado": endereco?.estado]'/> 
			
			<g:if test="${tipoUsuario.toString() != UsuarioType.CLIENTE.toString()}">
			<p>Descreva este endereço:</p>
			<g:textField name="enderecos[${enderecoIndice}].descricaoLocal" value="${endereco?.descricaoLocal}" placeholder="Ex.: Consultório" />	
			</g:if>
			
			<g:if test="${tipoUsuario.toString() != UsuarioType.CLIENTE.toString()}">
			</div>
			</g:if>
