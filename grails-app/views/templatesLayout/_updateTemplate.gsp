<g:if test="${mensagemRemoto}">
	${mensagemRemoto}
</g:if>
<g:elseif test="${retornoRemoto}">
	<g:renderErrors bean="${retornoRemoto}"/> 
</g:elseif>
<g:elseif test="${erroRemoto}">
	${erroRemoto}
</g:elseif>	