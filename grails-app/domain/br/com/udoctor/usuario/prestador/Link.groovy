package br.com.udoctor.usuario.prestador;

class Link implements Serializable { 

	static mapWith = "mongo"

	Long     id
	String   nome
	String   descricaoLink
	
	static constraints = {
		id(blank: false, nullable: false)
		nome(blank: true, nullable: true, url: true, size: 8..100, validator: { String nome, Link link ->
			if (((nome == null || link.descricaoLink == null) 
				&& (link.id > 0)) ||
			   (((nome != null && nome != "" && (link.descricaoLink == null || link.descricaoLink == "")) ||
				 ((nome == null || nome =="") && link.descricaoLink != null && link.descricaoLink != ""))
				&& (link.id == 0))) {
				return 'link.nome.error.vazio'
			}  
		})
		descricaoLink(blank: true, nullable: true, size: 3..100)
	}
	
	static mapping = {
	}
}