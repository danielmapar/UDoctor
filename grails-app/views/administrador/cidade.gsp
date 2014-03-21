<%@page import="br.com.udoctor.tipo.Enum.StatusType"%>
<html>
<head>
<title>Listagem de cidades - üDOCTOR</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="core"/>
</head>
<body>	
	<div id="breadcrumb">
		<div class="container">
			<h2>Admin</h2>
			<div id="breadcrumb-info">Listagem de cidades <span>&raquo;</span> Aproximadamente ${quantidadeDeCidades} resultado(s)</div>
		</div>
	</div>

	<div class="container content">
		<g:form action="cidade" method="POST" name="cidade">
			<g:hiddenField name="offset" value="${params.offset}"/>
			<g:hiddenField name="max" value="${params.max}"/>
		
			<div id="left-fields" class="content-left" >
			<p><i class="icon-map-marker"></i> Estados:</p>
			<g:render template="/templatesAdministracao/estados" model="['estados': estadosFiltro, 'nome':'estadoSelecionado', 'estadoSelecionado': estadoSelecionado ]"/> 
			<button type="submit" class="btn btn-mini btn-success"><i class="icon-refresh icon-white"></i> Redefinir</button>
			</div>
					
			<div id="admin-listing" class="content-middle">
				<div class="form-actions">
					<div class="pull-left">
					<button name="ativar" value="ativar" class="btn btn-warning" title="Ativar"><i class="icon-eye-open icon-white"></i></button>
					<button name="desativar" value="desativar" class="btn btn-warning" title="Desativar"><i class="icon-eye-close icon-white"></i></button>
				
					<div class="input-append">
						<input class="span2" id="appendedInputButton" size="16" type="text"><button class="btn" type="button" title="Pesquisar"><i class="icon-search"></i></button>
					</div>
				</div>
				
				<div class="pull-right">
					<a href=<g:createLink controller="administrador" action="cadastroCidade"/> class="btn" title="Inserir cidade"><i class="icon-plus"></i> <i class="icon-map-marker"></i></a>
				</div>
			</div>
			
			<table class="table">
				<thead>
					<tr>
						<th style="width:20px">&nbsp;</th>
						<th>Estado</th>
						<th>Cidade</th>
						<th class="center-cell">Status</th>
						<th class="center-cell">Editar</th>
					</tr>
				</thead>
				<tbody>
					<g:set var="id" value="${0}" />
					<g:each in="${estadosPagina}" var="estado">
						<g:each in="${estado?.cidades}" var="cidade">
					<tr>
						<input type="hidden" name="enderecos[${id}].cidades" value="${estado.id};${cidade?.id}">
						<td><input name="enderecos[${id}].ativo" type="checkbox"></td>
						<td>${estado?.nome}</td>
						<td>${cidade?.nome}</td>
						<td class="center-cell"><a href="#"><g:if test="${estado?.status == StatusType.ATIVO && cidade?.status == StatusType.ATIVO}"><i class="icon-eye-open" title="Ativo"></i></g:if><g:else><i class="icon-eye-close" title="Inativo"></i></g:else></a></td>
						<td class="center-cell"><a href="edit-personal.php"><i class="icon-pencil" title="Editar"></i></a></td>		
						<% id++ %>	
					</tr>
						</g:each>
					</g:each>
				</tbody>
			</table>
			<g:paginacao next="Proximo" max="10" controller="administrador" action="cidade" params='["estadoSelecionado": "${estadoSelecionado}"]' total="${quantidadeDeCidades}" />
		</div>
		</g:form>
	</div>
</body>
</html>