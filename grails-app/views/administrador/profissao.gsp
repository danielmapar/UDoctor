<%@page import="br.com.udoctor.tipo.Enum.StatusType"%>
<html>
<head>
<title>Listagem de profissões - üDOCTOR</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="app"/>
</head>
<body>
<script language="Javascript">
<!--
function AtivarProfissao()
{
    document.statusProfissoesFormulario.action = "profissoes/ativar"
    document.statusProfissoesFormulario.submit();             
    return true;
}

function DesativarProfissao()
{
    document.statusProfissoesFormulario.action = "profissoes/desativar"
    document.statusProfissoesFormulario.submit();            
    return true;
}
-->
</script>
<noscript>Voce precisa do Javascript ativo para essa pagina funcionar</noscript>
	<div id="breadcrumb">
		<div class="container">
			<h2>Admin</h2>
			<div id="breadcrumb-info">Listagem de profissoes <span>&raquo;</span> Aproximadamente ${quantidadeEntradas} resultado(s)</div>
		</div>
	</div>

	<div class="container content">
		<g:form action="filtrar" method="POST" name="atualizarListagemProfissoesFormulario">
			<div id="left-fields" class="content-left" >
			<p><i class="icon-map-marker"></i> Profissões:</p>
			<g:render template="/templatesAdministracao/profissoes" model="['profissoes': profissoes, 'nome':'profissaoSelecionada', 'profissaoSelecionada': profissaoSelecionada]"/> 

			<button type="submit" class="btn btn-mini btn-success"><i class="icon-refresh icon-white"></i> Redefinir</button>
			</div>
		</g:form>

		<g:form method="POST" name="statusProfissoesFormulario">
		
		<input type="hidden" name="max" value="${params.max}">
		<input type="hidden" name="offset" value="${params.offset}">
		<input type="hidden" name="profissaoSelecionada" value="${profissaoSelecionada?.id}">
		
		<div id="admin-listing" class="content-middle">
			<div class="form-actions">
				<div class="pull-left">
				
					<button type="submit" class="btn btn-warning" title="Desativar" onclick="return DesativarProfissao();"><i class="icon-eye-close icon-white"></i></button>
					<button type="submit" class="btn btn-warning" title="Ativar" onclick="return AtivarProfissao();"><i class="icon-eye-open icon-white"></i></button>
					
					<div class="input-append">
						<input class="span2" id="appendedInputButton" size="16" type="text"><button class="btn" type="button" title="Pesquisar"><i class="icon-search"></i></button>
					</div>
				</div>
				
				<div class="pull-right">
					<a href="#" class="btn" title="Inserir profissão"><i class="icon-plus"></i> <i class="icon-map-marker"></i></a>
				</div>
			</div>
			
			<table class="table">
				<thead>
					<tr>
						<th style="width:20px">&nbsp;</th>
						<th>Profissão</th>
						<th class="center-cell">Status</th>
						<th class="center-cell">Editar</th>
					</tr>
				</thead>
				<tbody>
					<g:set var="id" value="${0}" />
					<g:each in="${profissoesPagina}" var="profissao">
					<tr>
						<input type="hidden" name="profissoes[${id}].id" value="${profissao?.id}">
						<td><input name="profissoes[${id}].ativo" type="checkbox"></td>
						<td>${profissao?.nome}</td>
						<td class="center-cell"><a href="#"><g:if test="${profissao?.status == StatusType.ATIVO}"><i class="icon-eye-open" title="Ativo"></i></g:if><g:else><i class="icon-eye-close" title="Inativo"></i></g:else></a></td>
						<td class="center-cell"><a href="edit-personal.php"><i class="icon-pencil" title="Editar"></i></a></td>		
						<% id++ %>	
					</tr>
					</g:each>
				</tbody>
			</table>
			
			<ul class="pagination">
				<g:paginate next="Proximo" prev="Anterior" max="20" controller="profissao" action="listagem" total="${quantidadeEntradas}" params="['profissaoSelecionada': profissaoSelecionada?.id]" />
			</ul>
		</div>
		</g:form>
	</div>
</body>
</html>