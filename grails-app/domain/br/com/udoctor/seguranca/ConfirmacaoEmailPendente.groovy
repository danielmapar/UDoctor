package br.com.udoctor.seguranca

import org.bson.types.ObjectId;

class ConfirmacaoEmailPendente implements Serializable { 
	
	static mapWith = "mongo"
	
	ObjectId id
	Date     dataDeCriacao = new Date()
	String   email
	ObjectId usuarioId
	String   tokenDeConfirmacao = "?"

    static mapping = {
		id index: true, indexAttributes: [unique:true, dropDups:true]
		dataDeCriacao index:true, indexAttributes: [unique:true, dropDups:true]
		email index:true, indexAttributes: [unique:true, dropDups:true]
		usuarioId index:true, indexAttributes: [unique:true, dropDups:true]
		tokenDeConfirmacao index:true, indexAttributes: [unique:true, dropDups:true]
		version false
    }
    
	static constraints = {
		dataDeCriacao()
	    email(blank: false, nullable: false, email:true, unique: true, size:7..80)
		usuarioId(blank: false, nullable: false)
		tokenDeConfirmacao(blank: false, nullable: false, unique:true, size:1..80)
	}
}

