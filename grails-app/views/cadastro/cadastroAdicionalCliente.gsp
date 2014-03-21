<%@page import="br.com.udoctor.tipo.Enum.UsuarioType"%>
<html>
<head>
<title>${usuario?.nome} ${usuario?.sobrenome} - üDOCTOR</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="core"/>
<r:script>
    $(document).ready(function() {
        $('#dataDeNascimento').datepicker({
            format: "dd/mm/yyyy",
            autoclose: true,
            language:"pt-BR",
            weekStart: 1
        });
    });
</r:script >
</head>
<body>
	<div id="breadcrumb">
		<div class="container">
			<h2>Editar perfil</h2>
			<div id="breadcrumb-info"><i class="icon-user"></i> Pessoal</div>
		</div>
	</div>

    <g:if test="${validacaoAvatar != null}">
    <div class="alert alert-warning">
       <button type="button" class="close" data-dismiss="alert">&times;</button>
       ${validacaoAvatar}
    </div>
    </g:if>

	<g:hasErrors beans='[usuario:"${usuario}", cliente:"${cliente}", endereco:"${endereco}"]'>
	<div class="alert alert-warning">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
    	<g:renderErrors beans='[usuario:"${usuario}", cliente:"${cliente}", endereco:"${endereco}"]' as="list" />
    </div>
	</g:hasErrors>
	    
	<g:if test="${salvoComSucessoCA == true}">
	<div class="alert alert-success">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		Profile atualizado com sucesso!<br>
	</div>
	</g:if>
	
	<g:if test="${salvoComSucessoCI == true}">
	<div class="alert alert-success">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		Cadastro inicial realizado com sucesso!!<br>
	</div>
	</g:if>
	
	<g:form action="cadastroAdicionalCliente" method="POST"
			name="cadastroAdicionalCliente" class="container content" enctype="multipart/form-data">
			
		<div id="left-fields" class="content-left">
		
            <g:set var="usuarioComAvatar" value="${avatar != null}" />
			<g:render template="/templatesCadastroUsuario/avatar" model="['usuarioComAvatar': usuarioComAvatar, 'tipoUsuario': UsuarioType.CLIENTE, 'templateAvatar': 'avatar-user.png', 'avatar': avatar]"/> 
			
			<h5>
				<i class="icon-plus-sign"></i> Plano(s) de saúde: 
				<a class="help" href="#" title="Plano de saúde:" data-content="Faltou algum plano de saúde? <strong>Clique e faça uma sugestão</strong>.">[?]</a>
			</h5>		
			<g:render template="/templatesCadastroUsuario/planosDeSaude" model="['planosDeSaude': planosDeSaude, 'planosDeSaudeUtilizados': planosDeSaudeUtilizados]"/> 
			
			<h5>
				<i class="icon-map-marker"></i> Localização padrão:
				<a class="help" href="#" title="Localização:" data-content="Otimize suas pesquisas digitando sua localização padrão.">[?]</a>
			</h5>
			<g:render template="/templatesCadastroUsuario/endereco" model='["tipoUsuario": "${UsuarioType.CLIENTE}", "estados": estados, "endereco": endereco, "enderecoIndice": "${0}", "enderecoAtivo": "X"]'/> 
		</div>

		<div class="content-middle profile-info">
			<h1>Dados pessoais</h1>
			
			<label class="form-field pull-left">
				Nome:<br>
				<g:textField name="nome" id="nome" value="${usuario?.nome}"/>
			</label>

			<label class="form-field pull-right">
				Sobrenome:<br>
				<g:textField name="sobrenome" id="sobrenome" value="${usuario?.sobrenome}"/>
			</label>
			
			<label class="form-field pull-left">
				E-mail:<br>
				<g:textField name="email" id="email" value="${usuario?.email}"/>
			</label>
			
			<label class="form-field pull-right">
				Gênero:<br>
				<g:render template="/templatesCadastroUsuario/genero" model="['genero': cliente?.genero]"/> 
			</label>
			
			<div class="clearfix"></div>
			
			<label class="profile-field pull-left">
				Data de nascimento:<br>
				<g:render template="/templatesCadastroUsuario/dataDeNascimento" model="['dataDeNascimento': cliente?.dataDeNascimento]"/> 
			</label>
		</div>

		<div class="clearfix"></div>

		<div class="form-actions" style="text-align:right">				
			<button type="submit" class="btn btn-success"> Salvar</button>
			<g:link controller='pesquisa' action="index" class="btn">Cancelar</g:link>
		</div>
	</g:form>
</body>
</html>