<%@page import="br.com.udoctor.tipo.Enum.StatusType"%>
<html>
<head>
<title>Listagem de estados - üDOCTOR</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="app"/>
</head>
<body>
<script language="Javascript">
<!--
function AtivarEstado()
{
    document.statusEstadosFormulario.action = "estados/ativar"
    document.statusEstadosFormulario.submit();             
    return true;
}

function DesativarEstado()
{
    document.statusEstadosFormulario.action = "estados/desativar"
    document.statusEstadosFormulario.submit();            
    return true;
}
-->
</script>
<noscript>Voce precisa do Javascript ativo para essa pagina funcionar</noscript>
	<div id="breadcrumb">
		<div class="container">
			<h2>Admin</h2>
			<div id="breadcrumb-info">Listagem de estados <span>&raquo;</span> Aproximadamente ${quantidadeEntradas} resultado(s)</div>
		</div>
	</div>

	<div class="container content">
		<g:form action="filtrar" method="POST"	name="atualizarListagemEstadosFormulario">
			<div id="left-fields" class="content-left" >
			<p><i class="icon-map-marker"></i> Estado:</p>
			<g:render template="/templatesAdministracao/estados" model="['estados': estados, 'nome':'estadoSelecionado', 'estadoSelecionado': estadoSelecionado ]"/> 

			<button type="submit" class="btn btn-mini btn-success"><i class="icon-refresh icon-white"></i> Redefinir</button>
			</div>
		</g:form>

		<g:form method="POST" name="statusEstadosFormulario">
		
		<input type="hidden" name="max" value="${params.max}">
		<input type="hidden" name="offset" value="${params.offset}">
		<input type="hidden" name="estadoSelecionado" value="${estadoSelecionado?.id}">
		
		<div id="admin-listing" class="content-middle">
			<div class="form-actions">
				<div class="pull-left">
				
					<button type="submit" class="btn btn-warning" title="Desativar" onclick="return DesativarEstado();"><i class="icon-eye-close icon-white"></i></button>
					<button type="submit" class="btn btn-warning" title="Ativar" onclick="return AtivarEstado();"><i class="icon-eye-open icon-white"></i></button>
					
					<div class="input-append">
						<input class="span2" id="appendedInputButton" size="16" type="text"><button class="btn" type="button" title="Pesquisar"><i class="icon-search"></i></button>
					</div>
				</div>
				
				<div class="pull-right">
					<a href="#" class="btn" title="Inserir estado"><i class="icon-plus"></i> <i class="icon-map-marker"></i></a>
				</div>
			</div>
			
			<table class="table">
				<thead>
					<tr>
						<th style="width:20px">&nbsp;</th>
						<th>Estado</th>
						<th class="center-cell">Status</th>
						<th class="center-cell">Editar</th>
					</tr>
				</thead>
				<tbody>
					<g:set var="id" value="${0}" />
					<g:each in="${estadosPagina}" var="estado">

					<tr>
						<input type="hidden" name="estados[${id}].id" value="${estado.id}">
						<td><input name="estados[${id}].ativo" type="checkbox"></td>
						<td>${estado?.nome}</td>
						<td class="center-cell"><a href="#"><g:if test="${estado?.status == StatusType.ATIVO}"><i class="icon-eye-open" title="Ativo"></i></g:if><g:else><i class="icon-eye-close" title="Inativo"></i></g:else></a></td>
						<td class="center-cell"><a href="edit-personal.php"><i class="icon-pencil" title="Editar"></i></a></td>		
						<% id++ %>	
					</tr>

					</g:each>
				</tbody>
			</table>
			
			<ul class="pagination">
				<g:paginate next="Proximo" prev="Anterior" max="10" controller="estado" action="listagem" total="${quantidadeEntradas}" params="['estadoSelecionado': estadoSelecionado?.id]" />
			</ul>
		</div>
		</g:form>
	</div>
</body>
</html>