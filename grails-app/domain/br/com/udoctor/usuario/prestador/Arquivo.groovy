package br.com.udoctor.usuario.prestador

import br.com.udoctor.tipo.Enum.ArquivoType

class Arquivo implements Serializable {

    static mapWith = "mongo"

    Long        id
    ArquivoType tipoArquivo
	String      nomeOriginal
	String      nome
    String      nomeThumbnail
	Integer     tamanhoOriginal
    String      extensao
    String      detalhes

	
    static constraints = {
        id(blank: false, nullable: false)
        tipoArquivo(nullable: false, blank: false)
        nomeOriginal(nullable: false, blank: false)
        nome(nullable: false, blank: false)
        nomeThumbnail(nullable: true, blank: true)
        tamanhoOriginal(nullable: false, blank: false)
        extensao(nullable: false, blank: false)
        detalhes(nullable: false, )
    }
}
