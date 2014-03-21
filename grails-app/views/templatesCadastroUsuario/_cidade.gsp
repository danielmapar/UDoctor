			<select name="${nome}" id="${id}" data-placeholder="Selecione..." class="chzn-select" tabindex="1">
				<option value=""></option>
				<g:each in="${estados}" var="estado">
				<optgroup label="${estado?.nome}">
					<g:each in="${estado?.cidades}" var="cidade">
					<option<g:if test="${cidade?.id == cidadeSelecionada && estado?.id == estadoSelecionado}"> selected </g:if> value="${cidade?.id};${estado?.id}">${cidade?.nome}</option>
					</g:each>
				</optgroup>
				</g:each>
			</select>