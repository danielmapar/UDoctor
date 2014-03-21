package br.com.udoctor.senha;

import br.com.udoctor.seguranca.EsqueciMinhaSenha;
import grails.plugins.springsecurity.SpringSecurityService;

class SenhaController {

	SpringSecurityService springSecurityService
	SenhaService senhaService

	def mudarSenha(){
		
		if (!request.post) {
			// mostrar o formul�rio
			return
		}
		
		String senha = params.senha
		String senha2 = params.senha2
		String confirmarSenha2 = params.confirmarSenha2
		
		def retorno = senhaService.mudarSenha(springSecurityService.principal, senha, senha2, confirmarSenha2)
		
		if(retorno instanceof String){
			flash.message = retorno
			return
		}
		
		return[mudarSenha: retorno]
	}

	def esqueciMinhaSenha(){
		
		if (!request.post) {
			// mostrar o formul�rio
			return
		}
		
		String email = params.email
		
		def retorno = senhaService.enviarEmailResetarSenha(email)
		
		if(retorno.mensagem){
			flash.message = retorno.mensagem
		}else if(retorno.erro){
			flash.error = retorno.erro
		}
		return
	}

	def resetarSenha() { 	
		
		String token = params.id
		
		EsqueciMinhaSenha esqueciMinhaSenha = token ? EsqueciMinhaSenha.findByToken(token) : null
		if (!esqueciMinhaSenha) {
			chain (controller: "pesquisa", action: "index", model: [erro: "Token de confirma&ccedil;&atilde;o inv&aacute;lido"])
		}
		
		if (!request.post) {
			return [token: token]
		}
	
		String senha = params.senha
		String senha2 = params.senha2
		
		def retorno = senhaService.resetarSenha(token, senha, senha2, esqueciMinhaSenha)
		
		if(retorno.mensagem){
			chain (controller: "pesquisa", action: "index", model: [mensagem: retorno.mensagem])
			return
		}
		return [token: retorno.token, resetarSenha: retorno.resetarSenha]

	}
}
