<html>
<head>
<title>üDOCTOR - Encontre o médico que você precisa</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="" /> 
<r:require module="core"/>
</head>
<body>
	<br><br>

	<div id="index" class="container content">

		<g:if test="${mensagem}">
		<div class="alert alert-success">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			${mensagem}<br>
		</div>
		</g:if>
		<g:elseif test="${erro}">
		<div class="alert alert-warning">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			${erro}<br>
		</div>
		</g:elseif>
		<h1>Encontre o médico que você precisa</h1>
		<g:form action="index" method="POST">
			<label class="search pull-left">
				<input type="text" name="q" id="q" placeholder="Ex: Dentista, Cardiologista, Fisioterapeuta, Nutricionista...">
				<i id="location" class="icon-map-marker" title="Localização:" data-content="<strong>Clique no ícone</strong> para definir sua localização automaticamente."></i>
			</label>
			<button type="submit" class="btn btn-large btn-success pull-right"><i class="icon-search icon-white"></i></button>
			<div class="clearfix"></div>
		</g:form>
		<sec:ifNotLoggedIn>
		 <a href="${createLink(controller: 'cadastro', action: 'cadastroInicial')}" class="btn btn-info"><i class="icon-ok icon-white"></i> Cadastrar grátis</a></button>	
		</sec:ifNotLoggedIn>
	</div>
</body>
</html>