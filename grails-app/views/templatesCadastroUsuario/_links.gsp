			<div class="info-group">			
				<g:if test="${!links}">	
				<div class="link-field" id="link1">
					<input type="hidden" name="links[0].ativo" id="links[0].ativo" value="X">		
					<input type="text" id="links[0].nome" name="links[0].nome" placeholder="Endereço URL" value=""> <i class="icon-plus" title="Inserir link"></i>
					<input type="text" id="links[0].descricaoLink" name="links[0].descricaoLink" value="" placeholder="Descreva este link" value="">
				</div>
				</g:if>
				<g:else>
				<g:each status="i" var="link" in="${links}">
				<div class="link-field" id="link${i.next()}">
					<input type="hidden" name="links[${i}].ativo" id="links[${i}].ativo" value='X'>
					<g:if test="${i == 0}">
					<input type="text" name="links[${i}].nome" id="links[${i}].nome" placeholder="Endereço URL" value="${link.nome}"> <i class="icon-plus" title="Inserir link"></i>
					<input type="text" name="links[${i}].descricaoLink" id="links[${i}].descricaoLink" placeholder="Descreva este link" value="${link.descricaoLink}"> 			
					</g:if>
				<g:else>
					<input type="text" name="links[${i}].nome" id="links[${i}].nome" placeholder="Endereço URL" value="${link.nome}"> <i class="icon-remove" onclick=remove(this) title="Remover link"></i>
					<input type="text" name="links[${i}].descricaoLink" id="links[${i}].descricaoLink" placeholder="Descreva este link" value="${link.descricaoLink}"> 	
				</g:else>	
				</div>	
				</g:each>
				</g:else>
					<input type="hidden" id="count-links" value=<g:if test="${links}">${links?.size()}</g:if><g:else>1</g:else>>						
			</div>