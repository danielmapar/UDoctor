			<%@page import="br.com.udoctor.tipo.Enum.UsuarioType"%>
			<g:if test="${usuarioComAvatar}">
            <div class="fileupload fileupload-exists" data-provides="fileupload">
            </g:if>
            <g:else>
            <div class="fileupload fileupload-new" data-provides="fileupload">
            </g:else>
                <div class="fileupload-new thumbnail" style="width: 170px; height: 170px;"><img src="${ g.resource( dir:'css/udoctor/images', file: "${templateAvatar}")}" /></div>
                <div class="fileupload-preview fileupload-exists thumbnail" style="width: 170px; height: 170px; line-height: 20px;">             
                <g:if test="${tipoUsuario == UsuarioType.CLIENTE && usuarioComAvatar}">
                	<img src="${g.resource( dir: grailsApplication.config.arquivo.diretorio.nome.avatar ?: 'avatar', file:"${avatar.nomeThumbnail}") }" />
                </g:if>
                <g:elseif test="${usuarioComAvatar}">
                	<img src="${g.resource( dir: grailsApplication.config.arquivo.diretorio.nome.avatar ?: 'avatar', file:"${arquivos[1].nomeThumbnail}") }" />
                </g:elseif>
                </div>
                <div>
                    <span class="btn btn-file"><span class="fileupload-new">Selecionar imagem</span><span class="fileupload-exists">Alterar</span><input type="file" name="avatar" accept="image/*" /></span>
    				<a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Remove</a>
                </div>
            </div>