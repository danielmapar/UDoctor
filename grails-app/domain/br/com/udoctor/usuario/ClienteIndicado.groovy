package br.com.udoctor.usuario;

import org.bson.types.ObjectId;

class ClienteIndicado implements Serializable { 
	
	static mapWith = "mongo"
	
	Date      dataDeCriacao = new Date()
	ObjectId  id
	ObjectId  usuarioId
	String    nome
	String    email
	String    nomeAmigo
	String    emailAmigo
	
	static constraints = {
		dataDeCriacao()
		usuarioId(blank: true, nullable: true, 
			validator: { ObjectId usuarioId, ClienteIndicado clienteIndicado ->
			if((!usuarioId || usuarioId == "") && (!clienteIndicado.nome  || clienteIndicado.nome == "" ||
							  					   !clienteIndicado.email || clienteIndicado.email == "" )){
				return 'clienteIndicado.usuarioId.error.vazio'
			}
		})
		nome(blank: true, nullable: true)
		email(blank: true, nullable: true, email: true)
		nomeAmigo(blank: false, nullable: false)
		emailAmigo(blank:false, nullable:false, email: true)  
	}
	static mapping = {
		id index: true, indexAttributes: [unique:true, dropDups:true]
		compoundIndex usuarioId:1, nome:1, amigo: 1, nomeAmigo: 1, emailAmigo: 1
	}
}
