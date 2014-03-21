package br.com.udoctor.validacao;

import grails.validation.Validateable;
import grails.plugins.springsecurity.BCryptPasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import br.com.udoctor.tipo.Enum.StatusType;

@Validateable
class MudarSenha {
	
	String     nome
	String     sobrenome
	String     email
	
	StatusType status
	String 	   atualBCrypt
	String 	   atual
	String 	   nova
	String 	   confirmar
		
	static constraints = {
		
		nome (blank: false, nullable: false,  size: 2..20)
		sobrenome (blank: false, nullable: false,  size: 2..40)
		email (blank: false, nullable: false, unique: true, email: true, size: 7..80)
		status(blank: false, nullable: false, inList:StatusType.lista(), minSize:1, maxSize:1, validator: { StatusType status ->
			if(status != StatusType.ATIVO){
				return 'mudarSenha.atual.error.inativo'
			}
		})
		atualBCrypt(blank: false, nullable: false)
		atual(blank: false, nullable: false, validator: { String atual, MudarSenha mudarSenha ->
			if(!new BCryptPasswordEncoder().isPasswordValid(mudarSenha.atualBCrypt, atual, null)){
			   	return 'mudarSenha.atual.error.invalida'
			 }
		})
		nova(blank: false, nullable: false, size: 6..30, validator: { String nova, MudarSenha mudarSenha ->
			   if ((nova) && 
				   (nova.toLowerCase().equals(mudarSenha.nome.toLowerCase())      ||
					nova.toLowerCase().equals(mudarSenha.sobrenome.toLowerCase()) ||
					nova.toLowerCase().equals(mudarSenha.email.toLowerCase())
					)) {
					return 'resetarSenha.senha.error.igual'
				}
		  })
		confirmar(blank: false, nullable: false, size: 6..30, validator: { String confirmar, MudarSenha mudarSenha ->
			if(!confirmar.equals(mudarSenha.nova)){
			   	return 'mudarSenha.atual.error.repeticao'
			 }
		})
	}

}
