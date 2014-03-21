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
			log.info "Enviar email de confirmação para ${args.para}, id do usuário ${args.id}"
		}
		
		// Verifica se já existe uma confirmação de email para esse usuário
		ConfirmacaoEmailPendente confExistente = ConfirmacaoEmailPendente.findByEmail(args.para)
		if(confExistente){
			confExistente.delete(flush: true)
		}
		
		ConfirmacaoEmailPendente conf = new ConfirmacaoEmailPendente(email:args.para, usuarioId:args.id)
        conf.tokenDeConfirmacao = gerarTokenEmailService.criarToken(conf.email)

        if (!conf.save()) {
			throw new IllegalArgumentException("Não foi possivel salvar confirmação de email pendente: ${conf.errors}")
		}

		String uri = gerarTokenEmailService.criarURL(conf.tokenDeConfirmacao, 'confirmacao')
		String view = "/templatesEmail/confirmacao/requisicaoConfirmacao"
		String assuntoPadrao  = "Confirme sua conta"
		
		if (log.infoEnabled) {
			log.info("Enviando email de confirmação para $args.para - link de confirmação é: ${uri}")
		}
		
		String mensagem 
		String erro
		try {
			emailAssincronoService.enviarEmail {
				para args.para
				assunto assuntoPadrao
				def argsBody = [view:view, uri:uri]
				bodyArgs argsBody, true // recebe um Map de atributos e caso a view utilize HTML, o segundo atributo é true
				tipoDeMensagem args.reEnvio ? MensagemEmailType.RE_CONFIRMACAO_DE_EMAIL : MensagemEmailType.CONFIRMACAO_DE_EMAIL
				dataDeInicioParaEnvio new Date(System.currentTimeMillis())    // Na próxima execução do job esse e-mail será processado
				dataDeExpiracao new Date(System.currentTimeMillis()+3600000)   // Deve ser enviado em 1 hora, default infinito
				imediatamente false
				prioridade 9
			}
			mensagem = "Email enviado com sucesso!"
		} catch (Throwable t) {
	        throw t
			erro = "Erro durante o envio de email de confirmação!"
		}
		return [mensagem: mensagem, erro: erro]
    }
	
	def reEnviarConfirmacaoDeEmail(String email){
		if (log.traceEnabled) {
			log.trace("Procurando por email: $email para re-enviar o email de confirmação")
		}
		
		ConfirmacaoEmailPendente pendente = ConfirmacaoEmailPendente.findByEmail("${email}")
		
		if (pendente) {
			pendente.delete(flush: true)
		} 
		
		Usuario usuario = Usuario.findByEmail(email)
		if(usuario){
			if(usuario.enabled){
				return [erro: "Seu email já encontra-se confirmado"]
			}
			def retorno = enviarConfirmacao(id: usuario.id,
								            para: usuario.email,
											reEnvio: true)
			if(retorno.mensagem){
				return [mensagem: "Email de confirmação enviado com sucesso!"]
			}else if(retorno.erro){
				return [erro: "Ocorreu um erro durante o envio de email, tente novamente mais tarde!"]
			}
		}
		return [erro: "Esse email não está cadastrado"]
	}
    
    /**
     * Validar token de confirmação e retornar um Map indicando a ação a ser tomada
     */
	def validarConfirmacao(String tokenDeConfirmacao) {
		if (log.traceEnabled) {
            log.trace("Procurando por token de confirmação: $tokenDeConfirmacao")
        }

		ConfirmacaoEmailPendente conf
        if (tokenDeConfirmacao) {
            conf = ConfirmacaoEmailPendente.findByTokenDeConfirmacao(tokenDeConfirmacao)
        }
		
		String mensagem 
		String erro 
		// checa 100% se o token do objeto é de fato o tokenDeConfirmacao
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
				erro = "Esse email não está cadastrado!"
		}else{
			erro = "O token fornecido é inválido!"
		}
		
		return[mensagem: mensagem, erro: erro]		
	}

	void eliminarConfirmacoesObsoletas(long maisVelhoQue = 0) {
		if (log.infoEnabled) {
			log.info( "Verificando por emails de confirmação obsoletos...")
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
			log.info( "Finalizado o processo de verificação por emails de confirmação obsoletas, encontrou $c")
		}
	}
}
