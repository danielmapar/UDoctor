package br.com.udoctor.usuario.prestador;

class Telefone implements Serializable { 

	static mapWith = "mongo"

	Long     id
	String 	 prefixo
	String 	 numero
	String   numeroCompleto
	
    static constraints = {
		id(blank: false, nullable: false)
		prefixo(blank: true, nullable: true, minSize: 2, maxSize: 2)
		numero(blank: true, nullable: true, minSize: 8,  maxSize: 8)
		numeroCompleto (blank:false, nullable: false, minSize:14, maxSize:14)
    }
	
	static mapping = {
	}
	
}
