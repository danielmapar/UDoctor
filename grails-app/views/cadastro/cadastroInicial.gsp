<html>
<head>
<title>Cadastrar - üDOCTOR</title>
<meta name="layout" content="principal"/>
<parameter name="searchbarAtivo" value="X" /> 
<r:require module="core"/>
</head>
<body>
<g:set var="tab1" value='tab-pane fade'/>
<g:set var="tab2" value='tab-pane fade'/>
<g:set var="tab3" value='tab-pane fade'/>

	<div id="breadcrumb">
		<div class="container">
			<h2>Cadastrar</h2>
			<div id="breadcrumb-info">Selecione o tipo de perfil que deseja cadastrar</div>
		</div>
	</div>
	
	<div class="container content">
		<ul id="register-tabs" class="nav nav-tabs">		
			<g:if test="${profissional}">
			<li class="active">
			<g:set var="tab1" value='tab-pane active fade in'/>
			</g:if>
			<g:else>
			<li>
			</g:else>
				<a href="#tab1" data-toggle="tab">
					<i class="icon-briefcase"></i> Profissional
				</a>
			</li>
			<g:if test="${instituicao}">
			<li class="active">
			<g:set var="tab2" value='tab-pane active fade in'/>
			</g:if>
			<g:else>
			<li>
			</g:else>
				<a href="#tab2" data-toggle="tab">
					<i class="icon-plus-sign"></i> Centro médico
				</a>
			</li>
			<g:if test="${cliente || (!profissional && !instituicao)}">
			<li class="active">
			<g:set var="tab3" value='tab-pane active fade in'/>
			</g:if>
			<g:else>
			<li>
			</g:else>
				<a href="#tab3" data-toggle="tab">
					<i class="icon-user"></i> Pessoal
				</a>
			</li>
		</ul>

		<div class="tab-content">
			<div class="${tab1}" id="tab1">			
					<g:renderErrors bean="${profissional}"/>
					
					<g:form action="cadastroInicialProfissional" method="POST"
							name="cadastroInicialProfissional">
					
					<div class="content-left">
						<div class="avatar"><div class="doctor"></div></div>
					</div>
					
					<div class="content-middle profile-info">
						<label class="form-field pull-left">
							Nome:<br>
							<g:textField name="nome" id="nome" value="${profissional?.nome}"/>
						</label>
						
						<label class="form-field pull-right">
							Sobrenome:<br>
							<g:textField name="sobrenome" id="sobrenome" value="${profissional?.sobrenome}"/>
						</label>
			
						<label class="form-field pull-left">
							Email:<br>
							<g:textField name="email" id="email" value="${profissional?.email}"/>
						</label>
						
						<label class="form-field pull-right">
							Senha:<br>
							<g:passwordField name="senha" id="senha" value="" />
						</label>
						
						<div class="clearfix"></div>
						
						<label>
							<g:checkBox name="termos" /> Li os <a href="" target="_blank">Termos de uso</a> e aceito me cadastrar no üDOCTOR.
						</label>
					</div>
			
					<div class="clearfix"></div>
			
					<div class="form-actions" style="text-align:right">
						<button type="submit" class="btn btn-success btn-large"><i class="icon-ok icon-white"></i> Cadastrar grátis</button>
					</div>
				</g:form>
			</div>
				
			<div class="${tab2}" id="tab2">
					<g:renderErrors bean="${instituicao}"/>
				
					<g:form action="cadastroInicialInstituicao" method="POST"
							name="cadastroInicialInstituicao">
					
					<div class="content-left">
						<div class="avatar"><div class="center"></div></div>
					</div>
			
					<div class="content-middle profile-info">
					
						<label class="form-field pull-left">
							Nome:<br>
							<g:textField name="nome" id="nome" value="${instituicao?.nome}"/>
						</label>
			
						<label class="form-field pull-right">
							Email:<br>
							<g:textField name="email" id="email" value="${instituicao?.email}"/>
						</label>
						
						<label class="form-field pull-left">
							Senha:<br>
							<g:passwordField name="senha" id="senha" value="" />
						</label>
						
						<div class="clearfix"></div>
						
						<label>
							<g:checkBox name="termos" id="termos" /> Li os <a href="terms.php" target="_blank">Termos de uso</a> e aceito me cadastrar no üDOCTOR.
						</label>
					</div>
			
					<div class="clearfix"></div>
			
					<div class="form-actions" style="text-align:right">
						<button type="submit" class="btn btn-success btn-large"><i class="icon-ok icon-white"></i> Cadastrar grátis</button>
					</div>
				    </g:form>
			</div>
				
			<div class="${tab3}" id="tab3">
					<g:renderErrors bean="${cliente}"/>
				
					<g:form action="cadastroInicialCliente" method="POST" 
							name="cadastroInicialCliente">				

					<div class="content-left">
						<div class="avatar"><div class="user"></div></div>
					</div>
			
					<div class="content-middle profile-info">
					
						<label class="form-field pull-left">
							Nome:<br>
							<g:textField name="nome" id="nome" value="${cliente?.nome}"/>
						</label>
						
						<label class="form-field pull-right">
							Sobrenome:<br>
							<g:textField name="sobrenome" id="sobrenome" value="${cliente?.sobrenome}"/>
						</label>
			
						<label class="form-field pull-left">
							Email:<br>
							<g:textField name="email" id="email" value="${cliente?.email}"/>
						</label>
						
						<label class="form-field pull-right">
							Senha:<br>
							<g:passwordField name="senha" id="senha" value="" />
						</label>
						
						<div class="clearfix"></div>
						
						<label>
							<g:checkBox name="termos" id="termos" /> Li os <a href="/termos/" target="_blank">Termos de uso</a> e aceito me cadastrar no üDOCTOR.
						</label>
					</div>
			
					<div class="clearfix"></div>
					
					<div class="form-actions" style="text-align:right">			
						<button type="submit" class="btn btn-success btn-large"><i class="icon-ok icon-white"></i> Cadastrar grátis</button>
					</div>
				</g:form>
				</div>
		</div>
	</div>
</body>
</html>