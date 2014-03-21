package br.com.udoctor.seguranca

import org.bson.types.ObjectId;

class EsqueciMinhaSenha implements Serializable { 

	static mapWith = "mongo"
	
	ObjectId id
	Date     dataDeCriacao = new Date()
	String   email
	String   token 

	static mapping = {
		id index:true, indexAttributes: [unique:true, dropDups:true]
		email index:true, indexAttributes: [unique:true, dropDups:true]
		token index:true, indexAttributes: [unique:true, dropDups:true]
		dataDeCriacao index:true, indexAttributes: [unique:true, dropDups:true]
		version false
	}
	
	static constraints = {
		dataDeCriacao()
		email(blank: false, nullable: false, email:true, unique: true, size:7..80)
		token(blank: false, nullable: false, unique:true, size:1..80)
	}
}
