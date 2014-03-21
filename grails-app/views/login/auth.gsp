<html>
<head>
<title>Login - üDOCTOR</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="core"/>
</head>
<body>
	<div id="breadcrumb">
		<div class="container">
			<h2>Login</h2>
			<div id="breadcrumb-info"></div>
		</div>
	</div>

	<form action="${postUrl}" method='POST' 
		  name="${postUrl}" class="container content">
		<div class="content-left">
			&nbsp;
		</div>	

		<div class="content-middle">
			<g:if test="${flash.message}">		
			<div class="alert alert-warning">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<g:if test='${flash.message}'>
				${flash.message}<br>
				</g:if>
			</div>
			</g:if>
			<g:hiddenField name='spring-security-redirect' value="${request.forwardURI.substring(8)}" />
			
			<label class="form-field">
				E-mail:<br>
				<input type='text' name='j_username' />
			</label>
			<label class="form-field">
				Senha:<br>
				<input type='password' name='j_password' />
			</label>
			
			<p class="password-resend"><g:link controller='senha' action="esqueciMinhaSenha"><i class="icon-lock"></i> <span>Esqueci minha senha</span></g:link></p>
			
			<label class="form-field">
				<input type='checkbox' name='${rememberMeParameter}' <g:if test='${hasCookie}'>checked='checked'</g:if>/>
				Mantenha-me conectado:<br>
			</label>
		</div>

		<div class="clearfix"></div>

		<div class="form-actions" style="text-align:right">				
			<button type="submit" class="btn btn-success"> Entrar</button>
		</div>
	</form>
</body>
</html>
