<g:set var="titulo" value="Nome e Sobrenome do usuário - üDOCTOR" />
<g:render template="/templatesPadrao/config" model="['titulo':titulo]" />
<g:render template="/templatesPadrao/header" model="['searchbarAtivo':true]"/>

	<div id="breadcrumb">
		<div class="container">
			<h2>Pesquisar</h2>
			<div id="breadcrumb-info">Página 1 de aproximadamente 50 resultado(s)</div>
		</div>
	</div>

	<div class="container content">
		<form action="redefinirPesquisa" id="left-fields" class="content-left" method="POST">
		
			<p><i class="icon-briefcase"></i> Profissional: <a class="help" href="contact.php" title="Profissional:" data-content="Não encontrou o profissional que você precisa? <strong>Clique e faça uma sugestão</strong>.">[?]</a></p>
			<g:render template="/templatesCampos/profissoes" model="['profissoes': profissoes, 'Sprofissao': null]"/> 

			<p><i class="icon-map-marker"></i> Cidade: <a class="help" href="contact.php" title="Cidade:" data-content="Não encontrou sua cidade? <strong>Clique e faça uma sugestão</strong>.">[?]</a></p>			
			<select name="cidade" id="cidade" data-placeholder="Selecione..." class="chzn-select" tabindex="">
				<option value=""></option>
					<g:each in="${localizacaoMap}" var="localizacao">
						<optgroup label="${localizacao.key?.nome}">
							<g:each in="${localizacao.value}" var="cidade">
								<option 
								<g:if test="${cidade?.nome == endereco?.cidade & localizacao.key?.nome == endereco?.estado}">
								selected
								</g:if>
								value="${cidade?.nome};${localizacao.key?.nome}">
									${cidade?.nome}
								</option>
							</g:each>
						</optgroup>
					</g:each>
			</select>

			<p><i class="icon-plus-sign"></i> Plano(s) de saúde: <a class="help" href="contact.php" title="Plano de saúde:" data-content="Não encontrou seu plano de saúde? <strong>Clique e faça uma sugestão</strong>.">[?]</a></p>
			<g:render template="/templatesCampos/planosDeSaude" model="['planosDeSaude': planosDeSaude, 'SplanosDeSaude': null]"/> 

			<button type="submit" class="btn btn-mini btn-success"><i class="icon-refresh icon-white"></i> Redefinir</button>
		</form>

		<div id="search-results" class="content-middle">
			<h6><i class="icon-chevron-up icon-white"></i> Destaques</h6>
			<ul id="search-ads">
			<g:each var="resultado" in="${resultadosBusca?.results}">
				<li>
					<dl>
						<dt><span>&raquo;</span> <a href="profile-center.php">${resultado?.nome} ${resultado?.sobrenome}</a></dt>
						<dd><i class="icon-check"></i> <strong>Especialidade(s):</strong> Lorem ipsum</dd>
						<dd><i class="icon-plus-sign"></i> <strong>Plano(s) de saúde:</strong> Unimed</dd>
						<dd><i class="icon-map-marker"></i> <strong>Endereço:</strong> Logradouro, Complemento - Bairro</dd>
						<dd style="padding-left:28px">Cidade - Estado</dd>
					</dl>
				</li>
			</g:each>
			</ul>
			<ul id="search-organic">

				<li>
					<dl>
						<dt><a href="profile-doctor.php">Nome e Sobrenome do profissional</a></dt>
						<dd><i class="icon-briefcase"></i> <strong>Profissão</strong> - Código do conselho</dd>
						<dd><i class="icon-check"></i> <strong>Especialidade(s):</strong> Lorem ipsum</dd>
						<dd><i class="icon-plus-sign"></i> <strong>Plano(s) de saúde:</strong> Unimed</dd>
						<dd><i class="icon-map-marker"></i> <strong>Endereço:</strong> Logradouro, Complemento - Bairro</dd>
						<dd style="padding-left:18px">Cidade - Estado</dd>
					</dl>
				</li>

			</ul>

			<ul class="pagination">
				<li class="previous"><a href="#" title="Anterior"><i class="icon-chevron-left"></i></a></li>
				<li class="active"><a href="#">1</a></li>
				<li class="active"><a href="#">2</a></li>
				<li class="active"><a href="#">3</a></li>
				<li class="active"><a href="#">4</a></li>
				<li class="active"><a href="#">5</a></li>
				<li class="next"><a href="#" title="Próxima"><i class="icon-chevron-right"></i></a></li>
			</ul>
		</div>

<g:render template="/templatesPadrao/ads" />
	</div>

<g:render template="/templatesPadrao/footer" />