			<div class="info-group">
				<g:if test="${!telefones}">
					<div class="phone-field" id="phone1">
						<input type="hidden" name="telefones[0].ativo" id="telefones[0].ativo" value="X">
						<input type="text" name="telefones[0].numeroCompleto" id="telefones[0].numeroCompleto" value=""> <i class="icon-plus" title="Inserir telefone"></i>
					</div>
				</g:if>
				<g:else>
				<g:each status="i" var="telefone" in="${telefones}">
					<div class="phone-field" id="phone${i.next()}">
						<input type="hidden" name="telefones[${i}].ativo" id="telefones[${i}].ativo" value="X">
						<g:if test="${i==0}">
							<input type="text" name="telefones[${i}].numeroCompleto" id="telefones[${i}].numeroCompleto" value="${telefone.numeroCompleto}"> <i class="icon-plus" title="Inserir telefone"></i>
						</g:if>
						<g:else>
							<input type="text" name="telefones[${i}].numeroCompleto" id="telefones[${i}].numeroCompleto" value="${telefone.numeroCompleto}"> <i class="icon-remove" onclick=remove(this) title="Remover telefone"></i>
						</g:else>
				    </div>
				</g:each>
				</g:else>
				<input type="hidden" id="count-phones" value=<g:if test="${telefones}">${telefones?.size()}</g:if><g:else>1</g:else>>
			</div>