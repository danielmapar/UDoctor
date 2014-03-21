package br.com.udoctor.email.assincrono

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import br.com.udoctor.email.assincrono.EmailAssincronoPersistenciaService;
import br.com.udoctor.tipo.Enum.StatusMensagemEmailType;


class ColetorDeMensagensExpiradasJob {

	def concurrent = false
	def group = "EmailAssincrono"
	
	EmailAssincronoPersistenciaService emailAssincronoPersistenciaService
	
	static triggers = {
		if (!ConfigurationHolder.config.assincrono.email.desabilitado) {
			simple repeatInterval: (Long) ConfigurationHolder.config.assincrono.email.expirado.coletor.repetir.intervalo
		}
	}

	def execute() {
		
		/*
		 * print('ColetorDeMensagensExpiradasJob') 
		log.trace('Entrou no job de coletor de mensagens expiradas.')
		
		//def mongo = new GMongo() 
		//def db = mongo.getDB("udoctor")
		
		//db.mensagemEmailAssincrono.update([dataDeFinalizacao: [$lt: new Date().toString()], $or: [[status: StatusMensagemEmailType.CRIADO.toString()], [status: StatusMensagemEmailType.TENTADO.toString()]]],
		//								  [$set:[status: StatusMensagemEmailType.EXPIRADO.toString()]], false, true) // upsert e multi
			
		//int contador = db.command( "getlasterror" ).n
		
		def mensagens =  emailAssincronoPersistenciaService.selecionarMensagensParaExpirar()
		Integer contador = mensagens.size()
		
		mensagens.each {MensagemEmailAssincrono mensagem ->
			mensagem.status = StatusMensagemEmailType.EXPIRADO
			emailAssincronoPersistenciaService.salvar(mensagem, true)
		}
		
		print contador
		log.trace("$contador mensagens expiradas foram atualizadas.")		
		*/
	}
}
