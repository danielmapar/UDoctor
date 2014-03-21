package br.com.udoctor.email

class ConfirmacaoDeEmailController {

	def confirmacaoEmailService
	def springSecurityService
	
	static allowedMethods = [confirmacaoDeEmail: "GET"]
	
	def receberConfirmacaoDeEmail(){
		
		String token = params.id
		
		def retorno = confirmacaoEmailService.validarConfirmacao(token)
		
		if(retorno.mensagem){
			flash.message = retorno.mensagem
		}else if(retorno.erro){
			flash.error = retorno.erro
		}
		return
	}

	def reEnviarConfirmacaoDeEmail(){
		
		if (!request.post) {
			// mostrar o formul�rio
			return
		}
		
		String email = params.email
		
		def retorno = confirmacaoEmailService.reEnviarConfirmacaoDeEmail(email)
		
		if(retorno.mensagem){
			flash.message = retorno.mensagem
		}else if(retorno.erro){
			flash.error = retorno.erro
		}
		return
	}
}
