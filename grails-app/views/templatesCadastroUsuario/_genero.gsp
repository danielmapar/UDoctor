<%@ page import="br.com.udoctor.tipo.Enum.GeneroType" %>

<select name="genero" id="genero" data-placeholder="Selecione..." class="chzn-select" tabindex="1">
	<g:if test="${genero.toString() == GeneroType.MASCULINO.toString()}">
		<option value="${GeneroType.MASCULINO.toString()}">Masculino</option>
		<option value="${GeneroType.FEMININO.toString()}">Feminino</option>
	</g:if>
	<g:elseif test="${genero.toString() == GeneroType.FEMININO.toString()}">
		<option value="${GeneroType.FEMININO.toString()}">Feminino</option>
		<option value="${GeneroType.MASCULINO.toString()}">Masculino</option>
	</g:elseif>
	<g:else>
		<option value=""></option>
		<option value="${GeneroType.MASCULINO.toString()}">Masculino</option>
		<option value="${GeneroType.FEMININO.toString()}">Feminino</option>
	</g:else>
</select>