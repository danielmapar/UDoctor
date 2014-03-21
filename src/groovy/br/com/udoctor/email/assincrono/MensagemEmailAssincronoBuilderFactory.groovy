package br.com.udoctor.email.assincrono

import javax.activation.FileTypeMap
import javax.activation.MimetypesFileTypeMap

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.mail.javamail.JavaMailSender

class MensagemEmailAssincronoBuilderFactory {
    def mailMessageContentRenderer
    def mailSender

    private final FileTypeMap tipoArquivoMap = new MimetypesFileTypeMap()

    MensagemEmailAssincronoBuilder criarBuilder(def grailsApplication) {
		
        MensagemEmailAssincronoBuilder builder = new MensagemEmailAssincronoBuilder(
                (mailSender instanceof JavaMailSender),
                tipoArquivoMap,
                mailMessageContentRenderer
        )
        builder.init(grailsApplication.config)
        return builder
    }
}
