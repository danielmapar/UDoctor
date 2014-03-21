			<select name="planosDeSaude" id="planosDeSaude" data-placeholder="Selecione..." multiple class="chzn-select" tabindex="1">
				<option value=""></option> 	
				<g:each in="${planosDeSaude}" var="planoDeSaude">
           		<option <g:each in="${planosDeSaudeUtilizados}" var="planoDeSaudeUtilizado"><g:if test="${planoDeSaudeUtilizado == planoDeSaude?.id}">selected </g:if></g:each>value="${planoDeSaude?.id}">${planoDeSaude?.nome}</option>
           		</g:each>		
			</select>