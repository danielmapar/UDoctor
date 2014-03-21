package br.com.udoctor.validacao;

import grails.validation.Validateable;

@Validateable
class ResetarSenha {
	String nome
	String sobrenome
	String email
	String senha
	String senha2

	static constraints = {
		nome (blank: false, nullable: false,  size: 2..20)
		sobrenome (blank: false, nullable: false,  size: 2..40)
		email (blank: false, nullable: false, unique: true, email: true, size: 7..80)
		senha (blank: false, nullable: false, size: 6..30,
			validator: { String senha, ResetarSenha resetarSenha ->
			   if ((senha) && 
				   (senha.toLowerCase().equals(resetarSenha.nome.toLowerCase())      ||
					senha.toLowerCase().equals(resetarSenha.sobrenome.toLowerCase()) ||
					senha.toLowerCase().equals(resetarSenha.email.toLowerCase())
					)) {
					return 'resetarSenha.senha.error.igual'
				}
		  })
		senha2 (blank: false, nullable: false, size: 6..30,
			 validator: { String senha2, ResetarSenha resetarSenha ->
				 if(!senha2.equals(resetarSenha.senha)){
					 return 'resetarSenha.senha2.error.igual'
				 }
			 })
	}
}
