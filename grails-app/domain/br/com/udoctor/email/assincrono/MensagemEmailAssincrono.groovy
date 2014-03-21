package br.com.udoctor.email.assincrono

import org.apache.commons.lang.StringUtils
import org.bson.types.ObjectId;

import br.com.udoctor.tipo.Enum.MensagemEmailType;
import br.com.udoctor.tipo.Enum.StatusMensagemEmailType;

class MensagemEmailAssincrono implements Serializable { 
    
	static mapWith = "mongo"
	
    private static final DATA_MAXIMA
    static {
        Calendar c = Calendar.getInstance()
        c.set(3000, 0, 1, 0, 0, 0)
        c.set(Calendar.MILLISECOND, 0)
        DATA_MAXIMA = c.getTime()
    }
	// Tamanho m�ximo e m�nimo do emai
	private static final int MAX_TAM_EMAIL = 80
	private static final int MIN_TAM_EMAIL = 7
	
	ObjectId id
	
	// Tipo da mensagem
	MensagemEmailType tipoDeMensagem = MensagemEmailType.GENERICO
	
    // !!! Campos da Mensagem !!!
    // Atributos do remetente
    String de
    String replicarPara

    // Atributos do destinatario
    List<String> para
    List<String> cc
    List<String> bcc

    // Headers adicionais
    Map<String, String> headers

    // Assunto e corpo da mensagem
    String assunto
    String texto
	Map<String, String> bodyArgs  // view, uri etc...
    boolean html = false

    // Anexos
    List<AnexoEmailAssincrono> anexos

    // !!! Campos de status adicionais !!!
    // Status da mensagem
    StatusMensagemEmailType status = StatusMensagemEmailType.CRIADO
	
	// Data quando a mensagem foi criada
    Date dataDeCriacao = new Date()

    // Data quando a mensagem foi enviado
    Date dataDeEnvio

    //Intervalo de envio
    Date dataDeInicioParaEnvio = new Date()
    Date dataDeExpiracao = DATA_MAXIMA
	
	// Prioridade. O maior � o primeiro.
    int prioridade = 0

    // Tentativas
    int contadorDeTentativas = 0
    int maximoNumeroDeTentativas = 1
    Date dataDaUltimaTentativa  
	
	// Intervalo min�mo entre tentativas em milisegundos
    long intervaloEntreTentativas = 300000l

	// Marcar a mensagem para ser deletada ap�s o envio
    boolean deletarDepoisDoEnvio = false

	static embedded = ['para', 'cc', 'bcc', 'headers', 'bodyArgs', 'anexos']    
	
	static mapping = {
		id index: true, indexAttributes: [unique:true, dropDups:true]
		compoundIndex dataDeInicioParaEnvio:1, dataDeExpiracao: 1, status: 1, tipoDeMensagem: 1
    }

    static constraints = {
		
		tipoDeMensagem()
        // campos de mensagem
        de(nullable: true, email: true, size: MIN_TAM_EMAIL..MAX_TAM_EMAIL)
        replicarPara(nullable: true, email: true, size: MIN_TAM_EMAIL..MAX_TAM_EMAIL)
		
		// validador para lista de emails
        def listaEmailValidator = { List<String> lista ->
            boolean flag = true
            if (lista != null) {
                lista.each {String email ->
                    if (StringUtils.isBlank(email) || !EmailValidator.caixaDeEmail(email) || 
						email.size() > MAX_TAM_EMAIL || email.size() < MIN_TAM_EMAIL) {
                        flag = false
                    }
                }
            }
            return flag
        }

        para(nullable: false, minSize: 1, validator: listaEmailValidator)
        cc(nullable: true, validator: listaEmailValidator)
        bcc(nullable: true, validator: listaEmailValidator)
		
		def mapValidator = {Map<String, String> map ->
            boolean flag = true
            map?.each {String chave, String valor ->
                if (StringUtils.isBlank(chave) || StringUtils.isBlank(valor)) {
                    flag = false
                }
            }
            return flag
        }
		
        headers(nullable: true, validator: mapValidator)
		bodyArgs(nullable: true, validator: mapValidator)

        assunto(blank: false, maxSize: 988)
        texto(blank: true, nullable: true, validator: {String texto, MensagemEmailAssincrono mensagemEmailAssincrono ->
            boolean flag = true
			if(!texto && !mensagemEmailAssincrono.bodyArgs){
				flag = false
			}
            return flag
        } )

        // Campos de status
        status()
        dataDeCriacao()
        dataDeEnvio(nullable: true)
        dataDeInicioParaEnvio()
        dataDeExpiracao(
			    validator: {Date valor, MensagemEmailAssincrono mensagem ->
                    valor && mensagem.dataDeInicioParaEnvio && valor.after(mensagem.dataDeInicioParaEnvio)
                }
        )

        // Campos tentativa
        contadorDeTentativas(min: 0)
        maximoNumeroDeTentativas(min: 1)
        dataDaUltimaTentativa(nullable: true)
        intervaloEntreTentativas(min: 0l)
    }

    @Override
    String toString() {
        StringBuilder builder = new StringBuilder()
        builder.append("Mensagem email ass�ncrono{")
        builder.append("id:$id")
        builder.append("assunto: $assunto")
        builder.append("para: ")
        para.eachWithIndex {String email, int index ->
            if (index != 0) {
                builder.append(',')
            }
            builder.append(email)
        }
        builder.append("status: $status}")
        return builder.toString()
    }
}