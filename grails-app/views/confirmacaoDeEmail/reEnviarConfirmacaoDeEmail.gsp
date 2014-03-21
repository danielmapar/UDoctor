<html>
<head>
<title>Reenviar senha - üDOCTOR</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="core"/>
</head>
<body>
	<div id="breadcrumb">
		<div class="container">
			<h2>Login</h2>
			<div id="breadcrumb-info">Valide sua conta</div>
		</div>
	</div>
	
	<g:if test="${flash.message}">
	<div class="alert alert-success">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		${flash.message}<br>
	</div>
	</g:if>
	<g:elseif test="${flash.error}">
	<div class="alert alert-warning">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		${flash.error}<br>
	</div>
	</g:elseif>
	
	<g:form action="reEnviarConfirmacaoDeEmail" method="POST"
		name="reEnviarConfirmacaoDeEmail" class="container content">
	
		<div class="content-left">
			&nbsp;
		</div>

		<div class="content-middle">
			<h1>Não recebeu o e-mail de validação?</h1>
			<br>

			<label class="form-field">
				Digite seu e-mail:<br>
				<g:textField name="email" id="email" value="${email}"/>
			</label>
		</div>

		<div class="clearfix"></div>

		<div class="form-actions" style="text-align:right">
			<button type="submit" class="btn btn-success"> Enviar</button>
		</div>
	</g:form>
</body>
</html>