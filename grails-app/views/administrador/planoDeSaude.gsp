<%@page import="br.com.udoctor.tipo.Enum.StatusType"%>
<html>
<head>
<title>Listagem de planos de saude - üDOCTOR</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="app"/>
</head>
<body>
<script language="Javascript">
<!--
function AtivarPlanoDeSaude()
{
    document.statusPlanosDeSaudeFormulario.action = "planosDeSaude/ativar"
    document.statusPlanosDeSaudeFormulario.submit();             
    return true;
}

function DesativarPlanoDeSaude()
{
    document.statusPlanosDeSaudeFormulario.action = "planosDeSaude/desativar"
    document.statusPlanosDeSaudeFormulario.submit();            
    return true;
}
-->
</script>
<noscript>Voce precisa do Javascript ativo para essa pagina funcionar</noscript>
	<div id="breadcrumb">
		<div class="container">
			<h2>Admin</h2>
			<div id="breadcrumb-info">Listagem de planos de saúde <span>&raquo;</span> Aproximadamente ${quantidadeEntradas} resultado(s)</div>
		</div>
	</div>

	<div class="container content">
		<g:form action="filtrar" method="POST"	name="atualizarListagemPlanosDeSaudeFormulario">
			<div id="left-fields" class="content-left" >
			<p><i class="icon-map-marker"></i> Planos de Saúde:</p>
			<g:render template="/templatesAdministracao/planosDeSaude" model="['planosDeSaude': planosDeSaude, 'nome':'planoDeSaudeSelecionado', 'planoDeSaudeSelecionado': planoDeSaudeSelecionado]"/> 

			<button type="submit" class="btn btn-mini btn-success"><i class="icon-refresh icon-white"></i> Redefinir</button>
			</div>
		</g:form>

		<g:form method="POST" name="statusPlanosDeSaudeFormulario">
		
		<input type="hidden" name="max" value="${params.max}">
		<input type="hidden" name="offset" value="${params.offset}">
		<input type="hidden" name="planoDeSaudeSelecionado" value="${planoDeSaudeSelecionado?.id}">
		
		<div id="admin-listing" class="content-middle">
			<div class="form-actions">
				<div class="pull-left">
				
					<button type="submit" class="btn btn-warning" title="Desativar" onclick="return DesativarPlanoDeSaude();"><i class="icon-eye-close icon-white"></i></button>
					<button type="submit" class="btn btn-warning" title="Ativar" onclick="return AtivarPlanoDeSaude();"><i class="icon-eye-open icon-white"></i></button>
					
					<div class="input-append">
						<input class="span2" id="appendedInputButton" size="16" type="text"><button class="btn" type="button" title="Pesquisar"><i class="icon-search"></i></button>
					</div>
				</div>
				
				<div class="pull-right">
					<a href="#" class="btn" title="Inserir plano de saúde"><i class="icon-plus"></i> <i class="icon-map-marker"></i></a>
				</div>
			</div>
			
			<table class="table">
				<thead>
					<tr>
						<th style="width:20px">&nbsp;</th>
						<th>Plano de Saúde</th>
						<th class="center-cell">Status</th>
						<th class="center-cell">Editar</th>
					</tr>
				</thead>
				<tbody>
					<g:set var="id" value="${0}" />
					<g:each in="${planosDeSaudePagina}" var="planoDeSaude">
					<tr>
						<input type="hidden" name="planosDeSaude[${id}].id" value="${planoDeSaude?.id}">
						<td><input name="planosDeSaude[${id}].ativo" type="checkbox"></td>
						<td>${planoDeSaude?.nome}</td>
						<td class="center-cell"><a href="#"><g:if test="${planoDeSaude?.status == StatusType.ATIVO}"><i class="icon-eye-open" title="Ativo"></i></g:if><g:else><i class="icon-eye-close" title="Inativo"></i></g:else></a></td>
						<td class="center-cell"><a href="edit-personal.php"><i class="icon-pencil" title="Editar"></i></a></td>		
						<% id++ %>	
					</tr>
					</g:each>
				</tbody>
			</table>
			
			<ul class="pagination">
				<g:paginate next="Proximo" prev="Anterior" max="20" controller="planoDeSaude" action="listagem" total="${quantidadeEntradas}" params="['planoDeSaudeSelecionado': planoDeSaudeSelecionado?.id]" />
			</ul>
		</div>
		</g:form>
	</div>
</body>
</html>