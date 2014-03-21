package br.com.udoctor.seguranca

import grails.plugins.springsecurity.SpringSecurityService;
import br.com.udoctor.email.assincrono.EmailAssincronoService;
import br.com.udoctor.seguranca.ConfirmacaoEmailPendente;
import br.com.udoctor.tipo.Enum.MensagemEmailType;
import br.com.udoctor.tipo.Enum.StatusType;
import br.com.udoctor.tipo.Enum.UsuarioType;

class ConfirmacaoEmailService {

    static transactional = 'mongo'
	
	def idadeMaxima = 1000*60*60*24*30L // 30 dias
	
	EmailAssincronoService emailAssincronoService
	GerarTokenEmailService gerarTokenEmailService
	SpringSecurityService  springSecurityService

	def enviarConfirmacao(Map args) {
		if (log.infoEnabled) {
			log.info "Enviar email de confirma��o para ${args.para}, id do usu�rio ${args.id}"
		}
		
		// Verifica se j� existe uma confirma��o de email para esse usu�rio
		ConfirmacaoEmailPendente confExistente = ConfirmacaoEmailPendente.findByEmail(args.para)
		if(confExistente){
			confExistente.delete(flush: true)
		}
		
		ConfirmacaoEmailPendente conf = new ConfirmacaoEmailPendente(email:args.para, usuarioId:args.id)
        conf.tokenDeConfirmacao = gerarTokenEmailService.criarToken(conf.email)

        if (!conf.save()) {
			throw new IllegalArgumentException("N�o foi possivel salvar confirma��o de email pendente: ${conf.errors}")
		}

		String uri = gerarTokenEmailService.criarURL(conf.tokenDeConfirmacao, 'confirmacao')
		String view = "/templatesEmail/confirmacao/requisicaoConfirmacao"
		String assuntoPadrao  = "Confirme sua conta"
		
		if (log.infoEnabled) {
			log.info("Enviando email de confirma��o para $args.para - link de confirma��o �: ${uri}")
		}
		
		String mensagem 
		String erro
		try {
			emailAssincronoService.enviarEmail {
				para args.para
				assunto assuntoPadrao
				def argsBody = [view:view, uri:uri]
				bodyArgs argsBody, true // recebe um Map de atributos e caso a view utilize HTML, o segundo atributo � true
				tipoDeMensagem args.reEnvio ? MensagemEmailType.RE_CONFIRMACAO_DE_EMAIL : MensagemEmailType.CONFIRMACAO_DE_EMAIL
				dataDeInicioParaEnvio new Date(System.currentTimeMillis())    // Na pr�xima execu��o do job esse e-mail ser� processado
				dataDeExpiracao new Date(System.currentTimeMillis()+3600000)   // Deve ser enviado em 1 hora, default infinito
				imediatamente false
				prioridade 9
			}
			mensagem = "Email enviado com sucesso!"
		} catch (Throwable t) {
	        throw t
			erro = "Erro durante o envio de email de confirma��o!"
		}
		return [mensagem: mensagem, erro: erro]
    }
	
	def reEnviarConfirmacaoDeEmail(String email){
		if (log.traceEnabled) {
			log.trace("Procurando por email: $email para re-enviar o email de confirma��o")
		}
		
		ConfirmacaoEmailPendente pendente = ConfirmacaoEmailPendente.findByEmail("${email}")
		
		if (pendente) {
			pendente.delete(flush: true)
		} 
		
		Usuario usuario = Usuario.findByEmail(email)
		if(usuario){
			if(usuario.enabled){
				return [erro: "Seu email j� encontra-se confirmado"]
			}
			def retorno = enviarConfirmacao(id: usuario.id,
								            para: usuario.email,
											reEnvio: true)
			if(retorno.mensagem){
				return [mensagem: "Email de confirma��o enviado com sucesso!"]
			}else if(retorno.erro){
				return [erro: "Ocorreu um erro durante o envio de email, tente novamente mais tarde!"]
			}
		}
		return [erro: "Esse email n�o est� cadastrado"]
	}
    
    /**
     * Validar token de confirma��o e retornar um Map indicando a a��o a ser tomada
     */
	def validarConfirmacao(String tokenDeConfirmacao) {
		if (log.traceEnabled) {
            log.trace("Procurando por token de confirma��o: $tokenDeConfirmacao")
        }

		ConfirmacaoEmailPendente conf
        if (tokenDeConfirmacao) {
            conf = ConfirmacaoEmailPendente.findByTokenDeConfirmacao(tokenDeConfirmacao)
        }
		
		String mensagem 
		String erro 
		// checa 100% se o token do objeto � de fato o tokenDeConfirmacao
		if (conf && 
		   (conf.tokenDeConfirmacao == tokenDeConfirmacao) && 
		   (conf.dataDeCriacao > new Date((System.currentTimeMillis() - idadeMaxima)))) {
				conf.delete()
				
				Usuario usuario = Usuario.get(conf.usuarioId)
				if(usuario){
					if(usuario.tipoUsuario == UsuarioType.CLIENTE){
						usuario.status = StatusType.ATIVO
					}
					usuario.enabled = true
				
					if(usuario.save(validate: false)){
						if(springSecurityService.loggedIn && springSecurityService.principal.username == usuario.email) {
							springSecurityService.reauthenticate usuario.email
						}
					}
					mensagem = "Seu email ${usuario.email} foi confirmado com sucesso!"
				}
				erro = "Esse email n�o est� cadastrado!"
		}else{
			erro = "O token fornecido � inv�lido!"
		}
		
		return[mensagem: mensagem, erro: erro]		
	}

	void eliminarConfirmacoesObsoletas(long maisVelhoQue = 0) {
		if (log.infoEnabled) {
			log.info( "Verificando por emails de confirma��o obsoletos...")
		}
		def limiar = maisVelhoQue ?: System.currentTimeMillis() - idadeMaxima

		def confirmacoesObsoletas = ConfirmacaoEmailPendente.withCriteria {
            lt('dataDeCriacao', new Date(limiar))
        }
		
		def c = 0
        confirmacoesObsoletas.each { ConfirmacaoEmailPendente confirmacaoObsoleta ->
            def confirmacao = ConfirmacaoEmailPendente.get(confirmacaoObsoleta.id)
            if (confirmacao) {   
    			confirmacao.delete()
    			c++
            }
		}
		if (log.infoEnabled) {
			log.info( "Finalizado o processo de verifica��o por emails de confirma��o obsoletas, encontrou $c")
		}
	}
}
