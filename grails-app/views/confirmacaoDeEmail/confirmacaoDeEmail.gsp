<html>
<head>
<title>Confirmação de e-mail</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="core"/>
</head>
<body>
	<div id="index" class="container content">
	<g:if test="${flash.message}">
	${flash.message}
	</g:if>
	<g:elseif test="${flash.error}">
	${flash.error}
	</g:elseif>
	</div>
</body>
</html>