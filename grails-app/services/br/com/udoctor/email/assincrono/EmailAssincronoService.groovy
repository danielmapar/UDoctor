package br.com.udoctor.email.assincrono

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.validation.ObjectError

class EmailAssincronoService {

	static transactional = "mongo"

	EmailAssincronoPersistenciaService emailAssincronoPersistenciaService
	MensagemEmailAssincronoBuilderFactory mensagemEmailAssincronoBuilderFactory 
	GrailsApplication grailsApplication

	/**
	 * Criar mensagem sincrona e salvar no MongoDB
	 *
	 * Se o flag de configura��o assincrono.email.enviar.imediatamente for true (default)
	 * ent�o esse metodo inicia enviando job depois de criar a mensagem
	 */
	def enviarEmailAssincronamente(Closure resgatavel) {
		if(!mensagemEmailAssincronoBuilderFactory){
			mensagemEmailAssincronoBuilderFactory = new MensagemEmailAssincronoBuilderFactory()
		}
		def mensagemBuilder = mensagemEmailAssincronoBuilderFactory.criarBuilder(grailsApplication)
		resgatavel.delegate = mensagemBuilder
		resgatavel.resolveStrategy = Closure.DELEGATE_FIRST
		resgatavel()

		// Mail message
		MensagemEmailAssincrono mensagem = mensagemBuilder.mensagem

		// Verificar comportamento da configura��o imediatamente
		boolean imediatamente
		if (mensagemBuilder.imediatamenteSetado) {
			imediatamente = mensagemBuilder.imediatamente
		} else {
			imediatamente = grailsApplication.config.assincrono.email.enviar.imediatamente
		}

		imediatamente = 
				imediatamente &&
				mensagem.dataDeInicioParaEnvio.time <= System.currentTimeMillis() &&
				!grailsApplication.config.assincrono.email.desabilitado
				
		// Salvar mensagem no DB		
		if (!emailAssincronoPersistenciaService.salvar(mensagem, imediatamente)) {
			StringBuilder mensagemDeErro = new StringBuilder()
			mensagem.errors?.allErrors?.each {ObjectError error ->
				mensagemDeErro.append(error.getDefaultMessage())
			}
			throw new Exception(mensagemDeErro.toString())
		}

		// Iniciar job imediatamente
		if (imediatamente) {
			log.trace("Iniciar job de envio imediatamente.")
			enviarImediatamente()
		}

		return mensagem
	}

	def enviarEmail(Closure resgatavel) {
		return enviarEmailAssincronamente(resgatavel)
	}

	/**
	 * Iniciar job de envio imediatamente. Se voc� enviar mais de uma mensagem em um metodo, 
	 * voc� pode desabilitar o flag assincrono.email.enviar.imediatamente (default true) e usar esse metodo
	 * depois de criar todas as mensagens.
	 */
	def enviarImediatamente() {
		EmailAssincronoJob.triggerNow()
	}
}
