package br.com.udoctor.senha;

import grails.plugins.springsecurity.SpringSecurityService;
import br.com.udoctor.email.assincrono.EmailAssincronoService;
import br.com.udoctor.seguranca.CustomUserDetails;
import br.com.udoctor.seguranca.EsqueciMinhaSenha;
import br.com.udoctor.seguranca.GerarTokenEmailService;
import br.com.udoctor.seguranca.Usuario;
import br.com.udoctor.tipo.Enum.MensagemEmailType;
import br.com.udoctor.validacao.MudarSenha;
import br.com.udoctor.validacao.ResetarSenha;

class SenhaService {
	
	GerarTokenEmailService gerarTokenEmailService
	EmailAssincronoService emailAssincronoService
	SpringSecurityService  springSecurityService
	
	static transactional = 'mongo'
	
	// Modificar senha apartir de dados do usu�rios presentes em sua sess�o
	def mudarSenha(CustomUserDetails usuario, String senha, String senha2, String confirmarSenha2){
				
		MudarSenha mudarSenha   = new MudarSenha()
		mudarSenha.nome         = usuario.nome
		mudarSenha.sobrenome    = usuario.sobrenome
		mudarSenha.email        = usuario.username
		mudarSenha.status     	= usuario.status
		mudarSenha.atualBCrypt 	= usuario.password
		mudarSenha.atual       	= senha
		mudarSenha.nova       	= senha2
		mudarSenha.confirmar   	= confirmarSenha2
		
		if(mudarSenha.validate()){
			usuario.senhaNaoModificada = usuario.senha
			usuario.senha      		   = mudarSenha.nova
			if(usuario.save()){
				springSecurityService.reauthenticate(usuario.username)
				return "Senha modificada com sucesso!"
			}
			return usuario
		}
		return mudarSenha
	}
	
	def enviarEmailResetarSenha(String email){
		
		if (!email) {
			return [erro: "Favor preencher o campo email"]
		}

		Usuario usuario = Usuario.findByEmail("${email}")
		if (!usuario) {
			return [erro: "Esse email n&atilde;o encontra-se cadastrado"]
		}

		EsqueciMinhaSenha esqueciMinhaSenha = new EsqueciMinhaSenha(email: usuario.email)
		esqueciMinhaSenha.token = gerarTokenEmailService.criarToken(usuario.email)
		esqueciMinhaSenha.save(flush: true)
		
		String uri = gerarTokenEmailService.criarURL(esqueciMinhaSenha.token, ['senha','resetar'])
		String view = "/templatesEmail/esqueciMinhaSenha/trocarSenha"
		String assuntoPadrao  = "Gerar nova senha"
		
		if (log.infoEnabled) {
			log.info("Enviando email de confirma��o para ${usuario.email} - link de confirma��o �: ${uri}")
		}
		
		String mensagem
		String erro
		try {
			emailAssincronoService.enviarEmail {
				para usuario.email
				assunto assuntoPadrao
				def argsBody = [view:view, uri:uri]
				bodyArgs argsBody, true // recebe um Map de atributos e caso a view utilize HTML, o segundo atributo � true
				tipoDeMensagem MensagemEmailType.ESQUECI_MINHA_SENHA
				dataDeInicioParaEnvio new Date(System.currentTimeMillis())    // Na pr�xima execu��o do job esse e-mail ser� processado
				dataDeExpiracao new Date(System.currentTimeMillis()+3600000)   // Deve ser enviado em 1 hora, default infinito
				imediatamente false
				prioridade 10
			}
			mensagem = "Email enviado com sucesso!"
		} catch (Throwable t) {
			throw t
			erro = "Erro durante o envio de email de confirma��o!"
		}
		return [mensagem: mensagem, erro: erro]
	}
	
	def resetarSenha(String token, String senha, String senha2, EsqueciMinhaSenha esqueciMinhaSenha){
				
		Usuario usuario = Usuario.findByEmail(esqueciMinhaSenha.email)
		
		ResetarSenha resetarSenha = new ResetarSenha()
		resetarSenha.nome      = usuario.nome
		resetarSenha.sobrenome = usuario.sobrenome
		resetarSenha.email     = usuario.email
		resetarSenha.senha     = senha
		resetarSenha.senha2    = senha2
		resetarSenha.validate()

		if (resetarSenha.hasErrors()) {
			return [token: token, resetarSenha: resetarSenha] 
		}

		EsqueciMinhaSenha.withTransaction { status ->
			usuario.senhaNaoModificada = usuario.senha
			usuario.senha			   = resetarSenha.senha
			usuario.save()
			esqueciMinhaSenha.delete()
		}

		springSecurityService.reauthenticate usuario.email
		
		return [mensagem: "Senha modificada com sucesso!"]
	}
	
}
