	<%@ page import="br.com.udoctor.tipo.Enum.UsuarioType"%>
	<%@ page import="br.com.udoctor.tipo.Enum.StatusType"%>
	<div id="header">
		<div class="container navbar">
			<!-- início - search-bar -->
			<g:if test="${searchbarAtivo}">
			<h1 id="logo"><g:link controller='pesquisa' action="index"/></h1>
			
			<!-- início - Em construção -->
			<form id="header-search" action="results.php">
				<label class="search pull-left">
					<input type="text" placeholder="Encontre o médico que você precisa">
					<i id="location" class="icon-map-marker" title="Localização:" data-content="<strong>Clique</strong> para definir sua localização automaticamente."></i>
				</label>
				<button type="submit" class="btn btn-large btn-success pull-left"><i class="icon-search icon-white"></i></button>
				<div id="loca"></div>
			</form>
			<!-- final - Em construção -->
			</g:if>
			<!-- final - search-bar -->
			
			<ul id="header-options" class="nav pull-right">
			<!-- início - nao-loggado -->
			<sec:ifNotLoggedIn>
				<li id="option-register" class="dropdown">
					<g:link	controller='cadastro' action="cadastroInicial" class="dropdown-toggle">Cadastrar grátis</g:link>
				</li>
				
				<li id="option-login" class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						Fazer login 
						<b class="caret"></b>
					</a> 
					<form action='/udoctor/j_spring_security_check' method='POST'
						  name="loginFormulario" class="dropdown-menu pull-right">
							<g:hiddenField name='spring-security-redirect' value="${request.forwardURI.substring(8)}" />
							<g:textField name='j_username' placeholder="E-mail" />
							<g:passwordField name='j_password' placeholder="Senha" />
							<label>		
							    <g:checkBox name='_spring_security_remember_me'/>Mantenha-me conectado
							</label>
							<g:link controller='senha' action="esqueciMinhaSenha" class="pull-left">
								Esqueci minha senha
							</g:link>
							<button type="submit" class="btn btn-success btn-small pull-right">
								Entrar
							</button>
					</form>
				</li>
				
				<li id="option-share" class="dropdown">
					<a href="#"	title="Recomendar para um amigo" class="dropdown-toggle" data-toggle="dropdown">
						<i></i>
					</a> 
					
					<g:formRemote url="[controller:'indicar', action:'indicarAmigo']" method='POST' update="atualizarDeslogado"
								  name="recomendarDeslogadoFormulario" class="dropdown-menu pull-right">
						
						<div id="atualizarDeslogado">
    						<g:render template="/templatesLayout/updateTemplate"/>
						</div>
						
						<g:textField name='nome' placeholder="Seu nome" />
						<g:textField name='email' placeholder="Seu e-mail" />
						<g:textField name='nomeAmigo' placeholder="Nome do seu amigo" />
						<g:textField name='emailAmigo' placeholder="E-mail do seu amigo" />
						<button type="submit" class="btn btn-success btn-small pull-right">Recomendar</button>
					</g:formRemote>					
				</li>
			</sec:ifNotLoggedIn>
			<!-- final - nao-loggado -->
			
			<!-- início - loggado -->
			<sec:ifLoggedIn>
				<li id="option-login" class="dropdown">
					<a href="" title=<sec:username/> class="dropdown-toggle" data-toggle="dropdown"> 
						<i></i>
						<b class="caret"></b>
					</a>
					<ul class="dropdown-menu pull-right">
						<g:if test="${sec.loggedInUserInfo(field:'tipoUsuario').toString() == UsuarioType.CLIENTE.toString()}">
							<li><g:link controller='cadastro' action="cadastroAdicionalCliente"><i class="icon-pencil"></i> Editar perfil</g:link></li>
						</g:if>
						<g:elseif test="${sec.loggedInUserInfo(field:'tipoUsuario').toString() == UsuarioType.PROFISSIONAL.toString()}">
							<g:if test="${sec.loggedInUserInfo(field:'status').toString() == StatusType.ATIVO.toString()}">
							<li><g:link controller='profile' action="index"><i class="icon-user"></i> Visualizar perfil</g:link></li>
							</g:if>
							<li><g:link controller='cadastro' action="cadastroAdicionalProfissional"><i class="icon-pencil"></i> Editar perfil</g:link></li>
						</g:elseif>
						<g:elseif test="${sec.loggedInUserInfo(field:'tipoUsuario').toString() == UsuarioType.INSTITUICAO.toString()}">
							<g:if test="${sec.loggedInUserInfo(field:'status').toString() == StatusType.ATIVO.toString()}">
							<li><g:link controller='profile' action="index"><i class="icon-user"></i> Visualizar perfil</g:link></li>
							</g:if>
							<li><g:link controller='cadastro' action="cadastroAdicionalInstituicao"><i class="icon-pencil"></i> Editar perfil</g:link></li>
						</g:elseif>
						<g:elseif test="${sec.loggedInUserInfo(field:'tipoUsuario').toString() == UsuarioType.ADMINISTRADOR.toString()}">
							<li><g:link controller='cidade' action="index"><i class="icon-pencil"></i> Editar cidades</g:link></li>
							<li><g:link controller='estado' action="index"><i class="icon-pencil"></i> Editar estados</g:link></li>
							<li><g:link controller='planoDeSaude' action="index"><i class="icon-pencil"></i> Editar planos de saúde</g:link></li>
							<li><g:link controller='profissao' action="index"><i class="icon-pencil"></i> Editar profissões</g:link></li>
						</g:elseif>
						
						<li><g:link controller='senha' action="mudarSenha"><i class="icon-lock"></i> Alterar senha</g:link></li>
						<li class="divider"></li>
						<li><g:link controller='logout'><i class="icon-remove"></i> Sair</g:link></li>
					</ul>
				</li>
				<li id="option-share" class="dropdown">
					<a href="#"	title="Recomendar para um amigo" class="dropdown-toggle" data-toggle="dropdown"> 
						<i></i>
					</a> 
					<g:formRemote url="[controller:'indicar', action:'indicarAmigo']" method='POST' update="atualizarLogado"
								  name="recomendarLogadoFormulario" class="dropdown-menu pull-right">
								  
						<div id="atualizarLogado">
    						<g:render template="/templatesLayout/updateTemplate"/>
						</div>
								  
						<g:textField name='nomeAmigo' placeholder="Nome do seu amigo" />
						<g:textField name='emailAmigo' placeholder="E-mail do seu amigo" />
						<button type="submit" class="btn btn-success btn-small pull-right">Recomendar</button>
					</g:formRemote>
				</li>
			</sec:ifLoggedIn>
			</ul>
		</div>
	</div>
	