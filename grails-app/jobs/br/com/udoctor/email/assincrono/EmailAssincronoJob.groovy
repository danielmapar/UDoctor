package br.com.udoctor.email.assincrono

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import org.springframework.mail.*;

import br.com.udoctor.email.assincrono.MensagemEmailAssincrono;
import br.com.udoctor.tipo.Enum.StatusMensagemEmailType;

class EmailAssincronoJob {
	
	def concurrent = false
	def group = "EmailAssincrono"
	
	// Dependency injection
	EmailAssincronoPersistenciaService emailAssincronoPersistenciaService
	EnvioEmailAssincronoService envioEmailAssincronoService
	
	static triggers = {
		if (!ConfigurationHolder.config.assincrono.email.desabilitado) {
			simple repeatInterval: (Long) ConfigurationHolder.config.assincrono.email.enviar.repetir.intervalo
		}
	}

	def execute() {
		
		print "EmailAssincronoJob"
		
		MensagemEmailAssincrono.withNewSession {
		
		log.trace('Entrou no metodo execute')
		// Pega mensagens do DB
		def mensagens =  emailAssincronoPersistenciaService.selecionarMensagensParaEnvio()

		// Enviar cada mensagem e salvar status
		try {
			mensagens.each {MensagemEmailAssincrono mensagem ->

				log.trace("Encontrei a mensagem: " + mensagem.toString())
				
				Date agora = new Date()
				Date dataTentativa = new Date(agora.getTime() - mensagem.intervaloEntreTentativas)
				
				if (mensagem.status == StatusMensagemEmailType.CRIADO ||
				   (mensagem.status == StatusMensagemEmailType.TENTADO && mensagem.dataDaUltimaTentativa.before(dataTentativa))) {
				   
					mensagem.dataDaUltimaTentativa = agora
					mensagem.contadorDeTentativas++
					
					// Isso garante que um e-mail nao pode ser enviado mais de uma vez
					mensagem.status = StatusMensagemEmailType.ERRO
					emailAssincronoPersistenciaService.salvar(mensagem, true)
					// Tenta enviar e-mail
					try {
						envioEmailAssincronoService.enviarMensagem(mensagem)
						mensagem.dataDeEnvio = agora
						mensagem.status = StatusMensagemEmailType.ENVIADO
					} catch (MailException e) {
						log.warn("Tentou enviar o email de id=${mensagem.id}, mas falhou.", e)
						if (mensagem.contadorDeTentativas < mensagem.maximoNumeroDeTentativas &&
						  !(e instanceof MailParseException || e instanceof MailPreparationException)) {
							mensagem.status = StatusMensagemEmailType.TENTADO
						}

						if (e instanceof MailAuthenticationException) {
							throw e
						}
					} finally {
						emailAssincronoPersistenciaService.salvar(mensagem, true)
					}
					
					// Deletar mensagem se foi enviada com sucesso e pode ser deletada
					if (mensagem.status == StatusMensagemEmailType.ENVIADO && mensagem.deletarDepoisDoEnvio) {
						emailAssincronoPersistenciaService.deletar(mensagem)
					}
				}
			}
		} catch (Exception e) {
			//log.error('Abortar envio de email.', e)
		}
	}
	}
}