package br.com.udoctor.email.assincrono

import org.springframework.mail.MailMessage;

import grails.plugin.mail.MailService;

class EnvioEmailAssincronoService {
	
	static transactional = "mongo"
	
	MailService mailService
	
    MailMessage enviarMensagem(MensagemEmailAssincrono mensagem) {

			return mailService.sendMail {
				if (mensagem.anexos) {
					multipart true
				}
				to mensagem.para
				from mensagem.de
				subject mensagem.assunto
				if (mensagem.headers && !mensagem.headers.isEmpty() && isMimeCapable()) {
					headers mensagem.headers
				}
				if (mensagem.html && isMimeCapable()) {
					if(mensagem.bodyArgs) {
						String view = mensagem.bodyArgs['view']
						mensagem.bodyArgs.remove('view')
						def bodyArgs = [view: view, model: mensagem.bodyArgs]
						html bodyArgs
					}else{
						html mensagem.texto
					}
				} else {
					if(mensagem.bodyArgs) {
						String view = mensagem.bodyArgs['view']
						mensagem.bodyArgs.remove('view')
						def bodyArgs = [view: view, model: mensagem.bodyArgs]
						body bodyArgs
					}else{
						body mensagem.texto
					}	
				}
				if (mensagem.bcc && !mensagem.bcc.isEmpty()) {
					bcc mensagem.bcc
				}
				if (mensagem.cc && !mensagem.cc.isEmpty()) {
					cc mensagem.cc
				}
				if (mensagem.replicarPara) {
					replyTo mensagem.replicarPara
				}
				if (mensagem.de) {
					from mensagem.de
				}
				if (isMimeCapable()) {
					mensagem.anexos.each {AnexoEmailAssincrono anexo ->
						if (!anexo.inline) {
							attachBytes anexo.nomeAnexo, anexo.tipoMime, anexo.conteudo
						} else {
							inline anexo.nomeAnexo, anexo.tipoMime, anexo.conteudo
						}
					}
				}
				
			}
		}
}
