<html>
<head>
<title>Profile de ${usuario?.nome}</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="core"/>
</head>
<body>	
	<div id="breadcrumb">
		<div class="container">
			<h2>Perfil</h2>
			<div id="breadcrumb-info">
				<g:link controller='pesquisa' action="index">${profissional.nomeProfissao}</g:link> <span>&raquo;</span> 
				${enderecos[0]?.nomeCidade} <span>&raquo;</span>
				${usuario?.nome} ${usuario?.sobrenome}
			</div>
		</div>
	</div>

	<div class="container content">
		<div id="profile-contact" class="content-left">
			<div class="avatar">
                <g:if test="${avatar}">
                    <img src="${g.resource( dir: grailsApplication.config.arquivo.diretorio.nome.avatar ?: 'avatar', file:"${avatar.nomeThumbnail}") }" />
                </g:if>
                <g:else>
                    <div class="doctor"></div>
                </g:else>
			</div>

			<dl>
				<dt><i class="icon-plus-sign"></i> Plano(s) de saúde:</dt>
				<dd>
					<g:each status="i" in="${planosDeSaude}" var="planoDeSaude"> 
						${planoDeSaude} <g:if test="${i.next() < planosDeSaude?.size()}">,</g:if>
					</g:each>
				</dd>
	
				<dt><i class="icon-phone"></i> Telefone(s):</dt>
				<dd>
					<g:each status="i" in="${telefones}" var="telefone"> 
						${telefone?.numeroCompleto} <g:if test="${i.next() < telefones?.size()}">,</g:if>
					</g:each>
				</dd>
	
				<dt><i class="icon-map-marker"></i> Endereço(s):</dt>
				<dd>
					<g:each in="${enderecos}" var="endereco"> 
						${endereco?.logradouro}, ${endereco?.complemento} - ${endereco?.bairro}<br>
						${endereco?.nomeCidade} - ${endereco?.nomeEstado}<br>
						<span>&raquo;</span> <a class="map" href="https://maps.google.com/" target="_blank">Ver mapa</a>
					</g:each>
				</dd>
	
				<dt><i class="icon-globe"></i> Link(s):</dt>
				<dd>
					<g:each in="${links}" var="link"> 
						<span>&raquo;</span> <a href="${link?.nome}" target="_blank">${link?.descricaoLink}</a><br>
					</g:each>
				</dd>
			</dl>
		</div>

		<div class="content-middle profile-info">
			<h1>${usuario?.nome} ${usuario?.sobrenome}</h1>
			<h2>${profissional.nomeProfissao}</h2>
			<h3>CRM ${profissional?.codigoConselhoRegional}</h3>

			<div class="form-actions">
				<g:if test="${usuario?.email == (sec.loggedInUserInfo(field:'username').toString())}">
					<a id="profile-edit-btn" href="${createLink(controller: 'cadastro', action: 'cadastroAdicionalProfissional')}" class="btn" title="Editar perfil">
						<i class="icon-pencil"></i>
					</a>
				</g:if>
				<g:else>
					<a href="#" class="btn btn-info btn-small pull-right">
						<i class="icon-thumbs-up icon-white"></i> Recomendar este profissional
					</a>
				</g:else>
			</div>
			
			<h4><i class="icon-check"></i> Especialidade(s):</h4>
			<div class="info">${profissional?.especialidade}</div>

			<h4><i class="icon-certificate"></i> Patologia(s):</h4>
			<div class="info">${profissional?.patologia}</div>

			<h4><i class="icon-list-alt"></i> Currículo:</h4>
			<div class="info">${profissional?.curriculo}</div>
		</div>

		<g:render template="/templatesLayout/ads" />
	</div>
</body>
</html>