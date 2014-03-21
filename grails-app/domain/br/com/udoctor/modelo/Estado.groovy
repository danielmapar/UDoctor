package br.com.udoctor.modelo;

import org.bson.types.ObjectId;

import br.com.udoctor.tipo.Enum.StatusType;

class Estado implements Serializable { 
	
	static mapWith = "mongo"
	
	ObjectId     id
	StatusType 	 status
    String     	 nome
	List<Cidade> cidades
	
	static embedded = ['cidades']
	
	static constraints = {   
		status(blank: false, nullable: false, inList:StatusType.lista(), minSize:1, maxSize:1)
		nome(blank: false, nullable: false, size: 2..40)
		cidades(blank: true, nullable: true)
	}    
	
	static mapping = {
		cache: "read-write"
		id index: true, indexAttributes: [unique:true, dropDups:true]
		nome index: true, indexAttributes: [unique:true, dropDups:true]
	}
}
