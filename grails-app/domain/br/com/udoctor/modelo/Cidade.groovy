package br.com.udoctor.modelo;

import br.com.udoctor.tipo.Enum.StatusType;

class Cidade implements Serializable { 

	static mapWith = "mongo"
	
	Long       id
	StatusType status
	String     nome
	
	static constraints = {
		id(blank: false, nullable: false)
		status(blank: false, nullable: false, inList:StatusType.lista(), minSize:1, maxSize:1)
		nome(blank: false, nullable: false, size: 2..40)
	}
	
	static mapping = {
	}
}
