package br.com.udoctor.email.confirmacao

import br.com.udoctor.seguranca.ConfirmacaoEmailService;

class ConfirmacoesObsoletasJob {
	
	def concurrent = false
	def group = "ConfirmacaoEmailGroup"
	
	static triggers = {
		   cron name:'confirmacaoEmailTrigger',
			   cronExpression:"0 0 3 * * ? *"  // Uma vez a cada 24 horas as 3 da manha
	}
	   
	ConfirmacaoEmailService confirmacaoEmailService

	def execute() {
		
		print "ConfirmacoesObsoletasJob"
		
		log.info("Removendo emails obsoletos")
		try {
			confirmacaoEmailService.eliminarConfirmacoesObsoletas()
		} catch (Throwable t) {
			log.error("Nao foi possivel executar o processo de limpeza dos emails obsoletos")
		}
		
	}
}
