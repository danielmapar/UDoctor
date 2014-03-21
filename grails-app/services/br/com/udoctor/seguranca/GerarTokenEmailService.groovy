package br.com.udoctor.seguranca

import java.rmi.server.UID
import java.security.SecureRandom;
import org.codehaus.groovy.grails.commons.GrailsApplication;

class GerarTokenEmailService {
	
	static transactional = 'mongo'
	
	GrailsApplication grailsApplication
	
	static prng = new SecureRandom()
	
	String criarToken(String email)
	{
		def uid = email + new UID().toString() + prng.nextLong() + System.currentTimeMillis()
		def hash = uid.encodeAsSHA256Bytes()
		return hash.encodeAsBase62()
	}
	
	String criarURL(String token, def acao) {
		
		String urlServidor = grailsApplication.config.grails.serverURL ?: 'http://localhost:8080/'+grailsApplication.metadata.'app.name'
		
		String urlAction
		if(acao instanceof List){
			urlAction = "${urlServidor}/"
			acao.each{ String acaoStr ->
					urlAction += (acaoStr + '/')
			}
			urlAction += "${token.encodeAsURL()}"
		}else{
			urlAction = "${urlServidor}/${acao}/${token.encodeAsURL()}"
		}
		return urlAction
	}
	
}
