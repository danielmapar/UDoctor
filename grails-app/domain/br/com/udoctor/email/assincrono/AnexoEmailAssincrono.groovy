package br.com.udoctor.email.assincrono

class AnexoEmailAssincrono implements Serializable { 

	static mapWith = "mongo"
	
    static final MIME_TIPO_PADRAO = 'application/octet-stream'
	
    private static final TAMANHO_30_MB = 30*1024*1024
	
	Long     id
    String   nomeAnexo
    String   tipoMime = MIME_TIPO_PADRAO
    byte[]   conteudo
    boolean  inline = false

    static mapping = {
        version false
    }

    static constraints = {
        nomeAnexo(blank:false, nullable: false)
        tipoMime()
        conteudo(blank:false, nullable: false, maxSize:TAMANHO_30_MB)
    }
}




