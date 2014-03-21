			<select name="profissao" id="profissao" data-placeholder="Selecione..." class="chzn-select" tabindex="1">
				<option value=""></option>
				<g:each in="${profissoes}" var="profissao">
   	    			 <option<g:if test="${profissaoSelecionada == profissao?.id}"> selected </g:if> value="${profissao?.id}">${profissao?.nome}</option>
  			  </g:each>	
			</select>