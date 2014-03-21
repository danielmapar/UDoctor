package br.com.udoctor.indicar;

import br.com.udoctor.seguranca.Usuario
import org.bson.types.ObjectId;

class IndicarController {
		
	def springSecurityService
	def indicacaoService
	
	static allowedMethods = [indicarAmigo: "POST"]
	
	def indicarAmigo(){

        ObjectId usuarioId
		String nome
		String email
		
		if(springSecurityService.currentUser){
			 Usuario usuario = springSecurityService.currentUser
			 usuarioId = usuario.id
			 nome      = usuario.nome	
			 email     = usuario.email	
		}else{
			nome  = params.nome
			email = params.email
		}
	   
		String amigoNome  = params.nomeAmigo
		String amigoEmail = params.emailAmigo
		
		def retorno  = indicacaoService.indicarAmigo(usuarioId,nome,email,amigoNome,amigoEmail)
		
		String mensagem 
		String erro
		if(retorno instanceof Map){
			if(retorno.mensagem){
				mensagem = retorno.mensagem
			}else if(retorno.erro){
				erro = retorno.erro
			}
		}
		render (template: '/templatesLayout/updateTemplate', model: [retornoRemoto: retorno, mensagemRemoto: mensagem, erroRemoto: erro])
	}
}
