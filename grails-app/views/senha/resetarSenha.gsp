<html>
<head>
<title>Alterar senha - üDOCTOR</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="core"/>
</head>
<body>
	<div id="breadcrumb">
		<div class="container">
			<h2>Senha</h2>
			<div id="breadcrumb-info"><i class="icon-lock"></i> Alterar senha</div>
		</div>
	</div>
	
	<g:if test="${resetarSenha}">
	<div class="alert alert-warning">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<g:renderErrors bean="${resetarSenha}"/>
	</div>
	</g:if>

	<g:form action="resetarSenha" method="POST"
			name="resetarSenha" class="container content">
		
		<g:hiddenField name='id' value='${token}'/>
		
		<div id="profile-contact" class="content-left">
			&nbsp;
		</div>

		<div id="profile-info" class="content-middle">
			<label class="form-field">
			Digite a nova senha:<br>
			<g:passwordField name="senha" id="senha" value=""/><br>
			</label>
			
			<label class="form-field">
			Confirme a nova senha:<br>
			<g:passwordField name="senha2" id="senha2" value=""/><br>
			</label>
		</div>

		<div class="clearfix"></div>

		<div class="form-actions" style="text-align:right">				
			<button type="submit" class="btn btn-success">Alterar</button>
		</div>
	</g:form> 
</body>
</html>