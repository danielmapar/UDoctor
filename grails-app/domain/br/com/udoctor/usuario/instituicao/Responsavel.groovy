package br.com.udoctor.usuario.instituicao;

import org.bson.types.ObjectId;

import br.com.udoctor.tipo.Enum.GeneroType;

class Responsavel implements Serializable { 

	static mapWith = "mongo"

	static transients = ["nomeProfissao"]
	String nomeProfissao
	
	Long          id
	String        nome
	String        sobrenome
	GeneroType    genero
	ObjectId      profissao
	String        codigoConselhoRegional
	
	static constraints = {
		id(blank: false, nullable: false)
		nome(blank: false, nullable: false, size: 2..20)
		sobrenome(blank: false, nullable: false, size: 2..50)
		genero(blank:false, nullable: false, inList:GeneroType.lista(), minSize:1, maxSize:1)
		profissao(blank:false, nullable: false)
		codigoConselhoRegional(blank: false, nullable: false, minSize: 9, maxSize: 9)
	}
	
	static mapping = {
	}
}
