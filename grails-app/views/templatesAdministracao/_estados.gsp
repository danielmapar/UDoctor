			<select name="${nome}" data-placeholder="Selecione..." class="chzn-select" tabindex="1">
				<option value=""></option>
				<g:each in="${estados}" var="estado">
				<option <g:if test="${estado?.id.toString() == estadoSelecionado}">selected</g:if> value="${estado?.id}">${estado.nome}</option>
				</g:each>
			</select>