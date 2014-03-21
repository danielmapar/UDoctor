package br.com.udoctor.email.assincrono

import grails.plugin.mail.GrailsMailException
import grails.plugin.mail.MailMessageContentRender
import grails.plugin.mail.MailMessageContentRenderer
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.InputStreamSource
import org.springframework.mail.MailMessage
import org.springframework.mail.MailSender
import org.springframework.util.Assert

import br.com.udoctor.tipo.Enum.MensagemEmailType;

import javax.activation.FileTypeMap

/**
 * Construir nova mensagem sincrona
 */
class MensagemEmailAssincronoBuilder {
	
    private MensagemEmailAssincrono mensagem
    private boolean imediatamente = false
    private boolean imediatamenteSetado = false

    private Locale locale

    final boolean capazDeUtilizarMime
    final MailMessageContentRenderer conteudoMensagemEmailRenderer

    private FileTypeMap tipoArquivoMap
 
    MensagemEmailAssincronoBuilder(
            boolean capazDeUtilizarMime,
            FileTypeMap tipoArquivoMap,
            MailMessageContentRenderer conteudoMensagemEmailRenderer = null
    ) {
        this.capazDeUtilizarMime = capazDeUtilizarMime;
        this.tipoArquivoMap = tipoArquivoMap;
        this.conteudoMensagemEmailRenderer = conteudoMensagemEmailRenderer;
    }

    void init(ConfigObject config) {
        mensagem = new MensagemEmailAssincrono()
        mensagem.intervaloEntreTentativas = config?.assincrono?.email?.default?.tentativa?.intervalo ?: 300000l
        mensagem.maximoNumeroDeTentativas = config?.assincrono?.email?.default?.maximo?.tentativas?.contador ?: 1
        mensagem.deletarDepoisDoEnvio = config?.assincrono?.email?.deletar?.depois?.de?.enviar ?: false
		mensagem.de = config?.assincrono?.email?.default?.de ?: null
    }
	
	void tipoDeMensagem(MensagemEmailType mensagemEmail){
		Assert.notNull(mensagemEmail, "Tipo de mensagem não pode ser null.")
		
		mensagem.tipoDeMensagem = mensagemEmail
	}
	
	// Campos específicos para mensagem assíncrona
    void dataDeInicioParaEnvio(Date inicio) {
        Assert.notNull(inicio, "Data de início não pode ser null.")
		
        mensagem.dataDeInicioParaEnvio = inicio
    }

    void dataDeExpiracao(Date expiracao) {
        Assert.notNull(expiracao, "Data de finalização não pode ser null.")
		
        mensagem.dataDeExpiracao = expiracao
    }

    // Prioridade
    void prioridade(int prioridade) {
        mensagem.prioridade = prioridade
    }

    // Tentativas
    void maximoNumeroDeTentativas(int maximo) {
        mensagem.maximoNumeroDeTentativas = maximo
    }

    void intervaloEntreTentativas(long intervalo) {
        mensagem.intervaloEntreTentativas = intervalo
    }
	
	// Marcar que a mensagem deve ser enviada imediatamente
    void imediatamente(boolean valor) {
        imediatamente = valor
        imediatamenteSetado = true
    }
	
	// Marcar que a mensagem deve ser deletada depois de enviada
    void deletarDepoisDoEnvio(boolean valor) {
        mensagem.deletarDepoisDoEnvio = valor
    }
	
	// Campo Multipart não faz nada
	void multipart(boolean multipart) {
		// nada
		// Adicionado analogo ao mail plugin
	}

	void multipart(int multipartMode) {
		// nada
		// Adicionado analogo ao mail plugin
	}
	
	// Headers da mensagem de e-mail
    void headers(Map headers) {
        Assert.notEmpty(headers, "Headers não pode ser null.")

        if(!capazDeUtilizarMime){
            throw new GrailsMailException("Você deve usar um JavaMailSender para customizar os headers.")
        }

        Map map = new HashMap()

        headers.each{key, value->
            String chaveString = key?.toString()
            String valorString = value?.toString()

            Assert.hasText(chaveString, "Header não pode ser null ou vazio.")
            Assert.hasText(valorString, "Valor de header ${chaveString} não pode ser null ou vazio.")

            map.put(chaveString, valorString)
        }

        mensagem.headers = map
    }
	
	// bodyArgs da mensagem de e-mail para o uso de view's e parametros
	void bodyArgs(Map bodyArgs, Boolean html) {
		Assert.notEmpty(bodyArgs, "BodyArgs não pode ser null.")
		
		mensagem.html = html
		
		Map map = new HashMap()

		bodyArgs.each{key, value->
			String chaveString = key?.toString()
			String valorString = value?.toString()

			Assert.hasText(chaveString, "Chave não pode ser null ou vazio.")
			Assert.hasText(valorString, "Valor de chave ${chaveString} não pode ser null ou vazio.")

			map.put(chaveString, valorString)
		}

		mensagem.bodyArgs = map
	}
	
	
	// Campo "para"
    void para(CharSequence destinatario) {
        Assert.notNull(destinatario, "Campo para não pode ser null.")
        para([destinatario])
    }

    void para(Object[] destinatarios) {
        Assert.notNull(recipiente, "Campo para não pode ser null.")
        para(destinatarios*.toString())
    }

    void para(List<? extends CharSequence> destinatarios) {
        mensagem.para = validarEConverterListaDeEmail('para', destinatarios)
    }

    private List<String> validarEConverterListaDeEmail(String campoNome, List<? extends CharSequence> destinatarios) {
        Assert.notNull(destinatarios, "Campo $campoNome não pode ser null.")
        Assert.notEmpty(destinatarios, "Campo $campoNome não pode ser vazio.")

        List<String> lista = new ArrayList<String>(destinatarios.size())
        destinatarios.each {CharSequence seq ->
            String email = seq.toString()
            assertEmail(email, campoNome)
            lista.add(email)
        }
        return lista
    }

    private assertEmail(String email, String campoNome) {
        Assert.notNull(email, "Campo $campoNome não pode ser null.")
        Assert.hasText(email, "Campo $campoNome não pode ser vazio.")
        if (!EmailValidator.caixaDeEmail(email)) {
            throw new GrailsMailException("Campo $campoNome precisa ser uma endereço de e-mail.")
        }
    }

    // Campo "bcc"
    void bcc(CharSequence valor) {
        Assert.notNull(valor, "Campo bcc não pode ser null.")
        bcc([valor])
    }

    void bcc(Object[] destinatarios) {
        Assert.notNull(destinatarios, "Campo bcc não pode ser null.")
        bcc(destinatarios*.toString())
    }

    void bcc(List<? extends CharSequence> destinatarios) {
        mensagem.bcc = validarEConverterListaDeEmail('bcc', destinatarios)
    }

    // Campo "cc"
    void cc(CharSequence valor) {
        Assert.notNull(valor, "Campo cc não pode ser null.")
        cc([valor])
    }

    void cc(Object[] destinatarios) {
        Assert.notNull(destinatarios, "Campo cc não pode ser null.")
        cc(destinatarios*.toString())
    }

    void cc(List<? extends CharSequence> destinatarios) {
        mensagem.cc = validarEConverterListaDeEmail('cc', destinatarios)
    }

    // Campo "replicarPara"
    void replicarPara(CharSequence valor) {
        def email = valor?.toString()
        assertEmail(email, 'replicarPara')
        mensagem.replicarPara = email
    }

    // Campo "de"
    void de(CharSequence remetente) {
        def email = remetente?.toString()
        assertEmail(email, 'de')
        mensagem.de = email
    }

    // Campo "assunto"
    void titulo(CharSequence assunto) {
        assunto(assunto)
    }

    void assunto(CharSequence assunto) {
        String assuntoString = assunto?.toString()
        Assert.hasText(assuntoString, "Campo assunto não pode ser null, ou vazio.")
        mensagem.assunto = assuntoString
    }

    // Corpo
    void corpo(CharSequence seq) {
        texto(seq)
    }

    void corpo(Map params) {
        Assert.notEmpty(params, "corpo não pode ser null, ou vazio")

        def render = doRender(params)

        if (render.html) {
            html(render.out.toString())
        } else {
            texto(render.out.toString())
        }
    }

    void texto(CharSequence seq) {
        mensagem.html = false
        def string = seq?.toString()
        Assert.hasText(string, "Corpo não pode ser null, ou vazio.")
        mensagem.texto = string
    }

    void texto(Map params) {
        texto(doRender(params).out.toString())
    }

    void html(CharSequence seq) {
        mensagem.html = true
        def string = seq?.toString()
        Assert.hasText(string, "Corpo não pode ser null, ou vazio.")
        mensagem.texto = string
    }

    void html(Map params) {
        html(doRender(params).out.toString())
    }

    protected MailMessageContentRenderer doRender(Map params) {
        if (conteudoMensagemEmailRenderer == null) {
            throw new GrailsMailException(
                    "mensagem de email builder foi construido sem o conteudo da mensagem render então não foi possivel renderizar as views"
            )
        }

        if (!params.view) {
            throw new GrailsMailException("nenhuma view especificada")
        }

        return conteudoMensagemEmailRenderer.render(new StringWriter(), params.view, params.model, locale, params.plugin)
    }

    void locale(String localeStr) {
        Assert.hasText(localeStr, "locale não pode ser null, ou vazio")

        locale(new Locale(localeStr.split('_', 3).toArrayString()))
    }

    void locale(Locale locale) {
        Assert.notNull(locale, "locale não pode ser null")

        this.locale = locale
    }

    // Anexos
    void anexarBytes(String nome, String tipoMime, byte[] conteudo) {
        Assert.hasText(nome, "O nome do anexo não pode ser vazio.")
        Assert.notNull(conteudo, "Conteudo do anexo não pode ser vazio.")

        if(!capazDeUtilizarMime){
            throw new GrailsMailException("Você deve usar um JavaMailSender para adicionar um anexo.")
        }
		
		if(!mensagem.anexos){
			mensagem.anexos = new ArrayList<AnexoEmailAssincrono>()
		}
		Integer idAnexo = mensagem.anexos.size()
        mensagem.anexos.add(
                new AnexoEmailAssincrono(
                        id: idAnexo, nomeAnexo: nome, tipoMime: tipoMime, conteudo: conteudo
                )
        )
    }

    void anexo(String nomeArquivo, String tipoConteudo, byte[] bytes) {
        anexarBytes(nomeArquivo, tipoConteudo, bytes)
    }

    void anexo(File arquivo) {
        anexo(arquivo.name, arquivo)
    }

    void anexo(String nomeArquivo, File arquivo) {
        anexo(nomeArquivo, tipoArquivoMap.getContentType(arquivo), arquivo)
    }

    void anexo(String nomeArquivo, String tipoConteudo, File arquivo) {
        if (!arquivo.exists()) {
            throw new FileNotFoundException("não pode usar $arquivo como um anexo, pois o mesmo existe")
        }

        anexo(nomeArquivo, tipoConteudo, new FileSystemResource(arquivo))
    }

    void anexo(String nomeArquivo, String tipoConteudo, InputStreamSource source) {
        InputStream stream = source.inputStream
        try {
            anexarBytes(nomeArquivo, tipoConteudo, stream.bytes)
        } finally {
            stream.close()
        }
    }

    void inline(String nome, String tipoMime, byte[] conteudo) {
        Assert.hasText(nome, "Id inline não pode ser vazio.")
        Assert.notNull(conteudo, "Conteudo inline não pode ser null.")

        if(!capazDeUtilizarMime){
            throw new GrailsMailException("Você deve usar um JavaMailSender para adicionar inlines.")
        }

		if(!mensagem.anexos){
			mensagem.anexos = new ArrayList<AnexoEmailAssincrono>()
		}
		Integer idAnexo = mensagem.anexos.size()
        mensagem.anexos.add(
                new AnexoEmailAssincrono(
                        id: idAnexo, nomeAnexo: nome, tipoMime: tipoMime, conteudo: conteudo
                )
        )
    }

    void inline(File arquivo) {
        inline(arquivo.name, arquivo)
    }

    void inline(String nomeArquivo, File arquivo) {
        inline(nomeArquivo, tipoArquivoMap.getContentType(arquivo), arquivo)
    }

    void inline(String conteudoId, String tipoConteudo, File arquivo) {
        if (!arquivo.exists()) {
            throw new FileNotFoundException("não é possivel utilizar o arquivo $arquivo como um anexo, pois o mesmo existe")
        }

        inline(conteudoId, tipoConteudo, new FileSystemResource(arquivo))
    }

    void inline(String conteudoId, String tipoConteudo, InputStreamSource source) {
        InputStream stream = source.inputStream
        try {
            inline(conteudoId, tipoConteudo, stream.bytes)
        } finally {
            stream.close()
        }
    }
	
}
