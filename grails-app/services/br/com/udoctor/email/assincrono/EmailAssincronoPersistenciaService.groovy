package br.com.udoctor.email.assincrono

import org.codehaus.groovy.grails.commons.GrailsApplication

import br.com.udoctor.tipo.Enum.StatusMensagemEmailType;

class EmailAssincronoPersistenciaService {
	
	static transactional = "mongo"
	
	GrailsApplication grailsApplication
	
    Boolean salvar(MensagemEmailAssincrono mensagem, boolean flush = false){
        mensagem.save(flush: flush)
    }

    Boolean deletar(MensagemEmailAssincrono mensagem){
        mensagem.delete()
    }

    def selecionarMensagensParaEnvio(){
		return MensagemEmailAssincrono.withCriteria{
			Date agora = new Date()
			lt("dataDeInicioParaEnvio", agora)
			gt("dataDeExpiracao", agora)
		    or {
		        eq('status', StatusMensagemEmailType.CRIADO)
		        eq('status', StatusMensagemEmailType.TENTADO)
		    }
		    order('prioridade', 'desc')
		    order('dataDeExpiracao', 'asc')
		    order('contadorDeTentativas', 'asc')
		    order('dataDeInicioParaEnvio', 'asc')
		    maxResults((int) grailsApplication.config.assincrono.email.mensagens.enviar.de.uma.vez ?: 100)
		}
    }
	def selecionarMensagensParaExpirar(){
		return MensagemEmailAssincrono.withCriteria{
			Date agora = new Date()
			lt("dataDeFinalizacao", agora)
			or {
				eq('status', StatusMensagemEmailType.CRIADO)
				eq('status', StatusMensagemEmailType.TENTADO)
			}
			order('prioridade', 'desc')
			order('dataDeExpiracao', 'asc')
			order('contadorDeTentativas', 'asc')
			order('dataDeInicioParaEnvio', 'asc')
			maxResults((int) grailsApplication.config.assincrono.email.mensagens.expirar.de.uma.vez ?: 500)
		}
	}
}
